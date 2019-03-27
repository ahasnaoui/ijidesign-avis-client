package fr.interiale.moteur.devis2013.impl.labellise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.interfaces.IGarantieDeces;

public class GarantieDecesLabellise implements Serializable, IGarantieDeces{

	private static final long serialVersionUID = 55960527239974902L;
	private   BigDecimal[] cotisations_deces= new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	private choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	private BigDecimal assistance_prevoyance=BigDecimal.ZERO;
	
	public GarantieDecesLabellise(BigDecimal[] cotisation) {
		super();
		this.cotisations_deces = cotisation;
	}

	public GarantieDecesLabellise() {
		
	}

	public BigDecimal getAssistance_prevoyance() {
		return assistance_prevoyance;
	}

	public void setAssistance_prevoyance(BigDecimal assistance_prevoyance) {
		this.assistance_prevoyance = assistance_prevoyance;
	}

	public void setCotisations_deces(BigDecimal[] cotisations_deces) {
		this.cotisations_deces = cotisations_deces;
	}

	public BigDecimal[] getCotisations_deces() {
		return cotisations_deces;
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

	protected choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	protected void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}

	public BigDecimal getCotisation() {
		switch (prevoyance) {
		case NIV1:
			return cotisations_deces[0].add(assistance_prevoyance);
		case NIV2:
			return cotisations_deces[1].add(assistance_prevoyance);
		case NIV3:
			return cotisations_deces[2].add(assistance_prevoyance);
		case NIV4:
			return cotisations_deces[3].add(assistance_prevoyance);
		default:
			return cotisations_deces[0].add(assistance_prevoyance);
		}
	}

	@Override
	public String toString() {
		return "GarantieDecesLabellise [cotisations_deces=" + Arrays.toString(cotisations_deces) + ", prevoyance="
				+ prevoyance + ", assistance_prevoyance=" + assistance_prevoyance + "]";
	}
}
