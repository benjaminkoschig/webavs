package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.ZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.ZoneLocaliteSearch;
import ch.globaz.pegasus.business.services.models.parametre.ZoneLocaliteService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class ZoneLocaliteServiceImpl extends PegasusAbstractServiceImpl implements ZoneLocaliteService {

    @Override
    public int count(ZoneLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public ZoneLocalite create(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (zoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to create zoneLocalite, the model passed is null!");
        }
        SimpleLienZoneLocalite simpleLienZoneLocalite = PegasusImplServiceLocator.getSimpleLienZoneLocaliteService()
                .create(zoneLocalite.getSimpleLienZoneLocalite());
        zoneLocalite.setSimpleLienZoneLocalite(simpleLienZoneLocalite);
        return zoneLocalite;
    }

    @Override
    public ZoneLocalite delete(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (zoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to delete zoneLocalite, the model passed is null!");
        }
        SimpleLienZoneLocalite simpleLienZoneLocalite = PegasusImplServiceLocator.getSimpleLienZoneLocaliteService()
                .delete(zoneLocalite.getSimpleLienZoneLocalite());
        zoneLocalite.setSimpleLienZoneLocalite(simpleLienZoneLocalite);
        return zoneLocalite;
    }

    @Override
    public ZoneLocalite read(String idZoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (idZoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to read idZoneLocalite, the model passed is null!");
        }
        ZoneLocalite zoneLocalite = new ZoneLocalite();
        zoneLocalite.setId(idZoneLocalite);
        return (ZoneLocalite) JadePersistenceManager.read(zoneLocalite);
    }

    @Override
    public ZoneLocaliteSearch search(ZoneLocaliteSearch zoneLocaliteSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (zoneLocaliteSearch == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to search zoneLocaliteSearch, the model passed is null!");
        }

        if (!JadeStringUtil.isEmpty(zoneLocaliteSearch.getForDateValable())) {
            zoneLocaliteSearch.setWhereKey(ZoneLocaliteSearch.WITH_DATE_VALABLE_LE);
        }

        return (ZoneLocaliteSearch) JadePersistenceManager.search(zoneLocaliteSearch);
    }

    @Override
    public ZoneLocalite update(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (zoneLocalite == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to update zoneLocalite, the model passed is null!");
        }
        SimpleLienZoneLocalite simpleLienZoneLocalite = zoneLocalite.getSimpleLienZoneLocalite();
        PegasusImplServiceLocator.getSimpleLienZoneLocaliteService().update(simpleLienZoneLocalite);
        return zoneLocalite;
    }

}
