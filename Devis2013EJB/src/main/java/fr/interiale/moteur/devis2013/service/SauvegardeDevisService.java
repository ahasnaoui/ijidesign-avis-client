package fr.interiale.moteur.devis2013.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.interiale.moteur.devis2013.ejb.SauvegardeDevis;

@Stateless
public class SauvegardeDevisService implements SauvegardeDevisServiceRemote {

	
	@PersistenceContext(unitName="devisprospect")
	private EntityManager em;
	
	public int save(SauvegardeDevis devis ){
		
		em.persist(devis);
		return devis.getId();
	}
	
public void merge(SauvegardeDevis devis ){
		
		em.merge(devis);
	}
}
