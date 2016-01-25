package ch.globaz.al.businessimpl.services.models.adi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.model.adi.ALAdiSaisieModelException;
import ch.globaz.al.business.exceptions.model.adi.ALDecompteAdiModelException;
import ch.globaz.al.business.models.adi.AdiSaisieComplexModel;
import ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.adi.AdiSaisieComplexModelService;
import ch.globaz.al.businessimpl.checker.model.adi.AdiSaisieComplexModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services persistance du modèle AdiSaisiecomplexModel
 * 
 * @author GMO
 * 
 */
public class AdiSaisieComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        AdiSaisieComplexModelService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieComplexModelService
     * #create(ch.globaz.al.business.models.adi.AdiSaisieComplexModel)
     */
    @Override
    public AdiSaisieComplexModel create(AdiSaisieComplexModel adiSaisieComplexModel) throws JadePersistenceException,
            JadeApplicationException {
        if (adiSaisieComplexModel == null) {
            throw new ALDecompteAdiModelException(
                    "AdiSaisieComplexModelServiceImpl#create : unable to create saisieComplexModel  - the model passed is null");
        }

        // validation
        AdiSaisieComplexModelChecker.validate(adiSaisieComplexModel);
        // on ne change que adisaisiemodel, enfantcomplexmodel déjà existant et
        // lecture seule
        adiSaisieComplexModel.setAdiSaisieModel(ALServiceLocator.getAdiSaisieModelService().create(
                adiSaisieComplexModel.getAdiSaisieModel()));

        return adiSaisieComplexModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieComplexModelService
     * #delete(ch.globaz.al.business.models.adi.AdiSaisieComplexModel)
     */
    @Override
    public AdiSaisieComplexModel delete(AdiSaisieComplexModel adiSaisieComplexModel) throws JadePersistenceException,
            JadeApplicationException {
        if (adiSaisieComplexModel == null) {
            throw new ALAdiSaisieModelException(
                    "AdiSaisieComplexModelServiceImpl#delete : unable to delete saisieComplexModel  - the model passed is null");
        }
        if (adiSaisieComplexModel.isNew()) {
            throw new ALAdiSaisieModelException(
                    "AdiSaisieComplexModelServiceImpl#delete : unable to delete adiSaisieComplexModel, the id passed is new");
        }
        // on ne supprime que l'adiSaisieModel, l'enfant est laissé tel quel
        adiSaisieComplexModel.setAdiSaisieModel(ALServiceLocator.getAdiSaisieModelService().delete(
                adiSaisieComplexModel.getAdiSaisieModel()));
        return adiSaisieComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieComplexModelService
     * #initModel(ch.globaz.al.business.models.adi.AdiSaisieComplexModel)
     */
    @Override
    public AdiSaisieComplexModel initModel(AdiSaisieComplexModel adiSaisieComplexModel, HashMap listeASaisir)
            throws JadePersistenceException, JadeApplicationException {
        if (adiSaisieComplexModel == null) {
            throw new ALAdiSaisieModelException(
                    "AdiSaisieComplexModel#initModel: unable to init model - model passed is null");
        }
        adiSaisieComplexModel.setAdiSaisieModel(ALServiceLocator.getAdiSaisieModelService().initModel(
                adiSaisieComplexModel.getAdiSaisieModel(), listeASaisir));
        return adiSaisieComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieComplexModelService #read(java.lang.String)
     */
    @Override
    public AdiSaisieComplexModel read(String idSaisieModel) throws JadePersistenceException, JadeApplicationException {
        if (JadeStringUtil.isEmpty(idSaisieModel)) {
            throw new ALAdiSaisieModelException("unable to read adiSaisieComplexModel- the id passed is empty");

        }
        AdiSaisieComplexModel adiSaisieComplexModel = new AdiSaisieComplexModel();
        adiSaisieComplexModel.setId(idSaisieModel);

        return (AdiSaisieComplexModel) JadePersistenceManager.read(adiSaisieComplexModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiSaisieComplexModelService
     * #search(ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel)
     */
    @Override
    public AdiSaisieComplexSearchModel search(AdiSaisieComplexSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException {
        if (searchModel == null) {
            throw new ALAdiSaisieModelException(
                    "unable to search adiSaisieComplexModel- the search model passed is null");
        }

        return (AdiSaisieComplexSearchModel) JadePersistenceManager.search(searchModel);
    }

}
