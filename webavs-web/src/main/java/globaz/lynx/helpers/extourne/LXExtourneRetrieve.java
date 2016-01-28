package globaz.lynx.helpers.extourne;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.extourne.LXExtourneViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXExtourneRetrieve {

    /**
     * Recharge les écritures pour affichage écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrieveExtourne(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXExtourneViewBean extourne = (LXExtourneViewBean) viewBean;
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();

            LXHelperUtils.validateIdOperation(session, transaction, extourne.getIdOperation());

            LXHelperUtils.fillWithOperation(session, extourne);

            extourne.setMontant(extourne.getMontant());

            LXHelperUtils.fillWithSection(session, extourne);

            LXHelperUtils.fillForLayoutOnly(session, extourne);

            LXHelperUtils.fillWithVentilations(session, extourne);

        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
