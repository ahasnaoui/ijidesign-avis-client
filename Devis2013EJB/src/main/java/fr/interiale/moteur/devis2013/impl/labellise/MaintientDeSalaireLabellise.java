package fr.interiale.moteur.devis2013.impl.labellise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;

public class MaintientDeSalaireLabellise implements Serializable, IMaintientDeSalaire {

	private static final long serialVersionUID = -2263878144367015783L;
	private BigDecimal[] cotisations_maintient_de_salaire = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	private BigDecimal[] cotisations_jour_de_carence = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	private BigDecimal[] cotisations_maintient_de_salaire_inval = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	private BigDecimal assistance_prevoyance=BigDecimal.ZERO;
		
	choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	
	
	public BigDecimal[] getCotisations_maintient_de_salaire_inval() {
		return cotisations_maintient_de_salaire_inval;
	}

	public void setCotisations_maintient_de_salaire_inval(BigDecimal[] cotisations_maintient_de_salaire_inval) {
		this.cotisations_maintient_de_salaire_inval = cotisations_maintient_de_salaire_inval;
	}

	public BigDecimal getAssistance_prevoyance() {
		return assistance_prevoyance;
	}

	public void setAssistance_prevoyance(BigDecimal assistance_prevoyance) {
		this.assistance_prevoyance = assistance_prevoyance;
	}

	protected choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	protected void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}

	public MaintientDeSalaireLabellise(BigDecimal[] cotisation) {
		super();
		this.cotisations_maintient_de_salaire = cotisation;
	}

	public MaintientDeSalaireLabellise() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire#getCotisation
	 * ()
	 */
	public BigDecimal getCotisation() {

		switch (prevoyance) {
		case NIV1:
			return cotisations_maintient_de_salaire[0].add(cotisations_maintient_de_salaire_inval[0]).add(assistance_prevoyance);
		case NIV2:
			return cotisations_maintient_de_salaire[1].add(cotisations_maintient_de_salaire_inval[1]).add(assistance_prevoyance);
		case NIV3:
			return cotisations_maintient_de_salaire[2].add(cotisations_maintient_de_salaire_inval[2]).add(assistance_prevoyance);
		case NIV4:
			return cotisations_maintient_de_salaire[3].add(cotisations_maintient_de_salaire_inval[3]).add(cotisations_jour_de_carence[3]).add(assistance_prevoyance);
		default:
			return cotisations_maintient_de_salaire[0].add(cotisations_maintient_de_salaire_inval[0]).add(assistance_prevoyance);
		}
	}

	public BigDecimal[] getCotisation_maintient_de_salaire() {
		return cotisations_maintient_de_salaire;
	}

	public void setCotisation_maintient_de_salaire_inval(BigDecimal cotisation_maintient_de_salaire_inval, choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations_maintient_de_salaire_inval[0]=cotisation_maintient_de_salaire_inval;
			break;
		case NIV2:
			cotisations_maintient_de_salaire_inval[1]=cotisation_maintient_de_salaire_inval;
			break;
		case NIV3:
			cotisations_maintient_de_salaire_inval[2]=cotisation_maintient_de_salaire_inval;
			break;
		case NIV4:
			cotisations_maintient_de_salaire_inval[3]=cotisation_maintient_de_salaire_inval;
			break;
		default:
			cotisations_maintient_de_salaire_inval[0]=cotisation_maintient_de_salaire_inval;
			break;
		}
	}
	
	public void setCotisation_maintient_de_salaire(BigDecimal cotisation_maintient_de_salaire, choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations_maintient_de_salaire[0]=cotisation_maintient_de_salaire;
			break;
		case NIV2:
			cotisations_maintient_de_salaire[1]=cotisation_maintient_de_salaire;
			break;
		case NIV3:
			cotisations_maintient_de_salaire[2]=cotisation_maintient_de_salaire;
			break;
		case NIV4:
			cotisations_maintient_de_salaire[3]=cotisation_maintient_de_salaire;
			break;
		default:
			cotisations_maintient_de_salaire[0]=cotisation_maintient_de_salaire;
			break;
		}
	}
	
	public void setCotisation_jour_de_carence(BigDecimal cotisation_jour_de_carence, choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations_jour_de_carence[0]=cotisation_jour_de_carence;
		case NIV2:
			cotisations_jour_de_carence[1]=cotisation_jour_de_carence;
		case NIV3:
			cotisations_jour_de_carence[2]=cotisation_jour_de_carence;
		case NIV4:
			cotisations_jour_de_carence[3]=cotisation_jour_de_carence;
		default:
			cotisations_jour_de_carence[0]=cotisation_jour_de_carence;
		}
	}
	

	public BigDecimal[] getCotisations_maintient_de_salaire() {
		return cotisations_maintient_de_salaire;
	}

	public void setCotisations_maintient_de_salaire(BigDecimal[] cotisations_maintient_de_salaire) {
		this.cotisations_maintient_de_salaire = cotisations_maintient_de_salaire;
	}

	public BigDecimal[] getCotisations_jour_de_carence() {
		return cotisations_jour_de_carence;
	}

	public void setCotisations_jour_de_carence(BigDecimal[] cotisations_jour_de_carence) {
		this.cotisations_jour_de_carence = cotisations_jour_de_carence;
	}

	@Override
	public String toString() {
		return "MaintientDeSalaireLabellise [cotisations_maintient_de_salaire="
				+ Arrays.toString(cotisations_maintient_de_salaire) + ", cotisations_jour_de_carence="
				+ Arrays.toString(cotisations_jour_de_carence) + ", cotisations_maintient_de_salaire_inval="
				+ Arrays.toString(cotisations_maintient_de_salaire_inval) + ", assistance_prevoyance="
				+ assistance_prevoyance + ", prevoyance=" + prevoyance + "]";
	}

	
}
