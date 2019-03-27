package fr.interiale.moteur.devis2013.impl.commun;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Stub;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.PrefixedQName;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.log4j.Logger;
import org.tempuri.GetPointDIndiceSoapStub;
import org.tempuri.GetPointDIndice_In;
import org.tempuri.GetPointDIndice_Out;
import org.tempuri.Option;
import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.interiale.ChoixProspectInteriale;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.impl.labellise.ChoixProspectLabellise;
import fr.interiale.moteur.devis2013.impl.minju.ChoixProspectMINJU;
import fr.interiale.moteur.devis2013.service.IEdealHelper;

@Stateless
public class WebServiceWS implements IWebServiceWS {

	private final static Logger log = Logger.getLogger(WebServiceWS.class);
	private final static String login = DevisProperties.getString("ws.wyde.login");
	private final static String pwd = DevisProperties.getString("ws.wyde.pwd");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.interiale.moteur.devis2013.impl.commun.IWebServiceWS#getPointDIndice
	 * (java.util.Date, java.lang.String)
	 */
	public float getPointDIndice(Date dateAdhesionDate) throws ServiceException {
		long getPointDIndiceDebut = System.currentTimeMillis();
		GetPointDIndiceSoapStub pointDIndiceSoapStub;
		GetPointDIndice_Out out = null;
		GetPointDIndice_In in = new GetPointDIndice_In(dateAdhesionDate);
		try {
			pointDIndiceSoapStub = new GetPointDIndiceSoapStub();
			pointDIndiceSoapStub.setHeader(getAuthentication());
			pointDIndiceSoapStub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://" + ip + "/GetPointDIndice.gold");

			try {

				out = pointDIndiceSoapStub.getPointDIndice(in);

				if (log.isDebugEnabled())
					log.debug(new Date(getPointDIndiceDebut) + " TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT getPointDIndice : "
							+ ((System.currentTimeMillis() - getPointDIndiceDebut) / 1000) + " s");

			} catch (RemoteException e) {
				log.error(e);
				throw new ServiceException("probleme lors de la récupération du point d'indice", e);
			}
		} catch (AxisFault e1) {
			log.error(e1);
			throw new ServiceException("probleme lors de la récupération du point d'indice", e1);
		} catch (SOAPException e) {

			log.error(e);
			throw new ServiceException("probleme lors de la récupération du point d'indice", e);
		}
		return new Double(out.getPointDIndice()).floatValue();
	}

	public SOAPHeaderElement getAuthentication() throws SOAPException {
		String uri = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		SOAPHeaderElement wsseSecurity = new SOAPHeaderElement(new PrefixedQName(uri, "Security", "wsse"));
		MessageElement usernameToken = new MessageElement(uri, "wsse:UserNameToken");
		MessageElement username = new MessageElement(uri, "wsse:Username");
		MessageElement password = new MessageElement(uri, "wsse:Password");

		username.setObjectValue(login);
		usernameToken.addChild(username);

		password.setObjectValue(pwd);
		usernameToken.addChild(password);
		wsseSecurity.addChild(usernameToken);

		return wsseSecurity;
	}

	public boolean testActivite() {
		boolean test = true;
		try {
			getPointDIndice(new Date());
		} catch (Throwable t) {
			log.fatal("web service winsure indisponible : " + t.getMessage(), t);
			return false;
		}
		return test;
	}

	public void init() {

	}

	public static String getCode(Set<String> codes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : codes) {
			stringBuilder.append(string).append(" ");
		}
		return stringBuilder.toString();
	}
/*	@Deprecated
	public static String sendToEdeal(IChoixProspect choixProspect, RatingData ratingData, IEdealHelper iEdealHelper,String fonctionPublique)
			throws ServiceException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = null;

		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		try {
			out.writeObject(ratingData);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		bytes = bos.toByteArray();

		try {
			String ret = iEdealHelper.createOpportuniteDevisSynchro(bytes, choixProspect.isEnvoi_courrier(),
					choixProspect.isAcceptation_email_offre_interiale(), choixProspect
							.isAcceptation_email_offre_partenaire(), fonctionPublique, choixProspect.getType_tarification()
							.toString());
			return ret;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}

	}*/
