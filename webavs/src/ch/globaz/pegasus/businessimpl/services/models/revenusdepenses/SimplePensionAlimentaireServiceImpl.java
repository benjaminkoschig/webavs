package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimplePensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.SimplePensionAlimentaireSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimplePensionAlimentaireService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimplePensionAlimentaireChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimplePensionAlimentaireServiceImpl extends PegasusAbstractServiceImpl implements
        SimplePensionAlimentaireService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimplePensionAlimentaireService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimplePensionAlimentaire)
     */
    @Override
    public SimplePensionAlimentaire create(SimplePensionAlimentaire simplePensionAlimentaire)
            throws JadePersistenceException, PensionAlimentaireException {
        if (simplePensionAlimentaire == null) {
            throw new PensionAlimentaireException(
                    "Unable to create simplePensionAlimentaire, the model passed is null!");
        }
        SimplePensionAlimentaireChecker.checkForCreate(simplePensionAlimentaire);
        return (SimplePensionAlimentaire) JadePersistenceManager.add(simplePensionAlimentaire);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimplePensionAlimentaireService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimplePensionAlimentaire)
     */
    @Override
    public SimplePensionAlimentaire delete(SimplePensionAlimentaire simplePensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException {
        if (simplePensionAlimentaire == null) {
            throw new PensionAlimentaireException(
                    "Unable to delete simplePensionAlimentaire, the model passed is null!");
        }
        if (simplePensionAlimentaire.isNew()) {
            throw new PensionAlimentaireException("Unable to delete simplePensionAlimentaire, the model passed is new!");
        }
        SimplePensionAlimentaireChecker.checkForDelete(simplePensionAlimentaire);
        return (SimplePensionAlimentaire) JadePersistenceManager.delete(simplePensionAlimentaire);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimplePensionAlimentaireSearch search = new SimplePensionAlimentaireSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimplePensionAlimentaireService#read(java.lang.String)
     */
    @Override
    public SimplePensionAlimentaire read(String idPensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException {
        if (JadeStringUtil.isEmpty(idPensionAlimentaire)) {
            throw new PensionAlimentaireException(
                    "Unable to read simplePensionAlimentaire, the id passed is not defined!");
        }
        SimplePensionAlimentaire simplePensionAlimentaire = new SimplePensionAlimentaire();
        simplePensionAlimentaire.setId(idPensionAlimentaire);
        return (SimplePensionAlimentaire) JadePersistenceManager.read(simplePensionAlimentaire);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimplePensionAlimentaireService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimplePensionAlimentaire)
     */
    @Override
    public SimplePensionAlimentaire update(SimplePensionAlimentaire simplePensionAlimentaire)
            throws JadePersistenceException, PensionAlimentaireException {
        if (simplePensionAlimentaire == null) {
            throw new PensionAlimentaireException(
                    "Unable to update simplePensionAlimentaire, the model passed is null!");
        }
        if (simplePensionAlimentaire.isNew()) {
            throw new PensionAlimentaireException("Unable to update simplePensionAlimentaire, the model passed is new!");
        }
        SimplePensionAlimentaireChecker.checkForUpdate(simplePensionAlimentaire);
        return (SimplePensionAlimentaire) JadePersistenceManager.update(simplePensionAlimentaire);
    }

}
