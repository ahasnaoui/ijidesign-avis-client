package fr.interiale.portlet.siteavisclient.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;

import fr.interiale.portlet.liferay.services.model.InterialeAvisCommentaire;
import fr.interiale.portlet.liferay.services.model.InterialeAvisReponse;
import fr.interiale.portlet.liferay.services.service.InterialeAvisReponseLocalServiceUtil;
import fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO;
import fr.interiale.portlet.siteavisclient.bean.InterialeAvisReponseFO;


/**
 * Classe utilitaire
 * 
 * @author Antoine Comble (cckc3018)
 */
public final class SiteAvisClientUtil {

    private SiteAvisClientUtil() {
    }
    
    static {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#0.00");
        DECIMAL_FORMAT_FRANCE = df;
    }
    
    public static DecimalFormat DECIMAL_FORMAT_FRANCE;

    public static final String VIEW = "view";
	
	public static final String ADMIN_VIEW = "admin/view";
	
	public static final String POSTER_AVIS_VIEW = "poster-avis";
	
	public static final String SITE_AVIS_VIEW = "view-avis";
	
	public static final String ADMIN_EDIT_AVIS_VIEW = "admin/edit";
	
	public static final String ADMIN_ANSWER_AVIS_VIEW = "admin/answer";
	
	public static final String ADMIN_NOTES_VIEW = "admin/notesByItem";
	
	public static final String NOTE_GLOBALE_VIEW = "header-site-avis";
	
	public static final String IMPORT_DONNEES_VIEW = "admin/import-donnees";
	
	public static final String NOTES_CATEGORY_NAME_PRODUITS = "Produits";
	
	public static final String NOTES_CATEGORY_NAME_RELATION_ADHERENT = "Relation adhérent";
	
	public static final String NOTES_CATEGORY_NAME_ACTIONS_PREVENTION = "Actions de prévention";
	
	public static final String NOTES_VOCABULARY_NAME = "Intériale - Avis";
	
	public static final int NB_AVIS_PER_PAGE = 20;
	
	public static final String HOUR_PATTERN = "HH";
	
	public static final String MINUTS_PATTERN = "mm";
	
	public static final String DAY_PATTERN = "dd/MM/yyyy";
	
	public static final String DAY_HOUR_PATTERN = "dd/MM/yyyy hh:mm";
	
	public static final String FULL_DAY_PATTERN = "EEEE dd MMMM yyyy";
	
	public static final SimpleDateFormat DAY_DATE_FORMATTER = new SimpleDateFormat(DAY_PATTERN, Locale.FRANCE);
	
	public static final SimpleDateFormat DAY_HOUR_DATE_FORMATTER = new SimpleDateFormat(DAY_HOUR_PATTERN, Locale.FRANCE);
	
	public static final SimpleDateFormat HOUR_DATE_FORMATTER = new SimpleDateFormat(HOUR_PATTERN, Locale.FRANCE);
	
	public static final SimpleDateFormat FULL_DAY_DATE_FORMATTER = new SimpleDateFormat(FULL_DAY_PATTERN, Locale.FRANCE);
	
	public static final SimpleDateFormat MINUTS_DATE_FORMATTER = new SimpleDateFormat(MINUTS_PATTERN, Locale.FRANCE);
	
	public static final String SORT_PARAM_NAME = "avisSort";
	
	public static final String SORT_DATE_CREATION_ASC_PARAM_VALUE = "dateCreationAsc";
	
	public static final String SORT_DATE_CREATION_DESC_PARAM_VALUE = "dateCreationDesc";
	
	public static final String SORT_NOTES_ASC_PARAM_VALUE = "notesAsc";
	
	public static final String SORT_NOTES_DESC_PARAM_VALUE = "notesDesc";
	
	public static final int NB_ETOILES = 5;
	
	public static final String COMMENTAIRE_DEFAULT_AUTEUR = "INTERIALE";
	
	/**
	 * Transforme les commentaires BDD en objets front.
	 * 
	 * @param commentaires
	 * @param onlyPublished
	 * @return
	 */
	public static List<InterialeAvisCommentaireFO> map(final List<InterialeAvisCommentaire> commentaires, final boolean onlyPublished) {
		final List<InterialeAvisCommentaireFO> listeAvis = new ArrayList<InterialeAvisCommentaireFO>();
		
		for (final InterialeAvisCommentaire commentaire : commentaires) {
			if ((onlyPublished && commentaire.getDateDebutPublication() != null && commentaire.getDateFinPublication() == null) || (!onlyPublished)) {
				if (commentaire.getNom() != null && COMMENTAIRE_DEFAULT_AUTEUR.equals(commentaire.getNom()) && commentaire.getPrenom() != null && COMMENTAIRE_DEFAULT_AUTEUR.equals(commentaire.getPrenom())) {
					// Ne rien faire
				}else{
					listeAvis.add(map(commentaire));
				}
			}
		}
		
		return listeAvis;
	}
	
