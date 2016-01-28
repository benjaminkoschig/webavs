package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.services.models.home.PeriodeServiceEtatService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class PeriodeServiceEtatServiceImpl extends PegasusAbstractServiceImpl implements PeriodeServiceEtatService {

    @Override
    public int count(PeriodeServiceEtatSearch search) throws PeriodeServiceEtatException, JadePersistenceException {
        if (search == null) {
            throw new PeriodeServiceEtatException("Unable to count periodes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public PeriodeServiceEtat create(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException {

        if (periodeServiceEtat == null) {
            throw new PeriodeServiceEtatException("Unable to create periode, the model passed is null!");
        }

        try {
            periodeServiceEtat.setSimplePeriodeServiceEtat(PegasusImplServiceLocator
                    .getSimplePeriodeServiceEtatService().create(periodeServiceEtat.getSimplePeriodeServiceEtat()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeServiceEtatException("Service not available - " + e.getMessage());
        }

        return periodeServiceEtat;
    }

    @Override
    public PeriodeServiceEtat delete(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException {
        if (periodeServiceEtat == null) {
            throw new PeriodeServiceEtatException("Unable to delete periode, the model passed is null!");
        }

        try {
            periodeServiceEtat.setSimplePeriodeServiceEtat(PegasusImplServiceLocator
                    .getSimplePeriodeServiceEtatService().delete(periodeServiceEtat.getSimplePeriodeServiceEtat()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeServiceEtatException("Service not available - " + e.getMessage());
        }

        return periodeServiceEtat;
    }

    @Override
    public PeriodeServiceEtat read(String idPeriodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idPeriodeServiceEtat)) {
            throw new PeriodeServiceEtatException("Unable to read periode, the id passed is null!");
        }
        PeriodeServiceEtat periode = new PeriodeServiceEtat();
        periode.setId(idPeriodeServiceEtat);
        return (PeriodeServiceEtat) JadePersistenceManager.read(periode);
    }

    @Override
    public PeriodeServiceEtatSearch search(PeriodeServiceEtatSearch periodeServiceEtatSearch)
            throws JadePersistenceException, PeriodeServiceEtatException {
        if (periodeServiceEtatSearch == null) {
            throw new PeriodeServiceEtatException("Unable to search periode, the search model passed is null!");
        }
        return (PeriodeServiceEtatSearch) JadePersistenceManager.search(periodeServiceEtatSearch);
    }

    @Override
    public PeriodeServiceEtat update(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException {
        if (periodeServiceEtat == null) {
            throw new PeriodeServiceEtatException("Unable to update periode, the model passed is null!");
        }

        try {
            periodeServiceEtat.setSimplePeriodeServiceEtat(PegasusImplServiceLocator
                    .getSimplePeriodeServiceEtatService().update(periodeServiceEtat.getSimplePeriodeServiceEtat()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeServiceEtatException("Service not available - " + e.getMessage());
        }

        return periodeServiceEtat;
    }
}
