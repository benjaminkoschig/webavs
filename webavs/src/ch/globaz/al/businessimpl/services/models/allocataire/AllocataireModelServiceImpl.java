package ch.globaz.al.businessimpl.services.models.allocataire;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireModelException;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.allocataire.AllocataireSearchModel;
import ch.globaz.al.business.services.models.allocataire.AllocataireModelService;
import ch.globaz.al.businessimpl.checker.model.allocataire.AllocataireModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe d'implémentation des services de AllocataireModel
 * 
 * @author PTA
 * 
 */
public class AllocataireModelServiceImpl extends ALAbstractBusinessServiceImpl implements AllocataireModelService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AllocataireModelService#create
     * (ch.globaz.al.business.model.allocataire.AllocataireModel)
     */
    @Override
    public int count(AllocataireSearchModel allocataireSearch) throws JadePersistenceException,
            ALAllocataireModelException {

        if (allocataireSearch == null) {
            throw new ALAllocataireModelException("Unable to count, the passed model is null");
        }

        return JadePersistenceManager.count(allocataireSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AllocataireModelService#delete
     * (ch.globaz.al.business.model.allocataire.AllocataireModel)
     */
    @Override
    public AllocataireModel create(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException {
        if (allocataireModel == null) {
            throw new ALAllocataireModelException("Unable to create model allocataire-the id passed is new");
        }
        // Validation intégrité des données du modèle AllocataireModel
        AllocataireModelChecker.validate(allocataireModel);
        // ajoute l'allocataire dans la persistance et le retourne
        return (AllocataireModel) JadePersistenceManager.add(allocataireModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AllocataireModelService#read (java.lang.String)
     */
    @Override
    public AllocataireModel delete(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException {
        if (allocataireModel == null) {
            throw new ALAllocataireModelException("Unable to delete model allocataire-the id passed is null");
        }
        if (allocataireModel.isNew()) {
            throw new ALAllocataireModelException("Unable to delete model allocataire-the id passed is new");
        }

        // validation des données pour suppression du modèle AllocataireModel
        AllocataireModelChecker.validateForDelete(allocataireModel);

        // suppression des données agricoles (si nécessaire)
        ALImplServiceLocator.getAgricoleModelService().deleteForIdAllocataire(allocataireModel.getIdAllocataire());

        // suppression des revenus (si nécessaire)
        ALImplServiceLocator.getRevenuModelService().deleteForIdAllocataire(allocataireModel.getIdAllocataire());

        // suppression de l'allocataire
        return (AllocataireModel) JadePersistenceManager.delete(allocataireModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireModelService
     * #initModel(ch.globaz.al.business.models.allocataire.AllocataireModel)
     */
    @Override
    public AllocataireModel initModel(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException {

        // valeurs par défaut
        allocataireModel.setIdPaysResidence(ALCSPays.PAYS_SUISSE);
        allocataireModel.setCantonResidence("");
        // allocataireModel.setLangueAffilie(true);

        return allocataireModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireModelService #read(java.lang.String)
     */
    @Override
    public AllocataireModel read(String idAllocataireModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idAllocataireModel)) {
            throw new ALAllocataireModelException("Unable to read allocataire-The id passed is empty");
        }
        AllocataireModel allocataireModel = new AllocataireModel();
        allocataireModel.setId(idAllocataireModel);
        return (AllocataireModel) JadePersistenceManager.read(allocataireModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireModelService
     * #update(ch.globaz.al.business.models.allocataire.AllocataireModel)
     */
    @Override
    public AllocataireModel update(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException {
        if (allocataireModel == null) {
            throw new ALAllocataireModelException("Unable to update allocataire-the id passed is null");
        }
        if (allocataireModel.isNew()) {
            throw new ALAllocataireModelException("Unable to update allocataire-the id passed is new");
        }
        AllocataireModelChecker.validate(allocataireModel);

        return (AllocataireModel) JadePersistenceManager.update(allocataireModel);

    }
}
