package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;
import java.util.List;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.interiale.DependanceTotale;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.labellise.OptionMaintientSalaire;
import fr.interiale.moteur.devis2013.impl.pn.Produit;
import fr.interiale.moteur.devis2013.interfaces.IGarantieCouverturePrime;
import fr.interiale.moteur.devis2013.interfaces.IGarantieDeces;
import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;
import fr.interiale.moteur.devis2013.interfaces.IPack;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;



public class ReponseMINJU implements IReponseMINJU, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private static final long serialVersionUID = -2584014274611546041L;

	/**
	 * serialVersionUID
	 */
	//private static final long serialVersionUID = 2511133803474275392L;

	/**
	 * 
	 */

	private RatingData ratingData; 
	
	private final List<IProduitSante> produits_sante;
	
	private final IPack pack_pharmaPlus;
//	
//	
	private final GarantieCouverturePrimeMinju garantie_couverture_prime;
//	
	private final GarantieDecesMinju garantie_deces;
	
	private final MaintientDeSalaireMinju maintient_de_salaire;
	
	private final OptionMaintientSalaire option_maintient_salaire;

	private final DependanceTotale dependanceTotale;
	
	private Produit renfort_deces;
	
	private final InvaliditePermanenteMinju invalidite_permanente;
	
	private final RenteInvaliditeMinju rente_invalidite;
	
	public DependanceTotale getDependanceTotale() {
		return dependanceTotale;
	}


	
	public ReponseMINJU(List<IProduitSante> produits_sante,IPack pack_pharmaPlus, GarantieCouverturePrimeMinju garantie_couverture_prime,
			GarantieDecesMinju garantie_deces, MaintientDeSalaireMinju maintient_de_salaire,OptionMaintientSalaire option_maintient_salaire, InvaliditePermanenteMinju invalidite_permanente, RenteInvaliditeMinju rente_invalidite, DependanceTotale dependanceTotale) {
		
		this.produits_sante=produits_sante;
		this.pack_pharmaPlus = pack_pharmaPlus;
		this.garantie_couverture_prime = garantie_couverture_prime;
		this.garantie_deces = garantie_deces;
		this.maintient_de_salaire = maintient_de_salaire;
		this.invalidite_permanente = invalidite_permanente;
		this.option_maintient_salaire = option_maintient_salaire;
		this.rente_invalidite = rente_invalidite;
		this.dependanceTotale = dependanceTotale;
	}

	public void setPrevoyance(choix_prevoyance prevoyance) {
		if (garantie_deces != null)
			garantie_deces.setPrevoyance(prevoyance);
		if (maintient_de_salaire != null)
			maintient_de_salaire.setPrevoyance(prevoyance);
		if (rente_invalidite != null)
			rente_invalidite.setPrevoyance(prevoyance);
		if(garantie_couverture_prime != null) {
			garantie_couverture_prime.setPrevoyance(prevoyance);
		}
		if(invalidite_permanente != null) {
			invalidite_permanente.setPrevoyance(prevoyance);
		}
		
	}
	
	public Produit getRenfort_deces() {
		return renfort_deces;
}
//
//
//
	public void setRenfort_deces(Produit renfort_deces) {
		this.renfort_deces = renfort_deces;
	}



	



	/**
	 * @return the option_maintient_salaire
	 */
	public OptionMaintientSalaire getOption_maintient_salaire() {
		return option_maintient_salaire;
	}



	/**
	 * @return the invalidite_permanente
	 */
	public InvaliditePermanenteMinju getInvalidite_permanente() {
		return invalidite_permanente;
	}



	/**
	 * @return the rente_invalidite
	 */
	public RenteInvaliditeMinju getRente_invalidite() {
		return rente_invalidite;
	}



	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getProduits_sante()
	 */
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#getProduits_sante()
	 */
	public List<IProduitSante> getProduits_sante() {
		return produits_sante;
	}

	

	

//	/**
//	 * @return the pack_pharmaPlus
//	 */
//	public IPack getPack_pharmaPlus() {
//		return pack_pharmaPlus;
//	}
//
//
//
//	/* (non-Javadoc)
//	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getGarantie_couverture_prime()
//	 */
	public IGarantieCouverturePrime getGarantie_couverture_prime() {
		return garantie_couverture_prime;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getGarantie_deces()
	 */
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#getGarantie_deces()
	 */
	public IGarantieDeces getGarantie_deces() {
		return garantie_deces;
	}
//
//	/* (non-Javadoc)
//	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getMaintient_de_salaire()
//	 */
//	/* (non-Javadoc)
//	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#getMaintient_de_salaire()
//	 */
	public IMaintientDeSalaire getMaintient_de_salaire() {
		return maintient_de_salaire;
	}

	@Override
	public String toString() {
		return "ReponseMINJU [ratingData=" + ratingData + ", produits_sante=" + produits_sante + ", pack_pharmaPlus="
				+ pack_pharmaPlus + ", garantie_couverture_prime=" + garantie_couverture_prime + ", garantie_deces="
				+ garantie_deces + ", maintient_de_salaire=" + maintient_de_salaire + ", dependanceTotale="
				+ dependanceTotale + ", renfort_deces=" + renfort_deces + ", invalidite_permanente="
				+ invalidite_permanente + ", rente_invalidite=" + rente_invalidite + "]";
	}



	public RatingData getRatingData() {
		return ratingData;
	}



	public void setRatingData(RatingData ratingData) {
		this.ratingData = ratingData;
	}



	@Override
	public IPack getPack_pharmaPlus() {
		return this.pack_pharmaPlus;
	}
	
	
	
}
