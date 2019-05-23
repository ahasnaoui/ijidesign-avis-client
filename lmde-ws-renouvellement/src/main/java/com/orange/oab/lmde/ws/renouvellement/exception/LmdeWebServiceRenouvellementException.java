package com.orange.oab.lmde.ws.renouvellement.exception;

/**
 * Erreur lors de l'appel au Web Service
 * 
 * @author Quentin LE ROUX (kjlh7728) <qleroux.ext@orange.com>
 */
public class LmdeWebServiceRenouvellementException extends Exception {

    private static final long serialVersionUID = 571898312315534889L;

    public LmdeWebServiceRenouvellementException() {
        super();
    }

    public LmdeWebServiceRenouvellementException(String msg) {
        super(msg);
    }

    public LmdeWebServiceRenouvellementException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LmdeWebServiceRenouvellementException(Throwable cause) {
        super(cause);
    }

}