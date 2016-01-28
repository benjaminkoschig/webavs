package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAutresRevenus;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAutresRevenusSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleAutresRevenusService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleAutresRevenusChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAutresRevenusServiceImpl extends PegasusAbstractServiceImpl implements SimpleAutresRevenusService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAutresRevenusService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAutresRevenus)
     */
    @Override
    public SimpleAutresRevenus create(SimpleAutresRevenus simpleAutresRevenus) throws JadePersistenceException,
            AutresRevenusException {
        if (simpleAutresRevenus == null) {
            throw new AutresRevenusException("Unable to create simpleAutresRevenus, the model passed is null!");
        }
        SimpleAutresRevenusChecker.checkForCreate(simpleAutresRevenus);
        return (SimpleAutresRevenus) JadePersistenceManager.add(simpleAutresRevenus);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAutresRevenusService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAutresRevenus)
     */
    @Override
    public SimpleAutresRevenus delete(SimpleAutresRevenus simpleAutresRevenus) throws AutresRevenusException,
            JadePersistenceException {
        if (simpleAutresRevenus == null) {
            throw new AutresRevenusException("Unable to delete simpleAutresRevenus, the model passed is null!");
        }
        if (simpleAutresRevenus.isNew()) {
            throw new AutresRevenusException("Unable to delete simpleAutresRevenus, the model passed is new!");
        }
        SimpleAutresRevenusChecker.checkForDelete(simpleAutresRevenus);
        return (SimpleAutresRevenus) JadePersistenceManager.delete(simpleAutresRevenus);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAutresRevenusSearch search = new SimpleAutresRevenusSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleAutresRevenusService#read(java.lang.String)
     */
    @Override
    public SimpleAutresRevenus read(String idAutresRevenus) throws JadePersistenceException, AutresRevenusException {
        if (JadeStringUtil.isEmpty(idAutresRevenus)) {
            throw new AutresRevenusException("Unable to read simpleAutresRevenus, the id passed is not defined!");
        }
        SimpleAutresRevenus simpleAutresRevenus = new SimpleAutresRevenus();
        simpleAutresRevenus.setId(idAutresRevenus);
        return (SimpleAutresRevenus) JadePersistenceManager.read(simpleAutresRevenus);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAutresRevenusService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAutresRevenus)
     */
    @Override
    public SimpleAutresRevenus update(SimpleAutresRevenus simpleAutresRevenus) throws JadePersistenceException,
            AutresRevenusException {
        if (simpleAutresRevenus == null) {
            throw new AutresRevenusException("Unable to update simpleAutresRevenus, the model passed is null!");
        }
        if (simpleAutresRevenus.isNew()) {
            throw new AutresRevenusException("Unable to update simpleAutresRevenus, the model passed is new!");
        }
        SimpleAutresRevenusChecker.checkForUpdate(simpleAutresRevenus);
        return (SimpleAutresRevenus) JadePersistenceManager.update(simpleAutresRevenus);
    }

}
