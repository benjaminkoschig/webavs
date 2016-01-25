package ch.globaz.amal.process.repriseRecalculs.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

public class AMProcessRepriseRecalculs implements JadeProcessStepAfterable, JadeProcessStepBeforable,
        JadeProcessStepInterface {

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub

    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new AMProcessRepriseRecalculsEntityHandler();
    }

}
