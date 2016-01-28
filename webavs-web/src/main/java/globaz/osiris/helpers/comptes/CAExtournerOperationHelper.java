package globaz.osiris.helpers.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CAExtournerOperationViewBean;

/**
 * Classe : type_conteneur Description : Date de création: 2 sept. 04
 * 
 * @author scr
 */
public class CAExtournerOperationHelper extends FWHelper {

    /**
     * Constructor for CAExtournerOperationHelper.
     */
    public CAExtournerOperationHelper() {
        super();
    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        try {
            if ("afficherOperation".equals(action.getActionPart())) {
                CAExtournerOperationViewBean oper = (CAExtournerOperationViewBean) viewBean;
                oper.setSession((BSession) session);
                // Retrieve the operation
                oper.getOperation();
            } else if ("executerExtourne".equals(action.getActionPart())) {
                CAExtournerOperationViewBean oper = (CAExtournerOperationViewBean) viewBean;
                oper.setSession((BSession) session);
                oper.getOperation().extourner(null, null, oper.getComment());
            } else {
                return viewBean;
            }

        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        return viewBean;
    }

}
