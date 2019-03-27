package fr.interiale.moteur.devis2013.impl.interiale;

import java.io.Serializable;
import java.util.List;

import fr.interiale.moteur.devis2013.impl.commun.Adresse;
import fr.interiale.moteur.devis2013.impl.commun.Conjoint;
import fr.interiale.moteur.devis2013.impl.commun.Coordonnees;
import fr.interiale.moteur.devis2013.impl.commun.Enfant;
import fr.interiale.moteur.devis2013.impl.commun.Moi;

public class DemandeInteriale implements Serializable, IDemande {

	private static final long serialVersionUID = -4669015890786639208L;
	private Moi moi;
	private Conjoint conjoint;
	private List<Enfant> enfants;
	
	private Coordonnees coordonnees=new Coordonnees("","","","","","");
	private Adresse adresse=new Adresse("","","","","","");
	
	
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#getAdresse()
	 */
	public Adresse getAdresse() {
		return adresse;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#setAdresse(fr.interiale.moteur.devis2013.impl.commun.Adresse)
	 */
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#getCoordonnees()
	 */
	public Coordonnees getCoordonnees() {
		return coordonnees;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#setCoordonnees(fr.interiale.moteur.devis2013.impl.commun.Coordonnees)
	 */
	public void setCoordonnees(Coordonnees coordonnees) {
		this.coordonnees = coordonnees;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#getMoi()
	 */
	public Moi getMoi() {
		return moi;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#setMoi(fr.interiale.moteur.devis2013.impl.commun.Moi)
	 */
	public void setMoi(Moi moi) {
		this.moi = moi;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#getConjoint()
	 */
	public Conjoint getConjoint() {
		return conjoint;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#setConjoint(fr.interiale.moteur.devis2013.impl.commun.Conjoint)
	 */
	public void setConjoint(Conjoint conjoint) {
		this.conjoint = conjoint;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#getEnfants()
	 */
	public List<Enfant> getEnfants() {
		
		return enfants;
	}
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#setEnfants(java.util.List)
	 */
	public void setEnfants(List<Enfant> enfants) {
		this.enfants = enfants;
	}
	
	/* (non-Javadoc)
	 * @see fr.interiale.moteur.devis2013.impl.interiale.IDemande#toString()
	 */
	@Override
	public String toString() {
		return "DemandeInteriale [moi=" + moi + "\n conjoint=" + conjoint + "\n enfants=" + enfants + "\n coordonnees=" + coordonnees + "\n adresse=" + adresse + "]";
	}
	
	
	
	
	
}
