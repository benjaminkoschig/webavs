package ch.globaz.ij.businessimpl.services.Exception;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class ServiceTechnicalException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * <strong>Attention, le message sera internationalisé !</strong>
     * 
     * @param message
     *            clé du message d'erreur.
     */
    public ServiceTechnicalException(String message) {
        super(message);
    }

    public ServiceTechnicalException(String message, Exception exception) {
        super(message, exception);
    }
}
