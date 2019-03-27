package fr.interiale.moteur.devis2013.impl.pn;

import javax.ejb.Remote;
import javax.xml.rpc.ServiceException;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande;

@Remote
public interface IWebServiceWSPNUtils {

	public static String JNDIName = "WebServiceWSPNUtils/remote";
	
	public abstract ReponsePN getReponsePN(IDemande demandeLabellise,RatingData ratingData) throws ServiceException;

	public abstract String sendToEdeal(ChoixProspectPN choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException;

	public void init();
	
	public abstract String saveDevisEtapeQuatre(ChoixProspectPN choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email, String identifiantDevis) throws ServiceException;
	
}