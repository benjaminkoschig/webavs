/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.subsideannee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.exceptions.models.subsideannee.SubsideAnneeException;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.subsideannee.SimpleSubsideAnneeService;
import ch.globaz.amal.businessimpl.checkers.subsideannee.SimpleSubsideAnneeChecker;

/**
 * @author CBU
 * 
 */
public class SimpleSubsideAnneeServiceImpl implements SimpleSubsideAnneeService {

    @Override
    public void copyParams(String yearToCopy, String newYear) throws SubsideAnneeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        if (JadeStringUtil.isBlankOrZero(yearToCopy)) {
            JadeThread.logError(this.getClass().getName(), "L'année source doit être renseigné !");
            throw new SubsideAnneeException("L'année source doit être renseigné !");
        }

        if (JadeStringUtil.isBlankOrZero(newYear)) {
            JadeThread.logError(this.getClass().getName(), "L'année de destination doit être renseigné !");
            throw new SubsideAnneeException("L'année de destination doit être renseigné !");
        }

        SimpleSubsideAnneeSearch search = new SimpleSubsideAnneeSearch();
        search.setForAnneeSubside(newYear);
        if (count(search) > 0) {
            JadeThread.logError("SimpleSubsideAnneeServiceImpl.copyParams()", "L'année " + newYear + " existe déjà !");
            throw new SubsideAnneeException("L'année " + newYear + " existe déjà !");
        }

        search = new SimpleSubsideAnneeSearch();
        search.setForAnneeSubside(yearToCopy);
        if (count(search) == 0) {
            JadeThread.logError("SimpleSubsideAnneeServiceImpl.copyParams()", "L'année " + yearToCopy
                    + " n'existe pas!");
            throw new SubsideAnneeException("L'année " + yearToCopy + " n'existe pas!");
        }

        SimpleSubsideAnneeSearch simpleSubsideAnneeSearch = new SimpleSubsideAnneeSearch();
        simpleSubsideAnneeSearch.setForAnneeSubside(yearToCopy);
        simpleSubsideAnneeSearch = AmalServiceLocator.getSimpleSubsideAnneeService().search(simpleSubsideAnneeSearch);

