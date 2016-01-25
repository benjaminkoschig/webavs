package globaz.pavo.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.print.list.CIAnnoncesCentrale_Doc;
import globaz.pavo.print.list.CIAnnoncesCentrale_Doc_Summary;
import globaz.pavo.util.CIEnvoiEmailGroupes;
import java.util.ArrayList;

/**
 * @author jpa Exécute le process CIAnnonceCentraleProcess en tenant compte des périodes où l'on ne peut envoyer les
 *         annonces à la centrale et tenant compte des lots
 */
public class CIAnnonceCentraleParPeriodeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static String anneeEnCours = "";
    private static int dateCurrent = 0;
    private static int dateDebut = 0;
    private static int dateFin = 0;
    private static String erreurMessage = "";
    private static CIEnvoiEmailGroupes groupEmails;
    private static int limiteMin = 0;
    private static int maxEcritures;
    private static String message = "";
    private static String methodeOnFailure = "";
    private static CIAnnonceCentraleProcess myProcess;
    public final static int NO_ERROR = 0;
    public final static int ON_ERROR = 200;
    private static String subject = "";

    /**
     * On est avant le début de la période d'annonces
     */
    private static void genererBeforeDate() {
        System.exit(NO_ERROR);
    }

    /**
     * On est dans la bonne période, mais on a pas assez d'annonces pour faire un lot
     */
    private static void genererMinimum() {
        System.exit(NO_ERROR);
    }

    public static CIAnnonceCentraleParPeriodeProcess getInstance(BSession session) {
        CIAnnonceCentraleParPeriodeProcess newInstance = new CIAnnonceCentraleParPeriodeProcess();
        newInstance.setSession(session);
        return newInstance;
    }

    /**
     * Parse la date pour pouvoir faire des comparaison de dates
     * 
     * @param date
     *            de la forme JJ.MM.YYYY
     * @return date de la forme MMJJ
     */
    private static int parseDate(String dateAParser) {
        String date = "";
        int dateRetour;
        StringBuffer parseur = new StringBuffer(dateAParser);
        date = parseur.substring(3, 5) + parseur.substring(0, 2);
        dateRetour = JAUtil.parseInt(date, 0);
        return dateRetour;
    }

    private String dateCourante;
    private String propDateDebut;

    private String propDateFin;

    public CIAnnonceCentraleParPeriodeProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            myProcess = new CIAnnonceCentraleProcess(getSession());

            initProcess(getSession());
            myProcess.setSession(getSession());
            testPeriodeAnnonce(getSession(), getTransaction());
            if ((!myProcess.getMemoryLog().isOnErrorLevel()) && (erreurMessage.length() <= 0)) {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process CIAnnonceCentraleParPeriodeProcess executed successfully !");
                System.exit(NO_ERROR);
                return true;
            } else {
                // il y a eu une erreur dans l'exécution du processus, je
                // retourne le code de retour erreur
                getMemoryLog().logMessage("Process CIAnnonceCentraleParPeriodeProcess has error(s) !" + erreurMessage,
                        null, FWViewBeanInterface.ERROR, getClass().getName());
                System.out.println("Process CIAnnonceCentraleParPeriodeProcess has error(s) !");
                envoiEmailErr(erreurMessage, getSession());
                System.exit(ON_ERROR);
                return false;
            }
            // *************************************************************/
        } catch (Exception e) {
            methodeOnFailure = "_executeProcess()";
            message += "\n error : CIAnnonceCentraleParPeriodeProcess : _executeProcess() \n";
            JadeLogger.info("CIAnnonceCentraleParPeriodeProcess : _executeProcess()", e.getMessage());
            envoiEmailErr(erreurMessage, getSession());
            System.exit(ON_ERROR);
            return false;
        }
    }

    private int compteAnnoncesRestantes(BTransaction transaction, BSession session) {
        // On calcul le nombre d'annonces restantes à envoyer
        int NombreAnnonces = 0;
        CIEcritureManager ecrituresAAnnoncer = new CIEcritureManager();
        ecrituresAAnnoncer.setSession(session);
        // on cherche celle qui ne sont pas encore envoyées
        ecrituresAAnnoncer.setForDateAnnonceCentrale("0");
        // prendre seulement les genres 6,7
        ecrituresAAnnoncer.setForIdTypeCompteCompta("true");
        try {
            ecrituresAAnnoncer.cursorOpen(transaction);
            NombreAnnonces = ecrituresAAnnoncer.getCount();
            if (NombreAnnonces == 0) {
                genererMinimum();
            }
        } catch (Exception e) {
            JadeLogger.info("CIAnnonceCentraleParPeriodeProcess : compteAnnonceRestantes(Btransaction, BSession)",
                    e.getMessage() + "nombre annonces :" + NombreAnnonces);
            message += session.getLabel("MSG_ANNONCE_ERR_PROCESS_PAS_ANNONCE");
            message += "\n error : CIAnnonceCentraleParPeriodeProcess : compteAnnonceRestantes(Btransaction, BSession) \n";
            envoiEmailErr(erreurMessage, session);
            System.exit(ON_ERROR);
        }
        return NombreAnnonces;
    }

    public void envoiEmail(String sujet, String message, BSession session) throws Exception {
        groupEmails = new CIEnvoiEmailGroupes((CIApplication) session.getApplication());
        groupEmails.setSession(session);
        ArrayList to = ((CIApplication) session.getApplication()).getEMailResponsableCI(getTransaction());
        if (to == null) {
            groupEmails.addEmail(null, sujet, message);
        } else {
            for (int i = 0; i < to.size(); i++) {
                groupEmails.addEmail((String) to.get(i), sujet, message);
            }
        }
        groupEmails.send();
    }

    private void envoiEmailErr(String description, BSession session) {
        try {
            envoiEmail(subject, message, session);
        } catch (Exception e) {
            JadeLogger.info("CIAnnonceCentraleParPeriodeProcess : envoiEmailErr(String, BSession)", e.getMessage());
        }
    }

    private void envoiRapport(BSession session, BTransaction transaction) throws FWIException {
        try {
            // on imprime le rapport
            String file[] = new String[2];
            String sujet = session.getLabel("ANNONCESCENTRALE");
            String message = "";
            CIAnnoncesCentrale_Doc process;
            CIAnnoncesCentrale_Doc_Summary summary;
            if (!myProcess.getMemoryLog().isOnErrorLevel()) {
                CIApplication application = (CIApplication) transaction.getSession().getApplication();
                String to = getEMailAddress();
                process = new CIAnnoncesCentrale_Doc(session);
                process.setSendCompletionMail(false);
                process.setForAnnee(anneeEnCours);
                process.executeProcess();
                file[0] = process.getExporter().getExportNewFilePath();
                summary = new CIAnnoncesCentrale_Doc_Summary(session);
                summary.setSendCompletionMail(false);
                summary.setForAnnee(anneeEnCours);
                summary.executeProcess();
                file[1] = summary.getExporter().getExportNewFilePath();
                if (to == null) {
                    JadeSmtpClient.getInstance()
                            .sendMail(application.getEmailAdmin(), null, null, sujet, message, file);
                } else {
                    JadeSmtpClient.getInstance().sendMail(to, null, null, sujet, message, file);

                }
            }
        } catch (Exception e) {
            message += "\n envoiRapport Error";
            message += "\n error : CIAnnonceCentraleParPeriodeProcess : envoiRapport(BSession, Btransaction) \n";
            envoiEmailErr(erreurMessage, getSession());
            JadeLogger.info("CIAnnonceCentraleParPeriodeProcess : envoiRapport(BSession, BTransaction))",
                    e.getMessage());
            System.exit(ON_ERROR);
        }
    }

    /**
     * On est à la fin de la période d'annonces, il faut générer tous les lots possibles
     */
    private void genererDernier(BSession session, BTransaction transaction) {
        myProcess.setMaxEcrituresASelect(maxEcritures);
        myProcess.setTypeEnregistrement(CIAnnonceCentrale.TYPE_ENR_DERNIERE_ANNONCE_ANNEE);
        myProcess.setNbreLotMax(0);
        // on imprime le rapport
        CIAnnoncesCentrale_Doc process;
        try {
            myProcess.executeProcess();
            envoiRapport(session, transaction);
        } catch (Exception e) {
            message += "\n error : CIAnnonceCentraleParPeriodeProcess : genererDernier(BSession, Btransaction) \n";
            erreurMessage += " \n";
        }
    }

    /**
     * On se trouve dans le cas d'un intercalaire, bonne période d'annonces et on a assez d'annonces pour générer un lot
     */
    private void genererIntercalaire(BSession session, BTransaction transaction) {
        myProcess.setMaxEcrituresASelect(maxEcritures);
        myProcess.setTypeEnregistrement(CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE);
        myProcess.setNbreLotMax(1);
        try {
            myProcess.executeProcess();
            envoiRapport(session, transaction);
        } catch (Exception e) {
            erreurMessage += " \n";
            message += "\n error : CIAnnonceCentraleParPeriodeProcess : genererIntercalaire(BSession, BTransaction) \n";
        }
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError()) {
            return "L'annonce des inscriptions a réussi";
        } else {
            return "L'annonce des inscriptions a écouée";
        }
    }

    /**
     * @param session
     * @return
     */
    private String getErreurMSG(BSession session) {
        String mess = new String();
        mess = session.getLabel("MSG_ANNONCE_ERR_PROCESS_SUJET");
        mess += "\n\n";
        mess += "\n" + session.getLabel("MSG_ANNONCE_ERR_DATE_DEB") + ": " + dateDebut;
        mess += "\n" + session.getLabel("MSG_ANNONCE_ERR_DATE_FIN") + ": " + dateFin;
        mess += "\n" + session.getLabel("MSG_ANNONCE_ERR_DATE_CUR") + ": " + dateCurrent;
        mess += "\n" + session.getLabel("MSG_ANNONCE_ERR_LIMITE_MIN") + ": " + limiteMin;
        mess += "\n" + session.getLabel("MSG_ANNONCE_ERR_MAX_ECRI") + ": " + maxEcritures;
        mess += "\n\n";
        return mess;
    }

    /**
     * initialise les paramètres du process : dates et limites (properties)
     */
    private void initProcess(BSession session) {
        subject = session.getLabel("MSG_ANNONCE_ERR_PROCESS_SUJET");
        message = getErreurMSG(session);
        // On récupère les dates nécessaires sous forme MMJJ
        try {
            dateCurrent = parseDate(JACalendar.todayJJsMMsAAAA());
            StringBuffer parseur = new StringBuffer(JACalendar.todayJJsMMsAAAA());
            anneeEnCours = parseur.substring(6, 10);
            dateDebut = parseDate(session.getApplication().getProperty("annonce.periode.debut"));
            dateFin = parseDate(session.getApplication().getProperty("annonce.periode.fin"));
            limiteMin = JAUtil.parseInt(session.getApplication().getProperty("annonce.limite.min"), 0);
            maxEcritures = JAUtil.parseInt(session.getApplication().getProperty("annonce.limite.max"), 0);
            message = getErreurMSG(session);
            if ((dateCurrent == 0) || (dateDebut == 0) || (dateFin == 0) || (limiteMin == 0) || (maxEcritures == 0)) {
                erreurMessage += " \n";
                envoiEmailErr(erreurMessage, session);
                System.exit(ON_ERROR);
            }
        } catch (Exception e) {
            message += session.getLabel("MSG_ANNONCE_ERR_PROP");
            message += "\n error : CIAnnonceCentraleParPeriodeProcess : initProcess(BSession) \n";
            erreurMessage += " \n";
            envoiEmailErr(erreurMessage, session);
            System.exit(ON_ERROR);
        }

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void testPeriodeAnnonce(BSession session, BTransaction transaction) {
        // On test si on a dépasser la date de début des annonces
        if (dateDebut <= dateCurrent) {
            // on test si on a dépassé la période de fin
            if (dateFin >= dateCurrent) {
                // on test si on a le nombre limite d'annonces
                if (compteAnnoncesRestantes(transaction, session) >= limiteMin) {
                    // c'est un type intercalaire
                    genererIntercalaire(session, transaction);
                } else {
                    // on a pas atteint la limite min et c'est un intercalaire,
                    // donc attente
                    genererMinimum();
                }
            } else {
                // on a dépassé la date de fin, il faut tout générer
                genererDernier(session, transaction);
            }
        } else {
            // On est pas encore dans la période d'envoi
            genererBeforeDate();
        }
    }
}