	/**
	 * Transforme les commentaires BDD en objets front.
	 * Ne prend en compte que les avis publiés postés par INTERIALE INTERIALE.
	 * 
	 * @param commentaires
	 * @param onlyPublished
	 * @return
	 */
	public static List<InterialeAvisCommentaireFO> mapForInteriale(final List<InterialeAvisCommentaire> commentaires) {
		final List<InterialeAvisCommentaireFO> listeAvis = new ArrayList<InterialeAvisCommentaireFO>();
		
		for (final InterialeAvisCommentaire commentaire : commentaires) {
			if (commentaire.getNom() != null && COMMENTAIRE_DEFAULT_AUTEUR.equals(commentaire.getNom()) && commentaire.getPrenom() != null && COMMENTAIRE_DEFAULT_AUTEUR.equals(commentaire.getPrenom())) {
				listeAvis.add(map(commentaire));
			}
		}
		
		return listeAvis;
	}

	/**
	 * Transforme un commentaire BDD en objet front.
	 * 
	 * @param commentaire
	 * @return
	 */
	public static InterialeAvisCommentaireFO map(final InterialeAvisCommentaire commentaire) {
		final InterialeAvisCommentaireFO avis = new InterialeAvisCommentaireFO();
		
		AssetCategory category;
		try {
			category = AssetCategoryLocalServiceUtil.getAssetCategory(commentaire.getCategoryId());
			avis.setCategoryName(category.getName());
		} catch (final PortalException | SystemException e) {}
		
		avis.setCommentaire(commentaire.getCommentaire());
		avis.setEmail(commentaire.getEmail());
		avis.setNom(commentaire.getNom());
		avis.setNotes(String.valueOf(commentaire.getNote()));
		avis.setNumeroAdherent(commentaire.getNumeroAdherent());
		avis.setPrenom(commentaire.getPrenom());
		avis.setDateCreation(commentaire.getDateCreation());
		avis.setDateDebutPublication(commentaire.getDateDebutPublication());
		avis.setDateFinPublication(commentaire.getDateFinPublication());
		avis.setId(String.valueOf(commentaire.getId()));
		
		return avis;
	}
	
	/**
	 * Transforme les réponses aux avis BDD en objets front.
	 * 
	 * @param reponses
	 * @return
	 */
	public static List<InterialeAvisReponseFO> mapReponses(final List<InterialeAvisReponse> reponses) {
		final List<InterialeAvisReponseFO> listeReponses = new ArrayList<InterialeAvisReponseFO>();
		
		for (final InterialeAvisReponse reponse : reponses) {
			listeReponses.add(mapReponse(reponse));
		}
		
		return listeReponses;
	}

	private static InterialeAvisReponseFO mapReponse(InterialeAvisReponse reponse) {
		final InterialeAvisReponseFO reponseFO = new InterialeAvisReponseFO();
		
		reponseFO.setId(String.valueOf(reponse.getId()));
		reponseFO.setCommentaireId(String.valueOf(reponse.getCommentaireId()));
		reponseFO.setDateCreation(reponse.getDateCreation());
		reponseFO.setReponse(reponse.getReponse());
		reponseFO.setNom(reponse.getNom());
		reponseFO.setPrenom(reponse.getPrenom());
		
		return reponseFO;
	}

	public static void addReponsesByCommentaire(List<InterialeAvisCommentaireFO> listeAvis) {
		if (listeAvis == null || listeAvis.isEmpty()) {
			return;
		}
		for (final InterialeAvisCommentaireFO commentaire : listeAvis) {
			try {
				final List<InterialeAvisReponse> reponses = InterialeAvisReponseLocalServiceUtil.findByCommentaireId(Long.valueOf(commentaire.getId()));
				if (reponses != null && !reponses.isEmpty()) {
					final List<InterialeAvisReponseFO> reponsesFO = mapReponses(reponses);
					Collections.sort(reponsesFO, new InterialeAvisReponseDateCreationComparator());
					commentaire.setReponses(reponsesFO);
				}
				
			} catch (final SystemException e) {
				// TODO LOGGER
			}
		}
	}
	
	public static void addReponsesByCommentaire(InterialeAvisCommentaireFO avis) {
		final List<InterialeAvisCommentaireFO> listeAvis = new ArrayList<InterialeAvisCommentaireFO>();
		listeAvis.add(avis);
		addReponsesByCommentaire(listeAvis);
		avis = listeAvis.get(0);
	}
	
	/**
     * Round to certain number of decimals
     * 
     * @param d
     * @param decimalPlace the numbers of decimals
     * @return
     */
    public static float round(float d, int decimalPlace) {
         return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
    }
	
    public static int getNbEtoilesBlanches(final double moyenne) {
		if (moyenne < 1) {
			return SiteAvisClientUtil.NB_ETOILES;
		}
		return SiteAvisClientUtil.NB_ETOILES - getNbEtoilesJaunes(moyenne) - getNbEtoilesDemi(moyenne);
	}
	
    public static int getNbEtoilesJaunes(final double moyenne) {
		return (int) (moyenne / 1);
	}
	
    public static int getNbEtoilesDemi(final double moyenne) {
		final int decimalPart = (int)((moyenne % 1) * 100);
		if (decimalPart != 0) {
			return 1;
		}
		return 0;
	}
    
    public static int getNbEtoilesBlanches(final String notes) {
		final Float note = Float.parseFloat(notes);
		return getNbEtoilesBlanches(note);
	}
	
	public static int getNbEtoilesJaunes(final String notes) {
		final Float note = Float.parseFloat(notes);
		return getNbEtoilesJaunes(note);
	}
	
	public static int getNbEtoilesDemi(final String notes) {
		final Float note = Float.parseFloat(notes);
		return getNbEtoilesDemi(note);
	}
}
