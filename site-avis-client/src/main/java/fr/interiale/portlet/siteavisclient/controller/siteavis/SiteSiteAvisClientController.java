package fr.interiale.portlet.siteavisclient.controller.siteavis;

import java.util.Collections;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

import fr.interiale.common.portal.constants.IAuthConstantes;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireLocalServiceUtil;
import fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO;
import fr.interiale.portlet.siteavisclient.util.InterialeAvisCommentaireDateCreationAscComparator;
import fr.interiale.portlet.siteavisclient.util.InterialeAvisCommentaireDateCreationComparator;
import fr.interiale.portlet.siteavisclient.util.InterialeAvisCommentaireNotesComparator;
import fr.interiale.portlet.siteavisclient.util.InterialeAvisCommentaireNotesDescComparator;
import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

/**
 * Controller du portlet Site Avis Client
 * 
 * @author Antoine Comble (cckc3018)
 */
@Controller
@RequestMapping("VIEW")
public class SiteSiteAvisClientController {

	private static final Logger LOGGER = Logger.getLogger(SiteSiteAvisClientController.class);

	/**
     * Handler de l'action view.
     * 
     * @param request
     * @param response
     * @param model
     * @return
     * @throws SystemException
     * @throws LmdeServiceException
     */
    @RenderMapping(params = "action=viewAfterSendAvis")
    public String viewAfterSendMessage(final Model model, final RenderRequest request, final RenderResponse response) throws SystemException {
        model.addAttribute("avisEnvoye", true);
        return view(request, response, model);
    }
	
	/**
	 * Affiche le portlet relevé de prestations.
	 * 
	 * @param portletRequest
	 * @param portletResponse
	 * @param m
	 * @return
	 * @throws InterneLmdeUserException
	 */
	@RenderMapping
	public String view(final PortletRequest portletRequest, final PortletResponse portletResponse, Model m) {
		// Ajout des données par défaut
		addDefaultData(portletRequest, m);

		final String sort = ParamUtil.getString(portletRequest, SiteAvisClientUtil.SORT_PARAM_NAME);
		
		m.addAttribute("avisSortSelected", sort);
		
		try {
			// Récupération de tous les commentaires
			final List<InterialeAvisCommentaire> listeAvis = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaires(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			if (listeAvis != null && !listeAvis.isEmpty()) {
				final List<InterialeAvisCommentaireFO> listeAvisFO = SiteAvisClientUtil.map(listeAvis, true);
				
				// Trier du + récent au + ancien par défaut
				if (sort == null || sort.isEmpty() || SiteAvisClientUtil.SORT_DATE_CREATION_DESC_PARAM_VALUE.equals(sort)) { 
					Collections.sort(listeAvisFO, new InterialeAvisCommentaireDateCreationComparator());
				} else if (SiteAvisClientUtil.SORT_DATE_CREATION_ASC_PARAM_VALUE.equals(sort)) { 
					Collections.sort(listeAvisFO, new InterialeAvisCommentaireDateCreationAscComparator());
				} else if (SiteAvisClientUtil.SORT_NOTES_ASC_PARAM_VALUE.equals(sort)) { 
					Collections.sort(listeAvisFO, new InterialeAvisCommentaireNotesComparator());
				} else {
					Collections.sort(listeAvisFO, new InterialeAvisCommentaireNotesDescComparator());
				}
				
				// Récupérer les réponses
				SiteAvisClientUtil.addReponsesByCommentaire(listeAvisFO);
				
				m.addAttribute("total", listeAvisFO.size());
	            int pageId = ParamUtil.getInteger(portletRequest, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_CUR);
	            int start = SiteAvisClientUtil.NB_AVIS_PER_PAGE * (pageId - 1);
	            int end = SiteAvisClientUtil.NB_AVIS_PER_PAGE * pageId;
	            if (end > listeAvisFO.size()) {
	                end = listeAvisFO.size();
	            }
				m.addAttribute("listeAvis", listeAvisFO.subList(start, end));
			} else {
				m.addAttribute("listeAvis", null);
			}
		} catch (SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la récupération des avis", e);
		}
		
		return SiteAvisClientUtil.SITE_AVIS_VIEW;
	}
	
	/**
	 * Add default data to model.
	 * 
	 * @param portletRequest
	 * @param m
	 */
    private void addDefaultData(final PortletRequest portletRequest, Model m) {
    	Group globalGroup = null;
		AssetVocabulary vocabulary = null;
		List<AssetCategory> categories = null;
		
		m.addAttribute("delta", SiteAvisClientUtil.NB_AVIS_PER_PAGE);
		
		try {
			globalGroup = GroupLocalServiceUtil.getGroup(IAuthConstantes.COMPAGNY_ID, String.valueOf(IAuthConstantes.COMPAGNY_ID));
		} catch (PortalException | SystemException e) {
			LOGGER.error("Impossible de récupérer le group Global", e);
		}
		
		if (globalGroup != null) {
			try {
				// Récupération du vocabulaire pour les notes
				vocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(globalGroup.getGroupId(), SiteAvisClientUtil.NOTES_VOCABULARY_NAME);
			} catch (PortalException | SystemException e) {
				LOGGER.error("Impossible de récupérer le vocabulaire pour les notes", e);
			}
			
			if (vocabulary != null) {
				try {
					categories = AssetCategoryLocalServiceUtil.getVocabularyCategories(vocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
				} catch (SystemException e) {
					LOGGER.error("Impossible de récupérer les catégorie pour les notes", e);
				}
			}
			
			if (categories == null || categories.isEmpty()) {
				// Aucune catégorie trouvée pour les notes
				return;
			}
		}
		
		for (final AssetCategory category : categories) {
			if (SiteAvisClientUtil.NOTES_CATEGORY_NAME_PRODUITS.equals(category.getName())) {
				portletRequest.getPortletSession().setAttribute("categorieProduits", category);
				m.addAttribute("categorieProduits", category);
			}
			if (SiteAvisClientUtil.NOTES_CATEGORY_NAME_RELATION_ADHERENT.equals(category.getName())) {
				portletRequest.getPortletSession().setAttribute("categorieRelationAdherent", category);
				m.addAttribute("categorieRelationAdherent", category);
			}
			if (SiteAvisClientUtil.NOTES_CATEGORY_NAME_ACTIONS_PREVENTION.equals(category.getName())) {
				portletRequest.getPortletSession().setAttribute("categorieActionsPrevention", category);
				m.addAttribute("categorieActionsPrevention", category);
			}
		}
    }
}
