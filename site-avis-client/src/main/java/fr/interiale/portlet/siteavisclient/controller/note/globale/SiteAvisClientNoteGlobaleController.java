package fr.interiale.portlet.siteavisclient.controller.note.globale;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.SystemException;

import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaireNotes;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireLocalServiceUtil;
import fr.interiale.portlet.liferay.services.service.InterialeAvisCommentaireNotesLocalServiceUtil;
import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

/**
 * Controller du portlet Note globale Site Avis Client
 * 
 * @author Antoine Comble (cckc3018)
 */
@Controller
@RequestMapping("VIEW")
public class SiteAvisClientNoteGlobaleController {

	private static final Logger LOGGER = Logger.getLogger(SiteAvisClientNoteGlobaleController.class);

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
		
		return SiteAvisClientUtil.NOTE_GLOBALE_VIEW;
	}
	
	private void addDefaultData(final Model m, final PortletRequest portletRequest) {
		//
		final Date aujourdhui = Calendar.getInstance().getTime();
		
		// 
		Float noteGlobale = 0.0f;
		int nbAvisTotal = 0;
		
		// Calcul de la note par catégorie : compter uniquement les commentaires visibles sur la partie publique
		try {
			// Récupération de tous les commentaires
			final List<InterialeAvisCommentaire> listeAvis = InterialeAvisCommentaireLocalServiceUtil.getInterialeAvisCommentaires(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			
			if (listeAvis != null && !listeAvis.isEmpty()) {
				for (final InterialeAvisCommentaire avis : listeAvis) {
					if (SiteAvisClientUtil.COMMENTAIRE_DEFAULT_AUTEUR.equals(avis.getNom()) && SiteAvisClientUtil.COMMENTAIRE_DEFAULT_AUTEUR.equals(avis.getPrenom())) {
					} else {
						if (avis.getDateDebutPublication() != null && avis.getDateDebutPublication().before(aujourdhui) && avis.getDateFinPublication() == null) {
							try {
								nbAvisTotal++;
								int note = 0;
								final List<InterialeAvisCommentaireNotes> avisNotes = InterialeAvisCommentaireNotesLocalServiceUtil.findByCommentaireId(avis.getId());
								for (final InterialeAvisCommentaireNotes avisNote : avisNotes) {
									note += avisNote.getNote();
								}
								noteGlobale = noteGlobale + (note / new Float(avisNotes.size()));
							} catch (final SystemException e) {
								LOGGER.error("Aucune note trouvée pour le commentaire " + avis.getId());
							}
						}
					}
				}
				
			}
		} catch (final SystemException e) {
			LOGGER.error("Une erreur est survenue pendant la récupération des avis", e);
		}
		
		final Float moyenne = noteGlobale / new Float(nbAvisTotal);
		
		m.addAttribute("noteGlobale", noteGlobale);
		m.addAttribute("nbAvisTotal", nbAvisTotal);
		m.addAttribute("moyenne", SiteAvisClientUtil.round(moyenne, 1));
		
		m.addAttribute("nbEtoilesBlanches", SiteAvisClientUtil.getNbEtoilesBlanches(moyenne));
		m.addAttribute("nbEtoilesJaunes", SiteAvisClientUtil.getNbEtoilesJaunes(moyenne));
		m.addAttribute("nbEtoilesDemi", SiteAvisClientUtil.getNbEtoilesDemi(moyenne));
	}

}
