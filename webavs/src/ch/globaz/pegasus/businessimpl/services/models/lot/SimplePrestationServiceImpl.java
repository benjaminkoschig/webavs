package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;
import ch.globaz.pegasus.business.services.models.lot.SimplePrestationService;
import ch.globaz.pegasus.businessimpl.checkers.lot.SimplePrestationChecker;

public class SimplePrestationServiceImpl implements SimplePrestationService {

    @Override
    public int count(SimplePrestationSearch search) throws PrestationException, JadePersistenceException {
        if (search == null) {
            throw new PrestationException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public ArrayList<String> getIdsPrestationsByLot(String idLot) throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (idLot == null) {
            throw new PrestationException("Unable to search prestation for idLot, the idLot passed is null!");
        }

        SimplePrestationSearch search = new SimplePrestationSearch();
        search.setForIdLot(idLot);
        search = (SimplePrestationSearch) JadePersistenceManager.search(search);

        ArrayList<String> idsPrestations = new ArrayList<String>();

        for (JadeAbstractModel p : search.getSearchResults()) {
            SimplePrestation prestation = (SimplePrestation) p;
            idsPrestations.add(prestation.getIdPrestation());
        }

        return idsPrestations;
    }

    @Override
    public SimplePrestation create(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simplePrestation == null) {
            throw new PrestationException("Unable to create simplePresation, the model passed is null!");
        }
        SimplePrestationChecker.checkForCreate(simplePrestation);
        return (SimplePrestation) JadePersistenceManager.add(simplePrestation);
    }

    @Override
    public SimplePrestation delete(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException {
        if (simplePrestation == null) {
            throw new PrestationException("Unable to delete simplePresation, the model passed is null!");
        }

        return (SimplePrestation) JadePersistenceManager.delete(simplePrestation);
    }

    @Override
    public int delete(SimplePrestationSearch simplePrestationSearch) throws PrestationException,
            JadePersistenceException {
        if (simplePrestationSearch == null) {
            throw new PrestationException("Unable to delete simplePrestation, the model passed is null!");
        }

        return JadePersistenceManager.delete(simplePrestationSearch);
    }

    @Override
    public SimplePrestation read(String idPrestation) throws PrestationException, JadePersistenceException {
        if (idPrestation == null) {
            throw new PrestationException("Unable to read idPrestation, the model passed is null!");
        }
        SimplePrestation simplePrestation = new SimplePrestation();
        simplePrestation.setId(idPrestation);
        return (SimplePrestation) JadePersistenceManager.read(simplePrestation);

    }

    @Override
    public SimplePrestationSearch search(SimplePrestationSearch simplePrestationSearch) throws PrestationException,
            JadePersistenceException {
        if (simplePrestationSearch == null) {
            throw new PrestationException("Unable to search simplePresationSearch, the model passed is null!");
        }
        return (SimplePrestationSearch) JadePersistenceManager.search(simplePrestationSearch);
    }

    @Override
    public SimplePrestation update(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException {
        if (simplePrestation == null) {
            throw new PrestationException("Unable to update simplePresation, the model passed is null!");
        }

        return (SimplePrestation) JadePersistenceManager.update(simplePrestation);
    }

}
