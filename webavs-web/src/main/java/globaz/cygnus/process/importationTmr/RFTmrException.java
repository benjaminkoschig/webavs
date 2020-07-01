package globaz.cygnus.process.importationTmr;

import globaz.jade.exception.JadeApplicationException;

public class RFTmrException extends JadeApplicationException {
    public RFTmrException(String string, Exception e) {
        super(string, e);
    }

    private static final long serialVersionUID = 4340189222450547005L;
}
