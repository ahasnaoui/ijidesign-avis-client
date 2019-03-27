/**
 * 
 */
package fr.interiale.moteur.devis2013.impl.minju;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;
import org.apache.log4j.Logger;
import org.tempuri.Correspondence;
import org.tempuri.CoveredPerson;
import org.tempuri.Dependence;
import org.tempuri.DynamicBooleanField;
import org.tempuri.DynamicEnumerateField;
import org.tempuri.DynamicFields;
import org.tempuri.DynamicFloatField;
import org.tempuri.EmploymentStatus;
import org.tempuri.Frequency;
import org.tempuri.GBPContract;
import org.tempuri.GetMemberQuoteSoapStub;
import org.tempuri.GetPossibleOptionsSoapStub;
import org.tempuri.LegalEntity;
import org.tempuri.LegalEntityEmployee;
import org.tempuri.MemberContract;
import org.tempuri.Option;
import org.tempuri.OptionCoveredPerson;
import org.tempuri.PostalAddress;
import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.commun.Conjoint;
import fr.interiale.moteur.devis2013.impl.commun.DevisProperties;
import fr.interiale.moteur.devis2013.impl.commun.Enfant;
import fr.interiale.moteur.devis2013.impl.commun.IWebServiceWS;
import fr.interiale.moteur.devis2013.impl.commun.Moi;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Etat;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Statut;
import fr.interiale.moteur.devis2013.impl.commun.Pack;
import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;
import fr.interiale.moteur.devis2013.impl.interiale.CoveredPersonComparator;
import fr.interiale.moteur.devis2013.impl.interiale.DependanceTotale;
import fr.interiale.moteur.devis2013.impl.interiale.GarantieCouverturePrime;
import fr.interiale.moteur.devis2013.impl.interiale.GarantieDeces;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande;
import fr.interiale.moteur.devis2013.impl.interiale.MaintientDeSalaire;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.labellise.ChoixProspectLabellise;
import fr.interiale.moteur.devis2013.impl.minju.ProduitSanteMINJU.Code_produit_sante_minju;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;
import fr.interiale.moteur.devis2013.service.IEdealHelper;

/**
 * @author benjamin.morelle
 * 
 */
@Stateless
public class WebServiceWSMinJuUtils implements IWebServiceWSMinJuUtils {

	private  static Date premierJanvier2016;
	
	public WebServiceWSMinJuUtils() {
		try {
			premierJanvier2016=new SimpleDateFormat("yyyyMMdd").parse("20160101");
		} catch (ParseException e) {
			log.error(e);
		}
	}
	
	@EJB
	IWebServiceWS webServiceWS;

	@EJB
	IEdealHelper iEdealHelper;
	
	private static final String code_produit_sante_protection = "SPION";
	private static final String code_produit_sante_precision = "SPECI";
	private static final String code_produit_sante_precaution = "SPREC";
	private static final String code_produit_sante_perfection = "SPERF";
	
	//produit 2016
	
	private static final String code_pack_pharmaPlus = "SPHAJ";
	private static final String code_prevoyance_maintien_traitement_ITT = "ITMJU";
	private static final String code_prevoyance_pertes_primes_ITT = "PRMJU";
	private static final String code_prevoyance_invalidite_permanente = "IPAJU";
	private static final String code_prevoyance_rente_invalidite = "INVJU";
	private static final String code_prevoyance_garantie_deces = "DCMJU";
	private static final String code_dependance_totale ="DEPMJ";
	
	private static final String code_assistance_juridique = "JURIJ";
	private static final String code_assistance = "AXIST";
	private static final String code_reseau = "RESAM";
	private static final String code_teleconsultation = "CMEDI";
	private static final String code_maternite = "NAISJ";
	
	private static final String legalEntityClientIDMINJU = DevisProperties
			.getString("legalEntity.clientID.interiale");

	private static final String GBPContractGBPContractNumberMINJU = DevisProperties
			.getString("GBPContract.GBPContractNumber.minju");

	private static final String legalEntityNameMINJU = DevisProperties.getString("legalEntity.name.interiale");
	private static final Logger log = Logger.getLogger(WebServiceWSMinJuUtils.class);


