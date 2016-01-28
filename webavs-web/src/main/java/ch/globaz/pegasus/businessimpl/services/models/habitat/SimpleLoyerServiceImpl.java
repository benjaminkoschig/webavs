package ch.globaz.pegasus.businessimpl.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyer;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyerSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.SimpleLoyerService;
import ch.globaz.pegasus.businessimpl.checkers.habitat.SimpleLoyerChecker;

public class SimpleLoyerServiceImpl extends PegasusServiceLocator implements SimpleLoyerService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.habitat.SimpleLoyerService
     * #create(ch.globaz.pegasus.business.models.habitat.SimpleLoyer)
     */
    @Override
    public SimpleLoyer create(SimpleLoyer simpleLoyer) throws LoyerException, JadePersistenceException {
        if (simpleLoyer == null) {
            throw new LoyerException("Unable to create simpleLoyer, the model passed is null!");
        }
        SimpleLoyerChecker.checkForCreate(simpleLoyer);
        return (SimpleLoyer) JadePersistenceManager.add(simpleLoyer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.habitat.SimpleLoyerService
     * #delete(ch.globaz.pegasus.business.models.habitat.SimpleLoyer)
     */
    @Override
    public SimpleLoyer delete(SimpleLoyer simpleLoyer) throws LoyerException, JadePersistenceException {
        if (simpleLoyer == null) {
            throw new LoyerException("Unable to delete simpleLoyer, the model passed is null!");
        }
        SimpleLoyerChecker.checkForDelete(simpleLoyer);
        return (SimpleLoyer) JadePersistenceManager.delete(simpleLoyer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.habitat.SimpleLoyerService
     * #deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleLoyerSearch search = new SimpleLoyerSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.habitat.SimpleLoyerService #read(java.lang.String)
     */
    @Override
    public SimpleLoyer read(String idSimpleLoyer) throws LoyerException, JadePersistenceException {
        if (idSimpleLoyer == null) {
            throw new LoyerException("Unable to read idSimpleLoyer, the model passed is null!");
        }
        SimpleLoyer simpleLoyer = new SimpleLoyer();
        simpleLoyer.setId(idSimpleLoyer);
        return (SimpleLoyer) JadePersistenceManager.read(simpleLoyer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.habitat.SimpleLoyerService
     * #search(ch.globaz.pegasus.business.models.habitat.SimpleLoyerSearch)
     */
    @Override
    public SimpleLoyerSearch search(SimpleLoyerSearch loyerSearch) throws LoyerException, DonneeFinanciereException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.habitat.SimpleLoyerService
     * #update(ch.globaz.pegasus.business.models.habitat.SimpleLoyer)
     */
    @Override
    public SimpleLoyer update(SimpleLoyer simpleLoyer) throws LoyerException, DonneeFinanciereException,
            JadePersistenceException {
        if (simpleLoyer == null) {
            throw new LoyerException("Unable to update simpleLoyer, the model passed is null!");
        }
        SimpleLoyerChecker.checkForUpdate(simpleLoyer);
        return (SimpleLoyer) JadePersistenceManager.update(simpleLoyer);
    }

}
