package fr.interiale.renouvellement.entite;

public class Renouvellement {

	private String numAdherent;
	
	private String modePaiementActuel;
	
	private String modePaiementChoisi;
	
	private String statut;
	
	private String motifRenonciation;
	
	private String commentaireRenonciation;
	
	private Double montantCotisation;
	
	private MandatSepa mandatSepa;
	
	private String dateStatut;
	
	private Double montantTaxes;
	
	private String origineModification;

	/**
	 * 
	 * @return the dateStatut
	 */
	public String getDateStatut() {
		return dateStatut;
	}

	/**
	 * 
	 * @param dateStatut the dateStatut to set
	 */
	public void setDateStatut(String dateStatut) {
		this.dateStatut = dateStatut;
	}

	/**
	 * @return the numAdherent
	 */
	public String getNumAdherent() {
		return numAdherent;
	}

	/**
	 * @param numAdherent the numAdherent to set
	 */
	public void setNumAdherent(String numAdherent) {
		this.numAdherent = numAdherent;
	}

	/**
	 * @return the modePaiementActuel
	 */
	public String getModePaiementActuel() {
		return modePaiementActuel;
	}

	/**
	 * @param modePaiementActuel the modePaiementActuel to set
	 */
	public void setModePaiementActuel(String modePaiementActuel) {
		this.modePaiementActuel = modePaiementActuel;
	}

	/**
	 * @return the modePaiementChoisi
	 */
	public String getModePaiementChoisi() {
		return modePaiementChoisi;
	}

	/**
	 * @param modePaiementChoisi the modePaiementChoisi to set
	 */
	public void setModePaiementChoisi(String modePaiementChoisi) {
		this.modePaiementChoisi = modePaiementChoisi;
	}

	/**
	 * @return the statut
	 */
	public String getStatut() {
		return statut;
	}

	/**
	 * @param statut the statut to set
	 */
	public void setStatut(String statut) {
		this.statut = statut;
	}

	/**
	 * @return the motifRenonciation
	 */
	public String getMotifRenonciation() {
		return motifRenonciation;
	}

	/**
	 * @param motifRenonciation the motifRenonciation to set
	 */
	public void setMotifRenonciation(String motifRenonciation) {
		this.motifRenonciation = motifRenonciation;
	}

	/**
	 * @return the commentaireRenonciation
	 */
	public String getCommentaireRenonciation() {
		return commentaireRenonciation;
	}

	/**
	 * @param commentaireRenonciation the commentaireRenonciation to set
	 */
	public void setCommentaireRenonciation(String commentaireRenonciation) {
		this.commentaireRenonciation = commentaireRenonciation;
	}

	/**
	 * @return the montantCotisation
	 */
	public Double getMontantCotisation() {
		return montantCotisation;
	}

	/**
	 * @param montantCotisation the montantCotisation to set
	 */
	public void setMontantCotisation(Double montantCotisation) {
		this.montantCotisation = montantCotisation;
	}

	/**
	 * @return the mandatSepa
	 */
	public MandatSepa getMandatSepa() {
		return mandatSepa;
	}

	/**
	 * @param mandatSepa the mandatSepa to set
	 */
	public void setMandatSepa(MandatSepa mandatSepa) {
		this.mandatSepa = mandatSepa;
	}

	/**
	 * @return the montantTaxes
	 */
	public Double getMontantTaxes() {
		return montantTaxes;
	}

	/**
	 * @param montantTaxes the montantTaxes to set
	 */
	public void setMontantTaxes(Double montantTaxes) {
		this.montantTaxes = montantTaxes;
	}

	/**
	 * @return the origineModification
	 */
	public String getOrigineModification() {
		return origineModification;
	}

	/**
	 * @param origineModification the origineModification to set
	 */
	public void setOrigineModification(String origineModification) {
		this.origineModification = origineModification;
	}
}
