package fr.interiale.moteur.devis2013.impl.pn;

import java.math.BigDecimal;

public interface IProduitCouple extends ICotisationCouple{
	public abstract void setCotisation_base_adherent(BigDecimal cotisation_base_adherent);
	public abstract void setCotisation_base_conjoint(BigDecimal cotisation_base_conjoint); 
	public boolean isSelectParAdherent();
	public boolean isSelectParConjoint();
	// NSA added
	public boolean isSelect();
}