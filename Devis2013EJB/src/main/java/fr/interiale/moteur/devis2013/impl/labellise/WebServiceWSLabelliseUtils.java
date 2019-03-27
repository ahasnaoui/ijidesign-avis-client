package fr.interiale.moteur.devis2013.impl.labellise;

import java.math.BigDecimal;
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
import fr.interiale.moteur.devis2013.impl.commun.Moi.Etat;
import fr.interiale.moteur.devis2013.impl.commun.Pack;
import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;
import fr.interiale.moteur.devis2013.impl.interiale.CoveredPersonComparator;
import fr.interiale.moteur.devis2013.impl.interiale.DependanceTotale;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante;
import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante;
import fr.interiale.moteur.devis2013.impl.minju.ChoixProspectMINJU;
import fr.interiale.moteur.devis2013.interfaces.IProduitSante;
import fr.interiale.moteur.devis2013.service.IEdealHelper;

@Stateless
public class WebServiceWSLabelliseUtils implements IWebServiceWSLabelliseUtils {

//	private static Date premierJanvier2016=null;

	public WebServiceWSLabelliseUtils() {
/*		
 		try {
			premierJanvier2016 = new SimpleDateFormat("yyyyMMdd").parse("20160101");
		} catch (ParseException e) {
			log.error(e);
		}
*/
	}

	@EJB
	IWebServiceWS webServiceWS;

	@EJB
	IEdealHelper iEdealHelper;

	private static final String code_produit_sante_eco = "SECOT";
	private static final String code_produit_sante_responsable = "SRESL";
	private static final String code_produit_sante_essentielle = "SESSL";
	private static final String code_produit_sante_equilibre = "SEQUL";
	private static final String code_produit_sante_confort = "SCONL";
	private static final String code_produit_sante_serenite = "SEREL";
	private static final String code_produit_sante_plenitude = "SPLEL";
	
	private static final String code_pack_famille = "SUFAL";
	private static final String code_pack_pro = "SUPRL";
	private static final String code_pack_senior = "SUSEL";
	private static final String code_assistance_famille = "ASFAL";
	private static final String code_assistance_pro = "ASPRL";
	private static final String code_assistance_senior = "ASSEL";
	private static final String code_assistance_juridique = "JURIL";
	private static final String code_assistance = "ASINL";
	private static final String code_maintient_de_salaire = "ITL IPL";

	private static final String code_maintient_de_salaire_Inval = "INVL INPL";

	private static final String code_jour_de_carence = "ITCAL IPCAL";
	private static final String code_assistance_prevoyance = "ASPVL";
	private static final String code_capital_deces = "DCL";
	private static final String code_ecole = "ECOLL";
	private static final String code_reseau = "RESAL";
	private static final String code_cancer = "CANCR";

	private static final String code_option_itt = "PTL RENFORT";
	private static final String code_option_inv = "PVL RENFORT";

	private static final String code_option_OptionPTLFRASansFranchiseStandard = "PTLFRA STANDARD";
	private static final String code_option_OptionPTLFRASansFranchiseRenfort = "PTLFRA RENFORT";

	private static final String code_dependance_totale = "DEPAC";

	private static final String legalEntityClientIDLabellise = DevisProperties
			.getString("legalEntity.clientID.labellise");

	private static final String GBPContractGBPContractNumberLabellise = DevisProperties
			.getString("GBPContract.GBPContractNumber.labellise");

	private static final String legalEntityNameLabellise = DevisProperties.getString("legalEntity.name.labellise");

	Logger log = Logger.getLogger(WebServiceWSLabelliseUtils.class);

