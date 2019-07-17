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

package com.liferay.portal.velocity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.theme.NavItem;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.velocity.LienContactHelper.LienContactVO;
import com.liferay.portal.velocity.LienContactHelper.LienReseauSocialVO;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.orange.oab.lmde.liferay.contact.LienContactConstants;
import com.orange.oab.lmde.liferay.contact.model.StickBarItem;
import com.orange.oab.lmde.liferay.contact.service.StickBarItemLocalServiceUtil;
import com.orange.oab.lmde.service.bean.ContratBean;
import com.orange.oab.lmde.service.bean.PersonneBean;
import com.orange.oab.lmde.service.exception.LmdeServiceContratException;
import com.orange.oab.lmde.service.exception.LmdeServiceException;
import com.orange.oab.lmde.service.exception.LmdeServicePersonneException;
import com.orange.oab.lmde.service.renouvellement.service.RenouvellementServiceUtil;
import com.orange.oab.lmde.service.service.ContratServiceUtil;
import com.orange.oab.lmde.service.service.PersonneServiceUtil;
import com.orange.oab.lmde.service.util.LmdeDateUtil;
import com.orange.oab.lmde.service.util.StickBarItemPositionComparator;
import com.orange.oab.lmde.util.enums.LMDEEnums;
import com.orange.oab.lmde.util.enums.LMDEEnums.LMDEOffres;
import com.orange.oab.lmde.util.portal.LmdePortalProperties;
import com.orange.oab.lmde.util.portal.LmdePortalUtil;
import com.orange.oab.lmde.util.role.RoleUtil;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 */
public class LMDEVelocityTemplateContextHelper extends VelocityTemplateContextHelper {

    private static final Logger LOGGER = Logger.getLogger(LMDEVelocityTemplateContextHelper.class);

    @Override
    public void prepare(Template template, HttpServletRequest request) {
        super.prepare(template, request);
        
        LOGGER.info("Initialize theme templates");

        try {
            final Long lmdeGroupId = LmdePortalUtil.getLmdeGroupId();
            
            final ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

            // LMDE PUBLIC Header items
            final List<Layout> publicNavigationLayout = getPagesPubliquesNavigation(lmdeGroupId);
            if (publicNavigationLayout != null) {
                List<NavItem> publicNavItems = NavItem.fromLayouts(request, publicNavigationLayout, template);
                template.put("lmdePublicNavItems", publicNavItems);
            }
            
            
         // LMDE-834 : Afficher la popin de renouvellement que sur les pages privées
            
            // TODO à la fin de la période de renouvellement, mettre en commentaire le bloc ci-dessous 
            
            /* *******************************************
             *      Début de bloc : activation renouvellement
             * 
             ******************************************* */
            template.put("lmdeShowPopinRenouvellement", "false");
            
            // LMDE PRIVATE Menu items
            final Layout currentLayout = themeDisplay.getLayout();
			if (currentLayout.isPrivateLayout()) {
				// LMDE-834 & LMDE-LMDE-833 : Afficher la popin de renouvellement si l'adhÃ©rent est renouvelant et n'a pas encore fait son choix
				if (isRenouvelantAndNonPrononce(request)) {
					template.put("lmdeShowPopinRenouvellement", "true");
					
					// LMDE-835 : Renouvellement - Page renouvellement - Afficher la popin sauf s'il est en cours de parcours
					if (LmdePortalProperties.LMDE_PAGE_RENOUVELLEMENT_FRIENDLY_URL.equals(currentLayout.getFriendlyURL())) {
						String estRenouvellementEnCours = (String) request.getSession().getAttribute("renouvellementInProcess");
						if ("EN_COURS".equals(estRenouvellementEnCours)) {
							// S'il est en cours de parcours et sur la page "Mon renouvellement" alors ne pas afficher la popin
							template.put("lmdeShowPopinRenouvellement", "false");
						}
					}
				}
            } else {
            	// Si l'utilisateur va sur une autre page, alors le forcer Ã  recommencer son choix de renouvellement
            	request.getSession().removeAttribute("renouvellementInProcess");
            }
			/* *******************************************
			 *		Fin de bloc : activation renouvellement
			 * 
			 *********************************************/
            
			// Ajouter le menu privé
			final List<Layout> privateNavigationLayout = getPagesPriveesNavigation(lmdeGroupId, themeDisplay);
			if (privateNavigationLayout != null) {
				final List<NavItem> privateNavItems = NavItem.fromLayouts(request, privateNavigationLayout, template);
				template.put("lmdePrivateNavItems", privateNavItems);
			}

            // Ajout page privée
            try {
                // La page d'espace personnel
                Layout espacePersonnelNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, true,
                        LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_ESPACE_PERSONNEL);
                template.put("lmdeEspacePersonnelFriendlyUrl", espacePersonnelNavigationLayout.getRegularURL(request));
            } catch (NoSuchLayoutException e) {
                LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_ESPACE_PERSONNEL + " non existante");
            }

