package ch.globaz.pegasus.tests.util;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import junit.framework.Assert;

public abstract class LogTemplate {

    public void displayLog() {
        if (JadeThread.logHasMessages()) {
            if (JadeThread.logMessages().length > 0) {
                System.out
                        .println("-----------------------------(((((((((log debut))))))))----------------------------");
            }
            String body = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                    .render(JadeThread.logMessages(), "fr");

            if (JadeThread.logMessages().length > 0) {
                System.out.println(body);
                System.out.println("-----------------------------(((((((((log fin))))))))----------------------------");
            }

            if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                JadeThread.logClear();
                Assert.fail("Buissness error: " + body);
            }
            JadeThread.logClear();
        }
    }

    protected abstract void execute() throws Exception;

    public void run() throws Exception {

        displayLog();

        execute();

        displayLog();

    }

}
