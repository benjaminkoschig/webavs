package ch.globaz.perseus.tests.util;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;

public class BaseTestCase {
    private boolean standAloneTest = false;

    public void setUp() throws Exception {
        Object ctx = JadeThread.currentContext();
        if (ctx == null) {
            standAloneTest = true;
            Jade.getInstance();
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK")
                    .newSession("globazf", "globazf");
            JadeThreadActivator.startUsingJdbcContext(this, Init.initContext(session).getContext());
            JadeThread.currentContext().storeTemporaryObject("bsession", session);
        }
    }

    public void tearDown() {
        if (standAloneTest) {
            if (JadeThread.logHasMessages()) {
                JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
                System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
            }
            System.out.println("STOPING CONTEXT");
            JadeThreadActivator.stopUsingContext(this);
        }
    }

}
