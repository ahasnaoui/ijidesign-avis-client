package fr.interiale.moteur.devis2013.impl.minju;

import javax.ejb.Remote;
import javax.xml.rpc.ServiceException;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande;
import fr.interiale.moteur.devis2013.impl.labellise.ChoixProspectLabellise;

@Remote
public interface IWebServiceWSMinJuUtils {
	public static String JNDIName = "WebServiceWSMinJuUtils/remote";
	public abstract IReponseMINJU getReponseMinju(IDemande demandeInteriale,RatingData ratingData) throws ServiceException;
	public abstract String sendToEdeal(ChoixProspectMINJU choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email) throws ServiceException;
	public void init();
	public abstract String saveDevisEtapeQuatre(ChoixProspectMINJU choixProspect,RatingData ratingData,String nomFichier, byte[] pdf, String email, String identifiantDevis) throws ServiceException;

}