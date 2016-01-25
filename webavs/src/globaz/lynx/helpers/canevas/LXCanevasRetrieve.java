package globaz.lynx.helpers.canevas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.canevas.LXCanevasViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXCanevasRetrieve {

    /**
     * Recharge les écritures pour affichage écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrieveCanevas(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXCanevasViewBean canevas = (LXCanevasViewBean) viewBean;
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();

            LXHelperUtils.validateIdOperationCanevas(session, transaction, canevas.getIdOperationCanevas());

            LXHelperUtils.fillWithCanevasOperation(session, canevas);

            LXHelperUtils.fillWithCanevasSection(session, canevas);

            LXHelperUtils.fillForLayoutOnly(session, canevas);

            LXHelperUtils.fillWithCanevasVentilations(session, canevas);

        } catch (Exception e) {
            canevas.setMessage(e.getMessage());
            canevas.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
