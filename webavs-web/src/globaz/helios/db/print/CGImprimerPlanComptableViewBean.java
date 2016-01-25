package globaz.helios.db.print;

import globaz.globall.db.BSession;
import globaz.helios.application.CGApplication;
import globaz.helios.itext.list.CGProcessImpressionPlanComptable;

/**
 * Insérez la description du type ici. Date de création : (10.01.2003 10:23:23)
 * 
 * @author: Administrator
 */
public class CGImprimerPlanComptableViewBean extends CGProcessImpressionPlanComptable implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGImprimerPlanComptableViewBean() throws Exception {
        super(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS), CGApplication.APPLICATION_HELIOS_REP,
                "Plan Comptable");
        setFileTitle("Plan comptable");
    }
}
