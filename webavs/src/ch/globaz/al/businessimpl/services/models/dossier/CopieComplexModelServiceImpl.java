package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALCopieComplexModelException;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.dossier.CopieComplexModelService;
import ch.globaz.al.businessimpl.checker.model.dossier.CopieComplexModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe d'implémentation des service de CopieComplexModel
 * 
 * @author JER
 * 
 */
public class CopieComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements CopieComplexModelService {

    @Override
    public int count(CopieComplexSearchModel copieComplexSearchModel) throws JadeApplicationException,
            JadePersistenceException {

        if (copieComplexSearchModel == null) {
            throw new ALCopieComplexModelException("unable to search copieComplexModel - the model passed is null");
        }

        return JadePersistenceManager.count(copieComplexSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieComplexModelService
     * #create(ch.globaz.al.business.model.dossier.CopieComplexModel)
     */
    @Override
    public CopieComplexModel create(CopieComplexModel copieComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (copieComplexModel == null) {
            throw new ALCopieComplexModelException("CopieComplexModelServiceImpl#create : copieComplexModel is null");
        }

        CopieComplexModelChecker.validate(copieComplexModel);
        copieComplexModel.setCopieModel(ALServiceLocator.getCopieModelService().create(
                copieComplexModel.getCopieModel()));

        return copieComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieComplexModelService
     * #delete(ch.globaz.al.business.model.dossier.CopieComplexModel)
     */
    @Override
    public CopieComplexModel delete(CopieComplexModel copieComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (copieComplexModel == null) {
            throw new ALCopieComplexModelException(
                    "Unable to remove model (allocataireComplexModel) - the model passed is null!");
        }

        if (copieComplexModel.isNew()) {
            throw new ALCopieComplexModelException(
                    "Unable to remove model (allocataireComplexModel) - the model passed is new!");
        }

        copieComplexModel.setCopieModel(ALServiceLocator.getCopieModelService().delete(
                copieComplexModel.getCopieModel()));

        return copieComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier. CopieComplexModelService
     * #initModel(ch.globaz.al.business.models.dossier .CopieComplexModel)
     */
    @Override
    public CopieComplexModel initModel(CopieComplexModel copieComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (copieComplexModel == null) {
            throw new ALCopieComplexModelException("CopieComplexModelServiceImpl#initModel : copieComplexModel is null");
        }

        copieComplexModel.setCopieModel(ALServiceLocator.getCopieModelService().initModel(
                copieComplexModel.getCopieModel()));

        // Implémenté au cas où on en aurait un jour besoin
        return copieComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieComplexModelService #read(java.lang.String)
     */
    @Override
    public CopieComplexModel read(String idCopieComplexModel) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idCopieComplexModel)) {
            throw new ALCopieComplexModelException("unable to read allocataireComplexModel- the id passed is empty");
        }

        CopieComplexModel copieComplexModel = new CopieComplexModel();
        copieComplexModel.setId(idCopieComplexModel);

        return (CopieComplexModel) JadePersistenceManager.read(copieComplexModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier. CopieComplexModelService
     * #search(ch.globaz.al.business.models.dossier .CopieComplexSearchModel)
     */
    @Override
    public CopieComplexSearchModel search(CopieComplexSearchModel copieComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (copieComplexSearchModel == null) {
            throw new ALCopieComplexModelException("unable to search copieComplexModel - the model passed is null");
        }

        return (CopieComplexSearchModel) JadePersistenceManager.search(copieComplexSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieComplexModelService
     * #update(ch.globaz.al.business.model.dossier.CopieComplexModel)
     */
    @Override
    public CopieComplexModel update(CopieComplexModel copieComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (copieComplexModel == null) {
            throw new ALCopieComplexModelException("Unable to update the copieComplexModel-the model passed is empty");
        }

        if (copieComplexModel.isNew()) {
            throw new ALCopieComplexModelException("unable to update the copieComplexModel-the model passed is new");

        }

        CopieComplexModelChecker.validate(copieComplexModel);

        copieComplexModel.setCopieModel(ALServiceLocator.getCopieModelService().update(
                copieComplexModel.getCopieModel()));

        return copieComplexModel;
    }
}
