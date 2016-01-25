package ch.globaz.al.business.exceptions.generation.prestations;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour le contexte d'exécution de la génération de prestations
 * 
 * @author jts
 */
public class ALGenerationException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALGenerationException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALGenerationException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALGenerationException(String m, Throwable t) {
        super(m, t);
    }
}