package ch.globaz.corvus.process.echeances;

import globaz.corvus.application.REApplication;
import globaz.globall.db.BSessionUtil;
import globaz.lyra.process.LYAbstractListEcheanceProcess;

public abstract class REListeEcheanceProcess<T extends REListeEcheanceDocumentGenerator> extends
        LYAbstractListEcheanceProcess<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REListeEcheanceProcess() {
        super();
    }

    @Override
    protected abstract T buildListGenerator() throws Exception;

    @Override
    public String getDescription() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("TITRE_MAIL_ANALYSE_ECHEANCE_PROCESS");
    };

    @Override
    protected String getSessionApplicationName() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }
}
