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

public class SimpleExternalModuleTest {

    @Before
    public void startTest() {
        JadeThreadContextUtil.startContext();
    }

    @After
    public void shutdownContext() {
        JadeThreadContextUtil.shutdownContext();
    }

    @Ignore
    public void testSimpleInsertionWithJsonParameter() throws JadePersistenceException {

        SimpleExternalModule<ComptabilisationParameter> module = new SimpleExternalModule<ComptabilisationParameter>();
        module.setEtatJob(ExternalJobEtat.DONE);
        module.setSourceAction(ExternalJobActionSource.COMPTABILISATION);
        module.setParameterAsJson(new ComptabilisationParameter("12"));
        // module.setParameters(new SimpleExternalModule.Parameter("12").toJson());

        JadePersistenceManager.add(module);
        JadeThreadContextUtil.commitSession();

        assertNotNull(module);
        assertNotNull(module.getIdJob());
        assertNotNull(module.getId());
        assertNotNull(module.getParameters());
        assertNotNull(module.getSourceAction());

        SimpleExternalModule<ComptabilisationParameter> module2 = (SimpleExternalModule) JadePersistenceManager
                .read(module);

        ComptabilisationParameter params = module2.getParameterAsObject();

        assertNotNull(params);
        assertNotNull(params.getIdLot());

        JadePersistenceManager.delete(module);
        JadeThreadContextUtil.commitSession();

    }

    @Ignore
    @Test(expected = NullPointerException.class)
    public void testSimpleInsertionWithParameterClassNull() throws JadePersistenceException {

        SimpleExternalModule<ComptabilisationParameter> module = new SimpleExternalModule<ComptabilisationParameter>();
        module.setEtatJob(ExternalJobEtat.DONE);
        module.setSourceAction(ExternalJobActionSource.RELANCE_REST);
        module.setParameterAsJson(new ComptabilisationParameter("12"));
        // module.setParameters(new SimpleExternalModule.Parameter("12").toJson());

        JadePersistenceManager.add(module);
        JadeThreadContextUtil.commitSession();

        assertNotNull(module);
        assertNotNull(module.getIdJob());
        assertNotNull(module.getId());
        assertNotNull(module.getParameters());
        assertNotNull(module.getSourceAction());

        SimpleExternalModule<ComptabilisationParameter> module2 = (SimpleExternalModule) JadePersistenceManager
                .read(module);

        JadePersistenceManager.delete(module);
        JadeThreadContextUtil.commitSession();

        ComptabilisationParameter params = module2.getParameterAsObject();

    }

    @Test
    public void testSimpleInsertion() throws JadePersistenceException {

        SimpleExternalModule module = new SimpleExternalModule();
        module.setEtatJob(ExternalJobEtat.DONE);
        module.setSourceAction(ExternalJobActionSource.ADAPTATION);
        module.setParameters("as");

        JadePersistenceManager.add(module);
        JadeThreadContextUtil.commitSession();

        assertNotNull(module);
        assertNotNull(module.getIdJob());
        assertNotNull(module.getId());
        assertNotNull(module.getParameters());
        assertNotNull(module.getSourceAction());

        JadePersistenceManager.delete(module);
        JadeThreadContextUtil.commitSession();

    }

}
