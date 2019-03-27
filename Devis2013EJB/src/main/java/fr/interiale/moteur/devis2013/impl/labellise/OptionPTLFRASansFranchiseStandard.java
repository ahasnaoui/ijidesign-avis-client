package fr.interiale.moteur.devis2013.impl.labellise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;

public class OptionPTLFRASansFranchiseStandard  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2449693710521446986L;
	private BigDecimal[] cotisations=new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	
	public void setCotisation(BigDecimal cotisation, choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations[0]=cotisation;
			break;
		case NIV2:
			cotisations[1]=cotisation;
			break;
		case NIV3:
			cotisations[2]=cotisation;
			break;
		case NIV4:
			cotisations[3]=cotisation;
			break;
		default:
			cotisations[0]=cotisation;
			break;
		}
	}
	
	public BigDecimal getCotisation() {

		switch (prevoyance) {
		case NIV1:
			return cotisations[0];
		case NIV2:
			return cotisations[1];
		case NIV3:
			return cotisations[2];
		case NIV4:
			return cotisations[3];
		default:
			return cotisations[0];
		}
	}

	public BigDecimal[] getCotisations() {
		return cotisations;
	}

	public void setCotisations(BigDecimal[] cotisations) {
		this.cotisations = cotisations;
	}

	public choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	public void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}
	
	public OptionPTLFRASansFranchiseStandard() {
		
	}

	@Override
	public String toString() {
		return "OptionPTLFRASansFranchiseStandard [cotisations=" + Arrays.toString(cotisations) + ", prevoyance="
				+ prevoyance + "]";
	}
	
	
}
