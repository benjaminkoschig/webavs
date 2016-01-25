/**
 * 
 */
package ch.globaz.amal.process.repriseAdresses;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseAdressesPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<AMProcessRepriseAdressesEnum> {

    @Override
    public String getBusinessKey() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties#getEnumForProperties
     * ()
     */
    @Override
    public Class<AMProcessRepriseAdressesEnum> getEnumForProperties() {
        return AMProcessRepriseAdressesEnum.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface#getParametersForUrl
     * (ch.globaz.jade.process.business.models.process.JadeProcessEntity)
     */
    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface#searchPopulation()
     */
    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setProperties(Map arg0) {
        // TODO Auto-generated method stub

    }

}
