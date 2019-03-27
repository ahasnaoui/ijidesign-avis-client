/**
 * 
 */
package fr.interiale.moteur.devis2013.impl.pn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;
import org.apache.log4j.Logger;
import org.tempuri.Correspondence;
import org.tempuri.CoveredPerson;
import org.tempuri.Dependence;
import org.tempuri.DynamicBooleanField;
import org.tempuri.DynamicEnumerateField;
import org.tempuri.DynamicFields;
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
import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Etat;
import fr.interiale.moteur.devis2013.impl.interiale.CoveredPersonComparator;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande;
import fr.interiale.moteur.devis2013.impl.pn.ProduitSantePN.Code_produit_sante_pn;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;
import fr.interiale.moteur.devis2013.service.IEdealHelper;

/**
 * @author benjamin.morelle
 * 
 */
@Stateless
public class WebServiceWSPNUtils implements IWebServiceWSPNUtils {

	public WebServiceWSPNUtils() {

	}

	@EJB
	IWebServiceWS webServiceWS;

	@EJB
	IEdealHelper iEdealHelper;

	// obligatoire

	private final static BigDecimal cinquante = new BigDecimal("50");
	private final static BigDecimal quatre_mille = new BigDecimal("4000");

	private static final String code_produit_sante_azur = "SAZUR";
	private static final String code_produit_sante_indigo = "SINDI";
	private static final String code_produit_sante_horizon = "SHORI";
	private static final String code_assistance_juridique = "JURI2";
	private static final String code_assistance = "ASIN2";
	private static final String code_prime_naissance = "NAIR2";
	private static final String code_reseau = "RESA2";

	private static final String code_maintient_de_salaire = "ITTR2 ITPR2";

	private static final String code_frais_obseques = "DCR2F";

	private static final String code_capital_deces_accidentel = "DCAR2";

	private static final String code_dependance = "DEPR2";

	// private static final String code_dependance_totale = "DEPR2";

	private static final String code_capital_deces_base_tsca_0_inf_62 = "DCR2V";
	private static final String code_capital_deces_base_tsca_9_inf_62 = "DCRV2";

	private static final String code_capital_deces_base_tsca_0_sup_62 = "DCR3V";
	private static final String code_capital_deces_base_tsca_9_sup_62 = "DCRV3";

	private static final String code_capital_deces_temp_tsca_0_inf_50 = "DCR2T";
	private static final String code_capital_deces_temp_tsca_9_inf_50 = "DCRT2";

	private static final String code_capital_deces_temp_tsca_0_inf_62 = "DCR3T";
	private static final String code_capital_deces_temp_tsca_9_inf_62 = "DCRT3";

	private static final String code_capital_deces_complementaire_tsca_0 = "DCR2A";
	private static final String code_capital_deces_complementaire_tsca_9 = "DCRA2";

	private static final String code_capital_deces_complementaire_conjoint_tsca_0 = "DCR2C";
	private static final String code_capital_deces_complementaire_conjoint_tsca_9 = "DCRC2";

	private static final String code_capital_deces_sup_70 = "DCR2R";

	//nouveau produit 2017
	private static final String code_renfort_deces = "DMAID";
	
	// facultatif
	private static final String code_maintient_de_prime = "GMPR2";

	private static final String legalEntityClientIDfilia = DevisProperties.getString("legalEntity.clientID.filia");

	private static final String GBPContractGBPContractNumberPn = DevisProperties
			.getString("GBPContract.GBPContractNumber.pn");

	private static final String legalEntityNameInteriale = DevisProperties.getString("legalEntity.name.interiale");
	private static final Logger log = Logger.getLogger(WebServiceWSPNUtils.class);

	private static Date a_perpette;

