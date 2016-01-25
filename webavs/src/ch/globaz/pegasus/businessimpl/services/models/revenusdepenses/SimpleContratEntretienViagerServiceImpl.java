package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleContratEntretienViagerService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleContratEntretienViagerChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleContratEntretienViagerServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleContratEntretienViagerService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleContratEntretienViagerService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleContratEntretienViager)
     */
    @Override
    public SimpleContratEntretienViager create(SimpleContratEntretienViager simpleContratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException {
        if (simpleContratEntretienViager == null) {
            throw new ContratEntretienViagerException(
                    "Unable to create simpleContratEntretienViager, the model passed is null!");
        }
        SimpleContratEntretienViagerChecker.checkForCreate(simpleContratEntretienViager);
        return (SimpleContratEntretienViager) JadePersistenceManager.add(simpleContratEntretienViager);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleContratEntretienViagerService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleContratEntretienViager)
     */
    @Override
    public SimpleContratEntretienViager delete(SimpleContratEntretienViager simpleContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException {
        if (simpleContratEntretienViager == null) {
            throw new ContratEntretienViagerException(
                    "Unable to delete simpleContratEntretienViager, the model passed is null!");
        }
        if (simpleContratEntretienViager.isNew()) {
            throw new ContratEntretienViagerException(
                    "Unable to delete simpleContratEntretienViager, the model passed is new!");
        }
        SimpleContratEntretienViagerChecker.checkForDelete(simpleContratEntretienViager);
        return (SimpleContratEntretienViager) JadePersistenceManager.delete(simpleContratEntretienViager);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleContratEntretienViagerSearch search = new SimpleContratEntretienViagerSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleContratEntretienViagerService#read(java.lang.String)
     */
    @Override
    public SimpleContratEntretienViager read(String idContratEntretienViager) throws JadePersistenceException,
            ContratEntretienViagerException {
        if (JadeStringUtil.isEmpty(idContratEntretienViager)) {
            throw new ContratEntretienViagerException(
                    "Unable to read simpleContratEntretienViager, the id passed is not defined!");
        }
        SimpleContratEntretienViager simpleContratEntretienViager = new SimpleContratEntretienViager();
        simpleContratEntretienViager.setId(idContratEntretienViager);
        return (SimpleContratEntretienViager) JadePersistenceManager.read(simpleContratEntretienViager);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleContratEntretienViagerService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleContratEntretienViager)
     */
    @Override
    public SimpleContratEntretienViager update(SimpleContratEntretienViager simpleContratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException {
        if (simpleContratEntretienViager == null) {
            throw new ContratEntretienViagerException(
                    "Unable to update simpleContratEntretienViager, the model passed is null!");
        }
        if (simpleContratEntretienViager.isNew()) {
            throw new ContratEntretienViagerException(
                    "Unable to update simpleContratEntretienViager, the model passed is new!");
        }
        SimpleContratEntretienViagerChecker.checkForUpdate(simpleContratEntretienViager);
        return (SimpleContratEntretienViager) JadePersistenceManager.update(simpleContratEntretienViager);
    }

}
