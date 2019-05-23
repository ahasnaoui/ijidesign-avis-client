package com.orange.oab.lmde.liferay.comparateuroffres.controller;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.orange.oab.lmde.liferay.comparateuroffres.business.ComparateurOffresBusinessService;
import com.orange.oab.lmde.liferay.comparateuroffres.form.OffreForm;
import com.orange.oab.lmde.liferay.comparateuroffres.util.ComparateurOffresConstants;
import com.orange.oab.lmde.service.exception.LmdeServiceOffreException;
import com.orange.oab.lmde.util.portal.LmdePortalProperties;

@Controller("comparateurOffresController")
@RequestMapping("VIEW")
@SessionAttributes({ "listeOffres" })
public class ComparateurOffresController {

    private static final Logger LOGGER = Logger.getLogger(ComparateurOffresController.class);

    @Autowired
    private ComparateurOffresBusinessService comparateurOffresBusinessService;

    @ModelAttribute("listeOffres")
    public OffreForm addOffreFormToSessionScope() {
        return new OffreForm();
    }

    /**
     * Méthode de render par défaut.
     * 
     * @param portletRequest
     * @param portletResponse
     * @param m
     * @return view
     */
    @RenderMapping
    public String view(final RenderRequest request, final RenderResponse response, final Model model) {

        ThemeDisplay themeDisplay = null;
        if (request != null) {
            themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        }

        // Récupération du code gamme
        String codeGamme = null;
        String offre = ParamUtil.getString(PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(request)),
                LmdePortalProperties.LMDE_URL_PARAMETER_NAME_OFFRE);
        String millesime = ParamUtil.getString(PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(request)),
                LmdePortalProperties.LMDE_URL_PARAMETER_NAME_MILLESIME);

        if (Validator.isNull(offre) || Validator.isNull(millesime)) {
            codeGamme = ParamUtil.getString(PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(request)),
                    LmdePortalProperties.LMDE_URL_PARAMETER_NAME_GAMME);
            if (Validator.isNull(codeGamme)) {
                PortletPreferences pref = request.getPreferences();
                codeGamme = pref.getValue(ComparateurOffresConstants.PREFERENCE_CODE_GAMME, "");
            }
        } else {
            try {
                codeGamme = comparateurOffresBusinessService.getGammeFromOffre(offre, millesime);
            } catch (LmdeServiceOffreException e) {
                LOGGER.error("Une erreur est survenue en récupérant les informations de l'offre entrée en paramètre. Offre : " + offre + ", Millesime : "
                        + millesime, e);
            }
        }
        // Une fois le code gamme récupéré :
        if (!Validator.isBlank(codeGamme)) {
            try {
                OffreForm listeOffres = comparateurOffresBusinessService.listerOffresFromGamme(codeGamme, themeDisplay, offre);
                model.addAttribute("listeOffres", listeOffres);
            } catch (LmdeServiceOffreException | SystemException e) {
                LOGGER.error("Une erreur est survenue en récupérant les offres de la gamme " + codeGamme, e);
                model.addAttribute("error", "error");
            }
        } else {
            LOGGER.error("La gamme n'a pas pu être trouvée dans les paramètres de l'URL ou dans les préférences du portlet");
        }

        return ComparateurOffresConstants.VIEW;
    }

}
