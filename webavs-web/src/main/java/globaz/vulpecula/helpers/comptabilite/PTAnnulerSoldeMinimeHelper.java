package globaz.vulpecula.helpers.comptabilite;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.vulpecula.vb.comptabilite.PTAnnulerSoldeMinimeViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.process.comptabilite.AnnulerSoldeMinimeProcess;

/**
 * @since WebBMS 3.1
 * 
 */
public class PTAnnulerSoldeMinimeHelper extends FWHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTAnnulerSoldeMinimeViewBean vb = (PTAnnulerSoldeMinimeViewBean) viewBean;

            AnnulerSoldeMinimeProcess process = new AnnulerSoldeMinimeProcess();
            process.setJournalLibelle(vb.getLibelle());
            process.setTypeMembre(vb.getTypeMembre());
            process.setJournalDate(vb.getDateJournal());
            process.setMontantMinime(vb.getMontantMinime());
            process.setSimulation(vb.isSimulation());
            process.setSession(new BSession(session));
            process.setEMailAddress(vb.getEmail());

            BProcessLauncher.start(process);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    };
}
