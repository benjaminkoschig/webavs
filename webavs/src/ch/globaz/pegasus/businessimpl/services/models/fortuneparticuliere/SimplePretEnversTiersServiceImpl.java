package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiersSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimplePretEnversTiersService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere.SimplePretEnversTiersChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimplePretEnversTiersServiceImpl extends PegasusAbstractServiceImpl implements
        SimplePretEnversTiersService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimplePretEnversTiersService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimplePretEnversTiers)
     */
    @Override
    public SimplePretEnversTiers create(SimplePretEnversTiers simplePretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException {
        if (simplePretEnversTiers == null) {
            throw new PretEnversTiersException("Unable to create simplePretEnversTiers, the model passed is null!");
        }
        SimplePretEnversTiersChecker.checkForCreate(simplePretEnversTiers);
        return (SimplePretEnversTiers) JadePersistenceManager.add(simplePretEnversTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimplePretEnversTiersService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimplePretEnversTiers)
     */
    @Override
    public SimplePretEnversTiers delete(SimplePretEnversTiers simplePretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException {
        if (simplePretEnversTiers == null) {
            throw new PretEnversTiersException("Unable to delete simplePretEnversTiers, the model passed is null!");
        }
        if (simplePretEnversTiers.isNew()) {
            throw new PretEnversTiersException("Unable to delete simplePretEnversTiers, the model passed is new!");
        }
        SimplePretEnversTiersChecker.checkForDelete(simplePretEnversTiers);
        return (SimplePretEnversTiers) JadePersistenceManager.delete(simplePretEnversTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimplePretEnversTiersService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimplePretEnversTiersSearch search = new SimplePretEnversTiersSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimplePretEnversTiersService#read(java.lang.String)
     */
    @Override
    public SimplePretEnversTiers read(String idVehicule) throws JadePersistenceException, PretEnversTiersException {
        if (JadeStringUtil.isEmpty(idVehicule)) {
            throw new PretEnversTiersException("Unable to read simplePretEnversTiers, the id passed is not defined!");
        }
        SimplePretEnversTiers simplePretEnversTiers = new SimplePretEnversTiers();
        simplePretEnversTiers.setId(idVehicule);
        return (SimplePretEnversTiers) JadePersistenceManager.read(simplePretEnversTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimplePretEnversTiersService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimplePretEnversTiers)
     */
    @Override
    public SimplePretEnversTiers update(SimplePretEnversTiers simplePretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException {
        if (simplePretEnversTiers == null) {
            throw new PretEnversTiersException("Unable to update simplePretEnversTiers, the model passed is null!");
        }
        if (simplePretEnversTiers.isNew()) {
            throw new PretEnversTiersException("Unable to update simplePretEnversTiers, the model passed is new!");
        }
        SimplePretEnversTiersChecker.checkForUpdate(simplePretEnversTiers);
        return (SimplePretEnversTiers) JadePersistenceManager.update(simplePretEnversTiers);
    }

}
