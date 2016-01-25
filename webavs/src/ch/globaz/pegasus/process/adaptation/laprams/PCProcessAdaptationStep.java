package ch.globaz.pegasus.process.adaptation.laprams;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepEnable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.pegasus.business.constantes.EPCProperties;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepEnable {

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationEntityHandler();
    }

    @Override
    public Boolean isEnabled(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {
            return true;
        } else {
            return false;
        }
    }

}
