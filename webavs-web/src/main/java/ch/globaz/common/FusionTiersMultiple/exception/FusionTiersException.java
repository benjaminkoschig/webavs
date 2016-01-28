package ch.globaz.common.FusionTiersMultiple.exception;

/**
 * Exception mère des exceptions générées par l'application de gestion des prestations complémentaires famille
 * 
 * @author vyj
 */
public class FusionTiersException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public FusionTiersException() {
        super();
    }

    /**
     * @param m
     */
    public FusionTiersException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public FusionTiersException(String m, Throwable t) {
        super(m, t);
    }

}