/*
 * pdf envoyé directement vers edeal
 */
	
	public static String sendToEdeal(IChoixProspect choixProspect, RatingData ratingData, IEdealHelper iEdealHelper,String fonctionPublique,
			String nomFichier, byte[] pdf, String email) throws ServiceException {
		
		//suppression des options car plus de devis coté edeal
		
		ratingData.getLegalEntities()[0].getGBPContracts()[0].setOptions(new Option[0]);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = null;
		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		try {
			out.writeObject(ratingData);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		bytes = bos.toByteArray();
		try {
			String ret = iEdealHelper.createOpportuniteDevisSynchroIdentifieEtPDF(bytes, choixProspect
					.isEnvoi_courrier(), choixProspect.isAcceptation_email_offre_interiale(), choixProspect
					.isAcceptation_email_offre_partenaire(), fonctionPublique, choixProspect.getType_tarification().toString(),
					pdf, nomFichier, email);
			return ret;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Sauvegarde de l'Ã©tape 1 du devis.
	 * 
	 * @param params
	 * @param iEdealHelper
	 * @return
	 * @throws ServiceException
	 */
	public static String saveDevisEtapeUn(final Map<String, Object> params, final IEdealHelper iEdealHelper) throws ServiceException {
		byte[] bytes = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		try {
			out.writeObject(params);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		bytes = bos.toByteArray();
		
		try {
			final String retour = iEdealHelper.devisWebEtapeUn(bytes);
			log.debug("retour Edeal : " + retour);
			return retour;
		} catch (RemoteException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Sauvegarde de l'Ã©tape 2 du devis.
	 * 
	 * @param params
	 * @param iEdealHelper
	 * @return
	 * @throws ServiceException
	 */
	public static String saveDevisEtapeDeux(Map<String, Object> params,
			IEdealHelper iEdealHelper) throws ServiceException {
		byte[] bytes = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		try {
			out.writeObject(params);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		bytes = bos.toByteArray();
		
		try {
			final String retour = iEdealHelper.devisWebEtapeDeux(bytes);
			log.debug("retour Edeal : " + retour);
			return retour;
		} catch (RemoteException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Sauvegarde de l'Ã©tape 3 du devis.
	 * 
	 * @param params
	 * @param iEdealHelper
	 * @return
	 * @throws ServiceException
	 */
	public static String saveDevisEtapeTrois(Map<String, Object> params,
			IEdealHelper iEdealHelper) throws ServiceException {
		byte[] bytes = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		try {
			out.writeObject(params);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		bytes = bos.toByteArray();
		
		try {
			final String retour = iEdealHelper.devisWebEtapeTrois(bytes);
			log.debug("retour Edeal : " + retour);
			return retour;
		} catch (RemoteException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Sauvegarde de l'Ã©tape 4 du devis.
	 * 
	 * @param choixProspect
	 * @param ratingData
	 * @param iEdealHelper
	 * @param fonctionPublique
	 * @param nomFichier
	 * @param pdf
	 * @param email
	 * @return
	 * @throws ServiceException
	 */
	public static String saveDevisEtapeQuatre(IChoixProspect choixProspect, RatingData ratingData, IEdealHelper iEdealHelper,String fonctionPublique,
			String nomFichier, byte[] pdf, String email, final String identifiantDevis) throws ServiceException {

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("fonctionPublique", fonctionPublique);
		params.put("typeTarification", choixProspect.getType_tarification().toString());
		params.put("pdf", pdf);
		params.put("nomFichier", nomFichier);
		params.put("email", email);

		if (choixProspect instanceof ChoixProspectLabellise) {
			final choix_prevoyance prevoyance = ((ChoixProspectLabellise) choixProspect).getPrevoyance();
			if (prevoyance != null) {
				switch (prevoyance) {
					case NIV1:
						params.put("niveau_prev", "1");
						break;
					case NIV2:
						params.put("niveau_prev", "2");
						break;
					case NIV3:
						params.put("niveau_prev", "3");
						break;
					case NIV4:
						params.put("niveau_prev", "4");
						break;
						default:break;
				}
			}
		}
		if (choixProspect instanceof ChoixProspectMINJU) {
			final choix_prevoyance prevoyance = ((ChoixProspectMINJU) choixProspect).getPrevoyance();
			if (prevoyance != null) {
				switch (prevoyance) {
					case NIV1:
						params.put("niveau_prev", "1");
						break;
					case NIV2:
						params.put("niveau_prev", "2");
						break;
					case NIV3:
						params.put("niveau_prev", "3");
						break;
					case NIV4:
						params.put("niveau_prev", "4");
						break;
						default:break;
				}
			}
		}
		
		params.put("Dv_ID", identifiantDevis);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = null;
		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		try {
			out.writeObject(params);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		bytes = bos.toByteArray();
		try {
			final String ret = iEdealHelper.devisWebEtapeQuatre(bytes);
			return ret;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		
	}

	public static String saveDevisEtapeSante(Map<String, Object> params,
			IEdealHelper iEdealHelper) throws ServiceException {
		byte[] bytes = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		try {
			out.writeObject(params);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		bytes = bos.toByteArray();
		
		try {
			final String retour = iEdealHelper.devisWebEtapeSante(bytes);
			log.debug("retour Edeal : " + retour);
			return retour;
		} catch (RemoteException e) {
			throw new ServiceException(e);
		}
	}

}