	private static Date a_perpette;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.labellise.IWebServiceWSLabelliseUtils
	 * #getReponseLabellise
	 * (fr.interiale.moteur.devis2013.impl.interiale.IDemande)
	 */
	public IReponseLabellise getReponseLabellise(IDemande demandeLabellise, RatingData ratingData)
			throws ServiceException {
		ratingData = addPersonData(demandeLabellise);
		ratingData = getPossibleOptions(ratingData);
		ratingData = addDynamicFields(ratingData);
		ratingData = getMemberQuote(ratingData);
		LegalEntityEmployee legalEntityEmployee = ratingData.getLegalEntities()[0].getGBPContracts()[0]
				.getMemberContracts()[0].getSubscriber();
		Correspondence correspondence = new Correspondence();
		if ((demandeLabellise.getCoordonnees() != null) && (correspondence != null)) {

			if (demandeLabellise.getCoordonnees() != null) {
				legalEntityEmployee.setLastName(demandeLabellise.getCoordonnees().getNom());
				legalEntityEmployee.setFirstName(demandeLabellise.getCoordonnees().getPrenom());
				correspondence.setPhoneNumber(demandeLabellise.getCoordonnees().getTelephone());
				correspondence.setEmail(demandeLabellise.getCoordonnees().getEmail());
			}
			org.tempuri.PostalAddress postalAddress = new PostalAddress();
			postalAddress.setZipCode(demandeLabellise.getCoordonnees().getCode_postal());
			if (demandeLabellise.getAdresse() != null) {
				if ((demandeLabellise.getAdresse().getVille() == null)
						|| ("".equalsIgnoreCase(demandeLabellise.getAdresse().getVille())))
					postalAddress.setCity("inconnue");
				else
					postalAddress.setCity(demandeLabellise.getAdresse().getVille());

				postalAddress.setCountryCode(IWebServiceWS.postalAddressDefaultCountryCode);

				if ((demandeLabellise.getAdresse().getAdresse1() != null)
						|| (!"".equalsIgnoreCase(demandeLabellise.getAdresse().getAdresse1()))) {

					postalAddress.setLine1(demandeLabellise.getAdresse().getNumero() + " "
							+ demandeLabellise.getAdresse().getBis() + " "
							+ demandeLabellise.getAdresse().getType_voie() + " "
							+ demandeLabellise.getAdresse().getAdresse1() + " "
							+ demandeLabellise.getAdresse().getAdresse2());
				} else
					postalAddress.setLine1(" _ ");
				postalAddress.setZipCode(demandeLabellise.getCoordonnees().getCode_postal());
				correspondence.setPostalAddress(postalAddress);

			}

		}
		org.tempuri.Correspondence[] correspondences = new Correspondence[] { correspondence };
		legalEntityEmployee.setCorrespondences(correspondences);
		return ratingDatatoReponseInteriale(ratingData);

	}

