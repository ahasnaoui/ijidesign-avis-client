package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;

public class GarantieDecesAmeliorePN implements Serializable, ICotisationAvecSeuil {

	private static final long serialVersionUID = 294737472805957983L;
	private Map<Float, BigDecimal> cotisation_deces_seul = new HashMap<Float, BigDecimal>(7);
	private Map<Float, BigDecimal> cotisation_deces_accidentel = new HashMap<Float, BigDecimal>(7);
	private boolean select = false;
	private final static float COEF_COTISATIONS = 3;
	

	private Set<String> code=new HashSet<String>();

	
	
	
	public GarantieDecesAmeliorePN() {
	}

	public Collection<Float> getSeuils(){
		return cotisation_deces_seul.keySet();
	}
	
	// NSA modified (*3 seuil accidentel)
	public BigDecimal getCotisation(Float seuil) {
		return cotisation_deces_seul.get(seuil).add(cotisation_deces_accidentel.get(seuil*COEF_COTISATIONS));
	}

	public void setCotisation_deces_seul(BigDecimal cotisation, Float seuil) {
		select = true;
		this.cotisation_deces_seul.put(seuil, cotisation);
	}

	public void setCotisation_deces_accidentel(BigDecimal cotisation, Float seuil) {
		select = true;
		this.cotisation_deces_accidentel.put(seuil, cotisation);
	}

	public boolean isSelect() {
		return select;
	}

	@Override
	public String toString() {
		return "GarantieDecesAmeliorePN "+getCode()+" [cotisation_deces_seul=" + cotisation_deces_seul
				+ ", cotisation_deces_accidentel=" + cotisation_deces_accidentel + ", select=" + select + "]";
	}

	public String getCode() {
		return WebServiceWS.getCode(code);
	}

	public void addCode(String code) {
		this.code.add(code);
	}

	
}
