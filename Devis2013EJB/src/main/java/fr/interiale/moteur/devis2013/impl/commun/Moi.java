package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import fr.interiale.moteur.devis2013.impl.interiale.InitialisationMoteurException;

public class Moi implements Serializable {


	private static final long serialVersionUID = -5719983765802103247L;

	public enum Activite { fonction_public,fonction_public_etat,fonction_public_territoriale, prive, police_nationale, MIN_JU, MEN}
	
	public enum Etat {actif,actif_en_disponibilite,retraite,retraite_enfant_a_charge,eleve_fonction_public,sans_emploi,veuf}
	
	public enum Statut {titulaire, contractuel, eleve, stagiaire}
	
	public enum Profession{
		Personnel_Police_nationale_Administratif_Technique,						
		Adjoint_de_securite,						
		Agent_surveillance_Paris,						
		Autre_fonctionnaire,						
		Cadet_de_la_Republique,						
		Fonctionnaire_Police_Nationale_percevant_l_Indemnite_de_Sujetion_Speciale_ISSP,						
		Inspecteur_securite_Paris,						
		Ouvrier_d_Etat,						
		Personnel_des_prefectures,						
		Fonctionnaire_Securite_Civile,
		Personnel_des_Conseils_generaux_et_regionaux,
		Personnel_de_mairies_de_collectivites_territoriales_d_OPH,
		Police_municipale,
		Personnel_des_SDIS,
		Sapeur_Pompier_professionnel,
		Gardes_Champetres,
		Agent_de_la_Prefecture_de_Police_de_Paris,
		administration_penitentiaire,
		protection_juridique_de_la_jeunesse,
		juridiction,
		autres,
		
	}
	
	public enum typeTarification {S,P,SP}
	
	private final Date date_adhesion_souhaitee;
	
	private Date date_entree_administration;
	
	private Date date_entree_administration_MGP;
	
	private boolean exMFP;
	
	private final Date date_de_naissance;
	
	private final Etat etat;
	
	private final Activite activite;
	
	private boolean est_regime_alsace_moselle;

	private BigDecimal net_mensuel=BigDecimal.ZERO;
		
	private BigDecimal primeISSP=BigDecimal.ZERO;
	
	private BigDecimal prime_feu_rendement=BigDecimal.ZERO;
	
	private BigDecimal prime_hors_feu_rendement=BigDecimal.ZERO;
	
	private Integer indice=0;
	
	private Integer NBI=0;
	
	private String code_postal_lieu_travail="31000";
	
	private Profession profession;
	
	/**********************
	 *    CHAMPS MINJU 
	 *********************/
	 private Statut statut;
	 /** Traitement indiciaire brut*/
	 private BigDecimal tib; 
	 /** Salaire annuel brut  */
	 private BigDecimal sab;
	 /** Montant de la pension annuelle brut */
	 private BigDecimal pab;
	 /** Traitement indiciaire brut ayant servi au calcul de la pension */
	 private BigDecimal tibp;
	 /** Dernier salaire annuel brut ayant servi au calcul de la pension */
	 private BigDecimal sabp;
	 /** Affection Longue Durée */
	 private Boolean affectionLD;
	 /** Ex adhérent MMJ */
	 private Boolean exMmj;
	 /** ALD conjoint*/ 
	 private boolean ALDConjoint;
	 /** MMJ conjoint*/
	 private boolean MMJConjoint;
	 /** activité à temps partiel  */
	 private String tempsPartiel;
	 
	public Moi( Date date_de_naissance, Etat etat, Activite activite,Date date_adhesion_souhaitee) throws InitialisationMoteurException {
		if(date_adhesion_souhaitee==null)throw new InitialisationMoteurException("date_adhesion_souhaitee==null->obligatoire");
		if(date_de_naissance==null)	throw new InitialisationMoteurException("date_de_naissance==null->obligatoire");
		if(etat==null)throw new InitialisationMoteurException("etat==null->obligatoire");
		if(activite==null)throw new InitialisationMoteurException("activite==null->obligatoire");
		this.date_adhesion_souhaitee = date_adhesion_souhaitee;
		this.date_de_naissance = date_de_naissance;
		this.etat = etat;
		this.activite = activite;
	}	
	
