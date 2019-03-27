package fr.interiale.moteur.devis2013.impl.labellise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;

public class OptionMaintientSalaire implements IMaintientDeSalaire,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 398148531727639164L;
	private BigDecimal[] cotisations_option_ITT=new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	private BigDecimal[] cotisations_option_inval=new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	
	choix_prevoyance prevoyance=choix_prevoyance.NIV1;

	

	public BigDecimal[] getCotisations_option_ITT() {
		return cotisations_option_ITT;
	}

	public void setCotisations_option_ITT(BigDecimal[] cotisations_option_ITT) {
		this.cotisations_option_ITT = cotisations_option_ITT;
	}

	public BigDecimal[] getCotisations_option_inval() {
		return cotisations_option_inval;
	}

	public void setCotisations_option_inval(BigDecimal[] cotisations_option_inval) {
		this.cotisations_option_inval = cotisations_option_inval;
	}

	public choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	public void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}

	public BigDecimal getCotisation() {

		switch (prevoyance) {
		case NIV1:
			return cotisations_option_ITT[0].add(cotisations_option_inval[0]);
			
		case NIV2:
			return cotisations_option_ITT[1].add(cotisations_option_inval[1]);
		case NIV3:
			return cotisations_option_ITT[2].add(cotisations_option_inval[2]);
		case NIV4:
			return cotisations_option_ITT[3].add(cotisations_option_inval[3]);
		default:
			return cotisations_option_ITT[0].add(cotisations_option_inval[0]);
		}
	}

	public void setCotisation_option_ITT(BigDecimal cotisation_option_ITT, choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations_option_ITT[0]=cotisation_option_ITT;
			break;
		case NIV2:
			cotisations_option_ITT[1]=cotisation_option_ITT;
			break;
		case NIV3:
			cotisations_option_ITT[2]=cotisation_option_ITT;
			break;
		case NIV4:
			cotisations_option_ITT[3]=cotisation_option_ITT;
			break;
		default:
			cotisations_option_ITT[0]=cotisation_option_ITT;
			break;
		}
	}
	
	public void setCotisation_option_inval(BigDecimal cotisation_option_inval, choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations_option_inval[0]=cotisation_option_inval;
			break;
		case NIV2:
			cotisations_option_inval[1]=cotisation_option_inval;
			break;
		case NIV3:
			cotisations_option_inval[2]=cotisation_option_inval;
			break;
		case NIV4:
			cotisations_option_inval[3]=cotisation_option_inval;
			break;
		default:
			cotisations_option_inval[0]=cotisation_option_inval;
			break;
		}
	}

	@Override
	public String toString() {
		return "OptionMaintientSalaire [cotisations_option_ITT=" + Arrays.toString(cotisations_option_ITT)
				+ ", cotisations_option_inval=" + Arrays.toString(cotisations_option_inval) + ", prevoyance="
				+ prevoyance + "]";
	}
	
	
	

	
	
	
}
