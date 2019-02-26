package fr.interiale.ws.prospect.service;

import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import fr.interiale.service.utils.ws.InterialeRestWebServiceUtil;
import fr.interiale.ws.prospect.exception.IteWebServiceParrainageException;

/**
 * Classe Service gestion des offres.
 * @author adil.hasnaoui
 *
 */
public class GestionParrainageService {

	

	static {
		InterialeRestWebServiceUtil.initialize();
	}

	/**
	 * Chemin du web service gestion des codes parrains
	 * <p>
	 * Voir portal-ext.properties
	 */
	public static final String GESTION_PARRAINAGE_WS_LOCATION = "http://srvintpoa01rec:7003/api/v1/parrainage/codeparrainage/{code}/check/MIN1";//GetterUtil.getString(PropsUtil.get("ite.ws.ApiGestionProduit.url"));

	/** */
	public static final String GESTION_PARRAINAGE_WS_USE="http://srvintpoa01rec:7003/api/v1/parrainage/codeparrainage/{code}/usage/{bpu}";
	
	/**
	 * Permet de vérifier la validité d'un code parrain
	 * @param codeParrain
	 * @return
	 * @throws IteWebServiceParrainageException
	 */
	public static String checkCodeParrainage( String codeParrain) throws IteWebServiceParrainageException {
		
		String retVal="";
		
		try {
			
			String urlWS = GESTION_PARRAINAGE_WS_LOCATION.replace("{code}", codeParrain);
			final Map<String, String> headers = new HashMap<String, String>();
			
			headers.put("codeApplication", "webITE");
			headers.put("mutuelleGestion", "ITE");
			headers.put("typeApplication", "WEB");
			headers.put("streamPath", "TODO");
			headers.put("Content-Type", "application/json; charset=UTF-8");
			headers.put("accept", "application/json");
			
			final HttpResponse<String> jsonResponse = Unirest
					.get(urlWS)
					.headers(headers)//InterialeRestHeaderInterceptor.getRestHeaders())
					.asString();
			retVal = String.valueOf(jsonResponse.getStatus());
			

		} catch (final Exception e) {
			throw new IteWebServiceParrainageException(e);
		}
		
		return retVal;
		
	}
	
	
	/**
	 * Permet de vérifier la validité d'un code parrain
	 * @param codeParrain
	 * @return
	 * @throws IteWebServiceParrainageException
	 */
	public static String useCodeParrainage( String codeParrain, String bpu) throws IteWebServiceParrainageException {
		
		String retVal="";
		
		try {
			
			String urlWS = GESTION_PARRAINAGE_WS_USE.replace("{code}", codeParrain).replace("{bpu}", bpu);
			
			final Map<String, String> headers = new HashMap<String, String>();
			
			headers.put("codeApplication", "webITE");
			headers.put("mutuelleGestion", "ITE");
			headers.put("typeApplication", "WEB");
			headers.put("streamPath", "TODO");
			headers.put("Content-Type", "application/json; charset=UTF-8");
			headers.put("accept", "application/json");
			
			final HttpResponse<String> jsonResponse = Unirest
					.get(urlWS)
					.headers(headers)//InterialeRestHeaderInterceptor.getRestHeaders())
					.asString();
			retVal = String.valueOf(jsonResponse.getStatus());
			

		} catch (final Exception e) {
			throw new IteWebServiceParrainageException(e);
		}
		
		return retVal;
		
	}
	
}
