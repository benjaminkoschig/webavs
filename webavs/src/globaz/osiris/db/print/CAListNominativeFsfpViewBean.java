package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.print.list.CAListNominativeFsfp;

/**
 * @author BJO
 * 
 */
public class CAListNominativeFsfpViewBean extends CAListNominativeFsfp implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListNominativeFsfpViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * @param session
     */
    public CAListNominativeFsfpViewBean(BSession session) {
        super(session);
    }
}
