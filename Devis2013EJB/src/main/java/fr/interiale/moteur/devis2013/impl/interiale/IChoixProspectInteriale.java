package fr.interiale.moteur.devis2013.impl.interiale;

import fr.interiale.moteur.devis2013.impl.commun.IChoixProspect;
import fr.interiale.moteur.devis2013.impl.interiale.ProduitSante.Code_produit_sante;

public interface IChoixProspectInteriale extends IChoixProspect{

	public abstract Code_produit_sante getChoix_produit_sante();

	public abstract void setChoix_produit_sante(Code_produit_sante choix_produit_sante);

	public abstract boolean isChoix_pack_famille();

	public abstract void setChoix_pack_famille(boolean choix_pack_famille);

	public abstract boolean isChoix_pack_pro();

	public abstract void setChoix_pack_pro(boolean choix_pack_pro);

	public abstract boolean isChoix_pack_senior();

	public abstract void setChoix_pack_senior(boolean choix_pack_senior);

	public abstract boolean isChoix_garantie_couverture_prime();

	public abstract void setChoix_garantie_couverture_prime(boolean choix_garantie_couverture_prime);

	public abstract boolean isChoix_garantie_deces();

	public abstract void setChoix_garantie_deces(boolean choix_garantie_deces);

	public abstract boolean isChoix_maintient_de_salaire();

	public abstract void setChoix_maintient_de_salaire(boolean choix_maintient_de_salaire);

	public abstract int hashCode();

	public abstract boolean equals(Object obj);

	public abstract String toString();

}