package fr.interiale.moteur.devis2013.impl.commun;

public interface IChoixProspect {
	public abstract boolean isAcceptation_email_offre_interiale();

	public abstract void setAcceptation_email_offre_interiale(boolean acceptation_email_offre_interiale);

	public abstract boolean isAcceptation_email_offre_partenaire();

	public abstract void setAcceptation_email_offre_partenaire(boolean acceptation_email_offre_partenaire);
	
	public boolean isEnvoi_courrier() ;

	public void setEnvoi_courrier(boolean envoi_courrier);

	public abstract Object getType_tarification();
}
