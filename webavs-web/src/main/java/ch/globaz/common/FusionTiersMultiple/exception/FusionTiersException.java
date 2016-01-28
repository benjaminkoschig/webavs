package ch.globaz.common.FusionTiersMultiple.exception;

/**
 * Exception m�re des exceptions g�n�r�es par l'application de gestion des prestations compl�mentaires famille
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
