package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;

public class ProduitSantePN implements Serializable, IProduitFamille,IProduitSante {

	private static final long serialVersionUID = 7254589000745337287L;

	private boolean selectParAdherent = false;
	private boolean selectParConjoint = false;
	private boolean selectParEnfant = false;
	private Set<String> code=new HashSet<String>();

	public enum Code_produit_sante_pn {
		azur, indigo, horizon
	};

	final Code_produit_sante_pn code_produit_sante;
	BigDecimal cotisation_base_adherent = BigDecimal.ZERO;
	BigDecimal cotisation_base_conjoint = BigDecimal.ZERO;
	List<BigDecimal> cotisations_base_enfant = new ArrayList<BigDecimal>();
	BigDecimal cotisation_assistance = BigDecimal.ZERO;
	BigDecimal cotisation_reseau_de_soin_adherent = BigDecimal.ZERO;
	BigDecimal cotisation_reseau_de_soin_conjoint = BigDecimal.ZERO;
	List<BigDecimal> cotisations_reseau_de_soin_enfant = new ArrayList<BigDecimal>();
	BigDecimal cotisation_protection_juridique = BigDecimal.ZERO;
	BigDecimal cotisation_prime_naissance = BigDecimal.ZERO;


	
	public ProduitSantePN(Code_produit_sante_pn code_produit_sante) {
		super();
		this.code_produit_sante = code_produit_sante;
	}

	public BigDecimal getCotisation_prime_naissance() {
		return cotisation_prime_naissance;
	}

	public void setCotisation_prime_naissance(BigDecimal cotisation_prime_naissance) {
		this.cotisation_prime_naissance = cotisation_prime_naissance;
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
	 * getCode_produit_sante_pn()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.interfaces.IProduitSante#
	 * getCode_produit_sante_pn ()
	 */
	public Code_produit_sante_pn getCode_produit_sante_pn() {
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
				.add(cotisation_protection_juridique).add(cotisation_prime_naissance);
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
		return cotisation_base_conjoint.add(cotisation_reseau_de_soin_conjoint);
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.pn.IProduitFamille#
	 * getCotisation_totale_enfants()
	 */
	@Override
	public BigDecimal getCotisation_totale_enfants() {
		BigDecimal cotisation_totale_enfants = BigDecimal.ZERO;
		for (BigDecimal cotisation_base_enfant : cotisations_base_enfant) {
			cotisation_totale_enfants = (cotisation_totale_enfants.add(cotisation_base_enfant));
		}
		for (BigDecimal cotisation_reseau_de_soin_enfant : cotisations_reseau_de_soin_enfant) {
			cotisation_totale_enfants = cotisation_totale_enfants.add(cotisation_reseau_de_soin_enfant);
		}

		return cotisation_totale_enfants;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.pn.IProduitFamille#
	 * getCotisation_base_adherent()
	 */
	@Override
	public BigDecimal getCotisation_base_adherent() {
		return cotisation_base_adherent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.pn.IProduitFamille#
	 * setCotisation_base_adherent(java.math.BigDecimal)
	 */
	@Override
	public void setCotisation_base_adherent(BigDecimal cotisation_base_adherent) {
		selectParAdherent = true;
		this.cotisation_base_adherent = cotisation_base_adherent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.pn.IProduitFamille#
	 * getCotisation_base_conjoint()
	 */
	@Override
	public BigDecimal getCotisation_base_conjoint() {
		selectParConjoint = true;
		return cotisation_base_conjoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.pn.IProduitFamille#
	 * setCotisation_base_conjoint(java.math.BigDecimal)
	 */
	@Override
	public void setCotisation_base_conjoint(BigDecimal cotisation_base_conjoint) {
		selectParConjoint=true;
		this.cotisation_base_conjoint = cotisation_base_conjoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.pn.IProduitFamille#
	 * getCotisations_base_enfant()
	 */
	@Override
	public List<BigDecimal> getCotisations_base_enfant() {
		return cotisations_base_enfant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.interiale.moteur.devis2013.impl.pn.IProduitFamille#
	 * setCotisations_base_enfant(java.util.List)
	 */
	@Override
	public void setCotisation_base_enfant(BigDecimal cotisation_base_enfant) {
		selectParEnfant = true;
		this.cotisations_base_enfant.add(cotisation_base_enfant);
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

	

	@Override
	public String toString() {
		return "ProduitSantePN "+getCode_produit_sante_pn()+"\n [\n code=" + code
				+"\n"+" selectParAdherent=" + selectParAdherent  
				+" , cotidation total adhérent : "+getCotisation_adherent()
				+"\n"+ " selectParConjoint=" + selectParConjoint
				+" , cotisation total conjoint : "+getCotisation_conjoint()				
				+"\n"+ " selectParEnfant=" + selectParEnfant  
				+" , cotisation total enfant : "+getCotisation_totale_enfants()
				+ "\n DETAIL : "
				+ "\n cotisation_base_adherent=" + cotisation_base_adherent
				+ "\n cotisation_base_conjoint=" + cotisation_base_conjoint
				+ "\n cotisations_base_enfant="
				+ cotisations_base_enfant 
				+ "\n cotisation_assistance=" + cotisation_assistance
				+ "\n cotisation_reseau_de_soin_adherent=" + cotisation_reseau_de_soin_adherent
				+ "\n cotisation_reseau_de_soin_conjoint=" + cotisation_reseau_de_soin_conjoint
				+ "\n cotisations_reseau_de_soin_enfant=" + cotisations_reseau_de_soin_enfant
				+ "\n cotisation_protection_juridique=" + cotisation_protection_juridique
				+ "\n cotisation_prime_naissance=" + cotisation_prime_naissance +  "\n]\n";
	}

	public boolean isSelectParAdherent() {
		return selectParAdherent;
	}

	public boolean isSelectParConjoint() {
		return selectParConjoint;
	}

	public boolean isSelectParEnfant() {
		return selectParEnfant;
	}

	public String getCode() {
		return WebServiceWS.getCode(code);
	}

	

	public void addCode(String code) {
		this.code.add(code);
	}

}