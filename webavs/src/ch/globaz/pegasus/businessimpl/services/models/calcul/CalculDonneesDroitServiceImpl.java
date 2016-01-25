/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDernierePCASearch;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroit;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroitSearch;
import ch.globaz.pegasus.business.services.models.calcul.CalculDonneesDroitService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author ECO
 * 
 */
public class CalculDonneesDroitServiceImpl extends PegasusAbstractServiceImpl implements CalculDonneesDroitService {

    @Override
    public CalculDernierePCASearch calculDernierePCASearch(CalculDernierePCASearch calculDernierePCASearch)
            throws JadePersistenceException, CalculException {
        if (calculDernierePCASearch == null) {
            throw new CalculException("Unable to search calculDernierePCA, the search model passed is null!");
        }
        return (CalculDernierePCASearch) JadePersistenceManager.search(calculDernierePCASearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesDroitService
     * #count(ch.globaz.pegasus.business.models.calcul.CalculDonneesDroitSearch)
     */
    @Override
    public int count(CalculDonneesDroitSearch search) throws CalculException, JadePersistenceException {
        if (search == null) {
            throw new CalculException("Unable to count calculDonneesDroit, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesDroitService #read(java.lang.String)
     */
    @Override
    public CalculDonneesDroit read(String idCalculDonneesDroit) throws JadePersistenceException, CalculException {
        if (JadeStringUtil.isEmpty(idCalculDonneesDroit)) {
            throw new CalculException("Unable to read calculDonneesDroit, the id passed is null!");
        }
        CalculDonneesDroit calculDonneesDroit = new CalculDonneesDroit();
        calculDonneesDroit.setId(idCalculDonneesDroit);
        return (CalculDonneesDroit) JadePersistenceManager.read(calculDonneesDroit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculDonneesDroitService #
     * search(ch.globaz.pegasus.business.models.calcul.CalculDonneesDroitSearch)
     */
    @Override
    public CalculDonneesDroitSearch search(CalculDonneesDroitSearch calculDonneesDroitSearch)
            throws JadePersistenceException, CalculException {
        if (calculDonneesDroitSearch == null) {
            throw new CalculException("Unable to search calculDonneesDroit, the search model passed is null!");
        }
        return (CalculDonneesDroitSearch) JadePersistenceManager.search(calculDonneesDroitSearch);
    }

}
