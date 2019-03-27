package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

public class ContactWeb implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 576314441148169225L;
	
	private String id;
	private String civilite;
	private String prenom;
	private String nom;
	private String nomJeuneFille;
	private String email;
	private String telephone;
	private String portable;
	private Calendar dateNaissance;
	private String nni;
	private String cleNNI;
	private String situationFamiliale;
	private boolean ramo;
	private Adresse adresse;
	private String statut;
	private String csp;
	private String employeur;
	private Calendar dateEntreeAdministration;
	private String telephonePro;
	private String matricule;
	private String indiceMajore;
	private String salaire;
	private String nbi;
	private String primes;
	private String primesFacultatives;
	private String tpsTravail;
	private String typeContact;
	private String typeAdhesion;
	
	private String administration;
	private String fonction;
		
	private boolean optInITE;
	private boolean optInGrpITE;
	private String numAdherent;
	private String fonctionPublique;
	private String idEdeal;
	
	private String typeDemande;
	private String natureDemande;
	private String domaineDemande;
	private String[] infosSousFormulaire;
	private String objetMessage;
	private String message;
	private FichierAttache[] fichiersAttaches;
	
	
	
	@Override
	public String toString() {
		return "ContactWeb [id=" + id + ", civilite=" + civilite + ", prenom=" + prenom + ", nom=" + nom + ", nomJeuneFille=" + nomJeuneFille + ", email=" + email + ", telephone="
				+ telephone + ", portable=" + portable + ", dateNaissance=" + dateNaissance + ", nni=" + nni + ", cleNNI=" + cleNNI + ", situationFamiliale=" + situationFamiliale
				+ ", ramo=" + ramo + ", adresse=" + adresse + ", statut=" + statut + ", csp=" + csp + ", employeur=" + employeur + ", dateEntreeAdministration="
				+ dateEntreeAdministration + ", telephonePro=" + telephonePro + ", matricule=" + matricule + ", indiceMajore=" + indiceMajore + ", salaire=" + salaire + ", nbi="
				+ nbi + ", primes=" + primes + ", primesFacultatives=" + primesFacultatives + ", tpsTravail=" + tpsTravail + ", typeContact=" + typeContact + ", typeAdhesion="
				+ typeAdhesion + ", administration=" + administration + ", fonction=" + fonction + ", optInITE=" + optInITE + ", optInGrpITE=" + optInGrpITE + ", numAdherent="
				+ numAdherent + ", fonctionPublique=" + fonctionPublique + ", idEdeal=" + idEdeal + ", typeDemande=" + typeDemande + ", natureDemande=" + natureDemande
				+ ", domaineDemande=" + domaineDemande + ", infosSousFormulaire=" + Arrays.toString(infosSousFormulaire) + ", objetMessage=" + objetMessage + ", message="
				+ message +"]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCivilite() {
		return civilite;
	}
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getNomJeuneFille() {
		return nomJeuneFille;
	}
	public void setNomJeuneFille(String nomJeuneFille) {
		this.nomJeuneFille = nomJeuneFille;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPortable() {
		return portable;
	}
	public void setPortable(String portable) {
		this.portable = portable;
	}
	public Calendar getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(Calendar dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public String getNni() {
		return nni;
	}
	public void setNni(String nni) {
		this.nni = nni;
	}
	public String getCleNNI() {
		return cleNNI;
	}
	public void setCleNNI(String cleNNI) {
		this.cleNNI = cleNNI;
	}
	public String getSituationFamiliale() {
		return situationFamiliale;
	}
	public void setSituationFamiliale(String situationFamiliale) {
		this.situationFamiliale = situationFamiliale;
	}
	public boolean isRamo() {
		return ramo;
	}
	public void setRamo(boolean ramo) {
		this.ramo = ramo;
	}
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getCsp() {
		return csp;
	}
	public void setCsp(String csp) {
		this.csp = csp;
	}
	public String getEmployeur() {
		return employeur;
	}
	public void setEmployeur(String employeur) {
		this.employeur = employeur;
	}
	public Calendar getDateEntreeAdministration() {
		return dateEntreeAdministration;
	}
	public void setDateEntreeAdministration(Calendar dateEntreeAdministration) {
		this.dateEntreeAdministration = dateEntreeAdministration;
	}
	public String getTelephonePro() {
		return telephonePro;
	}
	public void setTelephonePro(String telephonePro) {
		this.telephonePro = telephonePro;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getIndiceMajore() {
		return indiceMajore;
	}
	public void setIndiceMajore(String indiceMajore) {
		this.indiceMajore = indiceMajore;
	}
	public String getSalaire() {
		return salaire;
	}
	public void setSalaire(String salaire) {
		this.salaire = salaire;
	}
	public String getNbi() {
		return nbi;
	}
	public void setNbi(String nbi) {
		this.nbi = nbi;
	}
	public String getPrimes() {
		return primes;
	}
	public void setPrimes(String primes) {
		this.primes = primes;
	}
	public String getPrimesFacultatives() {
		return primesFacultatives;
	}
	public void setPrimesFacultatives(String primesFacultatives) {
		this.primesFacultatives = primesFacultatives;
	}
	public String getTpsTravail() {
		return tpsTravail;
	}
	public void setTpsTravail(String tpsTravail) {
		this.tpsTravail = tpsTravail;
	}
	public String getTypeContact() {
		return typeContact;
	}
	public void setTypeContact(String typeContact) {
		this.typeContact = typeContact;
	}
	public String getTypeAdhesion() {
		return typeAdhesion;
	}
	public void setTypeAdhesion(String typeAdhesion) {
		this.typeAdhesion = typeAdhesion;
	}
	public String getAdministration() {
		return administration;
	}
	public void setAdministration(String administration) {
		this.administration = administration;
	}
	public String getFonction() {
		return fonction;
	}
	public void setFonction(String fonction) {
		this.fonction = fonction;
	}
	public boolean isOptInITE() {
		return optInITE;
	}
	public void setOptInITE(boolean optInITE) {
		this.optInITE = optInITE;
	}
	public boolean isOptInGrpITE() {
		return optInGrpITE;
	}
	public void setOptInGrpITE(boolean optInGrpITE) {
		this.optInGrpITE = optInGrpITE;
	}
	public String getNumAdherent() {
		return numAdherent;
	}
	public void setNumAdherent(String numAdherent) {
		this.numAdherent = numAdherent;
	}
	public String getFonctionPublique() {
		return fonctionPublique;
	}
	public void setFonctionPublique(String fonctionPublique) {
		this.fonctionPublique = fonctionPublique;
	}
	public String getIdEdeal() {
		return idEdeal;
	}
	public void setIdEdeal(String idEdeal) {
		this.idEdeal = idEdeal;
	}
	public String getTypeDemande() {
		return typeDemande;
	}
	public void setTypeDemande(String typeDemande) {
		this.typeDemande = typeDemande;
	}
	public String getNatureDemande() {
		return natureDemande;
	}
	public void setNatureDemande(String natureDemande) {
		this.natureDemande = natureDemande;
	}
	public String getDomaineDemande() {
		return domaineDemande;
	}
	public void setDomaineDemande(String domaineDemande) {
		this.domaineDemande = domaineDemande;
	}
	public String[] getInfosSousFormulaire() {
		return infosSousFormulaire;
	}
	public void setInfosSousFormulaire(String[] infosSousFormulaire) {
		this.infosSousFormulaire = infosSousFormulaire;
	}
	public String getObjetMessage() {
		return objetMessage;
	}
	public void setObjetMessage(String objetMessage) {
		this.objetMessage = objetMessage;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public FichierAttache[] getFichiersAttaches() {
		return fichiersAttaches;
	}
	public void setFichiersAttaches(FichierAttache[] fichiersAttaches) {
		this.fichiersAttaches = fichiersAttaches;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
