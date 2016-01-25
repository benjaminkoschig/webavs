package ch.globaz.pegasus.businessimpl.services.models.dessaisissement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementRevenuSearch;
import ch.globaz.pegasus.business.services.models.dessaisissement.SimpleDessaisissementRevenuService;
import ch.globaz.pegasus.businessimpl.checkers.dessaisissement.SimpleDessaisissementRevenuChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleDessaisissementRevenuServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleDessaisissementRevenuService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleDessaisissementRevenuService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleDessaisissementRevenu)
     */
    @Override
    public SimpleDessaisissementRevenu create(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException {
        if (simpleDessaisissementRevenu == null) {
            throw new DessaisissementRevenuException(
                    "Unable to create simpleDessaisissementRevenu, the model passed is null!");
        }
        SimpleDessaisissementRevenuChecker.checkForCreate(simpleDessaisissementRevenu);
        return (SimpleDessaisissementRevenu) JadePersistenceManager.add(simpleDessaisissementRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleDessaisissementRevenuService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleDessaisissementRevenu)
     */
    @Override
    public SimpleDessaisissementRevenu delete(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws DessaisissementRevenuException, JadePersistenceException {
        if (simpleDessaisissementRevenu == null) {
            throw new DessaisissementRevenuException(
                    "Unable to delete simpleDessaisissementRevenu, the model passed is null!");
        }
        if (simpleDessaisissementRevenu.isNew()) {
            throw new DessaisissementRevenuException(
                    "Unable to delete simpleDessaisissementRevenu, the model passed is new!");
        }
        SimpleDessaisissementRevenuChecker.checkForDelete(simpleDessaisissementRevenu);
        return (SimpleDessaisissementRevenu) JadePersistenceManager.delete(simpleDessaisissementRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.dessaisissement.
     * SimpleDessaisissementRevenuService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleDessaisissementRevenuSearch search = new SimpleDessaisissementRevenuSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleDessaisissementRevenuService#read(java.lang.String)
     */
    @Override
    public SimpleDessaisissementRevenu read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException {
        if (JadeStringUtil.isEmpty(idDessaisissementRevenu)) {
            throw new DessaisissementRevenuException(
                    "Unable to read simpleDessaisissementRevenu, the id passed is not defined!");
        }
        SimpleDessaisissementRevenu simpleDessaisissementRevenu = new SimpleDessaisissementRevenu();
        simpleDessaisissementRevenu.setId(idDessaisissementRevenu);
        return (SimpleDessaisissementRevenu) JadePersistenceManager.read(simpleDessaisissementRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleDessaisissementRevenuService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleDessaisissementRevenu)
     */
    @Override
    public SimpleDessaisissementRevenu update(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException {
        if (simpleDessaisissementRevenu == null) {
            throw new DessaisissementRevenuException(
                    "Unable to update simpleDessaisissementRevenu, the model passed is null!");
        }
        if (simpleDessaisissementRevenu.isNew()) {
            throw new DessaisissementRevenuException(
                    "Unable to update simpleDessaisissementRevenu, the model passed is new!");
        }
        SimpleDessaisissementRevenuChecker.checkForUpdate(simpleDessaisissementRevenu);
        return (SimpleDessaisissementRevenu) JadePersistenceManager.update(simpleDessaisissementRevenu);
    }

}
