package ch.globaz.al.businessimpl.services.models.allocataire;

import globaz.globall.db.BConstants;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireAgricoleComplexModelException;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireComplexModelException;
import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;
import ch.globaz.al.business.services.models.allocataire.AllocataireAgricoleComplexModelService;
import ch.globaz.al.businessimpl.checker.model.allocataire.AllocataireAgricoleComplexModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.service.Constants;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * 
 * classe d'implémentation des services de allocataireComplexModel
 * 
 * @author PTA
 * 
 */
public class AllocataireAgricoleComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        AllocataireAgricoleComplexModelService {

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.AllocataireAgricoleComplexModelService#create(ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel)
     */
    @Override
    public AllocataireAgricoleComplexModel create(AllocataireAgricoleComplexModel allocAgrComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        if (allocAgrComplexModel == null) {
            throw new ALAllocataireAgricoleComplexModelException(
                    "Unable to create allocataireAgricoleComplexModel - the model passed is null!");
        }

        // validation
        AllocataireAgricoleComplexModelChecker.validate(allocAgrComplexModel);

        if (allocAgrComplexModel.getPersonneEtendueComplexModel().isNew()) {
            // On définit explicitement qu'il s'agit d'une personne physique
            // (pour qu'un ctrl validité sur NSS soit fait, entre autres...)
            allocAgrComplexModel.getPersonneEtendueComplexModel().getTiers()
                    .set_personnePhysique(BConstants.DB_BOOLEAN_CHAR_TRUE.toString());
            allocAgrComplexModel.setPersonneEtendueComplexModel(TIBusinessServiceLocator.getPersonneEtendueService()
                    .create(allocAgrComplexModel.getPersonneEtendueComplexModel()));

        } else {
            // allocAgrComplexModel.getPersonneEtendueComplexModel().getTiers()
            // .setTypeTiers(Constants.CS_TIERS);
            //
            // allocAgrComplexModel
            // .setPersonneEtendueComplexModel(TIBusinessServiceLocator
            // .getPersonneEtendueService().update(
            // allocAgrComplexModel
            // .getPersonneEtendueComplexModel()));
        }

        // Ajout du rôle AF
        TIBusinessServiceLocator.getRoleService().beginRole(
                allocAgrComplexModel.getPersonneEtendueComplexModel().getId(), ALCSTiers.ROLE_AF);

        // enregistrement des données allocataire
        allocAgrComplexModel.getAllocataireModel().setIdTiersAllocataire(
                allocAgrComplexModel.getPersonneEtendueComplexModel().getId());

        // test si alloc existant
        if (allocAgrComplexModel.getAllocataireModel().isNew()) {
            allocAgrComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().create(
                    allocAgrComplexModel.getAllocataireModel()));
        } else {
            allocAgrComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().update(
                    allocAgrComplexModel.getAllocataireModel()));
        }

        // enregistrement des données agricoles
        allocAgrComplexModel.getAgricoleModel().setIdAllocataire(
                allocAgrComplexModel.getAllocataireModel().getIdAllocataire());
        allocAgrComplexModel.setAgricoleModel(ALImplServiceLocator.getAgricoleModelService().create(
                allocAgrComplexModel.getAgricoleModel()));

        return allocAgrComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.service.allocataire. AllocataireAgricoleComplexModelService
     * #delete(ch.globaz.al.business.model. allocataire.AllocataireAgricoleComplexModel)
     */
    @Override
    public AllocataireAgricoleComplexModel delete(AllocataireAgricoleComplexModel allocAgrComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        if (allocAgrComplexModel == null) {
            throw new ALAllocataireComplexModelException(
                    "Unable to remove model (allocAgrComplexModel) - the model passed is null!");
        }
        if (allocAgrComplexModel.isNew()) {
            throw new ALAllocataireComplexModelException(
                    "Unable to remove model (allocAgrComplexModel) - the model passed is new!");
        }

        // Suppression des données agricoles
        allocAgrComplexModel.setAgricoleModel(ALImplServiceLocator.getAgricoleModelService().delete(
                allocAgrComplexModel.getAgricoleModel()));

        // suppression des données de l'allocataire
        allocAgrComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().delete(
                allocAgrComplexModel.getAllocataireModel()));

        // suppression du rôle AF pour le tiers
        TIBusinessServiceLocator.getRoleService().endRole(
                allocAgrComplexModel.getPersonneEtendueComplexModel().getId(), ALCSTiers.ROLE_AF);

        return allocAgrComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.service.allocataire. AllocataireAgricoleComplexModelService#read(java.lang.String)
     */
    @Override
    public AllocataireAgricoleComplexModel read(String idAllocatairAgricoleComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idAllocatairAgricoleComplexModel)) {
            throw new ALAllocataireComplexModelException(
                    "unable to read AllocataireAgricoleComplexModel - the id passed is empty");
        }

        AllocataireAgricoleComplexModel model = new AllocataireAgricoleComplexModel();
        model.setId(idAllocatairAgricoleComplexModel);

        return (AllocataireAgricoleComplexModel) JadePersistenceManager.read(model);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire. AllocataireAgricoleComplexModelService #update(ch.globaz.
     *      al.business.models .allocataire. AllocataireAgricoleComplexModel )
     */
    @Override
    public AllocataireAgricoleComplexModel update(AllocataireAgricoleComplexModel allocAgrComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        if (allocAgrComplexModel == null) {
            throw new ALAllocataireComplexModelException(
                    "Unable to update the allocAgrComplexModel - the model passed is empty");
        }
        if (allocAgrComplexModel.isNew()) {
            throw new ALAllocataireComplexModelException(
                    "unable to update the allocAgrComplexModel - the model passed is new");

        }

        // validation
        AllocataireAgricoleComplexModelChecker.validate(allocAgrComplexModel);

        // mise à jour du tiers (désactivation tiers)
        // allocAgrComplexModel
        // .setPersonneEtendueComplexModel(TIBusinessServiceLocator
        // .getPersonneEtendueService().update(
        // allocAgrComplexModel
        // .getPersonneEtendueComplexModel()));

        // mise à jour des données allocataire
        allocAgrComplexModel.getPersonneEtendueComplexModel().getTiers().setTypeTiers(Constants.CS_TIERS);

        allocAgrComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().update(
                allocAgrComplexModel.getAllocataireModel()));

        // mise à jour des données agricoles
        allocAgrComplexModel.setAgricoleModel(ALImplServiceLocator.getAgricoleModelService().update(
                allocAgrComplexModel.getAgricoleModel()));

        return allocAgrComplexModel;
    }

}
