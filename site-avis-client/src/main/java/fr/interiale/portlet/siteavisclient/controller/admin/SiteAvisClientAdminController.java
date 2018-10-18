package fr.interiale.portlet.siteavisclient.controller.admin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

import fr.interiale.common.portal.constants.IAuthConstantes;
import fr.interiale.ejb.client.factory.Factory;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaireNotes;
import fr.interiale.portlet.liferay.services.model.InterialeAvisReponse;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireNotesLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.InterialeAvisReponseLocalServiceUtil;
import fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO;
import fr.interiale.portlet.siteavisclient.form.SiteAvisClientForm;
import fr.interiale.portlet.siteavisclient.form.SiteAvisClientReponseForm;
import fr.interiale.portlet.siteavisclient.util.InterialeAvisCommentaireDateCreationComparator;
import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;
import fr.mgpat.dao.beneficiaire.interfaces.AdhesionContratDaoRemote;
import fr.mgpat.dao.beneficiaire.interfaces.BeneficiaireDaoRemote;

/**
 * Controller du portlet Admin Site Avis Client
 * 
 * @author Antoine Comble (cckc3018)
 */
@Controller
@RequestMapping("VIEW")
public class SiteAvisClientAdminController {

	private static final Logger LOGGER = Logger.getLogger(SiteAvisClientAdminController.class);
	
	/**
     * Group Liferay dans lequel la page d'administration des avis a été créée
     */
    public static final long ITE_AVIS_ADMINISTRATION_PAGE_GROUP = GetterUtil.getLong(PropsUtil.get("avis-client.admin.page.group"));
    
    /**
     * Friendly URL de la page d'administration des avis Intériale
     */
    public static final String ITE_AVIS_ADMINISTRATION_PAGE_FRIENDLY_URL = GetterUtil.getString(PropsUtil.get("avis-client.admin.page.friendlyurl"));
	
	BeneficiaireDaoRemote beneficiaireDaoRemote = null;
	
	AdhesionContratDaoRemote adhesionContratDaoRemote = null;

	@PostConstruct
	public void init() {
		beneficiaireDaoRemote = (BeneficiaireDaoRemote) Factory.getInstance().getInterface(BeneficiaireDaoRemote.JNDIName, BeneficiaireDaoRemote.class);
		adhesionContratDaoRemote = (AdhesionContratDaoRemote) Factory.getInstance().getInterface(AdhesionContratDaoRemote.JNDIName, AdhesionContratDaoRemote.class);
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
		addDefaultData(portletRequest, m);
		
		return SiteAvisClientUtil.ADMIN_VIEW;
	}
	
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

