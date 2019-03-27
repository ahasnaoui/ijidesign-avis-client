package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;

public class MaintientDeSalaireMinju implements Serializable, IMaintientDeSalaire{

	private static final long serialVersionUID = -2263878144367015783L;
	private  BigDecimal cotisation_maintient_de_salaire=BigDecimal.ZERO;
	private  BigDecimal cotisation_jour_de_carence=BigDecimal.ZERO;
	choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	private   BigDecimal[] cotisations_maintiens= new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };
	public MaintientDeSalaireMinju(BigDecimal cotisation) {
		super();
		this.cotisation_maintient_de_salaire = cotisation;
	}
	
	public MaintientDeSalaireMinju() {}
	
	public void setCotisations_maintien(BigDecimal cotisations_maintien,choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			cotisations_maintiens[0]=cotisations_maintien;
			 break;
		case NIV2:
			cotisations_maintiens[1]=cotisations_maintien;
			 break;
		case NIV3:
			cotisations_maintiens[2]=cotisations_maintien;
			 break;
		case NIV4:
			cotisations_maintiens[3]=cotisations_maintien;
			break;
		default:
			cotisations_maintiens[0]=cotisations_maintien;
			 break;
		}
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire#getCotisation()
	 */
	public BigDecimal getCotisation() {
		switch (prevoyance) {
		case NIV1:
			return cotisations_maintiens[0];
		case NIV2:
			return cotisations_maintiens[1];
		case NIV3:
			return cotisations_maintiens[2];
		case NIV4:
			return cotisations_maintiens[3];
		default:
			return cotisations_maintiens[0];
		}
		}
	

	public BigDecimal getCotisation_maintient_de_salaire() {
		return cotisation_maintient_de_salaire;
	}

	public BigDecimal getCotisation_jour_de_carence() {
		return cotisation_jour_de_carence;
	}

	public void setCotisation_maintient_de_salaire(BigDecimal cotisation_maintient_de_salaire) {
		this.cotisation_maintient_de_salaire = cotisation_maintient_de_salaire;
	}

	public void setCotisation_jour_de_carence(BigDecimal cotisation_jour_de_carence) {
		this.cotisation_jour_de_carence = cotisation_jour_de_carence;
	}
	
	protected void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}

	@Override
	public String toString() {
		return "MaintientDeSalaire  : "+getCotisation()+"[cotisation_maintient_de_salaire=" + cotisation_maintient_de_salaire
				+ ", cotisation_jour_de_carence=" + cotisation_jour_de_carence + "]";
	}
	
}
