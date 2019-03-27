package fr.interiale.moteur.devis2013.impl.pn;

import java.math.BigDecimal;

public interface ICotisationAvecSeuil {
	public BigDecimal getCotisation(Float seuil);
	public boolean isSelect();
	public String getCode();
	
}
