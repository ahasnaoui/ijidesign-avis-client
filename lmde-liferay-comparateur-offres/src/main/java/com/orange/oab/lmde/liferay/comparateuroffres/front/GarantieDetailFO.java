package com.orange.oab.lmde.liferay.comparateuroffres.front;

import java.io.Serializable;

public class GarantieDetailFO implements Serializable {

	private static final long serialVersionUID = -5413946602461427870L;

	private String titre;
	private String textePriseEnCharge;
	private String textePriseEnChargeAide;
	private int position;
	
	public GarantieDetailFO(String titre, String textePriseEnCharge, String textePriseEnChargeAide, int position) {
		super();
		this.titre = titre;
		this.textePriseEnCharge = textePriseEnCharge;
		this.textePriseEnChargeAide = textePriseEnChargeAide;
		this.position = position;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
}