	private IReponseLabellise ratingDatatoReponseInteriale(RatingData ratingData) {
		List<IProduitSante> produits_sante = new ArrayList<IProduitSante>(7);
		Pack pack_famille = new Pack();
		Pack pack_pro = new Pack();
		Pack pack_senior = new Pack();

		GarantieDecesLabellise garantie_deces = new GarantieDecesLabellise();
		boolean have_garantie_deces = false;
		MaintientDeSalaireLabellise maintient_de_salaire = new MaintientDeSalaireLabellise();
		OptionMaintientSalaire option_maintient_salaire = new OptionMaintientSalaire();
		boolean have_maintient_de_salaire = false;

		BigDecimal assistance_prevoyance = null;
		ProduitSante produit_sante_responsable = new ProduitSante(Code_produit_sante.responsable);
		//ProduitSante produit_sante_eco = new ProduitSante(Code_produit_sante.eco);
		ProduitSante produit_sante_essentielle = new ProduitSante(Code_produit_sante.essentielle100);
		ProduitSante produit_sante_equilibre = new ProduitSante(Code_produit_sante.equilibre);
		ProduitSante produit_sante_confort = new ProduitSante(Code_produit_sante.confort);
		ProduitSante produit_sante_plenitude = new ProduitSante(Code_produit_sante.plenitude);
		ProduitSante produit_sante_serenite = new ProduitSante(Code_produit_sante.serenite);

		OptionPTLFRASansFranchiseRenfort optionPTLFRASansFranchiseRenfort = new OptionPTLFRASansFranchiseRenfort();

		OptionPTLFRASansFranchiseStandard optionPTLFRASansFranchiseStandard = new OptionPTLFRASansFranchiseStandard();

		DependanceTotale dependanceTotale = new DependanceTotale();
		
		Option[] options = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getOptions();
		for (Option option : options) {

			if (code_dependance_totale.equalsIgnoreCase(option.getCode())) {
				dependanceTotale=setCotisationDependanceTotale(dependanceTotale,option);
				
			} else if (code_option_OptionPTLFRASansFranchiseStandard.equalsIgnoreCase(option.getCode())) {
				optionPTLFRASansFranchiseStandard.setCotisation(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""), getChoix_prevoyance(option));
				
			} else if (code_option_OptionPTLFRASansFranchiseRenfort.equalsIgnoreCase(option.getCode())) {
				optionPTLFRASansFranchiseRenfort.setCotisation(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""), getChoix_prevoyance(option));
				
			} else if (code_produit_sante_responsable.equalsIgnoreCase(option.getCode())) {
				produit_sante_responsable = setCotisationSante(produit_sante_responsable, option);
			} /*else if (code_produit_sante_eco.equalsIgnoreCase(option.getCode())) {
				produit_sante_eco = setCotisationSante(produit_sante_eco, option);
			}*/ else if (code_produit_sante_essentielle.equalsIgnoreCase(option.getCode())) {
				produit_sante_essentielle = setCotisationSante(produit_sante_essentielle, option);
			} else if (code_produit_sante_equilibre.equalsIgnoreCase(option.getCode())) {
				produit_sante_equilibre = setCotisationSante(produit_sante_equilibre, option);
			} else if (code_produit_sante_confort.equalsIgnoreCase(option.getCode())) {
				produit_sante_confort = setCotisationSante(produit_sante_confort, option);
			} else if (code_produit_sante_serenite.equalsIgnoreCase(option.getCode())) {
				produit_sante_serenite = setCotisationSante(produit_sante_serenite, option);
			} else if (code_produit_sante_plenitude.equalsIgnoreCase(option.getCode())) {
				produit_sante_plenitude = setCotisationSante(produit_sante_plenitude, option);
			} else if (code_pack_famille.equalsIgnoreCase(option.getCode())) {
				pack_famille.setCotisation_base(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ ""));
			} else if (code_pack_pro.equalsIgnoreCase(option.getCode())) {
				pack_pro.setCotisation_base(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + ""));
			} else if (code_pack_senior.equalsIgnoreCase(option.getCode())) {
				pack_senior
						.setCotisation_base(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + ""));
			} else if (code_maintient_de_salaire.equalsIgnoreCase(option.getCode())) {
				maintient_de_salaire.setCotisation_maintient_de_salaire(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""), getChoix_prevoyance(option));

				have_maintient_de_salaire = true;

			} else if (code_maintient_de_salaire_Inval.equalsIgnoreCase(option.getCode())) {
				choix_prevoyance cp = getChoix_prevoyance(option);
				if ((cp.equals(choix_prevoyance.NIV3)) || cp.equals(choix_prevoyance.NIV4)) {
					maintient_de_salaire.setCotisation_maintient_de_salaire_inval(new BigDecimal(option
							.getOptionPremium().getEmployeePremiumAmount() + ""), cp);
				}
			} else if (code_option_inv.equalsIgnoreCase(option.getCode())) {
				choix_prevoyance cp = getChoix_prevoyance(option);
				if ((cp.equals(choix_prevoyance.NIV3)) || cp.equals(choix_prevoyance.NIV4)) {
					BigDecimal cotisation =new BigDecimal(option.getOptionPremium()
							.getEmployeePremiumAmount() + "");
					option_maintient_salaire.setCotisation_option_inval(cotisation, cp);
					optionPTLFRASansFranchiseRenfort.addCotisation(cotisation, cp);
					
				}

			} else if (code_option_itt.equalsIgnoreCase(option.getCode())) {
				option_maintient_salaire.setCotisation_option_ITT(new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + ""), getChoix_prevoyance(option));
				
			} else if (code_assistance_famille.equalsIgnoreCase(option.getCode())) {
				pack_famille.setAssistance(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + ""));
			} else if (code_assistance_pro.equalsIgnoreCase(option.getCode())) {
				pack_pro.setAssistance(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + ""));
			} else if (code_assistance_senior.equalsIgnoreCase(option.getCode())) {
				pack_senior.setAssistance(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + ""));
			} else if (code_assistance_juridique.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_protection_juridique = new BigDecimal(option.getOptionPremium()
						.getEmployeePremiumAmount() + "");

				produit_sante_responsable.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_essentielle.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_equilibre.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_confort.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_plenitude.setCotisation_protection_juridique(cotisation_protection_juridique);
				produit_sante_serenite.setCotisation_protection_juridique(cotisation_protection_juridique);
			} else if (code_assistance.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_assistance = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ "");

				produit_sante_responsable.setCotisation_assistance(cotisation_assistance);
				produit_sante_essentielle.setCotisation_assistance(cotisation_assistance);
				produit_sante_equilibre.setCotisation_assistance(cotisation_assistance);
				produit_sante_confort.setCotisation_assistance(cotisation_assistance);
				produit_sante_plenitude.setCotisation_assistance(cotisation_assistance);
				produit_sante_serenite.setCotisation_assistance(cotisation_assistance);
			} else if (code_ecole.equalsIgnoreCase(option.getCode())) {
				if (Dependence.cCoveredChild.equals(option.getOptionCoveredPersons()[0].getDependence())) {
					BigDecimal cotisation_ecole = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
							+ "");
					produit_sante_responsable.getCotisation_assurance_scolaire().add(cotisation_ecole);
					produit_sante_essentielle.getCotisation_assurance_scolaire().add(cotisation_ecole);
					produit_sante_equilibre.getCotisation_assurance_scolaire().add(cotisation_ecole);
					produit_sante_confort.getCotisation_assurance_scolaire().add(cotisation_ecole);
					produit_sante_plenitude.getCotisation_assurance_scolaire().add(cotisation_ecole);
					produit_sante_serenite.getCotisation_assurance_scolaire().add(cotisation_ecole);
				}
			} else if (code_reseau.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation_reseau = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
				if (dependence.equals(Dependence.cCoveredSubscriber)) {
					produit_sante_equilibre.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_confort.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_plenitude.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
					produit_sante_serenite.setCotisation_reseau_de_soin_adherent(cotisation_reseau);
				} else if (dependence.equals(Dependence.cCoveredSpouse)) {
					produit_sante_equilibre.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_confort.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_plenitude.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
					produit_sante_serenite.setCotisation_reseau_de_soin_conjoint(cotisation_reseau);
				} else if (dependence.equals(Dependence.cCoveredChild)) {
					produit_sante_equilibre.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_confort.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_plenitude.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
					produit_sante_serenite.getCotisations_reseau_de_soin_enfant().add(cotisation_reseau);
				}
			} else if (code_cancer.equalsIgnoreCase(option.getCode())) {
				BigDecimal cotisation = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
				Dependence dependence = option.getOptionCoveredPersons()[0].getDependence();
				if (dependence.equals(Dependence.cCoveredSubscriber)) {
					//produit_sante_eco.setCotisation_cancer_adherent(cotisation);
					produit_sante_essentielle.setCotisation_cancer_adherent(cotisation);
					produit_sante_equilibre.setCotisation_cancer_adherent(cotisation);
					produit_sante_confort.setCotisation_cancer_adherent(cotisation);
					produit_sante_plenitude.setCotisation_cancer_adherent(cotisation);
					produit_sante_serenite.setCotisation_cancer_adherent(cotisation);
				} else if (dependence.equals(Dependence.cCoveredSpouse)) {
					//produit_sante_eco.setCotisation_cancer_conjoint(cotisation);
					produit_sante_essentielle.setCotisation_cancer_conjoint(cotisation);
					produit_sante_equilibre.setCotisation_cancer_conjoint(cotisation);
					produit_sante_confort.setCotisation_cancer_conjoint(cotisation);
					produit_sante_plenitude.setCotisation_cancer_conjoint(cotisation);
					produit_sante_serenite.setCotisation_cancer_conjoint(cotisation);
				} else if (dependence.equals(Dependence.cCoveredChild)) {
					//produit_sante_eco.getCotisations_cancer_enfant().add(cotisation);
					produit_sante_essentielle.getCotisations_cancer_enfant().add(cotisation);
					produit_sante_equilibre.getCotisations_cancer_enfant().add(cotisation);
					produit_sante_confort.getCotisations_cancer_enfant().add(cotisation);
					produit_sante_plenitude.getCotisations_cancer_enfant().add(cotisation);
					produit_sante_serenite.getCotisations_cancer_enfant().add(cotisation);
				}
			} else if (code_jour_de_carence.equalsIgnoreCase(option.getCode())) {
				choix_prevoyance cp = getChoix_prevoyance(option);
				if (cp.equals(choix_prevoyance.NIV4)) {
					maintient_de_salaire.setCotisation_jour_de_carence(new BigDecimal(option.getOptionPremium()
							.getEmployeePremiumAmount() + ""), cp);
				}
			} else if (code_assistance_prevoyance.equalsIgnoreCase(option.getCode())) {
				assistance_prevoyance = new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount() + "");
			} else if (code_capital_deces.equalsIgnoreCase(option.getCode())) {
				garantie_deces.setCotisation_deces(new BigDecimal(option.getOptionPremium().getEmployeePremiumAmount()
						+ ""), getChoix_prevoyance(option));
				have_garantie_deces = true;
			}

		}
		// responsable, essentielle100, equilibre, confort,serenite,
		// plenitude
		if (assistance_prevoyance != null) {
			if (have_maintient_de_salaire) {
				maintient_de_salaire.setAssistance_prevoyance(assistance_prevoyance);
			} else if (have_garantie_deces) {
				garantie_deces.setAssistance_prevoyance(assistance_prevoyance);
			}
		}

