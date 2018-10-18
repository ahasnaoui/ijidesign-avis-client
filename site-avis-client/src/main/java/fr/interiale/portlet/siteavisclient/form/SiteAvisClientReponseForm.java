/**
 * 
 */
package fr.interiale.portlet.siteavisclient.form;

import java.io.Serializable;
import java.util.List;

import fr.interiale.portlet.siteavisclient.bean.InterialeAvisReponseFO;

/**
 * @author antoine.comble
 *
 */
public class SiteAvisClientReponseForm implements Serializable {

	private static final long serialVersionUID = 2390062082923362624L;

	private String reponseId;
	
	private String commentaireId;
	
	private String reponse;
	
	private String commentaire;
	
	private List<InterialeAvisReponseFO> reponses;
	
	private String nom;
	
	private String prenom;

	/**
	 * @return the reponseId
	 */
	public String getReponseId() {
		return reponseId;
	}

	/**
	 * @param reponseId the reponseId to set
	 */
	public void setReponseId(String reponseId) {
		this.reponseId = reponseId;
	}

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
	 * @return the reponses
	 */
	public List<InterialeAvisReponseFO> getReponses() {
		return reponses;
	}

	/**
	 * @param list the reponses to set
	 */
	public void setReponses(List<InterialeAvisReponseFO> reponses) {
		this.reponses = reponses;
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
	
}
