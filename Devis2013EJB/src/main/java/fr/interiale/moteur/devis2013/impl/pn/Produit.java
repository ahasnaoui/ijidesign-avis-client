package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;

public class Produit implements IProduit, Serializable {

	public Produit() {}
	
	private Set<String> code=new HashSet<String>();
	
	BigDecimal cotisation= BigDecimal.ZERO;
	private boolean select = false;
	public BigDecimal getCotisation() {
		return cotisation;
	}
	public void setCotisation(BigDecimal cotisation) {
		setSelect(true);
		this.cotisation = cotisation;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	@Override
	public String toString() {
		return "Produit "+getCode()+"[cotisation=" + cotisation + ", select=" + select + "]";
	}

	public String getCode() {
		return WebServiceWS.getCode(code);
	}

	public void addCode(String code) {
		this.code.add(code);
	}
}