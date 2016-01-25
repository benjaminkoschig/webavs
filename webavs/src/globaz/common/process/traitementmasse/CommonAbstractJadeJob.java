package globaz.common.process.traitementmasse;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.JadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class CommonAbstractJadeJob implements JadeJob {

    private static final long serialVersionUID = 1L;

    /**
     * Une barre de progression.
     */
    private JadeProgressBarModel jobProgress = new JadeProgressBarModel(1);

    /**
     * Une logSession.
     */
    private transient JadeBusinessLogSession logSession = null;

    /**
     * Une session utilisateur.
     */
    private transient BSession theUserSession = null;

    public CommonAbstractJadeJob() {
        super();
    }

    /**
     * Le destructeur.
     */
    @Override
    protected void finalize() throws Throwable {
        JadeBusinessLogger.getInstance().getLogAdapter().removeLogSession(getLogSession().getUUID());
        System.out.println(getLogSession().getUUID() + " removed.");
        super.finalize();
    }

    public abstract String getAdresseEmail();

    /**
     * Fournit un {@link JadeBusinessLogSession JadeBusinessLogSession} pour logguer les éventuels messages d'erreur. La
     * première fois qu'on utilise cette méthode, la logSession est créé.
     * 
     * @return la logSession de ce job. Jamais <code>null</code>.
     */
    protected final JadeBusinessLogSession getLogSession() {
        if (logSession == null) {
            logSession = JadeBusinessLogger.getInstance().getLogAdapter().createLogSession();
        }
        return logSession;
    }

    /**
     * Donne la progression de ce process.
     * 
     * @see JadeProgressBarModel#getProgressRatio()
     * @exception ArithmeticException
     *                si le maximum est setté à zéro.
     */
    @Override
    public final float getProgress() {
        return jobProgress.getProgressRatio();
    }

    /**
     * Donne la barre de progression de ce job.
     * 
     * @return la barre de progression de ce job. Jamais <code>null</code>.
     */
    protected final JadeProgressBarModel getProgressHelper() {
        return jobProgress;
    }

    /**
     * Fournit la sesssion utilisateur associée à ce process.
     * 
     * @return la session utilisateur de ce process. Peut être <code>null</code> .
     */
    @Override
    public final BSession getSession() {
        return theUserSession;
    }

    /**
     * Retourne la stackeTrace d'une exception sous forme de String
     * 
     * @param throwable
     *            l'exception
     * @return la stackTrace sous forme de String
     */
    protected String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return System.getProperty("line.separator") + sw.getBuffer().toString();
    }

    protected void handleException(Exception e, String errorKey) {
        JadeLogger.error("Error during process", e);
        JadeThread.logError(this.getClass().getName(), errorKey);
        JadeThread.logError(this.getClass().getName(), getStackTrace(e));

    }

    protected final JadeContext initContext(BSession session) throws Exception {
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
     * Renvoie le nom de la file d'attente d'exécution.
     * 
     * @return le nom de la file d'attente d'exécution
     */
    @Override
    public String jobQueueName() {
        return null;
    }

    protected abstract String process() throws Exception;

    @Override
    public void run() {
        try {
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));

            long startTime = System.currentTimeMillis();

            String processResultFileName = process();

            long elapsedTime = (System.currentTimeMillis() - startTime) / 60000;
            JadeThread.logInfo(this.getClass().getName(), "common.process.traitement.masse.duree.execution",
                    new String[] { String.valueOf(elapsedTime) });

            String[] mailAttachmentFilesName = null;
            if (!JadeStringUtil.isEmpty(processResultFileName)) {
                mailAttachmentFilesName = new String[] { processResultFileName };
            }

            sendDefaultCompletionMail(mailAttachmentFilesName);
        } catch (Exception e) {
            JadeLogger.error("Error during job excecution", e);
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    protected void sendDefaultCompletionMail(String[] mailAttachmentFilesName) throws Exception {
        JadeBusinessMessage[] messages = JadeThread.logMessages();

        String subject = JadeThread.getMessage("common.process.traitement.masse.excution.ok");
        if (JadeThread.logMaxLevel() == JadeBusinessMessageLevels.ERROR) {
            subject = JadeThread.getMessage("common.process.traitement.masse.excution.ko");
        } else if (JadeThread.logMaxLevel() == JadeBusinessMessageLevels.WARN) {
            subject = JadeThread.getMessage("common.process.traitement.masse.excution.warning");
        }

        String body = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(messages, getSession().getIdLangueISO());

        JadeSmtpClient.getInstance().sendMail(getAdresseEmail(), subject, body, mailAttachmentFilesName);

    }

    /**
     * Donne une session à ce process.
     * 
     * @param session
     *            une session pour ce process. Attention à bien réfléchir à ce qu'on fait si on passe une session
     *            <code>null</code>.
     */
    @Override
    public final void setSession(BSession session) {
        theUserSession = session;
    }

    /**
     * Fournit une transaction à ce process.
     * 
     * @param transaction
     *            une transaction pour ce process. Attention à bien réfléchir à ce qu'on fait si on passe une
     *            transaction <code>null</code>.
     */
    @Override
    public final void setTransaction(BTransaction transaction) {
    }
}
