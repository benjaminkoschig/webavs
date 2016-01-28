package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleLibelleContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleLibelleContratEntretienViagerService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleLibelleContratEntretienViagerChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleLibelleContratEntretienViagerServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleLibelleContratEntretienViagerService {

    @Override
    public SimpleLibelleContratEntretienViager create(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) throws JadePersistenceException,
            SimpleLibelleContratEntretienViagerException {
        if (simpleLibelleContratEntretienViager == null) {
            throw new SimpleLibelleContratEntretienViagerException(
                    "Unable to create simpleLibelleContratEntretienViager, the model passed is null!");
        }
        SimpleLibelleContratEntretienViagerChecker.checkForCreate(simpleLibelleContratEntretienViager);
        return (SimpleLibelleContratEntretienViager) JadePersistenceManager.add(simpleLibelleContratEntretienViager);
    }

    @Override
    public SimpleLibelleContratEntretienViager delete(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException {
        if (simpleLibelleContratEntretienViager == null) {
            throw new SimpleLibelleContratEntretienViagerException(
                    "Unable to delete simpleLibelleContratEntretienViager, the model passed is null!");
        }
        if (simpleLibelleContratEntretienViager.isNew()) {
            throw new SimpleLibelleContratEntretienViagerException(
                    "Unable to delete simpleLibelleContratEntretienViager, the model passed is new!");
        }
        SimpleLibelleContratEntretienViagerChecker.checkForDelete(simpleLibelleContratEntretienViager);
        return (SimpleLibelleContratEntretienViager) JadePersistenceManager.delete(simpleLibelleContratEntretienViager);
    }

    @Override
    public SimpleLibelleContratEntretienViager read(String idLibelleContratEntretienViager)
            throws JadePersistenceException, SimpleLibelleContratEntretienViagerException {
        if (JadeStringUtil.isEmpty(idLibelleContratEntretienViager)) {
            throw new SimpleLibelleContratEntretienViagerException(
                    "Unable to read simpleLibelleContratEntretienViager, the id passed is not defined!");
        }
        SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager = new SimpleLibelleContratEntretienViager();
        simpleLibelleContratEntretienViager.setId(idLibelleContratEntretienViager);
        return (SimpleLibelleContratEntretienViager) JadePersistenceManager.read(simpleLibelleContratEntretienViager);
    }

    @Override
    public SimpleLibelleContratEntretienViagerSearch search(
            SimpleLibelleContratEntretienViagerSearch libelleContratEntretienViagerSearch)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException {
        if (libelleContratEntretienViagerSearch == null) {
            throw new SimpleLibelleContratEntretienViagerException(
                    "Unable to search libelleContratEntretienViagerSearch, the model passed is null!");
        }
        return (SimpleLibelleContratEntretienViagerSearch) JadePersistenceManager
                .search(libelleContratEntretienViagerSearch);
    }

    @Override
    public SimpleLibelleContratEntretienViager update(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) throws JadePersistenceException,
            SimpleLibelleContratEntretienViagerException {
        if (simpleLibelleContratEntretienViager == null) {
            throw new SimpleLibelleContratEntretienViagerException(
                    "Unable to update simpleLibelleContratEntretienViager, the model passed is null!");
        }
        if (simpleLibelleContratEntretienViager.isNew()) {
            throw new SimpleLibelleContratEntretienViagerException(
                    "Unable to update simpleLibelleContratEntretienViager, the model passed is new!");
        }
        SimpleLibelleContratEntretienViagerChecker.checkForUpdate(simpleLibelleContratEntretienViager);
        return (SimpleLibelleContratEntretienViager) JadePersistenceManager.update(simpleLibelleContratEntretienViager);
    }

}
