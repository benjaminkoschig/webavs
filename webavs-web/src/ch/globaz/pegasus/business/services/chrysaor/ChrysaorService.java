package ch.globaz.pegasus.business.services.chrysaor;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobActionSource;
import ch.globaz.pegasus.business.models.externalmodule.ExternalModule;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ExternalModuleParameters;

public interface ChrysaorService extends JadeApplicationService {

    public ExternalModule<ExternalModuleParameters> sendJobFor(ExternalJobActionSource source,
            ExternalModuleParameters parameter) throws JadePersistenceException;

}
