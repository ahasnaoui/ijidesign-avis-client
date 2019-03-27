package fr.interiale.moteur.devis2013.impl.labellise;

import org.tempuri.RatingData;

import fr.interiale.moteur.devis2013.impl.commun.IReponse;
import fr.interiale.moteur.devis2013.impl.interiale.DependanceTotale;


public interface IReponseLabellise extends IReponse{

	public OptionMaintientSalaire getOption_maintient_salaire() ;
	
	public RatingData getRatingData();

	public DependanceTotale getDependanceTotale();


	public OptionPTLFRASansFranchiseRenfort getOptionPTLFRASansFranchiseRenfort();

	public OptionPTLFRASansFranchiseStandard getOptionPTLFRASansFranchiseStandard();
}