	static {
		log.error("ip serveur wyde : " + IWebServiceWS.ip + "\n legalEntityClientIDInteriale : "
				+ legalEntityClientIDfilia + "\n GBPContractGBPContractNumberInteriale : "
				+ GBPContractGBPContractNumberPn + "\n legalEntityNameInteriale : " + legalEntityNameInteriale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.interiale.IWebServiceWSInterialeUtils
	 * #getReponseInteriale
	 * (fr.interiale.moteur.devis2013.impl.interiale.IDemande)
	 */
	public ReponsePN getReponsePN(IDemande demandeInteriale, RatingData ratingData) throws ServiceException {

		ratingData = addPersonData(demandeInteriale);
		ratingData = getPossibleOptions(ratingData);
		ratingData = addDynamicFields(ratingData, demandeInteriale.getMoi().getPrime());
		ratingData = getMemberQuote(ratingData);

		LegalEntityEmployee legalEntityEmployee = ratingData.getLegalEntities()[0].getGBPContracts()[0]
				.getMemberContracts()[0].getSubscriber();
		Correspondence correspondence = new Correspondence();
		if ((demandeInteriale.getCoordonnees() != null) && (correspondence != null)) {

			if (demandeInteriale.getCoordonnees() != null) {
				legalEntityEmployee.setLastName(demandeInteriale.getCoordonnees().getNom());
				legalEntityEmployee.setFirstName(demandeInteriale.getCoordonnees().getPrenom());
				correspondence.setPhoneNumber(demandeInteriale.getCoordonnees().getTelephone());
				correspondence.setEmail(demandeInteriale.getCoordonnees().getEmail());

			}
			org.tempuri.PostalAddress postalAddress = new PostalAddress();
			if (demandeInteriale.getAdresse() != null) {
				if ((demandeInteriale.getAdresse().getVille() == null)
						|| ("".equalsIgnoreCase(demandeInteriale.getAdresse().getVille())))
					postalAddress.setCity("inconnue");
				else
					postalAddress.setCity(demandeInteriale.getAdresse().getVille());

				postalAddress.setCountryCode(IWebServiceWS.postalAddressDefaultCountryCode);

				if ((demandeInteriale.getAdresse().getAdresse1() != null)
						|| (!"".equalsIgnoreCase(demandeInteriale.getAdresse().getAdresse1()))) {

					postalAddress.setLine1(demandeInteriale.getAdresse().getNumero() + " "
							+ demandeInteriale.getAdresse().getBis() + " "
							+ demandeInteriale.getAdresse().getType_voie() + " "
							+ demandeInteriale.getAdresse().getAdresse1() + " "
							+ demandeInteriale.getAdresse().getAdresse2());
				} else
					postalAddress.setLine1(" _ ");
				postalAddress.setZipCode(demandeInteriale.getCoordonnees().getCode_postal());
				correspondence.setPostalAddress(postalAddress);

			}

		}
		org.tempuri.Correspondence[] correspondences = new Correspondence[] { correspondence };
		legalEntityEmployee.setCorrespondences(correspondences);

		return ratingDatatoReponseInteriale(ratingData);

	}

	private ReponsePN ratingDatatoReponseInteriale(RatingData ratingData) {

		List<IProduitSante> produits_sante = new ArrayList<IProduitSante>(3);

		FraisObsequePNFamille frais_obseque = new FraisObsequePNFamille();

		MaintienDesPrimesPN maintien_des_primes = new MaintienDesPrimesPN();

		MaintientDeSalairePN maintient_de_salaire = new MaintientDeSalairePN();

		ProduitSantePN produit_sante_azur = new ProduitSantePN(Code_produit_sante_pn.azur);
		ProduitSantePN produit_sante_horizon = new ProduitSantePN(Code_produit_sante_pn.horizon);
		ProduitSantePN produit_sante_indigo = new ProduitSantePN(Code_produit_sante_pn.indigo);

		GarantieDecesPN garantie_deces_base = new GarantieDecesPN();
		GarantieDecesPN garantie_deces_base_conjoint = new GarantieDecesPN();

		GarantieDecesPN garantie_deces_temp = new GarantieDecesPN();
		GarantieDecesPN garantie_deces_temp_conjoint = new GarantieDecesPN();

		GarantieDecesAmeliorePN garantie_deces_complementaire = new GarantieDecesAmeliorePN();
		GarantieDecesAmeliorePN garantie_deces_complementaire_conjoint = new GarantieDecesAmeliorePN();

		GarantieDeces70ansPNCouple garantie_deces_70 = new GarantieDeces70ansPNCouple();

		GarantieDecesAccidentelPNAdherent garantie_deces_accidentel = new GarantieDecesAccidentelPNAdherent();

		DependancePNCouple dependance = new DependancePNCouple();

		Produit renfort_deces=new Produit();
		
		Option[] options = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getOptions();

		for (Option option : options) {
			Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
			// SANTE
			if (code_produit_sante_azur.equalsIgnoreCase(option.getCode())) {
				produit_sante_azur = (ProduitSantePN) setCotisationFamille(produit_sante_azur, option);
				produit_sante_azur.addCode(code_produit_sante_azur);
			} else if (code_produit_sante_horizon.equalsIgnoreCase(option.getCode())) {
				produit_sante_horizon = (ProduitSantePN) setCotisationFamille(produit_sante_horizon, option);
				produit_sante_horizon.addCode(code_produit_sante_horizon);
			} else if (code_produit_sante_indigo.equalsIgnoreCase(option.getCode())) {
				produit_sante_indigo = (ProduitSantePN) setCotisationFamille(produit_sante_indigo, option);
				produit_sante_indigo.addCode(code_produit_sante_indigo);
			} else if (code_maintient_de_salaire.equalsIgnoreCase(option.getCode())) {
				maintient_de_salaire.setCotisation(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ ""));
				maintient_de_salaire.addCode(code_maintient_de_salaire);
			} else if (code_assistance_juridique.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_protection_juridique = new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + "");
				produit_sante_azur.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_azur.addCode(code_assistance_juridique);
				produit_sante_horizon.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_horizon.addCode(code_assistance_juridique);
				produit_sante_indigo.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_indigo.addCode(code_assistance_juridique);
			} else if (code_assistance.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_assistance = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ "");
				produit_sante_azur.setCotisation_assistance(cotisation_assistance);
				produit_sante_azur.addCode(code_assistance);
				produit_sante_horizon.setCotisation_assistance(cotisation_assistance);
				produit_sante_horizon.addCode(code_assistance);
				produit_sante_indigo.setCotisation_assistance(cotisation_assistance);
				produit_sante_indigo.addCode(code_assistance);
			} else if (code_prime_naissance.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_prime_naissance = new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + "");
				produit_sante_azur.setCotisation_prime_naissance(cotisation_prime_naissance);
				produit_sante_azur.addCode(code_prime_naissance);
				produit_sante_horizon.setCotisation_prime_naissance(cotisation_prime_naissance);
				produit_sante_horizon.addCode(code_prime_naissance);
				produit_sante_indigo.setCotisation_prime_naissance(cotisation_prime_naissance);
				produit_sante_indigo.addCode(code_prime_naissance);
			} else if (code_reseau.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_reseau = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				produit_sante_azur.addCode(code_reseau);
				produit_sante_horizon.addCode(code_reseau);
				produit_sante_indigo.addCode(code_reseau);
				if (dependence.equals(Dependence.cCoveredSubscriber)) {
					produit_sante_azur.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_horizon.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_indigo.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
				} else if (dependence.equals(Dependence.cCoveredSpouse)) {
					produit_sante_azur.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_horizon.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_indigo.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
				} else if (dependence.equals(Dependence.cCoveredChild)) {
					produit_sante_azur.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_horizon.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_indigo.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
				}

			} else if (code_dependance.equalsIgnoreCase(option.getCode())) {
				dependance = (DependancePNCouple) setCotisationCouple(dependance, option);
				dependance.addCode(code_dependance);
			}else if (code_renfort_deces.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				renfort_deces.setCotisation(cotisation);
			}
			// PREVOYANCE OBLIGATOIRE

