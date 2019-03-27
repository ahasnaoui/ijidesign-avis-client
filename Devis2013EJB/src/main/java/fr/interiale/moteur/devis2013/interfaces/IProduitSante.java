package fr.interiale.moteur.devis2013.interfaces;

import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante;

public interface IProduitSante {

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#toString()
	 */
	public abstract String toString();

	
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#getCotisation_adherant()
	 */
	public abstract BigDecimal getCotisation_adherent();

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#getCotisation_conjoint()
	 */
	public abstract BigDecimal getCotisation_conjoint();

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#getCotisation_totale_enfants()
	 */
	public abstract BigDecimal getCotisation_totale_enfants();

}