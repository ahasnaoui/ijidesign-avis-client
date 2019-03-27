package fr.interiale.moteur.devis2013.impl.labellise;

import fr.interiale.moteur.devis2013.impl.commun.IChoixProspect;
import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;


public interface IChoixProspectLabellise  extends IChoixProspect {

	public boolean isChoix_option_maintient_de_salaire() ;
	public void setChoix_option_maintient_de_salaire(boolean choix_option_maintient_de_salaire);
	public choix_prevoyance getPrevoyance();
	public void setPrevoyance(choix_prevoyance prevoyance) ;
}
