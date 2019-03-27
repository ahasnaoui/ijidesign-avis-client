package fr.interiale.ws.prospect.service;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import fr.interiale.service.utils.ws.InterialeRestWebServiceUtil;
import fr.interiale.ws.prospect.entite.ParrainageBean;
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
	public static final String GESTION_PARRAINAGE_WS_LOCATION = GetterUtil.getString(PropsUtil.get("ite.ws.ApiGestionParrainage.check.url"));

	/** */
	public static final String GESTION_PARRAINAGE_WS_USE=GetterUtil.getString(PropsUtil.get("ite.ws.ApiGestionParrainage.usage.url"));
	
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
			ParrainageBean parrain = new ParrainageBean();
			parrain.setActeurEdeal("");
			parrain.setBpuFilleul(bpu);
			parrain.setCanalUsage("WEB");
			parrain.setCodesContrat("MIN1");
			// Conversion de la map contenant les paramÃ¨tres en json
			Gson gson = new Gson();
			final String json = gson.toJson(parrain);
			String urlWS = GESTION_PARRAINAGE_WS_USE.replace("{code}", codeParrain).replace("{bpu}", bpu);
			
			final Map<String, String> headers = new HashMap<String, String>();
			
			headers.put("codeApplication", "webITE");
			headers.put("mutuelleGestion", "ITE");
			headers.put("typeApplication", "WEB");
			headers.put("streamPath", "TODO");
			headers.put("Content-Type", "application/json; charset=UTF-8");
			headers.put("accept", "application/json");
			
			final HttpResponse<String> jsonResponse = Unirest
					.post(urlWS)
					.headers(headers).body(json)//InterialeRestHeaderInterceptor.getRestHeaders())
					.asString();
			retVal = String.valueOf(jsonResponse.getStatus());
			

		} catch (final Exception e) {
			throw new IteWebServiceParrainageException(e);
	}
		
		return retVal;
		
	}
	
}
