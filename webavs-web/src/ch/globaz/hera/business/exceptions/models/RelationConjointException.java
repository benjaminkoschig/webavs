package ch.globaz.hera.business.exceptions.models;

import ch.globaz.hera.business.exceptions.HeraException;

public class RelationConjointException extends HeraException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RelationConjointException() {
    }

    public RelationConjointException(String m) {
        super(m);
    }

    public RelationConjointException(String m, Throwable t) {
        super(m, t);
    }

}
