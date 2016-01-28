package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.phenix.db.communications.CPRejetsViewBean;
import globaz.phenix.process.communications.CPProcessRejetsAbandonner;

/**
 * Insérez la description du type ici. Date de création : (27.11.2002 10:37:05)
 * 
 * @author: Administrator
 */
public class CPRejetsHelper extends FWHelper {
    /**
     * Commentaire relatif au constructeur TITiersHelper.
     */
    public CPRejetsHelper() {
        super();

    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        if ("abandonner".equals(action.getActionPart()) && (viewBean instanceof CPRejetsViewBean)) {
            CPRejetsViewBean db = (CPRejetsViewBean) viewBean;

            CPProcessRejetsAbandonner abandonner = new CPProcessRejetsAbandonner();
            abandonner.setIdRejet(db.getIdRejets());
            abandonner.setISession(db.getSession());
            abandonner.setSendMailOnError(true);
            abandonner.setSendCompletionMail(false);
            try {
                abandonner.executeProcess();
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
            }

        }
        return super.execute(viewBean, action, session);
    }

}
