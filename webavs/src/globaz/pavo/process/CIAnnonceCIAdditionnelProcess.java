package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;
import globaz.pavo.util.CIEnvoiEmailGroupes;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;

/**
 * Rassemble les écritures du jour à annoncer en tant que ci additionnel Date de création : (25.11.2002 11:52:37)
 * Périodicité: journalière
 * 
 * @author: dgi
 */
public class CIAnnonceCIAdditionnelProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String dateOrdre = null;
    private boolean echoToConsole = false;

    private CIEnvoiEmailGroupes groupEmails;

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIAnnonceCIAdditionnelProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIAnnonceCIAdditionnelProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        BITransaction remoteTransaction = null;
        try {
            if (echoToConsole) {
                System.out.println("starting process...");
            }
            // init
            CIApplication application = (CIApplication) getSession().getApplication();
            BISession remoteSession = application.getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();

            CIRassemblementOuvertureManager ciAddMgr = new CIRassemblementOuvertureManager();
            ciAddMgr.setSession(getSession());
            if (dateOrdre != null) {
                ciAddMgr.setForDateOrdre(dateOrdre);
            } // else {
              // ciAddMgr.setForDateOrdre(application.getCalendar().todayjjMMMMaaaa());
              // }
            ciAddMgr.setForTypeEnregistrement(CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS);
            ciAddMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            ciAddMgr.find(getTransaction());
            for (int j = 0; j < ciAddMgr.size(); j++) {
                CIRassemblementOuverture ciAdd = (CIRassemblementOuverture) ciAddMgr.getEntity(j);
                try {
                    if (CIUtil.isCIAddionelNonConforme(getSession())) {
                        if (JAUtil.isIntegerEmpty(dateOrdre)) {
                            dateOrdre = JACalendar.todayJJsMMsAAAA();
                        }
                        if (JAUtil.isIntegerEmpty(ciAdd.getMotifArc())
                                || JAUtil.isIntegerEmpty(ciAdd.getCaisseAgenceCommettante())) {
                            String message = getSession().getLabel("MSG_CI_ADD_NON_CONFORME")
                                    + NSUtil.formatAVSUnknown(ciAdd.getCi().getNumeroAvs());
                            // getTransaction().addErrors("CI non-conforme");
                            envoiEmailErreurTr(getTransaction(), message);
                            getTransaction().clearErrorBuffer();
                            getTransaction().rollback();
                            continue;

                        }
                    }

                    if (CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.equals(ciAdd.getTypeEnregistrementWA())) {
                        if (echoToConsole) {
                            System.out.println("processing CI id " + ciAdd.getCompteIndividuelId());
                        }
                        ciAdd.annonceEcritures(getTransaction(), remoteSession, remoteTransaction);
                        ciAdd.setTypeEnregistrement(CIRassemblementOuverture.CS_CI_ADDITIONNEL);
                        ciAdd.wantCallMethodBefore(false);
                        ciAdd.update(getTransaction());
                        // si on n'a pas d'erreurs on log un message d'envoi à l'utilisateur
                        if (!getTransaction().hasErrors() && !remoteTransaction.hasErrors()) {
                            getMemoryLog().logMessage("CI " + ciAdd.getCompteIndividuelId() + " sent",
                                    FWMessage.INFORMATION, "CI Add");
                            getTransaction().commit();
                        }
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                } finally {
                    if (getTransaction().hasErrors() || remoteTransaction.hasErrors()) {
                        // convertir le nss pour préparer le message
                        String nssconverted = NSUtil.formatAVSUnknown(ciAdd.getNumeroAvs());
                        // récupérer les erreurs de la transaction ET de la remote
                        StringBuffer errors = getTransaction().getErrors();
                        errors.append(remoteTransaction.getErrors());
                        // préparer le message d'erreurs
                        String ErrorMsg = getSession().getLabel("CI_ADD_NOT_SENT_BEGIN") + " " + nssconverted + " "
                                + getSession().getLabel("CI_ADD_NOT_SENT_END");
                        // envoyer un email au client
                        JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(),
                                getSession().getLabel("CI_ADD_NOT_SENT"), ErrorMsg + "\n" + errors.toString(), null);
                        // logger
                        JadeLogger.error(this.getClass().getName() + errors.toString(), ErrorMsg);
                        // nettoyer les erreurs et rollbacker
                        getTransaction().clearErrorBuffer();
                        getTransaction().rollback();
                        ((BTransaction) remoteTransaction).clearErrorBuffer();
                        remoteTransaction.rollback();
                    }
                }
            }
        } catch (Exception e) {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.rollback();
                } catch (Exception ex) {
                }
            }
            if (echoToConsole) {
                e.printStackTrace();
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception ex) {
                }
            }
        }
        if (echoToConsole) {
            System.out.println("Process done.");
        }
        return !isAborted();
    }

    public void envoiEmail(ArrayList to, String sujet, String message) throws Exception {
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        if (groupEmails != null) {
            if (to == null) {
                groupEmails.addEmail(null, sujet, message);
            } else {
                for (int i = 0; i < to.size(); i++) {
                    groupEmails.addEmail((String) to.get(i), sujet, message);
                }
            }
        } else {
            if (to == null) {
                // adresse non trouvée -> envoie à admin
                application.sendEmailToAdmin(getSession().getLabel("MSG_ANNONCE_EMAIL_ERREUR"), message, null);
            } else {
                for (int i = 0; i < to.size(); i++) {
                    JadeSmtpClient.getInstance().sendMail((String) to.get(i), sujet, message, null);
                }
            }
        }
    }

    public void envoiEmailErreurTr(BTransaction transaction, String description) throws Exception {
        // if(transaction.hasErrors()) {
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        String message = description;
        ArrayList to = application.getEMailResponsableCI(transaction);
        envoiEmail(to, getSession().getLabel("MSG_ANNONCE_ERR_CI_ADD"), message);
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError()) {
            return "Le traitement journalier des envois de CI additionnel a echoué!";
        } else {
            return "Le traitement journalier des envois de CI additionnel s'est effectué avec succès.";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     * 
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

}
