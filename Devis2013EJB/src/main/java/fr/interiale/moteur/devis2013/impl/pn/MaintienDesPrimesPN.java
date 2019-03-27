package fr.interiale.moteur.devis2013.impl.pn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.interiale.moteur.devis2013.impl.commun.WebServiceWS;

public class MaintienDesPrimesPN implements Serializable,ICotisation {

	private static final long serialVersionUID = 8932687429267768729L;
	private Map<Float , BigDecimal> cotisations=new HashMap<Float, BigDecimal>(1);
	private boolean select=false;
	
	private Set<String> code=new HashSet<String>();
	
	public void setCotisation(Float prime, BigDecimal cotisation ){
		select=true;
		cotisations.put(prime, cotisation);
	}

	public BigDecimal getCotisation() {
		return (BigDecimal) cotisations.values().toArray()[0];
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
				for (Float f : cotisations.keySet()) {
					stringBuilder.append(" "+f+" "+cotisations.get(f));
				} 
		return "MaintienDesPrimesPN "+getCode()+"[cotisations=" + stringBuilder.toString() + "]";
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public String getCode() {
		return WebServiceWS.getCode(code);
	}

	public void addCode(String code) {
		this.code.add(code);
	}
}

