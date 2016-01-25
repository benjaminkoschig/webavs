package globaz.pegasus.helpers.downloadDocument;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.vb.downloadDocument.PCDownloadDocumentViewBean;

public class PCDownloadDocumentHelper extends PegasusHelper {
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("download".equals(action.getActionPart())) {
            try {
                ((PCDownloadDocumentViewBean) viewBean).retrieve();
            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }
        } else {
            viewBean = super.execute(viewBean, action, session);
        }
        return viewBean;
    }

}
