package ch.globaz.ij.businessimpl.services.Exception;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class ServiceBusinessException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * <strong>Attention, le message sera internationalis� !</strong>
     * 
     * @param message
     *            cl� du message d'erreur.
     */
    public ServiceBusinessException(String message) {
        super(message);
    }

}
