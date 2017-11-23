package globaz.pegasus.helpers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWHelper;
import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;

public abstract class PegasusHelper extends FWHelper {

    /**
     * Gestion des erreurs au niveau du viewBean et du JadeThread
     * 
     * @param viewBean le viewBean à traiter
     * @param e l'exception catchée
     */
    public void putTransactionInError(final FWViewBeanInterface viewBean, final Throwable e) {

        if (e instanceof PegasusException) {
            setPegasusExceptionMessage(e);
        } else if (e instanceof RpcBusinessException) {
            setRPCExceptionMessage(e);
        } else {
            JadeThread.logError("", e.toString());
        }

        // Next 2 lines are overriden by FWHelper.class on afterExecute() method
        viewBean.setMessage(e.toString());
        viewBean.setMsgType(FWViewBeanInterface.ERROR);
        e.printStackTrace();

    }

    private void setPegasusExceptionMessage(final Throwable t) {
        PegasusException pc = (PegasusException) t;
        JadeThread.logError("", pc.getMessage(), pc.getParameters());
    }

    private void setRPCExceptionMessage(final Throwable t) {
        RpcBusinessException rpc = (RpcBusinessException) t;
        if (rpc.getLabelMessage().isEmpty()) {
            JadeThread.logError("", t.toString());
        } else {
            JadeThread.logError("", rpc.getLabelMessage(), rpc.getParams().toArray(new String[0]));
        }
    }
}
