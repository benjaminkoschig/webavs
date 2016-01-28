package ch.globaz.pegasus.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.dossier.SimpleDossier;
import ch.globaz.pegasus.business.models.dossier.SimpleDossierSearch;
import ch.globaz.pegasus.business.services.models.dossier.SimpleDossierService;
import ch.globaz.pegasus.businessimpl.checkers.dossier.SimpleDossierChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author ECO
 */
public class SimpleDossierServiceImpl extends PegasusAbstractServiceImpl implements SimpleDossierService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.SimpleDossierService
     * #count(ch.globaz.pegasus.business.models.dossier.SimpleDossierSearch)
     */
    @Override
    public int count(SimpleDossierSearch search) throws DossierException, JadePersistenceException {
        if (search == null) {
            throw new DossierException("Unable to count dossiers, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.SimpleDossierService
     * #create(ch.globaz.pegasus.business.models.dossier.SimpleDossier)
     */
    @Override
    public SimpleDossier create(SimpleDossier dossier) throws DossierException, JadePersistenceException {
        if (dossier == null) {
            throw new DossierException("Unable to create dossier, the model passed is null!");
        }
        SimpleDossierChecker.checkForCreate(dossier);
        return (SimpleDossier) JadePersistenceManager.add(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.SimpleDossierService
     * #delete(ch.globaz.pegasus.business.models.dossier.SimpleDossier)
     */
    @Override
    public SimpleDossier delete(SimpleDossier dossier) throws DossierException, JadePersistenceException {
        if (dossier == null) {
            throw new DossierException("Unable to delete dossier, the model passed is null!");
        }
        if (dossier.isNew()) {
            throw new DossierException("Unable to delete dossier, the model passed is new!");
        }
        SimpleDossierChecker.checkForDelete(dossier);
        return (SimpleDossier) JadePersistenceManager.delete(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.SimpleDossierService #read(java.lang.String)
     */
    @Override
    public SimpleDossier read(String idDossier) throws DossierException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DossierException("Unable to read dossier, the id passed is not defined!");
        }
        SimpleDossier dossier = new SimpleDossier();
        dossier.setId(idDossier);
        return (SimpleDossier) JadePersistenceManager.read(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.SimpleDossierService
     * #update(ch.globaz.pegasus.business.models.dossier.SimpleDossier)
     */
    @Override
    public SimpleDossier update(SimpleDossier dossier) throws DossierException, JadePersistenceException {
        if (dossier == null) {
            throw new DossierException("Unable to update dossier, the model passed is null!");
        }
        if (dossier.isNew()) {
            throw new DossierException("Unable to update dossier, the model passed is new!");
        }
        SimpleDossierChecker.checkForUpdate(dossier);
        return (SimpleDossier) JadePersistenceManager.update(dossier);
    }

}
