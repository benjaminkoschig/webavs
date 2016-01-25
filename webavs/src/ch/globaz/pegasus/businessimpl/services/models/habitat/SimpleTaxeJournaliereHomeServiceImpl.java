package ch.globaz.pegasus.businessimpl.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.SimpleTaxeJournaliereHomeService;
import ch.globaz.pegasus.businessimpl.checkers.habitat.SimpleTaxeJournaliereHomeChecker;

public class SimpleTaxeJournaliereHomeServiceImpl extends PegasusServiceLocator implements
        SimpleTaxeJournaliereHomeService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleTaxeJournaliereHomeService
     * #create(ch.globaz.pegasus.business.models. habitat.SimpleTaxeJournaliereHome)
     */
    @Override
    public SimpleTaxeJournaliereHome create(SimpleTaxeJournaliereHome simpleTaxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException {
        if (simpleTaxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to create simpleTaxeJournaliereHome, the model passed is null!");
        }
        SimpleTaxeJournaliereHomeChecker.checkForCreate(simpleTaxeJournaliereHome);
        return (SimpleTaxeJournaliereHome) JadePersistenceManager.add(simpleTaxeJournaliereHome);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleTaxeJournaliereHomeService
     * #delete(ch.globaz.pegasus.business.models. habitat.SimpleTaxeJournaliereHome)
     */
    @Override
    public SimpleTaxeJournaliereHome delete(SimpleTaxeJournaliereHome simpleTaxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException {
        if (simpleTaxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to delete simpleTaxeJournaliereHome, the model passed is null!");
        }
        SimpleTaxeJournaliereHomeChecker.checkForDelete(simpleTaxeJournaliereHome);
        return (SimpleTaxeJournaliereHome) JadePersistenceManager.delete(simpleTaxeJournaliereHome);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat.
     * SimpleTaxeJournaliereHomeService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleTaxeJournaliereHomeSearch search = new SimpleTaxeJournaliereHomeSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleTaxeJournaliereHomeService#read(java.lang.String)
     */
    @Override
    public SimpleTaxeJournaliereHome read(String idSimpleTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException {
        if (idSimpleTaxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to read idSimpleTaxeJournaliereHome, the model passed is null!");
        }
        SimpleTaxeJournaliereHome simpleTaxeJournaliereHome = new SimpleTaxeJournaliereHome();
        simpleTaxeJournaliereHome.setId(idSimpleTaxeJournaliereHome);
        return (SimpleTaxeJournaliereHome) JadePersistenceManager.read(simpleTaxeJournaliereHome);
    }

    @Override
    public SimpleTaxeJournaliereHomeSearch search(SimpleTaxeJournaliereHomeSearch simpleTaxesJournaliereSearch)
            throws TaxeJournaliereHomeException, JadePersistenceException {
        // TODO Auto-generated method stub
        if (simpleTaxesJournaliereSearch == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to search simpleTaxeJournaliereHome, the serach model passed is null!");
        }
        return (SimpleTaxeJournaliereHomeSearch) JadePersistenceManager.search(simpleTaxesJournaliereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleTaxeJournaliereHomeService
     * #update(ch.globaz.pegasus.business.models. habitat.SimpleTaxeJournaliereHome)
     */
    @Override
    public SimpleTaxeJournaliereHome update(SimpleTaxeJournaliereHome simpleTaxeJournaliereHome)
            throws TaxeJournaliereHomeException, DonneeFinanciereException, JadePersistenceException {
        if (simpleTaxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to update simpleTaxeJournaliereHome, the model passed is null!");
        }
        SimpleTaxeJournaliereHomeChecker.checkForUpdate(simpleTaxeJournaliereHome);
        return (SimpleTaxeJournaliereHome) JadePersistenceManager.update(simpleTaxeJournaliereHome);
    }

}
