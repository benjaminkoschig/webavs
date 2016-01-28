package globaz.lyra.process;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

public abstract class LYAbstractDocumentGenerator extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEcheance;
    private String idLog;
    private String moisTraitement;

    public LYAbstractDocumentGenerator(BProcess parent, String idApplication, String fileName) throws FWIException {
        super(parent, idApplication, fileName);
    }

    public LYAbstractDocumentGenerator(BSession session, String idApplication, String fileName) throws FWIException {
        super(session, idApplication, fileName);
    }

    public String getEmailObject() {
        return getEMailObject();
    }

    public String getIdEcheance() {
        return idEcheance;
    }

    public String getIdLog() {
        return idLog;
    }

    public String getMoisTraitement() {
        return moisTraitement;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public void setMoisTraitement(String moisTraitement) {
        this.moisTraitement = moisTraitement;
    }
}
