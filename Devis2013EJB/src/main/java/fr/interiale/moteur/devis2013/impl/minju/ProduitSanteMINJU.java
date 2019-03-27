package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import fr.interiale.moteur.devis2013.interfaces.IProduitSante;

public class ProduitSanteMINJU implements Serializable, IProduitSante {

	private static final long serialVersionUID = 7254589000745337287L;

	public enum Code_produit_sante_minju {
		 protection, precision, precaution, perfection
	};

	final Code_produit_sante_minju code_produit_sante;
	BigDecimal cotisation_base_adherent= BigDecimal.ZERO;
	BigDecimal cotisation_base_conjoint = BigDecimal.ZERO;
	List<BigDecimal> cotisations_base_enfant = new ArrayList<BigDecimal>();
	BigDecimal cotisation_assistance = BigDecimal.ZERO;

	BigDecimal cotisation_reseau_de_soin_adherent = BigDecimal.ZERO;
	BigDecimal cotisation_reseau_de_soin_conjoint = BigDecimal.ZERO;
	List<BigDecimal> cotisations_reseau_de_soin_enfant = new ArrayList<BigDecimal>();
	
	BigDecimal cotisation_cancer_adherent = BigDecimal.ZERO;
	BigDecimal cotisation_cancer_conjoint = BigDecimal.ZERO;
	List<BigDecimal> cotisations_cancer_enfant = new ArrayList<BigDecimal>();

	BigDecimal cotisation_protection_juridique = BigDecimal.ZERO;
	List<BigDecimal> cotisation_assurance_scolaire = new ArrayList<BigDecimal>();
	
	BigDecimal cotisation_maternite = BigDecimal.ZERO;
	BigDecimal cotisation_teleconsultation_adherent = BigDecimal.ZERO;
	BigDecimal cotisation_teleconsultation_conjoint = BigDecimal.ZERO;
	List<BigDecimal> cotisations_teleconsultation_enfant = new ArrayList<BigDecimal>();

	
	
	public ProduitSanteMINJU(Code_produit_sante_minju code_produit_sante) {
		super();
		this.code_produit_sante = code_produit_sante;
	}

	public ProduitSanteMINJU(Code_produit_sante_minju code_produit_sante, BigDecimal cotisation_adherent,
			BigDecimal cotisation_conjoint, BigDecimal cotisation_enfant) {
		super();
		this.code_produit_sante = code_produit_sante;
		this.cotisation_base_adherent = cotisation_adherent;
		this.cotisation_base_conjoint = cotisation_conjoint;
		this.cotisations_base_enfant.add(cotisation_enfant);
	}

	public BigDecimal getCotisation_cancer_adherent() {
		return cotisation_cancer_adherent;
	}

	public void setCotisation_cancer_adherent(BigDecimal cotisation_cancer_adherent) {
		this.cotisation_cancer_adherent = cotisation_cancer_adherent;
	}

	public BigDecimal getCotisation_cancer_conjoint() {
		return cotisation_cancer_conjoint;
	}

	public void setCotisation_cancer_conjoint(BigDecimal cotisation_cancer_conjoint) {
		this.cotisation_cancer_conjoint = cotisation_cancer_conjoint;
	}

	public List<BigDecimal> getCotisations_cancer_enfant() {
		return cotisations_cancer_enfant;
	}

	public void setCotisations_cancer_enfant(List<BigDecimal> cotisations_cancer_enfant) {
		this.cotisations_cancer_enfant = cotisations_cancer_enfant;
	}

	public BigDecimal getCotisation_reseau_de_soin_adherent() {
		return cotisation_reseau_de_soin_adherent;
	}

	public void setCotisation_reseau_de_soin_adherent(BigDecimal cotisation_reseau_de_soin_adherent) {
		this.cotisation_reseau_de_soin_adherent = cotisation_reseau_de_soin_adherent;
	}

	public BigDecimal getCotisation_reseau_de_soin_conjoint() {
		return cotisation_reseau_de_soin_conjoint;
	}

	public void setCotisation_reseau_de_soin_conjoint(BigDecimal cotisation_reseau_de_soin_conjoint) {
		this.cotisation_reseau_de_soin_conjoint = cotisation_reseau_de_soin_conjoint;
	}

	public List<BigDecimal> getCotisations_reseau_de_soin_enfant() {
		return cotisations_reseau_de_soin_enfant;
	}

