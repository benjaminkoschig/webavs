package ch.globaz.pegasus.businessimpl.services.chrysaor;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobActionSource;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobEtat;
import ch.globaz.pegasus.business.models.externalmodule.ExternalModule;
import ch.globaz.pegasus.business.models.externalmodule.ExternalModuleSearch;
import ch.globaz.pegasus.business.models.externalmodule.SimpleExternalModule;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ExternalModuleParameters;
import ch.globaz.pegasus.business.services.chrysaor.ChrysaorService;
import com.google.gson.GsonBuilder;

public class ChrysaorServiceImpl implements ChrysaorService {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ExternalModule<ExternalModuleParameters> sendJobFor(ExternalJobActionSource source,
            ExternalModuleParameters parameter) throws JadePersistenceException {

        if (null == source) {
            throw new IllegalArgumentException("The source action cannot be null");
        }

        ExternalModule externalModule = getExternalModule(source, parameter);

        if (ensureThatJobNotAlreadySubmitted(parameter)) {
            externalModule = saveModule(externalModule);

            JadeLogger.info(this, " [Chrysaor] job submitted for chrysaor, job id: "
                    + externalModule.getSimpleExternalModule().getIdJob() + ", for the action : " + source);
        } else {
            JadeLogger.info(this, " [Chrysaor] job not submitted for chrysaor, job id: "
                    + externalModule.getSimpleExternalModule().getIdJob() + ", for the action : " + source
                    + " , job already exist");
        }

        return externalModule;

    }

    private boolean ensureThatJobNotAlreadySubmitted(@SuppressWarnings("rawtypes") ExternalModuleParameters parameters)
            throws JadePersistenceException {
        ExternalModuleSearch search = new ExternalModuleSearch();
        GsonBuilder builder = new GsonBuilder();
        search.setForParameters(builder.create().toJson(parameters));
        int nbreJobExistant = JadePersistenceManager.count(search);

        return nbreJobExistant == 0;
    }

    @SuppressWarnings("unchecked")
    private <E> ExternalModule<E> saveModule(ExternalModule<E> externalModule) throws JadePersistenceException {

        // ExternalModule<E> moduleExterneJob = new ExternalModule<E>();
        // moduleExterneJob.getSimpleExternalModule().setParameterAsJson((E) parameter);
        // moduleExterneJob.getSimpleExternalModule().setEtatJob(ExternalJobEtat.SUBMIT);
        // moduleExterneJob.getSimpleExternalModule().setSourceAction(source);

        externalModule.setSimpleExternalModule((SimpleExternalModule<E>) JadePersistenceManager.add(externalModule
                .getSimpleExternalModule()));

        JadeLogger.info(this, " [Chrysaor] new job successfully submitted for chrysaor, job id: "
                + externalModule.getSimpleExternalModule().getIdJob() + ", for the action : "
                + externalModule.getSimpleExternalModule().getSourceAction());
        return externalModule;

    }

    @SuppressWarnings("unchecked")
    private <E> ExternalModule<E> getExternalModule(ExternalJobActionSource source,
            @SuppressWarnings("rawtypes") ExternalModuleParameters parameter) {
        ExternalModule<E> moduleExterneJob = new ExternalModule<E>();
        moduleExterneJob.getSimpleExternalModule().setParameterAsJson((E) parameter);
        moduleExterneJob.getSimpleExternalModule().setEtatJob(ExternalJobEtat.SUBMIT);
        moduleExterneJob.getSimpleExternalModule().setSourceAction(source);
        return moduleExterneJob;
    }
}
