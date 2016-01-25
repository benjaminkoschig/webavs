package globaz.perseus.process;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.ArrayList;
import java.util.List;

public abstract class PFAbstractJob extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final JadeContext initContext(BSession session) throws Exception {
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

    public void logError(String message) {
        if (getLogSession().hasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeThread.logError(this.getClass().getName(), message);
        }
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }
    }

    public boolean logErrorWithReturnBoolean(String message) {
        boolean hasError = false;

        if (getLogSession().hasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            logError(message);
            hasError = true;
        }
        return hasError;
    }

    public void logMessage(String message) {
        getLogSession().info(this.getClass().getName(), message);
    }

    protected abstract void process() throws Exception;

    @Override
    public void run() {
        // Défini l'utilisation du thread context
        try {
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));
            process();
        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(this.getClass().getName(),
                    "Exception thrown during Perseus Job Processing, exception : " + e.toString());
        } finally {
            // Si en erreur, alors...
            // TODO Voir avec VCH ce qu'il avait prévu comme intégration avec AbstractJadeJob
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    public void sendCompletionMail(String eMail) {
        List<String> email = new ArrayList<String>();
        email.add(eMail);
        try {
            this.sendCompletionMail(email);
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    "Exception thrown during Perseus Job Processing, exception : " + e.toString());
        }
    }

}
