/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.deductionsfiscalesenfants;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.deductionsFiscalesEnfants.DeductionsFiscalesEnfantsException;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService;

/**
 * @author CBU
 * 
 */
public class SimpleDeductionsFiscalesEnfantsServiceImpl implements SimpleDeductionsFiscalesEnfantsService {

    @Override
    public void copyParams(String yearToCopy, String newYear) throws DeductionsFiscalesEnfantsException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        if (JadeStringUtil.isBlankOrZero(yearToCopy)) {
            JadeThread.logError(this.getClass().getName(), "L'année source doit être renseigné !");
            throw new DeductionsFiscalesEnfantsException("L'année source doit être renseigné !");
        }

        if (JadeStringUtil.isBlankOrZero(newYear)) {
            JadeThread.logError(this.getClass().getName(), "L'année de destination doit être renseigné !");
            throw new DeductionsFiscalesEnfantsException("L'année de destination doit être renseigné !");
        }

        SimpleDeductionsFiscalesEnfantsSearch search = new SimpleDeductionsFiscalesEnfantsSearch();
        search.setForAnneeTaxation(newYear);
        if (count(search) > 0) {
            JadeThread.logError("SimpleParametreAnnuelServiceImpl.copyParams()", "L'année " + newYear
                    + " existe déjà !");
            throw new DeductionsFiscalesEnfantsException("L'année " + newYear + " existe déjà !");
        }

        search = new SimpleDeductionsFiscalesEnfantsSearch();
        search.setForAnneeTaxation(yearToCopy);
        if (count(search) == 0) {
            JadeThread.logError("SimpleParametreAnnuelServiceImpl.copyParams()", "L'année " + yearToCopy
                    + " n'existe pas!");
            throw new DeductionsFiscalesEnfantsException("L'année " + yearToCopy + " n'existe pas!");
        }

        SimpleDeductionsFiscalesEnfantsSearch simpleDeductionsFiscalesEnfantsSearch = new SimpleDeductionsFiscalesEnfantsSearch();
        simpleDeductionsFiscalesEnfantsSearch.setForAnneeTaxation(yearToCopy);
        simpleDeductionsFiscalesEnfantsSearch = AmalServiceLocator.getDeductionsFiscalesEnfantsService().search(
                simpleDeductionsFiscalesEnfantsSearch);

        for (int iParamSearch = 0; iParamSearch < simpleDeductionsFiscalesEnfantsSearch.getSize(); iParamSearch++) {
            SimpleDeductionsFiscalesEnfants newParam = new SimpleDeductionsFiscalesEnfants();
            SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants = (SimpleDeductionsFiscalesEnfants) simpleDeductionsFiscalesEnfantsSearch
                    .getSearchResults()[iParamSearch];
            newParam.setAnneeTaxation(newYear);
            newParam.setNbEnfant(simpleDeductionsFiscalesEnfants.getNbEnfant());
            newParam.setMontantDeductionParEnfant(simpleDeductionsFiscalesEnfants.getMontantDeductionParEnfant());
            newParam.setMontantDeductionTotal(simpleDeductionsFiscalesEnfants.getMontantDeductionTotal());
            newParam = AmalServiceLocator.getDeductionsFiscalesEnfantsService().create(newParam);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService#count
     * (ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch)
     */
    @Override
    public int count(SimpleDeductionsFiscalesEnfantsSearch search) throws DeductionsFiscalesEnfantsException,
            JadePersistenceException {
        if (search == null) {
            throw new DeductionsFiscalesEnfantsException(
                    "Unable to search deductionFisacleEnfant, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService#create
     * (ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants)
     */
    @Override
    public SimpleDeductionsFiscalesEnfants create(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws DeductionsFiscalesEnfantsException, JadePersistenceException {
        if (simpleDeductionsFiscalesEnfants == null) {
            throw new DeductionsFiscalesEnfantsException(
                    "Unable to create simpleDeductionsFiscalesEnfants, the search model passed is null!");
        }
        // SimplePrimeMoyenneChecker.checkForCreate(simplePrimeMoyenne);
        return (SimpleDeductionsFiscalesEnfants) JadePersistenceManager.add(simpleDeductionsFiscalesEnfants);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService#delete
     * (ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants)
     */
    @Override
    public SimpleDeductionsFiscalesEnfants delete(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws JadePersistenceException, DeductionsFiscalesEnfantsException {
        if (simpleDeductionsFiscalesEnfants == null) {
            throw new DeductionsFiscalesEnfantsException(
                    "Unable to delete simpleDeductionsFiscalesEnfants, the model passed is null!");
        }
        // SimplePrimeMoyenneChecker.checkForDelete(simpleDeductionsFiscalesEnfants);
        return (SimpleDeductionsFiscalesEnfants) JadePersistenceManager.delete(simpleDeductionsFiscalesEnfants);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService#read
     * (java.lang.String)
     */
    @Override
    public SimpleDeductionsFiscalesEnfants read(String idDeductionFiscaleEnfant) throws JadePersistenceException,
            DeductionsFiscalesEnfantsException {
        if (JadeStringUtil.isEmpty(idDeductionFiscaleEnfant)) {
            throw new DeductionsFiscalesEnfantsException(
                    "Unable to read simpleDeductionsFiscalesEnfants, the id passed is not defined!");
        }
        SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants = new SimpleDeductionsFiscalesEnfants();
        simpleDeductionsFiscalesEnfants.setId(idDeductionFiscaleEnfant);
        return (SimpleDeductionsFiscalesEnfants) JadePersistenceManager.read(simpleDeductionsFiscalesEnfants);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService#search
     * (ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsSearch)
     */
    @Override
    public SimpleDeductionsFiscalesEnfantsSearch search(
            SimpleDeductionsFiscalesEnfantsSearch simpleDeductionsFiscalesEnfantsSearch)
            throws JadePersistenceException, DeductionsFiscalesEnfantsException {
        if (simpleDeductionsFiscalesEnfantsSearch == null) {
            throw new DeductionsFiscalesEnfantsException(
                    "Unable to search simpleDeductionsFiscalesEnfantsSearch, the search model passed is null!");
        }
        return (SimpleDeductionsFiscalesEnfantsSearch) JadePersistenceManager
                .search(simpleDeductionsFiscalesEnfantsSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsService#update
     * (ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants)
     */
    @Override
    public SimpleDeductionsFiscalesEnfants update(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants)
            throws DeductionsFiscalesEnfantsException, JadePersistenceException {
        if (simpleDeductionsFiscalesEnfants == null) {
            throw new DeductionsFiscalesEnfantsException(
                    "Unable to update simpleDeductionsFiscalesEnfants, the search model passed is null!");
        }// Checker si 1 seul enregistrement avec colonne "Suivants identiques" à TRUE
         // SimplePrimeMoyenneChecker.checkForUpdate(simpleDeductionsFiscalesEnfants);
        return (SimpleDeductionsFiscalesEnfants) JadePersistenceManager.update(simpleDeductionsFiscalesEnfants);
    }

}
