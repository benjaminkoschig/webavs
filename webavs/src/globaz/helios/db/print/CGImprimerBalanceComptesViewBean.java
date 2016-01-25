package globaz.helios.db.print;

import globaz.globall.db.BSession;
import globaz.helios.application.CGApplication;
import globaz.helios.itext.list.CGProcessImpressionBalanceComptes;

public class CGImprimerBalanceComptesViewBean extends CGProcessImpressionBalanceComptes {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGImprimerBalanceComptesViewBean() throws Exception {
        super(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }
}
