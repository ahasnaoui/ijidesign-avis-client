package com.orange.oab.lmde.liferay.comparateuroffres.front;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.liferay.portal.kernel.util.StringPool;
import com.orange.oab.lmde.liferay.comparateuroffres.util.CodeGammeConstants;
import com.orange.oab.lmde.liferay.comparateuroffres.util.ComparateurOffresConstants;

/**
 * @author Florian FRANCHETEAU (flmc8433) <florian.francheteau@orange.com>
 * @author Quentin LE ROUX (kjlh7728) <qleroux.ext@orange.com>
 */
public class OffreFO implements Serializable, Comparable<OffreFO> {

    private static final long serialVersionUID = 5827301344341151640L;

    private String codeOffre;

    private String codeMillesime;

    private String codeGamme;

    private String libelle;

    private String description;

    private String tarifMois;

    private String tarifAnnee;

    private String lienDetails;

    private String lienDevis;

    private boolean devisObligatoire;

    private Map<String, GarantieFO> garanties = new HashMap<String, GarantieFO>();
    
    private Map<String, ServiceFO> services = new HashMap<String, ServiceFO>();

    public OffreFO() {
    }

    public OffreFO(String codeOffre, String codeMillesime) {
        super();
        this.codeOffre = codeOffre;
        this.codeMillesime = codeMillesime;
    }

    /**
     * @return the codeOffre
     */
    public String getCodeOffre() {
        return codeOffre;
    }

    /**
     * @param codeOffre the codeOffre to set
     */
    public void setCodeOffre(String codeOffre) {
        this.codeOffre = codeOffre;
    }

    /**
     * @return the codeMillesime
     */
    public String getCodeMillesime() {
        return codeMillesime;
    }

    /**
     * @param codeMillesime the codeMillesime to set
     */
    public void setCodeMillesime(String codeMillesime) {
        this.codeMillesime = codeMillesime;
    }

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @param libelle the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the codeGamme
     */
    public String getCodeGamme() {
        return codeGamme;
    }

    /**
     * @param codeGamme the codeGamme to set
     */
    public void setCodeGamme(String codeGamme) {
        this.codeGamme = codeGamme;
    }

    /**
     * @return the tarifMois
     */
    public String getTarifMois() {
        return tarifMois;
    }

    /**
     * @param tarifMois the tarifMois to set
     */
    public void setTarifMois(String tarifMois) {
        this.tarifMois = tarifMois;
    }

    /**
     * @return the tarifAnnee
     */
    public String getTarifAnnee() {
        return tarifAnnee;
    }

    /**
     * @param tarifAnnee the tarifAnnee to set
     */
    public void setTarifAnnee(String tarifAnnee) {
        this.tarifAnnee = tarifAnnee;
    }

    /**
     * @return the lienDetails
     */
    public String getLienDetails() {
        return lienDetails;
    }

    /**
     * @param lienDetails the lienDetails to set
     */
    public void setLienDetails(String lienDetails) {
        this.lienDetails = lienDetails;
    }

    /**
     * @return the garanties
     */
    public Map<String, GarantieFO> getGaranties() {
        return garanties;
    }

    /**
     * @param garanties the garanties to set
     */
    public void setGaranties(Map<String, GarantieFO> garanties) {
        this.garanties = garanties;
    }

    /**
     * @return the services
     */
    public Map<String, ServiceFO> getServices() {
		return services;
	}

    /**
     * @param services the services to set
     */
	public void setServices(Map<String, ServiceFO> services) {
		this.services = services;
	}

	public String getTarifMoisString() {
        if (tarifMois != null) {
            try {
                Float tarifMoisFloat = Float.parseFloat(tarifMois);
                return ComparateurOffresConstants.DECIMAL_FORMAT_FRANCE.format(tarifMoisFloat);
            } catch (NumberFormatException | NullPointerException e) {
                return StringPool.BLANK;
            }
        }
        return StringPool.BLANK;
    }

    public String getTarifAnneeString() {
        if (tarifAnnee != null) {
            try {
                Float tarifAnneeFloat = Float.parseFloat(tarifAnnee);
                return ComparateurOffresConstants.DECIMAL_FORMAT_FRANCE.format(tarifAnneeFloat);
            } catch (NumberFormatException | NullPointerException e) {
                return StringPool.BLANK;
            }
        }
        return StringPool.BLANK;
    }

    public Float getTarifMoisFloat() {
        if (tarifMois != null) {
            try {
                Float tarifMoisFloat = Float.parseFloat(tarifMois);
                return tarifMoisFloat;
            } catch (NumberFormatException | NullPointerException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Tri du moins cher au plus cher
     */
    public int compareTo(OffreFO offre) {
    	
    	if (this.getCodeGamme().equals(offre.getCodeGamme())) {
	        if (this.getTarifMoisFloat() != null && offre.getTarifMoisFloat() != null) {
	            try {
	                return this.getTarifMoisFloat().compareTo(offre.getTarifMoisFloat());
	            } catch (NumberFormatException e) {
	                return -1;
	            }
	        } else if (offre.getTarifMois() != null) {
	            return -1;
	        } else if (this.getTarifMois() != null) {
	            return 1;
	        } else {
	            return 0;
	        }
    	} else if (offre.getCodeGamme().equals(CodeGammeConstants.SURCOMPLEMENTAIRE)) {
    		return -1;
    	} else {
    		return 1;
    	}
    }

    /**
     * Implémentation de la méthode equals équivalente au compareTo (good practice)
     */
    public boolean equals(OffreFO offre) {
        return this.compareTo(offre) == 0;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OffreFO [codeOffre=" + codeOffre + ", codeMillesime=" + codeMillesime + ", codeGamme=" + codeGamme + ", libelle=" + libelle + ", description="
                + description + ", tarifMois=" + tarifMois + ", tarifAnnee=" + tarifAnnee + ", lienDetails=" + lienDetails + ", garanties="
                + garanties + "]";
    }

    public void addGaranties(Map<String, GarantieFO> garantiesFO) {
        garanties.putAll(garantiesFO);
    }

    public void addServices(Map<String, ServiceFO> servicesFO) {
        services.putAll(servicesFO);
    }

    /**
     * @return the lienDevis
     */
    public String getLienDevis() {
        return lienDevis;
    }

    /**
     * @param lienDevis the lienDevis to set
     */
    public void setLienDevis(String lienDevis) {
        this.lienDevis = lienDevis;
    }

    /**
     * @return the devisObligatoire
     */
    public boolean isDevisObligatoire() {
        return devisObligatoire;
    }

    /**
     * @param devisObligatoire the devisObligatoire to set
     */
    public void setDevisObligatoire(boolean devisObligatoire) {
        this.devisObligatoire = devisObligatoire;
    }

}
