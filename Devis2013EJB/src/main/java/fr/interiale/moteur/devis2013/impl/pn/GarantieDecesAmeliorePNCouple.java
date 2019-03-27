package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class GarantieDecesAmeliorePNCouple implements Serializable{

	
	private static final long serialVersionUID = 8776457732911365223L;

	// NSA added
	private Map<Integer, BigDecimal> cotisationsAdh;
	private Map<Integer, BigDecimal> cotisationsCjt;
	
	public GarantieDecesAmeliorePNCouple(GarantieDecesAmeliorePN garantieDecesAmeliorePNAdherent,
			GarantieDecesAmeliorePN garantieDecesAmeliorePNConjoint) {
		super();
		this.garantieDecesAmeliorePNAdherent = garantieDecesAmeliorePNAdherent;
		this.garantieDecesAmeliorePNConjoint = garantieDecesAmeliorePNConjoint;
	}

	public GarantieDecesAmeliorePNCouple() {
		
	}
	
	// NSA added
	public Map<Integer, BigDecimal> getCotisationsAdh() {
		this.cotisationsAdh = new TreeMap<Integer, BigDecimal>();
		for (Float seuil : this.garantieDecesAmeliorePNAdherent.getSeuils()) {
			this.cotisationsAdh.put(seuil.intValue(), this.garantieDecesAmeliorePNAdherent.getCotisation(seuil));
		}
		return this.cotisationsAdh;
	}
	
	// NSA added
	public Map<Integer, BigDecimal> getCotisationsCjt() {
		this.cotisationsCjt = new TreeMap<Integer, BigDecimal>();
		for (Float seuil : this.garantieDecesAmeliorePNConjoint.getSeuils()) {
			this.cotisationsCjt.put(seuil.intValue(), this.garantieDecesAmeliorePNConjoint.getCotisation(seuil));
		}
		return this.cotisationsCjt;
	}
	
	private GarantieDecesAmeliorePN garantieDecesAmeliorePNAdherent;
	
	private GarantieDecesAmeliorePN garantieDecesAmeliorePNConjoint;

	public GarantieDecesAmeliorePN getGarantieDecesAmeliorePNAdherent() {
		return garantieDecesAmeliorePNAdherent;
	}

	public void setGarantieDecesAmeliorePNAdherent(GarantieDecesAmeliorePN garantieDecesAmeliorePNAdherent) {
		this.garantieDecesAmeliorePNAdherent = garantieDecesAmeliorePNAdherent;
	}

	public GarantieDecesAmeliorePN getGarantieDecesAmeliorePNConjoint() {
		return garantieDecesAmeliorePNConjoint;
	}

	public void setGarantieDecesAmeliorePNConjoint(GarantieDecesAmeliorePN garantieDecesAmeliorePNConjoint) {
		this.garantieDecesAmeliorePNConjoint = garantieDecesAmeliorePNConjoint;
	}
	
	// NSA added
	public boolean isSelect() {
		return this.garantieDecesAmeliorePNAdherent.isSelect() || this.garantieDecesAmeliorePNConjoint.isSelect();
	}

	@Override
	public String toString() {
		return "GarantieDecesAmeliorePNCouple "+getCode()+" [garantieDecesAmeliorePNAdherent=" + garantieDecesAmeliorePNAdherent
				+ ", garantieDecesAmeliorePNConjoint=" + garantieDecesAmeliorePNConjoint + "]";
	}
	public String getCode(){
		return "[A "+garantieDecesAmeliorePNAdherent.getCode()+"]+[C "+garantieDecesAmeliorePNConjoint.getCode()+"]";
	}
	
	
}
