/**
 * 
 */
package ch.globaz.amal.process.repriseAdresses.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseAdressesMutationContribuable implements JadeProcessStepBeforable, JadeProcessStepInterface {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable#before(ch.globaz.jade.process.
     * business.models.step.SimpleStep, java.util.Map)
     */
    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface#getEntityHandler()
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        // TODO Auto -generated method stub
        return null;
    }

}
