package globaz.naos.helpers.annonceAffilie;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.annonceAffilie.AFImpressionMutationViewBean;
import globaz.naos.process.AFImpressionAnnonceBatch;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFImpressionMutationHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        ((AFImpressionMutationViewBean) viewBean).setEMailAddress(session.getUserEMail());
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFImpressionMutationViewBean afViewBean = (AFImpressionMutationViewBean) viewBean;
        try {

            AFImpressionAnnonceBatch annonce = new AFImpressionAnnonceBatch();
            annonce.setISession(session);
            annonce.setEMailAddress(afViewBean.getEMailAddress());
            // annonce.setAffiliationId("21154");
            annonce.setDateAnnonce(afViewBean.getDateAnnonce());
            // JadeLogger.enableDebug(true);
            BProcessLauncher.start(annonce);

        } catch (Exception e) {
            afViewBean.setMessage(e.getMessage());
            afViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