	static {
		log.error("ip serveur wyde : " + IWebServiceWS.ip + "\n legalEntityClientIDInteriale : "
				+ legalEntityClientIDMINJU + "\n GBPContractGBPContractNumberInteriale : "
				+ GBPContractGBPContractNumberMINJU + "\n legalEntityNameInteriale : " + legalEntityNameMINJU);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.interiale.IWebServiceWSInterialeUtils
	 * #getReponseInteriale
	 * (fr.interiale.moteur.devis2013.impl.interiale.IDemande)
	 */
	public IReponseMINJU getReponseMinju(IDemande demandeMINJU, RatingData ratingData)
			throws ServiceException {
		Statut statut = demandeMINJU.getMoi().getStatut();
		Double salaire=0.0;
		Double salAnnuel=0.0;
		ratingData = addPersonData(demandeMINJU);
		ratingData = getPossibleOptions(ratingData);
		
		salaire = getSalaireProspect(demandeMINJU, statut);
		ratingData = addDynamicFields(ratingData, statut,salaire);
		ratingData = getMemberQuote(ratingData);
		
		LegalEntityEmployee legalEntityEmployee = ratingData.getLegalEntities()[0].getGBPContracts()[0]
				.getMemberContracts()[0].getSubscriber();
		Correspondence correspondence = new Correspondence();
		if ((demandeMINJU.getCoordonnees() != null) && (correspondence != null)) {

			if (demandeMINJU.getCoordonnees() != null) {
				legalEntityEmployee.setLastName(demandeMINJU.getCoordonnees().getNom());
				legalEntityEmployee.setFirstName(demandeMINJU.getCoordonnees().getPrenom());
				correspondence.setPhoneNumber(demandeMINJU.getCoordonnees().getTelephone());
				correspondence.setEmail(demandeMINJU.getCoordonnees().getEmail());

			}
			org.tempuri.PostalAddress postalAddress = new PostalAddress();
			if (demandeMINJU.getAdresse() != null) {
				if ((demandeMINJU.getAdresse().getVille() == null)
						|| ("".equalsIgnoreCase(demandeMINJU.getAdresse().getVille())))
					postalAddress.setCity("inconnue");
				else
					postalAddress.setCity(demandeMINJU.getAdresse().getVille());

				postalAddress.setCountryCode(IWebServiceWS.postalAddressDefaultCountryCode);

				if ((demandeMINJU.getAdresse().getAdresse1() != null)
						|| (!"".equalsIgnoreCase(demandeMINJU.getAdresse().getAdresse1()))) {

					postalAddress.setLine1(demandeMINJU.getAdresse().getNumero() + " "
							+ demandeMINJU.getAdresse().getBis() + " "
							+ demandeMINJU.getAdresse().getType_voie() + " "
							+ demandeMINJU.getAdresse().getAdresse1() + " "
							+ demandeMINJU.getAdresse().getAdresse2());
				} else
					postalAddress.setLine1(" _ ");
				postalAddress.setZipCode(demandeMINJU.getCoordonnees().getCode_postal());
				correspondence.setPostalAddress(postalAddress);

			}

		}
		org.tempuri.Correspondence[] correspondences = new Correspondence[] { correspondence };
		legalEntityEmployee.setCorrespondences(correspondences);
		
		IReponseMINJU retVal = ratingDatatoReponseMINJU(ratingData);
		
		return retVal;

	}

	/**
	 * Calcule le salaire du prospect en fonction de son profil
	 * @param demandeMINJU
	 * @param statut
	 * @param salaire
	 * @return
	 */
	private Double getSalaireProspect(IDemande demandeMINJU, Statut statut) {
		Double salAnnuel=0.0;
		Double retVal=0.0;
		// Si non titulaire 
		if(statut != Statut.titulaire) {
			// si actif on récupère son salaire mensuel
			if(Etat.actif.equals( demandeMINJU.getMoi().getEtat())) {
				
				salAnnuel = demandeMINJU.getMoi().getSab().doubleValue();
			} 
			// si pas actif on récupére sa pension annuelle brut.
			else {
				salAnnuel = demandeMINJU.getMoi().getPab().doubleValue();
			}
			
			
		 } 
		// Si titulaire
		else {
			// Pour les retraités seulement, on récupére le salaire annuel brut.
			 if(demandeMINJU.getMoi().getEtat().equals(Etat.retraite) ||demandeMINJU.getMoi().getEtat().equals(Etat.retraite_enfant_a_charge) ) {
				 salAnnuel = demandeMINJU.getMoi().getPab().doubleValue();
				
			 }
		 }
		retVal = salAnnuel/12;
		
		// MANTIS 16040 si le temps de travail est à 80 ou 90%, il faudrait recalculer le salaire. 
		if(demandeMINJU.getMoi().getTempsPartiel() != null && ( "90".equals(demandeMINJU.getMoi().getTempsPartiel()) || "80".equals(demandeMINJU.getMoi().getTempsPartiel())) ) {
			
			Double salairePartiel = 0.0;
			if("90".equals(demandeMINJU.getMoi().getTempsPartiel())) {
				salairePartiel = (retVal * 32) / 35 ;
			} else {
				salairePartiel = (retVal * 6) / 7;
			}
			
			return salairePartiel;
		}
		
		return retVal;
	}

	private IReponseMINJU ratingDatatoReponseMINJU(RatingData ratingData) {

		List<IProduitSante> produits_sante = new ArrayList<IProduitSante>(7);
		Pack pack_pharmaPlus = new Pack();
		GarantieCouverturePrime garantie_couverture_prime = new GarantieCouverturePrime();
		GarantieCouverturePrimeMinju garantie_pertes_primes_ITT_minju = new GarantieCouverturePrimeMinju();
		boolean have_garantie_couverture_prime = false;
		GarantieDeces garantie_deces = new GarantieDeces();
		GarantieDecesMinju garantie_deces_minju = new GarantieDecesMinju();
		boolean have_garantie_deces = false;
		MaintientDeSalaire maintient_de_salaire = new MaintientDeSalaire();
		MaintientDeSalaireMinju maintien_traitement_ITT_minju = new MaintientDeSalaireMinju();
		boolean have_maintient_de_salaire = false;
		InvaliditePermanenteMinju invalidite_permanente = new InvaliditePermanenteMinju();
		RenteInvaliditeMinju rente_invalidite = new RenteInvaliditeMinju();
		ProduitSanteMINJU produit_sante_protection = new ProduitSanteMINJU(Code_produit_sante_minju.protection);
		ProduitSanteMINJU produit_sante_precision = new ProduitSanteMINJU(Code_produit_sante_minju.precision);
		ProduitSanteMINJU produit_sante_precaution = new ProduitSanteMINJU(Code_produit_sante_minju.precaution);
		ProduitSanteMINJU produit_sante_perfection = new ProduitSanteMINJU(Code_produit_sante_minju.perfection);
		DependanceTotale dependanceTotale = new DependanceTotale();

		Option[] options = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getOptions();
		for (Option option : options) {
			log.info(">>>>>>>>>>>>>>>>>>>>> OPTION code : " + option.getCode() + ", amount : " + option.getOptionPremium().getEmployeePremiumAmount());
			if (code_dependance_totale.equalsIgnoreCase(option.getCode())) {
				dependanceTotale=setCotisationDependanceTotale(dependanceTotale,option);
			} else
			if (code_produit_sante_protection.equalsIgnoreCase(option.getCode())) {
				produit_sante_protection = setCotisationSante(produit_sante_protection, option);
			} else if (code_produit_sante_precision.equalsIgnoreCase(option.getCode())) {
				produit_sante_precision = setCotisationSante(produit_sante_precision, option);
			} else if (code_produit_sante_precaution.equalsIgnoreCase(option.getCode())) {
				produit_sante_precaution = setCotisationSante(produit_sante_precaution, option);
			} else if (code_produit_sante_perfection.equalsIgnoreCase(option.getCode())) {
				produit_sante_perfection = setCotisationSante(produit_sante_perfection, option);
			}
			else if (code_pack_pharmaPlus.equalsIgnoreCase(option.getCode())) {
				pack_pharmaPlus.setCotisation_base(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ ""));
			}
			
			else if (code_assistance_juridique.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_protection_juridique = new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + "");
				produit_sante_protection.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_precision.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_precaution.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_perfection.setCotisation_protection_juridique(cotisation_protection_juridique);
			} else if (code_assistance.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_assistance = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ "");
				produit_sante_protection.setCotisation_assistance(cotisation_assistance);
				produit_sante_precision.setCotisation_assistance(cotisation_assistance);
				produit_sante_precaution.setCotisation_assistance(cotisation_assistance);
				produit_sante_perfection.setCotisation_assistance(cotisation_assistance);
			} else if (code_teleconsultation.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_teleconsultation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
				if (dependence.equals(Dependence.cCoveredSubscriber)) {
					produit_sante_protection.setCotisation_teleconsultation_adherent(cotisation_teleconsultation);
					produit_sante_precision.setCotisation_teleconsultation_adherent(cotisation_teleconsultation);
					produit_sante_precaution.setCotisation_teleconsultation_adherent(cotisation_teleconsultation);
					produit_sante_perfection.setCotisation_teleconsultation_adherent(cotisation_teleconsultation);
				} else if (dependence.equals(Dependence.cCoveredSpouse)) {
					produit_sante_protection.setCotisation_teleconsultation_conjoint(cotisation_teleconsultation);
					produit_sante_precision.setCotisation_teleconsultation_conjoint(cotisation_teleconsultation);
					produit_sante_precaution.setCotisation_teleconsultation_conjoint(cotisation_teleconsultation);
					produit_sante_perfection.setCotisation_teleconsultation_conjoint(cotisation_teleconsultation);
				} else if (dependence.equals(Dependence.cCoveredChild)) {
					produit_sante_protection.getCotisations_teleconsultation_enfant().add(cotisation_teleconsultation);
					produit_sante_precision.getCotisations_teleconsultation_enfant().add(cotisation_teleconsultation);
					produit_sante_precaution.getCotisations_teleconsultation_enfant().add(cotisation_teleconsultation);
					produit_sante_perfection.getCotisations_teleconsultation_enfant().add(cotisation_teleconsultation);
				}
			} else if (code_maternite.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_maternite = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ "");
				produit_sante_precision.setCotisation_maternite(cotisation_maternite);
				produit_sante_precaution.setCotisation_maternite(cotisation_maternite);
				produit_sante_perfection.setCotisation_maternite(cotisation_maternite);
			}
			else if (code_reseau.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_reseau = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
				if (dependence.equals(Dependence.cCoveredSubscriber)) {
					produit_sante_protection.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_precision.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_precaution.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_perfection.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
				} else if (dependence.equals(Dependence.cCoveredSpouse)) {
					produit_sante_protection.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_precision.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_precaution.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_perfection.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
				} else if (dependence.equals(Dependence.cCoveredChild)) {
					produit_sante_protection.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_precision.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_precaution.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_perfection.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
				}
			}

			// PREVOYANCE MINJU
			else if (code_prevoyance_maintien_traitement_ITT.equalsIgnoreCase(option.getCode())) {
				maintien_traitement_ITT_minju.setCotisations_maintien(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""),getChoix_prevoyance(option));
			} else if (code_prevoyance_garantie_deces.equalsIgnoreCase(option.getCode())) {
				garantie_deces_minju.setCotisation_deces(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""),getChoix_prevoyance(option));
				have_garantie_deces = true;
			} else if (code_prevoyance_pertes_primes_ITT.equalsIgnoreCase(option.getCode())) {
				garantie_pertes_primes_ITT_minju.setCotisation_prime(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""),getChoix_prevoyance(option));
			} else if (code_prevoyance_invalidite_permanente.equalsIgnoreCase(option.getCode())) {
				invalidite_permanente.setCotisation_invalidite(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""),getChoix_prevoyance(option));
			} else if (code_prevoyance_rente_invalidite.equalsIgnoreCase(option.getCode())) {
				rente_invalidite.setCotisation_rente(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""),getChoix_prevoyance(option));
			}
		}
		
		produits_sante.add(produit_sante_protection);	
		produits_sante.add(produit_sante_precision);
		produits_sante.add(produit_sante_precaution);
		produits_sante.add(produit_sante_perfection);

		if (!have_garantie_couverture_prime)
			garantie_couverture_prime = null;
		if (!have_garantie_deces)
			garantie_deces = null;
		if (!have_maintient_de_salaire)
			maintient_de_salaire = null;
		
		
		
		
		ReponseMINJU retVal = new ReponseMINJU(produits_sante, pack_pharmaPlus, 
				garantie_pertes_primes_ITT_minju, garantie_deces_minju, maintien_traitement_ITT_minju,
				null, invalidite_permanente, rente_invalidite, dependanceTotale);
		return retVal;
	}

	private ProduitSanteMINJU setCotisationSante(ProduitSanteMINJU produitSante, Option option) {
		String cotisation = null;
		if (option.getOptionPremium() != null) {
			cotisation = option.getOptionPremium().getEmployeePremiumAmount() + "";
			Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
			if (dependence.equals(Dependence.cCoveredSubscriber)) {
				produitSante.setCotisation_base_adherent(new BigDecimal(cotisation));
			} else if (dependence.equals(Dependence.cCoveredSpouse)) {
				produitSante.setCotisation_base_conjoint(new BigDecimal(cotisation));
			} else if (dependence.equals(Dependence.cCoveredChild)) {
				produitSante.getCotisations_base_enfant().add(new BigDecimal(cotisation));
			}

		}
		return produitSante;
	}

	private DependanceTotale setCotisationDependanceTotale(DependanceTotale dependanceTotale, Option option ){
		Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
		if(dependence.equals(Dependence.cCoveredSubscriber)) {
			dependanceTotale.setCotisationAdh(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + ""));
		}else if (dependence.equals(Dependence.cCoveredSpouse)) {
			dependanceTotale.setCotisationConjoint(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + ""));
		}
		return dependanceTotale;
	}
	
	private RatingData addPersonData(IDemande demandeMINJU) throws ServiceException {
		

		LegalEntity legalEntityInteriale = null;
		GBPContract GBPContractInteriale = null;
		MemberContract memberContractInteriale = null;

		// Subscriber
		LegalEntityEmployee legalEntityEmployee = new LegalEntityEmployee();
		legalEntityEmployee.setDistributionNetworkCode("INTERIALE");
		// legalEntityEmployee.setClientID("0");
		legalEntityEmployee.setDateAnciennete(demandeMINJU.getMoi().getDate_adhesion_souhaitee());
		Moi moi = demandeMINJU.getMoi();

		Conjoint conjoint = demandeMINJU.getConjoint();
		legalEntityEmployee.setBirthDate(moi.getDate_de_naissance());

		EmploymentStatus employmentStatus = EmploymentStatus.tesActif;
		if (moi.getEtat() == Etat.retraite || moi.getEtat() == Etat.retraite_enfant_a_charge)
			employmentStatus = EmploymentStatus.tesRetraite;

		legalEntityEmployee.setEmploymentStatus(employmentStatus);

		legalEntityEmployee.setFrequence(Frequency.fMonthly);
		legalEntityEmployee.setFrequenceNBI(Frequency.fMonthly);
		legalEntityEmployee.setFrequencePrimesCouvertes(Frequency.fMonthly);
		legalEntityEmployee.setFrequencePrimesSoumisesPension(Frequency.fMonthly);
		legalEntityEmployee.setHireDate(moi.getDate_entree_administration());
		//MANTIS 16040 : si le prospect est en temps partiel, recalculer l'indice. 
		if(moi.getTempsPartiel() != null && ( "90".equals(moi.getTempsPartiel()) || "80".equals(moi.getTempsPartiel())) ) {
			
			Integer indice = moi.getIndice(webServiceWS.getPointDIndice(moi.getDate_adhesion_souhaitee()));
			Integer indicePartiel = 0;
			if("90".equals(moi.getTempsPartiel())) {
				 indicePartiel = Math.round((indice*32) / 35) ;
			} else {
				indicePartiel = Math.round((indice*6) / 7) ;
			}
			legalEntityEmployee.setIndice(indicePartiel);
			
		} else {
			
			legalEntityEmployee.setIndice(moi.getIndice(webServiceWS.getPointDIndice(moi.getDate_adhesion_souhaitee())));
			
		}
		if ((moi.getNBI() != null) && (moi.getNBI() > 0))
			legalEntityEmployee.setIndiceNBI(moi.getNBI());
		else
			legalEntityEmployee.setIndiceNBI(0);

		legalEntityEmployee.setNombreDEnfantsDejaCouvertsSurContratsDuConjoint(0);
		// PRIMES
		BigDecimal primesSoumisesPension = BigDecimal.ZERO;
		BigDecimal primesCouvertes = BigDecimal.ZERO;
		
		if(employmentStatus == EmploymentStatus.tesRetraite) {
			if(moi.getStatut() == Statut.titulaire) {
				if(moi.getTibp() != null && moi.getPab() != null) {
				primesSoumisesPension = calculerResultatAnnuelRetraiteTitulaire(moi.getTibp().doubleValue(), webServiceWS.getPointDIndice(demandeMINJU.getMoi()
						.getDate_adhesion_souhaitee()), moi.getPab().doubleValue());
				}
			} else {
				if(moi.getSabp() != null && moi.getPab() != null) {
					primesSoumisesPension = calculerResultatAnnuelRetraiteContractuel(moi.getSabp().doubleValue(), webServiceWS.getPointDIndice(demandeMINJU.getMoi()
							.getDate_adhesion_souhaitee()), moi.getPab().doubleValue());
				}
			}
		}

		legalEntityEmployee.setPrimesSoumisesPension(primesSoumisesPension.floatValue());
		legalEntityEmployee.setPrimesCouvertes(primesCouvertes.floatValue());

		// Subscriber.Adresse

		org.tempuri.Correspondence correspondence = new Correspondence();
		if (demandeMINJU.getCoordonnees() != null) {
			// legalEntityEmployee.setLastName(demandeInteriale.getCoordonnees().getNom());
			// legalEntityEmployee.setFirstName(demandeInteriale.getCoordonnees().getPrenom());
			correspondence.setPhoneNumber(demandeMINJU.getCoordonnees().getTelephone());
			correspondence.setEmail(demandeMINJU.getCoordonnees().getEmail());
		}
		org.tempuri.PostalAddress postalAddress = new PostalAddress();
		if (demandeMINJU.getAdresse() != null) {

			if ((demandeMINJU.getAdresse().getVille() == null)
					|| ("".equalsIgnoreCase(demandeMINJU.getAdresse().getVille())))
				postalAddress.setCity("obligatoire il parait");
			else
				postalAddress.setCity(demandeMINJU.getAdresse().getVille());

			postalAddress.setCountryCode(IWebServiceWS.postalAddressDefaultCountryCode);

			if ((demandeMINJU.getAdresse().getAdresse1() == null)
					|| ("".equalsIgnoreCase(demandeMINJU.getAdresse().getAdresse1())))
				postalAddress.setLine1("obligatoire il parait");
			else
				postalAddress.setLine1(demandeMINJU.getAdresse().getAdresse1());
			if ((demandeMINJU.getAdresse().getAdresse2() == null)
					|| ("".equalsIgnoreCase(demandeMINJU.getAdresse().getAdresse2())))
				postalAddress.setLine2(demandeMINJU.getAdresse().getAdresse2());
			postalAddress.setZipCode(demandeMINJU.getCoordonnees().getCode_postal());
			correspondence.setPostalAddress(postalAddress);

		}
		org.tempuri.Correspondence[] correspondences = new Correspondence[] { correspondence };
		legalEntityEmployee.setCorrespondences(correspondences);

		// CoveredPersons
		ArrayList<CoveredPerson> coveredPersonsList = new ArrayList<CoveredPerson>();

		// Prospect
		CoveredPerson coveredPersonProspect = new CoveredPerson();

		coveredPersonProspect.setDependence(Dependence.cCoveredSubscriber);
		if (demandeMINJU.getCoordonnees() != null) {
			coveredPersonProspect.setFirstName(demandeMINJU.getCoordonnees().getNom());
			coveredPersonProspect.setLastName(demandeMINJU.getCoordonnees().getPrenom());
		}
		coveredPersonProspect.setDateAnciennete(moi.getDate_adhesion_souhaitee());
		boolean enActiviteProspect = (Etat.retraite != moi.getEtat() && Etat.retraite_enfant_a_charge!= moi.getEtat());
		boolean offreEcoleProspect = moi.isEcole();
		boolean offreJeuneProspect = moi.isJeune();
		boolean ancienneMutuelleMFPProspect = moi.isExMFP();

		boolean hanProspect = false;
		boolean situationEtudianteProspect = moi.isEcole();
		boolean situationChomeurProspect = false;
		boolean regimeAlsaceMoselleProspect = moi.isEst_regime_alsace_moselle();

		String csp = DevisProperties.getString("vide");

		if (moi.getProfession() != null)
			csp = DevisProperties.getString(moi.getProfession().toString());

		DynamicFields dynamicFieldsCoveredPersonProspect = getDynamicFieldsAdh(enActiviteProspect, offreEcoleProspect,
				offreJeuneProspect, ancienneMutuelleMFPProspect, hanProspect, situationEtudianteProspect,
				situationChomeurProspect, regimeAlsaceMoselleProspect, csp);
		coveredPersonProspect.setDynamicFields(dynamicFieldsCoveredPersonProspect);
		coveredPersonProspect.setBirthDate(moi.getDate_de_naissance());
		coveredPersonsList.add(coveredPersonProspect);

		// Conjoint
		if (conjoint != null) {

			CoveredPerson coveredPersonConjoint = new CoveredPerson();
			coveredPersonConjoint.setBirthDate(conjoint.getDate_de_naissance());
			coveredPersonConjoint.setDateAnciennete(moi.getDate_adhesion_souhaitee());
			// coveredPersonConjoint.setCorrespondences(correspondences);
			coveredPersonConjoint.setDependence(Dependence.cCoveredSpouse);
			coveredPersonConjoint.setFirstName("du prospect");
			coveredPersonConjoint.setLastName("conjoint");
			boolean enActiviteConjoint = Etat.retraite != conjoint.getEtat();
			boolean offreEcoleConjoint = offreEcoleProspect;
			boolean offreJeuneConjoint = offreJeuneProspect;

			boolean ancienneMutuelleMFPConjoint = false;
			boolean hanConjoint = false;
			boolean situationEtudianteConjoint = false;
			boolean situationChomeurConjoint = false;
			boolean regimeAlsaceMoselleConjoint = conjoint.isRegime_alsace_moselle();

			DynamicFields dynamicFieldsCoveredPersonConjoint = getDynamicFieldsAdh(enActiviteConjoint,
					offreEcoleConjoint, offreJeuneConjoint, ancienneMutuelleMFPConjoint, hanConjoint,
					situationEtudianteConjoint, situationChomeurConjoint, regimeAlsaceMoselleConjoint, csp);
			coveredPersonConjoint.setDynamicFields(dynamicFieldsCoveredPersonConjoint);
			coveredPersonsList.add(coveredPersonConjoint);

		}
		List<Enfant> enfants = demandeMINJU.getEnfants();

		String[] lettres = new String[] { "A", "B", "C", "D", "E", "F", "G" };

		if ((enfants != null) && (enfants.size() > 0)) {
			int i = 0;
			for (Enfant enfant : enfants) {
				CoveredPerson coveredPersonEnfant = new CoveredPerson();
				coveredPersonEnfant.setBirthDate(enfant.getDate_de_naissance());
				coveredPersonEnfant.setDateAnciennete(moi.getDate_adhesion_souhaitee());
				coveredPersonEnfant.setDependence(Dependence.cCoveredChild);
				coveredPersonEnfant.setLastName("Enfant " + lettres[i]);
				coveredPersonEnfant.setClientID(i + "");
				coveredPersonEnfant.setFirstName(lettres[i++]);
				coveredPersonEnfant.setDynamicFields(getDynamicFieldsAdh(enActiviteProspect, offreEcoleProspect,
						offreJeuneProspect, ancienneMutuelleMFPProspect, enfant.isTarifs_preferentiels(),
						situationEtudianteProspect, situationChomeurProspect, enfant.isRegime_alsace_moselle(), csp));
				coveredPersonsList.add(coveredPersonEnfant);

			}
		}

		org.tempuri.CoveredPerson[] coveredPersons = new CoveredPerson[coveredPersonsList.size()];
		coveredPersons = coveredPersonsList.toArray(coveredPersons);
		Arrays.sort(coveredPersons, new CoveredPersonComparator());
		Date minDate = new Date();
		try {
			minDate = new SimpleDateFormat("ddMMyyyy").parse("02092011");
		} catch (ParseException e) {
			// impossible
		}
		Date validityDate = demandeMINJU.getMoi().getDate_adhesion_souhaitee();
		Date signatureDate = validityDate;
		if (validityDate.before(minDate))
			validityDate = minDate;
		if (signatureDate.before(minDate))
			signatureDate = minDate;

		memberContractInteriale = new MemberContract(legalEntityEmployee, "INTERIALE", validityDate, signatureDate,
				coveredPersons, null);

		org.tempuri.MemberContract[] memberContracts = new MemberContract[] { memberContractInteriale };

		GBPContractInteriale = new GBPContract(GBPContractGBPContractNumberMINJU, "INTERIALE", null, null,
				memberContracts);
		org.tempuri.GBPContract[] GBPContracts = new GBPContract[] { GBPContractInteriale };
		legalEntityInteriale = new LegalEntity(legalEntityClientIDMINJU, legalEntityNameMINJU, null,
				GBPContracts);

		LegalEntity[] legalEntities = new LegalEntity[] { legalEntityInteriale };

		RatingData ratingData = new RatingData(validityDate, legalEntities);

		return ratingData;
	}

	private RatingData addDynamicFields(RatingData ratingData, Statut statut, Double salaire) {
		Option[] options = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getOptions();
		List<Option> optionsFinales = new ArrayList<Option>();
		for (Option option : options) {
			// dispertion du produit santé par personne couverte pour avoir le
			// détail du tarif
			if ((code_produit_sante_protection.equalsIgnoreCase(option.getCode()))
					|| (code_produit_sante_precision.equalsIgnoreCase(option.getCode()))
					|| (code_produit_sante_precaution.equalsIgnoreCase(option.getCode()))
					|| (code_produit_sante_perfection.equalsIgnoreCase(option.getCode()))
					|| (code_dependance_totale.equalsIgnoreCase(option.getCode()))
					|| (code_reseau.equalsIgnoreCase(option.getCode()))
					|| (code_teleconsultation.equalsIgnoreCase(option.getCode()))) {
				Boolean bonusCouple = new Boolean(false);
				OptionCoveredPerson[] familleCouverte = option.getOptionCoveredPersons();
				if(option.getDynamicFields().getDynamicBooleanField() != null) {
					
					for (DynamicBooleanField field : option.getDynamicFields().getDynamicBooleanField()) {
						if("FCT".equalsIgnoreCase(field.getFieldCode())) {
							field.setBooleanValue(statut.equals(Statut.titulaire));
						}
					}
					
					
					
				}
				
				if(option.getDynamicFields().getDynamicFloatField()!=null) {
					
					for (DynamicFloatField field : option.getDynamicFields().getDynamicFloatField()) {
						if("SEURO".equalsIgnoreCase(field.getFieldCode())) {
							field.setFloatValue(salaire);
						}
					}
					
				}
				
				for (OptionCoveredPerson optionCoveredPerson : familleCouverte) {
					Option nouvelleOption = new Option(option.getCode(), option.getName(), option.getLineOfBusiness(),
							option.getSubscriptionKind(), option.getCoverageAmount(), option.getPreRequiredOptions(),
							option.getExcludedOptions(), option.getDynamicFields(),
							new OptionCoveredPerson[] { optionCoveredPerson }, option.getOptionPremium(), bonusCouple);
					optionsFinales.add(nouvelleOption);
				}
				
				
				
				
			} else if (
					(code_prevoyance_garantie_deces.equalsIgnoreCase(option.getCode()))
					|| (code_prevoyance_invalidite_permanente.equalsIgnoreCase(option.getCode()))
					||(code_prevoyance_maintien_traitement_ITT.equalsIgnoreCase(option.getCode()))
					||(code_prevoyance_pertes_primes_ITT.equalsIgnoreCase(option.getCode()))
					|| (code_prevoyance_rente_invalidite.equalsIgnoreCase(option.getCode()))) {

				for (DynamicEnumerateField field : option.getDynamicFields().getDynamicEnumerateField()) {
					if( ("Niveau".equalsIgnoreCase(field.getFieldCode()))||("NIV".equalsIgnoreCase(field.getFieldCode()))) {
						field.setAllValues(true);
					}
					
				}
				
				// PRIMES
				Double primesSoumisesPension = 0.0;
				if (ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getSubscriber().getPrimesSoumisesPension() != null ){
					primesSoumisesPension = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getSubscriber().getPrimesSoumisesPension().doubleValue();
				}
				
				if(option.getDynamicFields().getDynamicFloatField()!=null) {
					
					for (DynamicFloatField field : option.getDynamicFields().getDynamicFloatField()) {
						if("SEURO".equalsIgnoreCase(field.getFieldCode())) {
							field.setFloatValue(salaire);
						}
						
						if("RETR".equalsIgnoreCase(field.getFieldCode()) && !(primesSoumisesPension < 0)) { // Pour les titulaires on ajoute ici le montant des autres primes soumises à pension
							field.setFloatValue(primesSoumisesPension);
						}
					}
					
				}
				// Indiquer si le prospect est fonctionnaire, si oui wynsure calcule par rapport à l'indice sinon wynsure calcule par rapport au salaire.
				if(option.getDynamicFields().getDynamicBooleanField() != null) {
					
					for (DynamicBooleanField field : option.getDynamicFields().getDynamicBooleanField()) {
						if("FCT".equalsIgnoreCase(field.getFieldCode())) {
							field.setBooleanValue(statut.equals(Statut.titulaire));
						}
					}
					
				}
				optionsFinales.add(option);
				

			
				
			} else
				optionsFinales.add(option);
		}
		ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].setOptions(optionsFinales
				.toArray(new Option[optionsFinales.size()]));
		return ratingData;
	}

	private org.tempuri.DynamicFields getDynamicFieldsAdh(boolean enActivite, boolean offreEcole, boolean offreJeune,
			boolean ancienneMutuelleMFP, boolean han, boolean situationEtudiante, boolean situationChomeur,
			boolean regimeAlsaceMoselle, String csp) {
		boolean AllVAlues = false;

		DynamicFields result = new DynamicFields();

		DynamicBooleanField enActiviteDBF = new DynamicBooleanField("ENACT", "En activité", enActivite, enActivite,
				AllVAlues);
		DynamicBooleanField minorationVeuvageDBF = new DynamicBooleanField("MINOVEUV", "Minoration veuvage", false,
				false, AllVAlues);
		DynamicBooleanField garantieSanteDiffereeDBF = new DynamicBooleanField("GARANSANTDIF",
				"Garantie santé différée", false, false, AllVAlues);
		DynamicBooleanField minorationVeufveDBF = new DynamicBooleanField("MINOVEUF", "Minoration_Veuf(ve)", false,
				false, AllVAlues);
		// NE PAS DEMANDER - contournement ..
		DynamicBooleanField offreEcoleDBF = new DynamicBooleanField("OECOLE", "Offre Ecole", false, false, AllVAlues);
		DynamicBooleanField offreJeuneDBF = new DynamicBooleanField("OJEUNE", "Offre Jeune", false, false, AllVAlues);
		DynamicBooleanField ancienneMutuelleMFP_DBF = new DynamicBooleanField("MFP", "Ancienne mutuelle MFP",
				ancienneMutuelleMFP, ancienneMutuelleMFP, AllVAlues);
		String situationEtudiante_S = "Non";
		if (situationEtudiante)
			situationEtudiante_S = "Oui";

		DynamicEnumerateField situationEtudiante_DEF = new DynamicEnumerateField("ETU", "ETU (situation etudiante)",
				new String[] { "Oui", "Non" }, "Non", situationEtudiante_S, AllVAlues);
		

		String situationChomeur_S = "Non";
		if (situationChomeur)
			situationChomeur_S = "Oui";
		DynamicEnumerateField situationChomeur_DEF = new DynamicEnumerateField("CHO", "CHO(situation chomeur)",
				new String[] { "Oui", "Non" }, "Non", situationChomeur_S, AllVAlues);

		String han_S = "Non";
		if (han == true)
			han_S = "Oui";

		DynamicEnumerateField situationHandicap_DEF = new DynamicEnumerateField("HAN", "HAN (situation handicap)",
				new String[] { "Oui", "Non" }, "Non", han_S, false);

		DynamicEnumerateField articleL115_DEF = new DynamicEnumerateField("L115", "Article L. 115", new String[] {
				"Oui", "Non" }, "Non", "Non", AllVAlues);

		/*
		 * DynamicEnumerateField derogation_DEF = new
		 * DynamicEnumerateField("DER", "Derogation", new String[] { "Oui",
		 * "Non" }, "Non", "Non", false);
		 */

		String regimeAlsaceMoselle_S = "NON";
		if (regimeAlsaceMoselle)
			regimeAlsaceMoselle_S = "OUI";
		DynamicEnumerateField regimeAlsaceMoselle_DEF = new DynamicEnumerateField("RAMO", "Régime Alsace Moselle",
				new String[] { "OUI", "NON" }, "NON", regimeAlsaceMoselle_S, AllVAlues);
		// TODO : CSP à voir!
		DynamicEnumerateField CSPInteriale_DEF = new DynamicEnumerateField("CSPI", "C.S.P. Intériale", new String[] {"13F88","13F89","13F90","13F91"}, "13F91", csp, AllVAlues);

		DynamicBooleanField[] dynamicBooleanField = new DynamicBooleanField[] { enActiviteDBF, minorationVeuvageDBF,
				garantieSanteDiffereeDBF, minorationVeufveDBF, offreEcoleDBF, offreJeuneDBF, ancienneMutuelleMFP_DBF };
		result.setDynamicBooleanField(dynamicBooleanField);

		DynamicEnumerateField[] dynamicEnumerateField = new DynamicEnumerateField[] { articleL115_DEF,
				regimeAlsaceMoselle_DEF, CSPInteriale_DEF, situationEtudiante_DEF, situationChomeur_DEF,
				situationHandicap_DEF};
		result.setDynamicEnumerateField(dynamicEnumerateField);
		return result;
	}

	private RatingData getMemberQuote(RatingData ratingData) throws ServiceException {
		long getMemberQuoteDebut = System.currentTimeMillis();
		GetMemberQuoteSoapStub getMemberQuoteSoapStub;
		try {
			getMemberQuoteSoapStub = new GetMemberQuoteSoapStub();

			getMemberQuoteSoapStub.setHeader(webServiceWS.getAuthentication());
			getMemberQuoteSoapStub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://" + IWebServiceWS.ip
					+ "/GetMemberQuote.gold");

			RatingData r = getMemberQuoteSoapStub.getMemberQuote(ratingData);
			if (log.isDebugEnabled())
				log.debug(new Date(getMemberQuoteDebut) + " TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT getMemberQuote : "
						+ ((System.currentTimeMillis() - getMemberQuoteDebut) / 1000) + " s");
			return r;

		} catch (Exception e) {
			log.error(e);
			throw new ServiceException("getMemberQuote " + ratingData, e);
		}
	}

	private RatingData getPossibleOptions(RatingData ratingData) throws ServiceException {
		try {
			long getPossibleOptionsDebut = System.currentTimeMillis();
			GetPossibleOptionsSoapStub possibleOptionsSoapStub = new GetPossibleOptionsSoapStub();
			possibleOptionsSoapStub.setHeader(webServiceWS.getAuthentication());
			possibleOptionsSoapStub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://" + IWebServiceWS.ip
					+ "/GetPossibleOptions.gold");

			RatingData r = possibleOptionsSoapStub.getPossibleOptions(ratingData);
			if (log.isDebugEnabled())
				log.debug(new Date(getPossibleOptionsDebut) + " TTTTTTTTTTTTTTTTTTTTTTTTTTTTT getPossibleOptions : "
						+ ((System.currentTimeMillis() - getPossibleOptionsDebut) / 1000) + " s");
			System.out.println(new Date(getPossibleOptionsDebut) + " TTTTTTTTTTTTTTTTTTTTTTTTTTTTT getPossibleOptions : "
					+ ((System.currentTimeMillis() - getPossibleOptionsDebut) / 1000) + " s");
			return r;
		} catch (Exception e) {
			log.error(e);
			throw new ServiceException("getPossibleOptions " + ratingData, e);
		}
	}
	
	
	private choix_prevoyance getChoix_prevoyance(Option option) {
		choix_prevoyance prevoyance = choix_prevoyance.NIV1;

		for (DynamicEnumerateField field : option.getDynamicFields().getDynamicEnumerateField()) {
			if( ("Niveau".equalsIgnoreCase(field.getFieldCode()))||("NIV".equalsIgnoreCase(field.getFieldCode()))) {
				String value = field.getEnumerateValue();
				if ("NIV1".equalsIgnoreCase(value))
					return choix_prevoyance.NIV1;
				else if ("NIV2".equalsIgnoreCase(value))
					return choix_prevoyance.NIV2;
				else if ("NIV3".equalsIgnoreCase(value))
					return choix_prevoyance.NIV3;
				else if ("NIV4".equalsIgnoreCase(value))
					return choix_prevoyance.NIV4;
			}
		}
		return prevoyance;
	}
