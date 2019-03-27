package fr.interiale.moteur.devis2013.impl.interiale;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire;

public class MaintientDeSalaire implements Serializable, IMaintientDeSalaire{

	private static final long serialVersionUID = -2263878144367015783L;
	private  BigDecimal cotisation_maintient_de_salaire=BigDecimal.ZERO;
	private  BigDecimal cotisation_jour_de_carence=BigDecimal.ZERO;
	
	public MaintientDeSalaire(BigDecimal cotisation) {
		super();
		this.cotisation_maintient_de_salaire = cotisation;
	}
	
	public MaintientDeSalaire() {}

	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.interfaces.IMaintientDeSalaire#getCotisation()
	 */
	public BigDecimal getCotisation() {
		return cotisation_maintient_de_salaire.add(cotisation_jour_de_carence);
	}

	public BigDecimal getCotisation_maintient_de_salaire() {
		return cotisation_maintient_de_salaire;
	}

	public BigDecimal getCotisation_jour_de_carence() {
		return cotisation_jour_de_carence;
	}

	public void setCotisation_maintient_de_salaire(BigDecimal cotisation_maintient_de_salaire) {
		this.cotisation_maintient_de_salaire = cotisation_maintient_de_salaire;
	}

	public void setCotisation_jour_de_carence(BigDecimal cotisation_jour_de_carence) {
		this.cotisation_jour_de_carence = cotisation_jour_de_carence;
	}

	@Override
	public String toString() {
		return "MaintientDeSalaire  : "+getCotisation()+"[cotisation_maintient_de_salaire=" + cotisation_maintient_de_salaire
				+ ", cotisation_jour_de_carence=" + cotisation_jour_de_carence + "]";
	}
	
}
