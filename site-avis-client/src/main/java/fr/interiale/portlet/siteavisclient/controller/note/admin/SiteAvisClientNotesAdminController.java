package fr.interiale.portlet.siteavisclient.controller.note.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

import fr.interiale.common.portal.constants.IAuthConstantes;
import fr.interiale.portlet.liferay.services.NoSuchInterialeAvisCommentaireNotesException;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaireNotes;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireNotesLocalServiceUtil;
import fr.interiale.portlet.siteavisclient.bean.CategorieNoteGlobaleFO;
import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

/**
 * Controller du portlet Admin Site Avis Client
 * 
 * @author Antoine Comble (cckc3018)
 */
@Controller
@RequestMapping("VIEW")
public class SiteAvisClientNotesAdminController {

	private static final Logger LOGGER = Logger.getLogger(SiteAvisClientNotesAdminController.class);

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
		
		addDefaultData(m, portletRequest);
		
		return SiteAvisClientUtil.ADMIN_NOTES_VIEW;
	}
	
	private void addDefaultData(final Model m, final PortletRequest portletRequest) {
		Group globalGroup = null;
		AssetVocabulary vocabulary = null;
		List<AssetCategory> categories = null;
		
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
		}
		
		final Map<Long,CategorieNoteGlobaleFO> results = new HashMap<Long,CategorieNoteGlobaleFO>();
		final List<CategorieNoteGlobaleFO> notes = new ArrayList<CategorieNoteGlobaleFO>();
		
		if (categories != null && !categories.isEmpty()) {
			// Récupération des catégories / items
			m.addAttribute("categories", categories);
			
			// Calcul de la note par catégorie : compter uniquement les commentaires visibles sur la partie publique
			for (final AssetCategory category : categories) {	
				if (results.get(category.getCategoryId()) == null) {
					final CategorieNoteGlobaleFO noteglobale = new CategorieNoteGlobaleFO();
					noteglobale.setCategory(category);
					results.put(category.getCategoryId(), noteglobale);
				}
			}
				
			try {
				// Récupération de tous les commentaires
				final List<InterialeAvisCommentaire> listeAvis = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaires(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
				
				if (listeAvis != null && !listeAvis.isEmpty()) {
					for (final InterialeAvisCommentaire avis : listeAvis) {
						
						if (SiteAvisClientUtil.COMMENTAIRE_DEFAULT_AUTEUR.equals(avis.getNom()) && SiteAvisClientUtil.COMMENTAIRE_DEFAULT_AUTEUR.equals(avis.getPrenom())) {
						} else {
							for (final Map.Entry<Long,CategorieNoteGlobaleFO> entry : results.entrySet()) {
								try {
									final InterialeAvisCommentaireNotes note = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireAndCategory(avis.getId(), entry.getKey());
	
									entry.getValue().getNoteGlobale().setNbAvis(entry.getValue().getNoteGlobale().getNbAvis() + 1);
									entry.getValue().getNoteGlobale().setNote(entry.getValue().getNoteGlobale().getNote() + note.getNote());
									
								} catch (NoSuchInterialeAvisCommentaireNotesException e) {
									LOGGER.error("Note non trouvée pour le commentaire " + avis.getId() + " et la catégorie " + entry.getKey());
								}
							}
						}
					}
					
				}
			
			} catch (SystemException e) {
				LOGGER.error("Impossible de récupérer les commentaires ", e);
			}
		}
		
		for (final Map.Entry<Long,CategorieNoteGlobaleFO> entry : results.entrySet()) {
			notes.add(entry.getValue());
		}
		
		m.addAttribute("notesByItem", notes);
		
	}

}
