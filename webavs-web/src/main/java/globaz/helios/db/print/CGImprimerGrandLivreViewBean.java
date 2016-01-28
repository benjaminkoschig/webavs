package globaz.helios.db.print;

import globaz.globall.db.BSession;
import globaz.helios.application.CGApplication;
import globaz.helios.itext.list.CGProcessImpressionGrandLivre;

public class CGImprimerGrandLivreViewBean extends CGProcessImpressionGrandLivre implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGImprimerGrandLivreViewBean() throws Exception {
        super(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }
}
