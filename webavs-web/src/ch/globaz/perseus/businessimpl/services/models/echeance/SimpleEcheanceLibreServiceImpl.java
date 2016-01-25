package ch.globaz.perseus.businessimpl.services.models.echeance;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.EcheanceLibreException;
import ch.globaz.perseus.business.models.echeance.SimpleEcheanceLibre;
import ch.globaz.perseus.business.models.echeance.SimpleEcheanceLibreSearchModel;
import ch.globaz.perseus.business.services.models.echeance.SimpleEcheanceLibreService;
import ch.globaz.perseus.businessimpl.checkers.echeance.SimpleEcheanceLibreChecker;

public class SimpleEcheanceLibreServiceImpl implements SimpleEcheanceLibreService {

    @Override
    public int count(SimpleEcheanceLibreSearchModel searchModel) throws JadePersistenceException,
            EcheanceLibreException {
        if (searchModel == null) {
            throw new EcheanceLibreException("Unable to count simple echeance libre, the model passed is null");
        }
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public SimpleEcheanceLibre create(SimpleEcheanceLibre echeanceLibre) throws JadePersistenceException,
            EcheanceLibreException {
        if (echeanceLibre == null) {
            throw new EcheanceLibreException("Unable to create simple echeance libre, the model passed is null");
        }
        SimpleEcheanceLibreChecker.checkForCreate(echeanceLibre);
        return (SimpleEcheanceLibre) JadePersistenceManager.add(echeanceLibre);
    }

    @Override
    public SimpleEcheanceLibre delete(SimpleEcheanceLibre echeanceLibre) throws JadePersistenceException,
            EcheanceLibreException {
        if (echeanceLibre == null) {
            throw new EcheanceLibreException("Unable to delete simple echeance libre, the model passed is null");
        }
        return (SimpleEcheanceLibre) JadePersistenceManager.delete(echeanceLibre);
    }

    @Override
    public SimpleEcheanceLibre read(String idEcheanceLibre) throws JadePersistenceException, EcheanceLibreException {
        if (idEcheanceLibre == null) {
            throw new EcheanceLibreException("Unable to read simple echeance libre, the id passed is null");
        }
        SimpleEcheanceLibre simpleEcheanceLibre = new SimpleEcheanceLibre();
        simpleEcheanceLibre.setId(idEcheanceLibre);
        return (SimpleEcheanceLibre) JadePersistenceManager.read(simpleEcheanceLibre);
    }

    @Override
    public SimpleEcheanceLibreSearchModel search(SimpleEcheanceLibreSearchModel searchModel)
            throws JadePersistenceException, EcheanceLibreException {
        if (searchModel == null) {
            throw new EcheanceLibreException("Unable to search simple echeance libre, the model passed is null");
        }
        return (SimpleEcheanceLibreSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleEcheanceLibre update(SimpleEcheanceLibre echeanceLibre) throws JadePersistenceException,
            EcheanceLibreException {
        if (echeanceLibre == null) {
            throw new EcheanceLibreException("Unable to update simple echeance libre, the model passed is null");
        }
        SimpleEcheanceLibreChecker.checkForUpdate(echeanceLibre);
        return (SimpleEcheanceLibre) JadePersistenceManager.update(echeanceLibre);
    }

}
