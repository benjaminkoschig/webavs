package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.process.CAProcessListSectionsOuvertes;

/**
 * @author dda
 */
public class CAListSectionsOuvertesViewBean extends CAProcessListSectionsOuvertes implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListSectionsOuvertesViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * @param session
     */
    public CAListSectionsOuvertesViewBean(BSession session) {
        super(session);
    }

}
