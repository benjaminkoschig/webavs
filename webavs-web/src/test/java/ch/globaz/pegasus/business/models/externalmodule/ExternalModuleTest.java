package ch.globaz.pegasus.business.models.externalmodule;

import static org.junit.Assert.*;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.JadeThreadContextUtil;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ComptabilisationParameter;

public class ExternalModuleTest {

    @Before
    public void startTest() {
        JadeThreadContextUtil.startContext();
    }

    @After
    public void shutdownContext() {
        JadeThreadContextUtil.shutdownContext();
    }

    @Ignore
    @Test
    public void testSimpleInsertion() throws JadePersistenceException {

        SimpleExternalModule<ComptabilisationParameter> module = new SimpleExternalModule<ComptabilisationParameter>();
        module.setSourceAction(ExternalJobActionSource.COMPTABILISATION);
        module.setParameterAsJson(new ComptabilisationParameter("99"));
        module.setEtatJob(ExternalJobEtat.RUNNING);

        ExternalModule<ComptabilisationParameter> moduleExterne = new ExternalModule<ComptabilisationParameter>();
        moduleExterne.setSimpleExternalModule(module);

        JadePersistenceManager.add(moduleExterne.getSimpleExternalModule());
        JadeThreadContextUtil.commitSession();

        assertNotNull(moduleExterne.getSimpleExternalModule());
        assertNotNull(moduleExterne.getSimpleExternalModule().getIdJob());
        assertNotNull(moduleExterne.getSimpleExternalModule().getId());
        assertNotNull(moduleExterne.getSimpleExternalModule().getParameters());
        assertNotNull(moduleExterne.getSimpleExternalModule().getSourceAction());

        JadePersistenceManager.delete(moduleExterne.getSimpleExternalModule());
        JadeThreadContextUtil.commitSession();

    }
}
