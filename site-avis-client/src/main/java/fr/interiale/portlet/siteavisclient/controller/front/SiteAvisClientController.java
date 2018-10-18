package fr.interiale.portlet.siteavisclient.controller.front;

import java.util.Collections;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

import fr.interiale.common.portal.constants.IAuthConstantes;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireLocalServiceUtil;
import fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO;
import fr.interiale.portlet.siteavisclient.util.InterialeAvisCommentaireDateCreationComparator;
import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

/**
 * Controller du portlet Site Avis Client
 * 
 * @author Antoine Comble (cckc3018)
 */
@Controller
@RequestMapping("VIEW")
public class SiteAvisClientController {

	private static final Logger LOGGER = Logger.getLogger(SiteAvisClientController.class);

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
		
		try {
			// Récupération de tous les commentaires
			final List<InterialeAvisCommentaire> listeAvis = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaires(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			if (listeAvis != null && !listeAvis.isEmpty()) {
				final List<InterialeAvisCommentaireFO> listeAvisFO = SiteAvisClientUtil.mapForInteriale(listeAvis);
				
				// Trier du + récent au + ancien par défaut
				Collections.sort(listeAvisFO, new InterialeAvisCommentaireDateCreationComparator());
				
				m.addAttribute("total", listeAvisFO.size());
				
	            final int pageId = ParamUtil.getInteger(portletRequest, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_CUR);
	            final int start = SiteAvisClientUtil.NB_AVIS_PER_PAGE * (pageId - 1);
	            int end = SiteAvisClientUtil.NB_AVIS_PER_PAGE * pageId;
	            if (end > listeAvisFO.size()) {
	                end = listeAvisFO.size();
	            }
				m.addAttribute("listeAvis", listeAvisFO.subList(start, end));
			} else {
				m.addAttribute("listeAvis", null);
			}
		} catch (final SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la récupération des avis", e);
		}
		
		return SiteAvisClientUtil.VIEW;
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

			try {
				categories = AssetCategoryLocalServiceUtil.getVocabularyCategories(vocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
			} catch (final SystemException e) {
				LOGGER.error("Impossible de récupérer les catégorie pour les notes", e);
			}
						
			if (categories == null || categories.isEmpty()) {
				// Aucune catégorie trouvée pour les notes : création des catégories
		        try {
					final AssetCategory cat1 = AssetCategoryLocalServiceUtil.createAssetCategory(CounterLocalServiceUtil.increment(AssetCategory.class.getName(), 1));
					cat1.setName(SiteAvisClientUtil.NOTES_CATEGORY_NAME_PRODUITS);
					cat1.setVocabularyId(vocabulary.getVocabularyId());
					cat1.setGroupId(globalGroup.getGroupId());
					AssetCategoryLocalServiceUtil.updateAssetCategory(cat1);
					
					final AssetCategory cat2 = AssetCategoryLocalServiceUtil.createAssetCategory(CounterLocalServiceUtil.increment(AssetCategory.class.getName(), 1));
					cat2.setName(SiteAvisClientUtil.NOTES_CATEGORY_NAME_RELATION_ADHERENT);
					cat2.setVocabularyId(vocabulary.getVocabularyId());
					cat2.setGroupId(globalGroup.getGroupId());
					AssetCategoryLocalServiceUtil.updateAssetCategory(cat2);
					
					final AssetCategory cat3 = AssetCategoryLocalServiceUtil.createAssetCategory(CounterLocalServiceUtil.increment(AssetCategory.class.getName(), 1));
					cat3.setName(SiteAvisClientUtil.NOTES_CATEGORY_NAME_ACTIONS_PREVENTION);
					cat3.setVocabularyId(vocabulary.getVocabularyId());
					cat3.setGroupId(globalGroup.getGroupId());
					AssetCategoryLocalServiceUtil.updateAssetCategory(cat3);
					
					categories.add(cat1);
					categories.add(cat2);
					categories.add(cat3);
		        } catch(Exception e) {
		        	LOGGER.error(e);
		        }
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
