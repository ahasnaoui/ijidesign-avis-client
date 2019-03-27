package fr.interiale.moteur.devis2013.service;

import java.rmi.RemoteException;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.exception.ExceptionUtils;

import com.edeal.ws.custom.interiale.EspaceAdherentUnitedWSProxy;

import fr.interiale.moteur.devis2013.impl.commun.Adresse;
import fr.interiale.moteur.devis2013.impl.commun.ContactWeb;
import fr.interiale.moteur.devis2013.impl.commun.Coordonnees;
import fr.interiale.moteur.devis2013.impl.commun.FichierAttache;
import fr.interiale.service.InterialeWSProperties;

@Stateless
public class EdealHelper implements IEdealHelper {

	Logger log = Logger.getLogger(EdealHelper.class);

	public void createOpportuniteDevis(byte[] bytes, boolean createSollicitation, boolean optInITE, boolean optInGPE,
			String fonctionPublique, String typeTarification) throws RemoteException {
		EspaceAdherentUnitedWSProxy devisWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		devisWSProxy.createOpportuniteDevis(bytes, createSollicitation, optInITE, optInGPE, fonctionPublique,
				typeTarification);
	}
/*
	public String createOpportuniteDevisSynchro(byte[] bytes, boolean createSollicitation, boolean optInITE,
			boolean optInGPE, String fonctionPublique, String typeTarification) throws RemoteException {

		EspaceAdherentUnitedWSProxy devisWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);

		return devisWSProxy.createOpportuniteDevisSynchrone(bytes, createSollicitation, optInITE, optInGPE,
				fonctionPublique, typeTarification);
	}
*/
	public String createOpportuniteDevisSynchroIdentifieEtPDF(byte[] bytes, boolean createSollicitation, boolean optInITE, boolean optInGPE, java.lang.String fonctionPublique, java.lang.String typeTarification, byte[] pdf, java.lang.String nomFichier, java.lang.String email)  throws RemoteException {
		EspaceAdherentUnitedWSProxy devisWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		return devisWSProxy.createOpportuniteDevisSynchroneIdentifieEtPDF(bytes, createSollicitation, optInITE, optInGPE, fonctionPublique, typeTarification, pdf, nomFichier, email);
	}

	public Site[] getSites(String code_postal) throws RemoteException {
		EspaceAdherentUnitedWSProxy agenceWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		com.edeal.ws.custom.interiale.beans.Site[] sites = agenceWSProxy.getSites(code_postal);
		Site[] agences = new Site[sites.length];
		for (int i = 0; i < sites.length; i++) {
			com.edeal.ws.custom.interiale.beans.Site site = sites[i];
			agences[i] = new Site(site);
		}
		return agences;
	}

	public Site getSite(String code_postal) throws RemoteException {
		EspaceAdherentUnitedWSProxy agenceWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		com.edeal.ws.custom.interiale.beans.Site[] sites = agenceWSProxy.getSites(code_postal);
		for (int i = 0; i < sites.length; i++) {
			com.edeal.ws.custom.interiale.beans.Site site = sites[i];
			if ("AGENCE".equalsIgnoreCase(site.getType()))
				return new Site(site);
		}
		return null;
	}

	public String validateAdhesion(ContactWeb[] contactsWeb, Coordonnees coord, boolean finito) {
		EspaceAdherentUnitedWSProxy edealWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		com.edeal.ws.custom.interiale.beans.ContactWeb[] contactWebEdeal = contactWebPortalToEdeal(contactsWeb, coord);
		String idProspect = null;
		try {
			idProspect = edealWSProxy.validateAdhesionCourrier(contactWebEdeal, finito, false);
		} catch (RemoteException re) {
			log.error(ExceptionUtils.getFullStackTrace(re));
		}
		return idProspect;
	}

