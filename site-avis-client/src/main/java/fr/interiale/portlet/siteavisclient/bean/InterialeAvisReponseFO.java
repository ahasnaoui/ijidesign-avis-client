package fr.interiale.portlet.siteavisclient.bean;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.liferay.portal.kernel.util.StringPool;

import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

public class InterialeAvisReponseFO implements Serializable {

	private static final long serialVersionUID = -4180285434348789290L;

	private String commentaireId;
	
	private String id;
	
	private String reponse;
	
	private Date dateCreation;
	
	private String nom;
	
	private String prenom;

	/**
	 * @return the commentaireId
	 */
	public String getCommentaireId() {
		return commentaireId;
	}

	/**
	 * @param commentaireId the commentaireId to set
	 */
	public void setCommentaireId(String commentaireId) {
		this.commentaireId = commentaireId;
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
	 * @return the reponse
	 */
	public String getReponse() {
		return reponse;
	}

	/**
	 * @param reponse the reponse to set
	 */
	public void setReponse(String reponse) {
		this.reponse = reponse;
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
	 * @return the identite
	 */
	public String getIdentite() {		
		final StringBuilder sb = new StringBuilder();
		if ((prenom == null || "".equals(prenom)) && (nom == null || "".equals(nom))) {
			sb.append("Intériale");
		} else {
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(prenom)));
			sb.append(StringPool.SPACE);
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(nom)));
		}
		return sb.toString();
	}
	
	public String getCompleteAuthor() {
		final StringBuilder sb = new StringBuilder();
		if ((prenom == null || "".equals(prenom) || "''".equals(prenom)) && (nom == null || "".equals(nom) || "''".equals(nom))) {
			sb.append("Intériale");
		} else {
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(prenom)));
			sb.append(StringPool.SPACE);
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(nom)));
		}
		final String author = sb.toString();
		if (author.startsWith("A") || author.startsWith("E") || author.startsWith("I") || author.startsWith("O") || author.startsWith("U") || author.startsWith("Y")) {
			return "Réponse d'" + author;
		}
		return "Réponse de " + sb.toString();
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
}
