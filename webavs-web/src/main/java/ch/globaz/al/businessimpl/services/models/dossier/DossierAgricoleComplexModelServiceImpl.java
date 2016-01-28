package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierAgricoleComplexModelException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.dossier.DossierAgricoleComplexModelService;
import ch.globaz.al.businessimpl.checker.model.dossier.DossierAgricoleComplexModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service de gestion de la persistance des dossiers agricoles
 * 
 * @author jts
 * 
 */
public class DossierAgricoleComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DossierAgricoleComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier. DossierAgricoleComplexModelService
     * #create(ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel)
     */
    @Override
    public DossierAgricoleComplexModel create(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        if (dossierComplexModel == null) {
            throw new ALDossierAgricoleComplexModelException(
                    "DossierAgricoleComplexModelServiceImpl#create : Unable to add model (dossierComplexModel) - the model passed is null!");
        }

        // validation
        DossierAgricoleComplexModelChecker.validate(dossierComplexModel);

        ALServiceLocator.getDossierModelService().create(dossierComplexModel.getDossierModel());

        dossierComplexModel.setAllocataireComplexModel(ALImplServiceLocator.getAllocataireAgricoleComplexModelService()
                .create(dossierComplexModel.getAllocataireAgricoleComplexModel()));

        return dossierComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.dossier. DossierAgricoleComplexModelService
     * #delete(ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel)
     */
    @Override
    public DossierAgricoleComplexModel delete(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        if (dossierComplexModel == null) {
            throw new ALDossierAgricoleComplexModelException(
                    "DossierAgricoleComplexModelServiceImpl#create : Unable to remove model (dossierComplexModel) - the model passed is null!");
        }
        if (dossierComplexModel.isNew()) {
            throw new ALDossierAgricoleComplexModelException(
                    "DossierAgricoleComplexModelServiceImpl#create : Unable to remove model (dossierComplexModel) - the model passed is new!");
        }

        ALServiceLocator.getDossierModelService().delete(dossierComplexModel.getDossierModel());

        dossierComplexModel.setAllocataireComplexModel(ALImplServiceLocator.getAllocataireAgricoleComplexModelService()
                .delete(dossierComplexModel.getAllocataireAgricoleComplexModel()));

        return dossierComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.dossier. DossierAgricoleComplexModelService#read(java.lang.String)
     */
    @Override
    public DossierAgricoleComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idDossier)) {
            throw new ALDossierModelException(
                    "DossierAgricoleComplexModelServiceImpl#create : Unable to read model (DossierAgricoleComplexModel) - the id passed is not defined!");
        }
        DossierAgricoleComplexModel dossierComplexModel = new DossierAgricoleComplexModel();
        dossierComplexModel.setId(idDossier);

        return (DossierAgricoleComplexModel) JadePersistenceManager.read(dossierComplexModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.dossier. DossierAgricoleComplexModelService
     * #update(ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel)
     */
    @Override
    public DossierAgricoleComplexModel update(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        if (dossierComplexModel == null) {
            throw new ALDossierModelException(
                    "DossierAgricoleComplexModelServiceImpl#create : Unable to update model (dossierComplexModel) - the model passed is null!");
        }
        if (dossierComplexModel.isNew()) {
            throw new ALDossierModelException(
                    "DossierAgricoleComplexModelServiceImpl#create : Unable to update model (dossierComplexModel) - the model passed is new!");
        }

        // validation
        DossierAgricoleComplexModelChecker.validate(dossierComplexModel);

        ALServiceLocator.getDossierModelService().update(dossierComplexModel.getDossierModel());

        dossierComplexModel.setAllocataireComplexModel(ALImplServiceLocator.getAllocataireAgricoleComplexModelService()
                .update(dossierComplexModel.getAllocataireAgricoleComplexModel()));

        return dossierComplexModel;
    }

}
