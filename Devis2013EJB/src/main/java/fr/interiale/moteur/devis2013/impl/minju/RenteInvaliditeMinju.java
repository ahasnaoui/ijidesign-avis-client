package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;

public class RenteInvaliditeMinju implements Serializable{
	
	private static final long serialVersionUID = -5692198530523771736L;
	private  BigDecimal cotisationAdh=BigDecimal.ZERO;
	private  BigDecimal cotisationConjoint=BigDecimal.ZERO;
	private choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	private   BigDecimal[] cotisations_rente= new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	
	public RenteInvaliditeMinju() {
		
	}

	public void setCotisation_rente(BigDecimal cotisation_rente,choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations_rente[0]=cotisation_rente;
			 break;
		case NIV2:
			cotisations_rente[1]=cotisation_rente;
			 break;
		case NIV3:
			cotisations_rente[2]=cotisation_rente;
			 break;
		case NIV4:
			cotisations_rente[3]=cotisation_rente;
			break;
		default:
			cotisations_rente[0]=cotisation_rente;
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
		switch (prevoyance) {
		case NIV1:
			return cotisations_rente[0];
		case NIV2:
			return cotisations_rente[1];
		case NIV3:
			return cotisations_rente[2];
		case NIV4:
			return cotisations_rente[3];
		default:
			return cotisations_rente[0];
		}
	}


	public BigDecimal getCotisationAdh() {
		return cotisationAdh;
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
