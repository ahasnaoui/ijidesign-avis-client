package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.interfaces.IGarantieDeces;

public class GarantieDecesMinju implements Serializable, IGarantieDeces{

	private static final long serialVersionUID = 55960527239974902L;
	private   BigDecimal cotisation_deces_seul=BigDecimal.ZERO;
	private   BigDecimal cotisation_deces_accidentel=BigDecimal.ZERO;
	private choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	private   BigDecimal[] cotisations_deces= new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	public GarantieDecesMinju(BigDecimal cotisation) {
		super();
		this.cotisation_deces_seul = cotisation;
	}

	public GarantieDecesMinju() {
		
	}
	
	
	public void setCotisation_deces(BigDecimal cotisation_deces,choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			 cotisations_deces[0]=cotisation_deces;
			 break;
		case NIV2:
			 cotisations_deces[1]=cotisation_deces;
			 break;
		case NIV3:
			 cotisations_deces[2]=cotisation_deces;
			 break;
		case NIV4:
			 cotisations_deces[3]=cotisation_deces;
			break;
		default:
			 cotisations_deces[0]=cotisation_deces;
			 break;
		}
	}
	
	public BigDecimal getCotisation() {
		switch (prevoyance) {
		case NIV1:
			return cotisations_deces[0];
		case NIV2:
			return cotisations_deces[1];
		case NIV3:
			return cotisations_deces[2];
		case NIV4:
			return cotisations_deces[3];
		default:
			return cotisations_deces[0];
		}
		}
	
	/**
	 * @return the prevoyance
	 */
	public choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	/**
	 * @param prevoyance the prevoyance to set
	 */
	public void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
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