	private void addDefaultData(final PortletRequest portletRequest, Model m) {
		Group globalGroup = null;
		AssetVocabulary vocabulary = null;
		List<AssetCategory> categories = null;
		
		m.addAttribute("delta", SiteAvisClientUtil.NB_AVIS_PER_PAGE);
		m.addAttribute("avisForm", new SiteAvisClientForm());
		
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
		
		try {
			// Récupération de tous les commentaires
			final List<InterialeAvisCommentaire> listeAvis = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaires(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			if (listeAvis != null && !listeAvis.isEmpty()) {
				// Trier du + récent au + ancien
				final List<InterialeAvisCommentaireFO> listeAvisFO = SiteAvisClientUtil.map(listeAvis, false);
				
				Collections.sort(listeAvisFO, new InterialeAvisCommentaireDateCreationComparator());
				
				// Récupérer les réponses
				SiteAvisClientUtil.addReponsesByCommentaire(listeAvisFO);
				
				m.addAttribute("total", listeAvisFO.size());
				m.addAttribute("listeAvis", listeAvisFO);
			} else {
				m.addAttribute("listeAvis", null);
			}
		} catch (SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la récupération des avis", e);
		}
	}

	/**
     * Action de préparation de modification d'un avis.
     * 
     * @throws SystemException
     * @throws IOException
     */
    @ActionMapping(params = { "action=preparerModifierAvis" })
    public void preparerModifierAvis(Model m, final PortletRequest portletRequest, final ActionResponse response) {
    	
    	final SiteAvisClientForm avisForm = new SiteAvisClientForm();
    	
    	final String commentaireId = portletRequest.getParameter("commentaireId");
    	
    	addDefaultData(portletRequest, m);

    	try {
			final InterialeAvisCommentaire interialeAvisCommentaire = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaire(Long.valueOf(commentaireId));
			//avisForm.setCategoryId(String.valueOf(interialeAvisCommentaire.getCategoryId()));
			avisForm.setCommentaire(interialeAvisCommentaire.getCommentaire());
			avisForm.setEmail(interialeAvisCommentaire.getEmail());
			avisForm.setNom(interialeAvisCommentaire.getNom());
			//avisForm.setNotes(String.valueOf(interialeAvisCommentaire.getNote()));
			avisForm.setNumeroAdherent(interialeAvisCommentaire.getNumeroAdherent());
			avisForm.setPrenom(interialeAvisCommentaire.getPrenom());
			avisForm.setId(interialeAvisCommentaire.getId());
			
			// Récupérer les catégories de note
			final AssetCategory categorieProduits = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieProduits");
			final AssetCategory categorieRelationAdherent = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieRelationAdherent");
			final AssetCategory categorieActionsPrevention = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieActionsPrevention");
			
			// Récupérer les notes par catégorie
			try {
				final InterialeAvisCommentaireNotes categorieProduitsNote = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireAndCategory(interialeAvisCommentaire.getId(), categorieProduits.getCategoryId());
				avisForm.setNoteProduits(String.valueOf(categorieProduitsNote.getNote()));
			} catch (final Exception e) {}
			try {
					final InterialeAvisCommentaireNotes categorieRelationAdherentNote = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireAndCategory(interialeAvisCommentaire.getId(), categorieRelationAdherent.getCategoryId());
					avisForm.setNoteRelationAdherent(String.valueOf(categorieRelationAdherentNote.getNote()));
			} catch (final Exception e) {}
			try {
					final InterialeAvisCommentaireNotes categorieActionsPreventionNote = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireAndCategory(interialeAvisCommentaire.getId(), categorieActionsPrevention.getCategoryId());
					avisForm.setNoteActionPrevention(String.valueOf(categorieActionsPreventionNote.getNote()));
			} catch (final Exception e) {}
			
    	} catch (PortalException | SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la récupération de l'avis " + commentaireId, e);
		}
    	
		m.addAttribute("avisForm", avisForm);
    	
    	response.setRenderParameter("action", "viewEditPage");
    }
    
    /**
	 * Vue de la page "Modifier un avis".
	 * 
	 * @return la vue à afficher
	 */
	@RenderMapping(params = { "action=viewEditPage" })
	public String viewEditPage(final Model model, final RenderRequest renderRequest) {
		// Affichage de la vue.
		return SiteAvisClientUtil.ADMIN_EDIT_AVIS_VIEW;
	}
	
	/**
     * Action de modification d'un avis.
     * 
     * @throws SystemException
     * @throws IOException
     */
    @ActionMapping(params = { "action=modifierAvis" })
    public void modifierAvis(Model m, final PortletRequest portletRequest, final ActionResponse response,
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
    		final InterialeAvisCommentaire avis = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaire(avisForm.getId());
    		avis.setNom(nom);
    		avis.setPrenom(prenom);
    		avis.setNumeroAdherent(numeroAdherent);
    		avis.setEmail(email);
    		avis.setCommentaire(commentaire);
    		avis.setNote(moyenne);
    		InterialeAvisCommentaireLocalServiceUtil.updateInterialeAvisCommentaire(avis);
    		
    		// Récupérer les catégories de note
			final AssetCategory categorieProduits = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieProduits");
			final AssetCategory categorieRelationAdherent = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieRelationAdherent");
			final AssetCategory categorieActionsPrevention = (AssetCategory) portletRequest.getPortletSession().getAttribute("categorieActionsPrevention");
			
			// Mettre à jour les notes par catégorie
			try {
				final InterialeAvisCommentaireNotes categorieProduitsNote = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireAndCategory(avis.getId(), categorieProduits.getCategoryId());
				categorieProduitsNote.setNote(noteProduitsAsNote);
				InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(categorieProduitsNote);
			} catch (final Exception e) {}
			try {
					final InterialeAvisCommentaireNotes categorieRelationAdherentNote = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireAndCategory(avis.getId(), categorieRelationAdherent.getCategoryId());
					categorieRelationAdherentNote.setNote(noteRelationAdherentAsNote);
					InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(categorieRelationAdherentNote);
			} catch (final Exception e) {}
			try {
					final InterialeAvisCommentaireNotes categorieActionsPreventionNote = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireAndCategory(avis.getId(), categorieActionsPrevention.getCategoryId());
					categorieActionsPreventionNote.setNote(noteActionPreventionAsNote);
					InterialeAvisCommentaireNotesLocalServiceUtil.updateInterialeAvisCommentaireNotes(categorieActionsPreventionNote);
			} catch (final Exception e) {}
    		
    	} catch (PortalException | SystemException e) {
            LOGGER.error("Impossible d'enregistrer l'avis en base.", e);
		}

    	// Vider les caches
    	InterialeAvisCommentaireLocalServiceUtil.clearCache();
    	
    	response.setRenderParameter("action", "view");
    }
    
