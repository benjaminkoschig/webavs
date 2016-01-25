package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.print.list.CAListRecapRubriques;

/**
 * @author dda
 */
public class CAListRecapParRubriquesViewBean extends CAListRecapRubriques implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListRecapParRubriquesViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * @param session
     */
    public CAListRecapParRubriquesViewBean(BSession session) {
        super(session);
    }

}
