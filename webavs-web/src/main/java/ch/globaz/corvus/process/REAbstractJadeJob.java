package ch.globaz.corvus.process;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;

public abstract class REAbstractJadeJob extends AbstractJadeJob {

    private static final long serialVersionUID = -3122803680911878744L;

    /**
     * Initialisation du context
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private JadeContext initContext(BSession session) throws Exception {
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
        return ctxtImpl;
    }

    @Override
    public final void run() {
        try {
            // Défini l'utilisation du thread context
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));
            JadeThread.storeTemporaryObject("bsession", getSession());
            process();
        } catch (Exception e) {
            JadeLogger.error(this, "Exception thrown during (" + this.getClass().getName()
                    + ") execution !!! Reason : " + e.toString());
        } finally {
            // Libère le context - Si nécessaire, commit ou rollback la transaction
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    /**
     * Process à exécuter. Doit être implémenté par la classe enfant
     * 
     */
    protected abstract void process();

}
