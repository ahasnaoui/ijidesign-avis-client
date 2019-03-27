package fr.interiale.moteur.devis2013.interfaces;

public interface IFormulaireWS {
	// Type demande
	public final static String ADHERENT = "ADHERENT";
	public final static String PROSPECT = "PROSPECT";
	public final static String CONJOINT = "CONJOINT";
	public final static String BENEF = "BENEF";

	// Nature demande
	public final static String INFO = "DEMANDE D'INFORMATION OU DE DOCUMENTATION";
	public final static String SUIVI_DOSSIER = "DEMANDE SUR UNE RECLAMATION EN COURS";
	public final static String RECLA = "RECLAMATION";

	public final static String ETUDE = "DEMANDE DE DEVIS";
	public final static String RAPPEL = "DEMANDE DE RAPPEL";
	public final static String RDV = "DEMANDE DE RDV EN AGENCE";

	// Domaine demande
	public final static String SANTE = "SANTE";
	public final static String AS = "ACTION SOCIALE";
	public final static String CONTRAT = "VOTRE CONTRAT";
	public final static String COTISATION = "VOTRE COTISATION";
	public final static String PREV = "VOTRE PREVOYANCE";

	// Infos sous-formulaire
	public final static String FPE = "FONCTION PUBLIQUE D'ETAT";
	public final static String FPT = "FONCTION PUBLIQUE TERRITORIALE";
	public final static String FPH = "FONCTION PUBLIQUE HOSPITALIERE";
	public final static String RBTSANTERO = "REMBOURSEMENT D'UNE PRESTATION SANTE RO";
	public final static String RBTSANTERC = "REMBOURSEMENT D'UNE PRESTATION SANTE RC";
	public final static String SUIVI_DEVIS = "SUIVI DES DEVIS OU PRISE EN CHARGE SANTE";
	public final static String RBTPREV = "REMBOURSEMENT D'UNE PRESTATION PREVOYANCE";
	public final static String GESTIONSP = "GESTION DE VOTRE CONTRAT SANTE OU PREVOYANCE";
	public final static String GESTIONS = "GESTION DE VOTRE CONTRAT SANTE";
	public final static String GESTIONP = "GESTION DE VOTRE CONTRAT PREVOYANCE";
	public final static String GESTIONC = "GESTION DE VOTRE COTISATION";
	public final static String RBTAS = "MISE EN PLACE OU REMBOURSEMENT D'UNE PRESTATION ACTION SOCIALE";
	public final static String CAUTION = "MISE EN PLACE OU SUIVI D'UN DOSSIER CAUTION";
	public final static String WEB = "SITE INTERNET OU ESPACE ADHERENT";
	public final static String AUT = "AUTRE";

	// Nouvelles infos formulaire
	public final static String SOL_ACTSOC = "Votre dossier action sociale";
	public final static String SOL_CAUTION = "Votre dossier caution immobilière";
	public final static String SOL_COORD = "Le changement de vos coordonnées";
	public final static String SOL_DEVIS = "Le suivi de vos devis ou de vos prises en charge";
	public final static String SOL_GCSANTE = "La gestion de votre contrat santé";
	public final static String SOL_GCPREV = "La gestion de votre contrat prévoyance";
	public final static String SOL_INDPREV = "Vos indemnités Prévoyance (maintien de salaire, itt…)";
	public final static String SOL_RBTITE = "Le remboursement  d’Intériale pour une prestation santé";
	public final static String SOL_RBTSS = "Le remboursement de la Sécurité Sociale";
	public final static String SOL_RAD = "Votre radiation";
	public final static String SOL_RECOUV = "Votre recouvrement";
	public final static String SOL_WEB = "Le site internet ou votre espace adhérent";
	public final static String SOL_AUT = "Autres";

	// Adhésion en ligne
	public final static String CLASSIQUE = "CLASSIQUE";
	public final static String FULLWEB = "FULLWEB";
}
