package fr.interiale.moteur.devis2013.impl.labellise;

import javax.ejb.Remote;
import javax.xml.rpc.ServiceException;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande;
import fr.interiale.moteur.devis2013.impl.minju.ChoixProspectMINJU;

@Remote
public interface IWebServiceWSLabelliseUtils {

	public static String JNDIName = "WebServiceWSLabelliseUtils/remote";
	
	public abstract IReponseLabellise getReponseLabellise(IDemande demandeLabellise,RatingData ratingData) throws ServiceException;

	public abstract String sendToEdeal(ChoixProspectLabellise choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException;
	
	public abstract String sendToEdeal(ChoixProspectMINJU choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException;

	public void init();
	
	public abstract String saveDevisEtapeQuatre(ChoixProspectLabellise choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email, String identifiantDevis) throws ServiceException;
	
}