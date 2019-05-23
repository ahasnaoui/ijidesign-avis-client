package com.orange.oab.lmde.liferay.comparateuroffres.front;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GarantieFO implements Serializable {

    private static final long serialVersionUID = -4608287880471755965L;

    /** Liste des attributs utiles pour le comparateur d'offres */
    private String code;
    private String libelle;
    private String classeCSSPicto;
    private int jauge;
    private int position;
    private List<GarantieDetailFO> garantieDetails;

    public GarantieFO() {
        super();
    }

    public GarantieFO(String codeGarantie, String libelleGarantie) {
    	this.code = codeGarantie;
    	this.libelle = libelleGarantie;
        this.jauge = 0;
        this.position = 0;
        this.garantieDetails = new ArrayList<GarantieDetailFO>();
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getClasseCSSPicto() {
		return classeCSSPicto;
	}

	public void setClasseCSSPicto(String classeCSSPicto) {
		this.classeCSSPicto = classeCSSPicto;
	}

	public int getJauge() {
		return jauge;
	}

	public void setJauge(int jauge) {
		this.jauge = jauge;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<GarantieDetailFO> getGarantieDetails() {
		return garantieDetails;
	}

	public void setGarantieDetails(
			List<GarantieDetailFO> garantieDetails) {
		this.garantieDetails = garantieDetails;
	}

}
