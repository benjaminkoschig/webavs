package ch.globaz.pegasus.businessimpl.services.chrysaor;

import static org.junit.Assert.*;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.pegasus.JadeThreadContextUtil;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobActionSource;
import ch.globaz.pegasus.business.models.externalmodule.ExternalModule;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.AdaptationParameter;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ExternalModuleParameters;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class ChrysaorServiceImplTest {

    @Before
    public void startTest() {
        JadeThreadContextUtil.startContext();
    }

    @After
    public void shutdownContext() {
        JadeThreadContextUtil.shutdownContext();
    }

    @Test
    public void testJobSubmission() throws JadeApplicationServiceNotAvailableException, JadePersistenceException {

        ExternalJobActionSource actionSource = ExternalJobActionSource.ADAPTATION;
        ExternalModuleParameters parameter = new AdaptationParameter("12");
        ExternalModule<ExternalModuleParameters> module = PegasusServiceLocator.getChrysaorService().sendJobFor(
                actionSource, parameter);

        assertNotNull(module);
        assertNotNull(module.getSimpleExternalModule());
        assertNotNull(module.getSimpleExternalModule().getId());

        AdaptationParameter parameterAsObject = module.getSimpleExternalModule().getParameterAsObject();

        assertNotNull(parameterAsObject);

    }

}
