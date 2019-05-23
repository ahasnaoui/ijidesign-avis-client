package com.orange.oab.lmde.liferay.comparateuroffres.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.orange.oab.lmde.liferay.comparateuroffres.form.OffreForm;
import com.orange.oab.lmde.liferay.comparateuroffres.front.GarantieDetailFO;
import com.orange.oab.lmde.liferay.comparateuroffres.front.GarantieFO;
import com.orange.oab.lmde.liferay.comparateuroffres.front.OffreFO;
import com.orange.oab.lmde.liferay.comparateuroffres.front.ServiceFO;
import com.orange.oab.lmde.liferay.comparateuroffres.util.CodeGammeConstants;
import com.orange.oab.lmde.liferay.comparateuroffres.util.ComparateurOffresConstants;
import com.orange.oab.lmde.service.bean.GammeBean;
import com.orange.oab.lmde.service.bean.GarantieComparateurBean;
import com.orange.oab.lmde.service.bean.GarantieComparateurDetailsBean;
import com.orange.oab.lmde.service.bean.OffreBean;
import com.orange.oab.lmde.service.bean.OffreWC;
import com.orange.oab.lmde.service.bean.ServiceComparateurBean;
import com.orange.oab.lmde.service.exception.LmdeServiceOffreException;
import com.orange.oab.lmde.service.service.AdhesionEtDevisServiceUtil;
import com.orange.oab.lmde.service.service.OffreServiceUtil;
import com.orange.oab.lmde.util.cache.CacheKey;
import com.orange.oab.lmde.util.portal.LmdePortalProperties;
import com.orange.oab.lmde.util.portal.LmdePortalUtil;

/**
 * Classe métier du portlet de comparateur d'offres
 * 
 * @author Florian FRANCHETEAU (flmc8433) <florian.francheteau@orange.com>
 * @author Quentin LE ROUX (kjlh7728) <qleroux.ext@orange.com>
 */
public class ComparateurOffresBusinessService {

    private static final Logger LOGGER = Logger.getLogger(ComparateurOffresBusinessService.class);
    
    /**
     * Le cache au niveau de toutes les sortes de beans contrats
     */
    private static PortalCache<CacheKey, OffreForm> COMPARATEUR_OFFRES_CACHE = MultiVMPoolUtil.getCache(OffreForm.class.getName());

    /**
     * Liste toutes les gammes avec le statut OUVERT
     * 
     * @return la liste des gammes ayant le statut OUVERT
     * @throws LmdeServiceOffreException
     */
    public List<GammeBean> listerGammes() throws LmdeServiceOffreException {
        LOGGER.trace("listerGammes.start");
        List<GammeBean> listeGammes = null;
        listeGammes = OffreServiceUtil.listerGamme(ComparateurOffresConstants.STATUT_GAMME);
        LOGGER.trace("listerGammes.end");
        return listeGammes;
    }

    /**
     * Récupère la gamme de l'offre et du millésime concerné
     * 
     * @param codeOffre le code de l'offre
     * @param codeMillesime le millésime de l'offre
     * @return le code de la gamme correspondant à l'offre
     * @throws LmdeServiceOffreException
     */
    public String getGammeFromOffre(final String codeOffre, final String codeMillesime) throws LmdeServiceOffreException {
        LOGGER.trace("getGammeFromOffre.start");
        String codegamme = null;
        OffreBean offre = OffreServiceUtil.consulterOffre(codeOffre, codeMillesime);
        if (Validator.isNotNull(offre)) {
            codegamme = offre.getGamme();
        }
        LOGGER.trace("getGammeFromOffre.end");
        return codegamme;
    }

