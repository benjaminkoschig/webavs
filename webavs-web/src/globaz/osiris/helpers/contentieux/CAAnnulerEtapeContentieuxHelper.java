package globaz.osiris.helpers.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean;

public class CAAnnulerEtapeContentieuxHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session) {
        try {
            execute(viewBean, action, session);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(FWViewBeanInterface viewBean, FWAction action, BISession
     *      session)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            ((CAEvenementContentieuxViewBean) viewBean).annulerEtapeContentieux(transaction);

            if (transaction.isRollbackOnly()) {
                transaction.rollback();
                viewBean.setMessage(transaction.getErrors().toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);

            transaction.addErrors(e.toString());
        } finally {
            if (transaction != null) {
                try {
                    try {
                        if (transaction.hasErrors()) {
                            transaction.rollback();
                        }
                    } finally {
                        transaction.closeTransaction();
                    }
                } catch (Exception e) {
                    viewBean.setMessage(e.getMessage());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            }
        }

        return viewBean;
    }
}
