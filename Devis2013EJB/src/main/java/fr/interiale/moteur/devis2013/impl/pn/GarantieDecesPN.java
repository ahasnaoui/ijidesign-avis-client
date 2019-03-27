package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;

public class GarantieDecesPN implements Serializable,ICotisation{

	private boolean select=false;

	private static final long serialVersionUID = 294737472805957983L;
	private   BigDecimal cotisation_deces_seul=BigDecimal.ZERO;
	private   BigDecimal cotisation_deces_accidentel=BigDecimal.ZERO;
	
	private Set<String> code=new HashSet<String>();
	
	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public GarantieDecesPN() {
		
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
		select=true;
		this.cotisation_deces_seul = cotisation_deces_seul;
	}

	public BigDecimal getCotisation_deces_accidentel() {
		return cotisation_deces_accidentel;
	}

	public void setCotisation_deces_accidentel(BigDecimal cotisation_deces_accidentel) {
		select=true;
		this.cotisation_deces_accidentel = cotisation_deces_accidentel;
	}

	@Override
	public String toString() {
		return "GarantieDeces  : "+getCotisation()+"[cotisation_deces_seul=" + cotisation_deces_seul + ", cotisation_deces_accidentel="
				+ cotisation_deces_accidentel + "]";
	}

	public String getCode() {
		return WebServiceWS.getCode(code);
	}

	public void addCode(String code) {
		this.code.add(code);
	}

	
	
}
