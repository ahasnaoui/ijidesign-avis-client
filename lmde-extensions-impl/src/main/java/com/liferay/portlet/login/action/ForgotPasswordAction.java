/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.login.action;

import java.rmi.dgc.Lease;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.RequiredReminderQueryException;
import com.liferay.portal.SendPasswordException;
import com.liferay.portal.UserActiveException;
import com.liferay.portal.UserEmailAddressException;
import com.liferay.portal.UserLockoutException;
import com.liferay.portal.UserReminderQueryException;
import com.liferay.portal.kernel.captcha.CaptchaException;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.login.util.LoginUtil;
import com.orange.oab.lmde.service.exception.LmdeServiceEspaceAdherentException;
import com.orange.oab.lmde.service.service.EspaceAdherentServiceUtil;
import com.orange.oab.lmde.util.validator.ValidatorUtil;
import com.orange.oab.lmde.ws.authentification.exception.LmdeWebServiceAuthentificationException;
import com.orange.oab.lmde.ws.authentification.service.AuthentificationClientWebServiceUtil;
import com.orange.oab.lmde.ws.contrat.exception.LmdeWebServiceContratException;
import com.orange.oab.lmde.ws.personne.exception.LmdeWebServicePersonneException;
import com.orange.oab.lmde.ws.personne.service.PersonneClientWebServiceUtil;

import fr.interiale.si.fonctionsgeneriques.securite.authentification.objets.v1.ConsulterCompteResponseParameter;
import fr.interiale.si.fonctionsgeneriques.securite.authentification.objets.v1.TypeApplicationOE;
import fr.interiale.si.gisements.contrats.contratsac.objets.v1.BeneficiaireOE;
import fr.interiale.si.gisements.contrats.contratsac.objets.v1.ContratOE;
import fr.interiale.si.gisements.contrats.contratsac.objets.v1.GarantieOE;
import fr.interiale.si.gisements.contrats.contratsac.objets.v1.TypeLienFamilialOE;
import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.CoordonneeOE;
import fr.interiale.si.gisements.personnes.coordonneesmediatiques.objets.v1.TypeCoordonneeOE;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.ConsulterPersonneResponseParameter;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.PersonneOE;

/**
 * @author Brian Wing Shun Chan
 * @author Tibor Kovacs
 */
public class ForgotPasswordAction extends PortletAction {

    static final Log LOGGER = LogFactoryUtil.getLog(ForgotPasswordAction.class);

    private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;

    private static final String MESSAGE_EMAIL_NON_TROUVE = "your-email-does-not-exist-in-our-database";

    @Override
    public void processAction(ActionMapping actionMapping, ActionForm actionForm, PortletConfig portletConfig, ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        Company company = themeDisplay.getCompany();

        if (!company.isSendPassword() && !company.isSendPasswordResetLink()) {
            throw new PrincipalException();
        }

        try {
            if (PropsValues.USERS_REMINDER_QUERIES_ENABLED) {
                checkReminderQueries(actionRequest, actionResponse);
            } else {
                checkCaptcha(actionRequest);

                //sendPassword(actionRequest, actionResponse);
                
                // LMDE-749 : Mot de passe temporaire
                sendTemporaryPassword(actionRequest, actionResponse);

                // CUSTOM begin
                SessionMessages.add(actionRequest, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
                PortalUtil.getHttpServletRequest(actionRequest).getSession().setAttribute("COMPANY_envoyer_reinitialiser_mdp", true);
                PortalUtil.getHttpServletRequest(actionRequest).getSession()
                        .setAttribute("COMPANY_envoyer_reinitialiser_mdp_email", getUser(actionRequest).getEmailAddress());
                // CUSTOM end
                
                sendRedirect(actionRequest, actionResponse);
            }
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Une erreur lors de l'oubli de mot de passe est survenue ");
            }
            if (e instanceof CaptchaTextException || e instanceof UserEmailAddressException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
            } else if (e instanceof LmdeWebServiceAuthentificationException) {
                SessionErrors.add(actionRequest, "authentificationWSerror");
            } else if (e instanceof LmdeServiceEspaceAdherentException) {
                    SessionErrors.add(actionRequest, "espaceAdherentWSerror");
            } else if (e instanceof LmdeWebServiceContratException) {
                SessionErrors.add(actionRequest, "contratWSerror");
            } else if (e instanceof LmdeWebServicePersonneException) {
                SessionErrors.add(actionRequest, "personneWSerror");
            } else if (e instanceof NoSuchUserException && e.getMessage() != null && e.getMessage().equals(MESSAGE_EMAIL_NON_TROUVE)) {
                SessionErrors.add(actionRequest, MESSAGE_EMAIL_NON_TROUVE);
            } else if (e instanceof NoSuchUserException || e instanceof RequiredReminderQueryException || e instanceof SendPasswordException
                    || e instanceof UserActiveException || e instanceof UserLockoutException || e instanceof UserReminderQueryException) {

                if (PropsValues.LOGIN_SECURE_FORGOT_PASSWORD) {
                    sendRedirect(actionRequest, actionResponse);
                } else {
                    SessionErrors.add(actionRequest, e.getClass());
                }
            } else {
                SessionErrors.add(actionRequest, e.getClass());
            }
        }
    }

