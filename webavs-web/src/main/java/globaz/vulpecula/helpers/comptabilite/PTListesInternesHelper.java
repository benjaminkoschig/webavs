package globaz.vulpecula.helpers.comptabilite;

import static ch.globaz.vulpecula.documents.listesinternes.ListesInternesProcess.ExportMode.*;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.comptabilite.PTListesInternesViewBean;
import ch.globaz.vulpecula.documents.listesinternes.ListesInternesProcess;
import ch.globaz.vulpecula.documents.listesinternes.ListesInternesProcess.ExportMode;
import ch.globaz.vulpecula.documents.listesinternes.RecapitulatifParGenreCaisseProcess;

public class PTListesInternesHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTListesInternesViewBean vb = (PTListesInternesViewBean) viewBean;
        try {
            BProcess process;
            switch (vb.getGenre()) {
                case RECAP_CONVENTION:
                    process = createListesInternesProcess(vb, CONVENTION);
                    break;
                case RECAP_CAISSE:
                    process = createListesInternesProcess(vb, CAISSE);
                    break;
                case RECAP_GENRE_CAISSE:
                    process = createRecapitulatifGenreCaisseProcess(vb);
                    break;
                case RECAP_CAISSE_CONVENTION:
                    process = createListesInternesProcess(vb, CAISSE_CONVENTION);
                    break;
                default:
                    throw new AssertionError(vb.getGenre() + " is not recognized!");
            }
            BProcessLauncher.start(process);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private BProcess createListesInternesProcess(PTListesInternesViewBean vb, ExportMode mode) {
        ListesInternesProcess process = new ListesInternesProcess();
        process.setEMailAddress(vb.getEmail());
        process.setAnnee(vb.getAnnee());
        process.setOmitTO(!vb.isAvecTo());
        process.setMode(mode);
        return process;
    }

    private BProcess createRecapitulatifGenreCaisseProcess(PTListesInternesViewBean vb) {
        RecapitulatifParGenreCaisseProcess process = new RecapitulatifParGenreCaisseProcess();
        process.setEMailAddress(vb.getEmail());
        process.setAnnee(vb.getAnnee());
        process.setOmitTO(!vb.isAvecTo());
        return process;
    }
}
