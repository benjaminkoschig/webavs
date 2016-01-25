package ch.globaz.perseus.businessimpl.services.models.parametres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.LoyerSearchModel;
import ch.globaz.perseus.business.models.parametres.SimpleLoyer;
import ch.globaz.perseus.business.services.models.parametres.SimpleLoyerService;
import ch.globaz.perseus.businessimpl.checkers.parametres.SimpleLoyerChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleLoyerServiceImpl extends PerseusAbstractServiceImpl implements SimpleLoyerService {

    @Override
    public SimpleLoyer create(SimpleLoyer simpleLoyer) throws JadePersistenceException, ParametresException {
        if (simpleLoyer == null) {
            throw new ParametresException("Unable to create a simple Loyer, the model passed is null !");
        }
        SimpleLoyerChecker.checkForCreate(simpleLoyer);
        return (SimpleLoyer) JadePersistenceManager.add(simpleLoyer);
    }

    @Override
    public SimpleLoyer delete(SimpleLoyer simpleLoyer) throws JadePersistenceException, ParametresException {
        if (simpleLoyer == null) {
            throw new ParametresException("Unable to delete a simple Loyer, the model passed is null!");
        }
        if (simpleLoyer.isNew()) {
            throw new ParametresException("Unable to delete a simple Loyer, the model passed is new!");
        }
        SimpleLoyerChecker.checkForDelete(simpleLoyer);
        return (SimpleLoyer) JadePersistenceManager.delete(simpleLoyer);
    }

    @Override
    public SimpleLoyer read(String idLoyer) throws JadePersistenceException, ParametresException {
        if (idLoyer == null) {
            throw new ParametresException("Unable to read a simple Zone, the model passed is null!");
        }
        SimpleLoyer simpleLoyer = new SimpleLoyer();
        simpleLoyer.setId(idLoyer);
        return (SimpleLoyer) JadePersistenceManager.read(simpleLoyer);
    }

    public LoyerSearchModel search(LoyerSearchModel searchModel) throws JadePersistenceException, ParametresException {
        if (searchModel == null) {
            throw new ParametresException("Unable to search a simple Loyer, the search model passed is null!");
        }
        return (LoyerSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleLoyer update(SimpleLoyer simpleLoyer) throws JadePersistenceException, ParametresException {
        if (simpleLoyer == null) {
            throw new ParametresException("Unable to update a simple Loyer, the model passed is null!");
        }
        if (simpleLoyer.isNew()) {
            throw new ParametresException("Unable to update a simple Loyer, the model passed is new!");
        }
        SimpleLoyerChecker.checkForUpdate(simpleLoyer);
        return (SimpleLoyer) JadePersistenceManager.update(simpleLoyer);
    }

}
