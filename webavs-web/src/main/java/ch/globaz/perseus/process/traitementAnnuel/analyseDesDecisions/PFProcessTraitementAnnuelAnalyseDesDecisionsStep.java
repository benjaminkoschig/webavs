package ch.globaz.perseus.process.traitementAnnuel.analyseDesDecisions;

import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

public class PFProcessTraitementAnnuelAnalyseDesDecisionsStep implements JadeProcessStepInterface {

    /**
     * Retourne le handler servant � manipuler les entit�s
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PFProcessTraitementAnnuelAnalyseDesDecisionsEntityHandler();
    }

}