            // LMDE TOOLS items
            addToolsPages(template, request, lmdeGroupId);

            // LMDE CONTACT items
            addContactPages(template, request, lmdeGroupId);

            // LMDE RECHERCHE PlIds
            addRecherchePlIds(template, request, lmdeGroupId);

            // LMDE LISTE LIENS du FOOTER
            addFooterLinksPages(template, request, lmdeGroupId);

            // LES LIENS EN BAS DU FOOTER
            addBottomFooterPages(template, request, lmdeGroupId);

            // La page d'authentification
            template.put("lmdeAuthentificationFriendlyUrl",
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_AUTHENTIFICATION);

            // La page d'aide
            template.put("lmdeAideFriendlyUrl", LmdePortalProperties.LMDE_PAGE_AIDE_FRIENDLY_URL);

            // Liens contact
            try {
                LienContactVO lienEmail = LienContactHelper.getLienContactVO(lmdeGroupId,
                        LienContactConstants.LIEN_CONTACT_EMAIL);
                LienContactVO lienTelephone = LienContactHelper.getLienContactVO(lmdeGroupId,
                        LienContactConstants.LIEN_CONTACT_TELEPHONE);
                LienContactVO lienRappel = LienContactHelper.getLienContactVO(lmdeGroupId,
                        LienContactConstants.LIEN_CONTACT_RAPPEL);
                LienContactVO lienMonAgence = LienContactHelper.getLienContactVO(lmdeGroupId,
                        LienContactConstants.LIEN_CONTACT_MON_AGENCE);

                template.put("lienEmail", lienEmail);
                template.put("lienTelephone", lienTelephone);
                template.put("lienRappel", lienRappel);
                template.put("lienMonAgence", lienMonAgence);

                LienReseauSocialVO lienFacebook = LienContactHelper.getLienReseauSocialVO(lmdeGroupId,
                        LienContactConstants.LIEN_RS_FACEBOOK);
                LienReseauSocialVO lienTwitter = LienContactHelper.getLienReseauSocialVO(lmdeGroupId,
                        LienContactConstants.LIEN_RS_TWITTER);
                LienReseauSocialVO lienGooglePlus = LienContactHelper.getLienReseauSocialVO(lmdeGroupId,
                        LienContactConstants.LIEN_RS_GOOGLE_PLUS);
                LienReseauSocialVO lienYoutube = LienContactHelper.getLienReseauSocialVO(lmdeGroupId,
                        LienContactConstants.LIEN_RS_YOUTUBE);
                LienReseauSocialVO lienInstagram = LienContactHelper.getLienReseauSocialVO(lmdeGroupId,
                        LienContactConstants.LIEN_RS_INSTAGRAM);

                template.put("lienFacebook", lienFacebook);
                template.put("lienTwitter", lienTwitter);
                template.put("lienGooglePlus", lienGooglePlus);
                template.put("lienYoutube", lienYoutube);
                template.put("lienInstagram", lienInstagram);
            } catch (RuntimeException e) {
                LOGGER.debug("Les liens de contact ne peuvent pas être récupérés.");
            }

            // Le lien Facebook
            template.put("lmdeFacebookUrl", LmdePortalProperties.LMDE_EXTERNAL_PAGE_FACEBOOK);
            // Le lien Twitter
            template.put("lmdeTwitterUrl", LmdePortalProperties.LMDE_EXTERNAL_PAGE_TWITTER);
            // Le lien Google+
            template.put("lmdeGooglePlusUrl", LmdePortalProperties.LMDE_EXTERNAL_PAGE_GOOGLEPLUS);
            // Le lien Youtube
            template.put("lmdeYoutubeUrl", LmdePortalProperties.LMDE_EXTERNAL_PAGE_YOUTUBE);
            // Le lien Instagram
            template.put("lmdeInstagramUrl", LmdePortalProperties.LMDE_EXTERNAL_PAGE_INSTAGRAM);
            // Le lien Scolarites
            template.put("lmdeScolariteUrl", LmdePortalProperties.LMDE_EXTERNAL_PAGE_SCOLARITES);
            // Le lien Reussite BAC
            template.put("lmdeReussiteBacUrl", LmdePortalProperties.LMDE_EXTERNAL_PAGE_REUSSITE_BAC);

