package globaz.osiris.db.ordres.sepa.exceptions;

import ch.globaz.osiris.business.exception.OsirisException;

public class ISODataMissingXMLException extends OsirisException {

    private static final long serialVersionUID = 3306337436055954685L;

    public ISODataMissingXMLException(String message) {
        super(message);
    }

    public ISODataMissingXMLException(String msg, Exception e) {
        super(msg, e);
    }
}
