package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;

public class Coordonnees implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7353272620851875860L;
	
	private String civilite;
	private String prenom;
	private String nom;
	private String code_postal;
	private String email;
	private String telephone;
	
	
	public Coordonnees(String civilite, String prenom, String nom, String code_postal, String email, String telephone) {
		super();
		this.civilite = civilite;
		this.prenom = prenom;
		this.nom = nom;
		this.code_postal = code_postal;
		this.email = email;
		this.telephone = telephone;
	}
	public String getCivilite() {
		return civilite;
	}
	public String getPrenom() {
		if (prenom != null) {
			return prenom.toUpperCase();
		}
		return prenom;
	}
	public String getNom() {
		if (nom != null) {
			return nom.toUpperCase();
		}
		return nom;
	}
	public String getCode_postal() {
		return code_postal;
	}
	public String getEmail() {
		return email;
	}
	public String getTelephone() {
		return telephone;
	}
	@Override
	public String toString() {
		return "Coordonnees [prenom=" + prenom + ", nom=" + nom + ", code_postal=" + code_postal + ", email=" + email
				+ ", telephone=" + telephone + "]";
	}
	/**
	 * @param civilite the civilite to set
	 */
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @param code_postal the code_postal to set
	 */
	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	
}
