package ch.globaz.pyxis.exceptions;

/**
 * Exception runtime lancée lors d'une erreur technique dans le module des tiers
 */
public class TITechnicalExcpetion extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TITechnicalExcpetion(String message) {
        super(message);
    }

    public TITechnicalExcpetion(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public TITechnicalExcpetion(Throwable nestedException) {
        super(nestedException);
    }
}