			else if (code_frais_obseques.equalsIgnoreCase(option.getCode())) {
				frais_obseque = (FraisObsequePNFamille) setCotisationFamille(frais_obseque, option);
				frais_obseque.addCode(code_frais_obseques);
			} else if ((code_capital_deces_accidentel.equalsIgnoreCase(option.getCode()))) {
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				garantie_deces_accidentel.setCotisation(cotisation);
				garantie_deces_accidentel.addCode(code_capital_deces_accidentel);
			} else if (code_capital_deces_base_tsca_0_inf_62.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				garantie_deces_base.addCode(code_capital_deces_base_tsca_0_inf_62);
				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_base.setCotisation_deces_seul(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_base_conjoint.setCotisation_deces_seul(cotisation);
			} else if (code_capital_deces_base_tsca_9_inf_62.equalsIgnoreCase(option.getCode())) {
				garantie_deces_base.addCode(code_capital_deces_base_tsca_9_inf_62);
				;
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_base.setCotisation_deces_accidentel(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_base_conjoint.setCotisation_deces_accidentel(cotisation);
			} else if (code_capital_deces_base_tsca_0_sup_62.equalsIgnoreCase(option.getCode())) {
				garantie_deces_base.addCode(code_capital_deces_base_tsca_0_sup_62);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_base.setCotisation_deces_seul(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_base_conjoint.setCotisation_deces_seul(cotisation);

			} else if (code_capital_deces_base_tsca_9_sup_62.equalsIgnoreCase(option.getCode())) {
				garantie_deces_base.addCode(code_capital_deces_base_tsca_9_sup_62);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_base.setCotisation_deces_accidentel(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_base_conjoint.setCotisation_deces_accidentel(cotisation);

			} else if (code_capital_deces_temp_tsca_0_inf_50.equalsIgnoreCase(option.getCode())) {
				garantie_deces_temp.addCode(code_capital_deces_temp_tsca_0_inf_50);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_temp.setCotisation_deces_seul(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_temp_conjoint.setCotisation_deces_seul(cotisation);

			} else if (code_capital_deces_temp_tsca_9_inf_50.equalsIgnoreCase(option.getCode())) {
				garantie_deces_temp.addCode(code_capital_deces_temp_tsca_9_inf_50);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");

				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_temp.setCotisation_deces_accidentel(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_temp_conjoint.setCotisation_deces_accidentel(cotisation);

			} else if (code_capital_deces_temp_tsca_0_inf_62.equalsIgnoreCase(option.getCode())) {
				garantie_deces_temp.addCode(code_capital_deces_temp_tsca_0_inf_62);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");

				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_temp.setCotisation_deces_seul(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_temp_conjoint.setCotisation_deces_seul(cotisation);

			} else if (code_capital_deces_temp_tsca_9_inf_62.equalsIgnoreCase(option.getCode())) {
				garantie_deces_temp.addCode(code_capital_deces_temp_tsca_9_inf_62);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");

				if (dependence.equals(Dependence.cCoveredSubscriber))
					garantie_deces_temp.setCotisation_deces_accidentel(cotisation);
				else if (dependence.equals(Dependence.cCoveredSpouse))
					garantie_deces_temp_conjoint.setCotisation_deces_accidentel(cotisation);

			}

			// FACULTATIF
			else if (code_capital_deces_complementaire_conjoint_tsca_0.equalsIgnoreCase(option.getCode())) {
				garantie_deces_complementaire_conjoint.addCode(code_capital_deces_complementaire_conjoint_tsca_0);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Float seuil = option.getCoverageAmount().getRequestedCoverageAmount();
				garantie_deces_complementaire_conjoint.setCotisation_deces_seul(cotisation, seuil);
			} else if (code_capital_deces_complementaire_conjoint_tsca_9.equalsIgnoreCase(option.getCode())) {
				garantie_deces_complementaire_conjoint.addCode(code_capital_deces_complementaire_conjoint_tsca_9);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Float seuil = option.getCoverageAmount().getRequestedCoverageAmount();
				garantie_deces_complementaire_conjoint.setCotisation_deces_accidentel(cotisation, seuil);
			} else if (code_capital_deces_complementaire_tsca_0.equalsIgnoreCase(option.getCode())) {
				garantie_deces_complementaire.addCode(code_capital_deces_complementaire_tsca_0);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Float seuil = option.getCoverageAmount().getRequestedCoverageAmount();
				garantie_deces_complementaire.setCotisation_deces_seul(cotisation, seuil);
			} else if (code_capital_deces_complementaire_tsca_9.equalsIgnoreCase(option.getCode())) {
				garantie_deces_complementaire.addCode(code_capital_deces_complementaire_tsca_9);
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Float seuil = option.getCoverageAmount().getRequestedCoverageAmount();
				garantie_deces_complementaire.setCotisation_deces_accidentel(cotisation, seuil);
			} else if (code_capital_deces_sup_70.equalsIgnoreCase(option.getCode())) {
				garantie_deces_70.addCode(code_capital_deces_sup_70);
				garantie_deces_70 = (GarantieDeces70ansPNCouple) setCotisationCouple(garantie_deces_70, option);
			} else if (code_maintient_de_prime.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Float seuil = option.getCoverageAmount().getRequestedCoverageAmount();
				maintien_des_primes.setCotisation(seuil, cotisation);
				maintien_des_primes.addCode(code_maintient_de_prime);
			}
		}

		produits_sante.add(produit_sante_azur);
		produits_sante.add(produit_sante_indigo);
		produits_sante.add(produit_sante_horizon);

		GarantieDecesPNCouple garantie_deces_base_couple = new GarantieDecesPNCouple(garantie_deces_base,
				garantie_deces_base_conjoint);
		GarantieDecesPNCouple garantie_deces_temp_couple = new GarantieDecesPNCouple(garantie_deces_temp,
				garantie_deces_temp_conjoint);
		GarantieDecesAmeliorePNCouple garantie_deces_complementaire_couple = new GarantieDecesAmeliorePNCouple(
				garantie_deces_complementaire, garantie_deces_complementaire_conjoint);

		ReponsePN reponse = new ReponsePN(ratingData, produits_sante, maintient_de_salaire, frais_obseque,
				garantie_deces_accidentel, dependance, garantie_deces_70, maintien_des_primes,
				garantie_deces_base_couple, garantie_deces_temp_couple, garantie_deces_complementaire_couple,renfort_deces);

		return reponse;
	}

	private IProduitFamille setCotisationFamille(IProduitFamille produit, Option option) {
		String cotisation = null;
		if (option.getOptionPremium() != null) {
			cotisation = option.getOptionPremium().getEmployeePremiumAmount() + "";
			Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
			if (dependence.equals(Dependence.cCoveredSubscriber)) {
				produit.setCotisation_base_adherent(new BigDecimal(cotisation));
			} else if (dependence.equals(Dependence.cCoveredSpouse)) {
				produit.setCotisation_base_conjoint(new BigDecimal(cotisation));
			} else if (dependence.equals(Dependence.cCoveredChild)) {
				produit.setCotisation_base_enfant(new BigDecimal(cotisation));
			}

		}
		return produit;
	}

	private IProduitCouple setCotisationCouple(IProduitCouple produitCouple, Option option) {
		String cotisation = null;
		if (option.getOptionPremium() != null) {
			cotisation = option.getOptionPremium().getEmployeePremiumAmount() + "";
			Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
			if (dependence.equals(Dependence.cCoveredSubscriber)) {
				produitCouple.setCotisation_base_adherent(new BigDecimal(cotisation));
			} else if (dependence.equals(Dependence.cCoveredSpouse)) {
				produitCouple.setCotisation_base_conjoint(new BigDecimal(cotisation));
			}
		}
		return produitCouple;
	}

	private RatingData addPersonData(IDemande demandeInteriale) throws ServiceException {
		if (a_perpette == null)
			try {
				a_perpette = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990");
			} catch (ParseException e1) {

				// imposssible!!
			}

		LegalEntity legalEntityInteriale = null;
		GBPContract GBPContractInteriale = null;
		MemberContract memberContractInteriale = null;

		// Subscriber
		LegalEntityEmployee legalEntityEmployee = new LegalEntityEmployee();
		legalEntityEmployee.setDistributionNetworkCode("INTERIALE");
		// legalEntityEmployee.setClientID("0");
		Moi moi = demandeInteriale.getMoi();
		if (moi.getDate_entree_administration_MGP() == null)
			legalEntityEmployee.setDateAnciennete(moi.getDate_adhesion_souhaitee());
		else
			legalEntityEmployee.setDateAnciennete(moi.getDate_entree_administration_MGP());

		Conjoint conjoint = demandeInteriale.getConjoint();
		legalEntityEmployee.setBirthDate(moi.getDate_de_naissance());

		EmploymentStatus employmentStatus = EmploymentStatus.tesActif;
		if (moi.getEtat() == Etat.retraite)
			employmentStatus = EmploymentStatus.tesRetraite;

		legalEntityEmployee.setEmploymentStatus(employmentStatus);

		legalEntityEmployee.setFrequence(Frequency.fMonthly);
		legalEntityEmployee.setFrequenceNBI(Frequency.fMonthly);
		legalEntityEmployee.setFrequencePrimesCouvertes(Frequency.fMonthly);
		legalEntityEmployee.setFrequencePrimesSoumisesPension(Frequency.fMonthly);

		legalEntityEmployee.setHireDate(moi.getDate_entree_administration());

		legalEntityEmployee.setIndice(moi.getIndice(webServiceWS.getPointDIndice(demandeInteriale.getMoi()
				.getDate_adhesion_souhaitee())));
		if ((moi.getNBI() != null) && (moi.getNBI() > 0))
			legalEntityEmployee.setIndiceNBI(moi.getNBI());
		else
			legalEntityEmployee.setIndiceNBI(0);

		legalEntityEmployee.setNombreDEnfantsDejaCouvertsSurContratsDuConjoint(0);
		// PRIMES
		BigDecimal primesSoumisesPension = BigDecimal.ZERO;
		BigDecimal primesCouvertes = BigDecimal.ZERO;
		if (moi.getPrimeISSP().compareTo(BigDecimal.ZERO) != 0)
			primesSoumisesPension = moi.getPrimeISSP();
		else if (moi.getPrime_feu_rendement().compareTo(BigDecimal.ZERO) != 0)
			primesSoumisesPension = moi.getPrime_feu_rendement();
		if (moi.getPrime_hors_feu_rendement().compareTo(BigDecimal.ZERO) != 0)
			primesCouvertes = moi.getPrime_hors_feu_rendement();

		legalEntityEmployee.setPrimesSoumisesPension(primesSoumisesPension.floatValue());
		legalEntityEmployee.setPrimesCouvertes(primesCouvertes.floatValue());

		// Subscriber.Adresse

		org.tempuri.Correspondence correspondence = new Correspondence();
		if (demandeInteriale.getCoordonnees() != null) {
			// legalEntityEmployee.setLastName(demandeInteriale.getCoordonnees().getNom());
			// legalEntityEmployee.setFirstName(demandeInteriale.getCoordonnees().getPrenom());
			correspondence.setPhoneNumber(demandeInteriale.getCoordonnees().getTelephone());
			correspondence.setEmail(demandeInteriale.getCoordonnees().getEmail());
		}
		org.tempuri.PostalAddress postalAddress = new PostalAddress();
		if (demandeInteriale.getAdresse() != null) {

			if ((demandeInteriale.getAdresse().getVille() == null)
					|| ("".equalsIgnoreCase(demandeInteriale.getAdresse().getVille())))
				postalAddress.setCity("obligatoire il parait");
			else
				postalAddress.setCity(demandeInteriale.getAdresse().getVille());

			postalAddress.setCountryCode(IWebServiceWS.postalAddressDefaultCountryCode);

			if ((demandeInteriale.getAdresse().getAdresse1() == null)
					|| ("".equalsIgnoreCase(demandeInteriale.getAdresse().getAdresse1())))
				postalAddress.setLine1("obligatoire il parait");
			else
				postalAddress.setLine1(demandeInteriale.getAdresse().getAdresse1());
			if ((demandeInteriale.getAdresse().getAdresse2() == null)
					|| ("".equalsIgnoreCase(demandeInteriale.getAdresse().getAdresse2())))
				postalAddress.setLine2(demandeInteriale.getAdresse().getAdresse2());
			postalAddress.setZipCode(demandeInteriale.getCoordonnees().getCode_postal());
			correspondence.setPostalAddress(postalAddress);

		}
		org.tempuri.Correspondence[] correspondences = new Correspondence[] { correspondence };
		legalEntityEmployee.setCorrespondences(correspondences);

		// CoveredPersons
		ArrayList<CoveredPerson> coveredPersonsList = new ArrayList<CoveredPerson>();

		// Prospect
		CoveredPerson coveredPersonProspect = new CoveredPerson();

		coveredPersonProspect.setDependence(Dependence.cCoveredSubscriber);
		if (demandeInteriale.getCoordonnees() != null) {
			coveredPersonProspect.setFirstName(demandeInteriale.getCoordonnees().getNom());
			coveredPersonProspect.setLastName(demandeInteriale.getCoordonnees().getPrenom());
		}
		coveredPersonProspect.setDateAnciennete(moi.getDate_adhesion_souhaitee());
		boolean enActiviteProspect = Etat.retraite != moi.getEtat();
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
		List<Enfant> enfants = demandeInteriale.getEnfants();

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
		Date validityDate = demandeInteriale.getMoi().getDate_adhesion_souhaitee();
		Date signatureDate = validityDate;
		if (validityDate.before(minDate))
			validityDate = minDate;
		if (signatureDate.before(minDate))
			signatureDate = minDate;

		memberContractInteriale = new MemberContract(legalEntityEmployee, "INTERIALE", validityDate, signatureDate,
				coveredPersons, null);

		org.tempuri.MemberContract[] memberContracts = new MemberContract[] { memberContractInteriale };

		GBPContractInteriale = new GBPContract(GBPContractGBPContractNumberPn, "INTERIALE", null, null, memberContracts);
		org.tempuri.GBPContract[] GBPContracts = new GBPContract[] { GBPContractInteriale };
		legalEntityInteriale = new LegalEntity(legalEntityClientIDfilia, legalEntityNameInteriale, null, GBPContracts);

		LegalEntity[] legalEntities = new LegalEntity[] { legalEntityInteriale };

		RatingData ratingData = new RatingData(validityDate, legalEntities);

		return ratingData;
	}

	private RatingData addDynamicFields(RatingData ratingData, BigDecimal prime) {
		Option[] options = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getOptions();
		List<Option> optionsFinales = new ArrayList<Option>();

		for (Option option : options) {
			// dispertion du produit famille par personne couverte pour avoir le
			// détail du tarif
			if ((code_produit_sante_azur.equalsIgnoreCase(option.getCode()))
					|| (code_produit_sante_horizon.equalsIgnoreCase(option.getCode()))
					|| (code_produit_sante_indigo.equalsIgnoreCase(option.getCode()))
					|| (code_frais_obseques.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_accidentel.equalsIgnoreCase(option.getCode()))
					|| (code_reseau.equalsIgnoreCase(option.getCode()))) {
				Boolean bonusCouple = new Boolean(false);
				OptionCoveredPerson[] familleCouverte = option.getOptionCoveredPersons();
				for (OptionCoveredPerson optionCoveredPerson : familleCouverte) {
					Option nouvelleOption = new Option(option.getCode(), option.getName(), option.getLineOfBusiness(),
							option.getSubscriptionKind(), option.getCoverageAmount(), option.getPreRequiredOptions(),
							option.getExcludedOptions(), option.getDynamicFields(),
							new OptionCoveredPerson[] { optionCoveredPerson }, option.getOptionPremium(), bonusCouple);
					optionsFinales.add(nouvelleOption);
				}
			} else if ((code_capital_deces_base_tsca_0_inf_62.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_base_tsca_0_sup_62.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_base_tsca_9_inf_62.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_base_tsca_9_sup_62.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_temp_tsca_0_inf_50.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_temp_tsca_0_inf_62.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_temp_tsca_9_inf_50.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_temp_tsca_9_inf_62.equalsIgnoreCase(option.getCode()))
					|| (code_dependance.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_accidentel.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_sup_70.equalsIgnoreCase(option.getCode()))) {

				Boolean bonusCouple = new Boolean(false);
				OptionCoveredPerson[] familleCouverte = option.getOptionCoveredPersons();
				for (OptionCoveredPerson optionCoveredPerson : familleCouverte) {
					Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
					if (dependence.equals(Dependence.cCoveredSubscriber)
							|| dependence.equals(Dependence.cCoveredSpouse)) {
						Option nouvelleOption = new Option(option.getCode(), option.getName(),
								option.getLineOfBusiness(), option.getSubscriptionKind(), option.getCoverageAmount(),
								option.getPreRequiredOptions(), option.getExcludedOptions(), option.getDynamicFields(),
								new OptionCoveredPerson[] { optionCoveredPerson }, option.getOptionPremium(),
								bonusCouple);
						optionsFinales.add(nouvelleOption);
					}
				}

			} else if ((code_capital_deces_complementaire_tsca_0.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_complementaire_tsca_9.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_complementaire_conjoint_tsca_0.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces_complementaire_conjoint_tsca_9.equalsIgnoreCase(option.getCode()))) {

				option.getCoverageAmount().setAllCoverageAmounts(true);
				optionsFinales.add(option);

			} else if (code_maintient_de_prime.equalsIgnoreCase(option.getCode())) {
				if (prime.compareTo(BigDecimal.ZERO) != 0) {
					BigDecimal amount = prime.divide(cinquante, BigDecimal.ROUND_UP).multiply(cinquante);
					if (amount.compareTo(quatre_mille) == 1)
						amount = quatre_mille;
					option.getCoverageAmount().setRequestedCoverageAmount(amount.floatValue());
					optionsFinales.add(option);
				}

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

		String regimeAlsaceMoselle_S = "NON";
		if (regimeAlsaceMoselle)
			regimeAlsaceMoselle_S = "OUI";
		DynamicEnumerateField regimeAlsaceMoselle_DEF = new DynamicEnumerateField("RAMO", "Régime Alsace Moselle",
				new String[] { "OUI", "NON" }, "NON", regimeAlsaceMoselle_S, AllVAlues);

		DynamicEnumerateField CSPInteriale_DEF = new DynamicEnumerateField("CSPI", "C.S.P. Intériale", new String[] {
				"13F68", "13F9", "13F70", "13F127", "13F71", "13F72", "13F73", "13F74", "13F75", "13F76", "13F77",
				"13F78", "13F79", "13F80", "13F96" }, "13F9", csp, AllVAlues);

		DynamicBooleanField[] dynamicBooleanField = new DynamicBooleanField[] { enActiviteDBF, minorationVeuvageDBF,
				garantieSanteDiffereeDBF, minorationVeufveDBF, offreEcoleDBF, offreJeuneDBF, ancienneMutuelleMFP_DBF };
		result.setDynamicBooleanField(dynamicBooleanField);

		DynamicEnumerateField[] dynamicEnumerateField = new DynamicEnumerateField[] { articleL115_DEF,
				regimeAlsaceMoselle_DEF, CSPInteriale_DEF, situationEtudiante_DEF, situationChomeur_DEF,
				situationHandicap_DEF };
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
			log.info(">>>>>>>>>>> + adresse ip wynsure" + IWebServiceWS.ip);
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
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new ServiceException("getPossibleOptions " + ratingData + " " + e.getMessage(), e);
		}
	}

	private void filtreRatingData(ChoixProspectPN choixProspect, RatingData ratingData) {
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
			} else if (code_assistance_juridique.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_maintient_de_salaire.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_produit_sante_azur.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante_pn.azur == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_horizon.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante_pn.horizon == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_produit_sante_indigo.equalsIgnoreCase(option.getCode())) {
				if (Code_produit_sante_pn.indigo == choixProspect.getChoix_produit_sante())
					optionsFinales.add(option);
			} else if (code_reseau.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_capital_deces_accidentel.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_capital_deces_complementaire_conjoint_tsca_0.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_complementaire_conjoint())
					if (choixProspect.getMontant_garantie_deces_complementaire_conjoint() == option.getCoverageAmount()
							.getRequestedCoverageAmount())
						optionsFinales.add(option);
			} else if (code_capital_deces_complementaire_conjoint_tsca_9.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_complementaire_conjoint())
					if (choixProspect.getMontant_garantie_deces_complementaire_conjoint() == option.getCoverageAmount()
							.getRequestedCoverageAmount())
						optionsFinales.add(option);
			} else if (code_capital_deces_complementaire_tsca_0.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_complementaire())
					if (choixProspect.getMontant_garantie_deces_complementaire() == option.getCoverageAmount()
							.getRequestedCoverageAmount())
						optionsFinales.add(option);
			} else if (code_capital_deces_complementaire_tsca_9.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_complementaire())
					if (choixProspect.getMontant_garantie_deces_complementaire() == option.getCoverageAmount()
							.getRequestedCoverageAmount())
						optionsFinales.add(option);
			} else if (code_capital_deces_base_tsca_0_inf_62.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_capital_deces_base_tsca_0_sup_62.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_capital_deces_base_tsca_9_inf_62.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_capital_deces_base_tsca_9_sup_62.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_capital_deces_sup_70.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_capital_deces_temp_tsca_0_inf_50.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_temp())
					optionsFinales.add(option);
			} else if (code_capital_deces_temp_tsca_0_inf_62.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_temp())
					optionsFinales.add(option);
			} else if (code_capital_deces_temp_tsca_9_inf_50.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_temp())
					optionsFinales.add(option);
			} else if (code_capital_deces_temp_tsca_9_inf_62.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_garantie_deces_temp())
					optionsFinales.add(option);
			} else if (code_dependance.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_renfort_deces.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_frais_obseques.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_maintient_de_prime.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_maintien_prime())
					optionsFinales.add(option);
			} else if (code_maintient_de_salaire.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			} else if (code_prime_naissance.equalsIgnoreCase(option.getCode())) {
				optionsFinales.add(option);
			}

		}

		ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].setOptions(optionsFinales
				.toArray(new Option[optionsFinales.size()]));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.interiale.IWebServiceWSInterialeUtils
	 * #sendToEdeal
	 * (fr.interiale.moteur.devis2013.impl.interiale.ChoixProspectInteriale)
	 */
	public String sendToEdeal(ChoixProspectPN choixProspect, RatingData ratingData, String nomFichier, byte[] pdf,
			String email) throws ServiceException {

		// filtreRatingData(choixProspect, ratingData);

		return WebServiceWS.sendToEdeal(choixProspect, ratingData, iEdealHelper, "FPE", nomFichier, pdf, email);

	}

	public void init() {

	}

	public static void main(String[] args) {
		System.out.println(new BigDecimal(49).divide(cinquante, BigDecimal.ROUND_UP));
		System.out.println(new BigDecimal(50).divide(cinquante, BigDecimal.ROUND_UP));
		System.out.println(new BigDecimal(51).divide(cinquante, BigDecimal.ROUND_UP));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.interiale.IWebServiceWSInterialeUtils
	 * #sendToEdeal
	 * (fr.interiale.moteur.devis2013.impl.interiale.ChoixProspectInteriale)
	 */
	public String saveDevisEtapeQuatre(ChoixProspectPN choixProspect, RatingData ratingData, String nomFichier, byte[] pdf,
			String email, final String identifiantDevis) throws ServiceException {

		// filtreRatingData(choixProspect, ratingData);

		return WebServiceWS.saveDevisEtapeQuatre(choixProspect, ratingData, iEdealHelper, "FPE", nomFichier, pdf, email, identifiantDevis);

	}
}
