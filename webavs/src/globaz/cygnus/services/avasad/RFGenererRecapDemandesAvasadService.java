package globaz.cygnus.services.avasad;

import globaz.cygnus.process.RFGenererRecapDemandesAvasadProcess;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;

/**
 * 
 * @author mbo / 01.07.2013
 * 
 */

public class RFGenererRecapDemandesAvasadService {

    /**
     * Methode qui appel le process de génération du document.
     */
    public void generateDocument(String emailAdress, boolean isMiseEnGed, FWMemoryLog memoryLog,
            Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitementMap, BSession session,
            JadeProcessStep step, String numAf) throws Exception {

        // Création d'une instance du process
        RFGenererRecapDemandesAvasadProcess process = new RFGenererRecapDemandesAvasadProcess(emailAdress, isMiseEnGed,
                regroupementDemandesParCodeTraitementMap, memoryLog, session, step, numAf);

        // Lancement du process
        process.run();

    }

}
