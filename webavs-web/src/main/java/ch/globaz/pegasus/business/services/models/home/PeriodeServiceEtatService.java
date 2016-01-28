package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtatSearch;

public interface PeriodeServiceEtatService extends JadeApplicationService {
    public int count(PeriodeServiceEtatSearch search) throws PeriodeServiceEtatException, JadePersistenceException;

    public PeriodeServiceEtat create(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException;

    public PeriodeServiceEtat delete(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException;

    public PeriodeServiceEtat read(String idPeriodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException;

    public PeriodeServiceEtatSearch search(PeriodeServiceEtatSearch periodeServiceEtatSearch)
            throws JadePersistenceException, PeriodeServiceEtatException;

    public PeriodeServiceEtat update(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException;
}
