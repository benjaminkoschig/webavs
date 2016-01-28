package ch.globaz.prestation.businessimpl.services.models.echeance;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.models.echance.SimpleEcheance;
import ch.globaz.prestation.business.models.echance.SimpleEcheanceSearch;
import ch.globaz.prestation.business.services.models.echeance.SimpleEcheanceService;

public class SimpleEcheanceServiceImpl implements SimpleEcheanceService {

    @Override
    public int count(SimpleEcheanceSearch search) throws JadeApplicationException, JadePersistenceException {
        if (search == null) {
            throw new PrestationCommonException("Unable to count for the simpleEchance, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleEcheance create(SimpleEcheance entity) throws JadeApplicationException, JadePersistenceException {
        if (entity == null) {
            throw new PrestationCommonException("Unable to create entity(SimpleEcheance), the model passed is null!");
        }
        SimpleEcheanceChecker.checkForCreate(entity);
        return (SimpleEcheance) JadePersistenceManager.add(entity);
    }

    @Override
    public SimpleEcheance delete(SimpleEcheance entity) throws JadeApplicationException, JadePersistenceException {
        if (entity == null) {
            throw new PrestationCommonException("Unable to delete entity(SimpleEcheance), the model passed is null!");
        }
        return (SimpleEcheance) JadePersistenceManager.delete(entity);
    }

    @Override
    public SimpleEcheance read(String idEntity) throws JadeApplicationException, JadePersistenceException {
        if (idEntity == null) {
            throw new PrestationCommonException("Unable to read the simpleEcheance, the model passed is null!");
        }
        SimpleEcheance model = new SimpleEcheance();
        model.setId(idEntity);
        return (SimpleEcheance) JadePersistenceManager.read(model);
    }

    @Override
    public SimpleEcheanceSearch search(SimpleEcheanceSearch search) throws JadeApplicationException,
            JadePersistenceException {
        if (search == null) {
            throw new PrestationCommonException("Unable to search search, the model passed is null!");
        }
        return (SimpleEcheanceSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleEcheance update(SimpleEcheance entity) throws JadeApplicationException, JadePersistenceException {
        if (entity == null) {
            throw new PrestationCommonException("Unable to delete entities, the searchmodel passed is null!");
        }
        SimpleEcheanceChecker.checkForCreate(entity);
        return (SimpleEcheance) JadePersistenceManager.update(entity);
    }

}
