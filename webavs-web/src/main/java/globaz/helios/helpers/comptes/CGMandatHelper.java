package globaz.helios.helpers.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGMandatViewBean;

/**
 * Classe : type_conteneur Description : Date de création: 23 sept. 04
 * 
 * @author scr
 */
public class CGMandatHelper extends FWHelper {

    /**
     * Constructor for CGMandatHelper.
     */
    public CGMandatHelper() {
        super();
    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {
        if (action.getActionPart().equals("ajouterMandat")) {
            viewBean.setMsgType(FWViewBeanInterface.OK);
            viewBean.setMessage("");

            CGMandatViewBean vBean = (CGMandatViewBean) viewBean;
            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                vBean.add(transaction);
                vBean.ouvrir((BTransaction) transaction, vBean.getDateDebut(), vBean.getDateFin());
                vBean.update(transaction);
            } catch (Exception e) {
                try {
                    transaction.addErrors(e.getMessage());
                } catch (Exception ee) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(ee.getMessage());
                }

                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            } finally {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.getMessage());
                } finally {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return viewBean;
    }

}