/*		Date signatureDate = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0]
				.getSignatureDate();
*/
//		boolean isSupAPremierJanvier2016 = (signatureDate.after(premierJanvier2016));
		
/*		if(!isSupAPremierJanvier2016)produits_sante.add(produit_sante_responsable);
		else produits_sante.add(produit_sante_eco);*/
		produits_sante.add(produit_sante_essentielle);
		produits_sante.add(produit_sante_equilibre);
		produits_sante.add(produit_sante_confort);
		produits_sante.add(produit_sante_serenite);
		produits_sante.add(produit_sante_plenitude);

		if (!have_garantie_deces)
			garantie_deces = null;
		if (!have_maintient_de_salaire)
			maintient_de_salaire = null;


		
		return new ReponseLabellise(dependanceTotale, optionPTLFRASansFranchiseStandard,
				optionPTLFRASansFranchiseRenfort, produits_sante, pack_famille, pack_pro, pack_senior, garantie_deces,
				maintient_de_salaire, option_maintient_salaire, ratingData);
	}

	private ProduitSante setCotisationSante(ProduitSante produitSante, Option option) {
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
	
	private RatingData addPersonData(IDemande demandeLabellise) throws ServiceException {
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
		legalEntityEmployee.setDateAnciennete(demandeLabellise.getMoi().getDate_adhesion_souhaitee());
		Moi moi = demandeLabellise.getMoi();

		Conjoint conjoint = demandeLabellise.getConjoint();
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

		legalEntityEmployee.setIndice(moi.getIndice(webServiceWS.getPointDIndice(demandeLabellise.getMoi()
				.getDate_adhesion_souhaitee())));
		if ((moi.getNBI() != null) && (moi.getNBI() > 0))
			legalEntityEmployee.setIndiceNBI(moi.getNBI());
		else
			legalEntityEmployee.setIndiceNBI(0);
		// if (demandeLabellise.getCoordonnees() != null)
		// legalEntityEmployee.setLastName(demandeLabellise.getCoordonnees().getNom());

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
		if (demandeLabellise.getCoordonnees() != null) {
			// legalEntityEmployee.setFirstName(demandeLabellise.getCoordonnees().getPrenom());
			correspondence.setPhoneNumber(demandeLabellise.getCoordonnees().getTelephone());
			correspondence.setEmail(demandeLabellise.getCoordonnees().getEmail());

		}
		org.tempuri.PostalAddress postalAddress = new PostalAddress();
		if (demandeLabellise.getAdresse() != null) {

			if ((demandeLabellise.getAdresse().getVille() == null)
					|| ("".equalsIgnoreCase(demandeLabellise.getAdresse().getVille())))
				postalAddress.setCity("obligatoire il parait");
			else
				postalAddress.setCity(demandeLabellise.getAdresse().getVille());

			postalAddress.setCountryCode(IWebServiceWS.postalAddressDefaultCountryCode);

			if ((demandeLabellise.getAdresse().getAdresse1() == null)
					|| ("".equalsIgnoreCase(demandeLabellise.getAdresse().getAdresse1())))
				postalAddress.setLine1("obligatoire il parait");
			else
				postalAddress.setLine1(demandeLabellise.getAdresse().getAdresse1());
			if ((demandeLabellise.getAdresse().getAdresse2() == null)
					|| ("".equalsIgnoreCase(demandeLabellise.getAdresse().getAdresse2())))
				postalAddress.setLine2(demandeLabellise.getAdresse().getAdresse2());
			postalAddress.setZipCode(demandeLabellise.getCoordonnees().getCode_postal());
			correspondence.setPostalAddress(postalAddress);

		}
		org.tempuri.Correspondence[] correspondences = new Correspondence[] { correspondence };
		legalEntityEmployee.setCorrespondences(correspondences);

		// CoveredPersons
		ArrayList<CoveredPerson> coveredPersonsList = new ArrayList<CoveredPerson>();

		// Prospect
		CoveredPerson coveredPersonProspect = new CoveredPerson();

		coveredPersonProspect.setDependence(Dependence.cCoveredSubscriber);
		if (demandeLabellise.getCoordonnees() != null) {
			coveredPersonProspect.setFirstName(demandeLabellise.getCoordonnees().getNom());
			coveredPersonProspect.setLastName(demandeLabellise.getCoordonnees().getPrenom());
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
		List<Enfant> enfants = demandeLabellise.getEnfants();

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
		Date validityDate = demandeLabellise.getMoi().getDate_adhesion_souhaitee();
		Date signatureDate = validityDate;
		if (validityDate.before(minDate))
			validityDate = minDate;
		if (signatureDate.before(minDate))
			signatureDate = minDate;

		memberContractInteriale = new MemberContract(legalEntityEmployee, "INTERIALE", validityDate, signatureDate,
				coveredPersons, null);

		org.tempuri.MemberContract[] memberContracts = new MemberContract[] { memberContractInteriale };

		GBPContractInteriale = new GBPContract(GBPContractGBPContractNumberLabellise, "INTERIALE", null, null,
				memberContracts);
		org.tempuri.GBPContract[] GBPContracts = new GBPContract[] { GBPContractInteriale };
		legalEntityInteriale = new LegalEntity(legalEntityClientIDLabellise, legalEntityNameLabellise, null,
				GBPContracts);

		LegalEntity[] legalEntities = new LegalEntity[] { legalEntityInteriale };

		RatingData ratingData = new RatingData(validityDate, legalEntities);

		return ratingData;
	}

	private RatingData addDynamicFields(RatingData ratingData) {
		Option[] options = ratingData.getLegalEntities()[0].getGBPContracts()[0].getMemberContracts()[0].getOptions();
		List<Option> optionsFinales = new ArrayList<Option>();
		for (Option option : options) {
			// dispertion des produits prévoyance par niveau
			if ((code_maintient_de_salaire.equalsIgnoreCase(option.getCode()))
					|| (code_jour_de_carence.equalsIgnoreCase(option.getCode()))
					||(code_option_OptionPTLFRASansFranchiseStandard.equalsIgnoreCase(option.getCode()))
					||(code_option_OptionPTLFRASansFranchiseRenfort.equalsIgnoreCase(option.getCode()))
					|| (code_option_itt.equalsIgnoreCase(option.getCode()))
					|| (code_option_inv.equalsIgnoreCase(option.getCode()))
					|| (code_maintient_de_salaire_Inval.equalsIgnoreCase(option.getCode()))
					|| (code_capital_deces.equalsIgnoreCase(option.getCode()))) {

				for (DynamicEnumerateField field : option.getDynamicFields().getDynamicEnumerateField()) {
					if( ("Niveau".equalsIgnoreCase(field.getFieldCode()))||("NIV".equalsIgnoreCase(field.getFieldCode()))) {
						field.setAllValues(true);
					}
				}

				optionsFinales.add(option);

			} else {

				// dispertion du produit santé par personne couverte pour avoir
				// le
				// détail du tarif
				if ((code_produit_sante_essentielle.equalsIgnoreCase(option.getCode()))
						|| (code_produit_sante_responsable.equalsIgnoreCase(option.getCode()))
						|| (code_produit_sante_eco.equalsIgnoreCase(option.getCode()))
						|| (code_produit_sante_equilibre.equalsIgnoreCase(option.getCode()))
						|| (code_produit_sante_serenite.equalsIgnoreCase(option.getCode()))
						|| (code_produit_sante_plenitude.equalsIgnoreCase(option.getCode()))
						|| (code_produit_sante_confort.equalsIgnoreCase(option.getCode()))
						|| (code_reseau.equalsIgnoreCase(option.getCode()))
						|| (code_cancer.equalsIgnoreCase(option.getCode()))
						|| (code_dependance_totale.equalsIgnoreCase(option.getCode()))
						|| (code_ecole.equalsIgnoreCase(option.getCode()))) {
					Boolean bonusCouple = new Boolean(false);
					OptionCoveredPerson[] familleCouverte = option.getOptionCoveredPersons();
					for (OptionCoveredPerson optionCoveredPerson : familleCouverte) {
						Option nouvelleOption = new Option(option.getCode(), option.getName(),
								option.getLineOfBusiness(), option.getSubscriptionKind(), option.getCoverageAmount(),
								option.getPreRequiredOptions(), option.getExcludedOptions(), option.getDynamicFields(),
								new OptionCoveredPerson[] { optionCoveredPerson }, option.getOptionPremium(),
								bonusCouple);
						optionsFinales.add(nouvelleOption);
					}
				} else
					//produit sante ECO T a ne pas présenté
					if(!code_produit_sante_eco.equalsIgnoreCase(option.getCode())){
						optionsFinales.add(option);
					}
			}
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
		DynamicBooleanField garantieSanteDifDBF = new DynamicBooleanField("GARANSANTDIF", "Garantie santé différée",
				false, false, AllVAlues);
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

		DynamicEnumerateField CSPInteriale_DEF = new DynamicEnumerateField("CSPI", "C.S.P. Intériale", new String[] {
				"13F68", "13F9", "13F70", "13F127", "13F71", "13F72", "13F73", "13F74", "13F75", "13F76", "13F77",
				"13F78", "13F79", "13F80", "13F96" }, "13F9", csp, AllVAlues);

		DynamicBooleanField[] dynamicBooleanField = new DynamicBooleanField[] { enActiviteDBF, minorationVeuvageDBF,
				garantieSanteDiffereeDBF, minorationVeufveDBF, offreEcoleDBF, offreJeuneDBF, ancienneMutuelleMFP_DBF,
				garantieSanteDifDBF };
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
	private void filtreRatingData(ChoixProspectLabellise choixProspect, RatingData ratingData) {
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
			} else if (code_capital_deces.equalsIgnoreCase(option.getCode())) {
				if ((choixProspect.isChoix_garantie_deces())
						&& (getChoix_prevoyance(option) == choixProspect.getPrevoyance()))
					optionsFinales.add(option);
			} else if (code_ecole.equalsIgnoreCase(option.getCode())
					&& (Dependence.cCoveredChild.equals(option.getOptionCoveredPersons()[0].getDependence()))) {
				optionsFinales.add(option);
			} else if (code_jour_de_carence.equalsIgnoreCase(option.getCode())) {
				if ((choixProspect.isChoix_maintient_de_salaire() && (choix_prevoyance.NIV4.equals(choixProspect
						.getPrevoyance()))) && (getChoix_prevoyance(option) == choixProspect.getPrevoyance()))
					optionsFinales.add(option);
			} else if (code_maintient_de_salaire.equalsIgnoreCase(option.getCode())) {
				if ((choixProspect.isChoix_maintient_de_salaire())
						&& (getChoix_prevoyance(option) == choixProspect.getPrevoyance()))
					optionsFinales.add(option);
			} else if (code_maintient_de_salaire_Inval.equalsIgnoreCase(option.getCode())) {
				choix_prevoyance cp = getChoix_prevoyance(option);
				if ((cp.equals(choix_prevoyance.NIV3)) || cp.equals(choix_prevoyance.NIV4)) {
				if ((choixProspect.isChoix_maintient_de_salaire())
						&& (cp == choixProspect.getPrevoyance()))
					optionsFinales.add(option);
				}

			} else if (code_pack_famille.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_famille())
					optionsFinales.add(option);
			} else if (code_pack_pro.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_pro())
					optionsFinales.add(option);
			} else if (code_pack_senior.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_pack_senior())
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
			} else if (code_option_itt.equalsIgnoreCase(option.getCode())) {
				if ((choixProspect.isChoix_option_maintient_de_salaire())
						&& (getChoix_prevoyance(option) == choixProspect.getPrevoyance()))
					optionsFinales.add(option);
			} else if (code_option_inv.equalsIgnoreCase(option.getCode())) {
				choix_prevoyance cp = getChoix_prevoyance(option);
				if ((cp.equals(choix_prevoyance.NIV3)) || cp.equals(choix_prevoyance.NIV4)) {
					if ((choixProspect.isChoix_option_maintient_de_salaire()) && (cp == choixProspect.getPrevoyance()))
						optionsFinales.add(option);
				}

			} else if (code_dependance_totale.equalsIgnoreCase(option.getCode())) {
				if (choixProspect.isChoix_dependance_totale())
					optionsFinales.add(option);
			} else if (code_option_OptionPTLFRASansFranchiseRenfort.equalsIgnoreCase(option.getCode())
					&& (getChoix_prevoyance(option) == choixProspect.getPrevoyance())) {
				if (choixProspect.isChoix_code_option_OptionPTLFRASansFranchiseRenfort())
					optionsFinales.add(option);
			} else if (code_option_OptionPTLFRASansFranchiseStandard.equalsIgnoreCase(option.getCode())
					&& (getChoix_prevoyance(option) == choixProspect.getPrevoyance())) {
				if (choixProspect.isChoix_Code_option_OptionPTLFRASansFranchiseStandard())
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
	 * fr.interiale.moteur.devis2013.impl.labellise.IWebServiceWSLabelliseUtils
	 * #sendToEdeal
	 * (fr.interiale.moteur.devis2013.impl.labellise.ChoixProspectLabellise)
	 */
	public String sendToEdeal(ChoixProspectLabellise choixProspect, RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException {
		
	//	filtreRatingData(choixProspect, ratingData);

		return WebServiceWS.sendToEdeal(choixProspect, ratingData, iEdealHelper,"FPT",nomFichier,pdf,email);

	}
	
	public String sendToEdeal(ChoixProspectMINJU choixProspect, RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException {
		
	//	filtreRatingData(choixProspect, ratingData);

		return WebServiceWS.sendToEdeal(choixProspect, ratingData, iEdealHelper,"MINJU",nomFichier,pdf,email);

	}

	public void init() {
		

	}
	
	public String saveDevisEtapeQuatre(ChoixProspectLabellise choixProspect, RatingData ratingData, String nomFichier, byte[] pdf, 
			String email, final String identifiantDevis) throws ServiceException {
		return WebServiceWS.saveDevisEtapeQuatre(choixProspect, ratingData, iEdealHelper, "FPT", nomFichier, pdf, email, identifiantDevis);
	}


}
