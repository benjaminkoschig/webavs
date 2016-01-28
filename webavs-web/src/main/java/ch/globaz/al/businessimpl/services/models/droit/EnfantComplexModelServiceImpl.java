package ch.globaz.al.businessimpl.services.models.droit;

import globaz.globall.db.BConstants;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.exceptions.model.droit.ALEnfantComplexModelException;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexSearchModel;
import ch.globaz.al.business.services.models.droit.EnfantComplexModelService;
import ch.globaz.al.businessimpl.checker.model.droit.EnfantComplexModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.service.Constants;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * classe d'implémentation des services de enfantComplexModel
 * 
 * @author JTS,GMO
 * 
 */
public class EnfantComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements EnfantComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantComplexModelService
     * #count(ch.globaz.al.business.models.droit.EnfantComplexSearchModel)
     */
    @Override
    public int count(EnfantComplexSearchModel enfantSearchComplex) throws JadePersistenceException,
            JadeApplicationException {

        if (enfantSearchComplex == null) {
            throw new ALEnfantComplexModelException("EnfantComplexModelServiceImpl#count : enfantSearchComplex is null");
        }

        return JadePersistenceManager.count(enfantSearchComplex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantComplexModelService
     * #create(ch.globaz.al.business.models.droit.EnfantComplexModel)
     */
    @Override
    public EnfantComplexModel create(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (enfantComplexModel == null) {
            throw new ALEnfantComplexModelException("EnfantComplexModelServiceImpl#create : enfantComplexModel is null");
        }

        // validation
        EnfantComplexModelChecker.validate(enfantComplexModel);

        if (enfantComplexModel.getPersonneEtendueComplexModel().isNew()) {
            // On définit explicitement qu'il s'agit d'une personne physique
            // (pour qu'un ctrl validité sur NSS soit fait, entre autres...)
            enfantComplexModel.getPersonneEtendueComplexModel().getTiers()
                    .set_personnePhysique(BConstants.DB_BOOLEAN_CHAR_TRUE.toString());
            // l'enfant est créé avec la langue française, car langue
            // obligatoire dans les tiers
            // et pas de dans l'écran
            enfantComplexModel.getPersonneEtendueComplexModel().getTiers().setLangue(ALCSTiers.LANGUE_FRANCAIS);
            enfantComplexModel.setPersonneEtendueComplexModel(TIBusinessServiceLocator.getPersonneEtendueService()
                    .create(enfantComplexModel.getPersonneEtendueComplexModel()));

            // Set l'id du tiers sur lequel est lié l'enfant
            enfantComplexModel.getEnfantModel().setIdTiersEnfant(
                    enfantComplexModel.getPersonneEtendueComplexModel().getId());

        } else {
            enfantComplexModel.getPersonneEtendueComplexModel().getTiers().setTypeTiers(Constants.CS_TIERS);

            // enfantComplexModel
            // .setPersonneEtendueComplexModel(TIBusinessServiceLocator
            // .getPersonneEtendueService().update(
            // enfantComplexModel
            // .getPersonneEtendueComplexModel()));

            if (JadeNumericUtil.isEmptyOrZero(enfantComplexModel.getEnfantModel().getIdTiersEnfant())) {
                enfantComplexModel.getEnfantModel().setIdTiersEnfant(
                        enfantComplexModel.getPersonneEtendueComplexModel().getId());
            }
        }

        // Ajout du rôle AF
        if (!JadeStringUtil.isEmpty(enfantComplexModel.getPersonneEtendueComplexModel().getId())) {
            TIBusinessServiceLocator.getRoleService().beginRole(
                    enfantComplexModel.getPersonneEtendueComplexModel().getId(), ALCSTiers.ROLE_AF);
        }

        enfantComplexModel.setEnfantModel(ALImplServiceLocator.getEnfantModelService().create(
                enfantComplexModel.getEnfantModel()));

        return enfantComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantComplexModelService
     * #delete(ch.globaz.al.business.models.droit.EnfantComplexModel)
     */
    @Override
    public EnfantComplexModel delete(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (enfantComplexModel == null) {
            throw new ALEnfantComplexModelException(
                    "EnfantComplexModelServiceImpl#delete : Unable to remove model (enfantComplexModel) - the model passed is null!");
        }
        if (enfantComplexModel.isNew()) {
            throw new ALDossierModelException(
                    "EnfantComplexModelServiceImpl#delete : Unable to remove model (enfantComplexModel) - the model passed is new!");
        }

        // Le supprime en DB
        enfantComplexModel.setEnfantModel(ALImplServiceLocator.getEnfantModelService().delete(
                enfantComplexModel.getEnfantModel()));

        // Suppression du rôle AF
        TIBusinessServiceLocator.getRoleService().endRole(enfantComplexModel.getPersonneEtendueComplexModel().getId(),
                ALCSTiers.ROLE_AF);

        return enfantComplexModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantComplexModelService
     * #initModel(ch.globaz.al.business.models.droit.EnfantComplexModel)
     */
    @Override
    public EnfantComplexModel initModel(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (enfantComplexModel == null) {
            throw new ALEnfantComplexModelException(
                    "EnfantComplexModelServiceImpl#initModel : enfantComplexModel is null");
        }

        String idEnfant = enfantComplexModel.getEnfantModel().getIdEnfant();

        if (!JadeStringUtil.isEmpty(idEnfant)) {
            enfantComplexModel = read(idEnfant);
        }

        if (enfantComplexModel.getEnfantModel().isNew()) {
            enfantComplexModel.setEnfantModel(ALImplServiceLocator.getEnfantModelService().initModel(
                    enfantComplexModel.getEnfantModel()));

        }

        if (enfantComplexModel.getPersonneEtendueComplexModel().isNew()) {

            enfantComplexModel.getPersonneEtendueComplexModel().getPersonne().setDateNaissance("");
            enfantComplexModel.getPersonneEtendueComplexModel().getTiers().setIdPays(ALCSPays.PAYS_SUISSE);
        }

        return enfantComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantComplexModelService #read(java.lang.String)
     */
    @Override
    public EnfantComplexModel read(String idEnfantComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnfantComplexModel)) {
            throw new ALEnfantComplexModelException(
                    "EnfantComplexModelServiceImpl#read : unable to read enfantComplexModel - the id passed is empty");
        }

        EnfantComplexModel enfantComplexModel = new EnfantComplexModel();
        enfantComplexModel.setId(idEnfantComplexModel);

        return (EnfantComplexModel) JadePersistenceManager.read(enfantComplexModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantComplexModelService
     * #search(ch.globaz.al.business.models.droit.EnfantComplexSearchModel)
     */
    @Override
    public EnfantComplexSearchModel search(EnfantComplexSearchModel enfantComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (enfantComplexSearchModel == null) {
            throw new ALEnfantComplexModelException(
                    "EnfantComplexModelServiceImpl#search : unable to search enfantComplexSearchModel- the model passed is null");
        }
        return (EnfantComplexSearchModel) JadePersistenceManager.search(enfantComplexSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.EnfantComplexModelService
     * #update(ch.globaz.al.business.models.droit.EnfantComplexModel)
     */
    @Override
    public EnfantComplexModel update(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (enfantComplexModel == null) {
            throw new ALEnfantComplexModelException(
                    "EnfantComplexModelServiceImpl#update : Unable to update the enfantComplex-the model passed is empty");
        }
        if (enfantComplexModel.isNew()) {
            throw new ALEnfantComplexModelException(
                    "EnfantComplexModelServiceImpl#update : unable to update the enfantComplex-the model passed is new");

        }

        // validation
        EnfantComplexModelChecker.validate(enfantComplexModel);

        // mise à jour des données de l'enfant
        enfantComplexModel.setEnfantModel(ALImplServiceLocator.getEnfantModelService().update(
                enfantComplexModel.getEnfantModel()));

        enfantComplexModel.getPersonneEtendueComplexModel().getTiers().setTypeTiers(Constants.CS_TIERS);

        // mise à jour du tiers
        // enfantComplexModel
        // .setPersonneEtendueComplexModel(TIBusinessServiceLocator
        // .getPersonneEtendueService().update(
        // enfantComplexModel
        // .getPersonneEtendueComplexModel()));

        return enfantComplexModel;
    }

}
