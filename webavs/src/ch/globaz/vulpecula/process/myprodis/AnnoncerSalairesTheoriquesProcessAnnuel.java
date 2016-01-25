package ch.globaz.vulpecula.process.myprodis;

import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import ch.globaz.vulpecula.externalservices.RequestFactory;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import com.google.gson.JsonObject;

public class AnnoncerSalairesTheoriquesProcessAnnuel extends MyProdisProcess {
    private static final long serialVersionUID = 3436102695557356290L;

    private int annee;

    private static ReentrantLock lock = new ReentrantLock();

    public AnnoncerSalairesTheoriquesProcessAnnuel() {

    }

    public AnnoncerSalairesTheoriquesProcessAnnuel(int annee) {
        this.annee = annee;
    }

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
            JsonObject json = new JsonObject();
            json.addProperty(PTConstants.YEAR, annee);
            Response response = Request.Post(RequestFactory.URL_SALAIRES_THEORIQUES_ANNUEL)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON).execute();
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

    public final int getAnnee() {
        return annee;
    }

    public final void setAnnee(int annee) {
        this.annee = annee;
    }
}
