package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.application.ApplicationConstants;

public abstract class BAbstractEntityExternalServiceWithContext extends BAbstractEntityExternalService {
    private JadeContext context;

    protected void startUsingThreadContext(BSession session) {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext(session));
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    protected void stopUsingThreadContext() {
        JadeThreadActivator.stopUsingContext(Thread.currentThread());
    }

    private JadeContext getContext(BSession session) throws Exception {
        if (context == null) {
            context = initContext(session).getContext();
        }
        return context;
    }

    private JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA);
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
