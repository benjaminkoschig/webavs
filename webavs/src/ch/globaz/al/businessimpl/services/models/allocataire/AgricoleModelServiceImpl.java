package ch.globaz.al.businessimpl.services.models.allocataire;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.allocataire.ALAgricoleModelException;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.services.models.allocataire.AgricoleModelService;
import ch.globaz.al.businessimpl.checker.model.allocataire.AgricoleModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * 
 * classe d'implémentation des services de AgricoleModel
 * 
 * @author PTA
 * 
 */
public class AgricoleModelServiceImpl extends ALAbstractBusinessServiceImpl implements AgricoleModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AgricoleModelService
     * #count(ch.globaz.al.business.models.allocataire.AgricoleSearch)
     */
    @Override
    public int count(AgricoleSearchModel agricoleSearch) throws JadePersistenceException {
        return JadePersistenceManager.count(agricoleSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AgricoleModelService#create
     * (ch.globaz.al.business.model.allocataire.AgricoleModel)
     */
    @Override
    public AgricoleModel create(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException {
        if (agricoleModel == null) {
            throw new ALAgricoleModelException("Unable to create model AgricoleModel - the id passed is null");
        }
        // contrôle de validité des données de l'agricole
        AgricoleModelChecker.validate(agricoleModel);

        // ajoute un agricole dans la persistance et le retourne
        return (AgricoleModel) JadePersistenceManager.add(agricoleModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AgricoleModelService#delete
     * (ch.globaz.al.business.model.allocataire.AgricoleModel)
     */
    @Override
    public AgricoleModel delete(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException {
        if (agricoleModel == null) {
            throw new ALAgricoleModelException(
                    "AgricoleModelServiceImpl#delete : Unable to delete model AgricoleModel-the model passed is null");
        }
        if (agricoleModel.isNew()) {
            throw new ALAgricoleModelException(
                    "AgricoleModelServiceImpl#delete : Unable to delete model ModelAgricole-the model passed is new");
        }

        return (AgricoleModel) JadePersistenceManager.delete(agricoleModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AgricoleModelService
     * #deleteForIdAllocataire(java.lang.String)
     */
    @Override
    public AgricoleModel deleteForIdAllocataire(String idAllocataire) throws JadePersistenceException,
            JadeApplicationException {

        if (idAllocataire == null) {
            throw new ALAgricoleModelException(
                    "AgricoleModelServiceImpl#deleteForIdAllocataire : Unable to delete AgricoleModel - the idAllocataire passed is null");
        }

        // recherche de l'agriculteur correspondant à l'allocataire
        AgricoleSearchModel as = new AgricoleSearchModel();
        as.setForIdAllocataire(idAllocataire);
        JadePersistenceManager.search(as);

        // si plusieurs enregistrement trouvé => problème
        if (as.getSize() > 1) {
            throw new ALAgricoleModelException(
                    "AgricoleModelServiceImpl#deleteForIdAllocataire : Unable to delete AgricoleModel - only one model should be deleted, "
                            + as.getSize() + " have been found");
            // si 0 enregistrement
        } else if (as.getSize() == 0) {
            return null;
        } else {
            // suppression
            return delete((AgricoleModel) as.getSearchResults()[0]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AgricoleModelService #read(java.lang.String)
     */
    @Override
    public AgricoleModel read(String idAgricoleModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idAgricoleModel)) {
            throw new ALAgricoleModelException(
                    "AgricoleModelServiceImpl#read : Unable to read Agricole-the id passed is empty");
        }
        AgricoleModel agricoleModel = new AgricoleModel();
        agricoleModel.setId(idAgricoleModel);
        return (AgricoleModel) JadePersistenceManager.read(agricoleModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AgricoleModelService
     * #search(ch.globaz.al.business.models.allocataire.AgricoleSearchModel)
     */
    @Override
    public AgricoleSearchModel search(AgricoleSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALAgricoleModelException("DossierModelServiceImpl#search : searchModel is null");
        }

        return (AgricoleSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AgricoleModelService#update
     * (ch.globaz.al.business.model.allocataire.AgricoleModel)
     */
    @Override
    public AgricoleModel update(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException {
        if (agricoleModel == null) {
            throw new ALAgricoleModelException(
                    "AgricoleModelServiceImpl#update : Unable to update the agricole-th model passed is null");
        }
        if (agricoleModel.isNew()) {
            throw new ALAgricoleModelException(
                    "AgricoleModelServiceImpl#update : Unable to update the agricole the model passed is new");
        }

        // contrôle de validité des données de l'agricole
        AgricoleModelChecker.validate(agricoleModel);

        // ajoute l' agricole à modifier dans la persistance et le retourne
        return (AgricoleModel) JadePersistenceManager.update(agricoleModel);
    }
}