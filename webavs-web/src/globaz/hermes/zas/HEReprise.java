package globaz.hermes.zas;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.application.HEProperties;
import globaz.hermes.db.gestion.HEAnnoncesListViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEConfigurationServiceListViewBean;
import globaz.hermes.db.gestion.HEConfigurationServiceViewBean;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceException;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.db.parametrage.HEChampannonceListViewBean;
import globaz.hermes.db.parametrage.HEChampannonceViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonce;
import globaz.hermes.db.parametrage.HEParametrageannonceManager;
import globaz.hermes.print.itext.HEAvisDeces_Doc;
import globaz.hermes.print.itext.HEDocumentRemiseAttestCA;
import globaz.hermes.print.itext.HEDocumentRemiseCertifCA;
import globaz.hermes.print.itext.HEDocumentZas;
import globaz.hermes.print.itext.HEListIrrecouvrable;
import globaz.hermes.print.itext.HERappelDocument_Doc;
import globaz.hermes.process.HEExtraitAnnonceProcess;
import globaz.hermes.service.HEExtraitService;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HEConfigurationServiceUtils;
import globaz.hermes.utils.HEEnvoiEmailsGroupe;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.gsc.bean.JadeRoleGroup;
import globaz.jade.admin.gsc.service.JadeRoleGroupService;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserGroupService;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import globaz.pavo.process.CICompteIndividuelProcess;
import globaz.pyxis.util.TISQL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

public class HEReprise extends BProcess {

    private static final long serialVersionUID = -7470376400278747638L;
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;
    public static final String ENCODING = "Cp037";
    public static String[] SERVICE_DEF = { HEReprise.serviceDCD, HEReprise.serviceCPT, HEReprise.serviceCIAD,
            HEReprise.serviceRACI };
    private static String serviceCIAD = "CIAD";
    private static String serviceCPT = "CPT";
    private static String serviceDCD = "DCD";
    private static String serviceRACI = "RACI";
    static boolean serviceRenseigne = false;
    // BZ 8934
    String saveAnnonce = "";

    public static boolean isRefDef(String value) {
        return (Arrays.asList(HEReprise.SERVICE_DEF).contains(value));
    }

    private static String[] jasperToArray(HEReprise process) {
        String[] filenames = new String[process.getAttachedDocuments().size()];
        int index = 0;
        for (Iterator iter = process.getAttachedDocuments().iterator(); iter.hasNext();) {
            JadePublishDocument document = (JadePublishDocument) iter.next();
            if (document.getPublishJobDefinition().getDocumentInfo().getPublishDocument()) {
                filenames[index++] = document.getDocumentLocation();
            }
        }
        return filenames;
    }

    public static List loadMails(String role, BSession session) throws Exception {
        List mails = new LinkedList();
        if (!JadeStringUtil.isBlank(role)) {
            JadeRoleGroupService roleGroupService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getRoleGroupService();
            JadeUserGroupService userGroupService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserGroupService();
            JadeUserService userService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserService();
            Map criteres = new HashMap();
            criteres.put(JadeRoleGroupService.CR_FOR_IDROLE, role);
            JadeRoleGroup[] groups = roleGroupService.findForCriteres(criteres);
            if (groups != null) {
                for (int i = 0; i < groups.length; i++) {
                    String[] userids = userGroupService.findAllIdUserForIdGroup(groups[i].getIdGroup());
                    if (userids != null) {
                        for (int j = 0; j < userids.length; j++) {
                            JadeUser user = userService.load(userids[j]);
                            if ((user != null) && (!JadeStringUtil.isBlank(user.getEmail()))) {
                                mails.add(user.getEmail());
                            }
                        }
                    }
                }
            }
        }
        if (mails.size() == 0) {
            mails.add(session.getApplication().getProperty(HEApplication.PROPERTY_DEFAULT_EMAIL));
        }
        return mails;
    }

