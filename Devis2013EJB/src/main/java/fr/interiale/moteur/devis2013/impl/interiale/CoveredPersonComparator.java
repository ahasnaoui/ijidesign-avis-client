package fr.interiale.moteur.devis2013.impl.interiale;

import java.util.Comparator;

import org.tempuri.CoveredPerson;

public class CoveredPersonComparator implements Comparator<CoveredPerson> {

	public int compare(CoveredPerson arg0, CoveredPerson arg1) {
		return arg0.getBirthDate().compareTo(arg1.getBirthDate());
		
	}

}
