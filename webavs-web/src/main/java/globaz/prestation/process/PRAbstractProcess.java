package globaz.prestation.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.JadeLogger;

/**
 * <p>
 * Structure de base pour un processus dans les prestations.
 * </p>
 * <p>
 * Permet de charger la session dans le thread context avant le lancement du process, et d'arrêter le thread contexte à
 * la fin de l'execution.<br/>
 * Ceci permet d'utiliser des services, utilisant la nouvelle persistance, dans ce processus même s'il est basé sur
 * l'ancien framework.
 * </p>
 * 
 * @author PBA
 */
public abstract class PRAbstractProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean threadContextInitialized;

    public PRAbstractProcess() {
        super();
        threadContextInitialized = false;
    }

    public PRAbstractProcess(boolean initThreadContext, BProcess process) {
        super(process);
        threadContextInitialized = initThreadContext;
    }

    public PRAbstractProcess(boolean initThreadContext, BSession session) {
        super(session);
        threadContextInitialized = initThreadContext;
    }

    @Override
    protected final boolean _executeProcess() throws Exception {
        boolean processResult = false;

        try {
            if (isThreadContextInitialized()) {
                initThreadContext();
            }

            processResult = runProcess();
        } catch (Exception ex) {
            ex.printStackTrace();
            getMemoryLog().logMessage("test", FWMessage.ERREUR, this.getClass().toString());
            JadeLogger.error(this, ex.toString());
            throw ex;
        } finally {
            stopThreadContext();
        }

        return processResult;
    }

    private void initThreadContext() throws Exception {
        JadeContextImplementation context = new JadeContextImplementation();
        context.setApplicationId(getSession().getApplicationId());
        context.setLanguage(getSession().getIdLangueISO());
        context.setUserEmail(getSession().getUserEMail());
        context.setUserId(getSession().getUserId());
        context.setUserName(getSession().getUserName());

        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(getSession().getUserId());
        if ((roles != null) && (roles.length > 0)) {
            context.setUserRoles(JadeConversionUtil.toList(roles));
        }

        JadeThreadActivator.startUsingJdbcContext(this, context);
        JadeThread.storeTemporaryObject("bsession", getSession());
        JadeThread.storeTemporaryObject("executeJob", this);
    }

    /**
     * Si le contexte doit être initialisé au démarrage du process.
     * 
     * @return <code>true</code> si le contexte sera initialisé au démarrage du process, sinon <code>false</code>
     */
    public boolean isThreadContextInitialized() {
        return threadContextInitialized;
    }

    protected abstract boolean runProcess() throws Exception;

    /**
     * <p>
     * Défini si le contexte doit être initialisé au démarrage du process.
     * </p>
     * <p>
     * N'aura plus aucun effet si le process est déjà lancé.
     * </p>
     * 
     * @param threadContextInitialized
     *            <code>true</code> ou <code>false</code>
     */
    public void setThreadContextInitialized(boolean threadContextInitialized) {
        this.threadContextInitialized = threadContextInitialized;
    }

    private void stopThreadContext() {
        JadeThreadActivator.stopUsingContext(this);
    }
}
