package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEConfigurationServiceListViewBean;
import globaz.hermes.db.gestion.HEConfigurationServiceViewBean;
import globaz.hermes.process.HEExtraitAnnonceProcess;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HEEnvoiEmailsGroupe;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeInitProperties;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.splitting.CIDossierSplitting;
import globaz.pavo.db.splitting.CIDossierSplittingManager;
import globaz.pavo.db.splitting.CIMandatSplitting;
import globaz.pavo.db.splitting.CIMandatSplittingManager;
import globaz.pavo.util.CIEnvoiEmailGroupes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Traitement journalier de <tt>CIMandatSplitting</tt> et <tt>CIDossierSplitting</tt>. Date de création : (25.11.2002
 * 08:58:37)
 * 
 * @author: Administrator
 */
public class CISplittingProcess extends BProcess {

    private static final long serialVersionUID = 3910310687497249741L;

    public static void main(String[] args) {
        String uid = "";
        if (args.length > 0) {
            uid = args[0];
        }
        String email = "";
        if (args.length > 1) {
            email = args[1];
        }
        String logg = "false";
        if (args.length > 2) {
            logg = args[2];
        }
        String password = "";
        if (args.length > 3) {
            password = args[3];
        }
        CISplittingProcess process = null;
        try {
            if (logg.equalsIgnoreCase("true")) {
                JadeInitProperties.setLogDirectory("d:\\myLogs");
                Jade.getInstance().beginProfiling(CISplittingProcess.class, args);
            }
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(uid, password);
            process = new CISplittingProcess((BSession) session);
            process.setEMailAddress(email);
            process.setEchoToConsole(true);
            process.executeProcess();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                }
                Jade.getInstance().endProfiling();
            }
        }
        System.exit(0);
    }

    private CIApplication application;
    private TreeMap container = new TreeMap();
    private boolean echoToConsole = false;

    private CIEnvoiEmailGroupes emailGroups;

    /**
     * Constructeur de CIMiseAJourSplitting.
     */
    public CISplittingProcess() {
        super();
    }

    /**
     * Constructeur de CIMiseAJourSplitting.
     * 
     * @param parent
     *            le processus parent.
     */
    public CISplittingProcess(FWProcess parent) {
        super(parent);
    }

    /**
     * Constructeur de CIMiseAJourSplitting.
     * 
     * @param session
     *            la session à utiliser.
     */
    public CISplittingProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Traitement journalier concernant le splitting. Date de création : (14.02.2002 14:26:51)
     * 
     * @return true si le traitement a été effectué avec succès.
     */
    @Override
    protected boolean _executeProcess() {
        try {
            application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            emailGroups = new CIEnvoiEmailGroupes(application);
            emailGroups.setSession(getSession());
            // ArrayList to =
            // application.getEMailResponsableCI(getTransaction());
            // traitement des mandats
            CIMandatSplittingManager mandats = new CIMandatSplittingManager();
            mandats.setSession(getSession());
            mandats.changeManagerSize(BManager.SIZE_NOLIMIT);
            mandats.setATraiter("true");
            mandats.find(getTransaction());
            for (int i = 0; i < mandats.size(); i++) {
                if (isAborted()) {
                    return false;
                }
                CIMandatSplitting mandat = (CIMandatSplitting) mandats.getEntity(i);
                if (CIMandatSplitting.CS_DEMANDE_REVOCATION.equals(mandat.getIdEtat())
                        || CIMandatSplitting.CS_SPLITTING_EN_COURS.equals(mandat.getIdEtat())) {
                    if (echoToConsole) {
                        System.out.print("Mandat no " + mandat.getId());
                    }
                    mandat.wantCallMethodBefore(false);
                    mandat.updateMandat(getTransaction());
                    if (getTransaction().hasErrors()) {
                        StringBuffer errors = getTransaction().getErrors();
                        CIDossierSplitting dos = mandat.loadDossier(getTransaction());
                        ArrayList to = new ArrayList();
                        to.add(dos.getEMailResponsable(getTransaction()));
                        this.envoiEmail(to, "Splitting",
                                getSession().getLabel("MSG_MANDAT_EMAIL_MESSAGE") + dos.getIdDossierSplitting() + "/"
                                        + dos.getIdDossierInterne() + ": " + errors.toString());
                        getMemoryLog().logStringBuffer(errors, this.getClass().getName());
                        getMemoryLog().logMessage("La mise à jour d'un mandat a échoué.", FWMessage.FATAL,
                                this.getClass().getName());
                        if (echoToConsole) {
                            System.out.println(" a échoué!");
                        }
                        getTransaction().clearErrorBuffer();
                    } else {
                        if (echoToConsole) {
                            System.out.println(" mis à jour avec succès.");
                        }
                        getTransaction().commit();
                        getMemoryLog().logMessage("Mandat " + mandat.getId() + " updated", FWMessage.INFORMATION,
                                "Splitting");
                    }
                }
            }
            // traitement des dossier

            CIDossierSplittingManager dossiers = new CIDossierSplittingManager();
            dossiers.setSession(getSession());
            dossiers.changeManagerSize(BManager.SIZE_NOLIMIT);
            dossiers.setForNotIdEtat(CIDossierSplitting.CS_CLOTURE);
            dossiers.find(getTransaction());
            for (int i = 0; i < dossiers.size(); i++) {
                if (isAborted()) {
                    return false;
                }
                CIDossierSplitting dossier = (CIDossierSplitting) dossiers.getEntity(i);
                if (CIDossierSplitting.CS_A_TRAITER.equals(dossier.getIdEtat())) {
                    if (echoToConsole) {
                        System.out.print("Exécution de splitting du dossier no " + dossier.getId() + "("
                                + dossier.getTiersAssureNomComplet() + ")");
                    }
                    // lancer le splitting
                    dossier.exectueSplittingBatch(getTransaction());
                    if (getTransaction().hasErrors()) {
                        StringBuffer errors = getTransaction().getErrors();
                        ArrayList to = new ArrayList();
                        to.add(dossier.getEMailResponsable(getTransaction()));
                        this.envoiEmail(to, "Splitting",
                                getSession().getLabel("MSG_EXDOS_EMAIL_MESSAGE") + dossier.getIdDossierSplitting()
                                        + "/" + dossier.getIdDossierInterne() + ": " + errors.toString());
                        getMemoryLog().logStringBuffer(errors, this.getClass().getName());
                        getMemoryLog().logMessage(
                                "L'exécution du splitting du dossier no " + dossier.getId() + "/"
                                        + dossier.getIdDossierInterne() + " a échoué.", FWMessage.FATAL,
                                this.getClass().getName());
                        if (echoToConsole) {
                            System.out.println(" a échoué! [" + errors.toString() + "]");
                        }
                        getTransaction().clearErrorBuffer();
                    } else {
                        if (echoToConsole) {
                            System.out.println(" effectué avec succès.");
                        }
                        getMemoryLog().logMessage("Dossier " + dossier.getId() + " splitted", FWMessage.INFORMATION,
                                "Splitting");
                    }
                } else if (!CIDossierSplitting.CS_SAISIE_DOSSIER.equals(dossier.getIdEtat())
                        && !CIDossierSplitting.CS_OUVERT.equals(dossier.getIdEtat())
                        && !CIDossierSplitting.CS_CLOTURE.equals(dossier.getIdEtat())
                        && !CIDossierSplitting.CS_REVOQUE.equals(dossier.getIdEtat())
                        && !CIDossierSplitting.CS_ANNULE.equals(dossier.getIdEtat())) {
                    if (echoToConsole) {
                        System.out.print("Dossier no " + dossier.getId() + "(" + dossier.getTiersAssureNomComplet()
                                + ")");
                    }
                    if (dossier.updateDossier(getTransaction(), null)) {
                        if (echoToConsole) {
                            System.out.print(" (à imprimer) ");
                        }
                        getMemoryLog().logMessage("Dossier " + dossier.getId() + " need to be printed out",
                                FWMessage.INFORMATION, "Splitting");
                        // impression des RCI 95 nécessaire
                        // recherche des ref de l'assuré
                        // jmc 27.06.2005 Le mandat n'était pas réinitialisé,
                        // donc conservait les anciens critères de recherche
                        mandats = new CIMandatSplittingManager();
                        mandats.setSession(getSession());
                        mandats.setForIdDossierSplitting(dossier.getIdDossierSplitting());
                        mandats.setForIdTiersPartenaire(dossier.getIdTiersAssure());
                        mandats.find(getTransaction());
                        for (int loop = 0; loop < mandats.size(); loop++) {
                            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(loop);
                            if (entity.isMandatAutomatique()) {
                                if (!isCiSecure(dossier.getIdTiersAssure(), dossier)) {
                                    add(dossier.getReferenceService(), entity.getIdArc());
                                    break;
                                }
                            }
                        }
                        // recherche des ref du conjoint
                        mandats = new CIMandatSplittingManager();
                        mandats.setForIdDossierSplitting(dossier.getIdDossierSplitting());
                        mandats.setForIdTiersPartenaire(dossier.getIdTiersConjoint());
                        mandats.setSession(getSession());
                        mandats.find(getTransaction());
                        for (int loop = 0; loop < mandats.size(); loop++) {
                            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(loop);
                            if (entity.isMandatAutomatique()) {
                                if (!isCiSecure(dossier.getIdTiersConjoint(), dossier)) {
                                    add(dossier.getReferenceService(), entity.getIdArc());
                                    break;
                                }
                            }
                        }
                    }
                    if (getTransaction().hasErrors()) {
                        StringBuffer errors = getTransaction().getErrors();
                        ArrayList to = new ArrayList();
                        to.add(dossier.getEMailResponsable(getTransaction()));
                        this.envoiEmail(to, "Splitting",
                                getSession().getLabel("MSG_DOSSIER_EMAIL_MESSAGE") + dossier.getIdDossierSplitting()
                                        + "/" + dossier.getIdDossierInterne() + ": " + errors.toString());
                        getMemoryLog().logStringBuffer(errors, this.getClass().getName());
                        getMemoryLog().logMessage(
                                "La mise à jour du dossier no " + dossier.getId() + "/" + dossier.getIdDossierInterne()
                                        + " a échoué.", FWMessage.FATAL, this.getClass().getName());
                        if (echoToConsole) {
                            System.out.println(" a échoué! [" + errors.toString() + "]");
                        }
                        getTransaction().clearErrorBuffer();
                    } else {
                        if (echoToConsole) {
                            System.out.println(" mis à jour avec succès.");
                        }
                        getTransaction().commit();
                        getMemoryLog().logMessage("Dossier " + dossier.getId() + " updated", FWMessage.INFORMATION,
                                "Splitting");
                    }
                }
            }
            // impression des RCI 95
            if (container.size() != 0) {
                if (echoToConsole) {
                    // System.out.print("Impression..." + refToPrint);
                }
                getMemoryLog().logMessage("Printing documents", FWMessage.INFORMATION, "Splitting");
                String title = "";
                if (getSession().getIdLangue().equals("FR")) {
                    title = "RCI_Splitting_" + DateUtils.getDateJJMMAAAA_Dots() + "_";
                } else {
                    title = "ZIK_Splitting_" + DateUtils.getDateJJMMAAAA_Dots() + "_";
                }
                BSession sessionHermes = (BSession) ((CIApplication) getSession().getApplication())
                        .getSessionAnnonce(getSession());
                // init le(s) email(s) pour envoyer le résultat
                HEEnvoiEmailsGroupe emailListe = new HEEnvoiEmailsGroupe(sessionHermes,
                        HEEnvoiEmailsGroupe.resultat_ARC);
                if (emailListe.size() == 0) {
                    // aucun responsable configuré en base...
                    if (globaz.hermes.utils.StringUtils.isStringEmpty(sessionHermes.getApplication().getProperty(
                            "zas.user.email"))) {
                        throw new Exception(
                                "PAVO-HERMES, impression des splittings  : Aucune adresse n'est spécifée pour le résultat du process !");
                    }
                    emailListe.addEmail(sessionHermes.getApplication().getProperty("zas.user.email"));
                }

                Iterator iterSplittingToPrint = container.keySet().iterator();
                while (iterSplittingToPrint.hasNext()) {
                    String refService = (String) iterSplittingToPrint.next();
                    Vector refToPrint = (Vector) container.get(refService);

                    HEExtraitAnnonceProcess papier = new HEExtraitAnnonceProcess();
                    papier.setSession(sessionHermes);
                    papier.setDocumentTitle(title);
                    papier.setTransaction(getTransaction());
                    papier.setSendMailOnError(true);
                    papier.setDeleteOnExit(false);
                    papier.setEMailAddress(sessionHermes.getApplication().getProperty("zas.user.email"));

                    if (getSession().getIdLangue().equals("FR")) {
                        papier.setEmailSubjectError("L'impression du RCI de splitting a échouée");
                        papier.setEmailSubjectOK("L'impression du RCI ("
                                + papier.getDocumentTitle().substring(0, papier.getDocumentTitle().lastIndexOf("_"))
                                + ") a réussie.");
                    } else {
                        papier.setEmailSubjectError("ERROR : IK-Zusammenrufe des Splittings");
                        papier.setEmailSubjectOK("OK : IK-Zusammenrufe drucken ("
                                + papier.getDocumentTitle().substring(0, papier.getDocumentTitle().lastIndexOf("_"))
                                + ")");
                    }
                    papier.setReferenceUniqueVector(refToPrint);
                    ;
                    papier.executeProcess();
                    // 02.11.2006 : Bidouille pour envoyer les documents des
                    // extraits terminés
                    boolean envoieEMail = false;
                    String[] documentAll = new String[1];
                    for (Iterator iter = papier.getAttachedDocuments().iterator(); iter.hasNext();) {
                        JadePublishDocument document = (JadePublishDocument) iter.next();
                        documentAll[0] = document.getDocumentLocation();
                        // Un document a été trouvé => on envoie le mail
                        envoieEMail = true;
                    }

                    if (JadeStringUtil.isBlankOrZero(refService)) {
                        if (envoieEMail) {
                            JadeSmtpClient.getInstance().sendMail(
                                    JadeConversionUtil.toStringArray(emailListe.getEmailListe()), "Splitting terminés",
                                    "", documentAll);
                        }
                    } else {
                        String emailService = "";
                        try {
                            emailService = getMailService(refService);
                        } catch (Exception e) {

                        }
                        if (envoieEMail && !JadeStringUtil.isBlankOrZero(emailService)) {
                            String[] mailServiceArray = new String[1];
                            mailServiceArray[0] = emailService;
                            JadeSmtpClient.getInstance().sendMail(mailServiceArray, "Splitting terminés", "",
                                    documentAll);
                        } else if (envoieEMail) {
                            JadeSmtpClient.getInstance().sendMail(
                                    JadeConversionUtil.toStringArray(emailListe.getEmailListe()), "Splitting terminés",
                                    "", documentAll);
                        }

                    }

                    // fin bidouille
                }

            }
            emailGroups.send();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            e.printStackTrace();
            return false;
        }
        return !isAborted();
    }

    public void add(String service, String reference) {
        // si le service existe => on ajoute au vect et on écrase
        if (container.containsKey(service)) {
            Vector vect = (Vector) container.get(service);
            container.remove(service);
            vect.add(reference);
            container.put(service, vect);
        } else {

            Vector vect = new Vector();
            vect.add(reference);
            container.put(service, vect);
        }
    }

    public void envoiEmail(ArrayList to, String sujet, String message) throws Exception {
        if (emailGroups != null) {
            if (to == null) {
                emailGroups.addEmail(null, sujet, message);
            } else {
                for (int i = 0; i < to.size(); i++) {
                    emailGroups.addEmail((String) to.get(i), sujet, message);
                }
            }
        } else {
            if (to == null) {
                // adresse non trouvée -> envoie à admin
                application.sendEmailToAdmin(getSession().getLabel("MSG_ANNONCE_EMAIL_ERREUR"), message,
                        this.getClass());
            } else {
                for (int i = 0; i < to.size(); i++) {
                    JadeSmtpClient.getInstance().sendMail((String) to.get(i), sujet, message, null);
                }
            }
        }
    }

    public void envoiEmail(String to, String sujet, String message) throws Exception {
        if (emailGroups != null) {
            emailGroups.addEmail(to, sujet, message);
        } else {
            if (to == null) {
                // adresse non trouvée -> envoie à admin
                application.sendEmailToAdmin(getSession().getLabel("MSG_ANNONCE_EMAIL_ERREUR"), message,
                        this.getClass());
            } else {
                JadeSmtpClient.getInstance().sendMail(to, sujet, message, null);
            }
        }
    }

    /**
     * Retourne le contenu de l'email à envoyer.. Date de création : (14.02.2002 14:22:21)
     * 
     * @return le contenu de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isAborted()) {
            return "Le traitement journalier des mandats et dossiers de splitting a echoué!";
        } else {
            return "Le traitement journalier des mandats et dossiers de splitting s'est effectué avec succès.";
        }
    }

    public String getMailService(String service) throws Exception {
        HEConfigurationServiceListViewBean serviceMgr = new HEConfigurationServiceListViewBean();
        serviceMgr.setSession(getSession());
        serviceMgr.setForReference(service);
        serviceMgr.find();
        if (serviceMgr.size() > 0) {
            return ((HEConfigurationServiceViewBean) serviceMgr.getFirstEntity()).getEmailAdresse();
        }
        return "";
    }

    public boolean isCiSecure(String nss, CIDossierSplitting dossier) throws Exception {
        HEApplication hermesApp = (HEApplication) GlobazServer.getCurrentSystem().getApplication(
                HEApplication.DEFAULT_APPLICATION_HERMES);

        if (hermesApp.isCISecure(getSession(), nss)) {

            // BZ 4552 : Assuré ou conjoint protégé --> pas d'impression
            StringBuffer message = new StringBuffer(
                    "Le CI de l'assuré ou du conjoint est protégé, pas d'impression automatique pour ce dossier");
            // le ci est protégé, niveau de sécurité > 0, pas d'impression sur le pool
            System.out.println(message.toString());
            JadeSmtpClient.getInstance().sendMail(
                    dossier.getEMailResponsable(getTransaction()),
                    getSession().getLabel("ERREUR_SPLITTING_SUJET") + dossier.getIdDossierInterne() + " / "
                            + dossier.getIdDossierSplitting(), message.toString(), null);

            return true;

        } else {
            return false;
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
