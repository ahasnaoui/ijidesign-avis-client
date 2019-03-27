package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProduitFamille extends ProduitCouple implements IProduitFamille, Serializable {

	public ProduitFamille() {
		super();
	}

	

	List<BigDecimal> cotisations_base_enfant = new ArrayList<BigDecimal>();
	private boolean selectParEnfant = false;

	public BigDecimal getCotisation_totale_enfants() {
		BigDecimal cotisation_totale_enfants = BigDecimal.ZERO;
		for (BigDecimal cotisation_base_enfant : cotisations_base_enfant) {
			cotisation_totale_enfants = (cotisation_totale_enfants.add(cotisation_base_enfant));
		}
		return cotisation_totale_enfants;
	}

	public List<BigDecimal> getCotisations_base_enfant() {
		return cotisations_base_enfant;
	}

	public void setCotisation_base_enfant(BigDecimal cotisation_base_enfant) {
		setSelectParEnfant(true);
		this.cotisations_base_enfant.add(cotisation_base_enfant);
	}

	public boolean isSelectParEnfant() {
		return selectParEnfant;
	}

	public void setSelectParEnfant(boolean selectParEnfant) {
		this.selectParEnfant = selectParEnfant;
	}

	public BigDecimal getCotisation() {

		BigDecimal cotisation = super.getCotisation();
		if (isSelectParEnfant())
			cotisation = cotisation.add(getCotisation_totale_enfants());
		return cotisation;
	}

	@Override
	public String toString() {
		return super.toString() + " ProduitFamille [cotisations_base_enfant=" + cotisations_base_enfant
				+ ", selectParEnfant=" + selectParEnfant + "]";
	}


}
