package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.process.CAProcessListSectionsOuvertesApresRadiation;

/**
 * @author dda
 */
public class CAListSectionsOuvertesApresRadiationViewBean extends CAProcessListSectionsOuvertesApresRadiation implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListSectionsOuvertesApresRadiationViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * @param session
     */
    public CAListSectionsOuvertesApresRadiationViewBean(BSession session) {
        super(session);
    }

}
