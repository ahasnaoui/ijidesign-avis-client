package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;

public class ProduitCouple implements IProduitCouple,ICotisationCouple, Serializable{

	private static final long serialVersionUID = -3238194076175918682L;
	private boolean selectParAdherent=false;
	private boolean selectParConjoint=false;
	private BigDecimal cotisation_base_adherent = BigDecimal.ZERO;
	private BigDecimal cotisation_base_conjoint = BigDecimal.ZERO;
	
	private Set<String> code=new HashSet<String>();
	
	public ProduitCouple() {}
	
	@Override
	public BigDecimal getCotisation() {
		
		BigDecimal cotisation=BigDecimal.ZERO;
		
		if(isSelectParAdherent())cotisation=cotisation.add(getCotisation_base_adherent());
		if(isSelectParConjoint())cotisation=cotisation.add(getCotisation_base_conjoint());
		return cotisation;
	}

	public boolean isSelectParAdherent() {
		return selectParAdherent;
	}

	public void setSelectParAdherent(boolean selectParAdherent) {
		this.selectParAdherent = selectParAdherent;
	}

	public boolean isSelectParConjoint() {
		return selectParConjoint;
	}

	public void setSelectParConjoint(boolean selectParConjoint) {
		this.selectParConjoint = selectParConjoint;
	}

	public BigDecimal getCotisation_base_adherent() {
		return cotisation_base_adherent;
	}

	public void setCotisation_base_adherent(BigDecimal cotisation_base_adherent) {
		selectParAdherent=true;
		this.cotisation_base_adherent = cotisation_base_adherent;
	}

	public BigDecimal getCotisation_base_conjoint() {
		return cotisation_base_conjoint;
	}

	public void setCotisation_base_conjoint(BigDecimal cotisation_base_conjoint) {
		selectParConjoint=true;
		this.cotisation_base_conjoint = cotisation_base_conjoint;
	}
	@Override
	public String toString() {
		return "ProduitCouple "+getCode()+" [selectParAdherent=" + selectParAdherent + ", selectParConjoint=" + selectParConjoint
				+ ", cotisation_base_adherent=" + cotisation_base_adherent + ", cotisation_base_conjoint="
				+ cotisation_base_conjoint + "]";
	}

	public String getCode() {
		return WebServiceWS.getCode(code);
	}

	public void addCode(String code) {
		this.code.add(code);
	}

	// NSA added
	public boolean isSelect() {
		return this.isSelectParAdherent() || this.isSelectParConjoint();
	}
		
}
