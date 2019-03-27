package fr.interiale.moteur.devis2013.impl.labellise;

import java.io.Serializable;

import fr.interiale.moteur.devis2013.impl.commun.IChoixProspect;
import fr.interiale.moteur.devis2013.impl.commun.Moi.typeTarification;
import fr.interiale.moteur.devis2013.impl.interiale.IChoixProspectInteriale;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante;

public class ChoixProspectLabellise implements Serializable,IChoixProspectLabellise,IChoixProspectInteriale,IChoixProspect{

	private static final long serialVersionUID = -1713966610920925105L;

	private Code_produit_sante choix_produit_sante;
	
	private boolean choix_pack_famille;
	
	private boolean choix_pack_pro;
	
	private boolean choix_pack_senior;
	
	private boolean choix_garantie_couverture_prime;
	
	private boolean choix_garantie_deces;
	
	private boolean choix_maintient_de_salaire;

	private boolean choix_option_maintient_de_salaire;
	
	private boolean acceptation_email_offre_interiale;
	
	private boolean acceptation_email_offre_partenaire;
	
	private boolean envoi_courrier;
	
	private choix_prevoyance prevoyance;
	
	private typeTarification type_tarification;
	
	private boolean choix_dependance_totale;
	
	private boolean choix_code_option_OptionPTLFRASansFranchiseRenfort;
	
	private boolean choix_code_option_OptionPTLFRASansFranchiseStandard;
	
	
	
	public boolean isChoix_code_option_OptionPTLFRASansFranchiseRenfort() {
		return choix_code_option_OptionPTLFRASansFranchiseRenfort;
	}

	public void setChoix_code_option_OptionPTLFRASansFranchiseRenfort(
			boolean choix_code_option_OptionPTLFRASansFranchiseRenfort) {
		this.choix_code_option_OptionPTLFRASansFranchiseRenfort = choix_code_option_OptionPTLFRASansFranchiseRenfort;
	}

	public boolean isChoix_Code_option_OptionPTLFRASansFranchiseStandard() {
		return choix_code_option_OptionPTLFRASansFranchiseStandard;
	}

	public void setChoix_Code_option_OptionPTLFRASansFranchiseStandard(boolean code_option_OptionPTLFRASansFranchiseStandard) {
		this.choix_code_option_OptionPTLFRASansFranchiseStandard = code_option_OptionPTLFRASansFranchiseStandard;
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

	public boolean isChoix_option_maintient_de_salaire() {
		return choix_option_maintient_de_salaire;
	}

	public void setChoix_option_maintient_de_salaire(boolean choix_option_maintient_de_salaire) {
		this.choix_option_maintient_de_salaire = choix_option_maintient_de_salaire;
	}

	public choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	public void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
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

	public Code_produit_sante getChoix_produit_sante() {
		return choix_produit_sante;
	}

	public void setChoix_produit_sante(Code_produit_sante choix_produit_sante) {
		this.choix_produit_sante = choix_produit_sante;
	}

	public boolean isChoix_pack_famille() {
		return choix_pack_famille;
	}

	public void setChoix_pack_famille(boolean choix_pack_famille) {
		this.choix_pack_famille = choix_pack_famille;
	}

	public boolean isChoix_pack_pro() {
		return choix_pack_pro;
	}

	public void setChoix_pack_pro(boolean choix_pack_pro) {
		this.choix_pack_pro = choix_pack_pro;
	}

	public boolean isChoix_pack_senior() {
		return choix_pack_senior;
	}

	public void setChoix_pack_senior(boolean choix_pack_senior) {
		this.choix_pack_senior = choix_pack_senior;
	}

	public boolean isChoix_garantie_couverture_prime() {
		return choix_garantie_couverture_prime;
	}

	public void setChoix_garantie_couverture_prime(boolean choix_garantie_couverture_prime) {
		this.choix_garantie_couverture_prime = choix_garantie_couverture_prime;
	}

	public boolean isChoix_garantie_deces() {
		return choix_garantie_deces;
	}

	public void setChoix_garantie_deces(boolean choix_garantie_deces) {
		this.choix_garantie_deces = choix_garantie_deces;
	}

	public boolean isChoix_maintient_de_salaire() {
		return choix_maintient_de_salaire;
	}

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
		result = prime * result + (choix_option_maintient_de_salaire ? 1231 : 1237);
		result = prime * result + (choix_pack_famille ? 1231 : 1237);
		result = prime * result + (choix_pack_pro ? 1231 : 1237);
		result = prime * result + (choix_pack_senior ? 1231 : 1237);
		result = prime * result + ((choix_produit_sante == null) ? 0 : choix_produit_sante.hashCode());
		result = prime * result + (envoi_courrier ? 1231 : 1237);
		result = prime * result + ((prevoyance == null) ? 0 : prevoyance.hashCode());
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
		ChoixProspectLabellise other = (ChoixProspectLabellise) obj;
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
		if (choix_option_maintient_de_salaire != other.choix_option_maintient_de_salaire)
			return false;
		if (choix_pack_famille != other.choix_pack_famille)
			return false;
		if (choix_pack_pro != other.choix_pack_pro)
			return false;
		if (choix_pack_senior != other.choix_pack_senior)
			return false;
		if (choix_produit_sante != other.choix_produit_sante)
			return false;
		if (envoi_courrier != other.envoi_courrier)
			return false;
		if (prevoyance != other.prevoyance)
			return false;
		if (type_tarification != other.type_tarification)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChoixProspectLabellise [choix_produit_sante=" + choix_produit_sante + ", choix_pack_famille="
				+ choix_pack_famille + ", choix_pack_pro=" + choix_pack_pro + ", choix_pack_senior="
				+ choix_pack_senior + ", choix_garantie_couverture_prime=" + choix_garantie_couverture_prime
				+ ", choix_garantie_deces=" + choix_garantie_deces + ", choix_maintient_de_salaire="
				+ choix_maintient_de_salaire + ", choix_option_maintient_de_salaire="
				+ choix_option_maintient_de_salaire + ", acceptation_email_offre_interiale="
				+ acceptation_email_offre_interiale + ", acceptation_email_offre_partenaire="
				+ acceptation_email_offre_partenaire + ", envoi_courrier=" + envoi_courrier + ", prevoyance="
				+ prevoyance + ", type_tarification=" + type_tarification + "]";
	}

	
}
