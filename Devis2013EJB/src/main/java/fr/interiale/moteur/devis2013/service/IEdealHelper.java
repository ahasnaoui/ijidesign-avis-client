package fr.interiale.moteur.devis2013.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import fr.interiale.moteur.devis2013.impl.commun.ContactWeb;
import fr.interiale.moteur.devis2013.impl.commun.Coordonnees;




@Remote
public interface IEdealHelper {
	
	String JNDIName = "EdealHelper/remote";

	public void createOpportuniteDevis(byte[] bytes, boolean createSollicitation, boolean optInITE, boolean optInGPE, String fonctionPublique, String typeTarification) throws java.rmi.RemoteException;
	
//	public String createOpportuniteDevisSynchro(byte[] bytes, boolean createSollicitation, boolean optInITE, boolean optInGPE, String fonctionPublique, String typeTarification) throws java.rmi.RemoteException;

	public Site[] getSites(java.lang.String codePostal) throws java.rmi.RemoteException;

	public Site getSite(String code_postal)throws java.rmi.RemoteException;
	
	public String validateAdhesion(ContactWeb[] contactsWeb, Coordonnees coord, boolean finito);
	
	public String validateAdhesionCourrier(ContactWeb[] contactsWeb, Coordonnees coord, boolean finito, boolean courrier);
	
	public String createOpportuniteDevisSynchroIdentifieEtPDF(byte[] bytes, boolean createSollicitation, boolean optInITE, boolean optInGPE, java.lang.String fonctionPublique, java.lang.String typeTarification, byte[] pdf, java.lang.String nomFichier, java.lang.String email)  throws RemoteException;
	
	// Sauvegarde de l'Ã©tape 1 du devis
		public java.lang.String devisWebEtapeUn(byte[] bytes) throws java.rmi.RemoteException;
		// Sauvegarde de l'Ã©tape 2 du devis
		public java.lang.String devisWebEtapeDeux(byte[] bytes) throws java.rmi.RemoteException;
		// Sauvegarde de l'Ã©tape 3 du devis
		public java.lang.String devisWebEtapeTrois(byte[] bytes) throws java.rmi.RemoteException;
		// Sauvegarde de l'Ã©tape 4 du devis
		public java.lang.String devisWebEtapeQuatre(byte[] bytes) throws java.rmi.RemoteException;
		// Sauvegarde de l'Ã©tape du choix de l'offre SantÃ© du devis
		public java.lang.String devisWebEtapeSante(byte[] bytes) throws java.rmi.RemoteException;
}
