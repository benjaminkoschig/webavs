/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers.step3;

import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseSourciersCalculDroits implements JadeProcessStepInterface {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface#getEntityHandler()
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new AMProcessRepriseSourciersEntityHandler();
    }

}
