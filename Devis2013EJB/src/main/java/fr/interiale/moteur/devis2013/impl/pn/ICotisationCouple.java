package fr.interiale.moteur.devis2013.impl.pn;

import java.math.BigDecimal;

public interface ICotisationCouple{
	public abstract BigDecimal getCotisation_base_adherent();
	public abstract BigDecimal getCotisation_base_conjoint();
	public BigDecimal getCotisation();
	public boolean isSelectParAdherent();
	public boolean isSelectParConjoint();
	public String getCode();
}
