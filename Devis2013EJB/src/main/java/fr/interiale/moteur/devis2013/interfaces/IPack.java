package fr.interiale.moteur.devis2013.interfaces;

import java.math.BigDecimal;

public interface IPack {

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.PackInterface#getCotisation()
	 */
	public abstract BigDecimal getCotisation();

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.PackInterface#toString()
	 */
	public abstract String toString();

}