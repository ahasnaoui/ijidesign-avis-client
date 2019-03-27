package fr.interiale.moteur.devis2013.service;

public class Site implements java.io.Serializable {

	private static final long serialVersionUID = 7152480974668068551L;

		private java.lang.String adresse1;

	    private java.lang.String adresse2;

	    private java.lang.String codePostal;

	    private java.lang.String fax;

	    private java.lang.Float latitude;

	    private java.lang.Float longitude;

	    private java.lang.String mail;

	    private java.lang.String nom;

	    private java.lang.String telephone;

	    private java.lang.String type;

	    private java.lang.String ville;

	    private java.lang.String horaires;

	    public Site() {
			
		}

	    public Site(com.edeal.ws.custom.interiale.beans.Site s) {
	    	this.adresse1 = s.getAdresse1();
	           this.adresse2 = s.getAdresse2();
	           this.codePostal = s.getCodePostal();
	           this.fax = s.getFax();
	           this.latitude = s.getLatitude();
	           this.longitude = s.getLongitude();
	           this.mail = s.getMail();
	           this.nom = s.getNom();
	           this.telephone = s.getTelephone();
	           this.type = s.getType();
	           this.ville = s.getVille();
	           this.horaires = s.getHoraires();
	    	
		}
	    
		public java.lang.String getAdresse1() {
			return adresse1;
		}

		public void setAdresse1(java.lang.String adresse1) {
			this.adresse1 = adresse1;
		}

		public java.lang.String getAdresse2() {
			return adresse2;
		}

		public void setAdresse2(java.lang.String adresse2) {
			this.adresse2 = adresse2;
		}

		public java.lang.String getCodePostal() {
			return codePostal;
		}

		public void setCodePostal(java.lang.String codePostal) {
			this.codePostal = codePostal;
		}

		public java.lang.String getFax() {
			return fax;
		}

		public void setFax(java.lang.String fax) {
			this.fax = fax;
		}

		public java.lang.Float getLatitude() {
			return latitude;
		}

		public void setLatitude(java.lang.Float latitude) {
			this.latitude = latitude;
		}

		public java.lang.Float getLongitude() {
			return longitude;
		}

		public void setLongitude(java.lang.Float longitude) {
			this.longitude = longitude;
		}

		public java.lang.String getMail() {
			return mail;
		}

		public void setMail(java.lang.String mail) {
			this.mail = mail;
		}

		public java.lang.String getNom() {
			return nom;
		}

		public void setNom(java.lang.String nom) {
			this.nom = nom;
		}

		public java.lang.String getTelephone() {
			return telephone;
		}

		public void setTelephone(java.lang.String telephone) {
			this.telephone = telephone;
		}

		public java.lang.String getType() {
			return type;
		}

		public void setType(java.lang.String type) {
			this.type = type;
		}

		public java.lang.String getVille() {
			return ville;
		}

		public void setVille(java.lang.String ville) {
			this.ville = ville;
		}

		public java.lang.String getHoraires() {
			return horaires;
		}

		public void setHoraires(java.lang.String horaires) {
			this.horaires = horaires;
		}

}
