package fr.interiale.moteur.devis2013.impl.commun;

import java.util.Date;

import javax.ejb.Remote;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.apache.axis.message.SOAPHeaderElement;
@Remote
public interface IWebServiceWS {
	

	public static final String postalAddressDefaultCountryCode = "FRA";
	public final static String ip = DevisProperties.getString("ws.wyde.ip.interiale");
	public static final String JNDIName = "WebServiceWS/remote";
	public abstract float getPointDIndice(Date dateAdhesionDate) throws ServiceException;
	public SOAPHeaderElement getAuthentication() throws SOAPException;
	public boolean testActivite();
	public void init();

}