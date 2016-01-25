package ch.globaz.perseus.tests.util;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import junit.framework.TestCase;

public class End extends TestCase {

    public void test() {
        assertTrue(true);
    }

    @Override
    public void tearDown() {
        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        JadeThreadActivator.stopUsingContext(this);
        System.out.println("Completed");
    }
}
