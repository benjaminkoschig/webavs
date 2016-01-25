package ch.globaz.al.business.exceptions.generation.prestations;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour le contexte d'exécution de la génération de prestations
 * 
 * @author jts
 */
public class ALGenerationPrestationsContextException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALGenerationPrestationsContextException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALGenerationPrestationsContextException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALGenerationPrestationsContextException(String m, Throwable t) {
        super(m, t);
    }
}