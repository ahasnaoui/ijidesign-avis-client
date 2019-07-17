package com.orange.oab.lmde.portal.security.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.liferay.portal.PwdEncryptorException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import com.liferay.portal.security.pwd.PwdAuthenticator;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.admin.util.OmniadminUtil;
import com.liferay.util.Normalizer;
import com.orange.oab.lmde.liferay.service.constant.CauseEchecAuthentification;
import com.orange.oab.lmde.liferay.service.service.LmdeAuthentificationTraceLocalServiceUtil;
import com.orange.oab.lmde.service.bean.ContratBean;
import com.orange.oab.lmde.service.exception.LmdeServiceContratException;
import com.orange.oab.lmde.service.service.ContratServiceUtil;
import com.orange.oab.lmde.util.enums.LMDEEnums.LMDEOffres;
import com.orange.oab.lmde.util.role.RoleUtil;
import com.orange.oab.lmde.ws.authentification.util.AuthentificationUtil;
import com.orange.oab.lmde.ws.personne.exception.LmdeWebServicePersonneException;
import com.orange.oab.lmde.ws.personne.service.PersonneClientWebServiceUtil;

import fr.interiale.si.fonctionsgeneriques.securite.authentification.objets.v1.TypeApplicationOE;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.ConsulterPersonneResponseParameter;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.PersonneOE;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.TypeSousStatutPersonneOE;
import fr.interiale.si.gisements.personnes.personnesphysiques.objets.v1.TypeStatutPersonneOE;

/**
 * Authentificateur spécifique LMDE.
 * 
 * @author Anthony BAUDOUIN (gvxg2439)
 */
/**
 * @author gvxg2439
 */
public class LMDEAuth implements Authenticator {

