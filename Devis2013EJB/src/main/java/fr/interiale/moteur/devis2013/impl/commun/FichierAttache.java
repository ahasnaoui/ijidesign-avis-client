package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;

public class FichierAttache implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2350748997905981173L;
	
	private String nom;
	private String typemime;
	private String description;
	private byte[] bytes;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getTypemime() {
		return typemime;
	}

	public void setTypemime(String typemime) {
		this.typemime = typemime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public String toString() {
		return "FichierAttache [nom=" + nom + ", typemime=" + typemime + ", description=" + description + "]";
	}
	
	
}
