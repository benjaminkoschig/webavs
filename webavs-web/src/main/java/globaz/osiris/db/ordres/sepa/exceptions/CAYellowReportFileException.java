package globaz.osiris.db.ordres.sepa.exceptions;

import ch.globaz.osiris.business.exception.OsirisException;

public class CAYellowReportFileException extends OsirisException {

    private static final long serialVersionUID = 3306337436055954685L;

    public CAYellowReportFileException(String message) {
        super(message);
    }

    public CAYellowReportFileException(String msg, Exception e) {
        super(msg, e);
    }
}
