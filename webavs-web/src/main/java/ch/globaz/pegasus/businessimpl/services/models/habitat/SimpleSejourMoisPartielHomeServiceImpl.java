package ch.globaz.pegasus.businessimpl.services.models.habitat;

import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.SimpleSejourMoisPartielHomeService;
import ch.globaz.pegasus.businessimpl.checkers.habitat.SimpleSejourMoisPartielHomeChecker;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

import java.util.List;

public class SimpleSejourMoisPartielHomeServiceImpl extends PegasusServiceLocator implements
        SimpleSejourMoisPartielHomeService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleSejourMoisPartielHomeService
     * #create(ch.globaz.pegasus.business.models. habitat.SimpleSejourMoisPartielHome)
     */
    @Override
    public SimpleSejourMoisPartielHome create(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException {
        if (simpleSejourMoisPartielHome == null) {
            throw new SejourMoisPartielHomeException(
                    "Unable to create simpleSejourMoisPartielHome, the model passed is null!");
        }
        SimpleSejourMoisPartielHomeChecker.checkForCreate(simpleSejourMoisPartielHome);
        return (SimpleSejourMoisPartielHome) JadePersistenceManager.add(simpleSejourMoisPartielHome);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleSejourMoisPartielHomeService
     * #delete(ch.globaz.pegasus.business.models. habitat.SimpleSejourMoisPartielHome)
     */
    @Override
    public SimpleSejourMoisPartielHome delete(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException {
        if (simpleSejourMoisPartielHome == null) {
            throw new SejourMoisPartielHomeException(
                    "Unable to delete simpleSejourMoisPartielHome, the model passed is null!");
        }
        SimpleSejourMoisPartielHomeChecker.checkForDelete(simpleSejourMoisPartielHome);
        return (SimpleSejourMoisPartielHome) JadePersistenceManager.delete(simpleSejourMoisPartielHome);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat.
     * SimpleSejourMoisPartielHomeService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleSejourMoisPartielHomeSearch search = new SimpleSejourMoisPartielHomeSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleSejourMoisPartielHomeService#read(java.lang.String)
     */
    @Override
    public SimpleSejourMoisPartielHome read(String idSimpleSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException {
        if (idSimpleSejourMoisPartielHome == null) {
            throw new SejourMoisPartielHomeException(
                    "Unable to read idSimpleSejourMoisPartielHome, the model passed is null!");
        }
        SimpleSejourMoisPartielHome simpleSejourMoisPartielHome = new SimpleSejourMoisPartielHome();
        simpleSejourMoisPartielHome.setId(idSimpleSejourMoisPartielHome);
        return (SimpleSejourMoisPartielHome) JadePersistenceManager.read(simpleSejourMoisPartielHome);
    }

    @Override
    public SimpleSejourMoisPartielHomeSearch search(SimpleSejourMoisPartielHomeSearch simpleTaxesJournaliereSearch)
            throws SejourMoisPartielHomeException, JadePersistenceException {
        if (simpleTaxesJournaliereSearch == null) {
            throw new SejourMoisPartielHomeException(
                    "Unable to search simpleSejourMoisPartielHome, the serach model passed is null!");
        }
        return (SimpleSejourMoisPartielHomeSearch) JadePersistenceManager.search(simpleTaxesJournaliereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.habitat. SimpleSejourMoisPartielHomeService
     * #update(ch.globaz.pegasus.business.models. habitat.SimpleSejourMoisPartielHome)
     */
    @Override
    public SimpleSejourMoisPartielHome update(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome)
            throws SejourMoisPartielHomeException, DonneeFinanciereException, JadePersistenceException {
        if (simpleSejourMoisPartielHome == null) {
            throw new SejourMoisPartielHomeException(
                    "Unable to update simpleSejourMoisPartielHome, the model passed is null!");
        }
        SimpleSejourMoisPartielHomeChecker.checkForUpdate(simpleSejourMoisPartielHome);
        return (SimpleSejourMoisPartielHome) JadePersistenceManager.update(simpleSejourMoisPartielHome);
    }

}
