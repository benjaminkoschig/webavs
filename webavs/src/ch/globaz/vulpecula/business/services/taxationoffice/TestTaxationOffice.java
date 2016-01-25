package ch.globaz.vulpecula.business.services.taxationoffice;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.daemon.suividecompte.ProcessSommation;
import ch.globaz.vulpecula.daemon.suividecompte.ProcessTaxationOffice;

/**
 * @author Arnaud Geiser (AGE) | Créé le 3 juin 2014
 * 
 */
public class TestTaxationOffice {
    /**
     * @param args
     */
    public static void main(final String[] args) {
        try {
            BSession bsession = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA).newSession("BMSGLO", "BMSGLO");
            BSessionUtil.initContext(bsession, new Object());

            // Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById("401352");
            // decompte.setEtat(EtatDecompte.SOMMATION);
            // decompte.setDateRappel(new Date("01.01.2014"));
            // VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);

            ProcessSommation sommation = new ProcessSommation();
            sommation.setPassword("BMSGLO");
            sommation.setUsername("BMSGLO");
            sommation.run();

            ProcessTaxationOffice to = new ProcessTaxationOffice();
            to.setPassword("BMSGLO");
            to.setUsername("BMSGLO");
            to.run();

            bsession.getCurrentThreadTransaction().commit();
            System.exit(0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
