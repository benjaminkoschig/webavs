package ch.globaz.musca.businessimpl.services.models.passage;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.musca.business.exceptions.models.PassageModelException;
import ch.globaz.musca.business.models.PassageModuleComplexModel;
import ch.globaz.musca.business.models.PassageModuleComplexSearchModel;
import ch.globaz.musca.business.services.models.passage.PassageModuleComplexModelService;

public class PassageModuleComplexModelServiceImpl implements PassageModuleComplexModelService {

    @Override
    public PassageModuleComplexModel read(String idPassage) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idPassage)) {
            throw new PassageModelException(
                    "Unable to read model (PassageModuleComplexModel) - the id passed is not defined!");
        }
        PassageModuleComplexModel passageModuleComplexModel = new PassageModuleComplexModel();
        passageModuleComplexModel.setId(idPassage);

        return (PassageModuleComplexModel) JadePersistenceManager.read(passageModuleComplexModel);
    }

    @Override
    public PassageModuleComplexSearchModel search(PassageModuleComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new PassageModelException("PassageModuleComplexModelServiceImpl#search : searchModel is null");
        }

        return (PassageModuleComplexSearchModel) JadePersistenceManager.search(searchModel);
    }

}
