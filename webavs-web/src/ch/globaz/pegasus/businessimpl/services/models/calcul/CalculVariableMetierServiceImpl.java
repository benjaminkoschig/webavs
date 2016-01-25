/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculVariableMetier;
import ch.globaz.pegasus.business.models.calcul.CalculVariableMetierSearch;
import ch.globaz.pegasus.business.services.models.calcul.CalculVariableMetierService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author ECO
 * 
 */
public class CalculVariableMetierServiceImpl extends PegasusAbstractServiceImpl implements CalculVariableMetierService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculVariableMetierService #
     * count(ch.globaz.pegasus.business.models.calcul.CalculVariableMetierSearch )
     */
    @Override
    public int count(CalculVariableMetierSearch search) throws CalculException, JadePersistenceException {
        if (search == null) {
            throw new CalculException("Unable to count calculVariableMetier, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculVariableMetierService #read(java.lang.String)
     */
    @Override
    public CalculVariableMetier read(String idCalculVariableMetier) throws JadePersistenceException, CalculException {
        if (JadeStringUtil.isEmpty(idCalculVariableMetier)) {
            throw new CalculException("Unable to read calculVariableMetier, the id passed is null!");
        }
        CalculVariableMetier calculVarMet = new CalculVariableMetier();
        calculVarMet.setId(idCalculVariableMetier);
        return (CalculVariableMetier) JadePersistenceManager.read(calculVarMet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculVariableMetierService #
     * search(ch.globaz.pegasus.business.models.calcul.CalculVariableMetierSearch )
     */
    @Override
    public CalculVariableMetierSearch search(CalculVariableMetierSearch calculVariableMetierSearch)
            throws JadePersistenceException, CalculException {
        if (calculVariableMetierSearch == null) {
            throw new CalculException("Unable to search calculVariableMetier, the search model passed is null!");
        }
        return (CalculVariableMetierSearch) JadePersistenceManager.search(calculVariableMetierSearch);
    }

}
