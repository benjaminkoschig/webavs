package ch.globaz.amal.business.exceptions.models.annoncesedexco;

import ch.globaz.amal.business.exceptions.AmalException;

public class AnnonceSedexCOException extends AmalException {

    public AnnonceSedexCOException() {
    }

    public AnnonceSedexCOException(String m) {
        super(m);
    }

    public AnnonceSedexCOException(String m, Throwable t) {
        super(m, t);
    }

}
