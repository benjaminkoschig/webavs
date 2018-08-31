package ch.globaz.vulpecula.daemon.suividecompte;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.process.taxationoffice.ImprimerTaxationOfficeAnnuleProcess;

public class ProcessTaxationOfficeAnnule extends AbstractDaemon {
    private BSession session;

    @Override
    public void run() {
        try {
            initBsession();
            ImprimerTaxationOfficeAnnuleProcess imprimerTaxationOfficeAnnule = new ImprimerTaxationOfficeAnnuleProcess();
            imprimerTaxationOfficeAnnule.setSession(session);
            imprimerTaxationOfficeAnnule.start();
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
        }
    }

    private void initBsession() throws Exception {
        session = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(session, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }
}
