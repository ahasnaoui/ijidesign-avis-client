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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.facebook.FacebookConnectUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.struts.ActionConstants;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.login.util.LoginUtil;
import com.orange.oab.lmde.liferay.service.model.LmdeAuthentificationToken;
import com.orange.oab.lmde.liferay.service.service.LmdeAuthentificationTokenLocalServiceUtil;
import com.orange.oab.lmde.service.bean.PersonneBean;
import com.orange.oab.lmde.service.exception.LmdeServiceEnvoiEmailException;
import com.orange.oab.lmde.service.service.CompteServiceUtil;
import com.orange.oab.lmde.service.service.EnvoiEmailServiceUtil;
import com.orange.oab.lmde.service.service.PersonneServiceUtil;
import com.orange.oab.lmde.service.util.LmdeCompteUtil;
import com.orange.oab.lmde.util.constant.LMDEConstants;
import com.orange.oab.lmde.ws.authentification.exception.LmdeWebServiceAuthentificationException;

import fr.interiale.si.fonctionsgeneriques.securite.authentification.objets.v1.ConsulterCompteResponseParameter;
import fr.interiale.si.fonctionsgeneriques.securite.authentification.objets.v1.DesactiverCompteResponseParameter;

/**
 * Extension de la Struts action de la connexion Facebook
 * 
 * @author Wilson Man
 * @author Sergio González
 * @author Mika Koivisto
 */
public class FacebookConnectAction extends PortletAction {

    static final Log LOGGER = LogFactoryUtil.getLog(FacebookConnectAction.class);

    @Override
    public ActionForward render(ActionMapping actionMapping, ActionForm actionForm, PortletConfig portletConfig, RenderRequest renderRequest,
            RenderResponse renderResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(CompanyConstants.AUTH_TYPE_ID);

        if (!FacebookConnectUtil.isEnabled(themeDisplay.getCompanyId())) {
            return actionMapping.findForward("portlet.login.login");
        }

        return actionMapping.findForward("portlet.login.facebook_login");
    }

    @Override
    public ActionForward strutsExecute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        if (!FacebookConnectUtil.isEnabled(themeDisplay.getCompanyId())) {
            throw new PrincipalException();
        }

        HttpSession session = request.getSession();