	private static final BigDecimal douze = new BigDecimal("12");
	
	public BigDecimal getPrime(){
		BigDecimal prime = BigDecimal.ZERO;
		//if(prime_feu_rendement!=null)prime=prime.add(prime_feu_rendement);
		if(prime_hors_feu_rendement!=null)prime=prime.add(prime_hors_feu_rendement.divide(douze,BigDecimal.ROUND_UP));
		//if(primeISSP!=null)prime=prime.add(primeISSP);
		return prime;
	}
	
	
	
	/**
	 * @return the tempsPartiel
	 */
	public String getTempsPartiel() {
		return tempsPartiel;
	}



	/**
	 * @param tempsPartiel the tempsPartiel to set
	 */
	public void setTempsPartiel(String tempsPartiel) {
		this.tempsPartiel = tempsPartiel;
	}



	/**
	 * @return the aLDConjoint
	 */
	public boolean isALDConjoint() {
		return ALDConjoint;
	}



	/**
	 * @param aLDConjoint the aLDConjoint to set
	 */
	public void setALDConjoint(boolean aLDConjoint) {
		ALDConjoint = aLDConjoint;
	}



	/**
	 * @return the mMJConjoint
	 */
	public boolean isMMJConjoint() {
		return MMJConjoint;
	}



	/**
	 * @param mMJConjoint the mMJConjoint to set
	 */
	public void setMMJConjoint(boolean mMJConjoint) {
		MMJConjoint = mMJConjoint;
	}



	public Date getDate_entree_administration_MGP() {
		return date_entree_administration_MGP;
	}



	public void setDate_entree_administration_MGP(Date date_entree_administration_MGP) {
		this.date_entree_administration_MGP = date_entree_administration_MGP;
	}



	public Date getDate_entree_administration() {
		return date_entree_administration;
	}



	public void setDate_entree_administration(Date date_entree_administration) {
		this.date_entree_administration = date_entree_administration;
	}



	public boolean isExMFP() {
		return exMFP;
	}



	public void setExMFP(boolean exMFP) {
		this.exMFP = exMFP;
	}



	public Integer getIndice() {
		return indice;
	}
	
	public Profession getProfession() {
		return profession;
	}


	public void setProfession(Profession profession) {
		this.profession = profession;
	}


	public BigDecimal getNet_mensuel() {
		return net_mensuel;
	}


	public BigDecimal getPrime_feu_rendement() {
		return prime_feu_rendement;
	}


	public void setPrime_feu_rendement(BigDecimal prime_feu_rendement) {
		this.prime_feu_rendement = prime_feu_rendement;
	}


	public BigDecimal getPrime_hors_feu_rendement() {
		return prime_hors_feu_rendement;
	}


	public void setPrime_hors_feu_rendement(BigDecimal prime_hors_feu_rendement) {
		this.prime_hors_feu_rendement = prime_hors_feu_rendement;
	}


	public void setNet_mensuel(BigDecimal net_mensuel) {
		this.net_mensuel = net_mensuel;
	}






	public BigDecimal getPrimeISSP() {
		return primeISSP;
	}



	public void setPrimeISSP(BigDecimal primeISSP) {
		this.primeISSP = primeISSP;
	}



	public Integer getIndice(float valeurPoint) {
		if(indice==0){
			return  Math.round(net_mensuel.floatValue()/valeurPoint);
		}
		return indice;
	}



	public void setIndice(Integer indice) {
		this.indice = indice;
	}



	public Integer getNBI() {
		return NBI;
	}



	public void setNBI(Integer nBI) {
		NBI = nBI;
	}

	public String getCode_postal_lieu_travail() {
		return code_postal_lieu_travail;
	}
	
	public void setCode_postal_lieu_travail(String cp) {
		code_postal_lieu_travail = cp;
	}



