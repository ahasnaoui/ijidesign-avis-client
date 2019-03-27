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
import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante;
import fr.interiale.moteur.devis2013.impl.labellise.ChoixProspectLabellise;
import fr.interiale.moteur.devis2013.impl.labellise.IReponseLabellise;
import fr.interiale.moteur.devis2013.impl.labellise.IWebServiceWSLabelliseUtils;
import fr.interiale.moteur.devis2013.impl.labellise.ReponseLabellise;


public class TestMoteurLAbellise {

	public static void main(String[] args)  {
		Date debut=new Date();	
		System.out.println("DEBUT : "+debut);
		
			DemandeInteriale demandeInteriale = new DemandeInteriale();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Moi moi = null;
			try {
				moi = new Moi(simpleDateFormat.parse("01/06/1979"), Etat.actif, Activite.fonction_public_territoriale,
						simpleDateFormat.parse("01/09/2016"));
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
				enfant1 = new Enfant(simpleDateFormat.parse("01/01/2001"), false);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			Enfant enfant2 = null;
			try {
				enfant2 = new Enfant(simpleDateFormat.parse("02/01/2000"), false);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			Enfant enfant3 = null;
			try {
				enfant3 = new Enfant(simpleDateFormat.parse("01/01/2003"), false);
			} catch (InitialisationMoteurException e1) {
				
				e1.printStackTrace();
			} catch (ParseException e1) {
				
				e1.printStackTrace();
			}
			Enfant enfant4 = null;
			try {
				enfant4 = new Enfant(simpleDateFormat.parse("01/01/2002"), false);
			} catch (InitialisationMoteurException e) {
				
				e.printStackTrace();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			Enfant enfant5 = null;
			try {
				enfant5 = new Enfant(simpleDateFormat.parse("01/01/2004"), false);
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
			moi.setIndice(389);
			moi.setNBI(0);
			//moi.setPrimeISSP(new BigDecimal("120"));
			moi.setPrime_hors_feu_rendement(new BigDecimal("2000"));
			moi.setCode_postal_lieu_travail("31400");
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
			
			
			IWebServiceWSLabelliseUtils iWebServiceWSPNUtils = (IWebServiceWSLabelliseUtils) getInterface(IWebServiceWSLabelliseUtils.JNDIName,IWebServiceWSLabelliseUtils.class);
			
			System.out.println(demandeInteriale);
			
			IReponseLabellise reponse = null;
			try {
				
				reponse = iWebServiceWSPNUtils.getReponseLabellise(demandeInteriale, null);
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
