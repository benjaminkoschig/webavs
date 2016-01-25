package globaz.orion.process;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.job.common.JadeJobAbortable;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;

/**
 * @author JMC
 * 
 *         Classe mère définissant le contexte général pour l'exécution de process dans WEB@AVS pour e-Business
 */
public abstract class EBAbstractJadeJob extends AbstractJadeJob implements JadeJobAbortable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean abort = false;
    private boolean initThreadContext = true;

    public boolean getInitThreadContext() {
        return initThreadContext;
    }

    public void setInitThreadContext(boolean initThreadContext) {
        this.initThreadContext = initThreadContext;
    }

    @Override
    public final void abort() {
        abort = true;
    }

    /**
     * @param session
     *            Session web en cours d'utilisation
     * @return Context d'exécution du thread
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

    public final boolean isAborted() {
        return abort;
    }

    /**
     * Méthode de déclenchement du process à implémenter dans toutes les classes filles
     */
    protected abstract void process() throws Exception;

    @Override
    public final void run() {
        try {
            // Défini l'utilisation du thread context
            if (initThreadContext) {
                JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));
                JadeThread.storeTemporaryObject("bsession", getSession());
            }
            process();
        } catch (Exception e) {
            // TODO Do better !!!
            JadeLogger.error(this, "Exception thrown during (" + this.getClass().getName()
                    + ") execution !!! Reason : " + e.toString());
        } finally {
            if (initThreadContext) {
                // Libère le context - Si nécessaire, commit ou rollback la
                // transaction
                JadeThreadActivator.stopUsingContext(this);
            }
        }
    }

    protected boolean threadOnError() {
        return JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
    }

    /**
     * Envoie un mail de complétion du process aux adresses email données. Le sujet du message varie en fonction du
     * logSession (ok, erreurs, warnings, ...). Le contenu du mail correspond au contenu du logSession. Une exception
     * sera jetée si la session de cet objet n'est pas settée. Cette session est nécessaire pour déterminer la langue de
     * l'utilisateur et lui envoyer le message.
     * 
     * @param emailAdresses
     *            les adresses email à qui envoyer le mail. Doit contenir au moins une adresse sinon rien n'est envoyé
     *            et rien ne se passe. Ne doit pas être <code>null</code>.
     * @throws Exception
     *             si l'email ne peut être envoyé
     * @see #getMemoryLog()
     */

}
