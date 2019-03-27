package fr.interiale.moteur.devis2013.impl.interiale;

import java.util.List;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.commun.IReponse;
import fr.interiale.moteur.devis2013.impl.pn.Produit;
import fr.interiale.moteur.devis2013.interfaces.IGarantieDeces;
import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;
import fr.interiale.moteur.devis2013.interfaces.IPack;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;

public interface IReponseInteriale extends IReponse{

	public abstract IPack getPack_senior();



	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getPack_famille()
	 */
	public abstract IPack getPack_famille();

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getPack_pro()
	 */
	public abstract IPack getPack_pro();

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getGarantie_deces()
	 */
	public abstract IGarantieDeces getGarantie_deces();

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getMaintient_de_salaire()
	 */
	public abstract IMaintientDeSalaire getMaintient_de_salaire();

	public abstract String toString();
	
	public RatingData getRatingData();
	
	public DependanceTotale getDependanceTotale();
	
	public Produit getRenfort_deces();	
	
	

}