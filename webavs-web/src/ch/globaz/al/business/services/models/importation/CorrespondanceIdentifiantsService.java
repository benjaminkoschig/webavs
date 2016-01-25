package ch.globaz.al.business.services.models.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.importation.CorrespondanceIdentifiantsModel;

public interface CorrespondanceIdentifiantsService extends JadeApplicationService {

    public CorrespondanceIdentifiantsModel create(CorrespondanceIdentifiantsModel model)
            throws JadeApplicationException, JadePersistenceException;
}