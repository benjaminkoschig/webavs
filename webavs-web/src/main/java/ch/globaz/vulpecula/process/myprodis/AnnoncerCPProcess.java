package ch.globaz.vulpecula.process.myprodis;

import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import ch.globaz.vulpecula.externalservices.RequestFactory;

public class AnnoncerCPProcess extends MyProdisProcess {
    private static final long serialVersionUID = -3616101620982529564L;

    private static ReentrantLock lock = new ReentrantLock();

    @Override
    protected boolean _executeProcess() throws Exception {
        if (lock.isLocked()) {
            getMemoryLog().logMessage(getSession().getLabel("PROCESSUS_DEJA_LANCE"), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
            return false;
        }
        super._executeProcess();
        try {
            lock.lock();
            Response response = Request.Post(RequestFactory.URL_CP).execute();
            return checkAndLogErrorInResponse(response);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

}
