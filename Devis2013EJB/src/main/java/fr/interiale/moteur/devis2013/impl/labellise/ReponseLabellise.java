package fr.interiale.moteur.devis2013.impl.labellise;

import java.io.Serializable;
import java.util.List;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.interiale.DependanceTotale;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.interiale.IReponseInteriale;
import fr.interiale.moteur.devis2013.impl.pn.Produit;
import fr.interiale.moteur.devis2013.interfaces.IPack;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;

public class ReponseLabellise implements IReponseLabellise, IReponseInteriale, Serializable {

	private static final long serialVersionUID = -6965251717797733831L;

	private final List<IProduitSante> produits_sante;

	private final IPack pack_famille;

	private final IPack pack_pro;

	private final IPack pack_senior;
	private final OptionMaintientSalaire option_maintient_salaire;

	private final GarantieDecesLabellise garantie_deces;

	private final MaintientDeSalaireLabellise maintient_de_salaire;

	private final OptionPTLFRASansFranchiseRenfort optionPTLFRASansFranchiseRenfort;

	private final OptionPTLFRASansFranchiseStandard optionPTLFRASansFranchiseStandard;

	private final DependanceTotale dependanceTotale;

	private RatingData ratingData;

	public ReponseLabellise(DependanceTotale dependanceTotale,
			OptionPTLFRASansFranchiseStandard optionPTLFRASansFranchiseStandard,
			OptionPTLFRASansFranchiseRenfort optionPTLFRASansFranchiseRenfort, List<IProduitSante> produits_sante,
			IPack pack_famille, IPack pack_pro, IPack pack_senior, GarantieDecesLabellise garantie_deces,
			MaintientDeSalaireLabellise maintient_de_salaire, OptionMaintientSalaire option_maintient_salaire,
			RatingData ratingData) {
		super();
		this.produits_sante = produits_sante;
		this.pack_famille = pack_famille;
		this.pack_pro = pack_pro;
		this.pack_senior = pack_senior;
		this.garantie_deces = garantie_deces;
		this.maintient_de_salaire = maintient_de_salaire;
		this.option_maintient_salaire = option_maintient_salaire;
		this.ratingData = ratingData;
		this.optionPTLFRASansFranchiseRenfort = optionPTLFRASansFranchiseRenfort;
		this.optionPTLFRASansFranchiseStandard = optionPTLFRASansFranchiseStandard;
		this.dependanceTotale = dependanceTotale;
	}

	
	
	public DependanceTotale getDependanceTotale() {
		return dependanceTotale;
	}



	public OptionPTLFRASansFranchiseRenfort getOptionPTLFRASansFranchiseRenfort() {
		return optionPTLFRASansFranchiseRenfort;
	}

	public OptionPTLFRASansFranchiseStandard getOptionPTLFRASansFranchiseStandard() {
		return optionPTLFRASansFranchiseStandard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#
	 * getOption_maintient_salaire()
	 */
	public OptionMaintientSalaire getOption_maintient_salaire() {
		return option_maintient_salaire;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#setPrevoyance
	 * (fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance)
	 */
	public void setPrevoyance(choix_prevoyance prevoyance) {
		if (garantie_deces != null)
			garantie_deces.setPrevoyance(prevoyance);
		if (maintient_de_salaire != null)
			maintient_de_salaire.setPrevoyance(prevoyance);
		if (option_maintient_salaire != null)
			option_maintient_salaire.setPrevoyance(prevoyance);
		if (optionPTLFRASansFranchiseRenfort != null)
			optionPTLFRASansFranchiseRenfort.setPrevoyance(prevoyance);
		if (optionPTLFRASansFranchiseStandard != null)
			optionPTLFRASansFranchiseStandard.setPrevoyance(prevoyance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#
	 * getProduits_sante()
	 */
	public List<IProduitSante> getProduits_sante() {
		return produits_sante;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#
	 * getPack_famille()
	 */
	public IPack getPack_famille() {
		return pack_famille;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#getPack_pro
	 * ()
	 */
	public IPack getPack_pro() {
		return pack_pro;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#getPack_senior
	 * ()
	 */
	public IPack getPack_senior() {
		return pack_senior;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#
	 * getGarantie_deces()
	 */
	public GarantieDecesLabellise getGarantie_deces() {
		return garantie_deces;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise#
	 * getMaintient_de_salaire()
	 */
	public MaintientDeSalaireLabellise getMaintient_de_salaire() {
		return maintient_de_salaire;
	}

	public RatingData getRatingData() {
		return ratingData;
	}

	public void setRatingData(RatingData ratingData) {
		this.ratingData = ratingData;
	}



	@Override
	public String toString() {
		return "ReponseLabellise [produits_sante=" + produits_sante + ", pack_famille=" + pack_famille + ", pack_pro="
				+ pack_pro + ", pack_senior=" + pack_senior + ", option_maintient_salaire=" + option_maintient_salaire
				+ ", garantie_deces=" + garantie_deces + ", maintient_de_salaire=" + maintient_de_salaire
				+ ", optionPTLFRASansFranchiseRenfort=" + optionPTLFRASansFranchiseRenfort
				+ ", optionPTLFRASansFranchiseStandard=" + optionPTLFRASansFranchiseStandard + ", dependanceTotale="
				+ dependanceTotale  + "]";
	}



	@Override
	public Produit getRenfort_deces() {
		
		return new Produit();
	}

	

}
