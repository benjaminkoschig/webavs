package ch.globaz.al.businessimpl.services.models.allocataire;

import globaz.globall.db.BConstants;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireComplexModelException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;
import ch.globaz.al.business.services.models.allocataire.AllocataireComplexModelService;
import ch.globaz.al.businessimpl.checker.model.allocataire.AllocataireComplexModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.service.Constants;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * classe d'implémentation des service de AllocataireComplexModel
 * 
 * @author PTA
 * 
 */
public class AllocataireComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        AllocataireComplexModelService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire. AllocataireComplexModelService
     * #count(ch.globaz.al.business.models.allocataire .AllocataireComplexSearchModel)
     */
    @Override
    public int count(AllocataireComplexSearchModel allocataireComplexSearch) throws JadePersistenceException,
            JadeApplicationException {

        return JadePersistenceManager.count(allocataireComplexSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AllocataireComplexModelService
     * #create(ch.globaz.al.business.model.allocataire.AllocataireComplexModel)
     */
    @Override
    public AllocataireComplexModel create(AllocataireComplexModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        // validation
        AllocataireComplexModelChecker.validate(allocataireComplexModel);

        if (allocataireComplexModel.getPersonneEtendueComplexModel().isNew()) {
            // On définit explicitement qu'il s'agit d'une personne physique
            // (pour qu'un ctrl validité sur NSS soit fait, entre autres...)
            allocataireComplexModel.getPersonneEtendueComplexModel().getTiers()
                    .set_personnePhysique(BConstants.DB_BOOLEAN_CHAR_TRUE.toString());

            allocataireComplexModel.setPersonneEtendueComplexModel(TIBusinessServiceLocator.getPersonneEtendueService()
                    .create(allocataireComplexModel.getPersonneEtendueComplexModel()));

        } else {
            // allocataireComplexModel.getPersonneEtendueComplexModel().getTiers()
            // .setTypeTiers(Constants.CS_TIERS);
            //
            // allocataireComplexModel.getPersonneEtendueComplexModel().getTiers()
            // .set_personnePhysique(
            // BConstants.DB_BOOLEAN_CHAR_TRUE.toString());

            // allocataireComplexModel
            // .setPersonneEtendueComplexModel(TIBusinessServiceLocator
            // .getPersonneEtendueService().update(
            // allocataireComplexModel
            // .getPersonneEtendueComplexModel()));

        }

        // Ajout du rôle AF si idTiers pas null
        if (!JadeStringUtil.isEmpty(allocataireComplexModel.getPersonneEtendueComplexModel().getId())) {
            TIBusinessServiceLocator.getRoleService().beginRole(
                    allocataireComplexModel.getPersonneEtendueComplexModel().getId(), ALCSTiers.ROLE_AF);
        }

        // enregistrement des données allocataire
        allocataireComplexModel.getAllocataireModel().setIdTiersAllocataire(
                allocataireComplexModel.getPersonneEtendueComplexModel().getId());
        allocataireComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().create(
                allocataireComplexModel.getAllocataireModel()));

        return allocataireComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AllocataireComplexModelService
     * #delete(ch.globaz.al.business.model.allocataire.AllocataireComplexModel)
     */
    @Override
    public AllocataireComplexModel delete(AllocataireComplexModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        if (allocataireComplexModel == null) {
            throw new ALAllocataireComplexModelException(
                    "Unable to remove model (allocataireComplexModel) - the model passed is null!");
        }
        if (allocataireComplexModel.isNew()) {
            throw new ALAllocataireComplexModelException(
                    "Unable to remove model (allocataireComplexModel) - the model passed is new!");
        }

        // Le supprime en DB
        allocataireComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().delete(
                allocataireComplexModel.getAllocataireModel()));

        // on ne supprime pas les données tiers, on enlève juste le rôle
        TIBusinessServiceLocator.getRoleService().endRole(
                allocataireComplexModel.getPersonneEtendueComplexModel().getId(), ALCSTiers.ROLE_AF);

        return allocataireComplexModel;
    }

    /*
     * Service qui remplit le modèle si existant et sinon valeurs par défaut Attention! Appeler que depuis le helper et
     * pas dans les actions ( gestion des droits d'accès)
     * 
     * @see ch.globaz.al.business.services.models.allocataire. AllocataireComplexModelService
     * #initModel(ch.globaz.al.business.models.allocataire .AllocataireComplexModel)
     */
    @Override
    public AllocataireComplexModel initModel(AllocataireComplexModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        String idAllocataire = allocataireComplexModel.getAllocataireModel().getIdAllocataire();

        if (!JadeStringUtil.isEmpty(idAllocataire)) {
            allocataireComplexModel = read(idAllocataire);
        }

        if (allocataireComplexModel.getAllocataireModel().isNew()) {
            allocataireComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().initModel(
                    allocataireComplexModel.getAllocataireModel()));
        }

        if (allocataireComplexModel.getPersonneEtendueComplexModel().isNew()) {

            allocataireComplexModel.getPersonneEtendueComplexModel().getTiers().setDesignation1("");
            allocataireComplexModel.getPersonneEtendueComplexModel().getTiers().setDesignation2("");
            allocataireComplexModel.getPersonneEtendueComplexModel().getTiers().setIdPays(ALCSPays.PAYS_SUISSE);
            allocataireComplexModel.getPersonneEtendueComplexModel().getPersonne().setDateNaissance("");
            allocataireComplexModel.getPersonneEtendueComplexModel().getPersonneEtendue().setNumAvsActuel("");
        }

        return allocataireComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AllocataireComplexModelService #read(java.lang.String)
     */
    @Override
    public AllocataireComplexModel read(String idAllocataireComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idAllocataireComplexModel)) {
            throw new ALAllocataireComplexModelException(
                    "unable to read allocataireComplexModel- the id passed is empty");
        }

        AllocataireComplexModel allocataireComplexModel = new AllocataireComplexModel();
        allocataireComplexModel.setId(idAllocataireComplexModel);

        return (AllocataireComplexModel) JadePersistenceManager.read(allocataireComplexModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire. AllocataireComplexModelService
     * #search(ch.globaz.al.business.models.allocataire .AllocataireComplexSearchModel)
     */
    @Override
    public AllocataireComplexSearchModel search(AllocataireComplexSearchModel allocataireComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (allocataireComplexSearchModel == null) {
            throw new ALAllocataireComplexModelException(
                    "unable to search allocataireComplexModel - the model passed is null");
        }
        return (AllocataireComplexSearchModel) JadePersistenceManager.search(allocataireComplexSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.allocataire.AllocataireComplexModelService
     * #update(ch.globaz.al.business.model.allocataire.AllocataireComplexModel)
     */
    @Override
    public AllocataireComplexModel update(AllocataireComplexModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        if (allocataireComplexModel == null) {
            throw new ALAllocataireComplexModelException(
                    "Unable to update the allocataireComplexModel-the model passed is empty");
        }
        if (allocataireComplexModel.isNew()) {
            throw new ALAllocataireComplexModelException(
                    "unable to update the allocataireComplexModel-the model passed is new");

        }

        // validation
        AllocataireComplexModelChecker.validate(allocataireComplexModel);

        allocataireComplexModel.getPersonneEtendueComplexModel().getTiers().setTypeTiers(Constants.CS_TIERS);
        //
        // allocataireComplexModel
        // .setPersonneEtendueComplexModel(TIBusinessServiceLocator
        // .getPersonneEtendueService().update(
        // allocataireComplexModel
        // .getPersonneEtendueComplexModel()));

        // mise à jour des données allocataire
        allocataireComplexModel.setAllocataireModel(ALImplServiceLocator.getAllocataireModelService().update(
                allocataireComplexModel.getAllocataireModel()));

        return allocataireComplexModel;
    }

}
