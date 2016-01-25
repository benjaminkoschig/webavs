package ch.globaz.perseus.tests.util;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.perseus.web.application.PFApplication;

public class Init {
    public static JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(PFApplication.DEFAULT_APPLICATION_PERSEUS);
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    public void setUp() throws Exception {
        System.out.println("Startup");
        Jade.getInstance();
        BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK")
                .newSession("testcalc", "testcalc");
        JadeThreadActivator.startUsingJdbcContext(this, Init.initContext(session).getContext());
        JadeThread.currentContext().storeTemporaryObject("bsession", session);
    }

    @Test
    @Ignore
    public void test() {
        Assert.assertTrue(true);
    }
}
