package fr.interiale.moteur.devis2013.impl.commun;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.xml.rpc.ServiceException;

import fr.interiale.moteur.devis2013.impl.commun.Moi.Activite;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Etat;
import fr.interiale.moteur.devis2013.impl.commun.Moi.Profession;
import fr.interiale.moteur.devis2013.impl.commun.Moi.typeTarification;
import fr.interiale.moteur.devis2013.impl.interiale.DemandeInteriale;
import fr.interiale.moteur.devis2013.impl.interiale.InitialisationMoteurException;
import fr.interiale.moteur.devis2013.impl.pn.ChoixProspectPN;
import fr.interiale.moteur.devis2013.impl.pn.IWebServiceWSPNUtils;
import fr.interiale.moteur.devis2013.impl.pn.ProduitSantePN.Code_produit_sante_pn;
import fr.interiale.moteur.devis2013.impl.pn.ReponsePN;

public class TestMoteurPN {

	public static void main(String[] args)  {
		Date debut=new Date();	
		System.out.println("DEBUT : "+debut);
		
			DemandeInteriale demandeInteriale = new DemandeInteriale();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Moi moi = null;
			try {
				moi = new Moi(simpleDateFormat.parse("01/05/1983"), Etat.actif, Activite.fonction_public_territoriale,
						simpleDateFormat.parse("01/08/2015"));
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}

			moi.setProfession(Profession.Personnel_des_Conseils_generaux_et_regionaux);
			Conjoint conjoint = null;
			try {
				conjoint = new Conjoint(simpleDateFormat.parse("04/03/1982"), Etat.actif, Activite.prive);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			Enfant enfant1 = null;
			try {
				enfant1 = new Enfant(simpleDateFormat.parse("01/01/2000"), false);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			Enfant enfant2 = null;
			try {
				enfant2 = new Enfant(simpleDateFormat.parse("01/01/2000"), false);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			Enfant enfant3 = null;
			try {
				enfant3 = new Enfant(simpleDateFormat.parse("01/01/2000"), false);
			} catch (InitialisationMoteurException e1) {
				
				e1.printStackTrace();
			} catch (ParseException e1) {
				
				e1.printStackTrace();
			}
			Enfant enfant4 = null;
			try {
				enfant4 = new Enfant(simpleDateFormat.parse("01/01/2000"), false);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			Enfant enfant5 = null;
			try {
				enfant5 = new Enfant(simpleDateFormat.parse("01/01/2000"), false);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			List<Enfant> enfants = new ArrayList<Enfant>();
			try {
				moi.setDate_entree_administration(simpleDateFormat.parse("01/01/2008"));
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			moi.setIndice(421);
			moi.setNBI(0);
			moi.setPrimeISSP(new BigDecimal("400"));
			moi.setPrime_hors_feu_rendement(new BigDecimal("4800"));
			moi.setCode_postal_lieu_travail("31200");
			moi.setEst_regime_alsace_moselle(false);
			moi.setExMFP(false);

			demandeInteriale.setMoi(moi);
			demandeInteriale.setConjoint(conjoint);
			enfants.add(enfant1);
			enfants.add(enfant2);
			enfants.add(enfant3);
			enfants.add(enfant4);
			enfants.add(enfant5);
			demandeInteriale.setEnfants(enfants);
			Adresse adresse = new Adresse("1", "", "rue", "aad1", "add2", "ville");
			demandeInteriale.setAdresse(adresse);
			Coordonnees coordonnees = new Coordonnees("M","prénomTrois", "nomTrois", "06000", "email@interiale.fr", "0101010101");
			demandeInteriale.setCoordonnees(coordonnees);
			
			ChoixProspectPN choixProspect = new ChoixProspectPN();
			choixProspect.setAcceptation_email_offre_interiale(true);
			choixProspect.setAcceptation_email_offre_partenaire(true);
			choixProspect.setChoix_produit_sante(Code_produit_sante_pn.azur);
			choixProspect.setType_tarification(typeTarification.SP);
			choixProspect.setChoix_garantie_deces_complementaire(true);
			choixProspect.setChoix_garantie_deces_complementaire_conjoint(true);
			choixProspect.setChoix_maintien_prime(true);
			choixProspect.setMontant_garantie_deces_complementaire(9500F);
			
			
			
			IWebServiceWSPNUtils iWebServiceWSPNUtils = (IWebServiceWSPNUtils) getInterface(IWebServiceWSPNUtils.JNDIName,IWebServiceWSPNUtils.class);
			
			System.out.println(demandeInteriale);
			
			ReponsePN reponse = null;
			try {
				
				reponse = iWebServiceWSPNUtils.getReponsePN(demandeInteriale, null);
			} catch (ServiceException e) {
				
				e.printStackTrace();
			}
			
			System.out.println(reponse);
			
			System.out.println("FIN - duree "+(new Date().getTime()-debut.getTime())+" ms");
			
			// début du test

/*			SessionTarificateurWynsure sessionTarificateurWynsure = new SessionTarificateurWynsure();
			System.out.println(MoteurDevis.getTarifsInteriale(sessionTarificateurWynsure, demandeInteriale, false));
			MoteurDevis.sendChoixProspectToEdeal(sessionTarificateurWynsure, choixProspect);
*/		//	 System.out.println(MoteurDevis.getTarifsLabellise(sessionTarificateurWynsure,demandeInteriale, false));
		//	 MoteurDevis.sendChoixProspectToEdeal(sessionTarificateurWynsure, choixProspectLabellise);
			
			
			
	
	}
	@SuppressWarnings("rawtypes")
	public static Object getInterface(String JNDIname, Class interfaceBean) {
	
/*		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
*/		Context context;
		Object o = null;
		try {
			Properties p = new Properties();
			//p.put("java.security.policy","alim.policy");
			p.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
			p.put("java.naming.provider.url","172.16.20.26:1099");
			p.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
			context = new InitialContext(p);
			o = context.lookup( "Devis2013/" + JNDIname);
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		PortableRemoteObject.narrow(o, interfaceBean);
		return o;
	}
	
	
}