	public void setEst_regime_alsace_moselle(boolean est_regime_alsace_moselle) {
		this.est_regime_alsace_moselle = est_regime_alsace_moselle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getDate_adhesion_souhaitee() {
		return date_adhesion_souhaitee;
	}

	public Date getDate_de_naissance() {
		return date_de_naissance;
	}

	public int getAgeAAdhesion() {
		Calendar adhesion = getCalendar(date_adhesion_souhaitee);
		Calendar naissance = getCalendar(date_de_naissance);
		
		int nbYears = adhesion.get(Calendar.YEAR) - naissance.get(Calendar.YEAR);
		if (adhesion.get(Calendar.MONTH) < naissance.get(Calendar.MONTH) || ( adhesion.get(Calendar.MONTH) == naissance.get(Calendar.MONTH) && adhesion.get(Calendar.DATE) < naissance.get(Calendar.DATE) ) ) {
			nbYears--;
		}
		return nbYears;
	}
	
	private Calendar getCalendar(Date d){
		Calendar cal = Calendar.getInstance(Locale.FRANCE);
		cal.setTime(d);
		return cal;
	}

	public Etat getEtat() {
		return etat;
	}

	public Activite getActivite() {
		return activite;
	}

	
	public boolean isEst_regime_alsace_moselle() {
		return est_regime_alsace_moselle;
	}


	/**
	 * @return the statut
	 */
	public Statut getStatut() {
		return statut;
	}



	/**
	 * @param statut the statut to set
	 */
	public void setStatut(Statut statut) {
		this.statut = statut;
	}



	






	/**
	 * @return the sab
	 */
	public BigDecimal getSab() {
		return sab;
	}



	/**
	 * @param sab the sab to set
	 */
	public void setSab(BigDecimal sab) {
		this.sab = sab;
	}



	



	


	


	/**
	 * @return the tib
	 */
	public BigDecimal getTib() {
		return tib;
	}



	/**
	 * @param tib the tib to set
	 */
	public void setTib(BigDecimal tib) {
		this.tib = tib;
	}



	/**
	 * @return the pab
	 */
	public BigDecimal getPab() {
		return pab;
	}



	/**
	 * @param pab the pab to set
	 */
	public void setPab(BigDecimal pab) {
		this.pab = pab;
	}



	/**
	 * @return the tibp
	 */
	public BigDecimal getTibp() {
		return tibp;
	}



	/**
	 * @param tibp the tibp to set
	 */
	public void setTibp(BigDecimal tibp) {
		this.tibp = tibp;
	}



	/**
	 * @return the sabp
	 */
	public BigDecimal getSabp() {
		return sabp;
	}



	/**
	 * @param sabp the sabp to set
	 */
	public void setSabp(BigDecimal sabp) {
		this.sabp = sabp;
	}



	public Boolean getAffectionLD() {
		return affectionLD;
	}



	public void setAffectionLD(Boolean affectionLD) {
		this.affectionLD = affectionLD;
	}



	public Boolean getExMmj() {
		return exMmj;
	}



	public void setExMmj(Boolean exMmj) {
		this.exMmj = exMmj;
	}



	@Override
	public String toString() {
		return "Moi [date_adhesion_souhaitee=" + date_adhesion_souhaitee + ", date_de_naissance=" + date_de_naissance
				+ ", etat=" + etat + ", activite=" + activite + ", est_regime_alsace_moselle="
				+ est_regime_alsace_moselle + "]";
	}

	
	public boolean isEcole(){
		return (Etat.eleve_fonction_public==etat);
	}
	
	public boolean isJeune(){
		return ((calculeAgeALaDateDAdhesion(getDate_de_naissance(),new Date())<30)&&(!isEcole()));
	}
	
	public static int calculeAgeALaDateDAdhesion(Date birthday, Date today) { 
	    Calendar cBirthday = new GregorianCalendar(); 
	    Calendar cToday = new GregorianCalendar(); 
	    cBirthday.setTime(birthday); 
	    cToday.setTime(today); 
	 
	    int yearDiff = cToday.get(Calendar.YEAR) - cBirthday.get(Calendar.YEAR); 
	    cBirthday.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); 
	    if (cBirthday.before(cToday)) { 
	        return yearDiff; //Birthday already celebrated this year 
	    } 
	    else { 
	        return Math.max(0, yearDiff-1); //Need a max to avoid -1 for baby 
	    } 
	}
}

