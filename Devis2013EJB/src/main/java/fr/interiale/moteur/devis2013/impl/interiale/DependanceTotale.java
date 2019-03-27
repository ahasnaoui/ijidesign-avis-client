package fr.interiale.moteur.devis2013.impl.interiale;

import java.io.Serializable;
import java.math.BigDecimal;

public class DependanceTotale implements Serializable{
	
	private static final long serialVersionUID = -5692198530523771736L;
	private  BigDecimal cotisationAdh=BigDecimal.ZERO;
	private  BigDecimal cotisationConjoint=BigDecimal.ZERO;
	
	public DependanceTotale() {
		
	}


	public BigDecimal getCotisation() {
		return cotisationAdh.add(cotisationConjoint);
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