    /**
     * Action de préparation de réponse à un avis.
     * 
     * @throws SystemException
     * @throws IOException
     */
    @ActionMapping(params = { "action=preparerRepondreAvis" })
    public void preparerRepondreAvis(Model m, final PortletRequest portletRequest, final ActionResponse response) {
    	
    	final SiteAvisClientReponseForm avisForm = new SiteAvisClientReponseForm();
    	
    	final String commentaireId = portletRequest.getParameter("commentaireId");
    	
    	addDefaultData(portletRequest, m);

    	try {
			final InterialeAvisCommentaire interialeAvisCommentaire = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaire(Long.valueOf(commentaireId));
			avisForm.setCommentaireId(String.valueOf(interialeAvisCommentaire.getId()));
			avisForm.setCommentaire(interialeAvisCommentaire.getCommentaire());
			final InterialeAvisCommentaireFO avisFO = SiteAvisClientUtil.map(interialeAvisCommentaire);
			SiteAvisClientUtil.addReponsesByCommentaire(avisFO);
			
			if (avisFO.getReponses() != null && !avisFO.getReponses().isEmpty()) {
				avisForm.setReponseId(avisFO.getReponses().get(0).getId());
				avisForm.setReponse(avisFO.getReponses().get(0).getReponse());
				avisForm.setNom(avisFO.getReponses().get(0).getNom());
				avisForm.setPrenom(avisFO.getReponses().get(0).getPrenom());
			}
    	} catch (PortalException | SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la récupération de l'avis " + commentaireId, e);
		}
    	
		m.addAttribute("avisForm", avisForm);
    	
    	response.setRenderParameter("action", "viewAnswerPage");
    }
    
    /**
	 * Vue de la page "Répondre à un avis".
	 * 
	 * @return la vue à afficher
	 */
	@RenderMapping(params = { "action=viewAnswerPage" })
	public String viewAnswerPage(final Model model, final RenderRequest renderRequest) {
		// Affichage de la vue.
		return SiteAvisClientUtil.ADMIN_ANSWER_AVIS_VIEW;
	}
	
	/**
     * Action de réponse à un avis.
     * 
     * @throws SystemException
     * @throws IOException
     */
    @ActionMapping(params = { "action=repondre" })
    public void repondre(Model m, final PortletRequest portletRequest, final ActionResponse response,
            @ModelAttribute("avisForm") final SiteAvisClientReponseForm avisForm, final BindingResult result) {
    	
    	final String reponseId = avisForm.getReponseId();
    	final String commentaireId = avisForm.getCommentaireId();
    	final String reponse = avisForm.getReponse();
    	
    	 // Récupération du theme display
        final ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
        final User connectedUser = themeDisplay.getUser();
        
        InterialeAvisReponse interialeAvisReponse = null;
    	try {
			if (reponseId == null || reponseId.isEmpty()) {
    			interialeAvisReponse = InterialeAvisReponseLocalServiceUtil.createInterialeAvisReponse(CounterLocalServiceUtil.increment(InterialeAvisReponse.class.getName(), 1));
    		} else {
    			interialeAvisReponse = InterialeAvisReponseLocalServiceUtil.fetchInterialeAvisReponse(Long.parseLong(reponseId));
    		}
			interialeAvisReponse.setCommentaireId(Long.parseLong(commentaireId));
			interialeAvisReponse.setReponse(reponse);
			interialeAvisReponse.setDateCreation(Calendar.getInstance().getTime());
			interialeAvisReponse.setLiferayUserId((connectedUser != null) ? connectedUser.getUserId() : 0);
			interialeAvisReponse.setNom(avisForm.getNom());
			interialeAvisReponse.setPrenom(avisForm.getPrenom());
			InterialeAvisReponseLocalServiceUtil.updateInterialeAvisReponse(interialeAvisReponse);
		} catch (final SystemException e) {
			LOGGER.error("Une erreur est survenue pendant l'enregistrement de la réponse", e);
		}
    	
    	response.setRenderParameter("action", "view");
    }
    
