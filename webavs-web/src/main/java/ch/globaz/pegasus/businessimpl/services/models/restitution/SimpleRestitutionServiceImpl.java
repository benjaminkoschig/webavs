package ch.globaz.pegasus.businessimpl.services.models.restitution;

import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.restitution.PCRestitutionException;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitution;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitutionSearch;
import ch.globaz.pegasus.business.services.models.restitution.SimpleRestitutionService;
import ch.globaz.pegasus.businessimpl.checkers.restitution.SimpleRestitutionChecker;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class SimpleRestitutionServiceImpl implements SimpleRestitutionService {


    @Override
    public int count(SimpleRestitutionSearch search) throws PCRestitutionException, JadePersistenceException {
        if (search == null) {
            throw new PCRestitutionException("Unable to count restitutions, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleRestitutionSearch search(SimpleRestitutionSearch simpleRestitutionSearch) throws PCRestitutionException, JadePersistenceException {
        if (simpleRestitutionSearch == null) {
            throw new PCRestitutionException("Unable to search simpleRestitutionSearch, the model passed is null!");
        }
        return (SimpleRestitutionSearch) JadePersistenceManager.search(simpleRestitutionSearch);
    }

    @Override
    public SimpleRestitution create(SimpleRestitution simpleRestitution) throws PCRestitutionException, JadeApplicationServiceNotAvailableException, DossierException, JadePersistenceException {
        if (simpleRestitution == null) {
            throw new PCRestitutionException("Unable to create restitution, the model passed is null!");
        }
        SimpleRestitutionChecker.checkForCreate(simpleRestitution);
        return (SimpleRestitution) JadePersistenceManager.add(simpleRestitution);
    }

    @Override
    public SimpleRestitution update(SimpleRestitution simpleRestitution) throws PCRestitutionException, JadePersistenceException {
        if (simpleRestitution == null) {
            throw new PCRestitutionException("Unable to update restitution, the model passed is null!");
        }
        if (simpleRestitution.isNew()) {
            throw new PCRestitutionException("Unable to update restitution, the model passed is new!");
        }
        SimpleRestitutionChecker.checkForUpdate(simpleRestitution);
        return (SimpleRestitution) JadePersistenceManager.update(simpleRestitution);
    }



}
