package fr.interiale.moteur.devis2013.impl.interiale;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.interfaces.IGarantieCouverturePrime;

public class GarantieCouverturePrime implements Serializable, IGarantieCouverturePrime {


	private static final long serialVersionUID = 8643237939401728959L;
	private   BigDecimal cotisation=BigDecimal.ZERO;

		
	public GarantieCouverturePrime(BigDecimal cotisation) {
		super();
		this.cotisation = cotisation;
	}

	public GarantieCouverturePrime() {
		
	}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IGarantieCouverturePrime#getCotisation()
	 */
	public BigDecimal getCotisation() {
		return cotisation;
	}


	public void setCotisation(BigDecimal cotisation) {
		this.cotisation = cotisation;
	}

	@Override
	public String toString() {
		return "GarantieCouverturePrime [cotisation=" + cotisation + "]";
	}
		
}
