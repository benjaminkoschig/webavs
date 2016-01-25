package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.RetroLotOVInit;
import ch.globaz.pegasus.tests.util.Init;
import ch.globaz.pegasus.tests.util.LoaderJade;

public class InitTestForBd {
    @AfterClass
    public static void killJade() {
        LoaderJade loaderJade = new LoaderJade();
        loaderJade.tearDown();
    }

    @BeforeClass
    public static void loadJade() throws Exception {
        LoaderJade loaderJade = new LoaderJade();
        loaderJade.setUp();
        // RetroLotOVInit.clearTablePC();
    }

    public void clearTablePC() throws JadePersistenceException, Exception {
        RetroLotOVInit.clearTablePC();
    }

    @Before
    public void setUp() throws Exception {

        Object ctx = JadeThread.currentContext();
        if (ctx == null) {
            // this.standAloneTest = true;
            Jade.getInstance();
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK")
                    .newSession("pctest", "pctest");
            JadeThreadActivator.startUsingJdbcContext(this, Init.initContext(session).getContext());
            JadeThread.currentContext().storeTemporaryObject("bsession", session);
        }
        clearTablePC();
    }

    @After
    public void tearDown() throws Exception {
        // if (this.standAloneTest) {
        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        JadeThread.logClear();
        JadeThread.commitSession();
        System.out.println("STOPING CONTEXT");
        JadeThreadActivator.stopUsingContext(this);
    }
}
