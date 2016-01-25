package globaz.helios.db.print;

import globaz.globall.db.BSession;
import globaz.helios.application.CGApplication;
import globaz.helios.itext.list.CGProcessImpressionBilan;

public class CGImprimerBilanViewBean extends CGProcessImpressionBilan implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGImprimerBilanViewBean() throws Exception {
        super(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS), CGApplication.APPLICATION_HELIOS_REP, "Bilan");
    }

}
