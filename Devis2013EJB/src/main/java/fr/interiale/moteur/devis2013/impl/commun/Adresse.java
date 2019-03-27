package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;

public class Adresse implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private  String numero="";
	private  String bis="";
	private  String type_voie="";
	private  String adresse1="";
	private  String adresse2="";
	private  String ville="";	
	
	public Adresse() {
		
	}
	
	
	public Adresse(String numero, String bis, String type_voie, String adresse1, String adresse2, 
			String ville) {
		super();
		this.numero = numero;
		this.bis = bis;
		this.type_voie = type_voie;
		this.adresse1 = adresse1;
		this.adresse2 = adresse2;
		this.ville = ville;
	}


	public String getNumero() {
		return numero;
	}
	public String getBis() {
		if (bis != null) {
			return bis.toUpperCase();
		}
		return bis;
	}
	public String getType_voie() {
		if (type_voie != null) {
			return type_voie.toUpperCase();
		}
		return type_voie;
	}
	public String getAdresse1() {
		if (adresse1 != null) {
			return adresse1.toUpperCase();
		}
		return adresse1;
	}
	public String getAdresse2() {
		if (adresse2 != null) {
			return adresse2.toUpperCase();
		}
		return adresse2;
	}
	
	public String getVille() {
		if (ville != null) {
			return ville.toUpperCase();
		}
		return ville;
	}


	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}


	/**
	 * @param bis the bis to set
	 */
	public void setBis(String bis) {
		this.bis = bis;
	}


	/**
	 * @param type_voie the type_voie to set
	 */
	public void setType_voie(String type_voie) {
		this.type_voie = type_voie;
	}


	/**
	 * @param adresse1 the adresse1 to set
	 */
	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}


	/**
	 * @param adresse2 the adresse2 to set
	 */
	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}


	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}
	
	
	
	
}
