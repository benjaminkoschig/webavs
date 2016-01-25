package globaz.vulpecula.helpers.comptabilite;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.vulpecula.vb.comptabilite.PTImportationcashinViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.process.comptabilite.ImportationCashInProcess;
import ch.globaz.vulpecula.process.comptabilite.ImportationCashInProcess.TypeImportation;
import com.sun.star.lang.IllegalArgumentException;

/**
 * 
 * @since WebBMS 1.0
 */
public class PTImportationcashinHelper extends FWHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTImportationcashinViewBean vb = (PTImportationcashinViewBean) viewBean;

            if (vb.getImportFilename() == null || vb.getImportFilename().isEmpty()) {
                throw new IllegalArgumentException("Aucun fichier renseigné !");
            }

            ImportationCashInProcess process = new ImportationCashInProcess();
            process.setJournalLibelle(vb.getLibelle());
            process.setJournalDate(vb.getDateJournal());
            process.setCsvFilename(vb.getImportFilename());
            process.setEMailAddress(vb.getEmail());

            TypeImportation typeImportation = TypeImportation.valueOf(vb.getTypeImportation());
            process.setTypeImportation(typeImportation);

            process.setSession(new BSession(session));
            BProcessLauncher.start(process);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    };
}
