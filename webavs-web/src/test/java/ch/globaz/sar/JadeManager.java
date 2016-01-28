package ch.globaz.sar;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import ch.globaz.pegasus.tests.util.Init;

public class JadeManager {

    public static void checkNotNull(String paramname, String param) {

        if (null == param) {
            throw new NullPointerException("the param [" + paramname + "] cannot be null");
        }
    }

    public static void runJade(String user, String pass, String applicationName) throws Exception {

        checkNotNull("user", user);
        checkNotNull("pass", user);
        checkNotNull("applicationname", applicationName);

        Jade.getInstance();
        BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication(applicationName)
                .newSession(user, pass);
        JadeThreadActivator.startUsingJdbcContext(new JadeManager(), Init.initContext(session).getContext());
        JadeThread.currentContext().storeTemporaryObject("bsession", session);
    }
}