        for (int iSubsideSearch = 0; iSubsideSearch < simpleSubsideAnneeSearch.getSize(); iSubsideSearch++) {
            SimpleSubsideAnnee newSubside = new SimpleSubsideAnnee();
            SimpleSubsideAnnee simpleSubsideAnnee = (SimpleSubsideAnnee) simpleSubsideAnneeSearch.getSearchResults()[iSubsideSearch];
            newSubside.setAnneeSubside(newYear);
            newSubside.setLimiteRevenu(simpleSubsideAnnee.getLimiteRevenu());
            newSubside.setSubsideAdo(simpleSubsideAnnee.getSubsideAdo());
            newSubside.setSubsideAdulte(simpleSubsideAnnee.getSubsideAdulte());
            newSubside.setSubsideEnfant(simpleSubsideAnnee.getSubsideEnfant());
            newSubside.setSubsideSalarie1618(simpleSubsideAnnee.getSubsideSalarie1618());
            newSubside.setSubsideSalarie1925(simpleSubsideAnnee.getSubsideSalarie1925());
            newSubside = AmalServiceLocator.getSimpleSubsideAnneeService().create(newSubside);
        }
    }

    @Override
    public int count(SimpleSubsideAnneeSearch search) throws SubsideAnneeException, JadePersistenceException {
        if (search == null) {
            throw new SubsideAnneeException("Unable to search subsideAnnee, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.subsideAnnee.SimpleSubsideAnneeService#create(ch.globaz.amal.business
     * .models.subsideAnnee.SimpleSubsideAnnee)
     */
    @Override
    public SimpleSubsideAnnee create(SimpleSubsideAnnee simpleSubsideAnnee) throws SubsideAnneeException,
            JadePersistenceException {
        if (simpleSubsideAnnee == null) {
            throw new SubsideAnneeException("Unable to create subsideAnnee, the model passed is null!");
        }
        SimpleSubsideAnneeChecker.checkForCreate(simpleSubsideAnnee);
        return (SimpleSubsideAnnee) JadePersistenceManager.add(simpleSubsideAnnee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.subsideAnnee.SimpleSubsideAnneeService#delete(ch.globaz.amal.business
     * .models.subsideAnnee.SimpleSubsideAnnee)
     */
    @Override
    public SimpleSubsideAnnee delete(SimpleSubsideAnnee simpleSubsideAnnee) throws JadePersistenceException,
            SubsideAnneeException {
        if (simpleSubsideAnnee == null) {
            throw new SubsideAnneeException("Unable to delete simpleSubsideAnnee, the model passed is null!");
        }
        SimpleSubsideAnneeChecker.checkForDelete(simpleSubsideAnnee);
        return (SimpleSubsideAnnee) JadePersistenceManager.delete(simpleSubsideAnnee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.subsideAnnee.SimpleSubsideAnneeService#read(java.lang.String)
     */
    @Override
    public SimpleSubsideAnnee read(String idSubsideAnnee) throws JadePersistenceException, SubsideAnneeException {
        if (JadeStringUtil.isEmpty(idSubsideAnnee)) {
            throw new SubsideAnneeException("Unable to read contribuable, the id passed is not defined!");
        }
        SimpleSubsideAnnee subsideAnnee = new SimpleSubsideAnnee();
        subsideAnnee.setId(idSubsideAnnee);
        return (SimpleSubsideAnnee) JadePersistenceManager.read(subsideAnnee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.subsideAnnee.SimpleSubsideAnneeService#search(ch.globaz.amal.business
     * .models.subsideAnnee.SimpleSubsideAnneeSearch)
     */
    @Override
    public SimpleSubsideAnneeSearch search(SimpleSubsideAnneeSearch subsideAnneeSearch)
            throws JadePersistenceException, SubsideAnneeException {
        if (subsideAnneeSearch == null) {
            throw new SubsideAnneeException("Unable to search subsideAnnee, the search model passed is null!");
        }
        return (SimpleSubsideAnneeSearch) JadePersistenceManager.search(subsideAnneeSearch);
    }

    @Override
    public ArrayList<SimpleSubsideAnnee> searchAJAX(String year) throws JadePersistenceException, SubsideAnneeException {
        SimpleSubsideAnneeSearch simpleSubsideAnneeSearch = new SimpleSubsideAnneeSearch();
        simpleSubsideAnneeSearch.setForAnneeSubside(year);
        simpleSubsideAnneeSearch.setOrderKey("limiteRevenuAsc");

        simpleSubsideAnneeSearch = (SimpleSubsideAnneeSearch) JadePersistenceManager.search(simpleSubsideAnneeSearch);
        ArrayList<SimpleSubsideAnnee> arraySubsides = new ArrayList<SimpleSubsideAnnee>();
        for (JadeAbstractModel model : simpleSubsideAnneeSearch.getSearchResults()) {
            SimpleSubsideAnnee simpleSubsideAnnee = (SimpleSubsideAnnee) model;
            arraySubsides.add(simpleSubsideAnnee);
        }

        return arraySubsides;
    }

    @Override
    public SimpleSubsideAnnee update(SimpleSubsideAnnee simpleSubsideAnnee) throws SubsideAnneeException,
            JadePersistenceException {
        if (simpleSubsideAnnee == null) {
            throw new SubsideAnneeException("Unable to update subsideAnnee, the search model passed is null!");
        }
        SimpleSubsideAnneeChecker.checkForUpdate(simpleSubsideAnnee);
        return (SimpleSubsideAnnee) JadePersistenceManager.update(simpleSubsideAnnee);
    }

}
