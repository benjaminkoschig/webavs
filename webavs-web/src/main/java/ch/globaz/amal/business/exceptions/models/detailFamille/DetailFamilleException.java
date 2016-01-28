package ch.globaz.amal.business.exceptions.models.detailFamille;

import ch.globaz.amal.business.exceptions.AmalException;

public class DetailFamilleException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DetailFamilleException() {
        super();
    }

    public DetailFamilleException(String m) {
        super(m);
    }

    public DetailFamilleException(String m, Throwable t) {
        super(m, t);
    }
}
