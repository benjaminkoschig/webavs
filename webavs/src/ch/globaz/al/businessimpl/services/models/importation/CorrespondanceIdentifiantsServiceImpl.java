package ch.globaz.al.businessimpl.services.models.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.importation.ALCorrespondanceIdentifiantsModelException;
import ch.globaz.al.business.models.importation.CorrespondanceIdentifiantsModel;
import ch.globaz.al.business.services.models.importation.CorrespondanceIdentifiantsService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class CorrespondanceIdentifiantsServiceImpl extends ALAbstractBusinessServiceImpl implements
        CorrespondanceIdentifiantsService {

    @Override
    public CorrespondanceIdentifiantsModel create(CorrespondanceIdentifiantsModel model)
            throws JadeApplicationException, JadePersistenceException {

        if (model == null) {
            throw new ALCorrespondanceIdentifiantsModelException(
                    "CorrespondanceIdentifiantsServiceImpl#create : model is null");
        }

        return (CorrespondanceIdentifiantsModel) JadePersistenceManager.add(model);
    }

}
