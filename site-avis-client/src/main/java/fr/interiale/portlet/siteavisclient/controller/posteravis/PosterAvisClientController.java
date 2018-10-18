package fr.interiale.portlet.siteavisclient.controller.posteravis;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

import fr.interiale.common.portal.constants.IAuthConstantes;
import fr.interiale.ejb.client.factory.Factory;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaireNotes;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireNotesLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.persistence.InterialeAvisCommentaireNotesPK;
import fr.interiale.portlet.siteavisclient.form.SiteAvisClientForm;
import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;
import fr.interiale.service.edeal.IEdealHelper;
import fr.mgpat.dao.DaoImp.Mode;
import fr.mgpat.dao.beneficiaire.interfaces.BeneficiaireDaoRemote;
import fr.mgpat.domain.beneficiaire.Beneficiaire;

/**
 * Controller du portlet Site Avis Client
 * 
 * @author Antoine Comble (cckc3018)
 */
@Controller
@RequestMapping("VIEW")
public class PosterAvisClientController {

	private static final Logger LOGGER = Logger.getLogger(PosterAvisClientController.class);
	
	/**
     * Group Liferay dans lequel la page du mini site contenant la liste des avis Intériale a été créée
     */
    public static final long ITE_MINI_SITE_LISTE_AVIS_PAGE_GROUP = GetterUtil.getLong(PropsUtil.get("avis-client.mini-site.liste-avis.group"));
    
    /**
     * Friendly URL de la page du mini site contenant la liste des avis Intériale
     */
    public static final String ITE_MINI_SITE_LISTE_AVIS_PAGE_FRIENDLY_URL = GetterUtil.getString(PropsUtil.get("avis-client.mini-site.liste-avis.friendlyurl"));
	
    private BeneficiaireDaoRemote beneficiaireDaoRemote = null;
    
	private IEdealHelper edealHelper = null;

	@PostConstruct
	public void init() {
		beneficiaireDaoRemote = (BeneficiaireDaoRemote) Factory.getInstance().getInterface(BeneficiaireDaoRemote.JNDIName, BeneficiaireDaoRemote.class);
		edealHelper = (IEdealHelper) Factory.getInstance().getInterface(IEdealHelper.JNDIName, IEdealHelper.class);
	}

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
		final SiteAvisClientForm posterAvisForm = new SiteAvisClientForm();
		m.addAttribute("avisForm", posterAvisForm);		
		
		addDefaultData(portletRequest, m);
		
