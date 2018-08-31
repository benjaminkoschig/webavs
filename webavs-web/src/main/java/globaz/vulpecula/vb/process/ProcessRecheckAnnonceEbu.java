package globaz.vulpecula.vb.process;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.daemon.suividecompte.AbstractDaemon;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.ws.services.TravailleurEbuServiceImpl;

public class ProcessRecheckAnnonceEbu extends AbstractDaemon {
    private BSession bsession;

    @Override
    public void run() {
        try {
            initBsession();
            JadeThreadContext threadContext = initThreadContext(bsession);
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            List<TravailleurEbuDomain> listToCheck = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                    .findAllSansQuittance();
            for (TravailleurEbuDomain travailleur : listToCheck) {
                TravailleurEbuServiceImpl.setStatus(travailleur);
            }

        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
            closeBsession();
        }
    }

    private void initBsession() throws Exception {
        bsession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(bsession, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    protected JadeThreadContext initThreadContext(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }
}
