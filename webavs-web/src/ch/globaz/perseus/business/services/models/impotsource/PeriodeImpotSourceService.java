package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.PeriodeImpotSourceException;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSourceSearchModel;

public interface PeriodeImpotSourceService extends JadeApplicationService {

    public int count(PeriodeImpotSourceSearchModel search) throws PeriodeImpotSourceException, JadePersistenceException;

    public PeriodeImpotSource create(PeriodeImpotSource periode) throws PeriodeImpotSourceException,
            JadePersistenceException;

    public PeriodeImpotSource delete(PeriodeImpotSource periode) throws PeriodeImpotSourceException,
            JadePersistenceException;

    public PeriodeImpotSource read(String idPeriodeImpotSource) throws PeriodeImpotSourceException,
            JadePersistenceException;

    public PeriodeImpotSourceSearchModel search(PeriodeImpotSourceSearchModel periodeSearch)
            throws JadePersistenceException, PeriodeImpotSourceException;

    public PeriodeImpotSource update(PeriodeImpotSource periode) throws PeriodeImpotSourceException,
            JadePersistenceException;

}
