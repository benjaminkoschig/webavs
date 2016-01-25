package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIjApg;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIjApgSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleIjApgService;
import ch.globaz.pegasus.businessimpl.checkers.renteijapi.SimpleIjApgChecker;

public class SimpleIjApgServiceImpl extends PegasusServiceLocator implements SimpleIjApgService {

    @Override
    public SimpleIjApg create(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException {
        if (simpleIjApg == null) {
            throw new IjApgException("Unable to create simpleIjApg, the model passed is null!");
        }
        SimpleIjApgChecker.checkForCreate(simpleIjApg);
        return (SimpleIjApg) JadePersistenceManager.add(simpleIjApg);
    }

    @Override
    public SimpleIjApg delete(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException {
        if (simpleIjApg == null) {
            throw new IjApgException("Unable to delete simpleIjApg, the model passed is null!");
        }
        SimpleIjApgChecker.checkForDelete(simpleIjApg);
        return (SimpleIjApg) JadePersistenceManager.delete(simpleIjApg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.renteijapi.SimpleIjApgService
     * #deleteParListeIdDoFinH(java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleIjApgSearch search = new SimpleIjApgSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);

    }

    @Override
    public SimpleIjApg read(String idSimpleIjApg) throws IjApgException, JadePersistenceException {
        if (idSimpleIjApg == null) {
            throw new IjApgException("Unable to read idSimpleIjApg, the model passed is null!");
        }
        SimpleIjApg ijApg = new SimpleIjApg();
        ijApg.setId(idSimpleIjApg);
        return (SimpleIjApg) JadePersistenceManager.read(ijApg);
    }

    @Override
    public SimpleIjApg update(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException {
        if (simpleIjApg == null) {
            throw new IjApgException("Unable to update simpleIjApg, the model passed is null!");
        }
        SimpleIjApgChecker.checkForUpdate(simpleIjApg);
        return (SimpleIjApg) JadePersistenceManager.update(simpleIjApg);
    }

}