    /**
     * Liste la liste des offres d'une gamme. Cette méthode supporte le code gamme custom COMP&SURCOMP, il s'agit d'un code custom pour récupérer les liste des
     * offres des deux gammes complémantaire et surcomplémentaire.
     * 
     * @param codeGamme le code de la gamme
     * @param themeDisplay
     * @param codeOffre le code de l'offre (optionnel). Sert à présélectionner une offre
     * @return
     * @throws LmdeServiceOffreException
     * @throws SystemException
     */
    public OffreForm listerOffresFromGamme(final String codeGamme, final ThemeDisplay themeDisplay, final String codeOffre) throws LmdeServiceOffreException,
            SystemException {
        LOGGER.trace("getOffresFromGamme.start");
        List<OffreBean> listeOffres = null;
        List<OffreFO> listeOffresCompletes = null;
        OffreForm resultat = null;//new OffreForm();
        
        // Création de la clé unique
        final CacheKey key = new CacheKey(codeGamme, codeGamme);
        
        // Récupération de la liste des beans Decomptes à partir du cache
       // resultat = COMPARATEUR_OFFRES_CACHE.get(key);
        
        if (Validator.isNotNull(resultat) && Validator.isNotNull(resultat.getOffre())) {
        	return resultat;
        }
		
		resultat = new OffreForm();

        if (CodeGammeConstants.COMPLEMENTAIRE_ET_SURCOMPLEMENTAIRE.equalsIgnoreCase(codeGamme)) {
            // On récupère les offres complémentaires ET les surcomplémentaires
            listeOffres = OffreServiceUtil.listerOffre(ComparateurOffresConstants.STATUT_GAMME, CodeGammeConstants.COMPLEMENTAIRE);
            listeOffres.addAll(OffreServiceUtil.listerOffre(ComparateurOffresConstants.STATUT_GAMME, CodeGammeConstants.SURCOMPLEMENTAIRE));
        } else {
            listeOffres = OffreServiceUtil.listerOffre(ComparateurOffresConstants.STATUT_GAMME, codeGamme);
        }
        
        if (Validator.isNotNull(listeOffres)) {

            listeOffresCompletes = new ArrayList<OffreFO>();
            List<OffreWC> listeOffreWebContent = AdhesionEtDevisServiceUtil.getOffreWebContentList(themeDisplay);
            for (OffreBean offre : listeOffres) {
                OffreFO offreResultat = new OffreFO(offre.getCodeOffre(), offre.getCodeMillesime());

                OffreBean offreComplete = OffreServiceUtil.consulterOffre(offre.getCodeOffre(), offre.getCodeMillesime());
                if (Validator.isNotNull(offreComplete)) {
                    offreResultat.setLibelle(offreComplete.getLibelleOffre());
                }
                

                for (OffreWC offreWC : listeOffreWebContent) {

                    if (offre.getCodeOffre().equals(offreWC.getCodeOffre()) && offre.getCodeMillesime().equals(offreWC.getCodeMillesime())) {
                        offreResultat.setDescription(offreWC.getDescriptionComparateur());
                        offreResultat.setTarifMois(offreWC.getTarifMois());
                        offreResultat.setTarifAnnee(offreWC.getTarifAnnee());
                        offreResultat.setLienDetails(offreWC.getLienDetails());
                        offreResultat.setDevisObligatoire(offreWC.isDevisObligatoire());
                        offreResultat.setLienDevis(offreWC.getLienDevis());

                        if (offreWC.getGarantiesComparateur() != null && !offreWC.getGarantiesComparateur().isEmpty()) {
                            Map<String, GarantieFO> garantiesFO = new HashMap<String, GarantieFO>();

                            for (GarantieComparateurBean garantie : offreWC.getGarantiesComparateur()) {
                                final GarantieFO garantieFO = new GarantieFO();
                                garantieFO.setCode(garantie.getCodeGarantie());
                                garantieFO.setLibelle(garantie.getLibelleGarantie());
                                garantieFO.setJauge(garantie.getJauge());
                                
                                if (garantie.getGarantieDetails() != null && !garantie.getGarantieDetails().isEmpty()) {
                                	final List<GarantieDetailFO> detailsFO = new ArrayList<GarantieDetailFO>();
	                                for (final GarantieComparateurDetailsBean detail : garantie.getGarantieDetails()) {
	                                	final String titre = detail.getTitre();
	                                	final String textePriseEnCharge = detail.getTextePriseEnCharge();
	                                	final String textePriseEnChargeAide = detail.getTextePriseEnChargeAide();
	                                	final int position = detail.getPosition();
	                                	final GarantieDetailFO detailFO = new GarantieDetailFO(titre, textePriseEnCharge, textePriseEnChargeAide, position);
	                                	detailsFO.add(detailFO);
	                                }
	                                garantieFO.setGarantieDetails(detailsFO);
                                }
                                
                                garantiesFO.put(garantie.getCodeGarantie(), garantieFO);
                            }

                            offreResultat.addGaranties(garantiesFO);
                        }
                        if (offreWC.getServicesComparateur() != null && offreWC.getServicesComparateur().size() > 0) {
                            Map<String, ServiceFO> servicesFO = new HashMap<String, ServiceFO>();

                            for (ServiceComparateurBean service : offreWC.getServicesComparateur()) {
                                ServiceFO serviceFO = new ServiceFO();
                                serviceFO.setTextePriseEnCharge(service.getTextePriseEnCharge());
                                serviceFO.setTextePriseEnChargeAide(service.getTextePriseEnChargeAide());
                                serviceFO.setClasseCSSPicto(service.getClasseCSSPicto());
                                serviceFO.setJauge(service.getJauge());
                                serviceFO.setCode(service.getCode());
                                servicesFO.put(service.getCode(), serviceFO);
                            }
                            offreResultat.addServices(servicesFO);
                        }
                        break;
                    }
                }
                offreResultat.setCodeGamme(offreComplete.getGamme());
                listeOffresCompletes.add(offreResultat);

            }
            Collections.sort(listeOffresCompletes);
            resultat.setOffre(listeOffresCompletes);
            resultat.setLienGammeDetail(getDefaultLienDocumentOffre(themeDisplay, codeGamme));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Liste des offres de la gamme " + codeGamme + " : ");
                for (OffreFO offre : listeOffresCompletes) {
                    LOGGER.debug(offre.toString());

                }
            }
        }
        
