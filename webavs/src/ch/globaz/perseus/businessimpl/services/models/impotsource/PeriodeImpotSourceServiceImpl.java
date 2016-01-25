package ch.globaz.perseus.businessimpl.services.models.impotsource;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.impotsource.PeriodeImpotSourceException;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSourceSearchModel;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSource;
import ch.globaz.perseus.business.services.models.impotsource.PeriodeImpotSourceService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PeriodeImpotSourceServiceImpl extends PerseusAbstractServiceImpl implements PeriodeImpotSourceService {

    @Override
    public int count(PeriodeImpotSourceSearchModel search) throws PeriodeImpotSourceException, JadePersistenceException {
        if (search == null) {
            throw new PeriodeImpotSourceException(
                    "Unable to count PeriodeImpotSource, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public PeriodeImpotSource create(PeriodeImpotSource periode) throws PeriodeImpotSourceException,
            JadePersistenceException {
        if (periode == null) {
            throw new PeriodeImpotSourceException("Unable to create Periode, the model passed is null!");
        }
        SimplePeriodeImpotSource simplePeriodeIP = periode.getSimplePeriodeImpotSource();
        try {
            simplePeriodeIP = PerseusImplServiceLocator.getSimplePeriodeImpotSourceService().create(simplePeriodeIP);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeImpotSourceException("Service not available : " + e.toString(), e);
        }

        periode.setSimplePeriodeImpotSource(simplePeriodeIP);

        return periode;
    }

    @Override
    public PeriodeImpotSource delete(PeriodeImpotSource periode) throws PeriodeImpotSourceException,
            JadePersistenceException {
        if ((periode == null) || periode.isNew()) {
            throw new PeriodeImpotSourceException("Unable to create Periode, the model passed is null or new!");
        }

        try {
            SimplePeriodeImpotSource simplePeriodeIp = periode.getSimplePeriodeImpotSource();
            simplePeriodeIp = PerseusImplServiceLocator.getSimplePeriodeImpotSourceService().delete(simplePeriodeIp);
            periode.setSimplePeriodeImpotSource(simplePeriodeIp);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeImpotSourceException("Service not available : " + e.toString(), e);
        }

        return periode;
    }

    @Override
    public PeriodeImpotSource read(String idPeriodeImpotSource) throws PeriodeImpotSourceException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idPeriodeImpotSource)) {
            throw new PeriodeImpotSourceException("Unable to read SimplePeriodeImpotSource, the id passed is empty !");
        }
        PeriodeImpotSource periode = new PeriodeImpotSource();
        periode.setId(idPeriodeImpotSource);

        return (PeriodeImpotSource) JadePersistenceManager.read(periode);
    }

    @Override
    public PeriodeImpotSourceSearchModel search(PeriodeImpotSourceSearchModel periodeSearch)
            throws JadePersistenceException, PeriodeImpotSourceException {
        if (periodeSearch == null) {
            throw new PeriodeImpotSourceException("Unable to count Periode, the search model is null");
        }

        if (!JadeStringUtil.isEmpty(periodeSearch.getForDuDateDebut())
                || !JadeStringUtil.isEmpty(periodeSearch.getForAuDateFin())) {
            periodeSearch.setWhereKey("withPeriode");
        }

        return (PeriodeImpotSourceSearchModel) JadePersistenceManager.search(periodeSearch);

    }

    @Override
    public PeriodeImpotSource update(PeriodeImpotSource periode) throws PeriodeImpotSourceException,
            JadePersistenceException {
        if ((periode == null) || periode.isNew()) {
            throw new PeriodeImpotSourceException("Unable to update Periode, the model passed is null or new!");
        }
        try {

            PerseusImplServiceLocator.getSimplePeriodeImpotSourceService()
                    .update(periode.getSimplePeriodeImpotSource());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeImpotSourceException("Service not available : " + e.toString(), e);
        }

        return periode;
    }

}
