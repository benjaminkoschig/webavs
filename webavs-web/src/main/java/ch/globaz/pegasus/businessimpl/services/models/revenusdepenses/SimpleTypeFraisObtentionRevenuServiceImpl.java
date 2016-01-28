package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleTypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleTypeFraisObtentionRevenuService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleTypeFraisObtentionRevenuChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleTypeFraisObtentionRevenuServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleTypeFraisObtentionRevenuService {
    @Override
    public SimpleTypeFraisObtentionRevenu create(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws JadePersistenceException, SimpleTypeFraisObtentionRevenuException {
        if (simpleTypeFraisObtentionRevenu == null) {
            throw new SimpleTypeFraisObtentionRevenuException(
                    "Unable to create simpleTypeFraisObtentionRevenu, the model passed is null!");
        }
        SimpleTypeFraisObtentionRevenuChecker.checkForCreate(simpleTypeFraisObtentionRevenu);
        return (SimpleTypeFraisObtentionRevenu) JadePersistenceManager.add(simpleTypeFraisObtentionRevenu);
    }

    @Override
    public SimpleTypeFraisObtentionRevenu delete(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException {
        if (simpleTypeFraisObtentionRevenu == null) {
            throw new SimpleTypeFraisObtentionRevenuException(
                    "Unable to delete simpleTypeFraisObtentionRevenu, the model passed is null!");
        }
        if (simpleTypeFraisObtentionRevenu.isNew()) {
            throw new SimpleTypeFraisObtentionRevenuException(
                    "Unable to delete simpleTypeFraisObtentionRevenu, the model passed is new!");
        }
        SimpleTypeFraisObtentionRevenuChecker.checkForDelete(simpleTypeFraisObtentionRevenu);
        return (SimpleTypeFraisObtentionRevenu) JadePersistenceManager.delete(simpleTypeFraisObtentionRevenu);
    }

    @Override
    public void delete(SimpleTypeFraisObtentionRevenuSearch searchType) throws JadePersistenceException {
        JadePersistenceManager.delete(searchType);
    }

    @Override
    public SimpleTypeFraisObtentionRevenu read(String idTypeFraisObtentionRevenu) throws JadePersistenceException,
            SimpleTypeFraisObtentionRevenuException {
        if (JadeStringUtil.isEmpty(idTypeFraisObtentionRevenu)) {
            throw new SimpleTypeFraisObtentionRevenuException(
                    "Unable to read simpleTypeFraisObtentionRevenu, the id passed is not defined!");
        }
        SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();
        simpleTypeFraisObtentionRevenu.setId(idTypeFraisObtentionRevenu);
        return (SimpleTypeFraisObtentionRevenu) JadePersistenceManager.read(simpleTypeFraisObtentionRevenu);
    }

    @Override
    public SimpleTypeFraisObtentionRevenuSearch search(
            SimpleTypeFraisObtentionRevenuSearch simpleTypeFraisObtentionRevenuSearch)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException {
        if (simpleTypeFraisObtentionRevenuSearch == null) {
            throw new SimpleTypeFraisObtentionRevenuException(
                    "Unable to search simpleTypeFraisObtentionRevenuSearch, the model passed is null!");
        }
        return (SimpleTypeFraisObtentionRevenuSearch) JadePersistenceManager
                .search(simpleTypeFraisObtentionRevenuSearch);
    }

    @Override
    public SimpleTypeFraisObtentionRevenu update(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws JadePersistenceException, SimpleTypeFraisObtentionRevenuException {
        if (simpleTypeFraisObtentionRevenu == null) {
            throw new SimpleTypeFraisObtentionRevenuException(
                    "Unable to update simpleTypeFraisObtentionRevenu, the model passed is null!");
        }
        if (simpleTypeFraisObtentionRevenu.isNew()) {
            throw new SimpleTypeFraisObtentionRevenuException(
                    "Unable to update simpleTypeFraisObtentionRevenu, the model passed is new!");
        }
        SimpleTypeFraisObtentionRevenuChecker.checkForUpdate(simpleTypeFraisObtentionRevenu);
        return (SimpleTypeFraisObtentionRevenu) JadePersistenceManager.update(simpleTypeFraisObtentionRevenu);
    }
}
