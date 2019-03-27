package fr.interiale.moteur.devis2013.impl.interiale;

import java.util.Map;

import javax.ejb.Remote;
import javax.xml.rpc.ServiceException;

import org.tempuri.RatingData;

@Remote
public interface IWebServiceWSInterialeUtils {
	public static String JNDIName = "WebServiceWSInterialeUtils/remote";
	public abstract IReponseInteriale getReponseInteriale(IDemande demandeInteriale,RatingData ratingData) throws ServiceException;
	public abstract String sendToEdeal(ChoixProspectInteriale choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException;
	public void init();
	public String saveDevisEtapeUn(Map<String, Object> params) throws ServiceException;
	public String saveDevisEtapeDeux(Map<String, Object> params) throws ServiceException;
	public String saveDevisEtapeTrois(Map<String, Object> params) throws ServiceException;
	public String saveDevisEtapeQuatre(ChoixProspectInteriale choixProspect, RatingData ratingData, String nomFichier, byte[] pdf, String email, String identifiantDevis) throws ServiceException;
	public String saveDevisEtapeSante(Map<String, Object> params) throws ServiceException;
}