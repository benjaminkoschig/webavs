package globaz.aquila.process;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.daemon.suividecompte.AbstractDaemon;
import globaz.aquila.api.ICOApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.jade.properties.JadePropertiesService;

public class COImportMessageELP extends AbstractDaemon {
    private BSession bsession;

    @Override
    public void run() {
        try {
            initBsession();
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
        }
    }

    private void initBsession() throws Exception {
        bsession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(bsession, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }
}
