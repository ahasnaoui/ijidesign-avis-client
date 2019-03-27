package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;

public class InvaliditePermanenteMinju implements Serializable{
	
	private static final long serialVersionUID = -5692198530523771736L;
	private  BigDecimal cotisationAdh=BigDecimal.ZERO;
	private  BigDecimal cotisationConjoint=BigDecimal.ZERO;
	private choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	private   BigDecimal[] cotisations_invalidite= new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	public InvaliditePermanenteMinju() {
		
	}

	public void setCotisation_invalidite(BigDecimal cotisation_invalidite,choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			 cotisations_invalidite[0]=cotisation_invalidite;
			 break;
		case NIV2:
			 cotisations_invalidite[1]=cotisation_invalidite;
			 break;
		case NIV3:
			 cotisations_invalidite[2]=cotisation_invalidite;
			 break;
		case NIV4:
			 cotisations_invalidite[3]=cotisation_invalidite;
			break;
		default:
			 cotisations_invalidite[0]=cotisation_invalidite;
			 break;
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


	public BigDecimal getCotisation() {
		return cotisationAdh.add(cotisationConjoint);
	}


	public BigDecimal getCotisationAdh() {
		switch (prevoyance) {
		case NIV1:
			return cotisations_invalidite[0];
		case NIV2:
			return cotisations_invalidite[1];
		case NIV3:
			return cotisations_invalidite[2];
		case NIV4:
			return cotisations_invalidite[3];
		default:
			return cotisations_invalidite[0];
		}
	}


	public void setCotisationAdh(BigDecimal cotisationAdh) {
		this.cotisationAdh = cotisationAdh;
	}


	public BigDecimal getCotisationConjoint() {
		return cotisationConjoint;
	}


	public void setCotisationConjoint(BigDecimal cotisationConjoint) {
		this.cotisationConjoint = cotisationConjoint;
	}


	@Override
	public String toString() {
		return "DependanceTotale [cotisationAdh=" + cotisationAdh
				+ ", cotisationConjoint=" + cotisationConjoint + "]";
	}

	

	
	
}