	public String validateAdhesionCourrier(ContactWeb[] contactsWeb, Coordonnees coord, boolean finito, boolean courrier) {
		EspaceAdherentUnitedWSProxy edealWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		com.edeal.ws.custom.interiale.beans.ContactWeb[] contactWebEdeal = contactWebPortalToEdeal(contactsWeb, coord);
		String idProspect = null;
		try {
			log.error("**** EdealHelper : on appelle validateAdhesionCourrier ******************");
			idProspect = edealWSProxy.validateAdhesionCourrier(contactWebEdeal, finito, courrier);
		} catch (RemoteException re) {
			log.error(ExceptionUtils.getFullStackTrace(re));
		}
		return idProspect;
	}

	private com.edeal.ws.custom.interiale.beans.ContactWeb[] contactWebPortalToEdeal(ContactWeb[] contactsWeb,
			Coordonnees coord) {
		com.edeal.ws.custom.interiale.beans.ContactWeb[] contactWebEdeal = new com.edeal.ws.custom.interiale.beans.ContactWeb[contactsWeb.length];

		for (int i = 0; i < contactsWeb.length; i++) {
			com.edeal.ws.custom.interiale.beans.ContactWeb contactWebEdeal_temp = new com.edeal.ws.custom.interiale.beans.ContactWeb();
			contactWebEdeal[i] = contactWebEdeal_temp;
			contactWebEdeal[i].setAdministration(contactsWeb[i].getAdministration());
			contactWebEdeal[i].setAdresse(adressePortalToEdeal(contactsWeb[i].getAdresse(), coord.getCode_postal()));
			contactWebEdeal[i].setCivilite(contactsWeb[i].getCivilite());
			contactWebEdeal[i].setNni(contactsWeb[i].getNni());
			contactWebEdeal[i].setCleNNI(contactsWeb[i].getCleNNI());
			contactWebEdeal[i].setCsp(contactsWeb[i].getCsp());
			contactWebEdeal[i].setDateEntreeAdministration(contactsWeb[i].getDateEntreeAdministration());
			contactWebEdeal[i].setDateNaissance(contactsWeb[i].getDateNaissance());
			contactWebEdeal[i].setDomaineDemande(contactsWeb[i].getDomaineDemande());
			contactWebEdeal[i].setEmail(contactsWeb[i].getEmail());
			contactWebEdeal[i].setEmployeur(contactsWeb[i].getEmployeur());
			contactWebEdeal[i].setFichiersAttaches(fichierAttachePortalToEdeal(contactsWeb[i].getFichiersAttaches()));
			contactWebEdeal[i].setFonction(contactsWeb[i].getFonction());
			contactWebEdeal[i].setFonctionPublique(contactsWeb[i].getFonctionPublique());
			contactWebEdeal[i].setId(contactsWeb[i].getId());
			contactWebEdeal[i].setIdEdeal(contactsWeb[i].getIdEdeal());
			contactWebEdeal[i].setIndiceMajore(contactsWeb[i].getIndiceMajore());
			contactWebEdeal[i].setInfosSousFormulaire(contactsWeb[i].getInfosSousFormulaire());
			contactWebEdeal[i].setMatricule(contactsWeb[i].getMatricule());
			contactWebEdeal[i].setMessage(contactsWeb[i].getMessage());
			contactWebEdeal[i].setNatureDemande(contactsWeb[i].getNatureDemande());
			contactWebEdeal[i].setNbi(contactsWeb[i].getNbi());
			contactWebEdeal[i].setNni(contactsWeb[i].getNni());
			contactWebEdeal[i].setNom(contactsWeb[i].getNom());
			contactWebEdeal[i].setNomJeuneFille(contactsWeb[i].getNomJeuneFille());
			contactWebEdeal[i].setNumAdherent(contactsWeb[i].getNumAdherent());
			contactWebEdeal[i].setObjetMessage(contactsWeb[i].getObjetMessage());
			contactWebEdeal[i].setOptInGrpITE(contactsWeb[i].isOptInGrpITE());
			contactWebEdeal[i].setOptInITE(contactsWeb[i].isOptInITE());
			contactWebEdeal[i].setPortable(contactsWeb[i].getPortable());
			contactWebEdeal[i].setPrenom(contactsWeb[i].getPrenom());
			contactWebEdeal[i].setPrimes(contactsWeb[i].getPrimes());
			contactWebEdeal[i].setPrimesFacultatives(contactsWeb[i].getPrimesFacultatives());
			contactWebEdeal[i].setRamo(contactsWeb[i].isRamo());
			contactWebEdeal[i].setSalaire(contactsWeb[i].getSalaire());
			contactWebEdeal[i].setSituationFamiliale(contactsWeb[i].getSituationFamiliale());
			contactWebEdeal[i].setStatut(contactsWeb[i].getStatut());
			contactWebEdeal[i].setTelephone(contactsWeb[i].getTelephone());
			contactWebEdeal[i].setTelephonePro(contactsWeb[i].getTelephonePro());
			contactWebEdeal[i].setTpsTravail(contactsWeb[i].getTpsTravail());
			contactWebEdeal[i].setTypeAdhesion(contactsWeb[i].getTypeAdhesion());
			contactWebEdeal[i].setTypeContact(contactsWeb[i].getTypeContact());
			contactWebEdeal[i].setTypeDemande(contactsWeb[i].getTypeDemande());
		}

		return contactWebEdeal;
	}

