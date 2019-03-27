package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;

import fr.interiale.moteur.devis2013.impl.commun.IChoixProspect;
import fr.interiale.moteur.devis2013.impl.commun.Moi.typeTarification;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante;

public class ChoixProspectMINJU implements Serializable, IChoixProspectMINJU,IChoixProspect{


	private static final long serialVersionUID = -1713966610920925105L;

	private Code_produit_sante choix_produit_sante;
	
	private boolean choix_pack_pharmaPlus;
	
	private boolean choix_garantie_couverture_prime;
	
	private boolean choix_garantie_deces;
	
	private boolean choix_maintient_de_salaire;

	private boolean acceptation_email_offre_interiale;
	
	private boolean acceptation_email_offre_partenaire;
	
	private typeTarification type_tarification;
	
	private boolean choix_dependance_totale;
	
	private boolean choix_dependance_totale_conjoint;
	
	private boolean envoi_courrier;
	
	private choix_prevoyance prevoyance;
	
	
	
	/**
	 * @return the choix_dependance_totale_conjoint
	 */
	public boolean isChoix_dependance_totale_conjoint() {
		return choix_dependance_totale_conjoint;
	}

	/**
	 * @param choix_dependance_totale_conjoint the choix_dependance_totale_conjoint to set
	 */
	public void setChoix_dependance_totale_conjoint(
			boolean choix_dependance_totale_conjoint) {
		this.choix_dependance_totale_conjoint = choix_dependance_totale_conjoint;
	}

	/**
	 * @return the choix_pack_pharmaPlus
	 */
	public boolean isChoix_pack_pharmaPlus() {
		return choix_pack_pharmaPlus;
	}

	/**
	 * @param choix_pack_pharmaPlus the choix_pack_pharmaPlus to set
	 */
	public void setChoix_pack_pharmaPlus(boolean choix_pack_pharmaPlus) {
		this.choix_pack_pharmaPlus = choix_pack_pharmaPlus;
	}

	public boolean isChoix_dependance_totale() {
		return choix_dependance_totale;
	}

	public void setChoix_dependance_totale(boolean choix_dependance_totale) {
		this.choix_dependance_totale = choix_dependance_totale;
	}

	public typeTarification getType_tarification() {
		return type_tarification;
	}

	public void setType_tarification(typeTarification type_tarification) {
		this.type_tarification = type_tarification;
	}

	public boolean isEnvoi_courrier() {
		return envoi_courrier;
	}

	public void setEnvoi_courrier(boolean envoi_courrier) {
		this.envoi_courrier = envoi_courrier;
	}
	
	

	/**
	 * @return the prevoyance
	 */
	public choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	/**
	 * @param prevoyance the prevoyance to set
	 */
	public void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#isAcceptation_email_offre_interiale()
	 */
	public boolean isAcceptation_email_offre_interiale() {
		return acceptation_email_offre_interiale;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#setAcceptation_email_offre_interiale(boolean)
	 */
	public void setAcceptation_email_offre_interiale(boolean acceptation_email_offre_interiale) {
		this.acceptation_email_offre_interiale = acceptation_email_offre_interiale;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#isAcceptation_email_offre_partenaire()
	 */
	public boolean isAcceptation_email_offre_partenaire() {
		return acceptation_email_offre_partenaire;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#setAcceptation_email_offre_partenaire(boolean)
	 */
	public void setAcceptation_email_offre_partenaire(boolean acceptation_email_offre_partenaire) {
		this.acceptation_email_offre_partenaire = acceptation_email_offre_partenaire;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#getChoix_produit_sante()
	 */
	public Code_produit_sante getChoix_produit_sante() {
		return choix_produit_sante;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#setChoix_produit_sante(fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante)
	 */
	public void setChoix_produit_sante(Code_produit_sante choix_produit_sante) {
		this.choix_produit_sante = choix_produit_sante;
	}




	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#isChoix_garantie_couverture_prime()
	 */
	public boolean isChoix_garantie_couverture_prime() {
		return choix_garantie_couverture_prime;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#setChoix_garantie_couverture_prime(boolean)
	 */
	public void setChoix_garantie_couverture_prime(boolean choix_garantie_couverture_prime) {
		this.choix_garantie_couverture_prime = choix_garantie_couverture_prime;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#isChoix_garantie_deces()
	 */
	public boolean isChoix_garantie_deces() {
		return choix_garantie_deces;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#setChoix_garantie_deces(boolean)
	 */
	public void setChoix_garantie_deces(boolean choix_garantie_deces) {
		this.choix_garantie_deces = choix_garantie_deces;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#isChoix_maintient_de_salaire()
	 */
	public boolean isChoix_maintient_de_salaire() {
		return choix_maintient_de_salaire;
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IChoixProspect#setChoix_maintient_de_salaire(boolean)
	 */
	public void setChoix_maintient_de_salaire(boolean choix_maintient_de_salaire) {
		this.choix_maintient_de_salaire = choix_maintient_de_salaire;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (acceptation_email_offre_interiale ? 1231 : 1237);
		result = prime * result + (acceptation_email_offre_partenaire ? 1231 : 1237);
		result = prime * result + (choix_garantie_couverture_prime ? 1231 : 1237);
		result = prime * result + (choix_garantie_deces ? 1231 : 1237);
		result = prime * result + (choix_maintient_de_salaire ? 1231 : 1237);
		result = prime * result + ((choix_produit_sante == null) ? 0 : choix_produit_sante.hashCode());
		result = prime * result + (envoi_courrier ? 1231 : 1237);
		result = prime * result + ((type_tarification == null) ? 0 : type_tarification.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChoixProspectMINJU other = (ChoixProspectMINJU) obj;
		if (acceptation_email_offre_interiale != other.acceptation_email_offre_interiale)
			return false;
		if (acceptation_email_offre_partenaire != other.acceptation_email_offre_partenaire)
			return false;
		if (choix_garantie_couverture_prime != other.choix_garantie_couverture_prime)
			return false;
		if (choix_garantie_deces != other.choix_garantie_deces)
			return false;
		if (choix_maintient_de_salaire != other.choix_maintient_de_salaire)
			return false;
		if (choix_produit_sante != other.choix_produit_sante)
			return false;
		if (envoi_courrier != other.envoi_courrier)
			return false;
		if (type_tarification != other.type_tarification)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChoixProspectInteriale [choix_produit_sante=" + choix_produit_sante + ",  choix_garantie_couverture_prime=" + choix_garantie_couverture_prime
				+ ", choix_garantie_deces=" + choix_garantie_deces + ", choix_maintient_de_salaire="
				+ choix_maintient_de_salaire + ", acceptation_email_offre_interiale="
				+ acceptation_email_offre_interiale + ", acceptation_email_offre_partenaire="
				+ acceptation_email_offre_partenaire + ", type_tarification=" + type_tarification + ", envoi_courrier="
				+ envoi_courrier + "]";
	}

	
}
