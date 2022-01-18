package ch.globaz.eform.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.process.CAProcessImportTraitementEBill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFProcessRecuperationFormulaires extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(CAProcessImportTraitementEBill.class);
    private final StringBuilder error = new StringBuilder();

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        LOG.info("Lancement du process de réception des formulaires.");
        this.setSendCompletionMail(true);
        this.setSendMailOnError(true);
        return false;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }
}
