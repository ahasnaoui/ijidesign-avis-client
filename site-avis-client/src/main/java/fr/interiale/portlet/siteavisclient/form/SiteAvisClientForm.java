package fr.interiale.portlet.siteavisclient.form;

import java.io.Serializable;

public class SiteAvisClientForm implements Serializable {

	private static final long serialVersionUID = -2483543151011853106L;

	private String nom;
	
	private String prenom;
	
	private String numeroAdherent;
	
	private String email;
	
	private String commentaire;
	
	private String noteProduits = "0";
	
	private String noteRelationAdherent = "0";
	
	private String noteActionPrevention = "0";
	
	private long id;

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
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the noteProduits
	 */
	public String getNoteProduits() {
		return noteProduits;
	}

	/**
	 * @param noteProduits the noteProduits to set
	 */
	public void setNoteProduits(String noteProduits) {
		this.noteProduits = noteProduits;
	}

	/**
	 * @return the noteRelationAdherent
	 */
	public String getNoteRelationAdherent() {
		return noteRelationAdherent;
	}

	/**
	 * @param noteRelationAdherent the noteRelationAdherent to set
	 */
	public void setNoteRelationAdherent(String noteRelationAdherent) {
		this.noteRelationAdherent = noteRelationAdherent;
	}

	/**
	 * @return the noteActionPrevention
	 */
	public String getNoteActionPrevention() {
		return noteActionPrevention;
	}

	/**
	 * @param noteActionPrevention the noteActionPrevention to set
	 */
	public void setNoteActionPrevention(String noteActionPrevention) {
		this.noteActionPrevention = noteActionPrevention;
	}
	
}
