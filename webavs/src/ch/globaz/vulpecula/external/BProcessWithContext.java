package ch.globaz.vulpecula.external;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;

public abstract class BProcessWithContext extends BProcess {
    private static final long serialVersionUID = 6914141522956367729L;

    private JadeThreadContext context;

    public BProcessWithContext() {
        super();
    }

    public BProcessWithContext(final BProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
        if (context != null) {
            stopContext(context);
        }
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        context = initializeContext();
        return true;
    }

    protected JadeThreadContext initializeContext() throws Exception {
        // initialisation du thread context et utilisation du contextjdbc
        JadeThreadContext threadContext = initThreadContext(getSession());
        JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
        return threadContext;
    }

    protected void stopContext(final JadeThreadContext context) {
        // stopper l'utilisation du context
        JadeThreadActivator.stopUsingContext(Thread.currentThread());
    }

    protected JadeThreadContext initThreadContext(final BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
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
}
