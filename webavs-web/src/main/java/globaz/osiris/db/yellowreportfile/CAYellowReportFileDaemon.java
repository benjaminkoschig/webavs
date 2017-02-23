package globaz.osiris.db.yellowreportfile;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.osiris.application.CAApplication;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.daemon.suividecompte.AbstractDaemon;

public class CAYellowReportFileDaemon extends AbstractDaemon {

    private BSession session;

    @Override
    public void run() {
        try {
            initBsession();

            CAYellowReportFileProcess process = new CAYellowReportFileProcess();
            process.setSession(session);
            BProcessLauncher.start(process);
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
        }
    }

    private void initBsession() throws Exception {
        session = (BSession) GlobazServer.getCurrentSystem().getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(session, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }
}
