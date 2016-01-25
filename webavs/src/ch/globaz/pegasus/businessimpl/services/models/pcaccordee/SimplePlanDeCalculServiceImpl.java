package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePlanDeCalculService;
import ch.globaz.pegasus.businessimpl.checkers.pcaccordee.SimplePlanDeCalculChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimplePlanDeCalculServiceImpl extends PegasusAbstractServiceImpl implements SimplePlanDeCalculService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePlanDeCalculService
     * #count(ch.globaz.pegasus.business.models.pcaccordee .SimplePlanDeCalculSearch)
     */
    @Override
    public int count(SimplePlanDeCalculSearch search) throws PCAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PCAccordeeException("Unable to count simplePlanDeCalcul, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePlanDeCalculService
     * #create(ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul)
     */
    @Override
    public SimplePlanDeCalcul create(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException {
        if (simplePlanDeCalcul == null) {
            throw new PCAccordeeException("Unable to create simplePlanDeCalcul, the model passed is null!");
        }
        SimplePlanDeCalculChecker.checkForCreate(simplePlanDeCalcul);
        return (SimplePlanDeCalcul) JadePersistenceManager.add(simplePlanDeCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePlanDeCalculService
     * #delete(ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul)
     */
    @Override
    public SimplePlanDeCalcul delete(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException {
        if (simplePlanDeCalcul == null) {
            throw new PCAccordeeException("Unable to delete simplePlanDeCalcul, the model passed is null!");
        }
        if (simplePlanDeCalcul.isNew()) {
            throw new PCAccordeeException("Unable to delete simplePlanDeCalcul, the model passed is new!");
        }
        SimplePlanDeCalculChecker.checkForDelete(simplePlanDeCalcul);
        return (SimplePlanDeCalcul) JadePersistenceManager.delete(simplePlanDeCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePlanDeCalculService
     * #delete(ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul)
     */
    @Override
    public int delete(SimplePlanDeCalculSearch simplePlanDeCalculSearch) throws PCAccordeeException,
            JadePersistenceException {
        if (simplePlanDeCalculSearch == null) {
            throw new PCAccordeeException("Unable to delete simplePlanDeCalcul, the critere model passed is null!");
        }

        return JadePersistenceManager.delete(simplePlanDeCalculSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePlanDeCalculService#read(java.lang.String)
     */
    @Override
    public SimplePlanDeCalcul read(String idSimplePlanDeCalcul) throws PCAccordeeException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimplePlanDeCalcul)) {
            throw new PCAccordeeException("Unable to read simplePlanDeCalcul, the id passed is not defined!");
        }
        SimplePlanDeCalcul simplePlanDeCalcul = new SimplePlanDeCalcul();
        simplePlanDeCalcul.setId(idSimplePlanDeCalcul);
        return (SimplePlanDeCalcul) JadePersistenceManager.read(simplePlanDeCalcul);
    }

    @Override
    public SimplePlanDeCalcul readPlanRetenuForIdPca(String idPca) throws JadePersistenceException, PCAccordeeException {
        SimplePlanDeCalculSearch search = new SimplePlanDeCalculSearch();
        search.setForIdPCAccordee(idPca);
        search.setForIsPlanRetenu(Boolean.TRUE);
        search = (SimplePlanDeCalculSearch) JadePersistenceManager.search(search, true);

        if ((search.getSearchResults().length > 1) || (search.getSearchResults().length == 0)) {
            throw new PCAccordeeException("The search results doesn't return exactly one result: "
                    + search.getSearchResults().length + " result(s) returned");
        }

        return (SimplePlanDeCalcul) search.getSearchResults()[0];

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePlanDeCalculService
     * #search(ch.globaz.pegasus.business.models.pcaccordee .SimplePlanDeCalculSearch)
     */
    @Override
    public SimplePlanDeCalculSearch search(SimplePlanDeCalculSearch simplePlanDeCalculSearch)
            throws JadePersistenceException, PCAccordeeException {
        if (simplePlanDeCalculSearch == null) {
            throw new PCAccordeeException("Unable to search simplePlanDeCalcul, the search model passed is null!");
        }
        return (SimplePlanDeCalculSearch) JadePersistenceManager.search(simplePlanDeCalculSearch);

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePlanDeCalculService
     * #update(ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul)
     */
    @Override
    public SimplePlanDeCalcul update(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException {
        if (simplePlanDeCalcul == null) {
            throw new PCAccordeeException("Unable to update simplePlanDeCalcul, the model passed is null!");
        }
        if (simplePlanDeCalcul.isNew()) {
            throw new PCAccordeeException("Unable to update simplePlanDeCalcul, the model passed is new!");
        }
        SimplePlanDeCalculChecker.checkForUpdate(simplePlanDeCalcul);
        return (SimplePlanDeCalcul) JadePersistenceManager.update(simplePlanDeCalcul);
    }

}
