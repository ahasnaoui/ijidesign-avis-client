package fr.interiale.moteur.devis2013.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import fr.interiale.moteur.devis2013.impl.commun.Adresse;
import fr.interiale.moteur.devis2013.impl.commun.Conjoint;
import fr.interiale.moteur.devis2013.impl.commun.Coordonnees;
import fr.interiale.moteur.devis2013.impl.commun.Enfant;
import fr.interiale.moteur.devis2013.impl.commun.Moi;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Activite;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Etat;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Profession;
import fr.interiale.moteur.devis2013.impl.interiale.ChoixProspectInteriale;
import fr.interiale.moteur.devis2013.impl.interiale.DemandeInteriale;
import fr.interiale.moteur.devis2013.impl.interiale.IChoixProspectInteriale;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.interiale.IReponseInteriale;
import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante;
import fr.interiale.moteur.devis2013.impl.interiale.ReponseInteriale;
import fr.interiale.moteur.devis2013.impl.labellise.ChoixProspectLabellise;
import fr.interiale.moteur.devis2013.impl.labellise.ReponseLabellise;
import fr.interiale.moteur.devis2013.impl.minju.ChoixProspectMINJU;
import fr.interiale.moteur.devis2013.impl.minju.ReponseMINJU;

@Entity
public class SauvegardeDevis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8186995386475432044L;

	public enum offre_commerciale {
		interiale, labellise, filia
	};

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	// MOI
	@Enumerated(EnumType.STRING)
	private offre_commerciale offre_commerciale_choisie;

	public offre_commerciale getOffre_commerciale_choisie() {
		return offre_commerciale_choisie;
	}

	public void setOffre_commerciale_choisie(offre_commerciale offre_commerciale_choisie) {
		this.offre_commerciale_choisie = offre_commerciale_choisie;
	}

	private Date date_creation = new Date();
	private Date date_adhesion_souhaitee;
	private Date date_entree_administration;
	private boolean exMFP;
	private Date date_de_naissance_prospect;
	@Enumerated(EnumType.STRING)
	private Etat etat_prospect;
	@Enumerated(EnumType.STRING)
	private Activite activite_prospect;
	private boolean regime_alsace_moselle_prospect;
	private BigDecimal net_mensuel = BigDecimal.ZERO;
	private BigDecimal primeISSP = BigDecimal.ZERO;
	private BigDecimal prime_feu_rendement = BigDecimal.ZERO;
	private BigDecimal prime_hors_feu_rendement = BigDecimal.ZERO;
	private Integer Indice = 0;
	private Integer NBI = 0;
	private String code_postal_lieu_travail = "31000";
	@Enumerated(EnumType.STRING)
	private Profession profession = Profession.Personnel_Police_nationale_Administratif_Technique;
	@Enumerated(EnumType.STRING)
	private choix_prevoyance prevoyance;
	// CONJOINT

	private Date date_de_naissance_conjoint;
	private boolean regime_alsace_moselle_conjoint;
	@Enumerated(EnumType.STRING)
	private Etat etat_conjoint;
	@Enumerated(EnumType.STRING)
	private Activite activite_conjoint;

	// ENFANTS

	private Date date_de_naissance_enfant_1;
	private boolean regime_alsace_moselle_enfant_1;
	private boolean tarifs_preferentiels_enfant_1;
	private Date date_de_naissance_enfant_2;
	private boolean regime_alsace_moselle_enfant_2;
	private boolean tarifs_preferentiels_enfant_2;
	private Date date_de_naissance_enfant_3;
	private boolean regime_alsace_moselle_enfant_3;
	private boolean tarifs_preferentiels_enfant_3;
	private Date date_de_naissance_enfant_4;
	private boolean regime_alsace_moselle_enfant_4;
	private boolean tarifs_preferentiels_enfant_4;
	private Date date_de_naissance_enfant_5;
	private boolean regime_alsace_moselle_enfant_5;
	private boolean tarifs_preferentiels_enfant_5;
	private Date date_de_naissance_enfant_6;
	private boolean regime_alsace_moselle_enfant_6;
	private boolean tarifs_preferentiels_enfant_6;
	private Date date_de_naissance_enfant_7;
	private boolean regime_alsace_moselle_enfant_7;
	private boolean tarifs_preferentiels_enfant_7;

	// PACK
	private BigDecimal cotisation_famille;
	private BigDecimal cotisation_pro;
	private BigDecimal cotisation_senior;

	// PREVOYANCE
	private BigDecimal cotisation_garantie_couverture_prime;
	private BigDecimal cotisation_garantie_deces;
	private BigDecimal cotisation_maintient_de_salaire;
	private BigDecimal option_cotisation_maintient_de_salaire;
	// ADRESSSE
	private String numero = "";
	private String bis = "";
	private String type_voie = "";
	private String adresse1 = "";
	private String adresse2 = "";
	private String ville = "";

	// COORDONNEES
	private String civilite;
	private String prenom;
	private String nom;
	private String code_postal;
	private String email;
	private String telephone;

	// CHOIX
	@Enumerated(EnumType.STRING)
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

	public SauvegardeDevis() {

	}

	private void update(IDemande demandeInteriale, IReponseInteriale iReponseInteriale,
			IChoixProspectInteriale choixProspect) {

		// MOI
		Moi moi = demandeInteriale.getMoi();
		date_adhesion_souhaitee = moi.getDate_adhesion_souhaitee();
		date_entree_administration = moi.getDate_entree_administration();
		exMFP = moi.isExMFP();
		date_de_naissance_prospect = moi.getDate_de_naissance();
		etat_prospect = moi.getEtat();
		activite_prospect = moi.getActivite();
		regime_alsace_moselle_prospect = moi.isEst_regime_alsace_moselle();
		net_mensuel = moi.getNet_mensuel();
		primeISSP = moi.getPrimeISSP();
		prime_feu_rendement = moi.getPrime_feu_rendement();
		prime_hors_feu_rendement = moi.getPrime_hors_feu_rendement();
		Indice = moi.getIndice();
		NBI = moi.getNBI();
		code_postal_lieu_travail = moi.getCode_postal_lieu_travail();
		profession = moi.getProfession();

		// CONJOINT
		Conjoint conjoint = demandeInteriale.getConjoint();
		if (conjoint != null) {
			date_de_naissance_conjoint = conjoint.getDate_de_naissance();
			regime_alsace_moselle_conjoint = conjoint.isRegime_alsace_moselle();
			etat_conjoint = conjoint.getEtat();
			activite_conjoint = conjoint.getActivite();
		}
		// ENFANTS
		List<Enfant> enfants = demandeInteriale.getEnfants();
		if (enfants != null) {
			int i = 1;
			for (Enfant enfant : enfants) {
				switch (i) {
				case 1:
					date_de_naissance_enfant_1 = enfant.getDate_de_naissance();
					regime_alsace_moselle_enfant_1 = enfant.isRegime_alsace_moselle();
					tarifs_preferentiels_enfant_1 = enfant.isTarifs_preferentiels();
					i++;
					break;
				case 2:
					date_de_naissance_enfant_2 = enfant.getDate_de_naissance();
					regime_alsace_moselle_enfant_2 = enfant.isRegime_alsace_moselle();
					tarifs_preferentiels_enfant_2 = enfant.isTarifs_preferentiels();
					i++;
					break;
				case 3:
					date_de_naissance_enfant_3 = enfant.getDate_de_naissance();
					regime_alsace_moselle_enfant_3 = enfant.isRegime_alsace_moselle();
					tarifs_preferentiels_enfant_3 = enfant.isTarifs_preferentiels();
					i++;
					break;
				case 4:
					date_de_naissance_enfant_4 = enfant.getDate_de_naissance();
					regime_alsace_moselle_enfant_4 = enfant.isRegime_alsace_moselle();
					tarifs_preferentiels_enfant_4 = enfant.isTarifs_preferentiels();
					i++;
					break;
				case 5:
					date_de_naissance_enfant_5 = enfant.getDate_de_naissance();
					regime_alsace_moselle_enfant_5 = enfant.isRegime_alsace_moselle();
					tarifs_preferentiels_enfant_5 = enfant.isTarifs_preferentiels();
					i++;
					break;
				case 6:
					date_de_naissance_enfant_6 = enfant.getDate_de_naissance();
					regime_alsace_moselle_enfant_6 = enfant.isRegime_alsace_moselle();
					tarifs_preferentiels_enfant_6 = enfant.isTarifs_preferentiels();
					i++;
					break;
				case 7:
					date_de_naissance_enfant_7 = enfant.getDate_de_naissance();
					regime_alsace_moselle_enfant_7 = enfant.isRegime_alsace_moselle();
					tarifs_preferentiels_enfant_7 = enfant.isTarifs_preferentiels();
					i++;
					break;
				default:
					break;
				}
			}
		}
		// CHOIX
		choix_produit_sante = choixProspect.getChoix_produit_sante();
		choix_pack_famille = choixProspect.isChoix_pack_famille();
		choix_pack_pro = choixProspect.isChoix_pack_pro();
		choix_pack_senior = choixProspect.isChoix_pack_senior();
		choix_garantie_couverture_prime = choixProspect.isChoix_garantie_couverture_prime();
		choix_garantie_deces = choixProspect.isChoix_garantie_deces();
		choix_maintient_de_salaire = choixProspect.isChoix_maintient_de_salaire();

		// PACK
		if (iReponseInteriale.getPack_famille() != null)
			cotisation_famille = iReponseInteriale.getPack_famille().getCotisation();
		if (iReponseInteriale.getPack_pro() != null)
			cotisation_pro = iReponseInteriale.getPack_pro().getCotisation();
		if (iReponseInteriale.getPack_senior() != null)
			cotisation_senior = iReponseInteriale.getPack_senior().getCotisation();

		// PREVOYANCE
		if (choix_garantie_deces)
			if (iReponseInteriale.getGarantie_deces() != null)
				cotisation_garantie_deces = iReponseInteriale.getGarantie_deces().getCotisation();
		if (choix_maintient_de_salaire)
			if (iReponseInteriale.getMaintient_de_salaire() != null)
				cotisation_maintient_de_salaire = iReponseInteriale.getMaintient_de_salaire().getCotisation();

		// COORDONNEES
		Coordonnees coordonnees = demandeInteriale.getCoordonnees();
		if (coordonnees != null) {
			civilite = coordonnees.getCivilite();
			prenom = coordonnees.getPrenom();
			nom = coordonnees.getNom();
			code_postal = coordonnees.getCode_postal();
			email = coordonnees.getEmail();
			telephone = coordonnees.getTelephone();
		}

		// ADRESSSE
		Adresse adresse = demandeInteriale.getAdresse();
		if (adresse != null) {
			numero = adresse.getNumero();
			bis = adresse.getBis();
			type_voie = adresse.getType_voie();
			adresse1 = adresse.getAdresse1();
			adresse2 = adresse.getAdresse2();
			ville = adresse.getVille();
			// code_postal = adresse.getCode_postal();
		}

	}

	private void update(DemandeInteriale demandeInteriale, ReponseInteriale iReponseInteriale,
			ChoixProspectInteriale choixProspect) {
		offre_commerciale_choisie = offre_commerciale.interiale;
		update((IDemande) demandeInteriale, iReponseInteriale, (IChoixProspectInteriale) choixProspect);
		if (choixProspect.isChoix_garantie_couverture_prime())
			cotisation_garantie_couverture_prime = iReponseInteriale.getGarantie_couverture_prime().getCotisation();
	}

	public SauvegardeDevis(DemandeInteriale demandeInteriale, ReponseInteriale iReponseInteriale,
			ChoixProspectInteriale choixProspect) {
		super();
		update(demandeInteriale, iReponseInteriale, choixProspect);

	}

	public void update(IDemande demandeInteriale, ReponseLabellise iReponseInteriale,
			ChoixProspectLabellise choixProspect) {
		update(demandeInteriale, iReponseInteriale, (IChoixProspectInteriale) choixProspect);
		offre_commerciale_choisie = offre_commerciale.labellise;
		prevoyance = choixProspect.getPrevoyance();
		if (prevoyance != null) {

			choix_option_maintient_de_salaire = choixProspect.isChoix_option_maintient_de_salaire();
			if (choix_option_maintient_de_salaire) {
				option_cotisation_maintient_de_salaire = iReponseInteriale.getOption_maintient_salaire()
						.getCotisation();
			}

		}

	}

	public SauvegardeDevis(IDemande demandeLabellise, ReponseLabellise iReponse,
			ChoixProspectLabellise choixProspectLabellise) {
		super();
		update(demandeLabellise, iReponse, choixProspectLabellise);

	}

	/**
	 * Consturcteur MINJU
	 * @param demande
	 * @param reponseMinju
	 * @param cpl
	 */
	public SauvegardeDevis(DemandeInteriale demande, ReponseMINJU reponseMinju,
			ChoixProspectMINJU cpl) {
		super();
	}

	public boolean isChoix_option_maintient_de_salaire() {
		return choix_option_maintient_de_salaire;
	}

	public void setChoix_option_maintient_de_salaire(boolean choix_option_maintient_de_salaire) {
		this.choix_option_maintient_de_salaire = choix_option_maintient_de_salaire;
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

	public Date getDate_adhesion_souhaitee() {
		return date_adhesion_souhaitee;
	}

	public void setDate_adhesion_souhaitee(Date date_adhesion_souhaitee) {
		this.date_adhesion_souhaitee = date_adhesion_souhaitee;
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

	public Date getDate_de_naissance_prospect() {
		return date_de_naissance_prospect;
	}

	public void setDate_de_naissance_prospect(Date date_de_naissance_prospect) {
		this.date_de_naissance_prospect = date_de_naissance_prospect;
	}

	public Etat getEtat_prospect() {
		return etat_prospect;
	}

	public void setEtat_prospect(Etat etat_prospect) {
		this.etat_prospect = etat_prospect;
	}

	public Activite getActivite_prospect() {
		return activite_prospect;
	}

	public void setActivite_prospect(Activite activite_prospect) {
		this.activite_prospect = activite_prospect;
	}

	public boolean isRegime_alsace_moselle_prospect() {
		return regime_alsace_moselle_prospect;
	}

	public void setRegime_alsace_moselle_prospect(boolean regime_alsace_moselle_prospect) {
		this.regime_alsace_moselle_prospect = regime_alsace_moselle_prospect;
	}

	public BigDecimal getNet_mensuel() {
		return net_mensuel;
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

	public Integer getIndice() {
		return Indice;
	}

	public void setIndice(Integer indice) {
		Indice = indice;
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

	public void setCode_postal_lieu_travail(String code_postal_lieu_travail) {
		this.code_postal_lieu_travail = code_postal_lieu_travail;
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public Date getDate_de_naissance_conjoint() {
		return date_de_naissance_conjoint;
	}

	public void setDate_de_naissance_conjoint(Date date_de_naissance_conjoint) {
		this.date_de_naissance_conjoint = date_de_naissance_conjoint;
	}

	public boolean isRegime_alsace_moselle_conjoint() {
		return regime_alsace_moselle_conjoint;
	}

	public void setRegime_alsace_moselle_conjoint(boolean regime_alsace_moselle_conjoint) {
		this.regime_alsace_moselle_conjoint = regime_alsace_moselle_conjoint;
	}

	public Etat getEtat_conjoint() {
		return etat_conjoint;
	}

	public void setEtat_conjoint(Etat etat_conjoint) {
		this.etat_conjoint = etat_conjoint;
	}

	public Activite getActivite_conjoint() {
		return activite_conjoint;
	}

	public void setActivite_conjoint(Activite activite_conjoint) {
		this.activite_conjoint = activite_conjoint;
	}

	public Date getDate_de_naissance_enfant_1() {
		return date_de_naissance_enfant_1;
	}

	public void setDate_de_naissance_enfant_1(Date date_de_naissance_enfant_1) {
		this.date_de_naissance_enfant_1 = date_de_naissance_enfant_1;
	}

	public boolean isRegime_alsace_moselle_enfant_1() {
		return regime_alsace_moselle_enfant_1;
	}

	public void setRegime_alsace_moselle_enfant_1(boolean regime_alsace_moselle_enfant_1) {
		this.regime_alsace_moselle_enfant_1 = regime_alsace_moselle_enfant_1;
	}

	public boolean isTarifs_preferentiels_enfant_1() {
		return tarifs_preferentiels_enfant_1;
	}

	public void setTarifs_preferentiels_enfant_1(boolean tarifs_preferentiels_enfant_1) {
		this.tarifs_preferentiels_enfant_1 = tarifs_preferentiels_enfant_1;
	}

	public Date getDate_de_naissance_enfant_2() {
		return date_de_naissance_enfant_2;
	}

	public void setDate_de_naissance_enfant_2(Date date_de_naissance_enfant_2) {
		this.date_de_naissance_enfant_2 = date_de_naissance_enfant_2;
	}

	public boolean isRegime_alsace_moselle_enfant_2() {
		return regime_alsace_moselle_enfant_2;
	}

	public void setRegime_alsace_moselle_enfant_2(boolean regime_alsace_moselle_enfant_2) {
		this.regime_alsace_moselle_enfant_2 = regime_alsace_moselle_enfant_2;
	}

	public boolean isTarifs_preferentiels_enfant_2() {
		return tarifs_preferentiels_enfant_2;
	}

	public void setTarifs_preferentiels_enfant_2(boolean tarifs_preferentiels_enfant_2) {
		this.tarifs_preferentiels_enfant_2 = tarifs_preferentiels_enfant_2;
	}

	public Date getDate_de_naissance_enfant_3() {
		return date_de_naissance_enfant_3;
	}

	public void setDate_de_naissance_enfant_3(Date date_de_naissance_enfant_3) {
		this.date_de_naissance_enfant_3 = date_de_naissance_enfant_3;
	}

	public boolean isRegime_alsace_moselle_enfant_3() {
		return regime_alsace_moselle_enfant_3;
	}

	public void setRegime_alsace_moselle_enfant_3(boolean regime_alsace_moselle_enfant_3) {
		this.regime_alsace_moselle_enfant_3 = regime_alsace_moselle_enfant_3;
	}

	public boolean isTarifs_preferentiels_enfant_3() {
		return tarifs_preferentiels_enfant_3;
	}

	public void setTarifs_preferentiels_enfant_3(boolean tarifs_preferentiels_enfant_3) {
		this.tarifs_preferentiels_enfant_3 = tarifs_preferentiels_enfant_3;
	}

	public Date getDate_de_naissance_enfant_4() {
		return date_de_naissance_enfant_4;
	}

	public void setDate_de_naissance_enfant_4(Date date_de_naissance_enfant_4) {
		this.date_de_naissance_enfant_4 = date_de_naissance_enfant_4;
	}

	public boolean isRegime_alsace_moselle_enfant_4() {
		return regime_alsace_moselle_enfant_4;
	}

	public void setRegime_alsace_moselle_enfant_4(boolean regime_alsace_moselle_enfant_4) {
		this.regime_alsace_moselle_enfant_4 = regime_alsace_moselle_enfant_4;
	}

	public boolean isTarifs_preferentiels_enfant_4() {
		return tarifs_preferentiels_enfant_4;
	}

	public void setTarifs_preferentiels_enfant_4(boolean tarifs_preferentiels_enfant_4) {
		this.tarifs_preferentiels_enfant_4 = tarifs_preferentiels_enfant_4;
	}

	public Date getDate_de_naissance_enfant_5() {
		return date_de_naissance_enfant_5;
	}

	public void setDate_de_naissance_enfant_5(Date date_de_naissance_enfant_5) {
		this.date_de_naissance_enfant_5 = date_de_naissance_enfant_5;
	}

	public boolean isRegime_alsace_moselle_enfant_5() {
		return regime_alsace_moselle_enfant_5;
	}

	public void setRegime_alsace_moselle_enfant_5(boolean regime_alsace_moselle_enfant_5) {
		this.regime_alsace_moselle_enfant_5 = regime_alsace_moselle_enfant_5;
	}

	public boolean isTarifs_preferentiels_enfant_5() {
		return tarifs_preferentiels_enfant_5;
	}

	public void setTarifs_preferentiels_enfant_5(boolean tarifs_preferentiels_enfant_5) {
		this.tarifs_preferentiels_enfant_5 = tarifs_preferentiels_enfant_5;
	}

	public Date getDate_de_naissance_enfant_6() {
		return date_de_naissance_enfant_6;
	}

	public void setDate_de_naissance_enfant_6(Date date_de_naissance_enfant_6) {
		this.date_de_naissance_enfant_6 = date_de_naissance_enfant_6;
	}

	public boolean isRegime_alsace_moselle_enfant_6() {
		return regime_alsace_moselle_enfant_6;
	}

	public void setRegime_alsace_moselle_enfant_6(boolean regime_alsace_moselle_enfant_6) {
		this.regime_alsace_moselle_enfant_6 = regime_alsace_moselle_enfant_6;
	}

	public boolean isTarifs_preferentiels_enfant_6() {
		return tarifs_preferentiels_enfant_6;
	}

	public void setTarifs_preferentiels_enfant_6(boolean tarifs_preferentiels_enfant_6) {
		this.tarifs_preferentiels_enfant_6 = tarifs_preferentiels_enfant_6;
	}

	public Date getDate_de_naissance_enfant_7() {
		return date_de_naissance_enfant_7;
	}

	public BigDecimal getOption_cotisation_maintient_de_salaire() {
		return option_cotisation_maintient_de_salaire;
	}

	public void setOption_cotisation_maintient_de_salaire(BigDecimal option_cotisation_maintient_de_salaire) {
		this.option_cotisation_maintient_de_salaire = option_cotisation_maintient_de_salaire;
	}

	public void setDate_de_naissance_enfant_7(Date date_de_naissance_enfant_7) {
		this.date_de_naissance_enfant_7 = date_de_naissance_enfant_7;
	}

	public boolean isRegime_alsace_moselle_enfant_7() {
		return regime_alsace_moselle_enfant_7;
	}

	public void setRegime_alsace_moselle_enfant_7(boolean regime_alsace_moselle_enfant_7) {
		this.regime_alsace_moselle_enfant_7 = regime_alsace_moselle_enfant_7;
	}

	public boolean isTarifs_preferentiels_enfant_7() {
		return tarifs_preferentiels_enfant_7;
	}

	public void setTarifs_preferentiels_enfant_7(boolean tarifs_preferentiels_enfant_7) {
		this.tarifs_preferentiels_enfant_7 = tarifs_preferentiels_enfant_7;
	}

	public BigDecimal getCotisation_famille() {
		return cotisation_famille;
	}

	public void setCotisation_famille(BigDecimal cotisation_famille) {
		this.cotisation_famille = cotisation_famille;
	}

	public BigDecimal getCotisation_pro() {
		return cotisation_pro;
	}

	public void setCotisation_pro(BigDecimal cotisation_pro) {
		this.cotisation_pro = cotisation_pro;
	}

	public BigDecimal getCotisation_senior() {
		return cotisation_senior;
	}

	public void setCotisation_senior(BigDecimal cotisation_senior) {
		this.cotisation_senior = cotisation_senior;
	}

	public BigDecimal getCotisation_garantie_couverture_prime() {
		return cotisation_garantie_couverture_prime;
	}

	public void setCotisation_garantie_couverture_prime(BigDecimal cotisation_garantie_couverture_prime) {
		this.cotisation_garantie_couverture_prime = cotisation_garantie_couverture_prime;
	}

	public BigDecimal getCotisation_garantie_deces() {
		return cotisation_garantie_deces;
	}

	public void setCotisation_garantie_deces(BigDecimal cotisation_garantie_deces) {
		this.cotisation_garantie_deces = cotisation_garantie_deces;
	}

	public BigDecimal getCotisation_maintient_de_salaire() {
		return cotisation_maintient_de_salaire;
	}

	public void setCotisation_maintient_de_salaire(BigDecimal cotisation_maintient_de_salaire) {
		this.cotisation_maintient_de_salaire = cotisation_maintient_de_salaire;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBis() {
		return bis;
	}

	public void setBis(String bis) {
		this.bis = bis;
	}

	public String getType_voie() {
		return type_voie;
	}

	public void setType_voie(String type_voie) {
		this.type_voie = type_voie;
	}

	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
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

	public String getCode_postal() {
		return code_postal;
	}

	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}

	public choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	public void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Indice == null) ? 0 : Indice.hashCode());
		result = prime * result + ((NBI == null) ? 0 : NBI.hashCode());
		result = prime * result + (acceptation_email_offre_interiale ? 1231 : 1237);
		result = prime * result + (acceptation_email_offre_partenaire ? 1231 : 1237);
		result = prime * result + ((activite_conjoint == null) ? 0 : activite_conjoint.hashCode());
		result = prime * result + ((activite_prospect == null) ? 0 : activite_prospect.hashCode());
		result = prime * result + ((adresse1 == null) ? 0 : adresse1.hashCode());
		result = prime * result + ((adresse2 == null) ? 0 : adresse2.hashCode());
		result = prime * result + ((bis == null) ? 0 : bis.hashCode());
		result = prime * result + (choix_garantie_couverture_prime ? 1231 : 1237);
		result = prime * result + (choix_garantie_deces ? 1231 : 1237);
		result = prime * result + (choix_maintient_de_salaire ? 1231 : 1237);
		result = prime * result + (choix_pack_famille ? 1231 : 1237);
		result = prime * result + (choix_pack_pro ? 1231 : 1237);
		result = prime * result + (choix_pack_senior ? 1231 : 1237);
		result = prime * result + ((choix_produit_sante == null) ? 0 : choix_produit_sante.hashCode());
		result = prime * result + ((code_postal == null) ? 0 : code_postal.hashCode());
		result = prime * result + ((code_postal_lieu_travail == null) ? 0 : code_postal_lieu_travail.hashCode());
		result = prime * result + ((cotisation_famille == null) ? 0 : cotisation_famille.hashCode());
		result = prime
				* result
				+ ((cotisation_garantie_couverture_prime == null) ? 0 : cotisation_garantie_couverture_prime.hashCode());
		result = prime * result + ((cotisation_garantie_deces == null) ? 0 : cotisation_garantie_deces.hashCode());
		result = prime * result
				+ ((cotisation_maintient_de_salaire == null) ? 0 : cotisation_maintient_de_salaire.hashCode());
		result = prime * result + ((cotisation_pro == null) ? 0 : cotisation_pro.hashCode());
		result = prime * result + ((cotisation_senior == null) ? 0 : cotisation_senior.hashCode());
		result = prime * result + ((date_adhesion_souhaitee == null) ? 0 : date_adhesion_souhaitee.hashCode());
		result = prime * result + ((date_creation == null) ? 0 : date_creation.hashCode());
		result = prime * result + ((date_de_naissance_conjoint == null) ? 0 : date_de_naissance_conjoint.hashCode());
		result = prime * result + ((date_de_naissance_enfant_1 == null) ? 0 : date_de_naissance_enfant_1.hashCode());
		result = prime * result + ((date_de_naissance_enfant_2 == null) ? 0 : date_de_naissance_enfant_2.hashCode());
		result = prime * result + ((date_de_naissance_enfant_3 == null) ? 0 : date_de_naissance_enfant_3.hashCode());
		result = prime * result + ((date_de_naissance_enfant_4 == null) ? 0 : date_de_naissance_enfant_4.hashCode());
		result = prime * result + ((date_de_naissance_enfant_5 == null) ? 0 : date_de_naissance_enfant_5.hashCode());
		result = prime * result + ((date_de_naissance_enfant_6 == null) ? 0 : date_de_naissance_enfant_6.hashCode());
		result = prime * result + ((date_de_naissance_enfant_7 == null) ? 0 : date_de_naissance_enfant_7.hashCode());
		result = prime * result + ((date_de_naissance_prospect == null) ? 0 : date_de_naissance_prospect.hashCode());
		result = prime * result + ((date_entree_administration == null) ? 0 : date_entree_administration.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((etat_conjoint == null) ? 0 : etat_conjoint.hashCode());
		result = prime * result + ((etat_prospect == null) ? 0 : etat_prospect.hashCode());
		result = prime * result + (exMFP ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((net_mensuel == null) ? 0 : net_mensuel.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((offre_commerciale_choisie == null) ? 0 : offre_commerciale_choisie.hashCode());
		result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
		result = prime * result + ((prevoyance == null) ? 0 : prevoyance.hashCode());
		result = prime * result + ((primeISSP == null) ? 0 : primeISSP.hashCode());
		result = prime * result + ((prime_feu_rendement == null) ? 0 : prime_feu_rendement.hashCode());
		result = prime * result + ((prime_hors_feu_rendement == null) ? 0 : prime_hors_feu_rendement.hashCode());
		result = prime * result + ((profession == null) ? 0 : profession.hashCode());
		result = prime * result + (regime_alsace_moselle_conjoint ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_enfant_1 ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_enfant_2 ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_enfant_3 ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_enfant_4 ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_enfant_5 ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_enfant_6 ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_enfant_7 ? 1231 : 1237);
		result = prime * result + (regime_alsace_moselle_prospect ? 1231 : 1237);
		result = prime * result + (tarifs_preferentiels_enfant_1 ? 1231 : 1237);
		result = prime * result + (tarifs_preferentiels_enfant_2 ? 1231 : 1237);
		result = prime * result + (tarifs_preferentiels_enfant_3 ? 1231 : 1237);
		result = prime * result + (tarifs_preferentiels_enfant_4 ? 1231 : 1237);
		result = prime * result + (tarifs_preferentiels_enfant_5 ? 1231 : 1237);
		result = prime * result + (tarifs_preferentiels_enfant_6 ? 1231 : 1237);
		result = prime * result + (tarifs_preferentiels_enfant_7 ? 1231 : 1237);
		result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
		result = prime * result + ((type_voie == null) ? 0 : type_voie.hashCode());
		result = prime * result + ((ville == null) ? 0 : ville.hashCode());
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
		SauvegardeDevis other = (SauvegardeDevis) obj;
		if (Indice == null) {
			if (other.Indice != null)
				return false;
		} else if (!Indice.equals(other.Indice))
			return false;
		if (NBI == null) {
			if (other.NBI != null)
				return false;
		} else if (!NBI.equals(other.NBI))
			return false;
		if (acceptation_email_offre_interiale != other.acceptation_email_offre_interiale)
			return false;
		if (acceptation_email_offre_partenaire != other.acceptation_email_offre_partenaire)
			return false;
		if (activite_conjoint != other.activite_conjoint)
			return false;
		if (activite_prospect != other.activite_prospect)
			return false;
		if (adresse1 == null) {
			if (other.adresse1 != null)
				return false;
		} else if (!adresse1.equals(other.adresse1))
			return false;
		if (adresse2 == null) {
			if (other.adresse2 != null)
				return false;
		} else if (!adresse2.equals(other.adresse2))
			return false;
		if (bis == null) {
			if (other.bis != null)
				return false;
		} else if (!bis.equals(other.bis))
			return false;
		if (choix_garantie_couverture_prime != other.choix_garantie_couverture_prime)
			return false;
		if (choix_garantie_deces != other.choix_garantie_deces)
			return false;
		if (choix_maintient_de_salaire != other.choix_maintient_de_salaire)
			return false;
		if (choix_pack_famille != other.choix_pack_famille)
			return false;
		if (choix_pack_pro != other.choix_pack_pro)
			return false;
		if (choix_pack_senior != other.choix_pack_senior)
			return false;
		if (choix_produit_sante != other.choix_produit_sante)
			return false;
		if (code_postal == null) {
			if (other.code_postal != null)
				return false;
		} else if (!code_postal.equals(other.code_postal))
			return false;
		if (code_postal_lieu_travail == null) {
			if (other.code_postal_lieu_travail != null)
				return false;
		} else if (!code_postal_lieu_travail.equals(other.code_postal_lieu_travail))
			return false;
		if (cotisation_famille == null) {
			if (other.cotisation_famille != null)
				return false;
		} else if (!cotisation_famille.equals(other.cotisation_famille))
			return false;
		if (cotisation_garantie_couverture_prime == null) {
			if (other.cotisation_garantie_couverture_prime != null)
				return false;
		} else if (!cotisation_garantie_couverture_prime.equals(other.cotisation_garantie_couverture_prime))
			return false;
		if (cotisation_garantie_deces == null) {
			if (other.cotisation_garantie_deces != null)
				return false;
		} else if (!cotisation_garantie_deces.equals(other.cotisation_garantie_deces))
			return false;
		if (cotisation_maintient_de_salaire == null) {
			if (other.cotisation_maintient_de_salaire != null)
				return false;
		} else if (!cotisation_maintient_de_salaire.equals(other.cotisation_maintient_de_salaire))
			return false;
		if (cotisation_pro == null) {
			if (other.cotisation_pro != null)
				return false;
		} else if (!cotisation_pro.equals(other.cotisation_pro))
			return false;
		if (cotisation_senior == null) {
			if (other.cotisation_senior != null)
				return false;
		} else if (!cotisation_senior.equals(other.cotisation_senior))
			return false;
		if (date_adhesion_souhaitee == null) {
			if (other.date_adhesion_souhaitee != null)
				return false;
		} else if (!date_adhesion_souhaitee.equals(other.date_adhesion_souhaitee))
			return false;
		if (date_creation == null) {
			if (other.date_creation != null)
				return false;
		} else if (!date_creation.equals(other.date_creation))
			return false;
		if (date_de_naissance_conjoint == null) {
			if (other.date_de_naissance_conjoint != null)
				return false;
		} else if (!date_de_naissance_conjoint.equals(other.date_de_naissance_conjoint))
			return false;
		if (date_de_naissance_enfant_1 == null) {
			if (other.date_de_naissance_enfant_1 != null)
				return false;
		} else if (!date_de_naissance_enfant_1.equals(other.date_de_naissance_enfant_1))
			return false;
		if (date_de_naissance_enfant_2 == null) {
			if (other.date_de_naissance_enfant_2 != null)
				return false;
		} else if (!date_de_naissance_enfant_2.equals(other.date_de_naissance_enfant_2))
			return false;
		if (date_de_naissance_enfant_3 == null) {
			if (other.date_de_naissance_enfant_3 != null)
				return false;
		} else if (!date_de_naissance_enfant_3.equals(other.date_de_naissance_enfant_3))
			return false;
		if (date_de_naissance_enfant_4 == null) {
			if (other.date_de_naissance_enfant_4 != null)
				return false;
		} else if (!date_de_naissance_enfant_4.equals(other.date_de_naissance_enfant_4))
			return false;
		if (date_de_naissance_enfant_5 == null) {
			if (other.date_de_naissance_enfant_5 != null)
				return false;
		} else if (!date_de_naissance_enfant_5.equals(other.date_de_naissance_enfant_5))
			return false;
		if (date_de_naissance_enfant_6 == null) {
			if (other.date_de_naissance_enfant_6 != null)
				return false;
		} else if (!date_de_naissance_enfant_6.equals(other.date_de_naissance_enfant_6))
			return false;
		if (date_de_naissance_enfant_7 == null) {
			if (other.date_de_naissance_enfant_7 != null)
				return false;
		} else if (!date_de_naissance_enfant_7.equals(other.date_de_naissance_enfant_7))
			return false;
		if (date_de_naissance_prospect == null) {
			if (other.date_de_naissance_prospect != null)
				return false;
		} else if (!date_de_naissance_prospect.equals(other.date_de_naissance_prospect))
			return false;
		if (date_entree_administration == null) {
			if (other.date_entree_administration != null)
				return false;
		} else if (!date_entree_administration.equals(other.date_entree_administration))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (etat_conjoint != other.etat_conjoint)
			return false;
		if (etat_prospect != other.etat_prospect)
			return false;
		if (exMFP != other.exMFP)
			return false;
		if (id != other.id)
			return false;
		if (net_mensuel == null) {
			if (other.net_mensuel != null)
				return false;
		} else if (!net_mensuel.equals(other.net_mensuel))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (offre_commerciale_choisie != other.offre_commerciale_choisie)
			return false;
		if (prenom == null) {
			if (other.prenom != null)
				return false;
		} else if (!prenom.equals(other.prenom))
			return false;
		if (prevoyance != other.prevoyance)
			return false;
		if (primeISSP == null) {
			if (other.primeISSP != null)
				return false;
		} else if (!primeISSP.equals(other.primeISSP))
			return false;
		if (prime_feu_rendement == null) {
			if (other.prime_feu_rendement != null)
				return false;
		} else if (!prime_feu_rendement.equals(other.prime_feu_rendement))
			return false;
		if (prime_hors_feu_rendement == null) {
			if (other.prime_hors_feu_rendement != null)
				return false;
		} else if (!prime_hors_feu_rendement.equals(other.prime_hors_feu_rendement))
			return false;
		if (profession != other.profession)
			return false;
		if (regime_alsace_moselle_conjoint != other.regime_alsace_moselle_conjoint)
			return false;
		if (regime_alsace_moselle_enfant_1 != other.regime_alsace_moselle_enfant_1)
			return false;
		if (regime_alsace_moselle_enfant_2 != other.regime_alsace_moselle_enfant_2)
			return false;
		if (regime_alsace_moselle_enfant_3 != other.regime_alsace_moselle_enfant_3)
			return false;
		if (regime_alsace_moselle_enfant_4 != other.regime_alsace_moselle_enfant_4)
			return false;
		if (regime_alsace_moselle_enfant_5 != other.regime_alsace_moselle_enfant_5)
			return false;
		if (regime_alsace_moselle_enfant_6 != other.regime_alsace_moselle_enfant_6)
			return false;
		if (regime_alsace_moselle_enfant_7 != other.regime_alsace_moselle_enfant_7)
			return false;
		if (regime_alsace_moselle_prospect != other.regime_alsace_moselle_prospect)
			return false;
		if (tarifs_preferentiels_enfant_1 != other.tarifs_preferentiels_enfant_1)
			return false;
		if (tarifs_preferentiels_enfant_2 != other.tarifs_preferentiels_enfant_2)
			return false;
		if (tarifs_preferentiels_enfant_3 != other.tarifs_preferentiels_enfant_3)
			return false;
		if (tarifs_preferentiels_enfant_4 != other.tarifs_preferentiels_enfant_4)
			return false;
		if (tarifs_preferentiels_enfant_5 != other.tarifs_preferentiels_enfant_5)
			return false;
		if (tarifs_preferentiels_enfant_6 != other.tarifs_preferentiels_enfant_6)
			return false;
		if (tarifs_preferentiels_enfant_7 != other.tarifs_preferentiels_enfant_7)
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		if (type_voie == null) {
			if (other.type_voie != null)
				return false;
		} else if (!type_voie.equals(other.type_voie))
			return false;
		if (ville == null) {
			if (other.ville != null)
				return false;
		} else if (!ville.equals(other.ville))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SauvegardeDevis [id=" + id + ", offre_commerciale_choisie=" + offre_commerciale_choisie
				+ ", date_creation=" + date_creation + ", date_adhesion_souhaitee=" + date_adhesion_souhaitee
				+ ", date_entree_administration=" + date_entree_administration + ", exMFP=" + exMFP
				+ ", date_de_naissance_prospect=" + date_de_naissance_prospect + ", etat_prospect=" + etat_prospect
				+ ", activite_prospect=" + activite_prospect + ", regime_alsace_moselle_prospect="
				+ regime_alsace_moselle_prospect + ", net_mensuel=" + net_mensuel + ", primeISSP=" + primeISSP
				+ ", prime_feu_rendement=" + prime_feu_rendement + ", prime_hors_feu_rendement="
				+ prime_hors_feu_rendement + ", Indice=" + Indice + ", NBI=" + NBI + ", code_postal_lieu_travail="
				+ code_postal_lieu_travail + ", profession=" + profession + ", prevoyance=" + prevoyance
				+ ", date_de_naissance_conjoint=" + date_de_naissance_conjoint + ", regime_alsace_moselle_conjoint="
				+ regime_alsace_moselle_conjoint + ", etat_conjoint=" + etat_conjoint + ", activite_conjoint="
				+ activite_conjoint + ", date_de_naissance_enfant_1=" + date_de_naissance_enfant_1
				+ ", regime_alsace_moselle_enfant_1=" + regime_alsace_moselle_enfant_1
				+ ", tarifs_preferentiels_enfant_1=" + tarifs_preferentiels_enfant_1 + ", date_de_naissance_enfant_2="
				+ date_de_naissance_enfant_2 + ", regime_alsace_moselle_enfant_2=" + regime_alsace_moselle_enfant_2
				+ ", tarifs_preferentiels_enfant_2=" + tarifs_preferentiels_enfant_2 + ", date_de_naissance_enfant_3="
				+ date_de_naissance_enfant_3 + ", regime_alsace_moselle_enfant_3=" + regime_alsace_moselle_enfant_3
				+ ", tarifs_preferentiels_enfant_3=" + tarifs_preferentiels_enfant_3 + ", date_de_naissance_enfant_4="
				+ date_de_naissance_enfant_4 + ", regime_alsace_moselle_enfant_4=" + regime_alsace_moselle_enfant_4
				+ ", tarifs_preferentiels_enfant_4=" + tarifs_preferentiels_enfant_4 + ", date_de_naissance_enfant_5="
				+ date_de_naissance_enfant_5 + ", regime_alsace_moselle_enfant_5=" + regime_alsace_moselle_enfant_5
				+ ", tarifs_preferentiels_enfant_5=" + tarifs_preferentiels_enfant_5 + ", date_de_naissance_enfant_6="
				+ date_de_naissance_enfant_6 + ", regime_alsace_moselle_enfant_6=" + regime_alsace_moselle_enfant_6
				+ ", tarifs_preferentiels_enfant_6=" + tarifs_preferentiels_enfant_6 + ", date_de_naissance_enfant_7="
				+ date_de_naissance_enfant_7 + ", regime_alsace_moselle_enfant_7=" + regime_alsace_moselle_enfant_7
				+ ", tarifs_preferentiels_enfant_7=" + tarifs_preferentiels_enfant_7 + ", cotisation_famille="
				+ cotisation_famille + ", cotisation_pro=" + cotisation_pro + ", cotisation_senior="
				+ cotisation_senior + ", cotisation_garantie_couverture_prime=" + cotisation_garantie_couverture_prime
				+ ", cotisation_garantie_deces=" + cotisation_garantie_deces + ", cotisation_maintient_de_salaire="
				+ cotisation_maintient_de_salaire + ", numero=" + numero + ", bis=" + bis + ", type_voie=" + type_voie
				+ ", adresse1=" + adresse1 + ", adresse2=" + adresse2 + ", ville=" + ville + ", prenom=" + prenom
				+ ", nom=" + nom + ", code_postal=" + code_postal + ", email=" + email + ", telephone=" + telephone
				+ ", choix_produit_sante=" + choix_produit_sante + ", choix_pack_famille=" + choix_pack_famille
				+ ", choix_pack_pro=" + choix_pack_pro + ", choix_pack_senior=" + choix_pack_senior
				+ ", choix_garantie_couverture_prime=" + choix_garantie_couverture_prime + ", choix_garantie_deces="
				+ choix_garantie_deces + ", choix_maintient_de_salaire=" + choix_maintient_de_salaire
				+ ", acceptation_email_offre_interiale=" + acceptation_email_offre_interiale
				+ ", acceptation_email_offre_partenaire=" + acceptation_email_offre_partenaire + "]";
	}

}