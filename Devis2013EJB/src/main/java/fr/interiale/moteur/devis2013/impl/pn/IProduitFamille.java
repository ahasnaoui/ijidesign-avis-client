package fr.interiale.moteur.devis2013.impl.pn;

import java.math.BigDecimal;
import java.util.List;

public interface IProduitFamille extends ICotisationFamille{

	public abstract List<BigDecimal> getCotisations_base_enfant();
	
	public abstract void setCotisation_base_adherent(BigDecimal cotisation_base_adherent);

	public abstract void setCotisation_base_conjoint(BigDecimal cotisation_base_conjoint);

	public abstract void setCotisation_base_enfant(BigDecimal cotisation_base_enfant);
	
	public boolean isSelectParAdherent();
	public boolean isSelectParConjoint();
	public boolean isSelectParEnfant();

}