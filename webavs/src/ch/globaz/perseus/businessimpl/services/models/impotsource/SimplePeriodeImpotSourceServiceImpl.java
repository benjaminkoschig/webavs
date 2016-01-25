package ch.globaz.perseus.businessimpl.services.models.impotsource;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.impotsource.PeriodeImpotSourceException;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSourceSearchModel;
import ch.globaz.perseus.business.services.models.impotsource.SimplePeriodeImpotSourceService;
import ch.globaz.perseus.businessimpl.checkers.impotsource.SimplePeriodeImpotSourceChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimplePeriodeImpotSourceServiceImpl extends PerseusAbstractServiceImpl implements
        SimplePeriodeImpotSourceService {

    @Override
    public int count(SimplePeriodeImpotSourceSearchModel search) throws PeriodeImpotSourceException,
            JadePersistenceException {
        if (search == null) {
            throw new PeriodeImpotSourceException(
                    "Unable to count periodeImpotSource, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimplePeriodeImpotSource create(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException {
        if (periodeImpotSource == null) {
            throw new PeriodeImpotSourceException(
                    "Unable to create a simplePeriodeImpotSource, the model passed is null");
        }
        SimplePeriodeImpotSourceChecker.checkForCreate(periodeImpotSource);
        return (SimplePeriodeImpotSource) JadePersistenceManager.add(periodeImpotSource);
    }

    @Override
    public SimplePeriodeImpotSource delete(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException {
        if (periodeImpotSource == null) {
            throw new PeriodeImpotSourceException(
                    "Unable to delete a simplePeriodeImpotSource, the model passed is null");
        }
        if (periodeImpotSource.isNew()) {
            throw new PeriodeImpotSourceException(
                    "Unable to delete a simplePeriodeImpotSource, the model passed is null");
        }
        SimplePeriodeImpotSourceChecker.checkForDelete(periodeImpotSource);
        return (SimplePeriodeImpotSource) JadePersistenceManager.delete(periodeImpotSource);
    }

    @Override
    public SimplePeriodeImpotSource read(String idPeriodeImpotSource) throws PeriodeImpotSourceException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idPeriodeImpotSource)) {
            throw new PeriodeImpotSourceException("Unable to read a simplePeriodeImpotSource, the id passed is null!");
        }
        SimplePeriodeImpotSource periodeIp = new SimplePeriodeImpotSource();
        periodeIp.setId(idPeriodeImpotSource);
        return (SimplePeriodeImpotSource) JadePersistenceManager.read(periodeIp);
    }

    @Override
    public SimplePeriodeImpotSourceSearchModel search(SimplePeriodeImpotSourceSearchModel searchMOdel)
            throws PeriodeImpotSourceException, JadePersistenceException {
        if (searchMOdel == null) {
            throw new PeriodeImpotSourceException(
                    "Unable to search a simplePeriodeImpotSource, the search model passed is null!");
        }

        return (SimplePeriodeImpotSourceSearchModel) JadePersistenceManager.search(searchMOdel);
    }

    @Override
    public SimplePeriodeImpotSource update(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException {
        if (periodeImpotSource == null) {
            throw new PeriodeImpotSourceException(
                    "Unable to update a simplePeriodeImpotSource, the model passed is null");
        }
        if (periodeImpotSource.isNew()) {
            throw new PeriodeImpotSourceException(
                    "Unable to update a simplePeriodeImpotSource, the model passed is null");
        }
        SimplePeriodeImpotSourceChecker.checkForUpdate(periodeImpotSource);
        return (SimplePeriodeImpotSource) JadePersistenceManager.update(periodeImpotSource);
    }

}