            // l'image meta par défaut dans portal-ext
            String metaDefaultImageUrl = LmdePortalProperties.LMDE_SEO_LINK_FAVICON;

            try {
                // l'image meta par défaut dans le document library
                DLFolder folder = DLFolderLocalServiceUtil.getFolder(lmdeGroupId,
                        DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, LmdePortalProperties.LMDE_META_FOLDER_NAME);
                if (folder != null) {
                    DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(lmdeGroupId, folder.getFolderId(),
                            LmdePortalProperties.LMDE_META_DEFAULT_FILE_NAME);
                    if (fileEntry != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(themeDisplay.getPortalURL());
                        sb.append(StringPool.SLASH + "documents");
                        sb.append(StringPool.SLASH + themeDisplay.getScopeGroupId());
                        sb.append(StringPool.SLASH + fileEntry.getFolderId());
                        sb.append(StringPool.SLASH + fileEntry.getTitle().replace(" ", "+"));
                        sb.append(StringPool.SLASH + fileEntry.getUuid());
                        metaDefaultImageUrl = sb.toString();
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("L'application ne trouve pas le fichier meta par défaut nommé "
                        + LmdePortalProperties.LMDE_META_DEFAULT_FILE_NAME + " dans le répertoire "
                        + LmdePortalProperties.LMDE_META_FOLDER_NAME, e);
            }

            template.put("lmdeMetaDefautImage", metaDefaultImageUrl);

            addInformationUtilisateur(template, request);

            // Accessibility
            addAsscessibilityInfo(template, request);

            // Cookie CNIL
            Cookie[] cookies = request.getCookies();
            boolean hasCookieCNIL = false;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (LmdePortalProperties.LMDE_COOKIE_CNIL_NAME.equals(cookie.getName())) {
                        hasCookieCNIL = true;
                        break;
                    }
                }
            }

