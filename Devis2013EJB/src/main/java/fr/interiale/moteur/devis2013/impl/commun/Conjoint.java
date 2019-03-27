package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;
import java.util.Date;

import fr.interiale.moteur.devis2013.impl.commun.Moi.Activite;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Etat;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Statut;
import fr.interiale.moteur.devis2013.impl.interiale.InitialisationMoteurException;

public class Conjoint implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5788741584266043682L;

	private final Date date_de_naissance;
	
	private  boolean regime_alsace_moselle;

	private final Etat etat;
	
	private final Activite activite;
	
	
	/**********************
	 *    CHAMPS MINJU 
	 *********************/
	
	 /** Affection Longue Durée */
	 private Boolean affectionLD;
	 /** Ex adhérent MMJ */
	 private Boolean exMmj;
	
	public Conjoint(Date date_de_naissance,  Etat etat, Activite activite) throws InitialisationMoteurException {
		super();
		if(date_de_naissance==null)throw new InitialisationMoteurException("date_de_naissance==null->obligatoire");
		this.date_de_naissance = date_de_naissance;
		this.etat = etat;
		this.activite = activite;
	}



	public void setRegime_alsace_moselle(boolean regime_alsace_moselle) {
		this.regime_alsace_moselle = regime_alsace_moselle;
	}



	public Etat getEtat() {
		return etat;
	}



	public Activite getActivite() {
		return activite;
	}



	public Date getDate_de_naissance() {
		return date_de_naissance;
	}

	public Boolean getAffectionLD() {
		return affectionLD;
	}



	public void setAffectionLD(Boolean affectionLD) {
		this.affectionLD = affectionLD;
	}



	public Boolean getExMmj() {
		return exMmj;
	}



	public void setExMmj(Boolean exMmj) {
		this.exMmj = exMmj;
	}



	public boolean isRegime_alsace_moselle() {
		return regime_alsace_moselle;
	}



	@Override
	public String toString() {
		return "Conjoint [date_de_naissance=" + date_de_naissance + ", regime_alsace_moselle=" + regime_alsace_moselle
				+ ", etat=" + etat + ", activite=" + activite + "]";
	}

	
	
}
