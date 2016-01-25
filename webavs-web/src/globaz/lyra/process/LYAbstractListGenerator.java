package globaz.lyra.process;

import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

public abstract class LYAbstractListGenerator extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEcheance;
    private String idLog;
    private String moisTraitement;

    public LYAbstractListGenerator() {
        super();
    }

    public LYAbstractListGenerator(BSession session, String filenameRoot, String companyName, String documentTitle,
            BManager manager) {
        super(session, filenameRoot, companyName, documentTitle, manager);
    }

    public LYAbstractListGenerator(BSession session, String filenameRoot, String companyName, String documentTitle,
            BManager manager, String applicationId) {
        super(session, filenameRoot, companyName, documentTitle, manager, applicationId);
    }

    public LYAbstractListGenerator(String filenameRoot, String companyName, String documentTitle, String applicationId) {
        super(filenameRoot, companyName, documentTitle, applicationId);
    }

    public String getEmailObject() {
        return super.getEMailObject();
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
