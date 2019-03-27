package fr.interiale.moteur.devis2013.service;

import javax.ejb.Remote;

import fr.interiale.moteur.devis2013.ejb.SauvegardeDevis;

@Remote
public interface SauvegardeDevisServiceRemote {

	public final static String JNDIName="SauvegardeDevisService/remote";
	
	public abstract int save(SauvegardeDevis devis);
	public abstract void merge(SauvegardeDevis devis);

}