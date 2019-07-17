package com.liferay.portal.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.UserEmailAddressException;
import com.liferay.portal.UserPasswordException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.ListType;
import com.liferay.portal.model.ListTypeConstants;
import com.liferay.portal.model.PasswordPolicy;
import com.liferay.portal.model.Ticket;
import com.liferay.portal.model.TicketConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.ldap.LDAPSettingsUtil;
import com.liferay.portal.security.pwd.PasswordEncryptorUtil;
import com.liferay.portal.security.pwd.PwdToolkitUtil;
import com.liferay.portal.security.pwd.RegExpToolkit;
import com.liferay.portal.service.ListTypeServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portlet.admin.util.OmniadminUtil;
import com.orange.oab.lmde.service.exception.LmdeServiceEnvoiEmailException;
import com.orange.oab.lmde.service.service.EnvoiEmailServiceUtil;
import com.orange.oab.lmde.util.constant.LMDEConstants;
import com.orange.oab.lmde.util.portal.LmdePortalProperties;
import com.orange.oab.lmde.util.role.RoleUtil;
import com.orange.oab.lmde.util.threadlocal.InitCompanyThreadLocal;
import com.orange.oab.lmde.ws.authentification.exception.LmdeWebServiceAuthentificationException;
import com.orange.oab.lmde.ws.authentification.util.CompteUtil;
import com.orange.oab.lmde.ws.personne.exception.LmdeWebServicePersonneException;
import com.orange.oab.lmde.ws.personne.service.PersonneClientWebServiceUtil;

import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.AdresseEmailOE;
import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.CoordonneeOE;
import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.EmailOE;
import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.RefCoordonneeOE;
import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.TypeCoordonneeOE;
import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.TypeUsageCoordonneeOE;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.ConsulterPersonneResponseParameter;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.PersonneOE;

/**
 * Surcharge du Service UserLocalServiceImpl et notamment la mise à jour du mot de passe.
 * 
 * @author Anthony Baudouin (gvxg2439)
 */
public class LMDEUserLocalServiceImpl extends UserLocalServiceImpl {

    /*
     * (non-Javadoc)
     * @see com.liferay.portal.service.impl.UserLocalServiceImpl#updatePassword(long, java.lang.String,
     * java.lang.String, boolean, boolean)
     */
    @Override
    public User updatePassword(long userId, String password1, String password2, boolean passwordReset, boolean silentUpdate) throws PortalException,
            SystemException {
        User user = userPersistence.findByPrimaryKey(userId);

        if (!silentUpdate) {
            validatePassword(user.getCompanyId(), userId, password1, password2);
        }

        String oldEncPwd = user.getPassword();

        if (!user.isPasswordEncrypted()) {
            oldEncPwd = PasswordEncryptorUtil.encrypt(user.getPassword());
        }

        String newEncPwd = PasswordEncryptorUtil.encrypt(password1);

        if (user.hasCompanyMx()) {
            mailService.updatePassword(user.getCompanyId(), userId, password1);
        }

        // DEBUT Custom : Mot de passe sauvé dans Liferay que si l'utilisateur est un utilisateur interne
        if (OmniadminUtil.isOmniadmin(userId) || UserLocalServiceUtil.hasRoleUser(user.getCompanyId(), RoleUtil.ROLE_INTERNE_LMDE_NOM, userId, false)) {
            user.setPassword(newEncPwd);
            user.setPasswordUnencrypted(password1);
        } else {
            // Stockage dans le SI LMDE
            try {
                CompteUtil.mettreAJourMotDePasseOubliMotDePasse(user.getEmailAddress(), password1);
            } catch (LmdeWebServiceAuthentificationException e) {
                throw new UserPasswordException(UserPasswordException.PASSWORD_INVALID);
            }
        }
        // FIN CUSTOM
        user.setPasswordEncrypted(true);
        user.setPasswordReset(passwordReset);
        user.setPasswordModifiedDate(new Date());
        user.setDigest(StringPool.BLANK);
        user.setGraceLoginCount(0);

        if (!silentUpdate) {
            user.setPasswordModified(true);
        }

        try {
            userPersistence.update(user);
        } catch (ModelListenerException mle) {
            String msg = GetterUtil.getString(mle.getCause().getMessage());

            if (LDAPSettingsUtil.isPasswordPolicyEnabled(user.getCompanyId())) {
                String passwordHistory = PrefsPropsUtil.getString(user.getCompanyId(), PropsKeys.LDAP_ERROR_PASSWORD_HISTORY);

                if (msg.contains(passwordHistory)) {
                    throw new UserPasswordException(UserPasswordException.PASSWORD_ALREADY_USED);
                }
            }

            throw new UserPasswordException(UserPasswordException.PASSWORD_INVALID);
        }

        if (!silentUpdate) {
            user.setPasswordModified(false);

            passwordTrackerLocalService.trackPassword(userId, oldEncPwd);
        }

        return user;
    }

