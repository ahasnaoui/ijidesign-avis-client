package fr.interiale.moteur.devis2013.impl.minju;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.commun.IReponse;
import fr.interiale.moteur.devis2013.impl.interiale.DependanceTotale;
import fr.interiale.moteur.devis2013.impl.pn.Produit;
import fr.interiale.moteur.devis2013.interfaces.IGarantieDeces;
import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;
import fr.interiale.moteur.devis2013.interfaces.IPack;

/**
 * Interface des services Minju
 * @author adil.hasnaoui
 *
 */
public interface IReponseMINJU extends IReponse{

	
	/**
	 * 
	 * @return
	 */
	public RatingData getRatingData();
	
	public abstract IGarantieDeces getGarantie_deces();
//
//	/* (non-Javadoc)
//	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getMaintient_de_salaire()
//	 */
	public abstract IMaintientDeSalaire getMaintient_de_salaire();
//
	public abstract String toString();
//	
	public abstract IPack getPack_pharmaPlus();
//	
	public DependanceTotale getDependanceTotale();
//	
	public Produit getRenfort_deces();	
	

}