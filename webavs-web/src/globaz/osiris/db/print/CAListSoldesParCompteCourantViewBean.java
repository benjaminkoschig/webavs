package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.process.CAProcessListSoldesParCompteCourant;

/**
 * @author dda
 */
public class CAListSoldesParCompteCourantViewBean extends CAProcessListSoldesParCompteCourant implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListSoldesParCompteCourantViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * @param session
     */
    public CAListSoldesParCompteCourantViewBean(BSession session) {
        super(session);
    }

}
