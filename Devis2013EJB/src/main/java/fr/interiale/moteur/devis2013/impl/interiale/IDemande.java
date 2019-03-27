package fr.interiale.moteur.devis2013.impl.interiale;

import java.util.List;

import fr.interiale.moteur.devis2013.impl.commun.Adresse;
import fr.interiale.moteur.devis2013.impl.commun.Conjoint;
import fr.interiale.moteur.devis2013.impl.commun.Coordonnees;
import fr.interiale.moteur.devis2013.impl.commun.Enfant;
import fr.interiale.moteur.devis2013.impl.commun.Moi;

public interface IDemande {

	public enum choix_prevoyance {NIV1,NIV2,NIV3,NIV4}
	
	public abstract Adresse getAdresse();

	public abstract void setAdresse(Adresse adresse);

	public abstract Coordonnees getCoordonnees();

	public abstract void setCoordonnees(Coordonnees coordonnees);

	public abstract Moi getMoi();

	public abstract void setMoi(Moi moi);

	public abstract Conjoint getConjoint();

	public abstract void setConjoint(Conjoint conjoint);

	public abstract List<Enfant> getEnfants();

	public abstract void setEnfants(List<Enfant> enfants);

	public abstract String toString();

}