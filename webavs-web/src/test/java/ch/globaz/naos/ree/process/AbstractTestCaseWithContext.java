package ch.globaz.naos.ree.process;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractTestCaseWithContext {

    private BSession session;

    /**
     * Must return the target application name
     * 
     * @return the application name
     */
    protected String getApplicationName() {
        return TestConfig.getDefaultConfig().getApplicationName();
    }

    protected String getEnvironnementName() {
        return TestConfig.getDefaultConfig().name();
    }

    /**
     * @return the session
     */
    public final BSession getSession() {
        return session;
    }

    /**
     * Must return the user name to connect it to the DB
     * 
     * @return the user name to connect it to the DB
     */
    protected String getUserName() {
        return TestConfig.getDefaultConfig().getUserName();
    }

    /**
     * Must return the user password to connect it to the DB
     * 
     * @return the user password to connect it to the DB
     */
    protected String getUserPassword() {
        return TestConfig.getDefaultConfig().getUserPassword();
    }

    private JadeContextImplementation initContext() throws Exception {
        session = (BSession) GlobazServer.getCurrentSystem().getApplication(getApplicationName())
                .newSession(getUserName(), getUserPassword());
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(getApplicationName());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        JadeThreadContext context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context.getContext();
    }

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream sysout = System.out;
        System.setOut(ps);
        JadeThreadActivator.startUsingJdbcContext(this, initContext());
        System.setOut(sysout);
        System.out.println("AbstractTestCaseWithContext.setUp() : CONTEXT STARTED");

    }

    @After
    public void tearDown() {
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            throw new RuntimeException(JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                    .render(JadeThread.logMessages(), "fr"));
        }
        System.out.println("AbstractTestCaseWithContext.tearDown() : STOPING CONTEXT");
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        JadeThreadActivator.stopUsingContext(this);
    }

}
