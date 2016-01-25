package ch.globaz.perseus.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.models.dossier.SimpleDossier;
import ch.globaz.perseus.business.services.models.dossier.SimpleDossierService;
import ch.globaz.perseus.businessimpl.checkers.dossier.SimpleDossierChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class SimpleDossierServiceImpl extends PerseusAbstractServiceImpl implements SimpleDossierService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.dossier.SimpleDossierService#create(ch.globaz.perseus.business.models
     * .dossier.SimpleDossier)
     */
    @Override
    public SimpleDossier create(SimpleDossier dossier) throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to create a simple dossier, the model passed is null!");
        }
        SimpleDossierChecker.checkForCreate(dossier);
        return (SimpleDossier) JadePersistenceManager.add(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.dossier.SimpleDossierService#delete(ch.globaz.perseus.business.models
     * .dossier.SimpleDossier)
     */
    @Override
    public SimpleDossier delete(SimpleDossier dossier) throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to delete a simple dossier, the model passed is null!");
        } else if (dossier.isNew()) {
            throw new DossierException("Unable to delete a simple dossier, the model passed is new!");
        }
        SimpleDossierChecker.checkForDelete(dossier);
        return (SimpleDossier) JadePersistenceManager.delete(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.dossier.SimpleDossierService#read(java.lang.String)
     */
    @Override
    public SimpleDossier read(String idDossier) throws JadePersistenceException, DossierException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DossierException("Unable to read a simple dossier, the id passed is null!");
        }
        SimpleDossier dossier = new SimpleDossier();
        dossier.setId(idDossier);
        return (SimpleDossier) JadePersistenceManager.read(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.dossier.SimpleDossierService#update(ch.globaz.perseus.business.models
     * .dossier.SimpleDossier)
     */
    @Override
    public SimpleDossier update(SimpleDossier dossier) throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to update a simple dossier, the model passed is null!");
        } else if (dossier.isNew()) {
            throw new DossierException("Unable to update a simple dossier, the model passed is new!");
        }
        SimpleDossierChecker.checkForUpdate(dossier);
        return (SimpleDossier) JadePersistenceManager.update(dossier);
    }

}
