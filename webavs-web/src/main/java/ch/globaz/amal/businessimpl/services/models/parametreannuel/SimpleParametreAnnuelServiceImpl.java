/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.parametreannuel;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService;
import ch.globaz.amal.businessimpl.checkers.parametreannuel.SimpleParametreAnnuelChecker;

/**
 * @author CBU
 * 
 */
public class SimpleParametreAnnuelServiceImpl implements SimpleParametreAnnuelService {

    @Override
    public void copyParams(String yearToCopy, String newYear) throws ParametreAnnuelException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        if (JadeStringUtil.isBlankOrZero(yearToCopy)) {
            JadeThread.logError(this.getClass().getName(), "L'année source doit être renseigné !");
            throw new ParametreAnnuelException("L'année source doit être renseigné !");
        }

        if (JadeStringUtil.isBlankOrZero(newYear)) {
            JadeThread.logError(this.getClass().getName(), "L'année de destination doit être renseigné !");
            throw new ParametreAnnuelException("L'année de destination doit être renseigné !");
        }

        SimpleParametreAnnuelSearch search = new SimpleParametreAnnuelSearch();
        search.setForAnneeParametre(newYear);
        if (count(search) > 0) {
            JadeThread.logError("SimpleParametreAnnuelServiceImpl.copyParams()", "L'année " + newYear
                    + " existe déjà !");
            throw new ParametreAnnuelException("L'année " + newYear + " existe déjà !");
        }

        search = new SimpleParametreAnnuelSearch();
        search.setForAnneeParametre(yearToCopy);
        if (count(search) == 0) {
            JadeThread.logError("SimpleParametreAnnuelServiceImpl.copyParams()", "L'année " + yearToCopy
                    + " n'existe pas!");
            throw new ParametreAnnuelException("L'année " + yearToCopy + " n'existe pas!");
        }

        SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
        simpleParametreAnnuelSearch.setForAnneeParametre(yearToCopy);
        simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService()
                .search(simpleParametreAnnuelSearch);

        for (int iParamSearch = 0; iParamSearch < simpleParametreAnnuelSearch.getSize(); iParamSearch++) {
            SimpleParametreAnnuel newParam = new SimpleParametreAnnuel();
            SimpleParametreAnnuel simpleParametreAnnuel = (SimpleParametreAnnuel) simpleParametreAnnuelSearch
                    .getSearchResults()[iParamSearch];
            newParam.setAnneeParametre(newYear);
            newParam.setCodeTypeParametre(simpleParametreAnnuel.getCodeTypeParametre());
            newParam.setValeurParametre(simpleParametreAnnuel.getValeurParametre());
            newParam = AmalServiceLocator.getParametreAnnuelService().create(newParam);
        }
    }

    @Override
    public int count(SimpleParametreAnnuelSearch search) throws ParametreAnnuelException, JadePersistenceException {
        if (search == null) {
            throw new ParametreAnnuelException("Unable to search parametreAnnuel, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService#create(ch.globaz.amal.business
     * .models.parametreannuel.SimpleParametreAnnuel)
     */
    @Override
    public SimpleParametreAnnuel create(SimpleParametreAnnuel simpleParametreAnnuel) throws ParametreAnnuelException,
            JadePersistenceException {
        if (simpleParametreAnnuel == null) {
            throw new ParametreAnnuelException("Unable to create parametre annuel, the model passed is null");
        }
        SimpleParametreAnnuelChecker.checkForCreate(simpleParametreAnnuel);
        return (SimpleParametreAnnuel) JadePersistenceManager.add(simpleParametreAnnuel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService#delete(ch.globaz.amal.business
     * .models.parametreannuel.SimpleParametreAnnuel)
     */
    @Override
    public SimpleParametreAnnuel delete(SimpleParametreAnnuel simpleParametreAnnuel) throws JadePersistenceException,
            ParametreAnnuelException {
        if (simpleParametreAnnuel == null) {
            throw new ParametreAnnuelException("Unable to delete parametre annuel, the model passed is null");
        }
        SimpleParametreAnnuelChecker.checkForDelete(simpleParametreAnnuel);
        return (SimpleParametreAnnuel) JadePersistenceManager.delete(simpleParametreAnnuel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService#read(java.lang.String)
     */
    @Override
    public SimpleParametreAnnuel read(String idParametreAnnuel) throws JadePersistenceException,
            ParametreAnnuelException {
        if (JadeStringUtil.isBlankOrZero(idParametreAnnuel)) {
            throw new ParametreAnnuelException("Unable to read parametre annuel, the model passed is null");
        }
        SimpleParametreAnnuel parametreAnnuel = new SimpleParametreAnnuel();
        parametreAnnuel.setId(idParametreAnnuel);
        return (SimpleParametreAnnuel) JadePersistenceManager.read(parametreAnnuel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService#search(ch.globaz.amal.business
     * .models.parametreannuel.SimpleParametreAnnuelSearch)
     */
    @Override
    public SimpleParametreAnnuelSearch search(SimpleParametreAnnuelSearch parametreAnnuelSearch)
            throws JadePersistenceException, ParametreAnnuelException {
        if (parametreAnnuelSearch == null) {
            throw new ParametreAnnuelException("Unable to search parametre annuel, the search model passed is null");
        }
        return (SimpleParametreAnnuelSearch) JadePersistenceManager.search(parametreAnnuelSearch);
    }

    @Override
    public SimpleParametreAnnuel searchAJAX(String year, String type) throws JadePersistenceException,
            ParametreAnnuelException {
        SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
        simpleParametreAnnuelSearch.setForAnneeParametre(year);
        simpleParametreAnnuelSearch.setForCodeTypeParametre(type);

        simpleParametreAnnuelSearch = (SimpleParametreAnnuelSearch) JadePersistenceManager
                .search(simpleParametreAnnuelSearch);
        SimpleParametreAnnuel simpleParametreAnnuel = new SimpleParametreAnnuel();

        if (simpleParametreAnnuelSearch.getSize() > 0) {
            simpleParametreAnnuel = (SimpleParametreAnnuel) simpleParametreAnnuelSearch.getSearchResults()[0];
        } else {
            throw new ParametreAnnuelException("Parametre annuel not found ! Year : " + year + " / codeType : " + type);
        }
        return simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService#update(ch.globaz.amal.business
     * .models.parametreannuel.SimpleParametreAnnuel)
     */
    @Override
    public SimpleParametreAnnuel update(SimpleParametreAnnuel simpleParametreAnnuel) throws ParametreAnnuelException,
            JadePersistenceException {
        if (simpleParametreAnnuel == null) {
            throw new ParametreAnnuelException("Unable to update parametre annuel, the model passed is null");
        }
        SimpleParametreAnnuelChecker.checkForUpdate(simpleParametreAnnuel);
        return (SimpleParametreAnnuel) JadePersistenceManager.update(simpleParametreAnnuel);
    }
}
