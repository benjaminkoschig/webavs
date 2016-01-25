package ch.globaz.pegasus.businessimpl.services.chrysaor;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobActionSource;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobEtat;
import ch.globaz.pegasus.business.models.externalmodule.ExternalModule;
import ch.globaz.pegasus.business.models.externalmodule.SimpleExternalModule;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ExternalModuleParameters;
import ch.globaz.pegasus.business.services.chrysaor.ChrysaorService;

public class ChrysaorServiceImpl implements ChrysaorService {

    @Override
    public ExternalModule<ExternalModuleParameters> sendJobFor(ExternalJobActionSource source,
            ExternalModuleParameters parameter) throws JadePersistenceException {

        if (null == source) {
            throw new IllegalArgumentException("The source action cannot be null");
        }

        ExternalModule<ExternalModuleParameters> moduleExterneJob = saveAndGetModuleFor(source, parameter);

        return moduleExterneJob;

    }

    @SuppressWarnings("unchecked")
    private <E> ExternalModule<E> saveAndGetModuleFor(ExternalJobActionSource source, ExternalModuleParameters parameter)
            throws JadePersistenceException {

        ExternalModule<E> moduleExterneJob = new ExternalModule<E>();
        moduleExterneJob.getSimpleExternalModule().setParameterAsJson((E) parameter);
        moduleExterneJob.getSimpleExternalModule().setEtatJob(ExternalJobEtat.SUBMIT);
        moduleExterneJob.getSimpleExternalModule().setSourceAction(source);

        moduleExterneJob.setSimpleExternalModule((SimpleExternalModule<E>) JadePersistenceManager.add(moduleExterneJob
                .getSimpleExternalModule()));

        JadeLogger.info(this, " [Chrysaor] new job successfully submitted for chrysaor, job id: "
                + moduleExterneJob.getSimpleExternalModule().getIdJob() + ", for the action : " + source);
        return moduleExterneJob;

    }
}
