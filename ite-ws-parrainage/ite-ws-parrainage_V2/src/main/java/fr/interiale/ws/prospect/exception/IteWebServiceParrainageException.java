package fr.interiale.ws.prospect.exception;

/**
 * Erreur lors de l'appel au Web Service
 * 
 * @author adil hasnaoui
 */
public class IteWebServiceParrainageException extends Exception {

    private static final long serialVersionUID = 571898312315534889L;

    public IteWebServiceParrainageException() {
        super();
    }

    public IteWebServiceParrainageException(String msg) {
        super(msg);
    }

    public IteWebServiceParrainageException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IteWebServiceParrainageException(Throwable cause) {
        super(cause);
    }

}