    /*
     * (non-Javadoc)
     * @see com.liferay.portal.security.auth.Authenticator#authenticateByEmailAddress(long, java.lang.String,
     * java.lang.String, java.util.Map, java.util.Map)
     */
    @Override
    public int authenticateByEmailAddress(long companyId, String emailAddress, String password, Map<String, String[]> headerMap,
            Map<String, String[]> parameterMap) throws AuthException {
        try {
            return authenticate(companyId, emailAddress, password);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new AuthException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.liferay.portal.security.auth.Authenticator#authenticateByScreenName(long, java.lang.String,
     * java.lang.String, java.util.Map, java.util.Map)
     */
    @Override
    public int authenticateByScreenName(long companyId, String screenName, String password, Map<String, String[]> headerMap, Map<String, String[]> parameterMap)
            throws AuthException {
        throw new AuthException("Authentification par screenName n'est pas geree.");
    }

    /*
     * (non-Javadoc)
     * @see com.liferay.portal.security.auth.Authenticator#authenticateByUserId(long, long, java.lang.String,
     * java.util.Map, java.util.Map)
     */
    @Override
    public int authenticateByUserId(long companyId, long userId, String password, Map<String, String[]> headerMap, Map<String, String[]> parameterMap)
            throws AuthException {
        try {
            return authenticate(companyId, userId);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new AuthException(e);
        }
    }

    /**
     * Authentification par USER_ID. On vérifie que l'utilisateur est bien présent côté WS LMDE. Le mot de passe n'est
     * pas
     * utilisé
     * 
     * @param companyId
     * @param userId
     * @return
     * @throws AuthException
     */
    protected int authenticate(long companyId, long userId) throws AuthException {

        // Récupération de l'utilisateur Liferay correspondant
        User user = null;
        ConsulterPersonneResponseParameter personne = null;
        try {
            user = UserLocalServiceUtil.getUserById(userId);
        } catch (PortalException | SystemException e1) {
            LmdeAuthentificationTraceLocalServiceUtil.traceAuthentification(String.valueOf(userId), CauseEchecAuthentification.LIFERAY_USER_NOT_FOUND,
                    "Erreur lors de la récupération de l'utilisateur dans la BDD");
            LOGGER.error("Impossible de récupérer l'utilisateur dans la BDD Liferay " + userId);
        }

        // Etape 2 : Si étape 1 OK, mise à jour de l'utilisateur dans Liferay
        if (user != null && user.getScreenName() != null) {
            try {
                // Récupération des informations de l'utilisateur
                personne = PersonneClientWebServiceUtil.consulterPersonneAvecCache(user.getScreenName());
                if (personne != null && personne.getPersonne() != null) {
                    // Mise à jour du nom, prénom et rôle de l'utilisateur
                    modifierUtilisateurLiferay(personne, companyId, user, user.getEmailAddress());
                } else {
                    LmdeAuthentificationTraceLocalServiceUtil.traceAuthentification(user.getEmailAddress(), CauseEchecAuthentification.LMDE_PERSONNE_ERROR,
                            "La Personne n'a pas été trouvée dans le SI Intériale. Retour vide du Web Service Personne");
                }
            } catch (LmdeWebServicePersonneException e) {
                LmdeAuthentificationTraceLocalServiceUtil.traceAuthentification(user.getEmailAddress(), CauseEchecAuthentification.LMDE_PERSONNE_ERROR,
                        "Erreur lors de l'appel au Web Service Personne");
                LOGGER.error("Impossible de mettre à jour les informations de l'utilisateur " + user.getScreenName());
            }
        }

        return user != null && personne != null && personne.getPersonne() != null ? SUCCESS : FAILURE;
    }

    /**
     * @param companyId
     * @param emailAddress
     * @param password
     * @return
     * @throws AuthException
     */
    protected int authenticate(long companyId, String emailAddress, String password) throws AuthException {

        // Etape 1 : Appel WS d'authentification
        String identifiantLMDE = AuthentificationUtil.authentificationWS(emailAddress, password, TypeApplicationOE.WEB_LMDE);

        // Récupération de l'utilisateur Liferay correspondant
        User user = obtenirUtilisateur(companyId, emailAddress);

        // Etape 2 : Si étape 1 OK, mise à jour de l'utilisateur dans Liferay
        if (identifiantLMDE != null) {
            try {
                // Récupération des informations de l'utilisateur
                ConsulterPersonneResponseParameter personne = PersonneClientWebServiceUtil.consulterPersonneAvecCache(identifiantLMDE);
                if (personne != null && personne.getPersonne() != null) {
                    // Mise à jour du nom, prénom et rôle de l'utilisateur
                    modifierUtilisateurLiferay(personne, companyId, user, emailAddress);
                } else {
                    LmdeAuthentificationTraceLocalServiceUtil.traceAuthentification(emailAddress, CauseEchecAuthentification.LMDE_PERSONNE_ERROR,
                            "La Personne n'a pas été trouvée dans le SI Intériale. Retour vide du Web Service Personne");
                }
            } catch (LmdeWebServicePersonneException e) {
                LmdeAuthentificationTraceLocalServiceUtil.traceAuthentification(emailAddress, CauseEchecAuthentification.LMDE_PERSONNE_ERROR,
                        "Erreur lors de l'appel au Web Service Personne");
                LOGGER.error("Impossible de mettre à jour les informations de l'utilisateur " + identifiantLMDE);
            }
        }

        // Etape 3 : Si étape 1 est échec, on regarde si l'utilisateur est un admin ou utilisateur interne
        int resultEtape3 = FAILURE;
        if (identifiantLMDE == null) {
            long userId = user.getUserId();
            String encryptPassword = user.getPassword();

            resultEtape3 = authentificationAdminPortail(userId, emailAddress, password, encryptPassword);
            if (resultEtape3 == FAILURE) {
                resultEtape3 = authentificationInterneLMDE(companyId, emailAddress, password, encryptPassword, userId);
            }
        }

        return identifiantLMDE != null || resultEtape3 == SUCCESS ? SUCCESS : FAILURE;
    }

    /**
     * Authentifie l'utilisateur si celui-ci est un Admin Portail
     * 
     * @param userId identifiant de l'utilisateur Liferay
     * @param emailAddress adresse email de l'utilisateur
     * @param password mot de passe saisi dans le formulaire de login
     * @param encryptPassword mot de passe crypté de l'utilisateur Liferay
     * @return SUCCESS si l'utilisateur est un Admin Portail et est authentifié, FAILURE sinon
     * @throws AuthException levée si le rôle "interne LMDE" n'existe pas dans Liferay
     */
    private int authentificationAdminPortail(long userId, String emailAddress, String password, String encryptPassword) throws AuthException {
        // Si utilisateur est admin Liferay
        if (OmniadminUtil.isOmniadmin(userId)) {
            // Verification que le mot de passe saisi est celui connu en base
            boolean authenticated;

            authenticated = verificationMotdePasse(emailAddress, password, encryptPassword);

            if (authenticated) {
                return SUCCESS;
            }
        }
        return FAILURE;
    }

    /**
     * Authentifie l'utilisateur si celui-ci est un utilisateur interne LMDE
     * 
     * @param companyId identifiant de l'instance du portail
     * @param emailAddress adresse email de l'utilisateur
     * @param password mot de passe saisi dans le formulaire de login
     * @param encryptPassword mot de passe crypté de l'utilisateur Liferay
     * @param userId identifiant de l'utilisateur Liferay
     * @return SUCCESS si l'utilisateur est interne et authentifié, FAILURE sinon
     * @throws AuthException levée si le rôle "interne LMDE" n'existe pas dans Liferay
     */
    private int authentificationInterneLMDE(long companyId, String emailAddress, String password, String encryptPassword, long userId) throws AuthException {
        try {
            // Authentification de l'utilisateur si celui-ci est un interne LMDE
            if (UserLocalServiceUtil.hasRoleUser(companyId, RoleUtil.ROLE_INTERNE_LMDE_NOM, userId, false)) {

                // Verification que le mot de passe saisi est celui connu en base
                boolean authenticated = verificationMotdePasse(emailAddress, password, encryptPassword);

                if (authenticated) {
                    return SUCCESS;
                }
            }
            return FAILURE;
        } catch (PortalException | SystemException e) {
            throw new AuthException("Role Interne LMDE n'existe pas");
        }
    }

    /**
     * Retourne l'utilisateur Liferay en fonction de son adresse email.
     * 
     * @param companyId identifiant de l'instance du portail
     * @param emailAddress adresse email de l'utilisateur
     * @return l'utilisateur avec l'adresse email
     * @throws AuthException levée si l'utilisateur n'existe pas dans Liferay
     */
    private User obtenirUtilisateur(long companyId, String emailAddress) throws AuthException {
        try {
            return UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress);
        } catch (PortalException | SystemException e) {
            throw new AuthException("L'utilisateur ayant pour adresse email " + emailAddress + " n'est pas connu du portail Liferay");
        }
    }

