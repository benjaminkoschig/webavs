package ch.globaz.pegasus.tests.util;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;

public class End {

    public void test() {
        // assertTrue(true);
    }

    public void tearDown() {
        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        JadeThreadActivator.stopUsingContext(this);
        System.out.println("Completed");
    }
}
