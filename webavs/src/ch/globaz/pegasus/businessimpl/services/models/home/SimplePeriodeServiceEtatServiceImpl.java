package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.services.models.home.SimplePeriodeServiceEtatService;
import ch.globaz.pegasus.businessimpl.checkers.home.SimplePeriodeServiceEtatChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author BSC
 */
public class SimplePeriodeServiceEtatServiceImpl extends PegasusAbstractServiceImpl implements
        SimplePeriodeServiceEtatService {

    public SimplePeriodeServiceEtat closeAnterieurPeriodes(SimplePeriodeServiceEtat periodeServiceEtatToClose,
            SimplePeriodeServiceEtat periodeServiceEtat) throws JadePersistenceException, PeriodeServiceEtatException {

        if (periodeServiceEtatToClose != null) {
            periodeServiceEtatToClose.setDateFin(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
                    + periodeServiceEtat.getDateDebut(), -1)));

            JadePersistenceManager.update(periodeServiceEtatToClose);
        }

        return periodeServiceEtat;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.home. SimplePeriodeServiceEtatService
     * #count(ch.globaz.pegasus.business.models.home .SimplePeriodeServiceEtatSearch)
     */
    @Override
    public int count(SimplePeriodeServiceEtatSearch search) throws PeriodeServiceEtatException,
            JadePersistenceException {
        if (search == null) {
            throw new PeriodeServiceEtatException(
                    "Unable to count periodeServiceEtat, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.home. SimplePeriodeServiceEtatService
     * #create(ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat)
     */
    @Override
    public SimplePeriodeServiceEtat create(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException {

        if (simplePeriodeServiceEtat == null) {
            throw new PeriodeServiceEtatException(
                    "Unable to create simplePeriodeServiceEtat, the model passed is null!");
        }

        // close anterieur periods
        closeAnterieurPeriodes(getExistantPeriod(simplePeriodeServiceEtat), simplePeriodeServiceEtat);

        SimplePeriodeServiceEtatChecker.checkForCreate(simplePeriodeServiceEtat);
        return (SimplePeriodeServiceEtat) JadePersistenceManager.add(simplePeriodeServiceEtat);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.home. SimplePeriodeServiceEtatService
     * #delete(ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat)
     */
    @Override
    public SimplePeriodeServiceEtat delete(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException {
        if (simplePeriodeServiceEtat == null) {
            throw new PeriodeServiceEtatException(
                    "Unable to delete simplePeriodeServiceEtat, the model passed is null!");
        }
        if (simplePeriodeServiceEtat.isNew()) {
            throw new PeriodeServiceEtatException("Unable to delete simplePeriodeServiceEtat, the model passed is new!");
        }

        SimplePeriodeServiceEtatChecker.checkForDelete(simplePeriodeServiceEtat);
        return (SimplePeriodeServiceEtat) JadePersistenceManager.delete(simplePeriodeServiceEtat);
    }

    private SimplePeriodeServiceEtat getExistantPeriod(SimplePeriodeServiceEtat periodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException {
        // Recherche si période ouverte pour le même type de prix de chambre
        SimplePeriodeServiceEtatSearch simplePeriodeServiceEtatSearch = new SimplePeriodeServiceEtatSearch();
        simplePeriodeServiceEtatSearch.setForIdHome(periodeServiceEtat.getIdHome());
        simplePeriodeServiceEtatSearch.setForIdPeriodeServiceEtat(periodeServiceEtat.getIdSimplePeriodeServiceEtat());
        simplePeriodeServiceEtatSearch.setForDateDebutBefore(periodeServiceEtat.getDateDebut());
        simplePeriodeServiceEtatSearch.setWhereKey("checkForAnterieurPeriods");
        simplePeriodeServiceEtatSearch = (SimplePeriodeServiceEtatSearch) JadePersistenceManager
                .search(simplePeriodeServiceEtatSearch);

        if (simplePeriodeServiceEtatSearch.getSearchResults().length > 1) {
            throw new PeriodeServiceEtatException(
                    "Problems with periodeServiceEtat periods, more than one periodes were open");
        } else if (simplePeriodeServiceEtatSearch.getSearchResults().length == 1) {
            return (SimplePeriodeServiceEtat) simplePeriodeServiceEtatSearch.getSearchResults()[0];
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.home. SimplePeriodeServiceEtatService #read(java.lang.String)
     */
    @Override
    public SimplePeriodeServiceEtat read(String idSimplePeriodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimplePeriodeServiceEtat)) {
            throw new PeriodeServiceEtatException(
                    "Unable to read simplePeriodeServiceEtat, the id passed is not defined!");
        }
        SimplePeriodeServiceEtat simplePeriodeServiceEtat = new SimplePeriodeServiceEtat();
        simplePeriodeServiceEtat.setId(idSimplePeriodeServiceEtat);
        return (SimplePeriodeServiceEtat) JadePersistenceManager.read(simplePeriodeServiceEtat);
        // throw new PeriodeServiceEtatException("Debug purpose error");
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.home. SimplePeriodeServiceEtatService
     * #search(ch.globaz.pegasus.business.models.home .SimplePeriodeServiceEtatSearch)
     */
    @Override
    public SimplePeriodeServiceEtatSearch search(SimplePeriodeServiceEtatSearch simplePeriodeServiceEtatSearch)
            throws JadePersistenceException, PeriodeServiceEtatException {
        if (simplePeriodeServiceEtatSearch == null) {
            throw new PeriodeServiceEtatException(
                    "Unable to search simplePeriodeServiceEtat, the search model passed is null!");
        }
        return (SimplePeriodeServiceEtatSearch) JadePersistenceManager.search(simplePeriodeServiceEtatSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.home. SimplePeriodeServiceEtat
     * #update(ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat)
     */
    @Override
    public SimplePeriodeServiceEtat update(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException {
        if (simplePeriodeServiceEtat == null) {
            throw new PeriodeServiceEtatException(
                    "Unable to update simplePeriodeServiceEtat, the model passed is null!");
        }
        if (simplePeriodeServiceEtat.isNew()) {
            throw new PeriodeServiceEtatException("Unable to update simplePeriodeServiceEtat, the model passed is new!");
        }

        // close anterieur periods
        closeAnterieurPeriodes(getExistantPeriod(simplePeriodeServiceEtat), simplePeriodeServiceEtat);

        SimplePeriodeServiceEtatChecker.checkForUpdate(simplePeriodeServiceEtat);
        return (SimplePeriodeServiceEtat) JadePersistenceManager.update(simplePeriodeServiceEtat);
    }

}
