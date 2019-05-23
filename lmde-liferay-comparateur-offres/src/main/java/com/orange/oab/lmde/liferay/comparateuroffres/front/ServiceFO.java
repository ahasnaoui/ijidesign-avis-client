package com.orange.oab.lmde.liferay.comparateuroffres.front;

import java.io.Serializable;

public class ServiceFO implements Serializable {

	private static final long serialVersionUID = 4714187383911691618L;
	
	private String code;
    private String textePriseEnCharge;
    private String textePriseEnChargeAide;
    private String classeCSSPicto;
    private int jauge;
    
	public ServiceFO() {
		super();
	}

	public ServiceFO(String code, String textePriseEnCharge,
			String textePriseEnChargeAide, String classeCSSPicto, int jauge) {
		super();
		this.code = code;
		this.textePriseEnCharge = textePriseEnCharge;
		this.textePriseEnChargeAide = textePriseEnChargeAide;
		this.classeCSSPicto = classeCSSPicto;
		this.jauge = jauge;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTextePriseEnCharge() {
		return textePriseEnCharge;
	}

	public void setTextePriseEnCharge(String textePriseEnCharge) {
		this.textePriseEnCharge = textePriseEnCharge;
	}

	public String getTextePriseEnChargeAide() {
		return textePriseEnChargeAide;
	}

	public void setTextePriseEnChargeAide(String textePriseEnChargeAide) {
		this.textePriseEnChargeAide = textePriseEnChargeAide;
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
    
}
