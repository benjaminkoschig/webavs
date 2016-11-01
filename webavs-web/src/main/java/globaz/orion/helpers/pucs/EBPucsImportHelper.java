package globaz.orion.helpers.pucs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.orion.process.EBTreatPucsFiles;
import globaz.orion.vb.pucs.EBPucsImportViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import ch.globaz.orion.business.models.pucs.PucsFile;

/**
 * Helper de lancement du process de traitement des fichiers PUCS
 * 
 * @author sco
 * 
 */
public class EBPucsImportHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        EBPucsImportViewBean vb = (EBPucsImportViewBean) viewBean;

        try {

            List<PucsFile> pucsEntrys = new ArrayList<PucsFile>();

            for (Entry<String, Collection<PucsFile>> entry : vb.getMapPucsByNumAffilie().entrySet()) {
                pucsEntrys.addAll(entry.getValue());
            }

            EBTreatPucsFiles process = new EBTreatPucsFiles();
            process.setSendCompletionMail(false);
            process.setEmailAdress(session.getUserEMail());
            process.setMode(vb.getMode());
            process.setIdMiseEnGed(vb.getIdMiseEnGed());
            process.setIdValidationDeLaDs(vb.getIdValidationDeLaDs());
            process.setPucsEntrys(pucsEntrys);
            process.setSession((BSession) session);
            process.setPucsToMerge(vb.getPucsToMerge());
            process.setName(((BSession) session).getLabel("DESCRIPTION_PROCESSUS_TRAITEMENT_PUCS"));
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }

    }
}
