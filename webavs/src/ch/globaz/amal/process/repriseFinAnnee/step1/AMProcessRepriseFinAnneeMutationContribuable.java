/**
 * 
 */
package ch.globaz.amal.process.repriseFinAnnee.step1;

import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseFinAnneeMutationContribuable implements JadeProcessStepInterface {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface#getEntityHandler()
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new AMProcessRepriseFinAnneeEntityHandler();
    }

}
