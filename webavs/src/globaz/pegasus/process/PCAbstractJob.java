package globaz.pegasus.process;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.lots.SimpleLot;

public abstract class PCAbstractJob extends AbstractJadeJob {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void addError(Exception e) {
        this.addError(e, null);
    }

    public void addError(Exception e, String[] param) throws JadeNoBusinessLogSessionError {

        JadeThread.logError("", (e.getMessage() != null) ? e.getMessage() : e.toString(), param);
        String cause = "";
        Throwable currentException = null;
        JadeBusinessMessage message = null;
        currentException = e;
        while (currentException.getCause() != null) {
            message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, getName(), currentException.getCause()
                    .toString());
            getLogSession().addMessage(message);
            currentException = currentException.getCause();
        }
        message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, getName(), e.toString() + " " + cause);
        getLogSession().addMessage(message);
    }

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

    protected abstract void process() throws Exception;

    @Override
    public void run() {
        // Défini l'utilisation du thread context
        try {
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));
            process();
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    "Exception thrown during Pegasus Job Processing, exception : " + e.toString());

            List<String> l = new ArrayList<String>();
            l.add(getSession().getUserEMail());
            try {
                sendCompletionMail(l);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            // Si en erreur, alors...
            // TODO Voir avec VCH ce qu'il avait prévu comme intégration avec AbstractJadeJob
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    /**
     * 
     * @param emailAdresses
     * @param simpleLot
     * @throws Exception
     */
    protected final void sendMail(List<String> emailAdresses, SimpleLot simpleLot) throws Exception {
        if (emailAdresses == null) {
            throw new NullPointerException("cannot send completion mail: dest list is null");
        }

        if (emailAdresses.isEmpty()) {
            return;
        }

        BSession theSession = getSession();

        if (theSession == null) {
            throw new IllegalStateException("cannot send completion mail: user session is null.");
        }

        JadeBusinessLogSession logs = getLogSession();

        String logSession = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(logs.getMessages(), getSession().getIdLangueISO());

        String body = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(JadeThread.logMessages(), JadeThread.currentLanguage());

        // Si en erreur ajout ligne au body + logSession
        if ((logs.getMaxLevel() == JadeBusinessMessageLevels.ERROR)) {
            body = body + " \n\r" + logSession;
        }

        String subject = "La comptablisation du lot(" + simpleLot.getIdLot() + ") : " + simpleLot.getDescription()
                + " s'est terminé avec succès";
        if ((logs.getMaxLevel() == JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            subject = "Erreur(s) dans la comptablisation du lot(" + simpleLot.getIdLot() + ") : "
                    + simpleLot.getDescription();
            body = body + " \n\r" + logSession;
        } else if (logs.getMaxLevel() == JadeBusinessMessageLevels.WARN) {
            subject = "Avertissements(s) dans la comptablisation du lot(" + simpleLot.getIdLot() + ") : "
                    + simpleLot.getDescription();
        }

        String[] emailsAsArray = emailAdresses.toArray(new String[emailAdresses.size()]);
        assert emailsAsArray != null : "emailsAsArray is null!";
        for (int i = 0; i < emailsAsArray.length; i++) {
            if (emailsAsArray[i] == null) {
                throw new NullPointerException(
                        "Cannot send completion mails: an email is null in List. No single mail sent!");
            }
        }
        JadeSmtpClient.getInstance().sendMail(emailsAsArray, subject, body, null);
    }
}
