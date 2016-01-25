/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistoriqueSearch;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuHistoriqueService;
import ch.globaz.amal.businessimpl.checkers.revenu.SimpleRevenuHistoriqueChecker;

/**
 * @author dhi
 * 
 */
public class SimpleRevenuHistoriqueServiceImpl implements SimpleRevenuHistoriqueService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuHistoriqueService#create(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuHistorique)
     */
    @Override
    public SimpleRevenuHistorique create(SimpleRevenuHistorique simpleRevenuHistorique)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuHistorique == null) {
            throw new RevenuException("Unable to create simpleRevenuHistorique, the model passed is null!");
        }
        SimpleRevenuHistoriqueChecker.checkForCreate(simpleRevenuHistorique);
        return (SimpleRevenuHistorique) JadePersistenceManager.add(simpleRevenuHistorique);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuHistoriqueService#delete(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuHistorique)
     */
    @Override
    public SimpleRevenuHistorique delete(SimpleRevenuHistorique simpleRevenuHistorique) throws RevenuException,
            JadePersistenceException {
        if (simpleRevenuHistorique == null) {
            throw new RevenuException("Unable to delete simpleRevenuHistorique, the model passed is null!");
        }
        SimpleRevenuHistoriqueChecker.checkForDelete(simpleRevenuHistorique);
        return (SimpleRevenuHistorique) JadePersistenceManager.update(simpleRevenuHistorique);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.SimpleRevenuHistoriqueService#read(java.lang.String)
     */
    @Override
    public SimpleRevenuHistorique read(String idRevenu) throws RevenuException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRevenu)) {
            throw new RevenuException("Unable to read simplerevenuhistorique, the id passed is null!");
        }
        SimpleRevenuHistorique simpleRevenuHistorique = new SimpleRevenuHistorique();
        simpleRevenuHistorique.setId(idRevenu);
        return (SimpleRevenuHistorique) JadePersistenceManager.read(simpleRevenuHistorique);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuHistoriqueService#search(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuHistoriqueSearch)
     */
    @Override
    public SimpleRevenuHistoriqueSearch search(SimpleRevenuHistoriqueSearch search) throws JadePersistenceException,
            RevenuException {
        if (search == null) {
            throw new RevenuException("Unable to search simpleRevenuHistorique, the model passed is null!");
        }
        return (SimpleRevenuHistoriqueSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuHistoriqueService#update(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuHistorique)
     */
    @Override
    public SimpleRevenuHistorique update(SimpleRevenuHistorique simpleRevenuHistorique) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuHistorique == null) {
            throw new RevenuException("Unable to update simpleRevenuHistorique, the model passed is null!");
        }
        SimpleRevenuHistoriqueChecker.checkForUpdate(simpleRevenuHistorique);
        return (SimpleRevenuHistorique) JadePersistenceManager.update(simpleRevenuHistorique);
    }

}
