package globaz.osiris.db.ordres.sepa.exceptions;

import ch.globaz.osiris.business.exception.OsirisException;

public class CACamt054UnsupportedVersionException extends OsirisException {

    private static final long serialVersionUID = 3306337436055954685L;

    public CACamt054UnsupportedVersionException(String message) {
        super(message);
    }

    public CACamt054UnsupportedVersionException(String msg, Exception e) {
        super(msg, e);
    }
}
