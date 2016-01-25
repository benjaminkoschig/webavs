package globaz.pegasus.helpers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWHelper;
import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.exceptions.PegasusException;

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
        } else {
            JadeThread.logError("", e.toString());
        }

        viewBean.setMessage(e.toString());
        viewBean.setMsgType(FWViewBeanInterface.ERROR);
        e.printStackTrace();

    }

    private void setPegasusExceptionMessage(final Throwable t) {
        PegasusException pc = (PegasusException) t;
        JadeThread.logError("", pc.getMessage(), pc.getParameters());
    }

}
