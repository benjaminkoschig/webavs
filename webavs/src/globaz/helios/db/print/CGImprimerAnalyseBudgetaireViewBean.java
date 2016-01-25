package globaz.helios.db.print;

import globaz.globall.db.BSession;
import globaz.helios.application.CGApplication;
import globaz.helios.itext.list.CGProcessImpressionAnalyseBudgetaire;

public class CGImprimerAnalyseBudgetaireViewBean extends CGProcessImpressionAnalyseBudgetaire {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGImprimerAnalyseBudgetaireViewBean() throws Exception {
        super(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

}