    /**
     * Met à jour les informations de l'utilisateur Liferay correspondant à l'utilisateur du SI Intériale.
     * 
     * @param personne utilisateur du SI Intériale.
     * @param companyId identifiant de l'instance Liferay
     * @param user utilisateur Liferay
     * @param emailAddress l'adresse e-mail de l'utilisateur
     * @throws AuthException levé si l'utilisateur n'est pas connu dans Liferay
     */
    private void modifierUtilisateurLiferay(ConsulterPersonneResponseParameter personne, long companyId, User user, String emailAddress) throws AuthException {
        long userId = user.getUserId();
        PersonneOE p = personne.getPersonne();

        try {
            // Changement du nom, prénom et du rôle
            user.setLastName(p.getNom());
            user.setFirstName(p.getPrenom());
            UserLocalServiceUtil.updateUser(user);

            // Récupération des rôles de l'utilisateur
            List<Role> userRoles = user.getRoles();

            Set<String> listRolesInteriale = new HashSet<String>();
            for (TypeStatutPersonneOE st : TypeStatutPersonneOE.values()) {
                listRolesInteriale.add(st.name());
            }

            // Suppression de l'ancien rôle (Adhérent, Prospect, Client, Assuré)
            for (Role role : userRoles) {
                String roleName = Normalizer.normalizeToAscii(role.getName().toUpperCase());

                if (listRolesInteriale.contains(roleName) || RoleUtil.ROLE_AFFILIE_LMDE_NOM.equalsIgnoreCase(roleName)) {
                    // Suppression de l'association
                    UserLocalServiceUtil.deleteRoleUser(role.getRoleId(), userId);
                }
                
                // LMDE-718 : Portail - Ajouter un rôle à l'adhérent en fonction de l'offre souscrite
                if (roleName.startsWith(RoleUtil.ROLE_CONTRAT_PREFIX)) {
                	UserLocalServiceUtil.deleteRoleUser(role.getRoleId(), userId);
                }

            }

            // Modification du rôle
            String roleInteriale = p.getStatut().value();
            if (Normalizer.normalizeToAscii(RoleUtil.ROLE_ASSURE_LMDE_NOM.toUpperCase()).equalsIgnoreCase(roleInteriale)) {
                if (p.getDonneeRO() != null && p.getDonneeRO().isRo()) {
                    roleInteriale = Normalizer.normalizeToAscii(RoleUtil.ROLE_AFFILIE_LMDE_NOM.toUpperCase());
                }
            }

            // Rôle PreAdherent si PROSPECT + sous statut = PRE_ADHERENT
            if (Normalizer.normalizeToAscii(RoleUtil.ROLE_PROSPECT_LMDE_NOM.toUpperCase()).equalsIgnoreCase(roleInteriale)) {
                if (TypeSousStatutPersonneOE.PRE_ADHERENT.equals(p.getSousStatut())) {
                    roleInteriale = Normalizer.normalizeToAscii(RoleUtil.ROLE_PRE_ADHERENT_LMDE_NOM.toUpperCase());
                }
            }

            Role roleLiferay = RoleUtil.getLiferayRoleInterialeStatut(companyId, roleInteriale);
            UserLocalServiceUtil.addRoleUser(roleLiferay.getRoleId(), userId);
            
            // LMDE-718 : Portail - Ajouter un rôle à l'adhérent en fonction de l'offre souscrite
            if (TypeStatutPersonneOE.ADHERENT.equals(p.getStatut())) {
            	ContratBean contrat = ContratServiceUtil.getActiveContratByIdPersonne(p.getId().getValeur());
            	if (contrat != null) {
            		// Ajout du rôle en fonction du contrat
            		final LMDEOffres labelOffre = LMDEOffres.getOffreByLabel(contrat.getNomContrat());
            		if (labelOffre != null) {
            			Role roleContrat = RoleUtil.getLiferayRoleInterialeStatut(companyId, labelOffre.name());
            			UserLocalServiceUtil.addRoleUser(roleContrat.getRoleId(), userId);
            		}
            	}
            }
            
        } catch (SystemException | PortalException | LmdeServiceContratException e) {
            LmdeAuthentificationTraceLocalServiceUtil.traceAuthentification(emailAddress, CauseEchecAuthentification.LIFERAY_USER_UPDATE_ERROR,
                    "Erreur lors de l'appel au Web Service Personne");
            LOGGER.error("Erreur lors de la mise a jour de l'utilisateur", e);
        }
    }

    /**
     * Vérifie que le mot de passe saisi est celui stocké en base liferay
     * 
     * @param emailAddress adresse email de l'utilisateur
     * @param password mot de passe saisi
     * @param encryptPassword mot de passe enregistré en base
     * @return vrai si les deux mots de passe sont identiques, faux sinon
     * @throws AuthException levé si il n'a pas été possible de vérifier le mot de passe.
     */
    private boolean verificationMotdePasse(String emailAddress, String password, String encryptPassword) throws AuthException {
        try {
            return PwdAuthenticator.authenticate(emailAddress, password, encryptPassword);
        } catch (PwdEncryptorException | SystemException e) {
            throw new AuthException("Impossible de vérifier le mot de passe");
        }
    }

    /**
     * Logger de la classe.
     */
    private static final Log LOGGER = LogFactoryUtil.getLog(LMDEAuth.class);

}