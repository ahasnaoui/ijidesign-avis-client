package fr.interiale.portlet.siteavisclient.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.liferay.portal.kernel.util.StringPool;

import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

public class InterialeAvisCommentaireFO implements Serializable {

	private static final long serialVersionUID = -4595429929327959933L;

	private String nom;
	
	private String prenom;
	
	private String numeroAdherent;
	
	private String email;
	
	private String commentaire;
	
	private String categoryName;
	
	private String notes;
	
	private Date dateCreation;
	
	private Date dateDebutPublication;
	
	private Date dateFinPublication;
	
	private String truncatedCommentaire;
	
	private String id;
	
	private List<InterialeAvisReponseFO> reponses = new ArrayList<InterialeAvisReponseFO>();
	
	private String identite;

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the numeroAdherent
	 */
	public String getNumeroAdherent() {
		return numeroAdherent;
	}

	/**
	 * @param numeroAdherent the numeroAdherent to set
	 */
	public void setNumeroAdherent(String numeroAdherent) {
		this.numeroAdherent = numeroAdherent;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the commentaire
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * @param commentaire the commentaire to set
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the dateCreation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return the dateDebutPublication
	 */
	public Date getDateDebutPublication() {
		return dateDebutPublication;
	}

	/**
	 * @param dateDebutPublication the dateDebutPublication to set
	 */
	public void setDateDebutPublication(Date dateDebutPublication) {
		this.dateDebutPublication = dateDebutPublication;
	}

	/**
	 * @return the dateFinPublication
	 */
	public Date getDateFinPublication() {
		return dateFinPublication;
	}

	/**
	 * @param dateFinPublication the dateFinPublication to set
	 */
	public void setDateFinPublication(Date dateFinPublication) {
		this.dateFinPublication = dateFinPublication;
	}
	
	public String getTruncatedCommentaire() {
		if (commentaire != null && commentaire.length() > 100) {
			return commentaire.substring(0, 100) + " (...)";
		}
		return commentaire;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param truncatedCommentaire the truncatedCommentaire to set
	 */
	public void setTruncatedCommentaire(String truncatedCommentaire) {
		this.truncatedCommentaire = truncatedCommentaire;
	}

	public List<InterialeAvisReponseFO> getReponses() {
		if (reponses == null) {
			return new ArrayList<InterialeAvisReponseFO>();
		}
		return reponses;
	}

	public void setReponses(List<InterialeAvisReponseFO> reponses) {
		this.reponses = reponses;
	}
	
	public String getFormattedDayDateCreation() {
		if (this.dateCreation != null)
			return SiteAvisClientUtil.DAY_DATE_FORMATTER.format(this.dateCreation);
		return "";
	}
	
	public String getFormattedDayDateCreationEntier() {
		if (this.dateCreation != null)
			return StringUtils.capitalize(SiteAvisClientUtil.FULL_DAY_DATE_FORMATTER.format(this.dateCreation));
		return "";
	}
	
	public String getFormattedHourDateCreation() {
		if (this.dateCreation != null)
			return SiteAvisClientUtil.HOUR_DATE_FORMATTER.format(this.dateCreation);
		return "";
	}
	
	public String getFormattedMinutsDateCreation() {
		if (this.dateCreation != null)
			return SiteAvisClientUtil.MINUTS_DATE_FORMATTER.format(this.dateCreation);
		return "";
	}
	
	public String getFormattedDayDatePublication() {
		if (this.dateDebutPublication != null)
			return SiteAvisClientUtil.DAY_DATE_FORMATTER.format(this.dateDebutPublication);
		return "";
	}
	
	public String getTruncatedCommentaireSiteAvis() {
		if (commentaire != null && commentaire.length() > 250) {
			return commentaire.substring(0, 250) + " (...)";
		}
		return commentaire;
	}
	
	public int getNbEtoilesBlanches() {
		return SiteAvisClientUtil.getNbEtoilesBlanches(notes);
	}
	
	public int getNbEtoilesJaunes() {
		return SiteAvisClientUtil.getNbEtoilesJaunes(notes);
	}
	
	public int getNbEtoilesDemi() {
		return SiteAvisClientUtil.getNbEtoilesDemi(notes);
	}

	/**
	 * @return the identite
	 */
	public String getIdentite() {
		final StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.capitalize(StringUtils.lowerCase(prenom)));
		if (nom != null && !"".equals(nom)) {
			sb.append(StringPool.SPACE);
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(nom).substring(0, 1)));
			sb.append(".");
		}
		return sb.toString();
	}
	
	public String getFormattedNote() {
		try {
			final Float note = Float.parseFloat(notes);
			return SiteAvisClientUtil.DECIMAL_FORMAT_FRANCE.format(note);
		} catch (NumberFormatException | NullPointerException e) {
			return StringPool.BLANK;
		}

	}

	public String getReponse() {
		if (reponses == null || reponses.isEmpty()) {
			return "";
		}
		
		return reponses.get(0).getReponse();
	}
}
