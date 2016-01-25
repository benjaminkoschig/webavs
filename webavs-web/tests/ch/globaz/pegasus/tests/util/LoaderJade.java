package ch.globaz.pegasus.tests.util;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import ch.globaz.pegasus.web.application.PCApplication;

public class LoaderJade {
    public static JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(PCApplication.DEFAULT_APPLICATION_PEGASUS);
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

    private String pass = "pctest";
    private String user = "pctest";

    protected void createCasTest() throws Exception {

        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                for (CasTest casToSave : TestBaseData.casForTest.values()) {
                    if ((casToSave.getIdTiers() != null) && !JadeStringUtil.isBlankOrZero(casToSave.getIdTiers())) {
                        casToSave = CasTestUtil.createDossierAndDemandeAndDroitInitial(casToSave);
                        // TestBaseData.casForTest.put(casToSave.getNSS(), casToSave);
                    }
                }
            }
        }.run();

    }

    protected void flushPCBd() throws JadePersistenceException {
        System.out.println("> Starting flushing db2pc tables");
        for (PCDB2Table table : PCDB2Tables.pcTables.values()) {
            PegasusPersistanceUtils.deletwByCsPY(table.getTableName(), this.getClass());
        }
        System.out.println("> Db2pc tables flushing complete");
    }

    public void setUp() throws Exception {
        System.out.println("Startup");
        Jade.getInstance();
        System.out.println(">User and pass default: " + user + " - " + pass);
        BSession session = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(PCApplication.DEFAULT_APPLICATION_PEGASUS).newSession(user, pass);

        JadeThreadActivator.startUsingJdbcContext(this, LoaderJade.initContext(session).getContext());
        JadeThread.currentContext().storeTemporaryObject("bsession", session);
    }

    public void setUserAndPass(String userPass[]) {
        if (!JadeStringUtil.isBlank(userPass[0])) {
            user = userPass[0];
        }
        if (!JadeStringUtil.isBlank(userPass[1])) {
            pass = userPass[1];
        }
        System.out.println(">User and pass settings modified: " + user + " - " + pass);
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
