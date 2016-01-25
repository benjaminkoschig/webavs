package ch.globaz.pegasus.business.models.externalmodule;

import static org.junit.Assert.*;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.utils.SessionForTestBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ExternalModuleTest.class, SimpleExternalModuleTest.class, ExternalModuleSearchTest.class })
public class ExternalModuleTestSuite {

    @Before
    public void startTest() {
        try {
            BSession session = SessionForTestBuilder.getSession("PEGASUS", "ccjuglo", "glob4az");
            JadeThreadActivator.startUsingJdbcContext(this, SessionForTestBuilder.initContext(session).getContext());
        } catch (Exception e1) {
            e1.printStackTrace();
            fail();
        }
    }

    @After
    public void shutdownContext() {
        JadeThreadActivator.stopUsingContext(this);
    }
}
