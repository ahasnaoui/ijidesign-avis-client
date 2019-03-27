package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.util.List;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.commun.IReponse;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;

public class ReponsePN implements Serializable,IReponse{

	private static final long serialVersionUID = -2584014274611546041L;

	private RatingData ratingData; 
	
	private  List<IProduitSante> produits_sante;
	
	private  MaintientDeSalairePN maintient_de_salaire;
	
	private  FraisObsequePNFamille frais_obseque;
	
	private GarantieDecesAccidentelPNAdherent garantie_deces_accidentel;
	
	private  DependancePNCouple dependance;

	// DCR2R : Facultatif : Capital DC/PTIA : DC toutes causes amélioré pour le MP ou le Cjt > 70 ans
	private  GarantieDeces70ansPNCouple garantie_deces_70;
	
	// Facultatif : Primes et indemnites (?)
	private  MaintienDesPrimesPN maintien_des_primes;
	
	// 1000 euro
	private  GarantieDecesPNCouple garantie_deces_base=new GarantieDecesPNCouple();
	
	// 7400 euro
	private GarantieDecesPNCouple garantie_deces_temp=new GarantieDecesPNCouple();
	
	// Facultatif : Capital DC/PTIA : DC/PTIA amélioré pour le MP/Conjoint <=70 ans
	private  GarantieDecesAmeliorePNCouple garantie_deces_complementaire = new GarantieDecesAmeliorePNCouple();

		
	private Produit renfort_deces;

	public ReponsePN(RatingData ratingData, List<IProduitSante> produits_sante,
			MaintientDeSalairePN maintient_de_salaire, FraisObsequePNFamille frais_obseque,
			GarantieDecesAccidentelPNAdherent garantie_deces_accidentel, DependancePNCouple dependance,
			GarantieDeces70ansPNCouple garantie_deces_70, MaintienDesPrimesPN maintien_des_primes,
			GarantieDecesPNCouple garantie_deces_base_couple, GarantieDecesPNCouple garantie_deces_temp_couple,
			GarantieDecesAmeliorePNCouple garantie_deces_complementaire_couple,Produit renfort_deces) {
		super();
		this.ratingData = ratingData;
		this.produits_sante = produits_sante;
		this.maintient_de_salaire = maintient_de_salaire;
		this.frais_obseque = frais_obseque;
		this.garantie_deces_accidentel = garantie_deces_accidentel;
		this.dependance = dependance;
		this.garantie_deces_70 = garantie_deces_70;
		this.maintien_des_primes = maintien_des_primes;
		this.garantie_deces_base = garantie_deces_base_couple;
		this.garantie_deces_temp = garantie_deces_temp_couple;
		this.garantie_deces_complementaire = garantie_deces_complementaire_couple;
		this.renfort_deces=renfort_deces;
	}

	
	
	public Produit getRenfort_deces() {
		return renfort_deces;
	}



	public void setRenfort_deces(Produit renfort_deces) {
		this.renfort_deces = renfort_deces;
	}



	public GarantieDecesPNCouple getGarantie_deces_temp() {
		return garantie_deces_temp;
	}

	public void setGarantie_deces_temp(GarantieDecesPNCouple garantie_deces_temp) {
		this.garantie_deces_temp = garantie_deces_temp;
	}

	public GarantieDecesAmeliorePNCouple getGarantie_deces_complementaire() {
		return garantie_deces_complementaire;
	}

	public void setGarantie_deces_complementaire(GarantieDecesAmeliorePNCouple garantie_deces_complementaire) {
		this.garantie_deces_complementaire = garantie_deces_complementaire;
	}

	public GarantieDecesPNCouple getGarantie_deces_base() {
		return garantie_deces_base;
	}

	public GarantieDecesAccidentelPNAdherent getGarantie_deces_accidentel() {
		return garantie_deces_accidentel;
	}


	public void setGarantie_deces_accidentel(GarantieDecesAccidentelPNAdherent garantie_deces_accidentel) {
		this.garantie_deces_accidentel = garantie_deces_accidentel;
	}


	public void setGarantie_deces_70(GarantieDeces70ansPNCouple garantie_deces_70) {
		this.garantie_deces_70 = garantie_deces_70;
	}

	public RatingData getRatingData() {
		return ratingData;
	}

	public void setRatingData(RatingData ratingData) {
		this.ratingData = ratingData;
	}

	public FraisObsequePNFamille getFrais_obseque() {
		return frais_obseque;
	}

	public DependancePNCouple getDependance() {
		return dependance;
	}


	public MaintientDeSalairePN getMaintient_de_salaire() {
		return maintient_de_salaire;
	}

	public GarantieDeces70ansPNCouple getGarantie_deces_70() {
		return garantie_deces_70;
	}

	public MaintienDesPrimesPN getMaintien_des_primes() {
		return maintien_des_primes;
	}

	public List<IProduitSante> getProduits_sante() {
		return produits_sante;
	}


	public void setMaintien_des_primes(MaintienDesPrimesPN maintien_des_primes) {
		this.maintien_des_primes = maintien_des_primes;
	}

	@Override
	public String toString() {
		return "ReponsePN [ produits_sante=" + produits_sante + "\n maintient_de_salaire="
				+ maintient_de_salaire + "\n frais_obseque=" + frais_obseque + "\n garantie_deces_accidentel="
				+ garantie_deces_accidentel + "\n dependance=" + dependance + "\n garantie_deces_70=" + garantie_deces_70
				+ "\n maintien_des_primes=" + maintien_des_primes + "\n garantie_deces_base=" + garantie_deces_base
				+ "\n garantie_deces_temp=" + garantie_deces_temp + "\n garantie_deces_complementaire="
				+ garantie_deces_complementaire + "]";
	}

		
}