		return SiteAvisClientUtil.POSTER_AVIS_VIEW;
	}
	
	/**
     * Action d'ajout d'un avis.
     * 
     * @throws SystemException
     * @throws IOException
     */
    @ActionMapping(params = { "action=posterAvis" })
    public void posterAvis(Model m, final PortletRequest portletRequest, final ActionResponse response,
            @ModelAttribute("avisForm") final SiteAvisClientForm avisForm, final BindingResult result) {
    	
    	final String nom = avisForm.getNom();
    	final String prenom = avisForm.getPrenom();
    	final String numeroAdherent = avisForm.getNumeroAdherent();
    	final String email = avisForm.getEmail();
    	final String commentaire = avisForm.getCommentaire();
    	final String noteProduits = avisForm.getNoteProduits();
    	final String noteRelationAdherent = avisForm.getNoteRelationAdherent();
    	final String noteActionPrevention = avisForm.getNoteActionPrevention();
    	
    	final long noteProduitsAsNote = Long.parseLong(noteProduits);
    	final long noteRelationAdherentAsNote = Long.parseLong(noteRelationAdherent);
    	final long noteActionPreventionAsNote = Long.parseLong(noteActionPrevention);
    	
    	final float moyenne = (noteProduitsAsNote + noteRelationAdherentAsNote + noteActionPreventionAsNote) / new Float(3);
    	
    	try {
    		int nextId = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentairesCount();
    		nextId++;
			final InterialeAvisCommentaire avis = InterialeAvisCommentaireLocalServiceUtil.createInterialeAvisCommentaire(nextId);
    		avis.setNom(nom);
    		avis.setPrenom(prenom);
    		avis.setNumeroAdherent(numeroAdherent);
    		
    		if (numeroAdherent != null && !"".equals(numeroAdherent) && email != null && "".equals(email)) {
    			Beneficiaire titulaireContrat = null;
    			try {
    				titulaireContrat = beneficiaireDaoRemote.findPrincipalByContratNum(numeroAdherent, Mode.ALIMENTATION);
    			} catch (final Exception e) {
    				LOGGER.error(e.getMessage(), e);
    			}
    			if (titulaireContrat != null) {
    				avis.setEmail(edealHelper.getMail(titulaireContrat.getAdhesioncontrat_id()));
    			} else {
    				avis.setEmail(email);
    			}
    		} else {
    			avis.setEmail(email);
    		}
    		
    		avis.setCommentaire(commentaire);
    		avis.setCommentaireOrigine(commentaire);
    		avis.setNote(SiteAvisClientUtil.round(moyenne, 2));
    		avis.setDateCreation(Calendar.getInstance().getTime());
    		InterialeAvisCommentaireLocalServiceUtil.updateInterialeAvisCommentaire(avis);
    		
    		// Note Produits
    		InterialeAvisCommentaireNotesPK notesProduitsKey = new InterialeAvisCommentaireNotesPK();
    		final AssetCategory categorieProduits = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieProduits");
			notesProduitsKey.setCategoryId(categorieProduits.getCategoryId());
    		notesProduitsKey.setCommentaireId(avis.getId());
			InterialeAvisCommentaireNotes notesProduits = InterialeAvisCommentaireNotesLocalServiceUtil.createInterialeAvisCommentaireNotes((fr.interiale.portlet.liferay.services.service.persistence.InterialeAvisCommentaireNotesPK) notesProduitsKey);
			notesProduits.setNote(noteProduitsAsNote);
    		InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(notesProduits);
    		
    		// Note Relation Adhérent
    		InterialeAvisCommentaireNotesPK notesRelationAdherentKey = new InterialeAvisCommentaireNotesPK();
    		final AssetCategory categorieRelationAdherent = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieRelationAdherent");
    		notesRelationAdherentKey.setCategoryId(categorieRelationAdherent.getCategoryId());
    		notesRelationAdherentKey.setCommentaireId(avis.getId());
			InterialeAvisCommentaireNotes notesRelationAdherent = InterialeAvisCommentaireNotesLocalServiceUtil.createInterialeAvisCommentaireNotes((fr.interiale.portlet.liferay.services.service.persistence.InterialeAvisCommentaireNotesPK) notesRelationAdherentKey);
			notesRelationAdherent.setNote(noteRelationAdherentAsNote);
    		InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(notesRelationAdherent);
    		
    		// Note Action de prévention
    		InterialeAvisCommentaireNotesPK notesActionsPreventionKey = new InterialeAvisCommentaireNotesPK();
    		final AssetCategory categorieActionsPrevention = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieActionsPrevention");
    		notesActionsPreventionKey.setCategoryId(categorieActionsPrevention.getCategoryId());
    		notesActionsPreventionKey.setCommentaireId(avis.getId());
			InterialeAvisCommentaireNotes notesActionsPrevention = InterialeAvisCommentaireNotesLocalServiceUtil.createInterialeAvisCommentaireNotes(notesActionsPreventionKey);
			notesActionsPrevention.setNote(noteActionPreventionAsNote);
    		InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(notesActionsPrevention);
			
    	} catch (SystemException e) {
            LOGGER.error("Impossible d'enregistrer l'avis en base.", e);
        }
    	
    	try {
    		// Si pas d'erreur => redirection vers la page Votre avis sur Intériale avec une notification de succes.
            final Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(ITE_MINI_SITE_LISTE_AVIS_PAGE_GROUP, false, ITE_MINI_SITE_LISTE_AVIS_PAGE_FRIENDLY_URL);
            final LiferayPortletResponse liferayPortletResponse = PortalUtil.getLiferayPortletResponse(response);
            final LiferayPortletURL location = liferayPortletResponse.createLiferayPortletURL(layout.getPlid(),
                    "itesitesiteavisclientportlet_WAR_itesiteavisclientportlet", PortletRequest.RENDER_PHASE);
            location.setParameter("action", "viewAfterSendAvis");
            response.sendRedirect(location.toString());
		} catch (final IOException | SystemException e) {
			response.setRenderParameter("action", "view");
		}
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
