package fr.interiale.moteur.devis2013.impl.minju;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.interiale.moteur.devis2013.impl.interiale.IDemande.choix_prevoyance;
import fr.interiale.moteur.devis2013.interfaces.IGarantieCouverturePrime;

public class GarantieCouverturePrimeMinju implements Serializable, IGarantieCouverturePrime {


	private static final long serialVersionUID = 8643237939401728959L;
	private   BigDecimal cotisation=BigDecimal.ZERO;
	private choix_prevoyance prevoyance=choix_prevoyance.NIV1;
	private   BigDecimal[] cotisations_prime= new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO,
			BigDecimal.ZERO, BigDecimal.ZERO };

		
	public GarantieCouverturePrimeMinju(BigDecimal cotisation) {
		super();
		this.cotisation = cotisation;
	}

	public GarantieCouverturePrimeMinju() {
		
	}
	
	public void setCotisation_prime(BigDecimal cotisation_primes,choix_prevoyance prev) {
		switch (prev) {
		case NIV1:
			 cotisations_prime[0]=cotisation_primes;
			 break;
		case NIV2:
			 cotisations_prime[1]=cotisation_primes;
			 break;
		case NIV3:
			 cotisations_prime[2]=cotisation_primes;
			 break;
		case NIV4:
			 cotisations_prime[3]=cotisation_primes;
			break;
		default:
			 cotisations_prime[0]=cotisation_primes;
			 break;
		}
	}
	
	public BigDecimal getCotisation() {
		switch (prevoyance) {
		case NIV1:
			return cotisations_prime[0];
		case NIV2:
			return cotisations_prime[1];
		case NIV3:
			return cotisations_prime[2];
		case NIV4:
			return cotisations_prime[3];
		default:
			return cotisations_prime[0];
		}
		}

	/**
	 * @return the prevoyance
	 */
	public choix_prevoyance getPrevoyance() {
		return prevoyance;
	}

	/**
	 * @param prevoyance the prevoyance to set
	 */
	public void setPrevoyance(choix_prevoyance prevoyance) {
		this.prevoyance = prevoyance;
	}



	public void setCotisation(BigDecimal cotisation) {
		this.cotisation = cotisation;
	}

	@Override
	public String toString() {
		return "GarantieCouverturePrime [cotisation=" + cotisation + "]";
	}
		
}
