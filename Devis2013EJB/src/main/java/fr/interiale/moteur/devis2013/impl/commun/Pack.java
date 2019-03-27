package fr.interiale.moteur.devis2013.impl.commun;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.interfaces.IPack;

public class Pack implements Serializable, IPack{

	private static final long serialVersionUID = -2177865760273148765L;
	private  BigDecimal  cotisation_base=BigDecimal.ZERO;
	private  BigDecimal  assistance=BigDecimal.ZERO;
	
	public Pack() {
		
	}
	

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.PackInterface#getCotisation()
	 */
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IPack#getCotisation()
	 */
	public BigDecimal getCotisation() {
		return cotisation_base.add(assistance);
	}

	public Pack(BigDecimal cotisation) {
		super();
		this.cotisation_base = cotisation;
	}

	@Override
	public String toString() {
		return " cotisation : "+getCotisation()+"[cotisation_base=" + cotisation_base + ", assistance=" + assistance + "]";
	}

	public BigDecimal getCotisation_base() {
		return cotisation_base;
	}

	public void setCotisation_base(BigDecimal cotisation_base) {
		this.cotisation_base = cotisation_base;
	}

	public BigDecimal getAssistance() {
		return assistance;
	}

	public void setAssistance(BigDecimal assistance) {
		this.assistance = assistance;
	}

	
}
