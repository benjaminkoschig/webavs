package ch.globaz.perseus.businessimpl.checkers.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimpleMembreFamilleChecker extends PerseusAbstractChecker {
    /**
     * @param membreFamille
     */
    public static void checkForCreate(SimpleMembreFamille membreFamille) {
        SimpleMembreFamilleChecker.checkMandatory(membreFamille);
    }

    /**
     * @param membreFamille
     */
    public static void checkForDelete(SimpleMembreFamille membreFamille) {

    }

    /**
     * @param membreFamille
     */
    public static void checkForUpdate(SimpleMembreFamille membreFamille) {
        SimpleMembreFamilleChecker.checkMandatory(membreFamille);
    }

    /**
     * @param membreFamille
     */
    private static void checkMandatory(SimpleMembreFamille membreFamille) {
        if (JadeStringUtil.isEmpty(membreFamille.getIdTiers())) {
            JadeThread.logError(SimpleMembreFamille.class.getName(), "perseus.simplemembrefamille.idTiers.mandatory");
        }
    }

}
