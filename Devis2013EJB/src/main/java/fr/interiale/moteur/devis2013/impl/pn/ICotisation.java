package fr.interiale.moteur.devis2013.impl.pn;

import java.math.BigDecimal;

public interface ICotisation {
	public BigDecimal getCotisation();
	
	public boolean isSelect();
	
	public String getCode();
}
