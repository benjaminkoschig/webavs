package globaz.amal.process;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;

/**
 * @author VCH
 * 
 *         Classe m�re d�finissant le contexte g�n�ral pour l'ex�cution de process dans Web@AF
 */
public abstract class AMALabstractProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param session
     *            Session web en cours d'utilisation
     * @return Context d'ex�cution du thread
     * @throws Exception
     *             Erreur lors du setting du thread context
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

    /**
     * M�thode de d�clenchement du process � impl�menter dans toutes les classes filles
     */
    protected abstract void process();

    @Override
    public final void run() {
        try {
            // D�fini l'utilisation du thread context
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));
            JadeThread.storeTemporaryObject("bsession", getSession());
            process();
        } catch (Exception e) {
            // TODO Do better !!!
            JadeLogger.error(this, "Exception thrown during (" + this.getClass().getName()
                    + ") execution !!! Reason : " + e.toString());
        } finally {
            // Lib�re le context - Si n�cessaire, commit ou rollback la
            // transaction
            JadeThreadActivator.stopUsingContext(this);
        }
    }
}
