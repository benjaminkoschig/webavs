package ch.globaz.perseus.process.traitementAdaptation.calculDesDecisions;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFProcessTraitementAdaptationCalculStep implements JadeProcessStepInterface, JadeProcessStepBeforable {

    private String moisProchainPaiement;

    /**
     * Retourne le handler servant à manipuler les entités
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PFProcessTraitementAdaptationCalculEntityHandler(moisProchainPaiement);
    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        moisProchainPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

    }

}
