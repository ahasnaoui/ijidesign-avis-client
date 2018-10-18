package fr.interiale.portlet.siteavisclient.controller.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
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
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaireNotes;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireNotesLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.persistence.InterialeAvisCommentaireNotesPK;
import fr.interiale.portlet.siteavisclient.bean.SpringFileVO;
import fr.interiale.portlet.siteavisclient.form.SiteAvisClientImportDonneesForm;
import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

/**
 * Controller du portlet Site Avis Client
 * 
 * @author Antoine Comble (cckc3018)
 */
@Controller
@RequestMapping("VIEW")
public class ImportDonneesController {

	private static final Logger LOGGER = Logger.getLogger(ImportDonneesController.class);

	private static final String CSV_SPLIT_BY = ";";

	/**
	 * Affiche le portlet Poster un avis.
	 * 
	 * @param portletRequest
	 * @param portletResponse
	 * @param m
	 * @return
	 * @throws InterneLmdeUserException
	 */
	@RenderMapping
	public String view(final PortletRequest portletRequest, final PortletResponse portletResponse, Model m) {
		m.addAttribute("importDonneesForm", new SiteAvisClientImportDonneesForm());

		addDefaultData(portletRequest, m);
		
		return SiteAvisClientUtil.IMPORT_DONNEES_VIEW;
	}

	/**
	 * Action d'ajout d'un avis.
	 * 
	 * @throws SystemException
	 * @throws IOException
	 */
	@ModelAttribute("springFileVO")
	public SpringFileVO getCommandObject() {
		return new SpringFileVO();
	}

	// For file upload
	@ActionMapping(params = "formAction=fileUpload")
	public void fileUpload(@ModelAttribute SpringFileVO springFileVO, BindingResult bndingResult, ActionRequest request, ActionResponse response, SessionStatus sessionStatus) {
		int nbAvis = 0;
		boolean hasError = false;
		BufferedReader br = null;
		String line = "";
		boolean isFirstLine = true;
		int index = 0;
		try {
			final byte[] fileContent = springFileVO.getFileData().getBytes();
			br = new BufferedReader(new StringReader(new String(fileContent)));
			while ((line = br.readLine()) != null) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}
				final String[] commentaireInfos = line.split(CSV_SPLIT_BY);
				if (commentaireInfos != null && commentaireInfos.length == 7) {
					index++;
					boolean result = ajouterAvis(commentaireInfos, request);
					if (result) {
						nbAvis++;
					}
				}
			}

		} catch (final IOException e) {
			hasError = true;
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					hasError = true;
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		if (!hasError && index == nbAvis) {
			springFileVO.setSuccessMessage("Votre demande d'import a été traitée avec succès : " + nbAvis + " ont été créés.");
		} else {
			springFileVO.setErrorMessage("Une erreur est survenue lors de votre demande d'import");
		}
		sessionStatus.setComplete();
	}

	/**
	 * Ajouter un avis et les notes associées.
	 * 
	 * @param avisInformations
	 * @param request
	 */
	private boolean ajouterAvis(final String[] avisInformations, final ActionRequest request) {
		// Vérifie que les données attendues sont correctes
		if (avisInformations == null || avisInformations.length != 7) {
			return false;
		}

		try {
			final String nom = avisInformations[0];
			final String prenom = avisInformations[1];
			final String dateCreation = avisInformations[2];
			final String noteProduits = avisInformations[3];
			final String noteRelationAdherent = avisInformations[4];
			final String noteActionPrevention = avisInformations[5];
			final String commentaire = avisInformations[6];
		
    		final long noteProduitsAsNote = Long.parseLong(noteProduits);
    		final long noteRelationAdherentAsNote = Long.parseLong(noteRelationAdherent);
    		final long noteActionPreventionAsNote = Long.parseLong(noteActionPrevention);
    		
    		final float moyenne = (noteProduitsAsNote + noteRelationAdherentAsNote + noteActionPreventionAsNote) / new Float(3);
    		
    		int nextId = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentairesCount();
    		nextId++;
			final InterialeAvisCommentaire avis = InterialeAvisCommentaireLocalServiceUtil.createInterialeAvisCommentaire(nextId);
    		avis.setNom(nom);
    		avis.setPrenom(prenom);
    		
    		avis.setCommentaire(commentaire);
    		avis.setCommentaireOrigine(commentaire);
    		avis.setNote(SiteAvisClientUtil.round(moyenne, 2));
    		try {
				avis.setDateCreation(SiteAvisClientUtil.DAY_HOUR_DATE_FORMATTER.parse(dateCreation));
			} catch (final ParseException e) {
				LOGGER.error(e.getMessage(), e);
				avis.setDateCreation(Calendar.getInstance().getTime());
			}
    		avis.setDateDebutPublication(Calendar.getInstance().getTime());
    		InterialeAvisCommentaireLocalServiceUtil.updateInterialeAvisCommentaire(avis);
    		
    		// Note Produits
    		InterialeAvisCommentaireNotesPK notesProduitsKey = new InterialeAvisCommentaireNotesPK();
    		final AssetCategory categorieProduits = (AssetCategory) request.getPortletSession().getAttribute("categorieProduits");
			notesProduitsKey.setCategoryId(categorieProduits.getCategoryId());
    		notesProduitsKey.setCommentaireId(avis.getId());
			InterialeAvisCommentaireNotes notesProduits = InterialeAvisCommentaireNotesLocalServiceUtil.createInterialeAvisCommentaireNotes((fr.interiale.portlet.liferay.services.service.persistence.InterialeAvisCommentaireNotesPK) notesProduitsKey);
			notesProduits.setNote(noteProduitsAsNote);
    		InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(notesProduits);
    		
    		// Note Relation Adhérent
    		InterialeAvisCommentaireNotesPK notesRelationAdherentKey = new InterialeAvisCommentaireNotesPK();
    		final AssetCategory categorieRelationAdherent = (AssetCategory) request.getPortletSession().getAttribute("categorieRelationAdherent");
    		notesRelationAdherentKey.setCategoryId(categorieRelationAdherent.getCategoryId());
    		notesRelationAdherentKey.setCommentaireId(avis.getId());
			InterialeAvisCommentaireNotes notesRelationAdherent = InterialeAvisCommentaireNotesLocalServiceUtil.createInterialeAvisCommentaireNotes((fr.interiale.portlet.liferay.services.service.persistence.InterialeAvisCommentaireNotesPK) notesRelationAdherentKey);
			notesRelationAdherent.setNote(noteRelationAdherentAsNote);
    		InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(notesRelationAdherent);
    		
    		// Note Action de prévention
    		InterialeAvisCommentaireNotesPK notesActionsPreventionKey = new InterialeAvisCommentaireNotesPK();
    		final AssetCategory categorieActionsPrevention = (AssetCategory) request.getPortletSession().getAttribute("categorieActionsPrevention");
    		notesActionsPreventionKey.setCategoryId(categorieActionsPrevention.getCategoryId());
    		notesActionsPreventionKey.setCommentaireId(avis.getId());
			InterialeAvisCommentaireNotes notesActionsPrevention = InterialeAvisCommentaireNotesLocalServiceUtil.createInterialeAvisCommentaireNotes(notesActionsPreventionKey);
			notesActionsPrevention.setNote(noteActionPreventionAsNote);
    		InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(notesActionsPrevention);
			
    	} catch (Exception e) {
            LOGGER.error("Impossible d'enregistrer l'avis en base.", e);
            return false;
        }
    	
    	return true;
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
