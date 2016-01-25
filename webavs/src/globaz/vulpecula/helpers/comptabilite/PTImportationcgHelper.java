package globaz.vulpecula.helpers.comptabilite;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.comptabilite.PTImportationcgViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.process.comptabilite.ImportationCGProcess;
import com.sun.star.lang.IllegalArgumentException;

/**
 * 
 * @since WebBMS 1.0
 */
public class PTImportationcgHelper extends FWHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTImportationcgViewBean vb = (PTImportationcgViewBean) viewBean;

            if (vb.getImportFilename() == null || vb.getImportFilename().isEmpty()) {
                throw new IllegalArgumentException("Aucun fichier renseigné !");
            }

            ImportationCGProcess process = new ImportationCGProcess();
            process.setJournalLibelle(vb.getLibelle());
            process.setJournalDate(vb.getDateJournal());
            process.setCsvFilename(vb.getImportFilename());
            process.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    };
}