    /**
     * Action de publication d'un avis.
     * 
     * @throws SystemException
     * @throws IOException
     */
    @ActionMapping(params = { "action=publierAvis" })
    public void publierAvis(Model m, final PortletRequest portletRequest, final ActionResponse response) {
    	
    	addDefaultData(portletRequest, m);

    	final String commentaireId = portletRequest.getParameter("commentaireId");

    	try {
			final InterialeAvisCommentaire interialeAvisCommentaire = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaire(Long.parseLong(commentaireId));
			interialeAvisCommentaire.setDateDebutPublication(Calendar.getInstance().getTime());
			interialeAvisCommentaire.setDateFinPublication(null);
			InterialeAvisCommentaireLocalServiceUtil.updateInterialeAvisCommentaire(interialeAvisCommentaire);
		} catch (NumberFormatException | PortalException | SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la publication de l'avis.", e);
		}
    	
    	try {
    		// Si pas d'erreur => redirection vers la page Votre avis sur Intériale avec une notification de succes.
            final Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(ITE_AVIS_ADMINISTRATION_PAGE_GROUP, false, ITE_AVIS_ADMINISTRATION_PAGE_FRIENDLY_URL);
            final LiferayPortletResponse liferayPortletResponse = PortalUtil.getLiferayPortletResponse(response);
            final LiferayPortletURL location = liferayPortletResponse.createLiferayPortletURL(layout.getPlid(),
                    "itesiteavisclientadminportlet_WAR_itesiteavisclientportlet", PortletRequest.RENDER_PHASE);
            location.setParameter("action", "viewAfterPublidhAvis");
            response.sendRedirect(location.toString());
		} catch (final IOException | SystemException e) {
			response.setRenderParameter("action", "view");
		}
    }
    
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
    @RenderMapping(params = "action=viewAfterPublidhAvis")
    public String viewAfterPublidhAvis(final Model model, final RenderRequest request, final RenderResponse response) throws SystemException {
        model.addAttribute("avisPublie", true);
        return view(request, response, model);
    }
    
    
    /**
     * Action de dé-publication d'un avis.
     * 
     * @throws SystemException
     * @throws IOException
     */
    @ActionMapping(params = { "action=depublierAvis" })
    public void depublierAvis(Model m, final PortletRequest portletRequest, final ActionResponse response) {
    	
    	addDefaultData(portletRequest, m);

    	final String commentaireId = portletRequest.getParameter("commentaireId");

    	try {
			final InterialeAvisCommentaire interialeAvisCommentaire = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaire(Long.parseLong(commentaireId));
			interialeAvisCommentaire.setDateFinPublication(Calendar.getInstance().getTime());
			interialeAvisCommentaire.setDateDebutPublication(null);
			InterialeAvisCommentaireLocalServiceUtil.updateInterialeAvisCommentaire(interialeAvisCommentaire);
		} catch (NumberFormatException | PortalException | SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la publication de l'avis.", e);
		}
    	
    	try {
    		// Si pas d'erreur => redirection vers la page Votre avis sur Intériale avec une notification de succes.
            final Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(ITE_AVIS_ADMINISTRATION_PAGE_GROUP, false, ITE_AVIS_ADMINISTRATION_PAGE_FRIENDLY_URL);
            final LiferayPortletResponse liferayPortletResponse = PortalUtil.getLiferayPortletResponse(response);
            final LiferayPortletURL location = liferayPortletResponse.createLiferayPortletURL(layout.getPlid(),
                    "itesiteavisclientadminportlet_WAR_itesiteavisclientportlet", PortletRequest.RENDER_PHASE);
            location.setParameter("action", "viewAfterUnpublidhAvis");
            response.sendRedirect(location.toString());
		} catch (final IOException | SystemException e) {
			response.setRenderParameter("action", "view");
		}
    }
    
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
    @RenderMapping(params = "action=viewAfterUnpublidhAvis")
    public String viewAfterUnpublidhAvis(final Model model, final RenderRequest request, final RenderResponse response) throws SystemException {
        model.addAttribute("avisDepublie", true);
        return view(request, response, model);
    }
}