    @Override
    public User addUserWithWorkflow(long creatorUserId, long companyId, boolean autoPassword, String password1, String password2, boolean autoScreenName,
            String screenName, String emailAddress, long facebookId, String openId, Locale locale, String firstName, String middleName, String lastName,
            int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, String jobTitle, long[] groupIds,
            long[] organizationIds, long[] roleIds, long[] userGroupIds, boolean sendEmail, ServiceContext serviceContext) throws PortalException,
            SystemException {

        User user = super.addUserWithWorkflow(creatorUserId, companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress,
                facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle,
                groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

        final boolean isCreationCompteLMDE = ParamUtil.getBoolean(serviceContext, LMDEConstants.CREATION_COMPTE_LMDE, false);

        // we don't execute custom code (update email address), when we are inside init company phase.
        if ((InitCompanyThreadLocal.get() == null || !InitCompanyThreadLocal.get()) && isCreationCompteLMDE) {
            // Bascule compte : Appelle WS pour MAJ l'adresse mail, que si l'ancien mail n'est pas identique
            try {

                LOGGER.debug("Appelle WS [consulterPersonneAvecCache]");
                ConsulterPersonneResponseParameter personneResponse = PersonneClientWebServiceUtil.consulterPersonneAvecCache(screenName);
                PersonneOE personne = personneResponse.getPersonne();

                /*
                 * needUpdating :
                 * null ==> rien à faire
                 * true ==> MAJ
                 * false ==> création
                 */
                Boolean needUpdating = null;
                List<CoordonneeOE> coordonnees = personne.getCoordonnees();
                for (CoordonneeOE coordonnee : coordonnees) {
                    if (coordonnee.getRefCoordonnee() != null && TypeCoordonneeOE.EMAIL_PERSONNEL.equals(coordonnee.getRefCoordonnee().getType())) {
                        // Mise à jour de l'email avec la nouvelle valeur
                        if (!emailAddress.equals(coordonnee.getEmail().getValeur().getValeur())) {
                            coordonnee.getEmail().getValeur().setValeur(emailAddress);
                            needUpdating = true;
                        } else {
                            needUpdating = false;
                        }
                        break;
                    }
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("La valeur du variable [needUpdating] : %s", needUpdating));
                }

                // Si la coordonnee 'EMAIL PERSONNEL' n'a pas été trouvée, on l'ajoute
                if (Boolean.FALSE.equals(needUpdating)) {
                    CoordonneeOE coordonnee = new CoordonneeOE();
                    RefCoordonneeOE refCoordonnee = new RefCoordonneeOE();
                    refCoordonnee.setType(TypeCoordonneeOE.EMAIL_PERSONNEL);
                    coordonnee.setRefCoordonnee(refCoordonnee);

                    EmailOE emailOE = new EmailOE();
                    AdresseEmailOE adresseEmailOE = new AdresseEmailOE();
                    adresseEmailOE.setValeur(emailAddress);

                    emailOE.setValeur(adresseEmailOE);
                    emailOE.setQualite(1);
                    coordonnee.setEmail(emailOE);

                    coordonnee.getUsageCoordonnees().add(TypeUsageCoordonneeOE.CRM);
                    coordonnee.getUsageCoordonnees().add(TypeUsageCoordonneeOE.COMMERCE);

                    personne.getCoordonnees().add(coordonnee);
                }

                LOGGER.debug("Appelle WS [updatePersonne]");
                PersonneClientWebServiceUtil.updatePersonne(personne);

            } catch (LmdeWebServicePersonneException e) {
                throw new SystemException("Erreur lors de la mise à jour du mail de la personne", e);
            }
        }

        return user;
    }