        // Mise en cache
        COMPARATEUR_OFFRES_CACHE.put(key, resultat);
 
        LOGGER.trace("getOffresFromGamme.end");
        return resultat;
    }

    /**
     * Renvoie l'URL du document de détails des offres par défaut
     * 
     * @param themeDisplay
     * @return document URL
     */
    private String getDefaultLienDocumentOffre(final ThemeDisplay themeDisplay, String codeGamme) {

        String documentURL = StringPool.BLANK;
        try {
            final DLFolder folder = DLFolderLocalServiceUtil.getFolder(LmdePortalUtil.getLmdeGroupId(), 0,
                    LmdePortalProperties.LMDE_OFFRE_DETAILS_DOCUMENT_FOLDER_NAME);
            DLFileEntry fileEntry = null;
            if (codeGamme != null && CodeGammeConstants.COMPLEMENTAIRE.equalsIgnoreCase(codeGamme)
                    && !LmdePortalProperties.LMDE_OFFRE_DETAILS_DOCUMENT_NAME.isEmpty()) {
                fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(LmdePortalUtil.getLmdeGroupId(), folder.getFolderId(),
                        LmdePortalProperties.LMDE_OFFRE_DETAILS_DOCUMENT_NAME);
            } else if (codeGamme != null && CodeGammeConstants.SURCOMPLEMENTAIRE.equalsIgnoreCase(codeGamme)
                    && !LmdePortalProperties.LMDE_OFFRE_DETAILS_SURCOMPLEMENTAIRE_DOCUMENT_NAME.isEmpty()) {
                fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(LmdePortalUtil.getLmdeGroupId(), folder.getFolderId(),
                        LmdePortalProperties.LMDE_OFFRE_DETAILS_SURCOMPLEMENTAIRE_DOCUMENT_NAME);
            } else if (codeGamme != null && CodeGammeConstants.ASSURANCE.equalsIgnoreCase(codeGamme)
                    && !LmdePortalProperties.LMDE_OFFRE_DETAILS_ASSURANCE_DOCUMENT_NAME.isEmpty()) {
                fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(LmdePortalUtil.getLmdeGroupId(), folder.getFolderId(),
                        LmdePortalProperties.LMDE_OFFRE_DETAILS_ASSURANCE_DOCUMENT_NAME);
            }
            if (fileEntry != null) {
                DLFileEntry fileEntryEscaped = fileEntry.toEscapedModel();
                documentURL = themeDisplay.getPortalURL() + themeDisplay.getPathContext() + "/documents/" + themeDisplay.getScopeGroupId() + "//"
                        + fileEntryEscaped.getFolderId() + "//" + HttpUtil.encodeURL(HtmlUtil.unescape(fileEntryEscaped.getTitle()));
            }
        } catch (PortalException | SystemException e) {
            LOGGER.error("Le document par défaut de détails des offres n'a pas été trouvé " + LmdePortalProperties.LMDE_OFFRE_DETAILS_DOCUMENT_FOLDER_NAME
                    + "/" + LmdePortalProperties.LMDE_OFFRE_DETAILS_DOCUMENT_NAME);
        }
        return documentURL;
    }

    /**
     * Invalide le cache.
     */
	public void invalidateCache() {
		COMPARATEUR_OFFRES_CACHE.removeAll();		
	}
}