        String redirect = ParamUtil.getString(request, "redirect");
        // Début CUSTOM
        String actionToPerform = null;
        String email = null;
        String errorRedirect = null;
        String idInteriale = null;
        String portletNamespace = null;
        String authToken = null;
        Map<String, String> parameters = getQueryMap(redirect);
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                // On décode deux fois car l'url est encodée une fois chez nous et une autre fois côté facebook
                if (entry.getKey().equals("email")) {
                    email = HttpUtil.decodeURL(entry.getValue());
                    email = HttpUtil.decodeURL(email);
                } else if (entry.getKey().equals("from")) {
                    actionToPerform = HttpUtil.decodeURL(entry.getValue());
                    actionToPerform = HttpUtil.decodeURL(actionToPerform);
                } else if (entry.getKey().equals("errorRedirectURL")) {
                    errorRedirect = HttpUtil.decodeURL(entry.getValue());
                    errorRedirect = HttpUtil.decodeURL(errorRedirect);
                } else if (entry.getKey().equals("idInteriale")) {
                    idInteriale = HttpUtil.decodeURL(entry.getValue());
                    idInteriale = HttpUtil.decodeURL(idInteriale);
                } else if (entry.getKey().equals("tokenKey")) {
                    authToken = HttpUtil.decodeURL(entry.getValue());
                    authToken = HttpUtil.decodeURL(authToken);
                } else if (entry.getKey().equals("namespace")) {
                    portletNamespace = HttpUtil.decodeURL(entry.getValue());
                    portletNamespace = HttpUtil.decodeURL(portletNamespace);
                }
            }
        }

        // Fin CUSTOM

        String code = ParamUtil.getString(request, "code");

        String token = FacebookConnectUtil.getAccessToken(themeDisplay.getCompanyId(), redirect, code);

        if (Validator.isNotNull(token)) {
            try {
                // Début CUSTOM
                User user = setFacebookCredentials(session, themeDisplay.getCompanyId(), token, actionToPerform, email, idInteriale, authToken);

                // On passe par une méthode de login via USER_ID pour vérifier l'user via WS. Le mot de passe est
                // indispensable mais ne sera pas utilisé. L'utilisateur doit être approuvé
                if (user != null && actionToPerform == null && user.getStatus() == WorkflowConstants.STATUS_APPROVED) {
                    LoginUtil.login(request, response, String.valueOf(user.getUserId()), "foobar", false, CompanyConstants.AUTH_TYPE_ID);
                    // si l'utilisateur est en PENDING alors on vérifie que le token de la requête soit ok;
                } else if (user != null && actionToPerform == null && user.getStatus() == WorkflowConstants.STATUS_PENDING) {
                    LmdeAuthentificationToken lmdeAuthentificationToken = LmdeAuthentificationTokenLocalServiceUtil.fetchLmdeAuthentificationToken(user
                            .getUserId());
                    if (lmdeAuthentificationToken != null && lmdeAuthentificationToken.getToken() != null
                            && lmdeAuthentificationToken.getToken().equals(authToken)) {
                        ServiceContext serviceContext = new ServiceContext();
                        user = UserLocalServiceUtil.updateStatus(user.getUserId(), WorkflowConstants.STATUS_APPROVED, serviceContext);
                        LmdeAuthentificationTokenLocalServiceUtil.deleteLmdeAuthentificationToken(lmdeAuthentificationToken);
                        LoginUtil.login(request, response, String.valueOf(user.getUserId()), "firstLogin", false, CompanyConstants.AUTH_TYPE_ID);
                    } else {
                        LOGGER.warn("Erreur d'athentification : Le compte " + email + " n'est pas actif et le token ne correspond pas");
                        session.setAttribute("facebookTokenFailed", "f");
                    }
                }
                // cas de la création de compte
                else if (user != null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_CREATION_COMPTE)) {
                    // création token + envoi mail
                    try {
                        PersonneBean personne = PersonneServiceUtil.getPersonneById(idInteriale);
                        authToken = UUID.randomUUID().toString();
                        LmdeAuthentificationToken lmdeAuthentificationToken = LmdeAuthentificationTokenLocalServiceUtil.createLmdeAuthentificationToken(user
                                .getUserId());
                        lmdeAuthentificationToken.setToken(authToken);
                        LmdeAuthentificationTokenLocalServiceUtil.addLmdeAuthentificationToken(lmdeAuthentificationToken);

                        String fullURL = PortalUtil.getPortalURL(request);

                        Map<String, String> emailValues = new HashMap<String, String>();
                        emailValues.put("PORTAL_URL", fullURL);
                        emailValues.put("TOKEN_KEY", authToken);
                        emailValues.put("USER_FIRST_NAME", personne.getPrenom());
                        emailValues.put("USER_LAST_NAME", personne.getNom());
                        EnvoiEmailServiceUtil.envoiEmail(email, "EMAIL_CREATION_COMPTE_FACEBOOK.html", "Votre création de compte My LMDE", emailValues);

                        lmdeAuthentificationToken.setEnvoyeParEmail(true);
                    } catch (SystemException | LmdeServiceEnvoiEmailException e) {
                        try {
                            UserLocalServiceUtil.deleteUser(user.getUserId());
                        } catch (PortalException | SystemException e2) {
                            LOGGER.error("Erreur lors de la suppression de l'utilisateur liferay '" + user.getUserId()
                                    + "' suite a une erreur lors de la création du token en base de données Liferay", e);
                        }
                        LOGGER.error("erreur lors de la gestion du token", e);
                        redirect = errorRedirect;
                    }

                }
                // cas du passage de mail à facebook
                else if (user != null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_EMAIL_TO_FACEBOOK)) {

                    boolean isOk = false;
                    ConsulterCompteResponseParameter compte = CompteServiceUtil.consulterCompte(user.getEmailAddress());
                    if (compte != null && compte.getCompte() != null && compte.getCompte().getId() != null) {
                        DesactiverCompteResponseParameter reponse = CompteServiceUtil.disableCompte(compte.getCompte().getId().getValeur());
                        if (reponse != null && reponse.getStatut() != null && reponse.getStatut().isFait()) {
                            isOk = true;
                        }
                    }
                    if (!isOk) {
                        LOGGER.error("erreur lors de la désactivation du compte de" + user.getEmailAddress());
                        UserLocalServiceUtil.updateFacebookId(user.getUserId(), 0);
                        redirect = redirect + "&" + portletNamespace + "notification=eMailToFacebookException";
                    } else {
                        redirect = redirect + "&" + portletNamespace + "notification=eMailToFacebookSuccess";
                    }

                }
                // cas du passage de facebook à mail
                else if (user != null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_FACEBOOK_TO_EMAIL)) {
                    boolean isOk = false;

                    ConsulterCompteResponseParameter compte = CompteServiceUtil.consulterCompte(user.getEmailAddress());
                    long facebookId = user.getFacebookId();
                    if (compte != null && (compte.getCompte() == null || (compte.getCompte() != null && !compte.getCompte().isActif()))) {
                        try {
                            String motDePasse = LmdeCompteUtil.generatePassword();
                            LmdeCompteUtil.activateInterialeAccount(user.getEmailAddress(), user.getScreenName(), motDePasse);
                            UserLocalServiceUtil.updateFacebookId(user.getUserId(), 0);
                            Map<String, String> emailValues = new HashMap<String, String>();
                            emailValues.put("TO_NAME", user.getFirstName());
                            emailValues.put("USER_PASSWORD", motDePasse);
                            EnvoiEmailServiceUtil.envoiEmail(user.getEmailAddress(), "EMAIL_MODIFICATION_AUTHENTIFICATION_EMAIL.html",
                                    "Votre mise à jour de votre connexion MyLMDE", emailValues);
                            isOk = true;
                        } catch (LmdeWebServiceAuthentificationException | LmdeServiceEnvoiEmailException | PortalException | SystemException e) {
                            CompteServiceUtil.disableCompte(compte.getCompte().getId().getValeur());
                            UserLocalServiceUtil.updateFacebookId(user.getUserId(), facebookId);
                            LOGGER.error("erreur lors de la modification du compte Facebook vers email de l'utilisateur " + email, e);
                        }
                    }
                    if (!isOk) {
                        redirect = redirect + "&" + portletNamespace + "notification=facebookToeMailException";
                    } else {
                        redirect = redirect + "&" + portletNamespace + "notification=facebookToeMailSuccess";
                    }
                }
                // utilisateur déjà présent lors de la création de compte
                else if (user == null && actionToPerform != null && (actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_CREATION_COMPTE))
                        && errorRedirect != null) {
                    errorRedirect = errorRedirect + "&" + portletNamespace + "errorCause=AuthentificationException";
                    redirect = errorRedirect;
                    // utilisateur déjà présent lors du passage de mail à Facebook
                } else if (user == null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_EMAIL_TO_FACEBOOK)) {
                    LOGGER.error("erreur lors du passage de connexion e-mail vers facebook pour " + idInteriale);
                    redirect = redirect + "&" + portletNamespace + "notification=eMailToFacebookException";
                    // utilisateur non présent lors du passage de facebook à email
                } else if (user == null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_FACEBOOK_TO_EMAIL)) {
                    LOGGER.error("erreur lors du passage de connexion facebook vers e-mail pour " + idInteriale);
                    redirect = redirect + "&" + portletNamespace + "notification=facebookToeMailException";
                }
            } catch (LmdeWebServiceAuthentificationException e) {
                LOGGER.error("erreur lors de la création du compte sur le SI Intériale", e);
                errorRedirect = errorRedirect + "&" + portletNamespace + "errorCause=AuthentificationException";
                redirect = errorRedirect;
            }
            // Fin CUSTOM
        } else {

            return actionMapping.findForward(ActionConstants.COMMON_REFERER_JSP);
        }

        response.sendRedirect(redirect);

        return null;
    }

    protected User addUser(HttpSession session, long companyId, JSONObject jsonObject, String email, String idInteriale) throws Exception {

        long creatorUserId = 0;
        boolean autoPassword = true;
        String password1 = StringPool.BLANK;
        String password2 = StringPool.BLANK;
        // Début CUSTOM
        boolean autoScreenName = false;
        String screenName = idInteriale;
        String emailAddress = email;
        // Fin CUSTOM
        long facebookId = jsonObject.getLong("id");
        String openId = StringPool.BLANK;
        Locale locale = LocaleUtil.getDefault();
        String firstName = jsonObject.getString("first_name");
        String middleName = StringPool.BLANK;
        String lastName = jsonObject.getString("last_name");
        int prefixId = 0;
        int suffixId = 0;
        boolean male = Validator.equals(jsonObject.getString("gender"), "male");
        int birthdayMonth = Calendar.JANUARY;
        int birthdayDay = 1;
        int birthdayYear = 1970;
        String jobTitle = StringPool.BLANK;
        long[] groupIds = null;
        long[] organizationIds = null;
        long[] roleIds = null;
        long[] userGroupIds = null;
        boolean sendEmail = true;

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setAttribute(LMDEConstants.CREATION_COMPTE_LMDE, true);

        User user = UserLocalServiceUtil.addUser(creatorUserId, companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress,
                facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle,
                groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

        user = UserLocalServiceUtil.updateLastLogin(user.getUserId(), user.getLoginIP());

        user = UserLocalServiceUtil.updatePasswordReset(user.getUserId(), false);

        user = UserLocalServiceUtil.updateEmailAddressVerified(user.getUserId(), true);

        // Début CUSTOM
        user = UserLocalServiceUtil.updateStatus(user.getUserId(), WorkflowConstants.STATUS_PENDING, serviceContext);
        // Fin CUSTOM

        session.setAttribute(WebKeys.FACEBOOK_USER_EMAIL_ADDRESS, emailAddress);

        return user;
    }

    protected void redirectUpdateAccount(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        PortletURL portletURL = PortletURLFactoryUtil.create(request, PortletKeys.LOGIN, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

        portletURL.setParameter("saveLastPath", Boolean.FALSE.toString());
        portletURL.setParameter("struts_action", "/login/update_account");

        PortletURL redirectURL = PortletURLFactoryUtil.create(request, PortletKeys.FAST_LOGIN, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

        redirectURL.setParameter("struts_action", "/login/login_redirect");
        redirectURL.setParameter("emailAddress", user.getEmailAddress());
        redirectURL.setParameter("anonymousUser", Boolean.FALSE.toString());
        redirectURL.setPortletMode(PortletMode.VIEW);
        redirectURL.setWindowState(LiferayWindowState.POP_UP);

        portletURL.setParameter("redirect", redirectURL.toString());
        portletURL.setParameter("userId", String.valueOf(user.getUserId()));
        portletURL.setParameter("emailAddress", user.getEmailAddress());
        portletURL.setParameter("firstName", user.getFirstName());
        portletURL.setParameter("lastName", user.getLastName());
        portletURL.setPortletMode(PortletMode.VIEW);
        portletURL.setWindowState(LiferayWindowState.POP_UP);

        response.sendRedirect(portletURL.toString());
    }

    /**
     * Cette méthode permet de mettre à jour / créer /récupérer l'utilisateur à partir des données reçues de facebook
     * <ul>
     * <li>Si les données proviennent de l'authentification alors on récupère l'utilisateur concerné</li>
     * <li>Si les données proviennent de la création de compte alors on créé un nouvel user</li>
     * <li>Si les données proviennent du passage d'email à Facebook alors on ajoute le facebookId à l'User</li>
     * </ul>
     * 
     * @param session
     * @param companyId
     * @param token
     * @param actionToPerform
     * @param email
     * @param idInteriale
     * @param authToken
     * @return
     * @throws LmdeWebServiceAuthentificationException
     * @throws Exception
     */
    protected User setFacebookCredentials(HttpSession session, long companyId, String token, String actionToPerform, String email, String idInteriale,
            String authToken) throws LmdeWebServiceAuthentificationException, Exception {

        JSONObject jsonObject = FacebookConnectUtil.getGraphResources(companyId, "/me", token, "id,email,first_name,last_name,gender");

        if ((jsonObject == null) || (jsonObject.getJSONObject("error") != null)) {

            return null;
        }

        if (FacebookConnectUtil.isVerifiedAccountRequired(companyId) && !jsonObject.getBoolean("verified")) {

            return null;
        }

        User user = null;

        long facebookId = jsonObject.getLong("id");

        if (facebookId > 0) {
            session.setAttribute(WebKeys.FACEBOOK_ACCESS_TOKEN, token);

            user = UserLocalServiceUtil.fetchUserByFacebookId(companyId, facebookId);

            if ((user != null) && (user.getStatus() != WorkflowConstants.STATUS_INCOMPLETE)) {

                session.setAttribute(WebKeys.FACEBOOK_USER_ID, String.valueOf(facebookId));
            }
        }
        // DEBUT CUSTOM
        session.setAttribute("connectionFrom", "facebook");

        /*
         * Pas d'authentification avec l'adresse e-mail
         * String emailAddress = jsonObject.getString("email");
         * if ((user == null) && Validator.isNotNull(emailAddress)) {
         * user = UserLocalServiceUtil.fetchUserByEmailAddress(companyId, emailAddress);
         * if ((user != null) && (user.getStatus() != WorkflowConstants.STATUS_INCOMPLETE)) {
         * session.setAttribute(WebKeys.FACEBOOK_USER_EMAIL_ADDRESS, emailAddress);
         * }
         * }
         */

        // Si l'utilisateur existe et que l'on n'est pas dans la création de compte
        if (user != null && actionToPerform == null) {
            return user;

            // si l'utilisateur n'est pas en BDD et que l'on est dans la création de compte
        } else if (user == null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_CREATION_COMPTE) && email != null
                && idInteriale != null) {
            user = addUser(session, companyId, jsonObject, email, idInteriale);
            // Code commenté, nous ne créons pas le compte Intériale si création de compte Facebook
            /*
             * if (user != null) {
             * String password = StringPool.BLANK;
             * try {
             * password = LmdeCompteUtil.generatePassword();
             * // Creation du compte utilisateur dans le SI interiale
             * } catch (LmdeWebServiceAuthentificationException e) {
             * try {
             * UserLocalServiceUtil.deleteUser(user.getUserId());
             * } catch (PortalException | SystemException e2) {
             * LOGGER.error("Erreur lors de la suppression de l'utilisateur liferay '" + user.getUserId()
             * + "' suite a une erreur lors de la création du compte Intériale", e);
             * }
             * throw e;
             * }
             * LmdeCompteUtil.createInterialAccount(email, idInteriale, password, user);
             * }
             */
            //
            // Si passage de mail à facebook et user non trouvé : OK
        } else if (user == null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_EMAIL_TO_FACEBOOK) && idInteriale != null
                && authToken != null) {
            user = addFacebookId(jsonObject, idInteriale, companyId, authToken);
            // Si passage de facebook à mail et user trouvé : on vérifie que l'e-mail correspond au compte facebook
        } else if (user != null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_FACEBOOK_TO_EMAIL)) {
            if (email == null || !email.equals(user.getEmailAddress())) {
                user = null;
            }
        } // si l'utilisateur existe mais que l'on est dans la création de compte ou dans l'action de mail -> facebook
          // (facebook id déjà présent). Alors erreur !
        else if (user != null
                && actionToPerform != null
                && (actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_CREATION_COMPTE) || actionToPerform
                        .equals(LMDEConstants.FACEBOOK_ACTION_EMAIL_TO_FACEBOOK))) {
            user = null;
            // Si passage de facebook à mail et user non trouvé : erreur !
        } else if (user == null && actionToPerform != null && actionToPerform.equals(LMDEConstants.FACEBOOK_ACTION_FACEBOOK_TO_EMAIL)) {
            user = null;
        }
        return user;

    }

    /**
     * Ajoute le facebookID à l'utilisateur si le token correspond bien
     * 
     * @param jsonObject l'objet JSON contenant le facebookID
     * @param idInteriale l'idInteriale de l'utilisateur pour le retrouver
     * @param companyId
     * @param authToken le token de vérification
     * @return l'utilisateur s'il a bien été mis à jour
     */
    protected User addFacebookId(JSONObject jsonObject, String idInteriale, long companyId, String authToken) {

        User user = null;
        try {
            user = UserLocalServiceUtil.getUserByScreenName(companyId, idInteriale);
            LmdeAuthentificationToken lmdeAuthentificationToken = LmdeAuthentificationTokenLocalServiceUtil.fetchLmdeAuthentificationToken(user.getUserId());
            if (lmdeAuthentificationToken != null && lmdeAuthentificationToken.getToken() != null && lmdeAuthentificationToken.getToken().equals(authToken)) {
                LmdeAuthentificationTokenLocalServiceUtil.deleteLmdeAuthentificationToken(lmdeAuthentificationToken);
                if (user != null) {
                    user = UserLocalServiceUtil.updateFacebookId(user.getUserId(), jsonObject.getLong("id"));
                }
            }
        } catch (PortalException | SystemException e) {
            LOGGER.error("erreur lors de l'ajout du facebookID pour l'utilisateur " + idInteriale, e);
            user = null;
        }

        return user;
    }

    protected User updateUser(User user, JSONObject jsonObject, String email, String idInteriale) throws Exception {

        long facebookId = jsonObject.getLong("id");
        String emailAddress = jsonObject.getString("email");
        String firstName = jsonObject.getString("first_name");
        String lastName = jsonObject.getString("last_name");
        boolean male = Validator.equals(jsonObject.getString("gender"), "male");

        if ((facebookId == user.getFacebookId()) && emailAddress.equals(user.getEmailAddress()) && firstName.equals(user.getFirstName())
                && lastName.equals(user.getLastName()) && (male == user.isMale())) {

            return user;
        }

        Contact contact = user.getContact();

        Calendar birthdayCal = CalendarFactoryUtil.getCalendar();

        birthdayCal.setTime(contact.getBirthday());

        int birthdayMonth = birthdayCal.get(Calendar.MONTH);
        int birthdayDay = birthdayCal.get(Calendar.DAY_OF_MONTH);
        int birthdayYear = birthdayCal.get(Calendar.YEAR);

        long[] groupIds = null;
        long[] organizationIds = null;
        long[] roleIds = null;
        List<UserGroupRole> userGroupRoles = null;
        long[] userGroupIds = null;

        ServiceContext serviceContext = new ServiceContext();

        if (!StringUtil.equalsIgnoreCase(emailAddress, user.getEmailAddress())) {

            UserLocalServiceUtil.updateEmailAddress(user.getUserId(), StringPool.BLANK, emailAddress, emailAddress);
        }

        UserLocalServiceUtil.updateEmailAddressVerified(user.getUserId(), true);

        return UserLocalServiceUtil.updateUser(user.getUserId(), StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, false, user.getReminderQueryQuestion(),
                user.getReminderQueryAnswer(), user.getScreenName(), emailAddress, facebookId, user.getOpenId(), user.getLanguageId(), user.getTimeZoneId(),
                user.getGreeting(), user.getComments(), firstName, user.getMiddleName(), lastName, contact.getPrefixId(), contact.getSuffixId(), male,
                birthdayMonth, birthdayDay, birthdayYear, contact.getSmsSn(), contact.getAimSn(), contact.getFacebookSn(), contact.getIcqSn(),
                contact.getJabberSn(), contact.getMsnSn(), contact.getMySpaceSn(), contact.getSkypeSn(), contact.getTwitterSn(), contact.getYmSn(),
                contact.getJobTitle(), groupIds, organizationIds, roleIds, userGroupRoles, userGroupIds, serviceContext);
    }

    /**
     * Récupère les paramètres de l'URL
     * 
     * @param query l'URL au format String
     * @return
     */
    private static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

}