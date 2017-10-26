package globaz.pegasus.helpers.rpc;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.pegasus.vb.rpc.PCAnnoncesViewBean;
import ch.globaz.common.process.byitem.ProcessItemsFactory;
import ch.globaz.common.process.byitem.ProcessItemsService;
import ch.globaz.pegasus.rpc.businessImpl.sedex.ExecutionMode;
import ch.globaz.pegasus.rpc.process.GenererAnnoncesProcess;

public class PCAnnoncesHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PCAnnoncesViewBean vb = (PCAnnoncesViewBean) viewBean;
        try {

            ExecutionMode executionMode = ExecutionMode.valueOf(vb.getMode());
            Boolean isProcessRunnig = ProcessItemsService.isProcessRunnig(GenererAnnoncesProcess.KEY);
            if (!isProcessRunnig) {
                ProcessItemsFactory.newInstance().start(new GenererAnnoncesProcess(executionMode)).build();
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

}