/*
	private void filtreRatingData(ChoixProspectInteriale choixProspect, RatingData ratingData) {

		MemberContract memberContract = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0];
		Date dateEntreAdmin = memberContract.getSubscriber().getHireDate();
		Date dateNaissance = memberContract.getSubscriber().getBirthDate();

		if (dateEntreAdmin.getTime() == dateNaissance.getTime())
			ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getSubscriber().setHireDate(
					null);

		Option[] options = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getOptions();
		List<Option> optionsFinales = new ArrayList<Option>();

		for (Option option : options) {
			if (code_assistance.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_assistance_famille.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_famille())
					optionsFinales.add(option);
			} else if (code_assistance_juridique.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_assistance_prevoyance.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces() || choixProspect.isChoix_maintient_de_salaire())
					optionsFinales.add(option);
			} else if (code_assistance_pro.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_pro())
					optionsFinales.add(option);
			} else if (code_assistance_senior.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_senior())
					optionsFinales.add(option);
			} else if (code_prime.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_couverture_prime())
					optionsFinales.add(option);
			} else if (code_prime_2.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_couverture_prime())
					optionsFinales.add(option);
			} else if (code_capital_deces_forfaitaire_accidentel.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces())
					optionsFinales.add(option);
			} else if (code_capital_deces_forfaitaire_seul.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces())
					optionsFinales.add(option);
			} else if (code_ecole.equalsIgnoreCase(option.getCode())
					&& (Dependence.cCoveredChild.equals(option.getOptionCoveredPersons()[0].getDependence()))) {
				optionsFinales.add(option);
			} else if (code_jour_de_carence.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_maintient_de_salaire())
					optionsFinales.add(option);
			} else if (code_maintient_de_salaire.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_maintient_de_salaire())
					optionsFinales.add(option);
			} else if (code_pack_famille.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_famille())
					optionsFinales.add(option);
			} else if (code_pack_pro.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_pro())
					optionsFinales.add(option);
			} else if (code_pack_senior.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_senior())
					optionsFinales.add(option);
			} else if (code_produit_sante_hospi_plus.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.hospi_plus == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_confort.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.confort == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_equilibre.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.equilibre == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_essentielle.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.essentielle100 == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_plenitude.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.plenitude == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_responsable.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.responsable == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_eco.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.eco == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_serenite.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante.serenite == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_reseau.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_cancer.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_dependance_totale.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_dependance_totale())
					optionsFinales.add(option);
			}else if (code_renfort_deces.equalsIgnoreCase(option.getCode())) {
					optionsFinales.add(option);
			}
		}

		ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].setOptions(optionsFinales
				.toArray(new Option[optionsFinales.size()]));

	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.interiale.IWebServiceWSInterialeUtils
	 * #sendToEdeal
	 * (fr.interiale.moteur.devis2013.impl.interiale.ChoixProspectInteriale)
	 */
	public String sendToEdeal(ChoixProspectMINJU choixProspect, RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException {

		//filtreRatingData(choixProspect, ratingData);

		return WebServiceWS.sendToEdeal(choixProspect, ratingData, iEdealHelper,"MINJU",nomFichier,pdf,email);
	}
	
	

	public void init() {
		

	}
	
	/**
	 * Pour les retraités titulaires, calculer les autres primes soumises à pension
	 * @param tibPension Traitement indiciaire brut
	 * @param pointIndice
	 * @param pensionAnnuelBrut
	 * @return
	 */
	private BigDecimal calculerResultatAnnuelRetraiteTitulaire(double tibPension, float pointIndice, double pensionAnnuelBrut) {
		
		double retVal=0;
		
		retVal = ((tibPension * 12) - pensionAnnuelBrut) / 12;
		
		
		return BigDecimal.valueOf(retVal);
		
	}
	
	/**
	 * Pour les retraités contactuels, calculer les autres primes soumises à pension
	 * @param sabPension salaire annuel brut
	 * @param pointIndice
	 * @param pensionAnnuelBrut
	 * @return
	 */
	private BigDecimal calculerResultatAnnuelRetraiteContractuel(double sabPension, float pointIndice, double pensionAnnuelBrut ) {
		
		double retVal = (sabPension - pensionAnnuelBrut) / 12 ;
		
		return BigDecimal.valueOf(retVal);
		
	}
	
	public String saveDevisEtapeQuatre(ChoixProspectMINJU choixProspect, RatingData ratingData, String nomFichier, byte[] pdf, 
			String email, final String identifiantDevis) throws ServiceException {
		return WebServiceWS.saveDevisEtapeQuatre(choixProspect, ratingData, iEdealHelper, "FPT", nomFichier, pdf, email, identifiantDevis);
	}
}