	private com.edeal.ws.custom.interiale.beans.Adresse adressePortalToEdeal(Adresse ad, String codePostal) {
		com.edeal.ws.custom.interiale.beans.Adresse adresse = new com.edeal.ws.custom.interiale.beans.Adresse();
		adresse.setCodePostal(codePostal);
		adresse.setComplementAdr1(ad.getAdresse2());
		adresse.setComplementAdr2("");
		adresse.setComplementNumVoie(ad.getBis());
		adresse.setLibelleVoie(ad.getAdresse1());
		adresse.setLocalite(ad.getVille());

		int numVoie = 0;
		if (!"".equals(ad.getNumero())) {
			numVoie = Integer.parseInt(ad.getNumero());
		}
		adresse.setNumVoie(numVoie);
		adresse.setPays("");
		adresse.setTypeVoie(ad.getType_voie());
		return adresse;
	}

	private com.edeal.ws.custom.interiale.beans.FichierAttache[] fichierAttachePortalToEdeal(FichierAttache[] fa) {
		com.edeal.ws.custom.interiale.beans.FichierAttache[] fichierAttache = null;
		if (null != fa) {
			fichierAttache = new com.edeal.ws.custom.interiale.beans.FichierAttache[fa.length];

			for (int i = 0; i < fa.length; i++) {
				com.edeal.ws.custom.interiale.beans.FichierAttache temp = new com.edeal.ws.custom.interiale.beans.FichierAttache();
				fichierAttache[i] = temp;
				fichierAttache[i].setBytes(fa[i].getBytes());
				fichierAttache[i].setDescription(fa[i].getDescription());
				fichierAttache[i].setNom(fa[i].getNom());
				fichierAttache[i].setTypemime(fa[i].getTypemime());
			}
		}
		return fichierAttache;
	}
	
	@Override
	public String devisWebEtapeUn(byte[] bytes) throws RemoteException {
		final EspaceAdherentUnitedWSProxy edealWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		return edealWSProxy.devisWebEtapeUn(bytes);
	}
	
	@Override
	public String devisWebEtapeDeux(byte[] bytes) throws RemoteException {
		final EspaceAdherentUnitedWSProxy edealWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		return edealWSProxy.devisWebEtapeDeux(bytes);
	}
	
	@Override
	public String devisWebEtapeTrois(byte[] bytes) throws RemoteException {
		final EspaceAdherentUnitedWSProxy edealWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		return edealWSProxy.devisWebEtapeTrois(bytes);
	}
	
	@Override
	public String devisWebEtapeQuatre(byte[] bytes) throws RemoteException {
		final EspaceAdherentUnitedWSProxy edealWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		return edealWSProxy.devisWebEtapeQuatre(bytes);
	}
	
	@Override
	public String devisWebEtapeSante(byte[] bytes) throws RemoteException {
		final EspaceAdherentUnitedWSProxy edealWSProxy = new EspaceAdherentUnitedWSProxy(InterialeWSProperties.EDEAL_WEBSERVICE_URL_VALUE);
		return edealWSProxy.devisWebEtapeSante(bytes);
	}

}