            if (!hasCookieCNIL) {
                // La durée de vie
                Calendar c = Calendar.getInstance();
                long startTime = c.getTimeInMillis();
                c.add(Calendar.MONTH, LmdePortalProperties.LMDE_COOKIE_CNIL_MAX_AGE);
                long endTime = c.getTimeInMillis();
                long diffMillis = endTime - startTime;

                template.put("cookieCNILName", LmdePortalProperties.LMDE_COOKIE_CNIL_NAME);
                template.put("cookieCNILHelpPageLink", LmdePortalProperties.LMDE_COOKIE_CNIL_HELP_PAGE_LINK);
                template.put("cookieCNILMaxAgeDiff", diffMillis);
            }
            template.put("hasCookieCNIL", hasCookieCNIL);

        } catch (SystemException | PortalException e) {
            LOGGER.error("Erreur lors de l'intialisation du contexte velocity : " + e.getMessage());
        }

        // Ajout des paths, vers les CSS/JS customs de la ged, au template velocity
        addCustomFiles(template, request);
        
        addStickBarItems(template);
    }

    /**
     * Ajoute les items pour la barre stick to scroll au contexte du template velocity.
     * 
     * @param template
     */
	private void addStickBarItems(Template template) {
		// Barre de contact scroll
        try {
			final List<StickBarItem> stickBarItems = StickBarItemLocalServiceUtil.getStickBarItems(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			final List<StickBarItem> resultsBarre1 = new ArrayList<StickBarItem>();
			final List<StickBarItem> resultsBarre2 = new ArrayList<StickBarItem>();
			final List<StickBarItem> resultsBarre3 = new ArrayList<StickBarItem>();
			
			for (final StickBarItem item : stickBarItems) {
				if (item.isActive() && item.getBarreType().equals("1")) {
					resultsBarre1.add(item);
				} else if (item.isActive() && item.getBarreType().equals("2")) {
					resultsBarre2.add(item);
				}  else if (item.isActive() && item.getBarreType().equals("3")) {
					resultsBarre3.add(item);
				} 
			}
			Collections.sort(resultsBarre1, new StickBarItemPositionComparator());
			Collections.sort(resultsBarre2, new StickBarItemPositionComparator());
			Collections.sort(resultsBarre3, new StickBarItemPositionComparator());
			template.put("stickBarItemsBarre1", resultsBarre1);
			template.put("stickBarItemsBarre2", resultsBarre2);
			template.put("stickBarItemsBarre3", resultsBarre3);
		} catch (final SystemException e) {
			LOGGER.error("Erreur lors de l'intialisation du contexte velocity : " + e.getMessage());
		}
	}

    /**
     * Ajoute les chemins des css/js customs (contenu dans la ged) au contexte du template velocity.
     * 
     * @param template
     * @param request
     */
    private void addCustomFiles(final Template template, final HttpServletRequest request) {

        final long lmdeGroupId = LmdePortalUtil.getLmdeGroupId();
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        long currentGroupId = 0;
        if (Validator.isNotNull(themeDisplay.getLayout())) {
            currentGroupId = themeDisplay.getLayout().getGroupId();
        }

        /*
         * Si on est sur le site LMDE, alors on charge les fichiers customs (css/js) stockés dans la ged. Inutile de les
         * charger si on est dans le control panel par exemple.
         */
        if (currentGroupId > 0 && lmdeGroupId > 0 && lmdeGroupId == currentGroupId) {

            Folder customPortalFolder = null, customThemeFolder = null;
            try {
                // Récupération du dossier custom principal (Portal level)
                customPortalFolder = DLAppServiceUtil
                        .getFolder(lmdeGroupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
                                LmdePortalProperties.LMDE_CUSTOM_PORTAL_FOLDER_NAME);

                String currentThemeId = themeDisplay.getThemeId();
                String folderThemeName = StringPool.BLANK;

                if (Validator.isNotNull(customPortalFolder)) {

                    if (LmdePortalProperties.LMDE_MAIN_THEME_ID.equalsIgnoreCase(currentThemeId)) {
                        folderThemeName = LmdePortalProperties.LMDE_CUSTOM_MAIN_THEME_FOLDER_NAME;

                    } else if (LmdePortalProperties.LMDE_HOME_THEME_ID.equalsIgnoreCase(currentThemeId)) {
                        folderThemeName = LmdePortalProperties.LMDE_CUSTOM_HOME_THEME_FOLDER_NAME;

                    } else if (LmdePortalProperties.LMDE_ESPACE_PERSONNEL_THEME_ID.equalsIgnoreCase(currentThemeId)) {
                        folderThemeName = LmdePortalProperties.LMDE_CUSTOM_ESPACE_PERSONNEL_THEME_FOLDER_NAME;
                    }

                    if (Validator.isNotNull(folderThemeName)) {
                        // Récupération du dossier custom subsidiaire (Theme level)
                        customThemeFolder = DLAppServiceUtil.getFolder(lmdeGroupId, customPortalFolder.getFolderId(),
                                folderThemeName);
                    }
                }
            } catch (PortalException e) {
                LOGGER.warn("Impossible de récupérer un ou plusieurs dossiers de la ged contenant les css/js customs. Vérifier l'existence du dossier "
                        + LmdePortalProperties.LMDE_CUSTOM_PORTAL_FOLDER_NAME
                        + " et des ses sous-dossiers "
                        + LmdePortalProperties.LMDE_CUSTOM_HOME_THEME_FOLDER_NAME
                        + ", "
                        + LmdePortalProperties.LMDE_CUSTOM_MAIN_THEME_FOLDER_NAME
                        + ", "
                        + LmdePortalProperties.LMDE_CUSTOM_ESPACE_PERSONNEL_THEME_FOLDER_NAME);
            } catch (SystemException e) {
                LOGGER.error("Impossible de récupérer le dossier de la ged contenant les css/js customs.", e);
            }

            // On ajoute les paths des fichies js et css trouvés dans les dossiers
            List<String> customCssFilesPath = new ArrayList<>(), customJsFilesPath = new ArrayList<>();
            addFoldersFilesToList(themeDisplay, lmdeGroupId, customPortalFolder, customCssFilesPath, customJsFilesPath);
            addFoldersFilesToList(themeDisplay, lmdeGroupId, customThemeFolder, customCssFilesPath, customJsFilesPath);

            template.put("customCssFilesPath", customCssFilesPath);
            template.put("customJsFilesPath", customJsFilesPath);

        }
    }

    /**
     * Ajoute les paths des fichiers JS ou CSS, trouvés dans le dossier passé en paramètre, à la liste adéquate.
     * 
     * @param themeDisplay
     * @param groupId
     * @param folder le dossier
     * @param customCssFilesPath la liste des path des fichiers CSS
     * @param customJsFilesPath la liste des path des fichiers JS
     */
    private void addFoldersFilesToList(final ThemeDisplay themeDisplay, final long groupId, final Folder folder,
            List<String> customCssFilesPath, List<String> customJsFilesPath) {

        if (Validator.isNotNull(folder)) {

            try {
                // On récupère tous les path des fichiers js et css contenu dans le dossier
                for (FileEntry fileEntry : DLAppServiceUtil.getFileEntries(groupId, folder.getFolderId())) {

                    if (StringUtil.equalsIgnoreCase("css", fileEntry.getExtension())) {
                        // Le fichier contenu dans le dossier est un fichier css, on l'ajoute
                        customCssFilesPath.add(DLUtil.getPreviewURL(fileEntry, fileEntry.getLatestFileVersion(),
                                themeDisplay, "", true, true));
                    }
                    if (StringUtil.equalsIgnoreCase("js", fileEntry.getExtension())) {
                        // Le fichier contenu dans le dossier est un fichier js, on l'ajoute
                        customJsFilesPath.add(DLUtil.getPreviewURL(fileEntry, fileEntry.getLatestFileVersion(),
                                themeDisplay, "", true, true));
                    }
                }
            } catch (SystemException | PortalException e) {
                LOGGER.error("Impossible de récupérer les fichiers du dossier " + folder.getFolderId()
                        + " + dans le repository " + groupId, e);
            }
        }
    }

    /**
     * Ajoute les pages d'outils
     * 
     * @param template
     * @param request
     * @param lmdeGroupId
     * @throws PortalException
     * @throws SystemException
     */
    private void addToolsPages(Template template, HttpServletRequest request, final Long lmdeGroupId)
            throws PortalException, SystemException {

        // Ajout des pages d'outils
        try {
            // La page du plan du site
            Layout planDuSiteNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_PLAN_DU_SITE);
            template.put("lmdePlanDuSiteFriendlyUrl", planDuSiteNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_PLAN_DU_SITE + " non existante");
        }
        try {
            // La page de FAQ
            Layout faqNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_FAQ);
            template.put("lmdeFaqFriendlyUrl", faqNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_FAQ + " non existante");
        }
        try {
            // La page de lexique
            Layout lexiqueNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_LEXIQUE);
            template.put("lmdeLexiqueFriendlyUrl", lexiqueNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_LEXIQUE + " non existante");
        }
        try {
            // La page d'aide
            Layout aideNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_PAGE_GENERAL_AIDE_FRIENDLY_URL);
            template.put("lmdeAideUrl", aideNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_PAGE_GENERAL_AIDE_FRIENDLY_URL + " non existante");
        }
    }

    /**
     * Ajoute les pages du bas du footer
     * 
     * @param template
     * @param request
     * @param lmdeGroupId
     * @throws PortalException
     * @throws SystemException
     */
    private void addBottomFooterPages(Template template, HttpServletRequest request, final Long lmdeGroupId)
            throws PortalException, SystemException {

        try {
            // La page des mentions legales
            Layout mentionsLegalesNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_MENTIONS_LEGALES);
            template.put("lmdeMentionsLegalesNavigationLayout", mentionsLegalesNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_MENTIONS_LEGALES + " non existante");
        }
        try {
            // La page des statuts
            Layout statutsNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_STATUTS);
            template.put("lmdeStatutsFriendlyUrl", statutsNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_STATUTS + " non existante");
        }
        try {
            // La page des partenaires
            Layout partenairesNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_PARTENAIRES);
            template.put("lmdePartenairesFriendlyUrl", partenairesNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_PARTENAIRES + " non existante");
        }
        try {
            // La page des professionnels
            Layout professionnelsNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_PROFESSIONNELS);
            template.put("lmdeProfessionnelsFriendlyUrl", professionnelsNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_PROFESSIONNELS + " non existante");
        }
        try {
            // La page des cookies
            Layout cookiesNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_COOKIES);
            template.put("lmdeCookiesFriendlyUrl", cookiesNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_COOKIES + " non existante");
        }
        try {
            // La page des données personnelles
            Layout donneesPersoNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_DONNEES_PERSO);
            template.put("lmdeDonneesPersoFriendlyUrl", donneesPersoNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_DONNEES_PERSO + " non existante");
        }
    }

    /**
     * Ajoute les liens disponibles dans les différentes listes du footer
     * 
     * @param template
     * @param request
     * @param lmdeGroupId
     * @throws PortalException
     * @throws SystemException
     */
    private void addFooterLinksPages(Template template, HttpServletRequest request, final Long lmdeGroupId)
            throws PortalException, SystemException {

        // Ajout des listes de liens
        try {
            Layout liste1Lien1Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN1_URL);
            template.put("liste1Lien1Layout", liste1Lien1Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN1_URL + " non existante");
        }
        try {
            Layout liste1Lien2Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN2_URL);
            template.put("liste1Lien2Layout", liste1Lien2Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN2_URL + " non existante");
        }
        try {
            Layout liste1Lien3Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN3_URL);
            template.put("liste1Lien3Layout", liste1Lien3Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN3_URL + " non existante");
        }
        try {
            Layout liste1Lien4Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN4_URL);
            template.put("liste1Lien4Layout", liste1Lien4Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN4_URL + " non existante");
        }
        try {
            Layout liste1Lien5Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN5_URL);
            template.put("liste1Lien5Layout", liste1Lien5Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN5_URL + " non existante");
        }
        try {
            Layout liste1Lien6Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN6_URL);
            template.put("liste1Lien6Layout", liste1Lien6Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN6_URL + " non existante");
        }
        try {
            Layout liste1Lien7Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN7_URL);
            template.put("liste1Lien7Layout", liste1Lien7Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST1_LIEN7_URL + " non existante");
        }
        try {
            Layout liste2Lien1Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN1_URL);
            template.put("liste2Lien1Layout", liste2Lien1Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN1_URL + " non existante");
        }
        try {
            Layout liste2Lien2Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN2_URL);
            template.put("liste2Lien2Layout", liste2Lien2Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN2_URL + " non existante");
        }
        try {
            Layout liste2Lien3Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN3_URL);
            template.put("liste2Lien3Layout", liste2Lien3Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN3_URL + " non existante");
        }
        try {
            Layout liste2Lien4Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN4_URL);
            template.put("liste2Lien4Layout", liste2Lien4Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST2_LIEN4_URL + " non existante");
        }
        try {
            Layout liste3Lien1Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST3_LIEN1_URL);
            template.put("liste3Lien1Layout", liste3Lien1Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST3_LIEN1_URL + " non existante");
        }
        try {
            Layout liste3Lien2Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST3_LIEN2_URL);
            template.put("liste3Lien2Layout", liste3Lien2Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST3_LIEN2_URL + " non existante");
        }
        try {
            Layout liste3Lien3Layout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FOOTER_LIST3_LIEN3_URL);
            template.put("liste3Lien3Layout", liste3Lien3Layout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FOOTER_LIST3_LIEN3_URL + " non existante");
        }
    }

    /**
     * Ajoute les pages privées pour la navigation dans le header
     * 
     * @param template
     * @param request
     * @param lmdeGroupId
     * @throws PortalException
     * @throws SystemException
     */
    private void addRecherchePlIds(Template template, HttpServletRequest request, final Long lmdeGroupId)
            throws PortalException, SystemException {

        try {
            // La page de recherche
            Layout rechercheNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_RECHERCHE);
            template.put("rechercheNavigationLayoutPlId", rechercheNavigationLayout.getPlid());
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_RECHERCHE + " non existante");
        }
        try {
            // La page de recherche de documents
            Layout rechercheDocumentsNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_RECHERCHE_DOCUMENTS);
            template.put("rechercheDocumentsNavigationLayoutPlId", rechercheDocumentsNavigationLayout.getPlid());
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_RECHERCHE_DOCUMENTS + " non existante");
        }
    }

    /**
     * Ajoute les pages de contact pour la navigation dans le footer
     * 
     * @param template
     * @param request
     * @param lmdeGroupId
     * @throws PortalException
     * @throws SystemException
     */
    private void addContactPages(Template template, HttpServletRequest request, final Long lmdeGroupId)
            throws PortalException, SystemException {
        try {
            Layout contacteMailNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_EMAIL);
            template.put("contacteMailNavigationLayout", contacteMailNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_EMAIL + " non existante");
        }

        try {
            Layout contactTelephoneNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_TELEPHONE);
            template.put("contactTelephoneNavigationLayout", contactTelephoneNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_TELEPHONE + " non existante");
        }
        try {
            Layout contactWebCallBackNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_WEBCALLBACK);
            template.put("contactWebCallBackNavigationLayout",
                    contactWebCallBackNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_WEBCALLBACK + " non existante");
        }
        try {
            Layout contactAgencesNavigationLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(lmdeGroupId, false,
                    LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_AGENCES);
            template.put("contactAgencesNavigationLayout", contactAgencesNavigationLayout.getRegularURL(request));
        } catch (NoSuchLayoutException e) {
            LOGGER.debug("Page " + LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_CONTACT_AGENCES + " non existante");
        }
    }

    /**
     * Ajoute les informations de l'utilisateur au template
     * 
     * @param template
     * @param request
     * @throws SystemException
     */
    private void addInformationUtilisateur(Template template, HttpServletRequest request) throws SystemException {
        // information de l'utilisateur
        final ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        final User user = LmdePortalUtil.getConnectedUser(themeDisplay);
        boolean isAdherent = false;
        boolean isAffilie = false;
        boolean isProspect = false;
        boolean isRadie = false;
        boolean isPreAdherent = false;
        String lmdeRole = "";        
        if (user != null) {
            template.put("lmdeConnectedUser", user);
            boolean hasLastLoginDate = false;
            if (user.getLastLoginDate() != null) {
                hasLastLoginDate = true;
            }
            for (Role role : user.getRoles()) {
                if (role.getName().equals(RoleUtil.ROLE_ADHERENT_LMDE_NOM)) {
                    isAdherent = true;
                    lmdeRole = LMDEEnums.LMDERole.ADHERENT.name();
                } else if (role.getName().equals(RoleUtil.ROLE_AFFILIE_LMDE_NOM)
                        || role.getName().equals(RoleUtil.ROLE_ASSURE_LMDE_NOM)) {
                    isAffilie = true;
                    lmdeRole = LMDEEnums.LMDERole.AFFILIE.name();
                } else if (role.getName().equals(RoleUtil.ROLE_PROSPECT_LMDE_NOM)) { 
                    isProspect = true;
                    lmdeRole = LMDEEnums.LMDERole.PROSPECT.name();
                } else if (role.getName().equals(RoleUtil.ROLE_RADIE_LMDE_NOM)) {
                    isRadie = true;
                    lmdeRole = LMDEEnums.LMDERole.RADIE.name();
                } else if (role.getName().equals(RoleUtil.ROLE_PRE_ADHERENT_LMDE_NOM)) {
                    isPreAdherent = true;
                    lmdeRole = LMDEEnums.LMDERole.PRE_ADHERENT.name();
                }
            }
            template.put("hasLastLoginDate", hasLastLoginDate);
            template.put("isAdherent", isAdherent);
            template.put("isAffilie", isAffilie);
            template.put("isProspect", isProspect);
            template.put("isRadie", isRadie);
            template.put("isPreAdherent", isPreAdherent);
            
            // LMDE-705
            template.put("lmdeRole", "");
        	// LMDE-706
            template.put("lmdeContrat", "");
            
            // Recherche du contrat actif uniquement si le rôle de l'utilisateur connecté correspond à un rôle LMDE
            if (!"".equals(lmdeRole)) {
            	// LMDE-705
                template.put("lmdeRole", "type-" + lmdeRole);
            	// LMDE-706
				try {
					ContratBean contrat = ContratServiceUtil.getActiveContratByIdPersonne(user.getScreenName().toUpperCase());
					template.put("lmdeContrat", ((contrat == null)?"":"contrat-" + LMDEOffres.getOffreByLabel(contrat.getNomContrat())));
				} catch (LmdeServiceContratException e) {
					LOGGER.error(e);
				}
//				setDisplayFiabilisationEmailMessage(template, request, user);
            }
        }
    }

    /**
     * 
     * @param template
     * @param request
     * @param user
     */
	private void setDisplayFiabilisationEmailMessage(final Template template, final HttpServletRequest request, final User user) {
		// LMDE-892
		final String lmdeShowPopinEmail = (String) request.getSession().getAttribute("lmdeShowPopinEmailAnswered");
		if (lmdeShowPopinEmail == null) {
			try {
				final PersonneBean personne = PersonneServiceUtil.getPersonneById(user.getScreenName().toUpperCase());
				if (personne.geteMail() != null && personne.geteMail().getDtDebutValid() != null) {
					final double diffAsDays = LmdeDateUtil.getDaysBetweenDates(personne.geteMail().getDtDebutValid(), Calendar.getInstance().getTime());
					if (diffAsDays > LmdePortalProperties.LMDE_POPIN_EMAIL_NUMBER_OF_DAYS) {
						// Email non modifié depuis X jours
						// Vérifier qu'il n'a pas répondu "Oui"
						final String userEmailChecked = String.valueOf(user.getExpandoBridge().getAttribute("userEmailChecked"));
						if (userEmailChecked == null || !"true".equals(userEmailChecked)) {
							template.put("lmdeShowPopinEmail", "true");
							request.getSession().setAttribute("lmdeShowPopinEmail", "true");
						} else {
							request.getSession().setAttribute("lmdeShowPopinEmail", "false");
						}
					}
				}
			} catch (final LmdeServicePersonneException e) {
				LOGGER.error(e);
			}
		}
	}

    /**
     * Retourne les différentes pages publiques utilisées pour la navigation
     * 
     * @param lmdeGroupId l'id du groupe LMDE
     * @return la liste des pages publiques concernées et leurs fils
     * @throws PortalException
     * @throws SystemException
     */
    private List<Layout> getPagesPubliquesNavigation(final Long lmdeGroupId) throws PortalException, SystemException {

        final List<Layout> publicNavigationLayout = new ArrayList<>();
        final Set<String> navigationLayoutFriendlyUrls = new HashSet<String>();
        final List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(lmdeGroupId, false,
                LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

        // La page de securite sociale etudiante
        navigationLayoutFriendlyUrls.add(LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_SECURITE_SOCIALE_ETUDIANTE);
        // La page de mutuelle sante
        navigationLayoutFriendlyUrls.add(LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_MUTUELLE_SANTE);
        // La page de prevention
        navigationLayoutFriendlyUrls.add(LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_PREVENTION);
        // La page d'avantages
        navigationLayoutFriendlyUrls.add(LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_AVANTAGES);
        // La page de la LMDE
        navigationLayoutFriendlyUrls.add(LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_LA_LMDE);
        // LMDE-798 : La page Actus et Bons plans
        navigationLayoutFriendlyUrls.add(LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_ACTUS_ET_BONS_PLANS);
        // LMDE-804 : La page Assurance et Banque
        navigationLayoutFriendlyUrls.add(LmdePortalProperties.LMDE_FRIENDLY_URL_PAGE_ASSURANCE_ET_BANQUE);

        for (Layout layout : layouts) {
            if (!layout.getHidden() && navigationLayoutFriendlyUrls.contains(layout.getFriendlyURL())) {
                publicNavigationLayout.add(layout);
            }
        }

        return publicNavigationLayout;
    }
    
    /**
     * Retourne les différentes pages privées utilisées pour la navigation
     * 
     * @param lmdeGroupId l'id du groupe LMDE
     * @param themeDisplay 
     * @return la liste des pages privées concernées
     * @throws PortalException
     * @throws SystemException
     */
    private List<Layout> getPagesPriveesNavigation(final Long lmdeGroupId, final ThemeDisplay themeDisplay) throws PortalException, SystemException {

        final List<Layout> privateNavigationLayout = new ArrayList<>();
        final List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(lmdeGroupId, true,
                LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

        for (final Layout layout : layouts) {
            if (!layout.getHidden() && LayoutPermissionUtil.contains(themeDisplay.getPermissionChecker(), layout, ActionKeys.VIEW)) {
                privateNavigationLayout.add(layout);
            }
        }

        return privateNavigationLayout;
    }

    /**
     * Ajoute les informations d'accessibility
     * 
     * @param template
     * @param request
     * @throws SystemException
     * @throws PortalException
     */
    private void addAsscessibilityInfo(Template template, HttpServletRequest request) throws SystemException,
            PortalException {
        // information de l'utilisateur
        final ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        Layout layout = themeDisplay.getLayout();

        // (parentPageTitle - )*pageTitle
        StringBuilder layoutTitleChain = new StringBuilder();

        if (layout != null) {

            layoutTitleChain.append(layout.getHTMLTitle(themeDisplay.getLocale()));
            while (layout.getParentLayoutId() != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
                try {
                    layout = LayoutLocalServiceUtil.getParentLayout(layout);
                    layoutTitleChain.insert(0, layout.getHTMLTitle(themeDisplay.getLocale()) + " - ");
                } catch (NoSuchLayoutException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Page (layoutId=" + layout.getLayoutId() + ") non existante");
                    }
                }
            }

        }

        template.put("the_title_chain", layoutTitleChain.toString());
    }
    
    /**
     * Retourne vrai si l'adhérent connecté est renouvelant et n'a pas encore fait son choix.
     * @param request
     * @return
     */
    private boolean isRenouvelantAndNonPrononce(final HttpServletRequest request) {
        final ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    	final User user = LmdePortalUtil.getConnectedUser(themeDisplay);
    	if (user != null) {
	    	try {
				final PersonneBean personne = PersonneServiceUtil.getPersonneById(user.getScreenName().toUpperCase());
				if (personne != null) {
					return RenouvellementServiceUtil.isRenouvelantAndNonPrononce(personne);
				}
			} catch (final LmdeServiceException e) {
				LOGGER.error(e);
			}
    	}
    	return false;
    }
}