package globaz.helios.db.print;

import globaz.globall.db.BSession;
import globaz.helios.application.CGApplication;
import globaz.helios.itext.list.CGProcessImpressionPertesProfits;

public class CGImprimerPertesProfitsViewBean extends CGProcessImpressionPertesProfits {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGImprimerPertesProfitsViewBean() throws Exception {
        super(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }
}