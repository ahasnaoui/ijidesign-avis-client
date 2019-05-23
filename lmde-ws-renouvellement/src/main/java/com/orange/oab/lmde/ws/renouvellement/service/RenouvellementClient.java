package com.orange.oab.lmde.ws.renouvellement.service;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orange.oab.lmde.util.cache.CacheKey;
import com.orange.oab.lmde.util.cache.CacheValue;
import com.orange.oab.lmde.util.cache.enums.ContratCacheKeyNameEnum;
import com.orange.oab.lmde.util.ws.LmdeRestWebServiceUtil;
import com.orange.oab.lmde.util.ws.interceptor.LmdeRestHeaderInterceptor;
import com.orange.oab.lmde.ws.renouvellement.exception.LmdeWebServiceRenouvellementException;

import fr.interiale.renouvellement.entite.Renouvellement;

/**
 * 
 * @author cckc3018
 *
 */
public class RenouvellementClient {
	
	private static final Logger LOGGER = Logger.getLogger(RenouvellementClient.class);
	
	static {
		LmdeRestWebServiceUtil.initialize();
	}
	
	/**
     * Cache des réponses du WS Personne - Consulter personne
     */
    private static PortalCache<CacheKey, CacheValue<Renouvellement>> CONSULTER_RENOUVELLEMENT_WS_CACHE = MultiVMPoolUtil
            .getCache(Renouvellement.class.getName());
	
	/**
     * Chemin du web service
     * <p>
     * Voir portal-ext.properties
     */
    public static final String RENOUVELLEMENT_WS_LOCATION = GetterUtil.getString(PropsUtil.get("lmde.ws.ApiRenouvellement.url"));

    public static final String RENOUVELLEMENT_WS_RENOUV_ADHERENT_URL_PATH = GetterUtil.getString(PropsUtil.get("lmde.ws.ApiRenouvellement.renouvellement.adherent.path"));
    
//    public static final String RENOUVELLEMENT_WS_LOCATION = "http://srvgiscon01rec:8080/refRenouvellementLMDE-rest-1.0.0-SNAPSHOT";

//    public static final String RENOUVELLEMENT_WS_RENOUV_ADHERENT_URL_PATH = "/api/v1/renouvellements/";
    
    /**
     * 
     * @param numeroAdherent
     * @return
     * @throws LmdeWebServiceRenouvellementException
     */
    public static Renouvellement getRenouvellement(final String numeroAdherent) throws LmdeWebServiceRenouvellementException {
    	Renouvellement renouvellement = null;
		
		if (numeroAdherent == null || numeroAdherent.isEmpty()) {
			// ERREUR
			LOGGER.error("Erreur lors de l'appel du WS getRenouvellement : Le numéro d'adherent est obligatoire.");
            throw new LmdeWebServiceRenouvellementException("Erreur lors de l'appel du WS getRenouvellement : Le numéro d'adherent est obligatoire.");
		}
		
		try {
			renouvellement = new Renouvellement();
			
			final HttpResponse<String> jsonResponse = Unirest.get(RENOUVELLEMENT_WS_LOCATION + RENOUVELLEMENT_WS_RENOUV_ADHERENT_URL_PATH + numeroAdherent)
					  .headers(LmdeRestHeaderInterceptor.getRestHeaders() /*getRestHeaders()*/)
					  .asString();
			
			if (jsonResponse.getStatus() == 200) {
				// Si code de retour = 200, cela signifie que le web service a répondu correctement
				
				final Gson gson = new Gson(); 
				renouvellement = gson.fromJson(jsonResponse.getBody(), Renouvellement.class);
				
				if (numeroAdherent != null && !numeroAdherent.equals(renouvellement.getNumAdherent())) {
					// ERREUR
					LOGGER.error("Erreur lors de l'appel du WS getRenouvellement : Retour du WS : " + jsonResponse.getStatus() + " numeroAdherent: " + numeroAdherent + " - Les numeros d'adherent sont differents.");
	                throw new LmdeWebServiceRenouvellementException("Erreur lors de l'appel du WS getRenouvellement pour le numero adherent: " + numeroAdherent + " - Les numeros d'adherent sont differents.");
				}
				
			} else {
				LOGGER.warn("Erreur lors de l'appel du WS getRenouvellement : Retour du WS : " + jsonResponse.getStatus() + " numeroAdherent: " + numeroAdherent);
				return null;
			}
		
		} catch (final UnirestException e) {
			LOGGER.error(e.getMessage(), e);
			throw new LmdeWebServiceRenouvellementException(e);
		}
		
		return renouvellement;
    }
    
    // ///////////////////////////////////////////////////////////
    //
    // Méthodes de recherche (utilisant le cache)
    //
    // //////////////////////////////////////////////////////////

