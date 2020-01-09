package globaz.cygnus.services.secutel;

import ch.globaz.jade.process.business.bean.JadeProcessStep;
import globaz.cygnus.process.RFGenererRecapDemandesSecutelProcess;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;

import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * @author mbo / 01.07.2013
 * 
 */

public class RFGenererRecapDemandesSecutelService {

    /**
     * Methode qui appel le process de g�n�ration du document.
     */
    public void generateDocument(String emailAdress, boolean isMiseEnGed, FWMemoryLog memoryLog,
            Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitementMap, BSession session,
            JadeProcessStep step, String numAf) throws Exception {

        // Cr�ation d'une instance du process
        RFGenererRecapDemandesSecutelProcess process = new RFGenererRecapDemandesSecutelProcess(emailAdress, isMiseEnGed,
                regroupementDemandesParCodeTraitementMap, memoryLog, session, step, numAf);

        // Lancement du process
        process.run();

    }

}
