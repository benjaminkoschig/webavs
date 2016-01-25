package globaz.osiris.db.print;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;

/**
 * @author Alain Dominé To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CABulletinsSoldesViewBean extends CAImpressionBulletinsSoldes_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CABulletinsSoldesViewBean() throws Exception {
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CABulletinsSoldesViewBean.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CABulletinsSoldesViewBean(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Constructor for CABulletinsSoldesViewBean.
     * 
     * @param session
     * @throws FWIException
     */
    public CABulletinsSoldesViewBean(BSession session) throws FWIException {
        super(session);
    }

    /**
     * Constructor for CABulletinsSoldesViewBean.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CABulletinsSoldesViewBean(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

}