    @Override
    public void sendPassword(long companyId, String emailAddress, String fromName, String fromAddress, String subject, String body,
            ServiceContext serviceContext) throws PortalException, SystemException {

        Company company = companyPersistence.findByPrimaryKey(companyId);

        if (!company.isSendPassword() && !company.isSendPasswordResetLink()) {
            return;
        }

        emailAddress = StringUtil.toLowerCase(emailAddress.trim());

        if (Validator.isNull(emailAddress)) {
            throw new UserEmailAddressException();
        }

        // CUSTOM begin
        User user = null;
        if (serviceContext.getAttribute("LMDE_user") != null) {
            user = (User) serviceContext.getAttribute("LMDE_user");
        } else {
            user = userPersistence.findByC_EA(companyId, emailAddress);
        }
        // CUSTOM end

        PasswordPolicy passwordPolicy = user.getPasswordPolicy();

        String newPassword = StringPool.BLANK;
        String passwordResetURL = StringPool.BLANK;

        if (company.isSendPasswordResetLink()) {
            Date expirationDate = null;

            if ((passwordPolicy != null) && (passwordPolicy.getResetTicketMaxAge() > 0)) {

                expirationDate = new Date(System.currentTimeMillis() + (passwordPolicy.getResetTicketMaxAge() * 1000));
            }

            Ticket ticket = ticketLocalService.addTicket(companyId, User.class.getName(), user.getUserId(), TicketConstants.TYPE_PASSWORD, null,
                    expirationDate, serviceContext);

            passwordResetURL = serviceContext.getPortalURL() + serviceContext.getPathMain() + "/portal/update_password?p_l_id=" + serviceContext.getPlid()
                    + "&ticketKey=" + ticket.getKey();
        } else {
            if (!PasswordEncryptorUtil.PASSWORDS_ENCRYPTION_ALGORITHM.equals(PasswordEncryptorUtil.TYPE_NONE)) {

                if (LDAPSettingsUtil.isPasswordPolicyEnabled(user.getCompanyId())) {

                    if (LOGGER.isWarnEnabled()) {
                        StringBundler sb = new StringBundler(5);

                        sb.append("When LDAP password policy is enabled, ");
                        sb.append("it is possible that portal generated ");
                        sb.append("passwords will not match the LDAP policy.");
                        sb.append("Using RegExpToolkit to generate new ");
                        sb.append("password.");

                        LOGGER.warn(sb.toString());
                    }

                    RegExpToolkit regExpToolkit = new RegExpToolkit();

                    newPassword = regExpToolkit.generate(null);
                } else {
                    newPassword = PwdToolkitUtil.generate(passwordPolicy);
                }

                boolean passwordReset = false;

                if (passwordPolicy.getChangeable() && passwordPolicy.getChangeRequired()) {

                    passwordReset = true;
                }

                user.setPassword(PasswordEncryptorUtil.encrypt(newPassword));
                user.setPasswordUnencrypted(newPassword);
                user.setPasswordEncrypted(true);
                user.setPasswordReset(passwordReset);
                user.setPasswordModified(true);
                user.setPasswordModifiedDate(new Date());

                userPersistence.update(user);

                user.setPasswordModified(false);
            } else {
                newPassword = user.getPassword();
            }
        }

        if (Validator.isNull(fromName)) {
            fromName = PrefsPropsUtil.getString(companyId, PropsKeys.ADMIN_EMAIL_FROM_NAME);
        }

        if (Validator.isNull(fromAddress)) {
            fromAddress = PrefsPropsUtil.getString(companyId, PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
        }

        // CUSTOM begin - utiliser le service mail LMDE

        StringBuilder prefix = new StringBuilder(StringPool.BLANK);
        Contact contact = user.getContact();
        if (contact != null) {
            int prefixId = contact.getPrefixId();
            if (prefixId != 0) {
                List<ListType> listTypes = ListTypeServiceUtil.getListTypes(ListTypeConstants.CONTACT_PREFIX);
                for (ListType listType : listTypes) {
                    if (listType.getListTypeId() == prefixId) {
                        prefix.append(LanguageUtil.get(serviceContext.getLocale(), "lmde-" + listType.getName(), StringPool.BLANK));
                        if (prefix.length() > 0) {
                            prefix.append(StringPool.SPACE);
                        }
                    }
                }
            }
        }
        String toName = prefix.toString() + user.getFullName();
        String toAddress = null;
        // LMDE-752 : Les emails envoyés depuis MyLMDE doivent tous être envoyés vers l'email utilisé comme login de connexion
        // toAddress = user.getEmailAddress();
        if (serviceContext.getAttribute("LMDE_user") != null) {
            toAddress = emailAddress;
        } else {
            toAddress = user.getEmailAddress();
        }

        /**
         * LMDE 711 - Récupération de mot de passe - Envoi de mails - Utiliser le web service envoiEmail
         *
         if (Validator.isNull(subject)) {
         * if (company.isSendPasswordResetLink()) {
         * subject = PrefsPropsUtil.getContent(companyId, PropsKeys.ADMIN_EMAIL_PASSWORD_RESET_SUBJECT);
         * } else {
         * subject = PrefsPropsUtil.getContent(companyId, PropsKeys.ADMIN_EMAIL_PASSWORD_SENT_SUBJECT);
         * }
         * }
         * if (Validator.isNull(body)) {
         * if (company.isSendPasswordResetLink()) {
         * body = PrefsPropsUtil.getContent(companyId, PropsKeys.ADMIN_EMAIL_PASSWORD_RESET_BODY);
         * } else {
         * body = PrefsPropsUtil.getContent(companyId, PropsKeys.ADMIN_EMAIL_PASSWORD_SENT_BODY);
         * }
         * }
         */

        Map<String, String> emailValues = new HashMap<String, String>();
        emailValues.put("PASSWORD_RESET_URL", passwordResetURL);
        emailValues.put("TO_NAME", toName);
        emailValues.put("LMDE_LINK_FACEBOOK", LmdePortalProperties.LMDE_EXTERNAL_PAGE_FACEBOOK);
        emailValues.put("LMDE_LINK_TWITTER", LmdePortalProperties.LMDE_EXTERNAL_PAGE_TWITTER);
        emailValues.put("LMDE_LINK_GOOGLE", LmdePortalProperties.LMDE_EXTERNAL_PAGE_GOOGLEPLUS);

        try {
            EnvoiEmailServiceUtil.envoiEmail(toAddress, "EMAIL_LMDE_PASSWORD_RESET.html", "My LMDE - Votre réinitialisation de mot de passe", emailValues);
        } catch (LmdeServiceEnvoiEmailException e) {
            LOGGER.error("Une erreur est survenue lors de l'envoi d'e-mail de réinialisation du mot de passe à l'utilisateur " + user.getEmailAddress(), e);
            throw new PortalException(e);
        }

        /**
         * SubscriptionSender subscriptionSender = new SubscriptionSender();
         * subscriptionSender.setBody(body);
         * subscriptionSender.setCompanyId(companyId);
         * // CUSTOM begin : ajouter les liens socicaux, id de connexion
         * subscriptionSender.setContextAttributes("[$PASSWORD_RESET_URL$]", passwordResetURL, "[$REMOTE_ADDRESS$]",
         * serviceContext.getRemoteAddr(),
         * "[$REMOTE_HOST$]", serviceContext.getRemoteHost(), "[$USER_ID$]", user.getUserId(), "[$USER_PASSWORD$]",
         * newPassword, "[$USER_SCREENNAME$]",
         * user.getScreenName(), "[$LMDE_LINK_FACEBOOK$]", LmdePortalProperties.LMDE_EXTERNAL_PAGE_FACEBOOK,
         * "[$LMDE_LINK_TWITTER$]",
         * LmdePortalProperties.LMDE_EXTERNAL_PAGE_TWITTER, "[$LMDE_LINK_GOOGLE$]",
         * LmdePortalProperties.LMDE_EXTERNAL_PAGE_GOOGLEPLUS,
         * "[$LMDE_LINK_YOUTUBE$]", LmdePortalProperties.LMDE_EXTERNAL_PAGE_YOUTUBE, "[$LMDE_LINK_INSTAGRAM$]",
         * LmdePortalProperties.LMDE_EXTERNAL_PAGE_INSTAGRAM, "[$LMDE_ID$]", user.getEmailAddress());
         * // CUSTOM end
         * subscriptionSender.setFrom(fromAddress, fromName);
         * subscriptionSender.setHtmlFormat(true);
         * subscriptionSender.setMailId("user", user.getUserId(), System.currentTimeMillis(),
         * PwdGenerator.getPassword());
         * subscriptionSender.setServiceContext(serviceContext);
         * subscriptionSender.setSubject(subject);
         * subscriptionSender.setUserId(user.getUserId());
         * subscriptionSender.addRuntimeSubscribers(toAddress, LmdePortalUtil.upperCaseAllFirst(toName));
         * subscriptionSender.flushNotificationsAsync();
         */
        // CUSTOM end
    }

    /**
     * Logger de la classe.
     */
    private static final Log LOGGER = LogFactoryUtil.getLog(LMDEUserLocalServiceImpl.class);
}