    @Override
    public ActionForward render(ActionMapping actionMapping, ActionForm actionForm, PortletConfig portletConfig, RenderRequest renderRequest,
            RenderResponse renderResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        Company company = themeDisplay.getCompany();

        if (!company.isSendPassword() && !company.isSendPasswordResetLink()) {
            return actionMapping.findForward("portlet.login.login");
        }

        renderResponse.setTitle(themeDisplay.translate("forgot-password"));

        return actionMapping.findForward("portlet.login.forgot_password");
    }

    protected void checkCaptcha(ActionRequest actionRequest) throws CaptchaException {

        if (PropsValues.CAPTCHA_CHECK_PORTAL_SEND_PASSWORD) {
            CaptchaUtil.check(actionRequest);
        }
    }

    protected void checkReminderQueries(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        PortletSession portletSession = actionRequest.getPortletSession();

        int step = ParamUtil.getInteger(actionRequest, "step");

        if (step == 1) {
            checkCaptcha(actionRequest);

            portletSession.removeAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_ATTEMPTS);
            portletSession.removeAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_USER_EMAIL_ADDRESS);
        }

        User user = getUser(actionRequest);

        portletSession.setAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_USER_EMAIL_ADDRESS, user.getEmailAddress());

        actionRequest.setAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_USER, user);

        if (step == 2) {
            Integer reminderAttempts = (Integer) portletSession.getAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_ATTEMPTS);

            if (reminderAttempts == null) {
                reminderAttempts = 0;
            } else if (reminderAttempts > 2) {
                checkCaptcha(actionRequest);
            }

            reminderAttempts++;

            portletSession.setAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_ATTEMPTS, reminderAttempts);

            sendPassword(actionRequest, actionResponse);
        }
    }

    /**
     * Retrouve l'utilisateur par
     * <ul>
     * <li>addresse e-mail</li>
     * <li>numéro de sécurité sociale</li>
     * <li>numéro d'adhérent</li>
     * </ul>
     * 
     * @param actionRequest
     * @return l'utilisaeeur
     * @throws Exception
     */
    protected User getUser(ActionRequest actionRequest) throws Exception {
        PortletSession portletSession = actionRequest.getPortletSession();

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        String sessionEmailAddress = (String) portletSession.getAttribute(WebKeys.FORGOT_PASSWORD_REMINDER_USER_EMAIL_ADDRESS);

        User user = null;

        // si le mail est stocké en session alors l'utilisateur est directement retrouvé
        if (Validator.isNotNull(sessionEmailAddress)) {
            user = UserLocalServiceUtil.getUserByEmailAddress(themeDisplay.getCompanyId(), sessionEmailAddress);
        } else {
            long userId = ParamUtil.getLong(actionRequest, "userId");
            String screenName = ParamUtil.getString(actionRequest, "screenName");
            String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
            // CUSTOM begin
            if (Validator.isNotNull(emailAddress) && ValidatorUtil.validateEmail(emailAddress)) {
                ConsulterCompteResponseParameter consulterCompte = AuthentificationClientWebServiceUtil.consulterCompte(emailAddress);
                if (consulterCompte == null || consulterCompte.getCompte() == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Aucun compte trouvée");
                    }
                    throw new NoSuchUserException(MESSAGE_EMAIL_NON_TROUVE);
                }
                // liferay login (email)
                String login = consulterCompte.getCompte().getLogin();
                user = UserLocalServiceUtil.getUserByEmailAddress(themeDisplay.getCompanyId(), login);
                // CUSTOM end
            } else if (Validator.isNotNull(screenName)) {
                user = UserLocalServiceUtil.getUserByScreenName(themeDisplay.getCompanyId(), screenName);
            } else if (userId > 0) {
                user = UserLocalServiceUtil.getUserById(userId);
            } else {
                throw new NoSuchUserException();
            }
        }

        if (!user.isActive()) {
            throw new UserActiveException();
        }

        if (user.isLockout()) {
            throw new UserLockoutException();
        }

        return user;
    }

    @Override
    protected boolean isCheckMethodOnProcessAction() {
        return _CHECK_METHOD_ON_PROCESS_ACTION;
    }
    
    /**
     * Retourne l'adresse email de l'utilsateur si l'envoi du mot de passe temporaire a fonctionné.
     * 
     * @param actionRequest
     * @param actionResponse
     * @throws Exception
     */
    protected void sendTemporaryPassword(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        final User user = getUser(actionRequest);
        if (user != null) {
	        boolean fait = EspaceAdherentServiceUtil.regenererMotDePasseOublie(user.getEmailAddress(), TypeApplicationOE.WEB_LMDE.value());
	        if (!fait) {
	        	throw new LmdeServiceEspaceAdherentException();
	        }
	    } else {
	    	throw new NoSuchUserException();
	    }
    }

    protected void sendPassword(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        Company company = themeDisplay.getCompany();

        User user = getUser(actionRequest);

        if (PropsValues.USERS_REMINDER_QUERIES_ENABLED) {
            if (PropsValues.USERS_REMINDER_QUERIES_REQUIRED && !user.hasReminderQuery()) {

                throw new RequiredReminderQueryException("No reminder query or answer is defined for user " + user.getUserId());
            }

            String answer = ParamUtil.getString(actionRequest, "answer");

            if (!user.getReminderQueryAnswer().equals(answer)) {
                throw new UserReminderQueryException();
            }
        }

        PortletPreferences portletPreferences = actionRequest.getPreferences();

        String languageId = LanguageUtil.getLanguageId(actionRequest);

        String emailFromName = portletPreferences.getValue("emailFromName", null);
        String emailFromAddress = portletPreferences.getValue("emailFromAddress", null);

        // CUSTOM begin
        String emailToAddress = null;
        final String lmdeToAddress = (String) actionRequest.getAttribute("LMDE_toAddress");
        if (null != lmdeToAddress && !"".equals(lmdeToAddress)) {
            emailToAddress = lmdeToAddress;
        } else {
            emailToAddress = user.getEmailAddress();
        }

        actionRequest.setAttribute("LMDE_user", user);

        // CUSTOM end

        String emailParam = "emailPasswordSent";

        if (company.isSendPasswordResetLink()) {
            emailParam = "emailPasswordReset";
        }

        String subject = portletPreferences.getValue(emailParam + "Subject_" + languageId, null);
        String body = portletPreferences.getValue(emailParam + "Body_" + languageId, null);

        LoginUtil.sendPassword(actionRequest, emailFromName, emailFromAddress, emailToAddress, subject, body);

        sendRedirect(actionRequest, actionResponse);
    }

    /**
     * Récupère l'id Intériale de la personne MEMBRE PARTICIPANT du contrat
     * 
     * @param contrat les informations du contrat
     * @param actionRequest l'action Request de la requete
     * @return L'id intériale de la personne signataire du contrat
     * @throws LmdeWebServicePersonneException
     * @throws NoSuchUserException
     */
    private static String getPersonneDuContrat(final ContratOE contrat, ActionRequest actionRequest) throws LmdeWebServicePersonneException,
            NoSuchUserException {
        String idInteriale = null;
        // Récupération des garanties du contrat
        List<GarantieOE> garanties = contrat.getGaranties();
        if (garanties == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Aucune garantie trouvée pour le contrat : '" + contrat.getId().getValeur() + "'");
            }
            throw new NoSuchUserException();
        } else {
            for (GarantieOE garantie : garanties) {
                // Récupération des beneficiaires de chaque garantie
                List<BeneficiaireOE> beneficiaires = garantie.getBeneficiaires();
                if (beneficiaires == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Aucun bénéficiaire trouvé pour la garantie : '" + garantie.getId().getValeur() + "'");
                    }
                    throw new NoSuchUserException();
                } else {
                    for (BeneficiaireOE beneficiaire : beneficiaires) {
                        if (TypeLienFamilialOE.MEMBRE_PARTICIPANT.equals(beneficiaire.getLienFamilial())) {
                            ConsulterPersonneResponseParameter consulterPersonne = PersonneClientWebServiceUtil.consulterPersonneParBPUAvecCache(beneficiaire
                                    .getBpuPersonne());
                            if (consulterPersonne.getPersonne() == null) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("Aucune personne trouvée");
                                }
                                throw new NoSuchUserException();
                            }
                            String toAddress = getEmailPerso(consulterPersonne.getPersonne());
                            if (toAddress == null) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("L'utilisateur n'a pas de mail de renseigné");
                                }
                                throw new NoSuchUserException();
                            } else {
                                // this attribute will be used in mail template
                                actionRequest.setAttribute("LMDE_toAddress", toAddress);
                            }

                            if (consulterPersonne.getPersonne().getId() != null) {
                                idInteriale = consulterPersonne.getPersonne().getId().getValeur();

                            }
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Récupération de l'adhérent intériale : " + idInteriale);
                            }
                            break;
                        }
                    }
                    if (idInteriale != null) {
                        break;
                    }
                }

            }
        }
        return idInteriale;
    }

    /**
     * @param personne
     * @param toAddress
     * @return
     */
    public static String getEmailPerso(PersonneOE personne) {
        List<CoordonneeOE> coordonnees = personne.getCoordonnees();
        if (coordonnees != null) {
            for (CoordonneeOE coordonnee : coordonnees) {
                if (coordonnee.getRefCoordonnee() != null && TypeCoordonneeOE.EMAIL_PERSONNEL.equals(coordonnee.getRefCoordonnee().getType())) {

                    return coordonnee.getEmail().getValeur().getValeur();
                }
            }
        }
        return null;
    }
}