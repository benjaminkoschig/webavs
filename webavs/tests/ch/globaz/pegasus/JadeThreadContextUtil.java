package ch.globaz.pegasus;

import static org.junit.Assert.*;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.utils.SessionForTestBuilder;

public class JadeThreadContextUtil {

    public static void startContext() {
        try {
            BSession session = SessionForTestBuilder.getSession("PEGASUS", "ccjuglo", "glob4az");
            JadeThreadActivator.startUsingJdbcContext(JadeThreadContextUtil.class,
                    SessionForTestBuilder.initContext(session).getContext());
        } catch (Exception e1) {
            e1.printStackTrace();
            fail();
        }
    }

    public static void commitSession() {
        try {
            try {
                JadeThread.commitSession();
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public static void shutdownContext() {
        JadeThreadActivator.stopUsingContext(JadeThreadContextUtil.class);
    }
}
