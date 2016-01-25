package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.print.list.CAListSuiviPaiementsAutresTaches;

public class CAListSuiviPaiementsAutresTachesViewBean extends CAListSuiviPaiementsAutresTaches implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListSuiviPaiementsAutresTachesViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * @param session
     */
    public CAListSuiviPaiementsAutresTachesViewBean(BSession session) {
        super(session);
    }
}
