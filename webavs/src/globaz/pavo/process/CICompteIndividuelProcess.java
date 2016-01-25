package globaz.pavo.process;

import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HEEnvoiEmailsGroupe;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeInitProperties;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceSuspens;
import globaz.pavo.db.compte.CIAnnonceSuspensManager;
import globaz.pavo.db.compte.CIAnnonceWrapper;
import globaz.pavo.db.compte.CIAnnonceWrapperComparator;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.upidaily.CIUpiDailyProcess;
import globaz.pavo.util.CIEnvoiEmailGroupes;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Traitement journalier des comptes individules (ca 21 et 23). Date de création : (25.11.2002 11:52:37)
 * 
 * @author: Administrator
 */
public class CICompteIndividuelProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        String uid = "";
        if (args.length > 0) {
            uid = args[0];
        }
        String email = "";
        if (args.length > 1) {
            email = args[1];
        }
        String password = "";
        if (args.length > 2) {
            password = args[2];
        }
        boolean alone = false;
        if (args.length > 3) {
            alone = "alone".equals(args[3].trim()) ? true : false;
        }
        String logg = "false";
        if (args.length > 4) {
            logg = args[4];
        }

        boolean echoToConsole = true;
        CICompteIndividuelProcess process = null;
        try {

            if (logg.equalsIgnoreCase("true")) {
                JadeInitProperties.setLogDirectory("d:\\myLogs");
                Jade.getInstance().beginProfiling(CICompteIndividuel.class, args);
            }
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(uid, password);
            // traitement CI
            process = new CICompteIndividuelProcess((BSession) session);
            process.setEMailAddress(email);
            process.setEchoToConsole(echoToConsole);
            process.setControleTransaction(true);
            process.executeProcess();

            if (!alone) {
                // traitement splitting
                CISplittingProcess processSpl = new CISplittingProcess((BSession) session);
                processSpl.setEMailAddress(email);
                processSpl.setEchoToConsole(echoToConsole);
                processSpl.setControleTransaction(true);
                processSpl.executeProcess();

                // traitement CI additionnel
                CIAnnonceCIAdditionnelProcess processCIAdd = new CIAnnonceCIAdditionnelProcess((BSession) session);
                processCIAdd.setEMailAddress(email);
                processCIAdd.setEchoToConsole(echoToConsole);
                processCIAdd.setControleTransaction(true);
                processCIAdd.executeProcess();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Jade.getInstance().endProfiling();
        }

    }

    private boolean echoToConsole = false;

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CICompteIndividuelProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CICompteIndividuelProcess(globaz.globall.db.BSession session) {
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
        boolean succesCi = true;
        boolean succesOther = true;
        try {
            // Booléans qui permettent de savoir si les traitement s'effectuent
            // correctement

            if (echoToConsole) {

                String logOut = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/CIProcess/out/"
                        + "CICompteIndivduelProcess" + DateUtils.getLocaleDateAndTime() + ".log";
                String logErr = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/CIProcess/err/"
                        + "CICompteIndivduelProcess" + DateUtils.getLocaleDateAndTime() + ".log";
                StringUtils.createDirectory(logOut);
                StringUtils.createDirectory(logErr);
                PrintStream streamOut = new PrintStream(new FileOutputStream(logOut));
                System.setOut(streamOut);
                PrintStream streamErr = new PrintStream(new FileOutputStream(logErr));
                System.setErr(streamErr);
                System.out.println("starting CI process...");
            }
            // init
            CIApplication application = (CIApplication) getSession().getApplication();
            BISession remoteSession = application.getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();
            // groupe d'email
            CIEnvoiEmailGroupes emailGroups = new CIEnvoiEmailGroupes(application);
            emailGroups.setSession(getSession());
            //
            if (echoToConsole) {
                System.out.println("Executing step 1...");
            }
            // traitements des révocations
            CIAnnonceSuspensManager annonces = new CIAnnonceSuspensManager();
            annonces.setSession(getSession());
            annonces.setOrderBy("KMDREC, KMNAVS, KMITTR");
            annonces.setForIdTypeTraitementList(new String[] { CIAnnonceSuspens.CS_REVOCATION_CLOTURE,
                    CIAnnonceSuspens.CS_REVOCATION_SPLITTING });
            annonces.changeManagerSize(BManager.SIZE_NOLIMIT);
            annonces.find(getTransaction());
            if (!isAborted()) {
                String avs = "";
                boolean enSuspens = false;
                for (int i = 0; i < annonces.size(); i++) {
                    CIAnnonceWrapper annonceWrapper = ((CIAnnonceSuspens) annonces.getEntity(i)).getWrapper();
                    annonceWrapper.setApplication(application);
                    annonceWrapper.setRemoteSession(remoteSession);
                    annonceWrapper.setRemoteTransaction(remoteTransaction);
                    annonceWrapper.setGroupEmails(emailGroups);
                    if (!avs.equals(annonceWrapper.getNumeroAvs())) {
                        // gère le fait que si une annonce est en suspens,
                        // suspendre toutes les
                        // autres annonces de cet assuré
                        avs = annonceWrapper.getNumeroAvs();
                        enSuspens = false;
                    }
                    if (!enSuspens && !annonceWrapper.isAnnonceSuspens()) {
                        // sans test final
                        if (echoToConsole && !JAUtil.isStringEmpty(avs)) {
                            System.out.println("  processing " + avs + "...");
                        }
                        annonceWrapper.traiter(getTransaction(), false);
                        getMemoryLog().logMessage(avs + ": step 1 processed", FWMessage.INFORMATION, "CI");
                    }
                    if (annonceWrapper.isAnnonceSuspens()) {
                        enSuspens = true;
                    }
                }
            }
            if (echoToConsole) {
                System.out.println("Executing step 2...");
            }
            // traitements de toutes les annonces
            annonces = new CIAnnonceSuspensManager();
            annonces.setSession(getSession());
            annonces.setOrderBy("KMDREC, KMNAVS");
            annonces.changeManagerSize(BManager.SIZE_NOLIMIT);
            annonces.find(getTransaction());
            // BStatement statement = annonces.cursorOpen(getTransaction());
            if (!isAborted()) {
                String avs = "";
                boolean enSuspens = false;
                TreeSet assure = null;
                ArrayList allAnnonces = new ArrayList();
                CIAnnonceSuspens annonceSus;
                int count = 1;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy 'at' hh:mm:ss");
                // while ((annonceSus = (CIAnnonceSuspens)
                // annonces.cursorReadNext(statement)) != null) {
                for (int i = 0; i < annonces.size(); i++) {
                    annonceSus = (CIAnnonceSuspens) annonces.getEntity(i);
                    CIAnnonceWrapper annonceWrapper = annonceSus.getWrapper();
                    annonceWrapper.setApplication(application);
                    annonceWrapper.setRemoteSession(remoteSession);
                    annonceWrapper.setRemoteTransaction(remoteTransaction);
                    annonceWrapper.setGroupEmails(emailGroups);
                    if (!avs.equals(annonceWrapper.getNumeroAvs())) {
                        // gère le fait que si une annonce est en suspens,
                        // suspendre toutes les
                        // autres annonces de cet assuré
                        // si liste non null et non vide
                        if (!JAUtil.isStringEmpty(avs)) {
                            if (echoToConsole && !JAUtil.isStringEmpty(avs)) {
                                String dateString = formatter.format(new Date());
                                System.out.println("  processing nbr " + count + " (" + dateString + ") for " + avs
                                        + "...");
                                count++;
                            }
                            traiteAssure(assure, enSuspens);
                            getMemoryLog().logMessage(avs + ": step 2 processed", FWMessage.INFORMATION, "CI");
                        }
                        // nouvel assuré
                        avs = annonceWrapper.getNumeroAvs();
                        enSuspens = false;
                        // recherche du CI
                        CICompteIndividuel ci = CICompteIndividuel.loadCITemporaire(avs, getTransaction());
                        // créer la liste d'annonce pour le même assuré
                        if (ci != null && ci.isCiOuvert().booleanValue()) {
                            assure = new TreeSet(new CIAnnonceWrapperComparator(true));
                        } else {
                            assure = new TreeSet(new CIAnnonceWrapperComparator(false));
                        }
                    }
                    if (!enSuspens && !annonceWrapper.isAnnonceSuspens()) {
                        // ajout de l'annonce dans la liste triée
                        assure.add(annonceWrapper);
                        // ajout dans la liste utilisée à la fin des traitements
                        // afin d'appeler
                        // la méthode 'terminer' de toutes les annonces
                        allAnnonces.add(annonceWrapper);
                    } else {
                        enSuspens = true;
                    }
                } // for
                  // dernier assuré
                if (echoToConsole) {
                    System.out.println("  processing " + avs + "...");
                }
                traiteAssure(assure, enSuspens);
                getMemoryLog().logMessage(avs + ": step 2 processed", FWMessage.INFORMATION, "CI");
                // appel de la méthode 'terminer' des toutes les annonce
                // traitées
                // (utilisé dans le cas du splitting pour annoncer qu'une seule
                // fois le CI)
                if (echoToConsole) {
                    System.out.println("Terminating...");
                }
                Iterator it = allAnnonces.iterator();
                while (it.hasNext()) {
                    CIAnnonceWrapper wrap = (CIAnnonceWrapper) it.next();
                    wrap.terminer(getTransaction(), allAnnonces);
                    getMemoryLog().logMessage(wrap.getNumeroAvs() + ": step 3 processed", FWMessage.INFORMATION, "CI");
                }
                CIAnnonceWrapper.reset();
            }
            // commit une dernière fois la transaction remote avant de sortir
            if (!remoteTransaction.hasErrors()) {
                remoteTransaction.commit();
            } else {
                remoteTransaction.rollback();
            }
            if (echoToConsole) {
                System.out.println("Sending email...");
            }
            // envoi des emails
            emailGroups.send();
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

            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
            succesCi = false;
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
        try {
            CIApplication application = (CIApplication) getSession().getApplication();

            CISplittingProcess processSpl = new CISplittingProcess(getSession());
            processSpl.setEchoToConsole(echoToConsole);
            // processSpl.setParent(this);
            processSpl.setControleTransaction(true);
            processSpl.executeProcess();
            // traitement CI additionnel
            CIAnnonceCIAdditionnelProcess processCIAdd = new CIAnnonceCIAdditionnelProcess(getSession());
            processCIAdd.setEMailAddress(getEMailAddress());
            processCIAdd.setEchoToConsole(echoToConsole);
            processCIAdd.setControleTransaction(true);
            // processSpl.setParent(this);
            processCIAdd.executeProcess();

            // Lancement du traitement journalier du fichier NRA/UPI
            if (application.wantUpiDaily()) {
                CIUpiDailyProcess processUpi = new CIUpiDailyProcess();
                processUpi.setSession(getSession());
                processUpi.executeProcess();

                // Envoi des documents en email
                BSession sessionHermes = (BSession) ((CIApplication) getSession().getApplication())
                        .getSessionAnnonce(getSession());
                // init le(s) email(s) pour envoyer le résultat
                HEEnvoiEmailsGroupe emailListe = new HEEnvoiEmailsGroupe(sessionHermes,
                        HEEnvoiEmailsGroupe.resultat_ARC);
                if (emailListe.size() == 0) {
                    // aucun responsable configuré en base...
                    if (JadeStringUtil.isEmpty(sessionHermes.getApplication().getProperty("zas.user.email"))) {
                        throw new Exception(this.getClass().getName()
                                + " Aucune adresse n'est spécifée pour le résultat du process !");
                    }
                    emailListe.addEmail(sessionHermes.getApplication().getProperty("zas.user.email"));
                }

                boolean envoieEMail = false;
                String[] documentAll = new String[1];
                for (Iterator iter = processUpi.getAttachedDocuments().iterator(); iter.hasNext();) {
                    JadePublishDocument document = (JadePublishDocument) iter.next();
                    documentAll[0] = document.getDocumentLocation();
                    // Un document a été trouvé => on envoie le mail
                    envoieEMail = true;
                }

                if (envoieEMail) {
                    JadeSmtpClient.getInstance().sendMail(JadeConversionUtil.toStringArray(emailListe.getEmailListe()),
                            "NRA/UPI daily terminé", "", documentAll);
                }
            }

        } catch (Exception e) {
            succesOther = false;

        }
        String mailSubjectReturn = "";

        try {
            mailSubjectReturn = ((CIApplication) getSession().getApplication()).getProperty(CIApplication.CODE_CAISSE)
                    + "." + ((CIApplication) getSession().getApplication()).getProperty(CIApplication.CODE_AGENCE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (succesCi && succesOther) {
            mailSubjectReturn = "OK : " + mailSubjectReturn;
        } else {
            mailSubjectReturn = "ECHEC : " + mailSubjectReturn;
        }
        try {

            JadeSmtpClient.getInstance().sendMail(((CIApplication) getSession().getApplication()).getEmailAdmin(),
                    mailSubjectReturn, "", null);
        } catch (Exception e) {
        }

        return !isAborted();
    }

    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError()) {
            return "Le traitement journalier des comptes individuels a echoué!";
        } else {
            return "Le traitement journalier des comptes individuels s'est effectué avec succès.";
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

    /**
     * Traite les annonces d'un assuré. Date de création : (09.05.2003 15:25:33)
     * 
     * @param assure
     *            java.util.TreeSet
     */
    private void traiteAssure(TreeSet assure, boolean enSuspens) throws Exception {
        if (assure != null && !enSuspens) {
            Iterator it = assure.iterator();
            CIAnnonceWrapper annonceToRun;
            while (it.hasNext()) {
                annonceToRun = (CIAnnonceWrapper) it.next();
                annonceToRun.traiter(getTransaction(), true);
            }
        }
    }

}
