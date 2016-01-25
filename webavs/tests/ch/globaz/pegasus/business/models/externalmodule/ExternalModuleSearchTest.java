package ch.globaz.pegasus.business.models.externalmodule;

import static org.junit.Assert.*;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.JadeThreadContextUtil;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ComptabilisationParameter;

public class ExternalModuleSearchTest {

    // @Before
    public void startTest() {
        JadeThreadContextUtil.startContext();
    }

    // @After
    public void shutdownContext() {
        JadeThreadContextUtil.shutdownContext();
    }

    @Ignore
    @Test
    public void testForId() throws JadePersistenceException {
        SimpleExternalModule<ComptabilisationParameter> module = new SimpleExternalModule<ComptabilisationParameter>();
        module.setEtatJob(ExternalJobEtat.DONE);
        module.setSourceAction(ExternalJobActionSource.COMPTABILISATION);
        module.setParameterAsJson(new ComptabilisationParameter("12"));

        JadePersistenceManager.add(module);
        JadeThreadContextUtil.commitSession();

        ExternalModuleSearch search = new ExternalModuleSearch();
        search.setForIdJob(module.getIdJob());
        search = (ExternalModuleSearch) JadePersistenceManager.search(search);

        assertTrue(search.getSearchResults().length == 1);

        JadePersistenceManager.delete(module);
        JadeThreadContextUtil.commitSession();

    }

    @Ignore
    @Test
    public void testForEtatJob() throws JadePersistenceException {
        SimpleExternalModule<ComptabilisationParameter> module = new SimpleExternalModule<ComptabilisationParameter>();
        module.setEtatJob(ExternalJobEtat.DONE);
        module.setSourceAction(ExternalJobActionSource.COMPTABILISATION);
        module.setParameterAsJson(new ComptabilisationParameter("12"));

        JadePersistenceManager.add(module);
        JadeThreadContextUtil.commitSession();

        ExternalModuleSearch search = new ExternalModuleSearch();
        search.setForEtatJob(ExternalJobEtat.DONE);
        search = (ExternalModuleSearch) JadePersistenceManager.search(search);

        assertTrue(search.getSearchResults().length == 1);

        SimpleExternalModule<ComptabilisationParameter> module2 = new SimpleExternalModule<ComptabilisationParameter>();
        module2.setEtatJob(ExternalJobEtat.DONE);
        module2.setSourceAction(ExternalJobActionSource.COMPTABILISATION);
        module2.setParameterAsJson(new ComptabilisationParameter("13"));

        JadePersistenceManager.add(module2);
        JadeThreadContextUtil.commitSession();

        search = new ExternalModuleSearch();
        search.setForEtatJob(ExternalJobEtat.DONE);
        search = (ExternalModuleSearch) JadePersistenceManager.search(search);

        assertTrue(search.getSearchResults().length == 2);

        JadePersistenceManager.delete(module);
        JadePersistenceManager.delete(module2);
        JadeThreadContextUtil.commitSession();

    }

    @Ignore
    @Test
    public void testForSourceActionJob() throws JadePersistenceException {
        SimpleExternalModule<ComptabilisationParameter> module = new SimpleExternalModule<ComptabilisationParameter>();
        module.setEtatJob(ExternalJobEtat.DONE);
        module.setSourceAction(ExternalJobActionSource.COMPTABILISATION);
        module.setParameterAsJson(new ComptabilisationParameter("12"));

        JadePersistenceManager.add(module);
        JadeThreadContextUtil.commitSession();

        ExternalModuleSearch search = new ExternalModuleSearch();
        search.setForSourceActionJob(ExternalJobActionSource.COMPTABILISATION);
        search = (ExternalModuleSearch) JadePersistenceManager.search(search);

        assertTrue(search.getSearchResults().length == 1);

        SimpleExternalModule<ComptabilisationParameter> module2 = new SimpleExternalModule<ComptabilisationParameter>();
        module2.setEtatJob(ExternalJobEtat.DONE);
        module2.setSourceAction(ExternalJobActionSource.COMPTABILISATION);
        module2.setParameterAsJson(new ComptabilisationParameter("13"));

        JadePersistenceManager.add(module2);
        JadeThreadContextUtil.commitSession();

        search = new ExternalModuleSearch();
        search.setForSourceActionJob(ExternalJobActionSource.COMPTABILISATION);
        search = (ExternalModuleSearch) JadePersistenceManager.search(search);

        assertTrue(search.getSearchResults().length == 2);

        search = new ExternalModuleSearch();
        search.setForSourceActionJob(ExternalJobActionSource.ADAPTATION);
        search = (ExternalModuleSearch) JadePersistenceManager.search(search);

        assertTrue(search.getSearchResults().length == 0);

        JadePersistenceManager.delete(module);
        JadePersistenceManager.delete(module2);
        JadeThreadContextUtil.commitSession();

    }
}
