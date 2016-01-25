package ch.globaz.al.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.model.droit.ALDroitComplexModelException;
import ch.globaz.al.business.exceptions.model.droit.ALDroitModelException;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.droit.DroitComplexModelService;
import ch.globaz.al.businessimpl.checker.model.droit.DroitComplexModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALImportUtils;

/**
 * Service de gestion de la persistance du modèle complexe des droits
 * 
 * @author jts
 * 
 */
public class DroitComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements DroitComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService#
     * clone(ch.globaz.al.business.models.droit.DroitComplexModel, java.lang.String)
     */
    @Override
    public DroitComplexModel clone(DroitComplexModel droitComplexModel, String idDossier)
            throws JadeApplicationException, JadePersistenceException {

        // met le même enfant que la référence
        // clone le droit (newId du droit)
        DroitComplexModel newDroitComplexModel = new DroitComplexModel();

        // même enfant lié au droit par défaut / unique en DB
        newDroitComplexModel.setEnfantComplexModel(droitComplexModel.getEnfantComplexModel());

        // clone du droit model / nouveau enregistrement en DB (crée quand
        // droitComplexModel.create)
        newDroitComplexModel.setDroitModel(ALImplServiceLocator.getDroitModelService().clone(
                droitComplexModel.getDroitModel(), idDossier));

        return newDroitComplexModel;

    }

    @Override
    public DroitComplexModel copie(DroitComplexModel droit) throws JadeApplicationException {

        if ((droit == null) || droit.isNew()) {
            throw new ALDroitComplexModelException(
                    "DroitComplexModelServiceImpl#copie : Unable to update the droitComplexModel-the model passed is null or is new");
        }

        DroitComplexModel newDroit = new DroitComplexModel();
        newDroit.setDroitModel(ALImplServiceLocator.getDroitModelService().copie(droit.getDroitModel()));
        newDroit.setEnfantComplexModel(droit.getEnfantComplexModel());

        return newDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService#
     * create(ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel create(DroitComplexModel droitCM) throws JadeApplicationException,
            JadePersistenceException {

        if (droitCM == null) {
            throw new ALDroitComplexModelException(
                    "Unable to add model (droitComplexModel) - the model passed is null!");
        }

        // validation
        DroitComplexModelChecker.validate(droitCM);

        // si le droit est de type ENF ou FORM on doit enregistrer l'enfant
        if (ALCSDroit.TYPE_ENF.equals(droitCM.getDroitModel().getTypeDroit())
                || ALCSDroit.TYPE_FORM.equals(droitCM.getDroitModel().getTypeDroit())) {

            // on ne créé pas forcément un nouvel enfant, on peut créer un droit
            // à partir d'un enfant existant
            if (droitCM.getEnfantComplexModel().getEnfantModel().isNew()) {
                droitCM.setEnfantComplexModel(ALImplServiceLocator.getEnfantComplexModelService().create(
                        droitCM.getEnfantComplexModel()));
            } else {
                droitCM.setEnfantComplexModel(ALImplServiceLocator.getEnfantComplexModelService().update(
                        droitCM.getEnfantComplexModel()));
            }

            droitCM.getDroitModel().setIdEnfant(droitCM.getEnfantComplexModel().getEnfantModel().getIdEnfant());

        }

        // enregistrement du droit
        droitCM.setDroitModel(ALImplServiceLocator.getDroitModelService().create(droitCM.getDroitModel()));
        // création et enregistrement du droit complémentaire si nécessaire avec
        // mis à jour du droit créé (cette opération n'est pas exécutée dans le
        // cadre d'une importation de données)
        if (!ALImportUtils.isImport) {
            ALServiceLocator.getDroitBusinessService().ajoutDroitMemeType(droitCM);
        }

        return droitCM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService#
     * delete(ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel delete(DroitComplexModel droitCM) throws JadeApplicationException,
            JadePersistenceException {

        if (droitCM == null) {
            throw new ALDroitComplexModelException(
                    "Unable to remove model (droitComplexModel) - the model passed is null!");
        }

        if (droitCM.isNew()) {
            throw new ALDroitComplexModelException(
                    "Unable to remove model (droitComplexModel) - the model passed is new!");
        }

        // suppression de l'enfant s'il n'est pas utilisé dans un autre droit
        if (droitCM.getEnfantComplexModel() != null) {
            DroitSearchModel droitSearch = new DroitSearchModel();
            droitSearch.setForIdEnfant(droitCM.getEnfantComplexModel().getEnfantModel().getIdEnfant());

            if (JadePersistenceManager.count(droitSearch) == 1) {
                droitCM.setEnfantComplexModel(ALImplServiceLocator.getEnfantComplexModelService().delete(
                        droitCM.getEnfantComplexModel()));
            }
        }

        // TODO supression des annonces non transmises

        // suppression du droit
        droitCM.setDroitModel(ALImplServiceLocator.getDroitModelService().delete(droitCM.getDroitModel()));

        return droitCM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService#
     * initModel(ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel initModel(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        String idDroit = droitComplexModel.getDroitModel().getIdDroit();
        String idEnfant = droitComplexModel.getEnfantComplexModel().getEnfantModel().getIdEnfant();

        if (!JadeStringUtil.isEmpty(idDroit)) {
            droitComplexModel = read(idDroit);
        }

        if (droitComplexModel.getDroitModel().isNew()) {
            droitComplexModel.getDroitModel().setIdEnfant(idEnfant);
            droitComplexModel.setDroitModel(ALImplServiceLocator.getDroitModelService().initModel(
                    droitComplexModel.getDroitModel()));
        }

        if (!JadeStringUtil.isEmpty(idEnfant)) {
            droitComplexModel.setEnfantComplexModel(ALImplServiceLocator.getEnfantComplexModelService().read(idEnfant));
        }

        if (droitComplexModel.getEnfantComplexModel().isNew()) {
            droitComplexModel.setEnfantComplexModel(ALImplServiceLocator.getEnfantComplexModelService().initModel(
                    droitComplexModel.getEnfantComplexModel()));
        }

        return droitComplexModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService# read(java.lang.String)
     */
    @Override
    public DroitComplexModel read(String idDroitComplexModel) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idDroitComplexModel)) {
            throw new ALDroitModelException("unable to read droitComplexModel- the id passed is empty");

        }
        DroitComplexModel droitComplexModel = new DroitComplexModel();
        droitComplexModel.setId(idDroitComplexModel);

        return (DroitComplexModel) JadePersistenceManager.read(droitComplexModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService#
     * search(ch.globaz.al.business.models.droit.DroitComplexSearchModel)
     */
    @Override
    public DroitComplexSearchModel search(DroitComplexSearchModel droitComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (droitComplexSearchModel == null) {
            throw new ALDroitModelException("unable to search droitBaseModel- the model passed is null");
        }
        return (DroitComplexSearchModel) JadePersistenceManager.search(droitComplexSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitComplexModelService#
     * update(ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel update(DroitComplexModel droitCM) throws JadeApplicationException,
            JadePersistenceException {

        if (droitCM == null) {
            throw new ALDroitComplexModelException("Unable to update the droitComplexModel-the model passed is empty");
        }

        if (droitCM.isNew()) {
            throw new ALDroitComplexModelException("unable to update the droitComplexModel-the model passed is new");
        }

        // validation
        DroitComplexModelChecker.validate(droitCM);

        // si le droit est de type ENF ou FORM on doit mettre à jour l'enfant
        if (ALCSDroit.TYPE_ENF.equals(droitCM.getDroitModel().getTypeDroit())
                || ALCSDroit.TYPE_FORM.equals(droitCM.getDroitModel().getTypeDroit())) {
            ALImplServiceLocator.getEnfantComplexModelService().update(droitCM.getEnfantComplexModel());
        }

        // mise à jour du droit
        droitCM.setDroitModel(ALImplServiceLocator.getDroitModelService().update(droitCM.getDroitModel()));

        return droitCM;
    }

}
