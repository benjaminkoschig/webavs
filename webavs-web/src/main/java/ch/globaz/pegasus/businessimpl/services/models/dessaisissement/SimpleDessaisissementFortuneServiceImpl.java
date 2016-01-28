package ch.globaz.pegasus.businessimpl.services.models.dessaisissement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortuneSearch;
import ch.globaz.pegasus.business.services.models.dessaisissement.SimpleDessaisissementFortuneService;
import ch.globaz.pegasus.businessimpl.checkers.dessaisissement.SimpleDessaisissementFortuneChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleDessaisissementFortuneServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleDessaisissementFortuneService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleDessaisissementFortuneService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleDessaisissementFortune)
     */
    @Override
    public SimpleDessaisissementFortune create(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException {
        if (simpleDessaisissementFortune == null) {
            throw new DessaisissementFortuneException(
                    "Unable to create simpleDessaisissementFortune, the model passed is null!");
        }
        SimpleDessaisissementFortuneChecker.checkForCreate(simpleDessaisissementFortune);
        return (SimpleDessaisissementFortune) JadePersistenceManager.add(simpleDessaisissementFortune);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleDessaisissementFortuneService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleDessaisissementFortune)
     */
    @Override
    public SimpleDessaisissementFortune delete(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws DessaisissementFortuneException, JadePersistenceException {
        if (simpleDessaisissementFortune == null) {
            throw new DessaisissementFortuneException(
                    "Unable to delete simpleDessaisissementFortune, the model passed is null!");
        }
        if (simpleDessaisissementFortune.isNew()) {
            throw new DessaisissementFortuneException(
                    "Unable to delete simpleDessaisissementFortune, the model passed is new!");
        }
        SimpleDessaisissementFortuneChecker.checkForDelete(simpleDessaisissementFortune);
        return (SimpleDessaisissementFortune) JadePersistenceManager.delete(simpleDessaisissementFortune);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.dessaisissement. SimpleDessaisissementFortuneService
     * #deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleDessaisissementFortuneSearch search = new SimpleDessaisissementFortuneSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleDessaisissementFortuneService#read(java.lang.String)
     */
    @Override
    public SimpleDessaisissementFortune read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException {
        if (JadeStringUtil.isEmpty(idDessaisissementFortune)) {
            throw new DessaisissementFortuneException(
                    "Unable to read simpleDessaisissementFortune, the id passed is not defined!");
        }
        SimpleDessaisissementFortune simpleDessaisissementFortune = new SimpleDessaisissementFortune();
        simpleDessaisissementFortune.setId(idDessaisissementFortune);
        return (SimpleDessaisissementFortune) JadePersistenceManager.read(simpleDessaisissementFortune);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleDessaisissementFortuneService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleDessaisissementFortune)
     */
    @Override
    public SimpleDessaisissementFortune update(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException {
        if (simpleDessaisissementFortune == null) {
            throw new DessaisissementFortuneException(
                    "Unable to update simpleDessaisissementFortune, the model passed is null!");
        }
        if (simpleDessaisissementFortune.isNew()) {
            throw new DessaisissementFortuneException(
                    "Unable to update simpleDessaisissementFortune, the model passed is new!");
        }
        SimpleDessaisissementFortuneChecker.checkForUpdate(simpleDessaisissementFortune);
        return (SimpleDessaisissementFortune) JadePersistenceManager.update(simpleDessaisissementFortune);
    }

}
