package fr.interiale.ws.gestionpdf.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import fr.interiale.service.utils.ws.InterialeClientWebServiceUtil;
import fr.interiale.service.utils.ws.InterialeRestWebServiceUtil;
import fr.interiale.ws.gestionpdf.exception.InterialeWebServiceGestionPDFException;

/**
 * Client WS de telechargement des attestations
 * @author adil.hasnaoui
 *
 */
public class AttestationPDFClient {
	
	private static final Logger LOGGER = Logger.getLogger(AttestationPDFClient.class);
	
	static {
		InterialeRestWebServiceUtil.initialize();
	}
	
	/**
     * Chemin du web service
     * <p>
     * Voir portal-ext.properties
     */
    public static final String GESTION_PDF_WS_LOCATION = GetterUtil.getString(PropsUtil.get("ite.ws.ApiAttestationPDF.url"));
    


	/**
	 * Génère le document pdfà partir des données data. Les données doivent être au format UTF-8.
	 * @param domaine
	 * @param formName
	 * @param data
	 * @return
	 * @throws InterialeWebServiceGestionPDFException
	 */
	public static InputStream getAttestationPDF(final String bpu, final String codeAttestation, String urlWS) throws InterialeWebServiceGestionPDFException {
		
		try {
			// Conversion de la map contenant les paramètres en json
			String urlWebService = BuildURLWS(bpu, codeAttestation, urlWS);
			final Map<String, String> headers = buildHeaders();
			
			HttpResponse<InputStream> jsonResponse = Unirest.get(urlWebService)
					  .headers(headers)
					  .asBinary();
			
			if (jsonResponse.getStatus() == 200) {
				// Si code de retour = 200, cela signifie que le fichier a été généré
				return jsonResponse.getBody();
			} else {
				// ERREUR
				LOGGER.error("Erreur lors de l'appel du WS get attestation : Retour du WS : " + jsonResponse.getStatus() + " token: " + bpu + ", code Attestation: " + codeAttestation );
                throw new InterialeWebServiceGestionPDFException("Erreur lors de l'appel du WS get Attestation pour le token: " + bpu + ", le form: " + codeAttestation);
			}
			
		} catch (UnirestException e) {
			LOGGER.error(e.getMessage(), e);
			throw new InterialeWebServiceGestionPDFException(e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new InterialeWebServiceGestionPDFException(e);
		}
		
	}



	/**
	 * @return
	 */
	private static Map<String, String> buildHeaders() {
		final Map<String, String> headers = new HashMap<String, String>();
		
		headers.put("codeApplication", InterialeClientWebServiceUtil.WEBSERVICE_METADONNEES_CODE_APPLICATION);
		headers.put("mutuelleGestion", InterialeClientWebServiceUtil.WEBSERVICE_METADONNEES_MUTUELLE_GESTION);
		headers.put("typeApplication", InterialeClientWebServiceUtil.WEBSERVICE_METADONNEES_TYPE_APPLICATION);
		headers.put("streamPath", InterialeClientWebServiceUtil.WEBSERVICE_METADONNEES_STREAM_PATH);
		headers.put("Content-Type", "application/pdf");
		headers.put("accept", "application/pdf");
		return headers;
	}



	/**
	 * @param bpu
	 * @param codeAttestation
	 * @param urlWS
	 * @return
	 */
	private static String BuildURLWS(final String bpu, final String codeAttestation, String urlWS) {
		StringBuilder urlWebService = new StringBuilder();
		if(urlWS != null && !urlWS.isEmpty()) {
			urlWebService.append(urlWS);
		} else {
			urlWebService.append(GESTION_PDF_WS_LOCATION);
		}
		urlWebService.append(bpu);
		urlWebService.append("/");
		urlWebService.append(codeAttestation);
		return urlWebService.toString();
	}

}

