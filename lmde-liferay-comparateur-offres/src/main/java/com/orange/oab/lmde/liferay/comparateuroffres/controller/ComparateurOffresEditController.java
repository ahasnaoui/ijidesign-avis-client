package com.orange.oab.lmde.liferay.comparateuroffres.controller;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.orange.oab.lmde.liferay.comparateuroffres.business.ComparateurOffresBusinessService;
import com.orange.oab.lmde.liferay.comparateuroffres.form.EditForm;
import com.orange.oab.lmde.liferay.comparateuroffres.util.ComparateurOffresConstants;
import com.orange.oab.lmde.service.bean.GammeBean;
import com.orange.oab.lmde.service.exception.LmdeServiceOffreException;

@Controller
@RequestMapping("EDIT")
public class ComparateurOffresEditController {

    private static final Logger LOGGER = Logger.getLogger(ComparateurOffresEditController.class);

    @Autowired
    private ComparateurOffresBusinessService comparateurOffresBusinessService;

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

        try {
            List<GammeBean> listeGammes = comparateurOffresBusinessService.listerGammes();
            model.addAttribute("gammes", listeGammes);
            EditForm editForm = new EditForm();
            PortletPreferences pref = request.getPreferences();
            String codeGamme = pref.getValue(ComparateurOffresConstants.PREFERENCE_CODE_GAMME, "");
            editForm.setCodeGamme(codeGamme);
            model.addAttribute("editForm", editForm);
            model.addAttribute("editFormError", "false");
        } catch (LmdeServiceOffreException e) {
            LOGGER.error("Une erreur est survenue lors de la récupération de la liste des gammes ", e);
            model.addAttribute("editFormError", "true");
        }
        return ComparateurOffresConstants.EDIT;
    }

    @ActionMapping(value = "submitEdit")
    public void submitSituation(final PortletRequest portletRequest, final ActionResponse actionResponse, @ModelAttribute("editForm") EditForm editForm) {

        PortletPreferences pref = portletRequest.getPreferences();

        try {
            pref.setValue(ComparateurOffresConstants.PREFERENCE_CODE_GAMME, editForm.getCodeGamme());
            pref.store();
            actionResponse.setRenderParameter("afterSave", "true");
            comparateurOffresBusinessService.invalidateCache();
        } catch (ReadOnlyException | ValidatorException | IOException e) {
            LOGGER.error("Une erreur est survenue lors de la sauvegarde des préférences", e);
            actionResponse.setRenderParameter("editFormSaveError", "true");
        }

    }
}
