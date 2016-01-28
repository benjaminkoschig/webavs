package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.home.SimplePrixChambre;
import ch.globaz.pegasus.business.models.home.SimplePrixChambreSearch;
import ch.globaz.pegasus.business.services.models.home.SimplePrixChambreService;
import ch.globaz.pegasus.businessimpl.checkers.home.SimplePrixChambreChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author BSC
 */
public class SimplePrixChambreServiceImpl extends PegasusAbstractServiceImpl implements SimplePrixChambreService {

    public SimplePrixChambre closeAnterieurPeriodes(SimplePrixChambre prixChambreToClose, SimplePrixChambre prixChambre)
            throws JadePersistenceException, PrixChambreException {

        if (prixChambreToClose != null) {
            prixChambreToClose.setDateFin(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
                    "01." + prixChambre.getDateDebut(), -1)));

            JadePersistenceManager.update(prixChambreToClose);
        }

        return prixChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimplePrixChambreService
     * #count(ch.globaz.pegasus.business.models.home.SimplePrixChambreSearch)
     */
    @Override
    public int count(SimplePrixChambreSearch search) throws PrixChambreException, JadePersistenceException {
        if (search == null) {
            throw new PrixChambreException("Unable to count prixChambres, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimplePrixChambreService
     * #create(ch.globaz.pegasus.business.models.home.SimplePrixChambre)
     */
    @Override
    public SimplePrixChambre create(SimplePrixChambre simplePrixChambre) throws PrixChambreException,
            JadePersistenceException {
        if (simplePrixChambre == null) {
            throw new PrixChambreException("Unable to create prixChambre, the model passed is null!");
        }

        // close anterieur periods
        closeAnterieurPeriodes(getExistantPeriod(simplePrixChambre), simplePrixChambre);

        SimplePrixChambreChecker.checkForCreate(simplePrixChambre);

        return (SimplePrixChambre) JadePersistenceManager.add(simplePrixChambre);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimplePrixChambreService
     * #delete(ch.globaz.pegasus.business.models.home.SimplePrixChambre)
     */
    @Override
    public SimplePrixChambre delete(SimplePrixChambre simplePrixChambre) throws PrixChambreException,
            JadePersistenceException {
        if (simplePrixChambre == null) {
            throw new PrixChambreException("Unable to delete simplePrix, the model passed is null!");
        }
        if (simplePrixChambre.isNew()) {
            throw new PrixChambreException("Unable to delete simplePrix, the model passed is new!");
        }

        SimplePrixChambreChecker.checkForDelete(simplePrixChambre);
        return (SimplePrixChambre) JadePersistenceManager.delete(simplePrixChambre);
    }

    private SimplePrixChambre getExistantPeriod(SimplePrixChambre simplePrixChambre) throws PrixChambreException,
            JadePersistenceException {
        // Recherche si période ouverte pour le même type de prix de chambre
        SimplePrixChambreSearch simplePrixChambreSearch = new SimplePrixChambreSearch();
        simplePrixChambreSearch.setForIdTypeChambre(simplePrixChambre.getIdTypeChambre());
        simplePrixChambreSearch.setForIdPrixChambre(simplePrixChambre.getId());
        simplePrixChambreSearch.setForDateDebutBefore(simplePrixChambre.getDateDebut());
        simplePrixChambreSearch.setWhereKey("checkForAnterieurPeriods");
        simplePrixChambreSearch = (SimplePrixChambreSearch) JadePersistenceManager.search(simplePrixChambreSearch);

        if (simplePrixChambreSearch.getSearchResults().length > 1) {
            throw new PrixChambreException("Problems with prixChambre periods, more than one periodes were open");
        } else if (simplePrixChambreSearch.getSearchResults().length == 1) {
            return (SimplePrixChambre) simplePrixChambreSearch.getSearchResults()[0];
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimplePrixChambreService #read(java.lang.String)
     */
    @Override
    public SimplePrixChambre read(String idSimplePrixChambre) throws PrixChambreException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimplePrixChambre)) {
            throw new PrixChambreException("Unable to read prixChambre, the id passed is not defined!");
        }
        SimplePrixChambre simplePrix = new SimplePrixChambre();
        simplePrix.setId(idSimplePrixChambre);
        return (SimplePrixChambre) JadePersistenceManager.read(simplePrix);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimplePrixChambreService
     * #update(ch.globaz.pegasus.business.models.home.SimplePrixChambre)
     */
    @Override
    public SimplePrixChambre update(SimplePrixChambre simplePrixChambre) throws PrixChambreException,
            JadePersistenceException {
        if (simplePrixChambre == null) {
            throw new PrixChambreException("Unable to update prixChambre, the model passed is null!");
        }
        if (simplePrixChambre.isNew()) {
            throw new PrixChambreException("Unable to update prixChambre, the model passed is new!");
        }
        // close anterieur periods
        // close anterieur periods
        closeAnterieurPeriodes(getExistantPeriod(simplePrixChambre), simplePrixChambre);

        SimplePrixChambreChecker.checkForUpdate(simplePrixChambre);
        return (SimplePrixChambre) JadePersistenceManager.update(simplePrixChambre);
    }
}
