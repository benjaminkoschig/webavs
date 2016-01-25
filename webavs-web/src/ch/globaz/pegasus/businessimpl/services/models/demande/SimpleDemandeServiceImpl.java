/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.demande;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.demande.SimpleDemandeSearch;
import ch.globaz.pegasus.business.services.models.demande.SimpleDemandeService;
import ch.globaz.pegasus.businessimpl.checkers.demande.SimpleDemandeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author ECO
 * 
 */
public class SimpleDemandeServiceImpl extends PegasusAbstractServiceImpl implements SimpleDemandeService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.SimpleDemandeService
     * #count(ch.globaz.pegasus.business.models.demande.SimpleDemandeSearch)
     */
    @Override
    public int count(SimpleDemandeSearch search) throws DemandeException, JadePersistenceException {
        if (search == null) {
            throw new DemandeException("Unable to count demandes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.SimpleDemandeService
     * #create(ch.globaz.pegasus.business.models.demande.SimpleDemande)
     */
    @Override
    public SimpleDemande create(SimpleDemande demande) throws DemandeException, JadePersistenceException,
            DossierException {
        if (demande == null) {
            throw new DemandeException("Unable to create demande, the model passed is null!");
        }
        SimpleDemandeChecker.checkForCreate(demande);
        return (SimpleDemande) JadePersistenceManager.add(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.SimpleDemandeService
     * #delete(ch.globaz.pegasus.business.models.demande.SimpleDemande)
     */
    @Override
    public SimpleDemande delete(SimpleDemande demande) throws DemandeException, JadePersistenceException {
        if (demande == null) {
            throw new DemandeException("Unable to delete demande, the model passed is null!");
        }
        if (demande.isNew()) {
            throw new DemandeException("Unable to delete demande, the model passed is new!");
        }
        SimpleDemandeChecker.checkForDelete(demande);
        return (SimpleDemande) JadePersistenceManager.delete(demande);
    }

    @Override
    public boolean isDemandeInitial(SimpleDemande simpleDemande, String dateDemandeToCheck) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if ((simpleDemande == null) || JadeStringUtil.isBlank(dateDemandeToCheck)) {
            throw new DemandeException(
                    "Unable to count simpleDemande, the model passed is not null, or the date to check is null or not defined!");
        }

        // Si la date de debut est vide, date de fin aussi, demande non validée

        SimpleDemandeSearch search = new SimpleDemandeSearch();
        search.setForIdDossier(simpleDemande.getIdDossier());
        search.setForDateFin(JadeDateUtil.addMonths("01." + dateDemandeToCheck, -1).substring(3));
        int result = PegasusImplServiceLocator.getSimpleDemandeService().count(search);

        return result == 0;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.SimpleDemandeService #read(java.lang.String)
     */
    @Override
    public SimpleDemande read(String idDemande) throws DemandeException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new DemandeException("Unable to read demande, the id passed is not defined!");
        }
        SimpleDemande demande = new SimpleDemande();
        demande.setId(idDemande);
        return (SimpleDemande) JadePersistenceManager.read(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.SimpleDemandeService
     * #update(ch.globaz.pegasus.business.models.demande.SimpleDemande)
     */
    @Override
    public SimpleDemande update(SimpleDemande demande) throws DemandeException, JadePersistenceException,
            DossierException {
        if (demande == null) {
            throw new DemandeException("Unable to update demande, the model passed is null!");
        }
        if (demande.isNew()) {
            throw new DemandeException("Unable to update demande, the model passed is new!");
        }
        SimpleDemandeChecker.checkForUpdate(demande);
        return (SimpleDemande) JadePersistenceManager.update(demande);
    }

}
