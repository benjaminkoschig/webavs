package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.process.CAProcessImpressionEcheancier;

/**
 * @author SEL <br>
 *         Date : 20 mai 08
 */
public class CAImpressionEcheancierViewBean extends CAProcessImpressionEcheancier implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CAImpressionEcheancierViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }
}
