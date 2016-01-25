package ch.globaz.amal.business.exceptions.models.uploadfichierreprise;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author cbu
 * 
 */
public class AMUploadFichierRepriseException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AMUploadFichierRepriseException() {
        super();
    }

    public AMUploadFichierRepriseException(String m) {
        super(m);
    }

    public AMUploadFichierRepriseException(String m, Throwable t) {
        super(m, t);
    }

}
