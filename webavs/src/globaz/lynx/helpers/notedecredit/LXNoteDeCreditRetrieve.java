package globaz.lynx.helpers.notedecredit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.notedecredit.LXNoteDeCreditViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;

public class LXNoteDeCreditRetrieve {

    /**
     * Recharge les écritures pour affichage écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrieveNoteDeCredit(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXNoteDeCreditViewBean noteDeCredit = (LXNoteDeCreditViewBean) viewBean;
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();

            LXHelperUtils.validateIdOperation(session, transaction, noteDeCredit.getIdOperation());

            LXHelperUtils.fillWithOperation(session, noteDeCredit);

            LXHelperUtils.fillWithSection(session, noteDeCredit);

            LXHelperUtils.fillForLayoutOnly(session, noteDeCredit);

            LXHelperUtils.fillWithVentilations(session, noteDeCredit);

        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
