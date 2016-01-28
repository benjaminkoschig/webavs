package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.process.CAProcessImpressionRappelPlan;

/**
 * @author Alain Dominé To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CARappelPlanViewBean extends CAProcessImpressionRappelPlan implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CARappelPlanViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }
}
