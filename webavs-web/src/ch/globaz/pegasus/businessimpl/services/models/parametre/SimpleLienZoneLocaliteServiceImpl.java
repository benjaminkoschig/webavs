package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocaliteSearch;
import ch.globaz.pegasus.business.services.models.parametre.SimpleLienZoneLocaliteService;
import ch.globaz.pegasus.businessimpl.checkers.parametre.SimpleLienZoneLocaliteChecker;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class SimpleLienZoneLocaliteServiceImpl implements SimpleLienZoneLocaliteService {

    @Override
    public int count(SimpleLienZoneLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleLienZoneLocalite create(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (simpleLienZoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to create simpleLienZoneLocalite, the model passed is null!");
        }
        SimpleLienZoneLocaliteChecker.checkForCreate(simpleLienZoneLocalite);
        return (SimpleLienZoneLocalite) JadePersistenceManager.add(simpleLienZoneLocalite);
    }

    @Override
    public SimpleLienZoneLocalite delete(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (simpleLienZoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to delete simpleLienZoneLocalite, the model passed is null!");
        }

        return (SimpleLienZoneLocalite) JadePersistenceManager.delete(simpleLienZoneLocalite);
    }

    @Override
    public SimpleLienZoneLocalite read(String idSimpleLienZoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (idSimpleLienZoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to read idSimpleLienZoneLocalite, the model passed is null!");
        }
        SimpleLienZoneLocalite simpleLienZoneLocalite = new SimpleLienZoneLocalite();
        simpleLienZoneLocalite.setId(idSimpleLienZoneLocalite);
        return (SimpleLienZoneLocalite) JadePersistenceManager.read(simpleLienZoneLocalite);

    }

    @Override
    public SimpleLienZoneLocaliteSearch search(SimpleLienZoneLocaliteSearch simpleLienZoneLocaliteSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (simpleLienZoneLocaliteSearch == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to search simpleLienZoneLocaliteSearch, the model passed is null!");
        }
        return (SimpleLienZoneLocaliteSearch) JadePersistenceManager.search(simpleLienZoneLocaliteSearch);
    }

    @Override
    public SimpleLienZoneLocalite update(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (simpleLienZoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to update simpleLienZoneLocalite, the model passed is null!");
        }
        SimpleLienZoneLocaliteChecker.checkForCreate(simpleLienZoneLocalite);
        return (SimpleLienZoneLocalite) JadePersistenceManager.update(simpleLienZoneLocalite);
    }

}
