package ch.globaz.vulpecula.daemon.suividecompte;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

public class ProcessTaxationOffice extends AbstractDaemon {
    private BSession bsession;

    @Override
    public void run() {
        try {
            initBsession();
            List<Decompte> decomptesForTaxationOffice = getDecomptesForTaxationOffice();

            if (decomptesForTaxationOffice.isEmpty() == false) {
                VulpeculaServiceLocator.getTaxationOfficeService().genererTaxationsOffice(decomptesForTaxationOffice);
            }
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
        }
    }

    private void initBsession() throws Exception {
        bsession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(bsession, this);
    }

    /**
     * Retourne la liste des décomptes à passer en taxation d'office.
     */
    private List<Decompte> getDecomptesForTaxationOffice() {
        return VulpeculaRepositoryLocator.getDecompteRepository().findDecomptesForTaxationOffice();
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }
}
