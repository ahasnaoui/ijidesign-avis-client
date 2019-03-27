package fr.interiale.moteur.devis2013.impl.interiale;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.interfaces.IGarantieDeces;

public class GarantieDeces implements Serializable, IGarantieDeces{

	private static final long serialVersionUID = 55960527239974902L;
	private   BigDecimal cotisation_deces_seul=BigDecimal.ZERO;
	private   BigDecimal cotisation_deces_accidentel=BigDecimal.ZERO;
	
	public GarantieDeces(BigDecimal cotisation) {
		super();
		this.cotisation_deces_seul = cotisation;
	}

	public GarantieDeces() {
		
	}
	
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IGarantieDeces#getCotisation()
	 */
	public BigDecimal getCotisation() {
		return cotisation_deces_seul.add(cotisation_deces_accidentel);
	}

	public BigDecimal getCotisation_deces_seul() {
		return cotisation_deces_seul;
	}

	public void setCotisation_deces_seul(BigDecimal cotisation_deces_seul) {
		this.cotisation_deces_seul = cotisation_deces_seul;
	}

	public BigDecimal getCotisation_deces_accidentel() {
		return cotisation_deces_accidentel;
	}

	public void setCotisation_deces_accidentel(BigDecimal cotisation_deces_accidentel) {
		this.cotisation_deces_accidentel = cotisation_deces_accidentel;
	}

	@Override
	public String toString() {
		return "GarantieDeces  : "+getCotisation()+"[cotisation_deces_seul=" + cotisation_deces_seul + ", cotisation_deces_accidentel="
				+ cotisation_deces_accidentel + "]";
	}

	
	
}
