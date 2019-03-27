package fr.interiale.moteur.devis2013.impl.interiale;

import java.io.Serializable;
import java.util.List;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.pn.Produit;
import fr.interiale.moteur.devis2013.interfaces.IGarantieCouverturePrime;
import fr.interiale.moteur.devis2013.interfaces.IGarantieDeces;
import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;
import fr.interiale.moteur.devis2013.interfaces.IPack;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;



public class ReponseInteriale implements Serializable, IReponseInteriale{

	private static final long serialVersionUID = -2584014274611546041L;

	private RatingData ratingData; 
	
	private final List<IProduitSante> produits_sante;
	
	private final IPack pack_famille;
	
	private final IPack pack_pro;
	
	private final IPack pack_senior;
	
	private final IGarantieCouverturePrime garantie_couverture_prime;
	
	private final IGarantieDeces garantie_deces;
	
	private final IMaintientDeSalaire maintient_de_salaire;

	private final DependanceTotale dependanceTotale;
	
	private Produit renfort_deces;
	
	public DependanceTotale getDependanceTotale() {
		return dependanceTotale;
	}



	public ReponseInteriale(DependanceTotale dependanceTotale, List<IProduitSante> produits_sante,IPack pack_famille, IPack pack_pro, IGarantieCouverturePrime garantie_couverture_prime,
			IGarantieDeces garantie_deces, IMaintientDeSalaire maintient_de_salaire, IPack pack_senior,RatingData ratingData,Produit renfort_dece) {
		super();
		this.produits_sante=produits_sante;
		this.pack_famille = pack_famille;
		this.pack_pro = pack_pro;
		this.garantie_couverture_prime = garantie_couverture_prime;
		this.garantie_deces = garantie_deces;
		this.maintient_de_salaire = maintient_de_salaire;
		this.pack_senior=pack_senior;
		this.ratingData=ratingData;
		this.dependanceTotale=dependanceTotale;
		this.renfort_deces=renfort_dece;
	}

	
	
	public Produit getRenfort_deces() {
		return renfort_deces;
	}



	public void setRenfort_deces(Produit renfort_deces) {
		this.renfort_deces = renfort_deces;
	}



	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#getPack_senior()
	 */
	public IPack getPack_senior() {
		return pack_senior;
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

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getPack_famille()
	 */
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#getPack_famille()
	 */
	public IPack getPack_famille() {
		return pack_famille;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getPack_pro()
	 */
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#getPack_pro()
	 */
	public IPack getPack_pro() {
		return pack_pro;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getGarantie_couverture_prime()
	 */
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

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IReponse#getMaintient_de_salaire()
	 */
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#getMaintient_de_salaire()
	 */
	public IMaintientDeSalaire getMaintient_de_salaire() {
		return maintient_de_salaire;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IReponse#toString()
	 */
	@Override
	public String toString() {
		String retour= "Reponse : \n[produits_sante=\n" ;
		for (IProduitSante produit : produits_sante) {
			retour+=produit+"\n";
		}
		
			retour+="\n pack_famille=" + pack_famille
				+ "\n pack_pro=" + pack_pro+ "\n pack_senior=" + pack_senior + "\n garantie_couverture_prime="
				+ garantie_couverture_prime + "\n garantie_deces=" + garantie_deces + "\n maintient_de_salaire="
				+ maintient_de_salaire + "\n dependanceTotale="+dependanceTotale+"\n renfort_deces="+renfort_deces+"]";
		return retour;
	}



	public RatingData getRatingData() {
		return ratingData;
	}



	public void setRatingData(RatingData ratingData) {
		this.ratingData = ratingData;
	}
	
	
	
}
