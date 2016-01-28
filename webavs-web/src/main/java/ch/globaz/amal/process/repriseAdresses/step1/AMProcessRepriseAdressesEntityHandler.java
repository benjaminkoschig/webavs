/**
 * 
 */
package ch.globaz.amal.process.repriseAdresses.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseAdressesEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#run()
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#setCurrentEntity(ch.globaz
     * .jade.process.business.models.process.JadeProcessEntity)
     */
    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties#setProperties(java.util
     * .Map)
     */
    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        // TODO Auto-generated method stub

    }

}
