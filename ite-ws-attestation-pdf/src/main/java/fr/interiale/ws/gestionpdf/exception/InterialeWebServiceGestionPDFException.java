package fr.interiale.ws.gestionpdf.exception;

/**
 * Erreur lors de l'appel au Web Service
 * 
 * @author Quentin LE ROUX (kjlh7728) <qleroux.ext@orange.com>
 */
public class InterialeWebServiceGestionPDFException extends Exception {

    private static final long serialVersionUID = 571898312315534889L;

    public InterialeWebServiceGestionPDFException() {
        super();
    }

    public InterialeWebServiceGestionPDFException(String msg) {
        super(msg);
    }

    public InterialeWebServiceGestionPDFException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InterialeWebServiceGestionPDFException(Throwable cause) {
        super(cause);
    }

}