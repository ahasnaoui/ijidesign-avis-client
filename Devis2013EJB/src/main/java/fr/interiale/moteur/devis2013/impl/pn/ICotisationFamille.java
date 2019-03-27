package fr.interiale.moteur.devis2013.impl.pn;

import java.math.BigDecimal;

public interface ICotisationFamille {

	public abstract BigDecimal getCotisation_totale_enfants();

	public abstract BigDecimal getCotisation_base_adherent();

	public abstract BigDecimal getCotisation_base_conjoint();

	public String getCode();
}

