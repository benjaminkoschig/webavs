package ch.globaz.amal.business.exceptions.models.annoncesedexco;

import ch.globaz.amal.business.exceptions.AmalException;

public class AnnonceSedexCOReceptionException extends AmalException {

    private static final long serialVersionUID = 316571628041280550L;

    public AnnonceSedexCOReceptionException(String m) {
        super(m);
    }

    public AnnonceSedexCOReceptionException(String m, Throwable t) {
        super(m, t);
    }

}
