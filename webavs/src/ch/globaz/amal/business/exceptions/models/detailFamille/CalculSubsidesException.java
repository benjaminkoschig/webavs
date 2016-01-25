package ch.globaz.amal.business.exceptions.models.detailFamille;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * Class exception qui permet de gérer les erreurs lors du calculs des sudsides
 * 
 * @author CBU
 * @version 1.00 17.06.2011
 * 
 */
public class CalculSubsidesException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CalculSubsidesException() {
        super();
    }

    public CalculSubsidesException(String m) {
        super(m);
    }

    public CalculSubsidesException(String m, Throwable t) {
        super(m, t);
    }
}
