package fr.interiale.moteur.devis2013.impl.commun;

import java.util.List;

import fr.interiale.moteur.devis2013.interfaces.IProduitSante;

public interface IReponse {
	
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getProduits_sante()
	 */
	public abstract List<IProduitSante> getProduits_sante();

}
