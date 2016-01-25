package ch.globaz.pegasus.tests.util;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;

public class BaseTestCase {
    private static boolean standAloneTest = false;

    public static void setUp() throws Exception {
        Object ctx = JadeThread.currentContext();
        if (ctx == null) {
            standAloneTest = true;
            Jade.getInstance();
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK")
                    .newSession("ccjuglo", "glob4az");
            JadeThreadActivator.startUsingJdbcContext(BaseTestCase.class, Init.initContext(session).getContext());
            JadeThread.currentContext().storeTemporaryObject("bsession", session);
        }
    }

    public static void tearDown() {
        if (standAloneTest) {
            if (JadeThread.logHasMessages()) {
                JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
                System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
            }
            System.out.println("STOPING CONTEXT");
            JadeThreadActivator.stopUsingContext(BaseTestCase.class);
        }
    }

}
