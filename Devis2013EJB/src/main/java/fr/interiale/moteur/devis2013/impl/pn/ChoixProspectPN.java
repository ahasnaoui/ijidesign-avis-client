package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;

import fr.interiale.moteur.devis2013.impl.commun.IChoixProspect;
import fr.interiale.moteur.devis2013.impl.commun.Moi.typeTarification;
import fr.interiale.moteur.devis2013.impl.pn.ProduitSantePN.Code_produit_sante_pn;

public class ChoixProspectPN implements Serializable, IChoixProspect{

	private static final long serialVersionUID = -1713966610920925105L;

	private Code_produit_sante_pn choix_produit_sante;
	
	private boolean choix_maintien_prime;
	
	private boolean choix_garantie_deces_complementaire;
	
	private boolean choix_garantie_deces_temp;
	
	private boolean choix_garantie_deces_complementaire_conjoint;
	
	private boolean acceptation_email_offre_interiale;
	
	private boolean acceptation_email_offre_partenaire;
	
	private boolean envoi_courrier;

	private typeTarification type_tarification;
	
	private Float montant_garantie_deces_complementaire;
	
	private Float montant_garantie_deces_complementaire_conjoint;
	
	
	
	public Float getMontant_garantie_deces_complementaire_conjoint() {
		return montant_garantie_deces_complementaire_conjoint;
	}

	public void setMontant_garantie_deces_complementaire_conjoint(Float montant_garantie_deces_complementaire_conjoint) {
		this.montant_garantie_deces_complementaire_conjoint = montant_garantie_deces_complementaire_conjoint;
	}

	public boolean isChoix_garantie_deces_complementaire_conjoint() {
		return choix_garantie_deces_complementaire_conjoint;
	}

	public void setChoix_garantie_deces_complementaire_conjoint(boolean choix_garantie_deces_complementaire_conjoint) {
		this.choix_garantie_deces_complementaire_conjoint = choix_garantie_deces_complementaire_conjoint;
	}

	public Float getMontant_garantie_deces_complementaire() {
		return montant_garantie_deces_complementaire;
	}

	public void setMontant_garantie_deces_complementaire(Float montant_garantie_deces_complementaire) {
		this.montant_garantie_deces_complementaire = montant_garantie_deces_complementaire;
	}

	public boolean isChoix_maintien_prime() {
		return choix_maintien_prime;
	}

	public void setChoix_maintien_prime(boolean choix_maintien_prime) {
		this.choix_maintien_prime = choix_maintien_prime;
	}

	public boolean isChoix_garantie_deces_complementaire() {
		return choix_garantie_deces_complementaire;
	}

	public void setChoix_garantie_deces_complementaire(boolean choix_garantie_deces_complementaire) {
		this.choix_garantie_deces_complementaire = choix_garantie_deces_complementaire;
	}

	public Code_produit_sante_pn getChoix_produit_sante() {
		return choix_produit_sante;
	}

	public void setChoix_produit_sante(Code_produit_sante_pn choix_produit_sante) {
		this.choix_produit_sante = choix_produit_sante;
	}

	public boolean isAcceptation_email_offre_interiale() {
		return acceptation_email_offre_interiale;
	}

	public void setAcceptation_email_offre_interiale(boolean acceptation_email_offre_interiale) {
		this.acceptation_email_offre_interiale = acceptation_email_offre_interiale;
	}

	public boolean isAcceptation_email_offre_partenaire() {
		return acceptation_email_offre_partenaire;
	}

	public void setAcceptation_email_offre_partenaire(boolean acceptation_email_offre_partenaire) {
		this.acceptation_email_offre_partenaire = acceptation_email_offre_partenaire;
	}

	public boolean isEnvoi_courrier() {
		return envoi_courrier;
	}

	public void setEnvoi_courrier(boolean envoi_courrier) {
		this.envoi_courrier = envoi_courrier;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public typeTarification getType_tarification() {
		return type_tarification;
	}

	public void setType_tarification(typeTarification type_tarification) {
		this.type_tarification = type_tarification;
	}

	public boolean isChoix_garantie_deces_temp() {
		return choix_garantie_deces_temp;
	}

	public void setChoix_garantie_deces_temp(boolean choix_garantie_deces_temp) {
		this.choix_garantie_deces_temp = choix_garantie_deces_temp;
	}
	
		
}
