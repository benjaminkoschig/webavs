package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleFraisGarde;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleFraisGardeSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleFraisGardeService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleFraisGardeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

import java.util.List;

public class SimpleFraisGardeServiceImpl extends PegasusAbstractServiceImpl implements SimpleFraisGardeService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleFraisGardeService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleFraisGarde)
     */
    @Override
    public SimpleFraisGarde create(SimpleFraisGarde simpleFraisGarde) throws JadePersistenceException,
            FraisGardeException {
        if (simpleFraisGarde == null) {
            throw new FraisGardeException("Unable to create simpleFraisGarde, the model passed is null!");
        }
        SimpleFraisGardeChecker.checkForCreate(simpleFraisGarde);
        return (SimpleFraisGarde) JadePersistenceManager.add(simpleFraisGarde);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleFraisGardeService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleFraisGarde)
     */
    @Override
    public SimpleFraisGarde delete(SimpleFraisGarde simpleFraisGarde) throws FraisGardeException,
            JadePersistenceException {
        if (simpleFraisGarde == null) {
            throw new FraisGardeException("Unable to delete simpleFraisGarde, the model passed is null!");
        }
        if (simpleFraisGarde.isNew()) {
            throw new FraisGardeException("Unable to delete simpleFraisGarde, the model passed is new!");
        }
        SimpleFraisGardeChecker.checkForDelete(simpleFraisGarde);
        return (SimpleFraisGarde) JadePersistenceManager.delete(simpleFraisGarde);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleFraisGardeSearch search = new SimpleFraisGardeSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleFraisGardeService#read(java.lang.String)
     */
    @Override
    public SimpleFraisGarde read(String idFraisGarde) throws JadePersistenceException, FraisGardeException {
        if (JadeStringUtil.isEmpty(idFraisGarde)) {
            throw new FraisGardeException("Unable to read simpleFraisGarde, the id passed is not defined!");
        }
        SimpleFraisGarde simpleFraisGarde = new SimpleFraisGarde();
        simpleFraisGarde.setId(idFraisGarde);
        return (SimpleFraisGarde) JadePersistenceManager.read(simpleFraisGarde);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleFraisGardeService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleFraisGarde)
     */
    @Override
    public SimpleFraisGarde update(SimpleFraisGarde simpleFraisGarde) throws JadePersistenceException,
            FraisGardeException {
        if (simpleFraisGarde == null) {
            throw new FraisGardeException("Unable to update simpleFraisGarde, the model passed is null!");
        }
        if (simpleFraisGarde.isNew()) {
            throw new FraisGardeException("Unable to update simpleFraisGarde, the model passed is new!");
        }
        SimpleFraisGardeChecker.checkForUpdate(simpleFraisGarde);
        return (SimpleFraisGarde) JadePersistenceManager.update(simpleFraisGarde);
    }

}
