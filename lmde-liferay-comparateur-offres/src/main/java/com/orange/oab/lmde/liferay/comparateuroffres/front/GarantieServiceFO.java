package com.orange.oab.lmde.liferay.comparateuroffres.front;

import java.io.Serializable;

/**
 * @author Florian FRANCHETEAU (flmc8433) <florian.francheteau@orange.com>
 * @author Quentin LE ROUX (kjlh7728) <qleroux.ext@orange.com>
 */
public class GarantieServiceFO implements Serializable {

    private static final long serialVersionUID = 3014161387874530140L;

    private String texte;

    private String aide;

    private int jauge;

    /**
     * @return the texte
     */
    public String getTexte() {
        return texte;
    }

    /**
     * @param texte the texte to set
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }

    /**
     * @return the jauge
     */
    public int getJauge() {
        return jauge;
    }

    /**
     * @param jauge the jauge to set
     */
    public void setJauge(int jauge) {
        this.jauge = jauge;
    }

    /**
     * @return the aide
     */
    public String getAide() {
        return aide;
    }

    /**
     * @param aide the aide to set
     */
    public void setAide(String aide) {
        this.aide = aide;
    }

}