    /**
     * Retourne les informations d'une personne.
     * Si présent dans le cache alors on récupère directement les informations du cache
     * Sinon on récupère les informations du Web Service
     * 
     * @param idPersonne l'identifiant Intériale de la personne
     * @return la réponse du Web Service ou du Cache
     * @throws LmdeWebServicePersonneException
     */
    public static Renouvellement getRenouvellementAvecCache(final String numeroAdherent) throws LmdeWebServiceRenouvellementException {
        Renouvellement wsResponse = null;

        if (numeroAdherent != null) {
            try {
                // Création de la clé unique
                CacheKey key = new CacheKey(ContratCacheKeyNameEnum.NUMERO_ADHERENT.toString(), numeroAdherent);

                // vérifie si l'information est présente dans le cache
                CacheValue<Renouvellement> wsCacheResponse = CONSULTER_RENOUVELLEMENT_WS_CACHE.get(key);

                if (Validator.isNotNull(wsCacheResponse)) {
                    wsResponse = wsCacheResponse.getWsResponse();
                } else {
                    // La cache ne contient pas d'informations, on appelle le ws
                    wsResponse = getRenouvellement(numeroAdherent);
                    // On met la réponse dans le cache
                    if (Validator.isNotNull(wsResponse)) {
                    	CONSULTER_RENOUVELLEMENT_WS_CACHE.put(key, new CacheValue<>(wsResponse));
                    }
                }
            } catch (Exception e) {
                throw new LmdeWebServiceRenouvellementException(e.getMessage());
            }
        }

        final StringBuilder log = new StringBuilder();
        log.append("getRenouvellementAvecCache [numeroAdherent:").append(numeroAdherent).append("] => renouvellement trouvé ").append(wsResponse != null);
        LOGGER.debug(log.toString());

        return wsResponse;
    }
    
    /**
     * 
     * @param renouvellement
     * @return
     * @throws LmdeWebServiceRenouvellementException
     */
    public static Renouvellement enregistrerRenouvellement(final Renouvellement renouvellement) throws LmdeWebServiceRenouvellementException {
    	Renouvellement renouvellementSaved = null;
    	if (renouvellement == null) {
    		LOGGER.error("Object renouvellement is null");
    		throw new LmdeWebServiceRenouvellementException("Object renouvellement is null");
    	}
    	if (renouvellement.getNumAdherent() == null || renouvellement.getNumAdherent().isEmpty()) {
    		LOGGER.error("numAdherent is null");
    		throw new LmdeWebServiceRenouvellementException("numAdherent is null");
    	}
    	
    	try {
    		// Conversion de la map contenant les paramètres en json
    		final Gson gson = new Gson(); 
    		final String json = gson.toJson(renouvellement); 
    		
    		final String numAdherent = renouvellement.getNumAdherent();
    		
    		final HttpResponse<Renouvellement> jsonResponse = Unirest.post(RENOUVELLEMENT_WS_LOCATION + RENOUVELLEMENT_WS_RENOUV_ADHERENT_URL_PATH + numAdherent)
					  .headers(LmdeRestHeaderInterceptor.getRestHeaders() /*getRestHeaders()*/)
					  .body(json)
					  .asObject(Renouvellement.class);
			
			if (jsonResponse.getStatus() == 200) {
				// Si code de retour = 200, cela signifie que le fichier a été généré
				renouvellementSaved = jsonResponse.getBody();
			} else {
				// ERREUR
				LOGGER.error("");
              throw new LmdeWebServiceRenouvellementException("");
			}
    		
    	} catch (UnirestException e) {
			LOGGER.error(e.getMessage(), e);
			throw new LmdeWebServiceRenouvellementException(e);
		}
    	
    	return renouvellementSaved;
    }
    
//    public static void main(String[] args) {
//    	try {
//			Renouvellement r = getRenouvellement("5678616");
//			//r.setMandatSepa(null);
//			r.setModePaiementActuel("PREL");
//			Renouvellement r1 = enregistrerRenouvellement(r);
//			System.out.println("PREL".equalsIgnoreCase(r1.getModePaiementActuel()));
//		} catch (LmdeWebServiceRenouvellementException e) {
//			e.printStackTrace();
//		}
//    }
//    
//    public static Map<String, String> getRestHeaders() {
//		final Map<String, String> headers = new HashMap<String, String>();
//		headers.put("codeApplication", "webLMDE");
//		headers.put("mutuelleGestion", "LMDE");
//		headers.put("typeApplication", "WEB");
//		headers.put("streamPath", "");
//		headers.put("Content-Type", "application/json; charset=UTF-8");
//		headers.put("accept", "application/json");
//		
//		return headers;
//	}
    
    // ///////////////////////////////////////////////////////////
    //
    // Invalidation des caches
    //
    // //////////////////////////////////////////////////////////

    /**
     * @param idPersonne
     * @param bpu
     */
    public static void invalidateRenouvellement(final String numeroAdherent) {
        if (Validator.isNotNull(numeroAdherent)) {
            LOGGER.debug("Invalidation du cache du WS Renouvellement pour l'utilisateur [numeroAdherent=" + numeroAdherent + "]");
            final CacheKey keyNumeroAdherent = new CacheKey(ContratCacheKeyNameEnum.NUMERO_ADHERENT.toString(), numeroAdherent);
            CONSULTER_RENOUVELLEMENT_WS_CACHE.remove(keyNumeroAdherent);
        }
    }
    
}