    public static void main(String[] args) {
        boolean profile = false;
        HEReprise myProcess = new HEReprise();

        try {

            if (args.length < 4) {
                System.out.println(DateUtils.getTimeStamp()
                        + "java globaz.hermes.zas.HEReprise <filename> <type input|output> <uid> <pwd>");
                System.out.println(DateUtils.getTimeStamp()
                        + "java globaz.hermes.zas.HEReprise <auto> <type input|output> <uid> <pwd>");
                System.out.println(DateUtils.getTimeStamp()
                        + "java globaz.hermes.zas.HEReprise <treat> <idLot> <uid> <pwd>");
                System.out.println(DateUtils.getTimeStamp()
                        + "java globaz.hermes.zas.HEReprise <print> <idLot> <uid> <pwd>");
                throw new Exception("Wrong number of arguments");
            }

            //
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[2], args[3]);

            //
            myProcess.setParam0(args[0]);
            myProcess.setParam1(args[1]);
            myProcess.setUserID(args[2]);
            myProcess.setPwd(args[3]);

            //
            myProcess.setSession(session);

            //
            // init email address
            myProcess.setSendCompletionMail(true);
            myProcess.setSendMailOnError(true);
            myProcess.setControleTransaction(true);

            //
            if (profile = ("true".equals(myProcess.getSession().getApplication().getProperty("profiling")))) {
                Jade.getInstance().beginProfiling(HEReprise.class, args);
            }
            // run le process
            myProcess.executeProcess();

            if (HEReprise.serviceRenseigne == false) {
                HEReprise.sendResult(myProcess);
            }

            // logger le memoryLog dans le system.err
            if (myProcess.getMemoryLog().size() > 0) {
                for (int i = 0; i < myProcess.getMemoryLog().size(); i++) {
                    System.err.println(myProcess.getMemoryLog().getMessage(i).getFullMessage());
                    System.err.println("\n");
                }
                System.out.println("Process Hermes has error(s) !");
                // erreur critique, je retourne un code d'erreur 200
                System.exit(HEReprise.CODE_RETOUR_ERREUR);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process Hermes executed successfully !");
                System.exit(HEReprise.CODE_RETOUR_OK);
            }
        } catch (Throwable t) {
            try {
                HEReprise.sendError(t, myProcess);
            } catch (Exception e2) {
                e2.printStackTrace(System.err);
            }
            t.printStackTrace(System.err);
            System.out.println("Process Hermes has error(s) !");
            // erreur critique, je retourne un code d'erreur 200
            System.exit(HEReprise.CODE_RETOUR_ERREUR);
        } finally {
            if (profile) {
                Jade.getInstance().endProfiling();
            }
        }
        System.exit(HEReprise.CODE_RETOUR_ERREUR);
    }

    private static void sendError(Throwable t, HEReprise process) throws Exception {
        List mails = HEReprise.loadMails(
                process.getSession().getApplication().getProperty(HEEnvoiEmailsGroupe.responsable_ARC),
                process.getSession());
        System.out.println(DateUtils.getTimeStamp() + " Envoi de l'erreur à " + mails);
        JadeSmtpClient.getInstance().sendMail(
                JadeConversionUtil.toStringArray(mails),
                process.getSession().getLabel("HERMES_10040"),
                JadeStringUtil.isBlank(process.getSubjectDetail()) ? t.getMessage() + t.toString() : process
                        .getSubjectDetail(), new String[] {});
    }

    private static void sendResult(HEReprise process) throws Exception {
        List mails = HEReprise.loadMails(
                process.getSession().getApplication().getProperty(HEEnvoiEmailsGroupe.resultat_ARC),
                process.getSession());
        System.out.println(DateUtils.getTimeStamp() + " Envoi du résultat à " + mails);
        HEReprise.jasperToArray(process);
        JadeSmtpClient.getInstance().sendMail(JadeConversionUtil.toStringArray(mails),
                process.getSession().getLabel("HERMES_10040"), process.getSubjectDetail(),
                HEReprise.jasperToArray(process));
    }

    // private String[] args;
    private List codeAppFiltre;
    private List<String> codeAppRentes;
    private char[] fileBuffer = new char[120];

    private List groupPrintPermit = null;

    private boolean isLotOuvert = false;
    private boolean isPremiereAnnonceFichier;
    // private Vector listeLotsTraite = new Vector();
    private HELotViewBean lotTemp;
    private List motifFiltre;
    private List motifsToPrint;
    private int nbreAnnonce;
    private Vector orphanSplitting = new Vector();

    private String param0;

    private String param1;

    private String pwd = "";

    private File sourceFile = null;

    private Vector toPrintCIAdditionnel = new Vector();

    private String userID = "";

    public HEReprise() {
        super();
    }

    /**
     * Method HEReprise.
     * 
     * @param uid
     * @param pwd
     */
    public HEReprise(String uid, String pwd) {
        super();

        //
        setUserID(uid);
        setPwd(pwd);
    }

    @Override
    protected void _executeCleanUp() {
        System.out.println("Process is terminating...");
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // lot = new HELotViewBean(getSession());
        loadMotifsToPrint(getSession());
        loadCodeAppFiltre(getSession());
        loadMotifFiltre(getSession());
        loadCodeAppRentes(getSession());

        Map listDesServices = new HashMap();

        HEConfigurationServiceUtils checkService = new HEConfigurationServiceUtils();
        boolean exist = checkService.checkReferenceInterneExiste(getSession());

        if (exist == true) {
            HEConfigurationServiceListViewBean serviceMgr = new HEConfigurationServiceListViewBean();
            serviceMgr.setSession(getSession());
            serviceMgr.find();
            for (Iterator iter = serviceMgr.iterator(); iter.hasNext();) {
                HEConfigurationServiceViewBean srv = (HEConfigurationServiceViewBean) iter.next();
                String refInterne = srv.getReferenceInterne();
                String serviceEmail = srv.getEmailAdresse();
                listDesServices.put(refInterne, serviceEmail);
            }
            HEReprise.serviceRenseigne = true;
        }

        if (getSession().getApplication().getProperty("zas.log").equals("true")) {
            String logOut = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/HEReprise/out/"
                    + getSession().getApplication().getProperty("zas.log.get.out") + DateUtils.getLocaleDateAndTime()
                    + ".log";
            String logErr = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/HEReprise/err/"
                    + getSession().getApplication().getProperty("zas.log.get.err") + DateUtils.getLocaleDateAndTime()
                    + ".log";
            StringUtils.createDirectory(logOut);
            StringUtils.createDirectory(logErr);

            PrintStream streamOut = new PrintStream(new FileOutputStream(logOut));
            System.setOut(streamOut);

            PrintStream streamErr = new PrintStream(new FileOutputStream(logErr));
            System.setErr(streamErr);
        }

        System.out.println("Filtre sur les codes applications " + codeAppFiltre);
        System.out.println("Filtre sur les motifs " + motifFiltre);
        System.out.println("Motifs utilisés pour l'impression " + motifsToPrint);

        if ("input".equalsIgnoreCase(getParam1())) {
            ajoutAnnonces(getParam0(), HELotViewBean.CS_TYPE_RECEPTION);
            // commit transaction et suppression du fichier
            if (getTransaction() != null) {
                if (getTransaction().hasErrors()) {
                    getTransaction().rollback();
                    throw new Exception(getTransaction().getErrors().toString());
                } else {
                    // tout est ok donc je peux supprimer le fichier source
                    sourceFile.delete();
                    getTransaction().commit();
                }
            }
        } else if ("output".equalsIgnoreCase(getParam1())) {
            ajoutAnnonces(getParam0(), HELotViewBean.CS_TYPE_ENVOI);
            commit();
        } else if ("treat".equalsIgnoreCase(getParam0())) {
            HELotViewBean lot = new HELotViewBean();
            lot.setSession(getSession());
            lot.setIdLot(getParam1());
            lot.retrieve(getTransaction());
            traiterRetours(lot);
            doOrphanSplittings();
            associationCI(lot);
            traiterSeries(lot);
            commit();
            TreeSet res = annoncesTerminees(lot);
            confirmLot(lot);
            commit();

            generateDocs(lot, res, listDesServices);
            generateCiAdditionnels(listDesServices);
        } else if ("print".equalsIgnoreCase(getParam0())) {
            HELotViewBean lot = new HELotViewBean();
            lot.setSession(getSession());
            lot.setIdLot(getParam1());
            lot.retrieve(getTransaction());
            TreeSet res = annoncesTerminees(lot);
            generateDocs(lot, res, listDesServices);
        } else {
            System.out.println(DateUtils.getTimeStamp()
                    + "java globaz.hermes.zas.HEReprise <filename> <type input|output> <uid> <pwd>");
            System.out.println(DateUtils.getTimeStamp()
                    + "java globaz.hermes.zas.HEReprise <auto> <type input|output> <uid> <pwd>");
            System.out.println(DateUtils.getTimeStamp()
                    + "java globaz.hermes.zas.HEReprise <treat> <idLot> <uid> <pwd>");
            System.out.println(DateUtils.getTimeStamp()
                    + "java globaz.hermes.zas.HEReprise <print> <idLot> <uid> <pwd>");
            System.exit(HEReprise.CODE_RETOUR_ERREUR);
        }
        // confirmer chaque lot traité
        if ("input".equals(getParam1())) {
            HELotListViewBean listeLots = new HELotListViewBean();
            listeLots.setSession(getSession());
            listeLots.setForQuittance(HELotViewBean.LOT_NON_QUITTANCE);
            listeLots.setForLotReception(true);
            listeLots.setForEtat(HELotViewBean.CS_LOT_ETAT_CHARGE);
            listeLots.setOrder(HELotListViewBean.ORDER_BY_RMI_LOT);
            listeLots.find(getTransaction());
            for (int i = 0; i < listeLots.size(); i++) {
                HELotViewBean crt = (HELotViewBean) listeLots.getEntity(i);
                if ((crt != null) && HELotViewBean.CS_TYPE_RECEPTION.equals(crt.getType())) {
                    traiterRetours(crt);
                    doOrphanSplittings();
                    associationCI(crt);
                    traiterSeries(crt);
                    commit();
                }
                confirmLot(crt);
                // je confirme le traitement du lot
                commit();
                // je génère les documents nécessaires
                TreeSet res = annoncesTerminees(crt);
                generateDocs(crt, res, listDesServices);
            }
            if ("true".equals(getSession().getApplication().getProperty("zas.rappel.print"))) {
                generateRappels(listDesServices);
            }
            generateCiAdditionnels(listDesServices);
        }
        if ("output".equals(getParam1())) {
            HELotListViewBean listeLots = new HELotListViewBean();
            listeLots.setSession(getSession());
            listeLots.setForQuittance(HELotViewBean.LOT_NON_QUITTANCE);
            listeLots.setForType(HELotViewBean.CS_TYPE_ENVOI);
            listeLots.find(getTransaction());
            for (int i = 0; i < listeLots.size(); i++) {
                HELotViewBean crt = (HELotViewBean) listeLots.getEntity(i);
                confirmLot(crt);
            }
        }
        // on commit tout les modifications d'hermes avant de lancer le process de Pavo
        commit();
        BISession sessionPavo = globaz.globall.db.GlobazServer.getCurrentSystem()
                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(getUserID(), getPwd());
        System.out.println(DateUtils.getTimeStamp() + "Lancement CI");
        CICompteIndividuelProcess pPavo = new CICompteIndividuelProcess();
        pPavo.setSession((BSession) sessionPavo);
        pPavo.setEchoToConsole(true);
        // pPavo.setParent(this);
        pPavo.executeProcess();
        return true;
    }

    @Override
    protected void _validate() throws Exception {
        setSendMailOnError(false);
        setSendCompletionMail(false);
    }

    public void ajoutAnnonces(String file, String typeLot) throws Exception {

        // init variables
        Vector vecRef = new Vector();
        Vector vecIdLot = new Vector();
        String line = "";

        boolean hasCarriageReturns = true;
        BufferedReader r = null;
        nbreAnnonce = 0;
        isPremiereAnnonceFichier = true;
        String codeArcExclusion = null;
        boolean isExclus01 = false;
        HashSet<String> CiAdditionnel = null;
        boolean isInclus01 = true;
        String referenceInclusion = null;
        boolean isCiAdditionnel = false;

        // le vecteur des références
        if (file.equals("auto")) {

            // chargement du fichier PTZ_NumCaisse_O
            /* getSession().getApplication().getProperty("zas.home.dir") */
            file = Jade.getInstance().getSharedDir() + getSession().getApplication().getProperty("ftp.file.input");
        }

        /** config fichier, le fichier reçu a-t'il des carriage return ? * */
        hasCarriageReturns = "true".equals(((HEApplication) getSession().getApplication())
                .getProperty("ftp.file.input.carriagereturn"));

        sourceFile = new File(file);

        if ("true".equals(HEProperties.getParameter(HEProperties.PROP_INCLURE_CI_ADDITIONNEL, getSession(),
                getTransaction()))) {
            System.out.println(DateUtils.getTimeStamp() + "Handling file for CI additionnl research" + file);
            CiAdditionnel = getCiAdditionnel(hasCarriageReturns);
        }

        System.out.println(DateUtils.getTimeStamp() + "Handling file for treatement" + file);
        // RandomAccessFile raf = new RandomAccessFile(sourceFile, "r");
        r = initRead();

        // l'annonce à ajouter
        HEInputAnnonceViewBean annonce = new HEInputAnnonceViewBean(getSession());

        // le type de lot (envoi ou réception)
        annonce.setTypeLot(typeLot);
        annonce.wantCallValidate(false);

        String codeApp = "";
        String codeEnr = "";
        String motif = "";
        String referenceUnique = "";
        String statut = "";
        String caisse = "";
        String numeroAVS = "";
        Boolean nss = new Boolean(false);
        String crtTypeLot = typeLot;
        String lastCodeApp = "";

        // type de parametrage
        HEParametrageannonceManager paramM = new HEParametrageannonceManager(getSession());
        HEParametrageannonce param = new HEParametrageannonce(getSession());

        // la liste des champs
        HEChampannonceListViewBean champAnnonceM = new HEChampannonceListViewBean(getSession());

        while ((line = readLine(r, hasCarriageReturns)) != null) {

            // vérifie que ça commence pas par 01 ou 99
            if (isPremiereAnnonceFichier) {

                if (!line.startsWith("01")) {
                    throw new Exception("Securite : Le début du fichier ne commence pas par une trame 01");
                }

                isPremiereAnnonceFichier = false;
            }

            if (isRecord(line)) {

                saveAnnonce = line;
                // si le code application est un code application avis de décès, initialiser un lot de type avis de
                // décès
                if (line.startsWith("52")) {
                    crtTypeLot = HELotViewBean.CS_TYPE_AVIS_DECES;
                } else if (line.startsWith("50")) {
                    crtTypeLot = HELotViewBean.CS_TYPE_RENTES;
                } else if (line.startsWith("51")) {
                    crtTypeLot = HELotViewBean.CS_TYPE_ADAPTATION_RENTES;
                } else if (line.startsWith("53")) {
                    crtTypeLot = HELotViewBean.CS_TYPE_ADAPTATION_RENTES;
                } else if (line.startsWith("61")) {
                    crtTypeLot = HELotViewBean.CS_TYPE_ADAPTATION_RENTES_PC;
                } else {
                    crtTypeLot = typeLot;
                }

                nbreAnnonce++;

                if (lotTemp == null) {
                    throw new Exception(
                            "Erreur : Il n'y a pas de trame 01 donc impossible d'initialiser le lot de depart");
                }

                annonce = new HEInputAnnonceViewBean(getSession());
                annonce.wantCallValidate(false);
                annonce.setDateAnnonce(lotTemp.getDateCentrale());
                annonce.setTypeLot(crtTypeLot);

                // on le fait à 120
                line = StringUtils.padAfterString(line, " ", 120);

                // y'a des records, on créé le lot
                System.out.print(DateUtils.getTimeStamp() + "Reading record ");

                // récupère le code application
                codeApp = getCodeApp(line);

                // récupère le code enregistrement
                codeEnr = getCodeEnr(codeApp, line);

                //
                annonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, codeApp);
                annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, codeEnr);
                annonce.setRefUnique("");
                // annonce.setStatut("");
                param = loadParametrage(codeApp, codeEnr, line, paramM);
                champAnnonceM = loadChamps(champAnnonceM, param.getIdParametrageAnnonce());
                annonce = loadFields(annonce, champAnnonceM, line);

                if (HELotViewBean.CS_TYPE_ENVOI.equals(typeLot)) {
                    annonce.wantCheckUnique(false);
                }
                // filtre sur les codes applications/motifs
                // Avec (!"39".equals(codeApp)||!"38".equals(lastCodeApp)) : liaison des 38, 39
                if ((codeEnr.equals("01") || codeEnr.equals("001"))
                        && (!"39".equals(codeApp) || !"38".equals(lastCodeApp))) {
                    motif = annonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE);
                    // po 5522
                    if ((CiAdditionnel != null) && "38".equals(codeApp)) {
                        if (CiAdditionnel.contains(annonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {
                            isCiAdditionnel = true;
                        } else {
                            isCiAdditionnel = false;
                        }
                    }
                    // on ne procède à aucun filtre lorsqu'il s'agit des annonces pour les rentes
                    if (!isArcRentes(codeApp)) {
                        if (!isCiAdditionnel) {
                            // po 850 exclusion du chargement des arcs par référence interne
                            if (codeArcExclusion == null) {
                                codeArcExclusion = HEProperties.getParameter(HEProperties.PROP_CODE_ARC_EXCLUSION,
                                        getSession(), getTransaction());
                            }
                            if (!JadeStringUtil.isEmpty(codeArcExclusion)) {
                                String refInterne = annonce.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE);
                                if (!JadeStringUtil.isEmpty(refInterne) && refInterne.startsWith(codeArcExclusion)) {
                                    isExclus01 = true;
                                    continue;
                                }
                            }
                            isExclus01 = false;
                            // PO 5522 : filtre sur les références
                            if (referenceInclusion == null) {
                                referenceInclusion = HEProperties.getParameter(HEProperties.PROP_REFERENCE_FILTRE_ARC,
                                        getSession(), getTransaction());
                            }
                            if (!JadeStringUtil.isEmpty(referenceInclusion)) {
                                String refInterne;
                                if ("38".equals(codeApp) || "22".equals(codeApp) || "39".equals(codeApp)) {
                                    refInterne = annonce
                                            .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE);
                                } else {
                                    refInterne = annonce.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE);
                                }
                                // si la référence de l'arc ne commence pas par la référence configurée,
                                // l'arc ne sera pas inclue
                                if (!JadeStringUtil.isEmpty(refInterne) && refInterne.startsWith(referenceInclusion)) {
                                    isInclus01 = true;
                                } else {
                                    isInclus01 = false;
                                    continue;
                                }
                            }
                        } else {
                            // il faut aussi prévoir d'ajouter les 02,03,etc.
                            isInclus01 = true;
                        }
                    } else {
                        // il faut aussi prévoir d'ajouter les 02,03,etc.
                        isInclus01 = true;
                    }
                    // *************
                    if (isToBeAdded(annonce, motif)) { // nouvelle série

                        // nouveau parametrage
                        // System.out.println(
                        // "Adding annonce " + codeApp + codeEnr + ", motif " +
                        // annonce.getField(annonce.MOTIF_ANNONCE));
                        annonce.add(getTransaction());
                        System.out.println("and adding to lot :" + annonce.getIdLot() + " with ref : "
                                + annonce.getRefUnique() + " - " + line);
                        lotTemp.setIdLot(annonce.getIdLot());
                        referenceUnique = annonce.getRefUnique();
                        statut = annonce.getStatut();
                        motif = annonce.getMotif();
                        caisse = annonce.getNumeroCaisse();
                        numeroAVS = annonce.getNumeroAVS();
                        nss = annonce.getNnss();

                        if (!vecRef.contains(referenceUnique)) {

                            // garde une trace des nouvelles références unique
                            // et id du lot du courant
                            vecRef.add(referenceUnique);
                            vecIdLot.add(annonce.getIdLot());
                        }
                    } else {
                        System.out.println("Skipping annonce " + codeApp + codeEnr + ", motif " + motif);
                    }
                } else {
                    // po 850 exclusion du chargement des arcs par référence interne
                    // si le 01 traité durant le tour d'avant n'as pas été exclus, on l'ajoute
                    if (!isExclus01 && isToBeAdded(annonce, motif) && isInclus01) {

                        // System.out.println("Adding annonce " + codeApp + codeEnr + ", motif " + motif);
                        // je garde la même référence que dans le 01
                        annonce.setRefUnique(referenceUnique);

                        // je garde le même statut que dans le 01
                        annonce.setStatut(statut);

                        // je garde le même motif que dans le 01
                        annonce.setMotif(motif);

                        // je garde la même référence que dans le 01
                        annonce.setNumeroCaisse(caisse);

                        // je garde le même numero avs que dans le 01
                        annonce.setNumeroAVS(numeroAVS);
                        // et le formattage du NNSS
                        annonce.setNnss(nss);

                        annonce.add(getTransaction());
                        System.out.println("and adding to lot: " + annonce.getIdLot() + " with ref : "
                                + annonce.getRefUnique() + " - " + annonce.getChampEnregistrement() + ","
                                + annonce.getRefUnique());
                    } else {
                        System.out.println("Skipping annonce " + codeApp + codeEnr + ", motif " + motif);
                    }
                }
                lastCodeApp = codeApp;
            }
        } // ??

        // contrôle que le(s) lots soit bien cadré par des trames 01-99
        if (isLotOuvert) {
            throw new Exception("Securite : il n'y a pas de trame pour délimiter le dernier lot !!!");
        }

        champAnnonceM = null;
        param = null;
        paramM = null;
        annonce = null;
        r.close();
        System.gc();
    }

    private TreeSet annoncesTerminees(HELotViewBean lot) throws Exception {
        TreeSet toPrintRefs = new TreeSet((new HEExtraitService()).getSortExtrait(getSession()));
        if (HELotViewBean.CS_TYPE_RECEPTION.equals(lot.getType())) {
            System.out.println("/*************************************/");
            System.out.println("/************* Terminees *************/");
            System.out.println("/*************************************/");
            HEOutputAnnonceListViewBean annoncesATraiter = new HEOutputAnnonceListViewBean();
            System.out.println("Load list Extrait Ci for printing..");
            annoncesATraiter.setSession(getSession());
            annoncesATraiter.setForIdLot(lot.getIdLot());
            annoncesATraiter.setForCodeEnr01Or001(true);
            annoncesATraiter.setForCodeApplication("39");
            annoncesATraiter.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
            BStatement stat = annoncesATraiter.cursorOpen(getTransaction());
            HEOutputAnnonceViewBean crt;
            ArrayList traiteDoublon = new ArrayList();
            while ((crt = (HEOutputAnnonceViewBean) annoncesATraiter.cursorReadNext(stat)) != null) {
                if (!traiteDoublon.contains(crt.getRefUnique())) {
                    if (!crt.isCiSecure()) {

                        // le ci n'est pas protégé, niveau de sécurité = 0
                        // ALD ajout: on imprime que les extraits pour lesquels l'utilisateur ayant créé
                        // l'annonce ne fait pas partie de la section exclue
                        // String utilisateurArc = crt.getUtilisateur().toLowerCase();

                        // if (!isGroupPrintPermit(utilisateurArc)) {
                        // System.out.println(DateUtils.getTimeStamp() +
                        // "L'utilisateur est dans le groupe exclu, pas d'impression pour réf :" + crt.getRefUnique() +
                        // " numAVS : " + crt.getNumeroAVS() + " motif: " + crt.getMotif());
                        // } else {
                        if (isPrintableMotif(crt.getMotif())) {
                            // on imprimera cette annonce, on utilise HEOutputAnnonceViewBean
                            // ceci pour charger la table de hachage
                            HEOutputAnnonceViewBean vbToPrint = new HEOutputAnnonceViewBean();
                            vbToPrint.setSession(crt.getSession());
                            vbToPrint.setIdAnnonce(crt.getIdAnnonce());
                            vbToPrint.retrieve(getTransaction());
                            toPrintRefs.add(vbToPrint);
                        } else {
                            System.out.println(DateUtils.getTimeStamp() + "Splitting, pas d'impression pour réf :"
                                    + crt.getRefUnique() + " numAVS : " + crt.getNumeroAVS() + " motif: "
                                    + crt.getMotif());
                        }
                        // }
                    } else {
                        StringBuffer message = new StringBuffer(
                                "Ce CI est protégé, pas d'impression automatique pour réf :");
                        message.append(crt.getRefUnique());
                        message.append(" numAVS : ");
                        message.append(crt.getNumeroAVS());
                        message.append(" motif: ");
                        message.append(crt.getMotif());

                        // le ci est protégé, niveau de sécurité > 0, pas d'impression sur le pool
                        System.out.println(DateUtils.getTimeStamp() + message.toString());

                        getMemoryLog().logMessage(message.toString(), FWMessage.INFORMATION, "Batch HEReprise");
                    }
                    // mémorise la référence traitée
                    traiteDoublon.add(crt.getRefUnique());
                }
            }
            annoncesATraiter.cursorClose(stat);
        }
        return toPrintRefs;
    }

    private void associationCI(HELotViewBean lotCrt) throws Exception {
        System.out.println("/***********************************/");
        System.out.println("/* Association des CI de splitting */");
        System.out.println("/***********************************/");
        System.out.println(DateUtils.getTimeStamp() + "lot en cours de traitement :" + lotCrt.getIdLot());

        HEOutputAnnonceListViewBean annonces95 = new HEOutputAnnonceListViewBean();
        annonces95.setSession(getSession());
        annonces95.setForStatut(IHEAnnoncesViewBean.CS_ORPHELIN);
        annonces95.setLikeEnregistrement("3");
        annonces95.setForMotif("95");
        annonces95.setForIdLot(lotCrt.getIdLot());

        // annonces95.wantCallMethodAfter(false);
        annonces95.wantCallMethodAfterFind(false);
        annonces95.wantCallMethodBefore(false);
        annonces95.wantCallMethodBeforeFind(false);
        annonces95.find(getTransaction(), BManager.SIZE_NOLIMIT);

        // j'ai tout les extraits de CI de splitting orphelins
        // pour chaque ligne je retrouve l'ARC
        for (int i = 0; i < annonces95.size(); i++) {

            // je cherche l'ARC 11 par le biais du 25 (au cas où pour le numéro AVS différent dans une autre caisse)
            HEOutputAnnonceViewBean annonce95 = (HEOutputAnnonceViewBean) annonces95.getEntity(i);

            if (annonce95.getChampEnregistrement().startsWith("38001")
                    || annonce95.getChampEnregistrement().startsWith("39001")) {
                System.out
                        .println(DateUtils.getTimeStamp() + "Recherche ci splitting pour " + annonce95.getRefUnique());

                HEOutputAnnonceListViewBean annonces25 = new HEOutputAnnonceListViewBean();
                annonces25.setSession(getSession());
                annonces25.setForMotif("95");
                annonces25.setLikeEnregistrement("2501");
                annonces25.setForNumeroAVS(annonce95.getNumeroAVS());
                annonces25.wantCallMethodAfterFind(false);
                annonces25.wantCallMethodBefore(false);
                annonces25.wantCallMethodBeforeFind(false);
                annonces25.find(getTransaction(), BManager.SIZE_NOLIMIT);
                System.out.println(annonces25.size() + " annonces 25 trouvées");

                if (annonces25.size() >= 1) {
                    HEOutputAnnonceViewBean annonce25 = null;

                    // j'ai trouvé un envoi, avant de faire l'update, je vérifie qu'il existe pas déjà, donc en double
                    for (int j = 0; j < annonces25.size(); j++) {
                        HEOutputAnnonceViewBean tmpAnnonce = (HEOutputAnnonceViewBean) annonces25.getEntity(j);

                        if (tmpAnnonce.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA).equals(
                                annonce95.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA))) {
                            annonce25 = (HEOutputAnnonceViewBean) annonces25.getEntity(j);
                        }
                    }

                    if (annonce25 != null) {
                        System.out.println("Une serie trouvee pour la référence " + annonce25.getRefUnique());

                        // contrôler que pour cette série on n'ait pas déjà reçu un CI
                        // pour la même caisse
                        HEAttenteRetourListViewBean rechercheAttente = new HEAttenteRetourListViewBean();
                        rechercheAttente.setSession(getSession());
                        rechercheAttente.setForMotif("95");
                        rechercheAttente.setForNotIdAnnonceRetour("0");
                        rechercheAttente.setForIdAnnonceRetourAttendue(annonce95.getIdParamAnnonce());
                        rechercheAttente.setForNumeroCaisse(annonce95.getNumeroCaisse());
                        rechercheAttente.setForNumeroAVS(annonce95.getNumeroAVS());
                        rechercheAttente.setForReferenceUnique(annonce25.getRefUnique());
                        rechercheAttente.wantCallMethodAfterFind(false);
                        rechercheAttente.wantCallMethodAfter(false);
                        rechercheAttente.find(getTransaction(), BManager.SIZE_NOLIMIT);

                        // ***********************************************************
                        if (rechercheAttente.size() == 0) {

                            // recherche également si un extrait de CI a déjà été associés de cette manière
                            if (!hasDejaCILie(annonce95, annonce25.getRefUnique())) {
                                // aucun ci déjà présent pour même num avs, caisse...
                                // donc association de cette série au 95 trouvé
                                HEAnnoncesListViewBean annonces = new HEAnnoncesListViewBean();
                                annonces.setSession(getSession());
                                annonces.setForRefUnique(annonce95.getRefUnique());
                                annonces.find(getTransaction(), BManager.SIZE_NOLIMIT);

                                for (int j = 0; j < annonces.size(); j++) {
                                    HEAnnoncesViewBean vBean95 = (HEAnnoncesViewBean) annonces.getEntity(j);
                                    vBean95.wantCallMethodBefore(false);
                                    vBean95.wantCallValidate(false);
                                    vBean95.setRefUnique(annonce25.getRefUnique());
                                    vBean95.setUtilisateur(annonce25.getUtilisateur());
                                    vBean95.setIdProgramme(annonce25.getIdProgramme());
                                    vBean95.setStatut(annonce25.getStatut());
                                    vBean95.update(getTransaction());
                                }

                                // vérifier qu'il n'y avait pas d'attentes pour ce dernier ci associé
                                if (!IHEAnnoncesViewBean.CS_TERMINE.equals(annonce25.getStatut())) {

                                    // si la série n'est pas terminée, on attendait peut-être le dernier ci associé
                                    rechercheAttente.setForNotIdAnnonceRetour("");
                                    rechercheAttente.setForIdAnnonceRetour("0");
                                    rechercheAttente.find(getTransaction());

                                    if (rechercheAttente.size() > 0) {

                                        // on a trouvé une attente pour ce ci !
                                        // on associe l'attente
                                        System.out.println("on attendait un ci pour la référence "
                                                + annonce25.getRefUnique() + ", on associe");

                                        HEAttenteRetourViewBean attente = (HEAttenteRetourViewBean) rechercheAttente
                                                .getFirstEntity();
                                        attente.setIdAnnonceRetour(annonce95.getIdAnnonce());
                                        attente.setReferenceUnique(annonce25.getRefUnique());
                                        attente.wantCallMethodAfter(false);
                                        attente.wantCallMethodBefore(false);
                                        attente.wantCallValidate(false);
                                        attente.update(getTransaction());
                                    }
                                }
                            } else {
                                System.out
                                        .println("Aucune association, il y a déjà un extrait avec les mêmes caractéristiques !");
                            }
                        } else {
                            System.out.println("Aucune association, un ci avec les mêmes caract. est déjà associé !");
                        }
                    }
                }
            }
        }

        System.out.println(DateUtils.getTimeStamp() + "lot " + lotCrt.getIdLot() + " traité");
    }

    private void commit() throws Exception {
        // je confirme le traitement du lot
        if (getTransaction() != null) {
            if (getTransaction().hasErrors()) {
                getTransaction().rollback();
                throw new Exception(getTransaction().getErrors().toString());
            } else {
                getTransaction().commit();
            }
        }
    }

    /**
     * Method confirmLot. Confirme un lot
     * 
     * @param lot
     * @throws Exception
     */
    private void confirmLot(HELotViewBean lot) throws Exception {
        lot.retrieve(getTransaction());

        if (!lot.isNew()) {
            System.out.println(DateUtils.getTimeStamp() + "Confirming lot " + lot.getIdLot());
            lot.setQuittance("1");
            lot.setEtat(HELotViewBean.CS_LOT_ETAT_TRAITE);
            lot.setDateTraitement(JACalendar.todayJJsMMsAAAA());
            lot.setHeureTraitement(JACalendar.formatTime(JACalendar.now()));
            lot.update(getTransaction());
        } else {
            System.out.println("Aucune annonce dans le fichier d'entrée");
        }
    }

    /**
     * Method doOrphanSplittings.
     */
    private void doOrphanSplittings() throws Exception {

        for (int i = 0; i < orphanSplitting.size(); i++) {
            String ref = (String) orphanSplitting.elementAt(i);
            HEAnnoncesListViewBean annonces = new HEAnnoncesListViewBean();
            annonces.setSession(getSession());
            annonces.wantCallMethodAfterFind(false);
            annonces.wantCallMethodBeforeFind(false);
            annonces.setForRefUnique(ref);
            annonces.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int j = 0; j < annonces.size(); j++) {

                if (j == 0) {
                    System.out.println("Setting to orphan splitting : " + ref);
                }

                HEAnnoncesViewBean entity = (HEAnnoncesViewBean) annonces.getEntity(j);
                entity.setStatut(IHEAnnoncesViewBean.CS_ORPHELIN);
                entity.wantCallValidate(false);
                entity.wantCallMethodBefore(false);
                entity.update(getTransaction());
            }
        }
    }

    private void generateCiAdditionnels(Map service) throws Exception {
        // // Impression des ci additionnel
        System.out.println(DateUtils.getTimeStamp() + "Impression des Cis additionnels...["
                + toPrintCIAdditionnel.toString() + "]");
        if (toPrintCIAdditionnel.size() > 0) {
            HEExtraitAnnonceProcess ciAdd = new HEExtraitAnnonceProcess();
            ciAdd.setSession(getSession());
            ciAdd.setTransaction(getTransaction());
            ciAdd.setSendMailOnError(true);
            ciAdd.setReferenceUniqueVector(toPrintCIAdditionnel);
            ciAdd.setEmailSubjectOK(getSession().getLabel("HERMES_10032") + "/" + JACalendar.todayJJsMMsAAAA());
            ciAdd.setEmailSubjectError(getSession().getLabel("HERMES_10033") + "/" + JACalendar.todayJJsMMsAAAA());
            ciAdd.setSendCompletionMail(false);

            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();

                    if (HEReprise.serviceCIAD.equals(refInterne)) {
                        String email = (String) service.get(refInterne);
                        ciAdd.setEMailAddress(email);
                        ciAdd.setEmailSubjectOK(getSession().getLabel("HERMES_10032") + "/"
                                + JACalendar.todayJJsMMsAAAA() + "/" + refInterne);
                        ciAdd.setEmailSubjectError(getSession().getLabel("HERMES_10033") + "/"
                                + JACalendar.todayJJsMMsAAAA() + "/" + refInterne);
                        ciAdd.start();

                        while (!ciAdd.getJob().isOut() && !ciAdd.getJob().isAborted()) {
                            Thread.sleep(100);
                        }
                    }
                }
            } else {
                ciAdd.setParent(this);
                ciAdd.executeProcess();
            }
        }

    }

    /**
     * Method generateDocs. Imprime la liste de confirmations ARC et les RCI
     * 
     * @param toPrintRefs
     * @throws FWIException
     * @throws Exception
     */
    private void generateDocs(HELotViewBean lotCrt, TreeSet arcTerminees, Map service) throws FWIException, Exception {
        System.out.println("/*************Génération des documents relatifs au traitement*******************/");

        if (HELotViewBean.CS_TYPE_RECEPTION.equals(lotCrt.getType())) {
            // accusé réception uniquement CA

            HEDocumentZas doc = new HEDocumentZas();
            doc.setSession(getSession());
            doc.setTransaction(getTransaction());
            doc.setIdLot(lotCrt.getIdLot());
            doc.setCaOnly("true");
            doc.setSendCompletionMail(false);
            doc.setPrintBatch(true);
            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();
                    String email = (String) service.get(refInterne);
                    if (!HEReprise.isRefDef(refInterne)) {
                        doc.setForService(refInterne);
                        doc.setEMailAddress(email);
                        doc.start();

                        while (!doc.getJob().isOut() && !doc.getJob().isAborted()) {
                            Thread.sleep(100);
                        }
                    }
                }
            } else {
                doc.setParent(this);
                doc.executeProcess();
            }

            // accusé réception globale

            doc = new HEDocumentZas();
            doc.setSession(getSession());
            doc.setTransaction(getTransaction());
            doc.setIdLot(lotCrt.getIdLot());
            doc.setCaOnly("false");
            doc.setSendCompletionMail(false);
            doc.setPrintBatch(true);
            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();
                    String email = (String) service.get(refInterne);

                    if (!HEReprise.isRefDef(refInterne)) {
                        doc.setForService(refInterne);
                        doc.setEMailAddress(email);
                        doc.start();

                        while (!doc.getJob().isOut() && !doc.getJob().isAborted()) {
                            Thread.sleep(100);
                        }
                    }
                }
            } else {
                doc.setParent(this);
                doc.executeProcess();
            }

            // impression des extraits terminés
            System.out.println("/********************************/");

            String viewPrint = DateUtils.getTimeStamp() + "Impression des CI terminés [";

            for (Iterator it = arcTerminees.iterator(); it.hasNext();) {
                viewPrint += ((HEAnnoncesViewBean) it.next()).getRefUnique() + ";";
            }
            System.out.println(viewPrint + "]");

            HEExtraitAnnonceProcess papier = new HEExtraitAnnonceProcess();
            papier.setSession(getSession());
            papier.setTransaction(getTransaction());
            papier.setSendMailOnError(true);
            papier.setDeleteOnExit(false);
            papier.setSendCompletionMail(false);
            papier.setImpressionBatch(true);

            if (getSession().getIdLangue().equals("FR")) {
                papier.setDocumentTitle("RCI_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
            } else {
                papier.setDocumentTitle("ZIK_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
            }
            papier.setReferenceUniqueList(arcTerminees);
            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();
                    String email = (String) service.get(refInterne);

                    if (!HEReprise.isRefDef(refInterne)) {
                        papier.setService(refInterne);
                        papier.setEMailAddress(email);
                        papier.start();

                        while (!papier.getJob().isOut() && !papier.getJob().isAborted()) {
                            Thread.sleep(100);
                        }
                    }
                }
            } else {
                papier.setParent(this);
                papier.executeProcess();
            }
            // //
            // impression de la liste des irrécouvrables

            HEListIrrecouvrable listIrrec = new HEListIrrecouvrable(getSession(), lotCrt.getIdLot());
            listIrrec.setSession(getSession());
            listIrrec.setSendCompletionMail(false);

            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();

                    if (HEReprise.serviceCPT.equals(refInterne)) {
                        String email = (String) service.get(refInterne);
                        // imprimer la liste que si plus d'un cas trouvé
                        BManager m = listIrrec.getManager();
                        m.find();
                        if (m.size() > 0) {
                            listIrrec.setEMailAddress(email);
                            listIrrec.setService(refInterne);
                            listIrrec.start();

                            while (!listIrrec.getJob().isOut() && !listIrrec.getJob().isAborted()) {
                                Thread.sleep(100);
                            }
                        }
                    }
                }
            } else {
                listIrrec.setParent(this);
                listIrrec.executeProcess();
            }

            // impression des certificats

            HEDocumentRemiseCertifCA docRemiseCa = new HEDocumentRemiseCertifCA();
            docRemiseCa.setSession(getSession());
            docRemiseCa.setIdLot(lotCrt.getIdLot());
            docRemiseCa.setSendCompletionMail(false);

            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();
                    String email = (String) service.get(refInterne);
                    if (!HEReprise.isRefDef(refInterne)) {
                        docRemiseCa.setService(refInterne);
                        docRemiseCa.setEMailAddress(email);
                        docRemiseCa.start();

                        while (!docRemiseCa.getJob().isOut() && !docRemiseCa.getJob().isAborted()) {
                            Thread.sleep(100);
                        }
                    }
                }
            } else {
                docRemiseCa.setParent(this);
                docRemiseCa.executeProcess();
            }

            // impression des attestations

            HEDocumentRemiseAttestCA docRemiseAttestCa = new HEDocumentRemiseAttestCA();
            docRemiseAttestCa.setSession(getSession());
            docRemiseAttestCa.setIdLot(lotCrt.getIdLot());
            docRemiseAttestCa.setSendCompletionMail(false);

            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();
                    String email = (String) service.get(refInterne);
                    if (!HEReprise.isRefDef(refInterne)) {
                        docRemiseAttestCa.setService(refInterne);
                        docRemiseAttestCa.setEMailAddress(email);
                        docRemiseAttestCa.start();

                        while (!docRemiseAttestCa.getJob().isOut() && !docRemiseAttestCa.getJob().isAborted()) {
                            Thread.sleep(100);
                        }
                    }
                }
            } else {
                docRemiseAttestCa.setParent(this);
                docRemiseAttestCa.executeProcess();
            }

        } else if (HELotViewBean.CS_TYPE_AVIS_DECES.equals(lotCrt.getType())) {
            // impression des avis de décès
            HEAvisDeces_Doc avisDeces = new HEAvisDeces_Doc();
            avisDeces.setSession(getSession());
            avisDeces.setIdLot(lotCrt.getIdLot());
            avisDeces.setSendCompletionMail(false);

            if (!service.isEmpty()) {
                for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                    String refInterne = (String) iter.next();

                    if (HEReprise.serviceDCD.equals(refInterne)) {
                        String email = (String) service.get(refInterne);
                        avisDeces.setEMailAddress(email);
                        avisDeces.setService(refInterne);
                        avisDeces.start();

                        while (!avisDeces.getJob().isOut() && !avisDeces.getJob().isAborted()) {
                            Thread.sleep(100);
                        }
                    }
                }
            } else {
                avisDeces.setParent(this);
                avisDeces.executeProcess();
            }
        }
    }

    /**
     * Method generateRappels.
     */
    private void generateRappels(Map service) throws Exception {
        System.out.println("Generation des rappels...");

        String nbresJours = ((HEApplication) getSession().getApplication()).getProperty("rappels.nbresJours.attendre");

        // String email = ((HEApplication) getSession().getApplication()).getProperty("zas.user.email");
        HERappelDocument_Doc rappelsProc = new HERappelDocument_Doc();
        rappelsProc.setSession(getSession());
        rappelsProc.setNbJours(nbresJours);
        rappelsProc.setSendCompletionMail(false);

        if (!service.isEmpty()) {
            for (Iterator iter = service.keySet().iterator(); iter.hasNext();) {
                String refInterne = (String) iter.next();
                if (HEReprise.serviceRACI.equals(refInterne)) {
                    rappelsProc.setEmail((String) service.get(refInterne));
                    rappelsProc.start();
                    while (!rappelsProc.getJob().isOut() && !rappelsProc.getJob().isAborted()) {
                        Thread.sleep(100);
                    }
                }
            }
        } else {
            rappelsProc.setParent(this);
            rappelsProc.executeProcess();
        }

        System.out.println("Fin de la Generation des rappels");
    }

    private HashSet<String> getCiAdditionnel(boolean hasCarriageReturns) throws Exception {
        BufferedReader read = null;
        String line;
        HashSet<String> result = new HashSet<String>();
        try {
            read = initRead();
            while ((line = readLine(read, hasCarriageReturns)) != null) {
                // extrait que les records 39001 sur lequels l'information d'un CI additionnel existe
                if (line.startsWith("39001")) {
                    // la position 92 à 1 indique que c'est un CI additionnel
                    if ((line.length() > 92) && "1".equals(line.substring(91, 92))) {
                        // on mémorise le NAVS dans un hashSet
                        result.add(HENNSSUtils.convertNegatifToNNSS(line.substring(60, 71)));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (read != null) {
                read.close();
            }
        }
    }

    /**
     * Method getCodeApp.
     * 
     * @param line
     */
    private String getCodeApp(String line) {
        return line.substring(0, 2);
    }

    /**
     * Method getCodeEnr.
     * 
     * @param line
     * @return String
     */
    private String getCodeEnr(String codeApp, String line) {

        if (codeApp.equals("38") || codeApp.equals("39")) {
            return line.substring(2, 5);
        } else {
            return line.substring(2, 4);
        }
    }

    /**
     * Method getCSCodeApp.
     * 
     * @param codeApp
     * @return String
     */
    private String getCSCodeApp(String codeApp) throws Exception {
        // FWParametersSystemCodeManager csCodeAppM = ((HEApplication)
        // getSession().getApplication()).getCsCodeApplicationListe(getSession());
        FWParametersSystemCodeManager csCodeAppM = new FWParametersSystemCodeManager();
        csCodeAppM.setForIdGroupe("HECODAPP");
        csCodeAppM.setForIdTypeCode("11100001");
        csCodeAppM.setSession(getSession());
        csCodeAppM.setForCodeUtilisateur(codeApp);
        csCodeAppM.find(getTransaction());

        FWParametersSystemCode csCodeAppE = (FWParametersSystemCode) csCodeAppM.getFirstEntity();

        if (csCodeAppE == null) {
            throw new Exception("Erreur critique, impossible de récupérer le code système du code application :"
                    + codeApp);
        }

        return csCodeAppE.getIdCode();
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    /**
     * @return
     */
    public String getParam0() {
        return param0;
    }

    /**
     * @return
     */
    public String getParam1() {
        return param1;
    }

    /**
     * Returns the pwd.
     * 
     * @return String
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Method getRef38.
     * 
     * @param string
     * @return String
     */
    private String getRef38(String idAnnonce39001) throws Exception {
        HEOutputAnnonceViewBean annonce38 = new HEOutputAnnonceViewBean(getSession());
        annonce38.setIdAnnonce(String.valueOf(Integer.parseInt(idAnnonce39001) - 1));
        annonce38.retrieve(getTransaction());

        return annonce38.getRefUnique();
    }

    /**
     * Returns the userID.
     * 
     * @return String
     */
    public String getUserID() {
        return userID;
    }

    private boolean hasDejaCILie(HEOutputAnnonceViewBean annonce95, String ref) {
        try {
            HEOutputAnnonceListViewBean extraitList = new HEOutputAnnonceListViewBean();
            extraitList.setSession(getSession());
            extraitList.setForMotif(annonce95.getMotif());
            extraitList.setLikeEnregistrement("39001");
            extraitList.setForNumeroAVS(annonce95.getNumeroAVS());
            extraitList.setForNumCaisse(annonce95.getNumeroCaisse());
            extraitList.setForRefUnique(ref);
            extraitList.wantCallMethodAfterFind(false);
            extraitList.wantCallMethodBefore(false);
            extraitList.wantCallMethodBeforeFind(false);
            extraitList.find(getTransaction(), BManager.SIZE_NOLIMIT);
            System.out.println(extraitList.size() + " extraits trouvées");
            return extraitList.size() > 0;
        } catch (Exception e) {
            // erreur je retourne que ça existe car sensible
            System.err.println("Erreur dans la méthode hasDejaCILie : " + e.getMessage());
            return true;
        }
    }

    private BufferedReader initRead() throws Exception {
        boolean isFileEBCDIC = "true".equals(((HEApplication) getSession().getApplication())
                .getProperty("ftp.file.input.ebcdic"));

        if (isFileEBCDIC) {

            // je lis de l'EBCDIC
            return new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), HEReprise.ENCODING));
        } else {
            return new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
        }
    }

    private boolean isArcRentes(String codeApp) {
        if (codeAppRentes != null) {
            return codeAppRentes.contains(codeApp);
        } else {
            return false;
        }
    }

    private boolean isGroupPrintPermit(String utilisateurArc) throws Exception {

        if (groupPrintPermit == null) {

            // charge les groupes autorisés
            String groupePermis = ((HEApplication) getSession().getApplication())
                    .getProperty("zas.impression.group.permit");
            StringTokenizer st = new StringTokenizer(groupePermis, ",");
            String[] listGroup = new String[st.countTokens()];

            while (st.hasMoreTokens()) {
                listGroup[st.countTokens() - 1] = st.nextToken();
            }

            groupPrintPermit = Arrays.asList(listGroup);
            System.out.println("Impression automatique pour les utilisateurs des groupes : " + groupPrintPermit);
        }

        if (groupPrintPermit.size() > 0) {
            JadeUserGroupService userGroupService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserGroupService();
            String[] groupeUtilisateur = userGroupService.findAllIdGroupForIdUser(utilisateurArc);

            for (int i = 0; i < groupeUtilisateur.length; i++) {
                if (groupPrintPermit.contains(groupeUtilisateur[i])) {
                    // l'utilisateur appartient à un groupe ayant les permissions
                    // d'impression automatique
                    System.out.println("utilisateur :" + utilisateurArc + " fait partie du groupe automatique");
                    return true;
                }
            }

            // le groupe de l'utilisateur spécifié n'est pas
            // dans le groupe ayant la permission d'imprimer
            //
            System.out.println("utilisateur :" + utilisateurArc + " ne fait pas partie du groupe automatique");

            return false;
        } else {
            System.out.println("utilisateur :" + utilisateurArc + " ne fait pas partie du groupe automatique");

            return false;
        }
    }

    private boolean isPrintableMotif(String motif) {
        return motifsToPrint.contains(motif);
    }

    private boolean isPrintRefContainsRef(HEAnnoncesViewBean annonce, TreeSet toPrintRefs) {
        for (Iterator it = toPrintRefs.iterator(); it.hasNext();) {
            if (((HEAnnoncesViewBean) it.next()).getRefUnique().equals(annonce.getRefUnique())) {
                // on a trouvé déjà une réf pour cette annonce
                return true;
            }
        }

        // on a pas encore mémorisé cette ref (celle de annnonce)
        return false;
    }

    /**
     * Method isRecord.
     * 
     * @param line
     * @return boolean
     */
    private boolean isRecord(String line) throws Exception {

        if (line.startsWith("01")) {
            // jj
            StringBuffer date = new StringBuffer(line.substring(26, 28));
            // mois
            date.append(line.substring(28, 30));
            // annee
            date.append("20");
            date.append(line.substring(30, 32));
            lotTemp = new HELotViewBean(getSession());
            lotTemp.setDateCentrale(date.toString());
            isLotOuvert = true;
            nbreAnnonce = 0;

        }
        if (line.startsWith("99")) {

            if (JadeStringUtil.isBlank(saveAnnonce) || saveAnnonce == null) {
                throw new Exception(getSession().getLabel("EXCEPTION_SAVEANNONCE_MUST_NOT_BE_BLANK_OR_NULL"));
            }
            // contrôle si le nombre d'annonce traitée est
            // égal au nombre d'annonce spéicifié dans la trame 99
            String nbreAnnonceSpec = line.substring(32, 38);
            int nbreSpec = Integer.parseInt(nbreAnnonceSpec);

            if ((nbreAnnonce != 0) && (nbreSpec != nbreAnnonce)) {
                throw new Exception(
                        "Securite : le nombre d'annonces ajoutees ne correspond pas au nombre mentionne dans la trame 99 : "
                                + nbreSpec + " vs " + nbreAnnonce);
            }

            if (isLotOuvert) {
                isLotOuvert = false;
            } else {
                throw new Exception("Securite : le lot ne commence pas par une trame 01 !!!");
            }

            // jj
            StringBuffer date99 = new StringBuffer(line.substring(26, 28));
            // mois
            date99.append(line.substring(28, 30));
            // AAAA
            date99.append("20");
            date99.append(line.substring(30, 32));

            if (!lotTemp.getDateCentrale().equals(date99.toString())) {
                throw new Exception(
                        "Securite : la date de la trame 99 ne correspond pas a la date de la trame 01 : date trame 01 "
                                + lotTemp.getDateCentrale() + " vs date trame 99 " + date99.toString());
            }
            if (nbreAnnonce > 0) {
                if (HEProperties.isCheckDoublonActif(getSession())) {
                    // on recherche si d'autres lots sont présents
                    HELotListViewBean listeLot = new HELotListViewBean();
                    listeLot.setSession(getSession());
                    listeLot.setForType(HELotViewBean.CS_TYPE_RECEPTION);
                    listeLot.setForDateEnvoi(lotTemp.getDateCentrale());
                    listeLot.setForNotEtat(HELotViewBean.CS_LOT_ETAT_OUVERT);
                    listeLot.find(getTransaction());
                    for (int i = 0; i < listeLot.size(); i++) {
                        HELotViewBean l = (HELotViewBean) listeLot.getEntity(i);
                        if (nbreAnnonce == l.getNbAnnonces()) {
                            // pour chaque lots, on récupère la liste d'annonces
                            HEAnnoncesListViewBean listeAnnonces = new HEAnnoncesListViewBean();
                            listeAnnonces.setSession(getSession());
                            listeAnnonces.setForIdLot(l.getIdLot());
                            listeAnnonces.setForEnregistrement(saveAnnonce.toUpperCase());
                            listeAnnonces.find(BManager.SIZE_NOLIMIT);
                            // Si on a une annonce semblable, on lance une exception.
                            if (listeAnnonces.size() > 0) {
                                throw new Exception("Securite : Il existe deja un lot traite le "
                                        + lotTemp.getDateCentrale() + " avec " + l.getNbAnnonces()
                                        + " arcs traités dans la base de donnees");
                            }

                        }

                    }
                }
                // ok il n'y a pas d'autre fichier avec la même date et le même nombre d'arc
                // lot chargé, on change le statut de celui en cours
                if (!JadeStringUtil.isBlankOrZero(lotTemp.getIdLot())) {
                    HELotViewBean crt = new HELotViewBean();
                    crt.setSession(getSession());
                    crt.setIdLot(lotTemp.getIdLot());
                    crt.retrieve(getTransaction());
                    crt.setEtat(HELotViewBean.CS_LOT_ETAT_CHARGE);
                    crt.setHeureTraitement(JACalendar.formatTime(JACalendar.now()));
                    crt.setDateTraitement(JACalendar.todayJJsMMsAAAA());

                    crt.update(getTransaction());
                }
            }
            lotTemp = null;
            commit();
        }

        return ((line.trim().length() != 0) && !line.startsWith("01") && !line.startsWith("99") && (line
                .startsWith("11")
                || line.startsWith("20")
                || line.startsWith("21")
                || line.startsWith("22")
                || line.startsWith("23")
                || line.startsWith("24")
                || line.startsWith("25")
                || line.startsWith("29")
                || line.startsWith("38")
                || line.startsWith("39")
                || line.startsWith("52")
                || line.startsWith("50")
                || line.startsWith("51") || line.startsWith("53") || line.startsWith("61")));
    }

    /**
     * Method isToBeAdded.
     * 
     * @param annonce
     * @return boolean
     */
    private boolean isToBeAdded(HEInputAnnonceViewBean annonce, String motif) throws HEOutputAnnonceException {
        String codeApplication = annonce.getField(IHEAnnoncesViewBean.CODE_APPLICATION);

        // le code est-il à filtrer
        if (codeAppFiltre.contains(codeApplication)) {

            // le code doit être filtré
            if (motifFiltre.contains(motif)) {

                // on ajoute, car c'est un motif que l'on veut
                return true;
            } else { // le code app est filtré, le motif est pas dans la liste des motifs désirés
                return false;
            }
        } else { // il n'est pas à filtrer, on peut l'ajouter
            return true;
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Method loadChamps. Charge les champs d'une annonce
     * 
     * @param champAnnonceM
     * @param idParametrage
     * @return HEChampannonceListViewBean
     * @throws Exception
     */
    private HEChampannonceListViewBean loadChamps(HEChampannonceListViewBean champAnnonceM, String idParametrage)
            throws Exception {

        if (!champAnnonceM.getForIdParametrageAnnonce().equals(idParametrage)) {
            champAnnonceM.setForIdParametrageAnnonce(idParametrage);
            champAnnonceM.find(getTransaction());
        }

        return champAnnonceM;
    }

    /**
     * Method loadCodeAppFiltre.
     * 
     * @param session
     */
    private void loadCodeAppFiltre(BSession session) throws Exception {
        String caf = ((HEApplication) session.getApplication()).getProperty("zas.codeapplication.filtre");
        StringTokenizer st = new StringTokenizer(caf, ",");
        String[] listM = new String[st.countTokens()];

        while (st.hasMoreTokens()) {
            listM[st.countTokens() - 1] = st.nextToken();
        }

        codeAppFiltre = Arrays.asList(listM);
    }

    private void loadCodeAppRentes(BSession session) throws Exception {
        String codeRentes = HEProperties.getParameter(HEProperties.PROP_CODE_APP_RENTES, session, getTransaction());
        StringTokenizer st = new StringTokenizer(codeRentes, ",");
        String[] listM = new String[st.countTokens()];

        while (st.hasMoreTokens()) {
            listM[st.countTokens() - 1] = st.nextToken();
        }

        codeAppRentes = Arrays.asList(listM);
    }

    /**
     * Method loadFields. Charge les valeurs des champs
     * 
     * @param annonce
     * @param champAnnonceM
     * @param line
     * @return HEInputAnnonceViewBean
     */
    private HEInputAnnonceViewBean loadFields(HEInputAnnonceViewBean annonce, HEChampannonceListViewBean champAnnonceM,
            String line) {

        for (int j = 0; j < champAnnonceM.size(); j++) {
            HEChampannonceViewBean champ = (HEChampannonceViewBean) champAnnonceM.getEntity(j);
            int debut = Integer.parseInt(champ.getDebut()) - 1;
            int fin = debut + Integer.parseInt(champ.getLongueur());

            if (HEAnnoncesViewBean.isReferenceInterne(champ.getIdChamp())) {
                annonce.put(champ.getIdChamp(), HEUtil.checkReferenceUnique(line.substring(debut, fin)));
            } else if (HEAnnoncesViewBean.isNumeroAVS(champ.getIdChamp())) {
                // pour le cas où il s'agit d'un numéro AVS, on remplit la table avec
                // sans le signe -, donc soit un numéro avs, soit un NNSS
                String num = line.substring(debut, fin);
                if (IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS.equals(champ.getIdChamp())) {
                    // C'est le champ de NNSS de la confirmation --> setter comme numéro AVS (Exception)
                    annonce.put(champ.getIdChamp(), line.substring(debut, fin));
                    annonce.put(champ.getIdChamp() + HENNSSUtils.PARAM_NNSS, "true");
                } else if (HENNSSUtils.isNNSSNegatif(num)) {
                    annonce.put(champ.getIdChamp(), HENNSSUtils.convertNegatifToNNSS(num));
                    annonce.put(champ.getIdChamp() + HENNSSUtils.PARAM_NNSS, "true");
                } else {
                    // Il s'agit d'un numéro AVS, donc on le met tel quel dans la map
                    annonce.put(champ.getIdChamp(), line.substring(debut, fin));
                }
            } else {
                annonce.put(champ.getIdChamp(), line.substring(debut, fin));
            }
        }

        return annonce;
    }

    /**
     * Method loadMotifFiltre.
     * 
     * @param session
     */
    private void loadMotifFiltre(BSession session) throws Exception {
        String mf = ((HEApplication) session.getApplication()).getProperty("zas.motifs.filtre");
        StringTokenizer st = new StringTokenizer(mf, ",");
        String[] listM = new String[st.countTokens()];

        while (st.hasMoreTokens()) {
            listM[st.countTokens() - 1] = st.nextToken();
        }

        motifFiltre = Arrays.asList(listM);
    }

    /**
     * Method loadMotifsToPrint.
     * 
     * @param session
     */
    private void loadMotifsToPrint(BSession session) throws Exception {
        String motifs = ((HEApplication) session.getApplication()).getProperty("zas.impression.motifs");
        StringTokenizer st = new StringTokenizer(motifs, ",");
        String[] listM = new String[st.countTokens()];

        while (st.hasMoreTokens()) {
            listM[st.countTokens() - 1] = st.nextToken();
        }

        motifsToPrint = Arrays.asList(listM);
    }

    /**
     * Method loadParametrage. Charge le parametrage
     * 
     * @param codeApp
     * @param codeEnr
     * @param line
     * @param paramM
     * @return HEParametrageannonce
     * @throws Exception
     */
    private HEParametrageannonce loadParametrage(String codeApp, String codeEnr, String line,
            HEParametrageannonceManager paramM) throws Exception {

        String csCodeApp = "";

        if (line.startsWith("38")) {

            if (line.substring(71, 72).equals("1")) {
                csCodeApp = "111011";
            } else if (line.substring(71, 72).equals("2")) {
                csCodeApp = "111040";
            }
        } else {
            csCodeApp = getCSCodeApp(codeApp);
        } // je recharge seulement si C nécessaire

        if (!paramM.getForIdCSCodeApplication().equals(csCodeApp)
                || !paramM.getForAfterCodeEnregistrementDebut().equals(StringUtils.unPad(codeEnr))
                || !paramM.getForBeforeCodeEnregistrementFin().equals(StringUtils.unPad(codeEnr))) {
            paramM.setForIdCSCodeApplication(csCodeApp);
            paramM.setForAfterCodeEnregistrementDebut(StringUtils.unPad(codeEnr));
            paramM.setForBeforeCodeEnregistrementFin(StringUtils.unPad(codeEnr));
        }

        paramM.find(getTransaction());

        return (HEParametrageannonce) paramM.getFirstEntity();
    }

    private String readLine(BufferedReader file, boolean hasCarridgeReturn) throws Exception {

        if (hasCarridgeReturn) {
            return file.readLine();
        } else {
            int nread = 0;

            if ((nread = file.read(fileBuffer)) >= 0) {
                return String.valueOf(fileBuffer, 0, nread);
            } else {
                return null;
            }
        }
    }

    /**
     * Method removeFromOrphan.
     * 
     * @param string
     */
    private void removeFromOrphan(String ref) {

        if (orphanSplitting.contains(ref)) {
            orphanSplitting.remove(ref);
        }
    }

    /**
     * @param string
     */
    public void setParam0(String string) {
        param0 = string;
    }

    /**
     * @param string
     */
    public void setParam1(String string) {
        param1 = string;
    }

    /**
     * Sets the pwd.
     * 
     * @param pwd
     *            The pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Method setToOrphan.
     * 
     * @param string
     */
    private void setToOrphan(String ref) {

        if (!orphanSplitting.contains(ref)) {
            orphanSplitting.add(ref);
        }
    }

    /**
     * Sets the userID.
     * 
     * @param userID
     *            The userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    private void traiterRetours(HELotViewBean lotCrt) throws Exception, HEOutputAnnonceException {
        if (HELotViewBean.CS_TYPE_RECEPTION.equals(lotCrt.getType())) {
            HEOutputAnnonceListViewBean annoncesATraiter = new HEOutputAnnonceListViewBean();
            annoncesATraiter.setSession(getSession());
            annoncesATraiter.setForIdLot(lotCrt.getIdLot());
            annoncesATraiter.setForCodeEnr01Or001(true);
            annoncesATraiter.setForNotLikeCodeAppl("20");
            annoncesATraiter.setForNotLikeCodeAppl2("38");
            annoncesATraiter.setForNotStatut(IHEAnnoncesViewBean.CS_A_TRAITER);
            annoncesATraiter.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");
            BStatement stat = annoncesATraiter.cursorOpen(getTransaction());
            HEOutputAnnonceViewBean annonceOut;
            String thisIdLot = lotCrt.getIdLot();

            while ((annonceOut = (HEOutputAnnonceViewBean) annoncesATraiter.cursorReadNext(stat)) != null) {

                String thisRef = annonceOut.getRefUnique();
                System.out.print("\n" + DateUtils.getTimeStamp() + "Recherche d'attentes pour cette réf : " + thisRef
                        + " et le numéro de lot : " + thisIdLot);

                // attend-on un retour pour ça
                HEAttenteRetourListViewBean attenteRetourM = new HEAttenteRetourListViewBean(getSession());
                attenteRetourM.setForIdAnnonceRetourAttendue(annonceOut.getIdParamAnnonce());
                attenteRetourM.setForIdAnnonceRetour("0");

                if (annonceOut.getChampEnregistrement().startsWith("2201")) {
                    annonceOut.setNumeroCaisse(annonceOut.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI)
                            + "."
                            + StringUtils.removeUnsignigicantZeros(annonceOut
                                    .getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI)));
                }

                attenteRetourM.setForNumeroCaisse(annonceOut.getNumeroCaisse());
                attenteRetourM.setForNumeroAVS(annonceOut.getNumeroAVS());
                attenteRetourM.setForMotif(annonceOut.getMotif());
                attenteRetourM.wantCallMethodAfterFind(false);
                attenteRetourM.wantCallMethodAfter(false);
                attenteRetourM.find(getTransaction(), BManager.SIZE_NOLIMIT);

                if (attenteRetourM.size() == 0) {
                    System.out.print("\n" + DateUtils.getTimeStamp()
                            + "Pas d'attente retour pour la série ayant comme param,type="
                            + annonceOut.getIdParamAnnonce() + " numAVS=" + annonceOut.getNumeroAVS() + " num caisse="
                            + annonceOut.getNumeroCaisse() + " motif=" + annonceOut.getMotif());

                }

                if (!annonceOut.getChampEnregistrement().startsWith("2001") && (attenteRetourM.size() >= 1)) {

                    if (attenteRetourM.size() > 1) {
                        System.out.println("\n" + DateUtils.getTimeStamp()
                                + " Attention, plus de 1 attente retour trouvée pour le n° AVS pour le num AVS :"
                                + annonceOut.getNumeroAVS() + " et motif :" + annonceOut.getMotif() + " et caisse "
                                + annonceOut.getNumeroCaisse() + " et type :" + annonceOut.getIdParamAnnonce());

                        for (int r = 0; r < attenteRetourM.size(); r++) {
                            System.out.println("Attente numero "
                                    + ((HEAttenteRetourViewBean) attenteRetourM.getEntity(r)).getIdAttenteRetour()
                                    + " trouvee");
                        }
                    }

                    // on a trouvé une attente
                    // je récupère l'annonce qui attend
                    HEAttenteRetourViewBean attenteRetour = (HEAttenteRetourViewBean) attenteRetourM.getFirstEntity();

                    // pour le cas des 2501, il faut modifier la référence des attentes retours
                    if (annonceOut.getChampEnregistrement().startsWith("2501")) {
                        HEAttenteRetourListViewBean attenteRetour25 = new HEAttenteRetourListViewBean(getSession());
                        attenteRetour25.setForReferenceUnique(annonceOut.getRefUnique());
                        attenteRetour25.find(getTransaction(), BManager.SIZE_NOLIMIT);

                        HEAttenteRetourViewBean retour;

                        for (int index = 0; index < attenteRetour25.size(); index++) {
                            retour = (HEAttenteRetourViewBean) attenteRetour25.getEntity(index);
                            retour.setReferenceUnique(attenteRetour.getReferenceUnique());
                            retour.wantCallValidate(false);
                            retour.wantCallMethodAfter(false);
                            retour.wantCallMethodBefore(false);
                            retour.update(getTransaction());
                        }
                    }

                    HEOutputAnnonceViewBean annonceQuiAttend = new HEOutputAnnonceViewBean(getSession());

                    if ("95".equals(annonceOut.getMotif()) && annonceOut.getChampEnregistrement().startsWith("3")) {

                        // si c'est un CI de splitting, il faut vérifier contre le numéro du conjoint
                        System.out.print(" CI Splitting");

                        // y'a peut-être plusieurs attenteRetours
                        // je prend attenteRetourM, je compile les refs, je recherche le 1105 dont le numéro du conjoint
                        // correspond
                        Vector tmpRefs = new Vector();

                        for (int ii = 0; ii < attenteRetourM.size(); ii++) {
                            String ref = ((HEAttenteRetourViewBean) attenteRetourM.getEntity(ii)).getReferenceUnique();

                            if (!tmpRefs.contains(ref)) {
                                tmpRefs.add(ref);
                            }
                        }

                        for (int iii = 0; iii < tmpRefs.size(); iii++) {
                            HEOutputAnnonceListViewBean tmpAnnonces = new HEOutputAnnonceListViewBean(getSession());
                            tmpAnnonces.setForRefUnique((String) tmpRefs.get(iii));
                            tmpAnnonces.setForCodeApplicationLike("2501");
                            tmpAnnonces.find(getTransaction());

                            HEOutputAnnonceViewBean tmpAnnonce = (HEOutputAnnonceViewBean) tmpAnnonces.getFirstEntity();

                            if (tmpAnnonce == null) {
                                // TODO if null
                                // PAS de 25...
                            } else {

                                if (annonceOut.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE)
                                        .equals(tmpAnnonce
                                                .getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE))
                                        && annonceOut.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA).equals(
                                                tmpAnnonce.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA))) {

                                    annonceQuiAttend.setIdAnnonce(tmpAnnonce.getIdAnnonce());
                                    removeFromOrphan(annonceOut.getRefUnique());
                                    System.out.print(" ---> " + tmpAnnonce.getRefUnique());
                                } else {

                                    // c'est une annonce qui a une attente plausible
                                    // mais l'attente ne lui correspond pas
                                    setToOrphan(annonceOut.getRefUnique());
                                    System.out.print(" ---> ??? ");
                                }
                            }
                        }
                    } else if (!annonceOut.getChampEnregistrement().startsWith("3")
                            || !"95".equals(annonceOut.getMotif())) {
                        annonceQuiAttend.setIdAnnonce(attenteRetour.getIdAnnonce());
                    }

                    if (!JadeStringUtil.isEmpty(annonceQuiAttend.getIdAnnonce())) {

                        annonceQuiAttend.retrieve(getTransaction());

                        HEOutputAnnonceListViewBean outputAnnonceM = new HEOutputAnnonceListViewBean();
                        outputAnnonceM.setSession(getSession());
                        outputAnnonceM.setForRefUnique(annonceOut.getRefUnique());
                        outputAnnonceM.setOrder(HEOutputAnnonceListViewBean.ORDER_BY_RNIANN);
                        outputAnnonceM.find(getTransaction(), BManager.SIZE_NOLIMIT);

                        for (int j = 0; j < outputAnnonceM.size(); j++) {
                            HEOutputAnnonceViewBean annonceOut2 = (HEOutputAnnonceViewBean) outputAnnonceM.getEntity(j);
                            annonceOut2.setUtilisateur(annonceQuiAttend.getUtilisateur());
                            annonceOut2.setIdProgramme(annonceQuiAttend.getIdProgramme());

                            if (j == 0) {
                                System.out.print(" --> " + annonceQuiAttend.getRefUnique() + " ");
                            }

                            annonceOut2.setRefUnique(annonceQuiAttend.getRefUnique());
                            annonceOut2.setStatut(annonceQuiAttend.getStatut());
                            annonceOut2.wantCallMethodAfter(false);
                            annonceOut2.wantCallMethodBefore(false);
                            annonceOut2.wantCallValidate(false);
                            System.out.print(".");
                            annonceOut2.update(getTransaction());
                        }

                        attenteRetour.setIdAnnonceRetour(annonceOut.getIdAnnonce());
                        attenteRetour.setReferenceUnique(annonceQuiAttend.getRefUnique());
                        attenteRetour.wantCallMethodAfter(false);
                        attenteRetour.wantCallMethodBefore(false);
                        attenteRetour.wantCallValidate(false);
                        attenteRetour.update(getTransaction());
                        // temporaire,migration de l'autre procédé, check si on a une attente pour le 38
                        if ("9".equals(attenteRetour.getIdAnnonceRetourAttendue())) {
                            attenteRetourM.setForIdAnnonceRetourAttendue("8");
                            attenteRetourM.setForReferenceUnique(attenteRetour.getReferenceUnique());
                            attenteRetourM.setForNumeroCaisse(annonceOut.getNumeroCaisse());
                            attenteRetourM.setForNumeroAVS(annonceOut.getNumeroAVS());
                            attenteRetourM.setForMotif(annonceOut.getMotif());
                            attenteRetourM.wantCallMethodAfterFind(false);
                            attenteRetourM.wantCallMethodAfter(false);
                            attenteRetourM.find(getTransaction(), BManager.SIZE_NOLIMIT);
                            if (attenteRetourM.size() == 1) {
                                // on a trouve un 8 qui n'est plus nécessaire
                                attenteRetour = (HEAttenteRetourViewBean) attenteRetourM.getFirstEntity();
                                attenteRetour.delete(getTransaction());
                            }
                        }
                    }
                } else {

                    // pas d'attente retour
                    System.out.print(DateUtils.getTimeStamp()
                            + "Pas d'attente retour pour la série ayant comme param,type="
                            + annonceOut.getIdParamAnnonce() + " numAVS=" + annonceOut.getNumeroAVS() + " num caisse="
                            + annonceOut.getNumeroCaisse() + " motif=" + annonceOut.getMotif());

                    // on vérifie que cette annonce a pas déjà été confirmée
                    HEAttenteRetourListViewBean retourQuestion = new HEAttenteRetourListViewBean(getSession());
                    retourQuestion.setForIdAnnonceRetour(annonceOut.getIdAnnonce());
                    retourQuestion.find(getTransaction(), BManager.SIZE_NOLIMIT);

                    if (retourQuestion.size() == 0) { // pas trouvé d'attente

                        // pas reçue
                        // c'est une orpheline
                        // on change le statut
                        HEAnnoncesListViewBean annoncesOrphelines = new HEAnnoncesListViewBean();
                        annoncesOrphelines.setSession(getSession());
                        annoncesOrphelines.setForRefUnique(annonceOut.getRefUnique());
                        annoncesOrphelines.setOrder(HEOutputAnnonceListViewBean.ORDER_BY_RNIANN);
                        annoncesOrphelines.find(getTransaction(), BManager.SIZE_NOLIMIT);

                        String statutO = "";
                        System.out.println("\n");

                        for (int j = 0; j < annoncesOrphelines.size(); j++) {
                            HEAnnoncesViewBean annonceOrpheline = (HEAnnoncesViewBean) annoncesOrphelines.getEntity(j);

                            if (j == 0) {
                                System.out.println(DateUtils.getTimeStamp() + "\tChangement de statut pour "
                                        + annonceOrpheline.getRefUnique());
                            }

                            if (annonceOrpheline.getChampEnregistrement().startsWith("2201")) {
                                HEOutputAnnonceViewBean annonceOrphelineOut = new HEOutputAnnonceViewBean(getSession());
                                annonceOrphelineOut.setIdAnnonce(annonceOrpheline.getIdAnnonce());
                                annonceOrphelineOut.retrieve(getTransaction());

                                if (!annonceOrphelineOut
                                        .getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE)
                                        .equals(((HEApplication) getSession().getApplication()).getProperty("noCaisse"))) {
                                    statutO = IHEAnnoncesViewBean.CS_TERMINE;
                                } else {
                                    // statut pour les 22 sans ARC de départ
                                    // dépend de la caisse, au cas où y'a des filtres
                                    statutO = ((HEApplication) getSession().getApplication())
                                            .getProperty("zas.22.orphanstatus");
                                }
                            } else if (annonceOrpheline.getChampEnregistrement().startsWith("20")) {
                                statutO = annonceOrpheline.getStatut();
                            } else if (annonceOrpheline.getChampEnregistrement().startsWith("23")) {
                                statutO = IHEAnnoncesViewBean.CS_TERMINE;
                            } else if (annonceOrpheline.getChampEnregistrement().startsWith("21")) {
                                // // ALD : ajout pour NNSS caisse suisse, on recherche une confirmation reçue
                                HEOutputAnnonceListViewBean liste20 = new HEOutputAnnonceListViewBean();
                                liste20.setSession(getSession());
                                liste20.setLikeEnregistrement("2001");
                                liste20.setForNumeroAVS(annonceOrpheline.getNumeroAVS());
                                liste20.setForMotif(annonceOrpheline.getMotif());
                                liste20.setOrder(" RNDDAN DESC");
                                liste20.find(getTransaction());
                                if (liste20.size() > 0) {
                                    // On a trouve un 20, on l'associe à celui trouvé
                                    HEOutputAnnonceViewBean arc20 = (HEOutputAnnonceViewBean) liste20.getFirstEntity();
                                    annonceOrpheline.setRefUnique(arc20.getRefUnique());
                                    statutO = arc20.getStatut();
                                } else {
                                    statutO = IHEAnnoncesViewBean.CS_ORPHELIN;
                                }
                            } else if (annonceOrpheline.getChampEnregistrement().startsWith("29")) {

                                statutO = IHEAnnoncesViewBean.CS_TERMINE;

                            } else if (annonceOrpheline.getChampEnregistrement().startsWith("39")) {
                                HEOutputAnnonceViewBean annonceOrphelineOut = new HEOutputAnnonceViewBean(getSession());
                                annonceOrphelineOut.setIdAnnonce(annonceOrpheline.getIdAnnonce());
                                annonceOrphelineOut.retrieve(getTransaction());

                                if (annonceOrpheline.getChampEnregistrement().startsWith("39001")) {

                                    if (annonceOrphelineOut.getField(IHEAnnoncesViewBean.CI_ADDITIONNEL).equals("1")) {
                                        statutO = IHEAnnoncesViewBean.CS_TERMINE;
                                        // // ajouter la référence pour imprimer une liste des extraits Cis additionnels
                                        if ("true".equals(getSession().getApplication().getProperty(
                                                "extraitCI.additionnel.impression", "false"))) {
                                            if (!toPrintCIAdditionnel.contains(annonceOrpheline.getRefUnique())) {
                                                toPrintCIAdditionnel.add(annonceOrpheline.getRefUnique());
                                            }
                                        }
                                        System.out.println(DateUtils.getTimeStamp() + "CI Additionel "
                                                + annonceOrpheline.getRefUnique());
                                        // je change toutes les 38 avec
                                        HEOutputAnnonceListViewBean annonces38 = new HEOutputAnnonceListViewBean(
                                                getSession());
                                        annonces38.setForCodeApplication("38");
                                        annonces38.setForRefUnique(annonceOrpheline.getRefUnique());

                                        annonces38.find(getTransaction(), BManager.SIZE_NOLIMIT);

                                        HEOutputAnnonceViewBean annonce38;
                                        //
                                        for (int k = 0; k < annonces38.size(); k++) {
                                            annonce38 = (HEOutputAnnonceViewBean) annonces38.getEntity(k);
                                            annonce38.wantCallMethodAfter(false);
                                            annonce38.wantCallMethodBefore(false);
                                            annonce38.wantCallValidate(false);
                                            annonce38.setStatut(statutO);
                                            annonce38.update(getTransaction());
                                        }

                                    } else {
                                        System.out.println(DateUtils.getTimeStamp() + "CI Orphelin "
                                                + annonceOrpheline.getRefUnique());
                                        statutO = IHEAnnoncesViewBean.CS_ORPHELIN;
                                    }
                                }
                            }

                            if (!annonceOrpheline.getChampEnregistrement().startsWith("38")) {

                                // pour rien faire dans les 38 car c'est dans le 39 qu'on le fait
                                annonceOrpheline.setStatut(statutO);
                                annonceOrpheline.wantCallMethodAfter(false);
                                annonceOrpheline.wantCallMethodBefore(false);
                                annonceOrpheline.wantCallValidate(false);

                                if (j == 0) {
                                    System.out.println(DateUtils.getTimeStamp() + "\t" + getSession().getCode(statutO));
                                }

                                annonceOrpheline.update(getTransaction());
                                System.out.println(DateUtils.getTimeStamp() + "Màj du statut pour le "
                                        + annonceOrpheline.getChampEnregistrement().substring(0, 5)
                                        + " effectué avec succès");
                            }
                        }
                    }
                }
            }
            annoncesATraiter.cursorClose(stat);
        }
    }

    private void traiterSeries(HELotViewBean crt) throws Exception {
        System.out.println("Execute prepare statement for terminating series");
        List<String> l = TISQL.querySingleField(crt.getSession(), "distinct RNREFU RNREFU", "FROM "
                + Jade.getInstance().getDefaultJdbcSchema()
                + ".HEANNOP h  where RNTSTA=117002 AND NOT EXISTS(SELECT ROIARA FROM "
                + Jade.getInstance().getDefaultJdbcSchema() + ".HEAREAP WHERE HEA_RNIANN=0 AND h.RNREFU=ROLRUN)");

        Iterator it = l.iterator();
        BStatement terminatingSeries = new BStatement(getTransaction());
        terminatingSeries.createStatement();
        while (it.hasNext()) {
            String reference = (String) it.next();
            if (!JadeStringUtil.isEmpty(reference)) {
                terminatingSeries.execute(HEOutputAnnonceListViewBean.getSqlForTerminateSeries(JadeStringUtil
                        .removeChar(reference, ' ')));
            }
        }
        terminatingSeries.closeStatement();
    }

}
