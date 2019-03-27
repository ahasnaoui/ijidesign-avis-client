package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;
import java.util.Date;

import fr.interiale.moteur.devis2013.impl.interiale.InitialisationMoteurException;

public class Enfant implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5762588075545558231L;

	private final Date date_de_naissance;

	private boolean regime_alsace_moselle;

	private boolean tarifs_preferentiels;
	
	
	public Enfant(Date date_de_naissance, boolean tarifs_preferentiels) throws InitialisationMoteurException {
		super();
		if(date_de_naissance==null)throw new InitialisationMoteurException("date_de_naissance==null->obligatoire");
		this.date_de_naissance = date_de_naissance;
		this.tarifs_preferentiels=tarifs_preferentiels;
	}


	public boolean isTarifs_preferentiels() {
		return tarifs_preferentiels;
	}


	public void setTarifs_preferentiels(boolean tarifs_preferentiels) {
		this.tarifs_preferentiels = tarifs_preferentiels;
	}


	public Date getDate_de_naissance() {
		return date_de_naissance;
	}


	public boolean isRegime_alsace_moselle() {
		return regime_alsace_moselle;
	}


	public void setRegime_alsace_moselle(boolean regime_alsace_moselle) {
		this.regime_alsace_moselle = regime_alsace_moselle;
	}


	@Override
	public String toString() {
		return "Enfant [date_de_naissance=" + date_de_naissance + ", regime_alsace_moselle=" + regime_alsace_moselle
				+ ", tarifs_preferentiels=" + tarifs_preferentiels + "]";
	}

			
	
}
