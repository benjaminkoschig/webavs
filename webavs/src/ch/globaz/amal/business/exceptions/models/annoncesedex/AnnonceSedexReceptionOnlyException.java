package ch.globaz.amal.business.exceptions.models.annoncesedex;

import ch.globaz.amal.business.exceptions.AmalException;

public class AnnonceSedexReceptionOnlyException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public AnnonceSedexReceptionOnlyException() {
    }

    /**
     * @param m
     */
    public AnnonceSedexReceptionOnlyException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AnnonceSedexReceptionOnlyException(String m, Throwable t) {
        super(m, t);
    }
}
