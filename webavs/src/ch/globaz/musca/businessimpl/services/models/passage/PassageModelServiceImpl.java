package ch.globaz.musca.businessimpl.services.models.passage;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.musca.business.exceptions.models.PassageModelException;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.models.PassageSearchModel;
import ch.globaz.musca.business.services.models.passage.PassageModelService;

/**
 * Classe d'implémentation des services persistencs de PassageModel
 * 
 * @author GMO
 * 
 */
public class PassageModelServiceImpl implements PassageModelService {

    @Override
    public PassageModel read(String idPassage) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idPassage)) {
            throw new PassageModelException(
                    "PassageModelServiceImpl#read: unable to read the passage, idPassage is empty");
        }
        PassageModel passageModel = new PassageModel();
        passageModel.setId(idPassage);
        return (PassageModel) JadePersistenceManager.read(passageModel);
    }

    @Override
    public PassageSearchModel search(PassageSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new PassageModelException("PassageModelServiceImpl#search:unable to search, searchModel is null");
        }

        return (PassageSearchModel) JadePersistenceManager.search(searchModel);
    }

}
