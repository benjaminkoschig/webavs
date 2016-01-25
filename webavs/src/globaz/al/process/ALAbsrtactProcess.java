package globaz.al.process;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstDocument;

/**
 * @author VCH
 * 
 *         Classe mère définissant le contexte général pour l'exécution de process dans Web@AF
 */
public abstract class ALAbsrtactProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Méthode permettant d'avoir certaines informations de l'utilisateur (couple clé, valeur)
     * 
     * @return <HashMap> contenant des infos spécifiques de l'utilisateur
     */
    protected HashMap<String, String> getUserInfo() {
        HashMap<String, String> userInfos = new HashMap<String, String>();

        userInfos.put(ALConstDocument.USER_MAIL, getSession().getUserEMail());
        userInfos.put(ALConstDocument.USER_NAME, getSession().getUserFullName());
        userInfos.put(ALConstDocument.USER_PHONE, getSession().getUserInfo().getPhone());
        userInfos.put(ALConstDocument.USER_VISA, getSession().getUserInfo().getVisa());

        return userInfos;
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

    /**
     * Méthode de déclenchement du process à implémenter dans toutes les classes filles
     */
    protected abstract void process();

    @Override
    public final void run() {
        try {
            // Défini l'utilisation du thread context
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));
            JadeThread.storeTemporaryObject("bsession", getSession());
            process();
        } catch (Exception e) {
            // TODO Do better !!!
            JadeLogger.error(this, "Exception thrown during (" + this.getClass().getName()
                    + ") execution !!! Reason : " + e.toString());
        } finally {
            // Libère le context - Si nécessaire, commit ou rollback la
            // transaction
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    protected void sendMail() {
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        try {
            sendCompletionMail(emails);
        } catch (Exception e) {
            JadeLogger.error(
                    this,
                    JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "globaz.al.process.generic.err_mail_result")
                            + e.getMessage() + ", " + e.getCause());
        }
    }
}
