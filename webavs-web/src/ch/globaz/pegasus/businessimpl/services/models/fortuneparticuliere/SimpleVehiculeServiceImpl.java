package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleVehicule;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleVehiculeSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleVehiculeService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere.SimpleVehiculeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleVehiculeServiceImpl extends PegasusAbstractServiceImpl implements SimpleVehiculeService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleVehiculeService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleVehicule)
     */
    @Override
    public SimpleVehicule create(SimpleVehicule simpleVehicule) throws JadePersistenceException, VehiculeException {
        if (simpleVehicule == null) {
            throw new VehiculeException("Unable to create simpleVehicule, the model passed is null!");
        }
        SimpleVehiculeChecker.checkForCreate(simpleVehicule);
        return (SimpleVehicule) JadePersistenceManager.add(simpleVehicule);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleVehiculeService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleVehicule)
     */
    @Override
    public SimpleVehicule delete(SimpleVehicule simpleVehicule) throws VehiculeException, JadePersistenceException {
        if (simpleVehicule == null) {
            throw new VehiculeException("Unable to delete simpleVehicule, the model passed is null!");
        }
        if (simpleVehicule.isNew()) {
            throw new VehiculeException("Unable to delete simpleVehicule, the model passed is new!");
        }
        SimpleVehiculeChecker.checkForDelete(simpleVehicule);
        return (SimpleVehicule) JadePersistenceManager.delete(simpleVehicule);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleVehiculeService#deleteParListeIdDoFinH(java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleVehiculeSearch search = new SimpleVehiculeSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleVehiculeService#read(java.lang.String)
     */
    @Override
    public SimpleVehicule read(String idVehicule) throws JadePersistenceException, VehiculeException {
        if (JadeStringUtil.isEmpty(idVehicule)) {
            throw new VehiculeException("Unable to read simpleVehicule, the id passed is not defined!");
        }
        SimpleVehicule simpleVehicule = new SimpleVehicule();
        simpleVehicule.setId(idVehicule);
        return (SimpleVehicule) JadePersistenceManager.read(simpleVehicule);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleVehiculeService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleVehicule)
     */
    @Override
    public SimpleVehicule update(SimpleVehicule simpleVehicule) throws JadePersistenceException, VehiculeException {
        if (simpleVehicule == null) {
            throw new VehiculeException("Unable to update simpleVehicule, the model passed is null!");
        }
        if (simpleVehicule.isNew()) {
            throw new VehiculeException("Unable to update simpleVehicule, the model passed is new!");
        }
        SimpleVehiculeChecker.checkForUpdate(simpleVehicule);
        return (SimpleVehicule) JadePersistenceManager.update(simpleVehicule);
    }

}