	public void setCotisations_reseau_de_soin_enfant(List<BigDecimal> cotisation_reseau_de_soin_enfant) {
		this.cotisations_reseau_de_soin_enfant = cotisation_reseau_de_soin_enfant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#
	 * getCode_produit_sante()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.interfaces.IProduitSante#getCode_produit_sante
	 * ()
	 */
	public Code_produit_sante_minju getCode_produit_sante() {
		return code_produit_sante;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#
	 * getcotisation_adherent()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.interfaces.IProduitSante#getcotisation_adherent
	 * ()
	 */
	public BigDecimal getCotisation_adherent() {
		return ((cotisation_base_adherent.add(cotisation_assistance)).add(cotisation_reseau_de_soin_adherent))
				.add(cotisation_protection_juridique).add(cotisation_cancer_adherent)
				.add(cotisation_teleconsultation_adherent).add(cotisation_maternite);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#
	 * getCotisation_conjoint()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.interfaces.IProduitSante#getCotisation_conjoint
	 * ()
	 */
	public BigDecimal getCotisation_conjoint() {
		return cotisation_base_conjoint.add(cotisation_reseau_de_soin_conjoint).add(cotisation_cancer_conjoint)
				.add(cotisation_teleconsultation_conjoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.interfaces.ProduitSanteInterface#
	 * getCotisation_totale_enfants()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.interfaces.IProduitSante#
	 * getCotisation_totale_enfants()
	 */
	public BigDecimal getCotisation_totale_enfants() {
		BigDecimal cotisation_totale_enfants = BigDecimal.ZERO;
		for (BigDecimal cotisation_base_enfant : cotisations_base_enfant) {
			cotisation_totale_enfants = (cotisation_totale_enfants.add(cotisation_base_enfant));
		}
		for (BigDecimal cotisation_reseau_de_soin_enfant : cotisations_reseau_de_soin_enfant) {
			cotisation_totale_enfants = cotisation_totale_enfants.add(cotisation_reseau_de_soin_enfant);
		}
		for (BigDecimal  cotisation:cotisation_assurance_scolaire){
			cotisation_totale_enfants=cotisation_totale_enfants.add(cotisation);
		}
		for (BigDecimal  cotisation:cotisations_cancer_enfant){
			cotisation_totale_enfants=cotisation_totale_enfants.add(cotisation);
		}
		for (BigDecimal  cotisation:cotisations_teleconsultation_enfant){
			cotisation_totale_enfants=cotisation_totale_enfants.add(cotisation);
		}
		
		return cotisation_totale_enfants;
	}

	public BigDecimal getCotisation_base_adherent() {
		return cotisation_base_adherent;
	}

	public void setCotisation_base_adherent(BigDecimal cotisation_base_adherent) {
		this.cotisation_base_adherent = cotisation_base_adherent;
	}

	public BigDecimal getCotisation_base_conjoint() {
		return cotisation_base_conjoint;
	}

	public void setCotisation_base_conjoint(BigDecimal cotisation_base_conjoint) {
		this.cotisation_base_conjoint = cotisation_base_conjoint;
	}

	public List<BigDecimal> getCotisations_base_enfant() {
		return cotisations_base_enfant;
	}

	public void setCotisations_base_enfant(List<BigDecimal> cotisations_base_enfant) {
		this.cotisations_base_enfant = cotisations_base_enfant;
	}

	public BigDecimal getCotisation_assistance() {
		return cotisation_assistance;
	}

	public void setCotisation_assistance(BigDecimal cotisation_assistance) {
		this.cotisation_assistance = cotisation_assistance;
	}

	public BigDecimal getCotisation_protection_juridique() {
		return cotisation_protection_juridique;
	}

	public void setCotisation_protection_juridique(BigDecimal cotisation_protection_juridique) {
		this.cotisation_protection_juridique = cotisation_protection_juridique;
	}

	
	public List<BigDecimal> getCotisation_assurance_scolaire() {
		return cotisation_assurance_scolaire;
	}

	public void setCotisation_assurance_scolaire(List<BigDecimal> cotisation_assurance_scolaire) {
		this.cotisation_assurance_scolaire = cotisation_assurance_scolaire;
	}

	public BigDecimal getCotisation_maternite() {
		return cotisation_maternite;
	}

	public void setCotisation_maternite(BigDecimal cotisation_maternite) {
		this.cotisation_maternite = cotisation_maternite;
	}

	public BigDecimal getCotisation_teleconsultation_adherent() {
		return cotisation_teleconsultation_adherent;
	}

	public void setCotisation_teleconsultation_adherent(BigDecimal cotisation_teleconsultation_adherent) {
		this.cotisation_teleconsultation_adherent = cotisation_teleconsultation_adherent;
	}

	public BigDecimal getCotisation_teleconsultation_conjoint() {
		return cotisation_teleconsultation_conjoint;
	}

	public void setCotisation_teleconsultation_conjoint(BigDecimal cotisation_teleconsultation_conjoint) {
		this.cotisation_teleconsultation_conjoint = cotisation_teleconsultation_conjoint;
	}

	public List<BigDecimal> getCotisations_teleconsultation_enfant() {
		return cotisations_teleconsultation_enfant;
	}

	public void setCotisations_teleconsultation_enfant(List<BigDecimal> cotisations_teleconsultation_enfant) {
		this.cotisations_teleconsultation_enfant = cotisations_teleconsultation_enfant;
	}

	@Override
	public String toString() {
		return "ProduitSanteMINJU [code_produit_sante=" + code_produit_sante + ", cotisation_base_adherent="
				+ cotisation_base_adherent + ", cotisation_base_conjoint=" + cotisation_base_conjoint
				+ ", cotisations_base_enfant=" + cotisations_base_enfant + ", cotisation_assistance="
				+ cotisation_assistance + ", cotisation_reseau_de_soin_adherent=" + cotisation_reseau_de_soin_adherent
				+ ", cotisation_reseau_de_soin_conjoint=" + cotisation_reseau_de_soin_conjoint
				+ ", cotisations_reseau_de_soin_enfant=" + cotisations_reseau_de_soin_enfant
				+ ", cotisation_cancer_adherent=" + cotisation_cancer_adherent + ", cotisation_cancer_conjoint="
				+ cotisation_cancer_conjoint + ", cotisations_cancer_enfant=" + cotisations_cancer_enfant
				+ ", cotisation_protection_juridique=" + cotisation_protection_juridique
				+ ", cotisation_assurance_scolaire=" + cotisation_assurance_scolaire + ", cotisation_maternite="
				+ cotisation_maternite + ", cotisation_teleconsultation_adherent="
				+ cotisation_teleconsultation_adherent + ", cotisation_teleconsultation_conjoint="
				+ cotisation_teleconsultation_conjoint + ", cotisations_teleconsultation_enfant="
				+ cotisations_teleconsultation_enfant + "]";
	}

	
}