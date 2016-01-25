package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleBetail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleBetailSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleBetailService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere.SimpleBetailChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleBetailServiceImpl extends PegasusAbstractServiceImpl implements SimpleBetailService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleBetailService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleBetail)
     */
    @Override
    public SimpleBetail create(SimpleBetail simpleBetail) throws JadePersistenceException, BetailException {
        if (simpleBetail == null) {
            throw new BetailException("Unable to create simpleBetail, the model passed is null!");
        }
        SimpleBetailChecker.checkForCreate(simpleBetail);
        return (SimpleBetail) JadePersistenceManager.add(simpleBetail);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleBetailService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleBetail)
     */
    @Override
    public SimpleBetail delete(SimpleBetail simpleBetail) throws BetailException, JadePersistenceException {
        if (simpleBetail == null) {
            throw new BetailException("Unable to delete simpleBetail, the model passed is null!");
        }
        if (simpleBetail.isNew()) {
            throw new BetailException("Unable to delete simpleBetail, the model passed is new!");
        }
        SimpleBetailChecker.checkForDelete(simpleBetail);
        return (SimpleBetail) JadePersistenceManager.delete(simpleBetail);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleBetailService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleBetailSearch search = new SimpleBetailSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleBetailService#read(java.lang.String)
     */
    @Override
    public SimpleBetail read(String idBetail) throws JadePersistenceException, BetailException {
        if (JadeStringUtil.isEmpty(idBetail)) {
            throw new BetailException("Unable to read simpleBetail, the id passed is not defined!");
        }
        SimpleBetail simpleBetail = new SimpleBetail();
        simpleBetail.setId(idBetail);
        return (SimpleBetail) JadePersistenceManager.read(simpleBetail);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleBetailService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleBetail)
     */
    @Override
    public SimpleBetail update(SimpleBetail simpleBetail) throws JadePersistenceException, BetailException {
        if (simpleBetail == null) {
            throw new BetailException("Unable to update simpleBetail, the model passed is null!");
        }
        if (simpleBetail.isNew()) {
            throw new BetailException("Unable to update simpleBetail, the model passed is new!");
        }
        SimpleBetailChecker.checkForUpdate(simpleBetail);
        return (SimpleBetail) JadePersistenceManager.update(simpleBetail);
    }

}
