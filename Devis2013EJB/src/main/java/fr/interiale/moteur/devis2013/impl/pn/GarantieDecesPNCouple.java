package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;

public class GarantieDecesPNCouple implements Serializable,ICotisationCouple{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3875978941987227809L;

	private GarantieDecesPN garantieDecesPNAdherent;
	
	private GarantieDecesPN garantieDecesPNconjoint;

	
	
	public GarantieDecesPNCouple(GarantieDecesPN garantieDecesPNAdherent, GarantieDecesPN garantieDecesPNconjoint) {
		super();
		this.garantieDecesPNAdherent = garantieDecesPNAdherent;
		this.garantieDecesPNconjoint = garantieDecesPNconjoint;
	}

	public GarantieDecesPNCouple() {
		
	}

	public GarantieDecesPN getGarantieDecesPNAdherent() {
		return garantieDecesPNAdherent;
	}

	public void setGarantieDecesPNAdherent(GarantieDecesPN garantieDecesPNAdherent) {
		this.garantieDecesPNAdherent = garantieDecesPNAdherent;
	}

	public GarantieDecesPN getGarantieDecesPNconjoint() {
		return garantieDecesPNconjoint;
	}

	public void setGarantieDecesPNconjoint(GarantieDecesPN garantieDecesPNconjoint) {
		this.garantieDecesPNconjoint = garantieDecesPNconjoint;
	}

	@Override
	public String toString() {
		return "GarantieDecesPNCouple "+getCode() +"[garantieDecesPNAdherent=" + garantieDecesPNAdherent
				+ ", garantieDecesPNconjoint=" + garantieDecesPNconjoint + "]";
	}

	@Override
	public BigDecimal getCotisation_base_adherent() {
		
		return garantieDecesPNAdherent.getCotisation();
	}

	@Override
	public BigDecimal getCotisation_base_conjoint() {
		return garantieDecesPNconjoint.getCotisation();
	}

	@Override
	public BigDecimal getCotisation() {
	
		return getCotisation_base_adherent().add(getCotisation_base_conjoint());
	}

	@Override
	public boolean isSelectParAdherent() {
		
		return garantieDecesPNAdherent.isSelect();
	}

	@Override
	public boolean isSelectParConjoint() {
		
		return garantieDecesPNconjoint.isSelect();
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return "[A-"+garantieDecesPNAdherent.getCode()+"]+[C"+garantieDecesPNconjoint.getCode()+"]";
	}
	
	
}
