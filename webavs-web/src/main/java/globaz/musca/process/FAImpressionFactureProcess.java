package globaz.musca.process;

import globaz.docinfo.FADocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIContainer;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.log.JadeLogger;
import globaz.jade.pdf.JadePdfUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAModuleImpression;
import globaz.musca.api.IFAPassage;
import globaz.musca.api.IFAPrintDoc;
import globaz.musca.constantes.EFAProperties;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleImpression;
import globaz.musca.db.facturation.FAModuleImpressionManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.external.IntModuleImpression;
import globaz.musca.external.ServicesFacturation;
import globaz.musca.itext.FAImpressionFacture_BVR_Doc;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.skirnir.client.SkirnirUtils;

public class FAImpressionFactureProcess extends FAGenericProcess implements IFAImpressionFactureProcess {

    private static final long serialVersionUID = 5666547535629204700L;

    public enum ModeFonctionnementEnum {
        INFOROM336,
        STANDARD
    }

    public static final String CLASSE_IMPLEMENTATION_REMBOURSEMENT_INFOROM200 = "globaz.musca.api.musca.FAInfoRom200RembourserImpl";
    public static final String DOCTYPE_FACTURE = "1";
    public static final String DOCTYPE_LETTER = "2";
    public final static String FACTURES_A_IMPRIMER_BLOQUEES = "bloquees";
    public final static String FACTURES_A_IMPRIMER_NORMALES = "normales";
    // private final static String WRAPPER_WORKINGDIR = "work";
    public final static String FACTURES_A_IMPRIMER_TOUTES = "toutes";

    public final static String IMPRESSION_LETTRE = "lettres";

    /**
     * Lancement de l'impression en mode batch Date de création : (05.05.2003 15:53:19)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {

        System.out
                .println("Utiliser FAImpressionFacture <user> <pwd> <[idPassage]> <dateImpression> <from> <to> <email> <y> pour JADE\n");
        FAImpressionFactureProcess process = null;

        String user = "";
        String pwd = "";
        String idPassage = "";
        String dateImpression = "";
        String from = ""; // "180.100";
        String to = ""; // "190.000";
        // remplacer selon les properties
        String email = "";
        String useJade = "n";
        if (args.length > 0) {
            user = args[0];
        }
        if (args.length > 1) {
            pwd = args[1];
        }
        if (args.length > 2) {
            idPassage = args[2];
        }
        if (args.length > 3) {
            dateImpression = args[3];
        }
        if (args.length > 4) {
            from = args[4];
        }
        if (args.length > 5) {
            to = args[5];
        }
        if (args.length > 6) {
            email = args[6];
        }
        if (args.length > 7) {
            useJade = args[7];
        }
        System.out.println("User : " + user);
        System.out.println("Password : " + pwd);
        System.out.println("IdPassage : " + idPassage);
        System.out.println("Date impression: " + dateImpression);
        System.out.println("From : " + from);
        System.out.println("To : " + to);
        System.out.println("useJade:" + useJade);
        System.out.println("Email : " + email);

        long t0 = (new Date()).getTime();

        System.out.println("TEST IMPRESSION:user " + user + " pwd: " + pwd);

        try {
            process = new FAImpressionFactureProcess();
            process.setIdPassage(idPassage);
            if (from != null) {
                process.setFromIdExterneRole(from);
            }
            if (to != null) {
                process.setTillIdExterneRole(to);
            }
            if (!"".equals(dateImpression)) {
                process.setDateImpression(dateImpression);
            }
            // utilisation de JADE
            if ("y".equalsIgnoreCase(useJade)) {
                FAGenericProcess.jadeProfile(process);
            } else {

                BSession session = new BSession("MUSCA");

                System.out.println("************ CONNECTING *************");
                session.connect(user, pwd);

                process.setSession(session);
                GlobazServer.getCurrentSystem().getApplication(process.getSession().getApplicationId());
                process.setEMailAddress(email);
                process.setTransaction(new BTransaction(process.getSession()));
                process.getTransaction().openTransaction();
                JadeJobInfo job = BProcessLauncher.start(process);

                while ((!job.isOut()) && (!job.isError())) {
                    Thread.sleep(1000);
                    job = JadeJobServerFacade.getJobInfo(job.getUID());
                }
                Thread.sleep(60000);
                if (job.isError()) {
                    // erreurs critique, je retourne le code de retour not ok
                    System.out.println("Process Cloture Journaux CIs not executed successfully !");
                    System.out.println(job.getFatalErrorMessage());
                    System.exit(1);
                } else {
                    // pas d'erreurs critique, je retourne le code de retour ok
                    System.out.println("Process Cloture Journaux CIsexecuted successfully !");
                    System.exit(2);
                }

            }

            System.out.println("Programme terminé ! Copier le fichier PDF avant de presser <Enter>....");
            System.out.println("Temps : " + (((new Date()).getTime() - t0) / 1000.0));
            System.in.read();
            System.out.println("Arrêt du programme lancé !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }

    }

    protected java.lang.String aImprimer = new String();
    // le constructeur d'un document
    protected java.lang.String dateImpression = new String();
    private String documentType = new String();
    private boolean flagCodeMaj = false;
    private java.lang.String fromIdExterneRole = new String();
    private String idPersRef = new String();
    private String idSign1 = new String();
    private String idSign2 = new String();
    private java.lang.String idSousType = new String();
    private java.lang.String idTri = new String();
    private java.lang.String idTriDecompte = new String();
    // Le nom de la classe d'impression
    private String ImpressionClassName = new String();
    private Boolean impressionDef = new Boolean(false);
    protected boolean imprimable = false;
    public java.util.HashMap interfaceImpressionContainer = null;

    protected boolean isNewDocument = true;

    // Factures normales)
    // user pour référence
    JadeUser jadeUser = null;

    private java.lang.String libelle = new String();

    private ModeFonctionnementEnum modeFonctionnement = ModeFonctionnementEnum.INFOROM336;

    protected globaz.musca.db.facturation.FAModuleImpressionManager modImpManager;
    protected java.util.HashMap modImpMap;
    // Variable pour le comptage
    /* CHANGED */protected int nbImprimer = 0; // compteur d'entêtes de facture
    // nombre d'entêtes de facture que le manager contient
    protected int nbPasImprime = 0;
    private int shouldNbImprimer = 0;
    private int sizeManager = 0;
    private java.lang.String tillIdExterneRole = new String();
    // public static final int TAILLE_LOT =
    // (getSession().getApplication()).getProperty("mettreGed");
    private String titreDocument; // nom donné au document (Factures bloquées,

    public boolean unificationProcess = false;

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAImpressionFactureProcess() throws Exception {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAImpressionFactureProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAImpressionFactureProcess(BSession session) throws Exception {
        super(session);
    }

    @Override
    public boolean _createDocument(BIContainer container, IntModuleImpression interface_moduleImpression,
            String impressionClassName) {

        BStatement statement = null;
        BManager manager = null;
        try {

            manager = (BManager) container;
            BEntity myEntity = null;
            statement = manager.cursorOpen(getTransaction());
            // traitement des lignes trouvées
            int progressCounter = 0;
            int cpt = 0;
            while (((myEntity = manager.cursorReadNext(statement)) != null) && (!myEntity.isNew())
                    && !super.isAborted()) {
                cpt++;
                // setProgressDescription(entete.getIdExterneRole()+" <br>"+cpt+"/"+manager.size()+"<br>");
                setProgressCounter(++progressCounter);
                if (isAborted()) {
                    // setProgressDescription("Traitement interrompu<br> sur l'affilié : "
                    // +
                    // entete.getIdExterneRole()+" <br>"+cpt+"/"+manager.size()+"<br>");
                    // if(getParent()!=null && getParent().isAborted()){
                    // getParent().setProcessDescription("Traitement interrompu<br> sur l'affilié : "
                    // +
                    // entete.getIdExterneRole()+" <br>"+cpt+"/"+manager.size()+"<br>");
                    // }
                    break;
                } else {
                    // la prochaine facture à imprimer
                    // valeur dynamique du compteur du progress

                    // Instancier une classe d'implémentation avec son nom si
                    // non vide
                    if (!JadeStringUtil.isBlank(impressionClassName)) {

                        interface_moduleImpression = _getInterfaceImpressionFromCache(impressionClassName);

                        interface_moduleImpression.beginPrinting(passage, this);
                        interface_moduleImpression.add(myEntity);
                        // success =
                        // interface_moduleImpression.print(
                        // (BIEntity) myEntity,
                        // this);
                        // success =
                        // interface_moduleImpression.endPrinting(passage,
                        // this);

                        // construit le document pour la prochaine facture avec
                        // l'entête suivante
                        nbImprimer++;

                    } else {
                        // il n'existe pas d'implémentation de classe
                        // d'impression pour ce type de
                        // décopmpte
                        getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FA_MODULEIMPRESSION_ERROR"),
                                FWViewBeanInterface.ERROR, this.getClass().getName());
                        nbPasImprime++;

                    }
                }

            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        } finally {
            try {
                // fermer le cursor
                if (manager != null) {
                    manager.cursorClose(statement);
                    statement = null;
                }
            } catch (Exception ee) {
                getMemoryLog().logMessage(ee.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                return false;

            } finally {
                return !isAborted();
            }

        }

    }

    /**
     * Insert the method's description here. Creation date: (12.06.2003 11:15:52)
     * 
     * @return boolean
     */
    public boolean _createDocument(FAEnteteFactureManager manager, IntModuleImpression interface_moduleImpression) {

        BStatement statement = null;
        try {
            // IFAPrintDoc enteteFacture = null;
            FAEnteteFacture entete = null;

            statement = manager.cursorOpen(getTransaction());
            // traitement des lignes trouvées
            int progressCounter = manager.getCount();
            setProgressScaleValue(progressCounter);
            int cpt = 0;
            while (((entete = (FAEnteteFacture) manager.cursorReadNext(statement)) != null) && (!entete.isNew())) {
                cpt++;
                setProgressDescription(entete.getIdExterneRole() + " <br>" + cpt + "/" + progressCounter + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : " + entete.getIdExterneRole()
                            + " <br>" + cpt + "/" + progressCounter + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : " + entete.getIdExterneRole() + " <br>"
                                        + cpt + "/" + progressCounter + "<br>");
                    }
                    break;
                } else {
                    incProgressCounter();
                    FAAfactManager afact = new FAAfactManager();
                    afact.setSession(getSession());
                    afact.setForIdEnteteFacture(entete.getId());
                    afact.setImpression(new Boolean(true));
                    afact.find();
                    if (afact.size() > 0) {
                        // la prochaine facture à imprimer
                        // valeur dynamique du compteur du progress
                        // this.setProgressCounter(++progressCounter);
                        FAModuleImpression moduleImpression = (FAModuleImpression) _getModuleImpressionByCritere(entete);
                        // imprimer la facture si le module d'impression
                        // correspond à ce type d'impression de facture
                        if (moduleImpression != null) {
                            String nomClasse = moduleImpression.getNomClasse();

                            // Instancier une classe d'implémentation avec son
                            // nom si non vide
                            if (!JadeStringUtil.isBlank(nomClasse)) {

                                interface_moduleImpression = _getInterfaceImpressionFromCache(nomClasse);

                                interface_moduleImpression.beginPrinting(passage, this);
                                interface_moduleImpression.add(entete);
                                // success =
                                // interface_moduleImpression.endPrinting(
                                // passage,
                                // this);

                                // construit le document pour la prochaine
                                // facture avec l'entête suivante
                                nbImprimer++;

                            } else {
                                // il n'existe pas d'implémentation de classe
                                // d'impression pour ce type de
                                // décopmpte
                                getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FA_MODULEIMPRESSION_ERROR"),
                                        FWViewBeanInterface.ERROR, this.getClass().getName());
                                nbPasImprime++;

                            }
                        }

                    }
                }

            } // end while
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        } finally {
            try {
                // fermer le cursor
                manager.cursorClose(statement);
                statement = null;
            } catch (Exception ee) {
                getMemoryLog().logMessage(ee.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                return false;

            } finally {
                return !isAborted();
            }

        }

    }

    protected boolean _createDocumentFromManager(FAEnteteFactureManager manager) {
        boolean success = false;
        // manager.setForAImprimer(getAImprimer());

        try {
            if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE)) {
                CAInteretMoratoireManager intMana = new CAInteretMoratoireManager();
                intMana.setSession(getSession());
                intMana.setForIdJournalFacturation(passage.getIdPassage());
                shouldNbImprimer = intMana.getCount(getTransaction());
            } else if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES)) {
                FAAfactManager managerInt = new FAAfactManager();
                managerInt.setSession(getSession());
                managerInt.setIsAfacForBulletinsSoldes(true);
                managerInt.setForIdPassage(passage.getIdPassage());
                managerInt.setForMontantSuppZero(true);
                managerInt.setForIdExterneFactureCompensationNotEmpty(Boolean.TRUE);
                managerInt.wantCallMethodAfter(false);
                shouldNbImprimer = managerInt.getCount(getTransaction());
            } else {
                shouldNbImprimer = manager.getCount(getTransaction());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Ne peut pas obtenir le COUNT(*) du manager", FWViewBeanInterface.ERROR,
                    this.getClass().getName());

        }
        // le manager ne contient aucune entête de facture
        if (shouldNbImprimer == 0) {
            // getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FAPRINT_NOFACTURE"),
            // globaz.framework.util.FWMessage.INFORMATION, getClass().getName());
            return true;
        }

        // Entrer les informations pour l' état du process
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_CREATIONDOCUMENTS"));
        if (shouldNbImprimer > 0) {
            setProgressScaleValue(shouldNbImprimer);
        } else {
            setProgressScaleValue(1);
        }

        // interface_moduleImpression is of type FAImpressionFactureBVR
        IntModuleImpression interface_moduleImpression = null;

        // appel de la méthode d'impression effective
        if (JadeStringUtil.isEmpty(getDocumentType()) || JadeStringUtil.isBlank(getDocumentType())) {
            success = this._createDocument(manager, interface_moduleImpression);
        } else if (FAImpressionFactureProcess.DOCTYPE_LETTER.equalsIgnoreCase(getDocumentType())) {
            interface_moduleImpression = _getInterfaceImpressionFromCache(getImpressionClassName());

            // Remplacer par le BIManager!
            BIContainer morManager = interface_moduleImpression.get_documentManager(this);

            success = this._createDocument(morManager, interface_moduleImpression, getImpressionClassName());
        }

        if (getProgress() != 0) {
            setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_WRAPPING"));
            // TBD en label
            success = _doWrapperDocument();
            // fusionner tous les documents et retourner si successful
        } else {
            success = false;
        }

        return success;
    }

    /**
     * Launch a process for each entete to print Creation date: (12.06.2003 11:33:57)
     * 
     * @return boolean
     */
    @Override
    public boolean _doWrapperDocument() {
        try {
            Iterator<IntModuleImpression> doc = interfaceImpressionContainer.values().iterator();
            while (doc.hasNext()) {
                IntModuleImpression myMod = doc.next();
                myMod.print();
            }
            return true;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return false;
        }
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    public boolean _executeImpressionProcess(IFAPassage p) {
        // test du passage
        FAPassage passage = (FAPassage) p;

        boolean success = false;

        if ((passage == null) || passage.isNew()) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setSession(getSession());
            try {
                passage.retrieve(getTransaction());
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, this.getClass().getName());
                return false;
            }
        }

        // mettre le passage passé en param en variable de classe
        this.setPassage(passage);
        try {
            if (!JadeStringUtil.isBlank(getPassage().getPersonneRef())) {
                JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                jadeUser = service.loadForVisa(getPassage().getPersonneRef());
            }
        } catch (Exception ex) {
            // impossible de récupérer les informations de la personne de
            // contact
            jadeUser = null;
        }

        // instancier un nouveau cache
        modImpMap = new java.util.HashMap();

        // mettre en cache le module d'impression
        modImpManager = new FAModuleImpressionManager();
        modImpManager.setSession(getSession());

        try {
            modImpManager.find();
            for (int i = 0; i < modImpManager.size(); i++) {
                // Stocker dans le cache
                FAModuleImpression modImpression = (FAModuleImpression) modImpManager.getEntity(i);
                modImpMap.put(modImpression.getIdCritereDecompte() + modImpression.getIdModeRecouvrement(),
                        modImpression);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, this.getClass().getName());
        }

        if (!globaz.globall.util.JAUtil.isDateEmpty(dateImpression)) {
        }
        if (!(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE))
                && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES))
                && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_LETTRE_TAXE_CO2))) {
            executeRemboursementLSV();
        }

        // état du process
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_RECHERCHEDONNEE"));

        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(getSession());
        manager.setForIdEntete(getIdEnteteFacture());
        manager.setForIdPassage(getIdPassage());
        manager.setForTriDecompte(getIdTriDecompte());
        manager.setForIdSousType(getIdSousType());
        manager.setFromIdExterneRole(getFromIdExterneRole());
        manager.setForTillIdExterneRole(getTillIdExterneRole());
        manager.setForNotModeImpression(FAEnteteFacture.CS_MODE_IMP_NOT_IMPRIMABLE);

        if (ModeFonctionnementEnum.INFOROM336 == getModeFonctionnement()) {
            manager.setConditionInfoRom336(giveConditionInfoRom336());
        }

        manager.setOrderBy(getIdTri());

        if (aImprimer.equals(FAImpressionFactureProcess.IMPRESSION_LETTRE)) {
            success = _createDocumentFromManager(manager);
        } else if (aImprimer.equals(FAImpressionFactureProcess.FACTURES_A_IMPRIMER_NORMALES)) {
            imprimable = true;
            manager.setForNonImprimable(Boolean.FALSE); // imprimer seulement
            // les factures normales
            manager.setForNonImprimable2(new Integer(1));
            setFactureType("Factures normales");
            success = _createDocumentFromManager(manager);
        } else if (aImprimer.equals(FAImpressionFactureProcess.FACTURES_A_IMPRIMER_BLOQUEES)) {
            imprimable = false;
            manager.setForNonImprimable(Boolean.TRUE);
            manager.setForNonImprimable2(new Integer(2));
            setFactureType(getSession().getLabel("BVR_FILE_NAME_FACTURES_BLOQUEES"));
            success = _createDocumentFromManager(manager);
        } else // imprimer toutes les factures
        {
            imprimable = true;
            manager.setForNonImprimable(Boolean.FALSE); // imprimer seulement
            // les factures normales
            manager.setForNonImprimable2(new Integer(1));
            setFactureType(getSession().getLabel("BVR_FILE_NAME_FACTURES_NORMALES"));
            success = _createDocumentFromManager(manager);
            if ((success)
                    && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE))
                    && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES))) {
                imprimable = false;
                isNewDocument = true;
                manager.setForNonImprimable(Boolean.TRUE); // imprimer seulement
                // les factures
                // normales
                manager.setForNonImprimable2(new Integer(2));
                setFactureType(getSession().getLabel("BVR_FILE_NAME_FACTURES_BLOQUEES"));
                success = _createDocumentFromManager(manager);
            }
        }

        // statistiques des documents pas imprimer
        if (nbPasImprime > 0) {
            getMemoryLog().logMessage(
                    "\n" + getSession().getLabel("OBJEMAIL_FAPRINT_SEPARATOR") + "\n"
                            + getSession().getLabel("OBJEMAIL_FAPRINT_DOCPASIMPRIMER") + ": " + nbPasImprime,
                    globaz.framework.util.FWMessage.INFORMATION, "");
        }
        // sujet de l'email à envoyer
        String emailSubject = getSession().getLabel("OBJEMAIL_FAPRINT_IMPRESSSIONFACTURE");
        super.setEMailObject(emailSubject + "::(" + nbImprimer + ")::" + passage.getIdPassage() + "::"
                + passage.getDateFacturation());

        JadePublishDocumentInfo docinfo = createDocumentInfo();
        if (isAborted()) {
            docinfo.setPublishDocument(false);
            docinfo.setArchiveDocument(false);
        } else {
            docinfo.setPublishDocument(true);
            docinfo.setArchiveDocument(false);
        }
        try {
            int TAILLE_LOT = Integer.parseInt((getSession().getApplication()).getProperty("tailleLot", "0"));
            // attacher le document à l'email
            if (getImpressionDef().booleanValue()
                    || (getSession().getApplication()).getProperty("impressionDef").equals("true")) {
                docinfo.setFinalDocument(true);
            }
            boolean duplex = false;

            boolean separerDoc = Boolean.FALSE;

            if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE)) {
                docinfo.setDocumentType("globaz.musca.itext.FADetailInteretMoratoire_Doc");

                // Demande de service AGLAU pour code OMR
                // SCO 24.04.2013
                // On précise quand même le numéro inforom "0094CFA".
                // Ce numéro gouverne tous les autres pour les besoins de la publication
                // Et plus précisément le cas du renommage du fichier
                docinfo.setDocumentTypeNumber("0094CFA");
                docinfo.setDuplex(duplex);

            } else if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES)) {
                docinfo.setDocumentType("globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc");
                docinfo.setDocumentTypeNumber(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE);
            } else if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_IMPRESSION)) {
                docinfo.setDocumentType(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                docinfo.setDocumentTypeNumber(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);

                FAModulePassageManager modulePassageManager = new FAModulePassageManager();
                modulePassageManager.setSession(getSession());
                modulePassageManager.setForIdPassage(passage.getId());
                modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
                boolean hasPassageModuleFacturationDecisionCAP_CGAS = modulePassageManager.getCount() >= 1;

                modulePassageManager = new FAModulePassageManager();
                modulePassageManager.setSession(getSession());
                modulePassageManager.setForIdPassage(passage.getId());
                modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_CAP_CGAS);
                hasPassageModuleFacturationDecisionCAP_CGAS = hasPassageModuleFacturationDecisionCAP_CGAS
                        || (modulePassageManager.getCount() >= 1);

                if (!hasPassageModuleFacturationDecisionCAP_CGAS) {
                    duplex = (getSession().getApplication()).getProperty("gestionVerso").equals("avec");
                }

                separerDoc = getSeparateMultiSheetsValue();
                docinfo.setDuplex(duplex);

            } else if (getName().equals("FAPassageImprimerDecomptesViewBean")
                    || getName().equals("FAPassageImprimerDecomptesSansLSVRembViewBean")) {
                docinfo.setDocumentType(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                docinfo.setDocumentTypeNumber(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);

                FAModulePassageManager modulePassageManager = new FAModulePassageManager();
                modulePassageManager.setSession(getSession());
                modulePassageManager.setForIdPassage(passage.getId());
                modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
                boolean hasPassageModuleFacturationDecisionCAP_CGAS = modulePassageManager.getCount() >= 1;

                modulePassageManager = new FAModulePassageManager();
                modulePassageManager.setSession(getSession());
                modulePassageManager.setForIdPassage(passage.getId());
                modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_CAP_CGAS);
                hasPassageModuleFacturationDecisionCAP_CGAS = hasPassageModuleFacturationDecisionCAP_CGAS
                        || (modulePassageManager.getCount() >= 1);

                if (!hasPassageModuleFacturationDecisionCAP_CGAS) {
                    duplex = (getSession().getApplication()).getProperty("gestionVerso").equals("avec");
                }
                separerDoc = getSeparateMultiSheetsValue();
                docinfo.setDuplex(duplex);

            }
            if (!getEnvoyerGed().booleanValue()) {
                try {
                    FADocumentInfoHelper.fill(docinfo, passage.getIdPassage(), passage.getDateFacturation());
                    if (!(getSession().getApplication()).getProperty("mettreGed").equals("true")
                            || ((getSession().getApplication()).getProperty("mettreGed").equals("true") && !isUnificationProcess())
                            || ((getSession().getApplication()).getProperty("mettreGed").equals("true") && getCallEcran()
                                    .booleanValue())) {
                        if (duplex) {
                            this.mergePDF(docinfo, true, TAILLE_LOT, separerDoc, null, JadePdfUtil.DUPLEX_ALL_NOT_LAST);
                        } else {
                            this.mergePDF(docinfo, true, TAILLE_LOT, separerDoc, null, null);
                        }
                    } else {
                        if (duplex) {
                            this.mergePDF(docinfo, false, TAILLE_LOT, separerDoc, null, JadePdfUtil.DUPLEX_ALL_NOT_LAST);
                        } else {
                            this.mergePDF(docinfo, false, TAILLE_LOT, separerDoc, null, null);
                        }
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, this.getClass().getName());
                }
            }
        } catch (Exception f) {
            getMemoryLog().logMessage("Impossible de fusionner les documents: " + f.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }

        // permet l'affichage des données du processus
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_ENVOIEMAIL"));
        // zone email
        getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FAPRINT_ATTACHMENTINFO"),
                globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
        
        // EDI0001 - SKIRNIR - invoque the rest service
        // EDI0001 - SKIRNIR - invoque the rest service
        try {        	
			SkirnirUtils.buildDefaultClient(getSession()).publishFacturation(
					Long.valueOf(getIdPassage()), //
					"decompte_coti", //
					"0099CFA", //
					getIdSousType(), //
					aImprimer, //
					getFromIdExterneRole(), //
					getTillIdExterneRole(), //
					getIdTri(), //
					getEnvoyerGed(), //
					docinfo.getFinalDocument() //
				);
			
		} catch (Exception e) {
			JadeLogger.warn(this, "Exception encountered trying to invoke publish service: "+e);
		}

        return success;

    }

    private boolean getSeparateMultiSheetsValue() {
        try {
            return EFAProperties.FACTUARTION_MULTISHEET.getBooleanValue();
        } catch (PropertiesException ex) {
            JadeLogger.warn(this, "The property [" + EFAProperties.FACTUARTION_MULTISHEET.getPropertyName()
                    + "] is undefined!");
            return Boolean.FALSE;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // initialiser l'executionDate avec l'heure actuelle
        Date today = new Date();
        // l'assigner à la classe parente BProcess
        super._setExecutionDate(today);

        // prendre le passage en cours;
        passage = new FAPassage();
        passage.setIdPassage(getIdPassage());
        passage.setSession(getSession());
        try {
            passage.retrieve(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }

        // Vérifier si le passage a les critères de validité pour une impression
        if (!this._passageIsValid((IFAPassage) passage)) {
            abort();
            return false;
        }

        // initialiser le passage
        if (!_initializePassage(passage)) {
            abort();
            return false;
        }

        // au préalable, générer les modules associés (manuellement) au passage
        /*
         * if (!_passageModuleGenerate(passage)) { this.abort(); return false; };
         */

        // Exécuter l'impression
        // ----------------------------------------------------------------
        boolean estImprime = _executeImpressionProcess(passage);

        // finaliser le passage (le déverrouiller)
        // un passage comptabilisé ne change plus jamais d'état
        if (!FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passage.getStatus())) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setSession(getSession());
            try {
                passage.retrieve(getTransaction());
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, passage.getClass().getName());
            }
            if (!_finalizePassageSetState(passage, FAPassage.CS_ETAT_IMPRIME)) {
                abort();
                return false;
            }
            ;
            // s'il est comptabilise, le délocké
        } else {
            if (!_finalizePassage(passage)) {
                abort();
                return false;
            }
        }

        return estImprime;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.05.2003 11:20:23)
     * 
     * @return globaz.musca.api.IFAModuleImpression
     */
    @Override
    public IntModuleImpression _getInterfaceImpressionFromCache(String nomClasse) {

        if (interfaceImpressionContainer == null) {
            interfaceImpressionContainer = new java.util.HashMap();
        }

        if (interfaceImpressionContainer.containsKey(nomClasse) && !isNewDocument) {
            return (IntModuleImpression) interfaceImpressionContainer.get(nomClasse);
        } else {
            try {
                isNewDocument = false;
                Class<?> cl = Class.forName(nomClasse);
                //
                IntModuleImpression interface_moduleImpression = (IntModuleImpression) cl.newInstance();
                interfaceImpressionContainer.put(nomClasse, interface_moduleImpression);
                return interface_moduleImpression;
            } catch (Exception e) {
                // TBD avec des eroors labels
                return null;
            }
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 14:58:23)
     * 
     * @return globaz.musca.db.facturation.FAModuleImpression
     */
    @Override
    public IFAModuleImpression _getModuleImpressionByCritere(IFAPrintDoc intEnteteFacture) {

        // On utilise qu'un type de document, plus facile pour la maintenance
        // cette méthode devient obsolète, enteteFacture n'est plus prise en
        // compte
        // voir FAImpressionFacture_DS
        String critereDecompte = FAModuleImpression.CS_DECOMPTE_POSITIF;
        String modeRecouvrement = FAModuleImpression.CS_BVR;

        // Rechercher dans le cache le module d'impression
        // -----------------------------------------------------------------------
        if (modImpMap == null) {
            modImpMap = new java.util.HashMap();
        }
        String key = critereDecompte + modeRecouvrement;
        if (modImpMap.containsKey(key)) {
            return (FAModuleImpression) modImpMap.get(key);
            // le rechercher dans la DB... système de secours
        } else {
            FAModuleImpressionManager moduleManager = new FAModuleImpressionManager();
            moduleManager.setSession(getSession());
            moduleManager.setForIdCritereDecompte(critereDecompte);
            moduleManager.setForIdModeRecouvrement(modeRecouvrement);
            try {
                moduleManager.find();
                FAModuleImpression module = null;
                // prendre le premier qui vient... c'est pas correct mais ça
                // marche
                for (int i = 0; i < moduleManager.size(); i++) {
                    module = new FAModuleImpression();
                    module = (FAModuleImpression) moduleManager.getEntity(i);
                    if (!module.isNew()) { // le placer dans le cache
                        modImpMap.put(module.getIdCritereDecompte() + module.getIdModeRecouvrement(), module);
                        return module;
                    } else {
                        return null;
                    }
                }
                return module;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.05.2003 11:02:21)
     * 
     * @return boolean
     * @param passage
     *            globaz.musca.db.facturation.FAPassage
     */
    @Override
    public boolean _passageIsValid(IFAPassage p) { // si le passage n'existe
        // pas, arrêter le process
        FAPassage passage = (FAPassage) p;

        if ((passage == null) || passage.isNew()) {
            return false;
        } // quitter si l'état du passage est ouvert
        if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {

            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_PRINTNONCOMPTA") + " : passage: " + getIdPassage(),
                    FWMessage.INFORMATION, this.getClass().getName());
            return false;
        } // quitter si le passage est déjà verrouillé
        if (passage.isEstVerrouille().booleanValue()) {
            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_ISVERROUILLE_INFO") + " : passage: " + getIdPassage(),
                    FWMessage.INFORMATION, this.getClass().getName());
            return false;
        } else {
            return true;
        }
    }

    /**
     * Générer les modules associés au passage. Si l'action d'un des modules est GENERER, effectuer la génération. Date
     * de création : (20.05.2003 11:12:27)
     * 
     * @return boolean
     * @param passage
     *            globaz.musca.db.facturation.FAPassage
     */
    @Override
    public boolean _passageModuleGenerate(IFAPassage p) {
        FAPassage passage = (FAPassage) p;

        boolean successful = false;
        FAModulePassageManager modPassManager = new FAModulePassageManager();
        modPassManager.setSession(getSession());
        modPassManager.setForIdPassage(getIdPassage());
        BStatement statement = null;
        try {
            FAModulePassage modulePassage = new FAModulePassage();
            statement = modPassManager.cursorOpen(getTransaction());
            // itérer sur tous les modules liés au passage
            while ((modulePassage = (FAModulePassage) modPassManager.cursorReadNext(statement)) != null) {
                // Le nom de la classe d'implémentation depuis la BD
                String nomClasse = modulePassage.getNomClasse();
                // si l'action du module est vide (avant: générer) et que la
                // classe existe, executer le module
                if (FAModulePassage.CS_ACTION_VIDE.equalsIgnoreCase(modulePassage.getIdAction())
                        && !globaz.jade.client.util.JadeStringUtil.isBlank(nomClasse)) {
                    try {
                        // effacer les afacts
                        _deleteAfactDuModule(null, modulePassage);
                        // Instancier une classe anonyme avec son nom
                        Class<?> cl = Class.forName(nomClasse);
                        IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
                        // générer
                        successful = interface_modulefacturation.generer(passage, this,
                                modulePassage.getIdModuleFacturation());
                    } catch (Exception e) {
                        getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                    }
                }

            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
        } finally { // fermer le cursor
            if (statement != null) {
                try {
                    modPassManager.cursorClose(statement);
                    statement = null;
                } catch (Exception e) {
                    if (statement != null) {
                        statement.closeStatement();
                    }
                    getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                }
            }
            return successful;
        }
    }

    @Override
    public void _setInterfaceImpressionFromCache(String nomClasse, IntModuleImpression module) {
        if (interfaceImpressionContainer == null) {
            interfaceImpressionContainer = new java.util.HashMap();
        }
        interfaceImpressionContainer.put(nomClasse, module);
    }

    protected void executeRemboursementLSV() {

        // Relance les processus de remboursement et de recouvrement
        // Ne tiendra pas compte des entêtes qui sont en mode retenu
        String errorMessage1 = "";

        FAPassageRemboursementProcess process = instanceForPassageRemboursementProcess();
        errorMessage1 = process.updateEnteteForRemboursement(passage, getSession(), getTransaction(),
                getFromIdExterneRole(), getTillIdExterneRole());

        if (!JadeStringUtil.isEmpty(errorMessage1)) {
            getMemoryLog().logMessage(errorMessage1, FWMessage.ERREUR, "");
        }
        // System.out.println("oca>> start FAImpressionFactureProcess.updateEnteteForRecouvrement" + new Date());
        String errorMessage2 = new FAPassageRecouvrementProcess().updateEnteteForRecouvrement(passage, getSession(),
                getTransaction(), getFromIdExterneRole(), getTillIdExterneRole());
        if (!JadeStringUtil.isEmpty(errorMessage2)) {
            getMemoryLog().logMessage(errorMessage2, FWMessage.ERREUR, "");
        }
        // System.out.println("oca>> end FAImpressionFactureProcess.updateEnteteForRecouvrement" + new Date());

    }

    /**
     * @return
     */
    @Override
    public java.lang.String getAImprimer() {
        return aImprimer;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.06.2002 07:42:07)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDateImpression() {
        return dateImpression;
    }

    /**
     * Returns the documentType.
     * 
     * @return String
     */
    @Override
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @return
     */
    @Override
    public String getFactureType() {
        return titreDocument;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2003 13:41:58)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * @return
     */
    public String getIdPersRef() {
        return idPersRef;
    }

    /**
     * @return
     */
    public String getIdSign1() {
        return idSign1;
    }

    /**
     * @return
     */
    public String getIdSign2() {
        return idSign2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:25:37)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdSousType() {
        return idSousType;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:25:47)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdTri() {
        return idTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:25:47)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdTriDecompte() {
        return idTriDecompte;
    }

    /**
     * Returns the impressionClassName.
     * 
     * @return String
     */
    @Override
    public String getImpressionClassName() {
        return ImpressionClassName;
    }

    public Boolean getImpressionDef() {
        return impressionDef;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.05.2003 11:25:09)
     * 
     * @return java.util.HashMap
     */
    @Override
    public java.util.HashMap getInterfaceImpressionContainer() {
        return interfaceImpressionContainer;
    }

    public JadeUser getJadeUser() {
        return jadeUser;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:45:09)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLibelle() {
        return libelle;
    }

    public ModeFonctionnementEnum getModeFonctionnement() {
        return modeFonctionnement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 17:05:05)
     * 
     * @return globaz.musca.db.facturation.FAModuleImpressionManager
     */
    public globaz.musca.db.facturation.FAModuleImpressionManager getModImpManager() {
        return modImpManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 16:50:59)
     * 
     * @return java.util.HashMap
     */
    @Override
    public java.util.HashMap getModImpMap() {
        return modImpMap;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 14:17:54)
     * 
     * @return long
     */
    @Override
    public int getNbImprimer() {
        return nbImprimer;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.06.2002 16:48:19)
     * 
     * @return int
     */
    @Override
    public int getSizeManager() {
        return sizeManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2003 13:44:12)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     * @throws Exception
     */

    private String giveConditionInfoRom336() {

        StringBuffer theConditionInfoRom336 = new StringBuffer();

        try {

            String theSchemaDB = TIToolBox.getCollection();
            String tFAENTFP = theSchemaDB + "FAENTFP";
            String tFAAFACP = theSchemaDB + "FAAFACP";

            theConditionInfoRom336.append(" ( ");
            theConditionInfoRom336.append("FAENTFP.MODIMP <> " + FAEnteteFacture.CS_MODE_IMP_PASIMPZERO);

            StringBuffer theQuery = new StringBuffer();

            theQuery.append(" FROM ");
            theQuery.append(tFAENTFP + " INNER JOIN " + tFAAFACP);
            theQuery.append(" ON " + tFAENTFP + ".IDENTETEFACTURE = " + tFAAFACP + ".IDENTETEFACTURE");
            theQuery.append(" WHERE ");
            theQuery.append(tFAENTFP + ".MODIMP = " + FAEnteteFacture.CS_MODE_IMP_PASIMPZERO);
            theQuery.append(" AND ");
            theQuery.append(tFAAFACP + ".MONTANTFACTURE <> 0");
            theQuery.append(" AND ");
            theQuery.append(tFAENTFP + ".IDPASSAGE = " + getIdPassage());
            theQuery.append(" GROUP BY " + tFAENTFP + ".IDENTETEFACTURE");

            List<String> theResultList = TISQL.querySingleField(getSession(), tFAENTFP
                    + ".IDENTETEFACTURE IDENTETEFACTURE", theQuery.toString());

            if ((theResultList != null) && (theResultList.size() >= 1)) {
                StringBuffer InClause = new StringBuffer();
                InClause.append(" OR ");
                InClause.append("FAENTFP.IDENTETEFACTURE IN ( ");

                for (String elem : theResultList) {
                    InClause.append(elem);
                    InClause.append(",");
                }
                InClause = InClause.deleteCharAt(InClause.length() - 1);
                InClause.append(" ) ");
                theConditionInfoRom336.append(InClause.toString());
            }

            theConditionInfoRom336.append(" ) ");
        } catch (Exception e) {
            /**
             * FWMessage.INFORMATION
             * 
             * Car si le memoryLog est à l'état fatal ou erreur la classe appelante (FAPassageFacturationProcess) lève
             * une exception et abort le process
             * 
             * Or ici ce comportement n'est pas désiré
             * 
             * En effet, la conséquence d'une exception ici, est simplement l'impression des décomptes qui ont un mode
             * d'impression 'Pas d'impression lignes à zéro'. Il faut donc avertir l'utilisateur mais pas aborter le
             * process
             */
            getMemoryLog().logMessage("unable to give condition InfoRom336 due to : " + e.toString(),
                    FWMessage.INFORMATION, this.getClass().getName());
            return "";
        }

        return theConditionInfoRom336.toString();
    }

    // private boolean hasPassageModuleRemboursementInfoRom200(){
    private FAPassageRemboursementProcess instanceForPassageRemboursementProcess() {
        boolean hasPassageModuleRemboursementInfoRom200 = false; // par défaut,
        // c'est
        // l'ancien
        // module
        // qui prime
        String idModuleFacturation = ServicesFacturation.getIdModFacturationByTypeAndPassage(getSession(),
                getTransaction(), FAModuleFacturation.CS_MODULE_REMBOURSER, passage.getIdPassage());
        if (!JadeStringUtil.isBlankOrZero(idModuleFacturation)) {
            try {
                FAModuleFacturation moduleFactu = new FAModuleFacturation();
                moduleFactu.setSession(getSession());
                moduleFactu.setIdModuleFacturation(idModuleFacturation);
                moduleFactu.retrieve();
                if (!moduleFactu.isNew()) {
                    hasPassageModuleRemboursementInfoRom200 = FAImpressionFactureProcess.CLASSE_IMPLEMENTATION_REMBOURSEMENT_INFOROM200
                            .equalsIgnoreCase(moduleFactu.getNomClasse());
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.AVERTISSEMENT, this.getClass().getName());
            }
        }

        return (hasPassageModuleRemboursementInfoRom200) ? new FAInfoRom200PassageRemboursementProcess()
                : new FAPassageRemboursementProcess();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.06.2002 20:42:47)
     * 
     * @return boolean
     */
    @Override
    public boolean isFlagCodeMaj() {
        return flagCodeMaj;
    }

    /**
     * @return
     */
    public boolean isImprimable() {
        return imprimable;
    }

    public boolean isUnificationProcess() {
        return unificationProcess;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param string
     */
    @Override
    public void setAImprimer(java.lang.String string) {
        aImprimer = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.06.2002 07:42:07)
     * 
     * @param newDateImpression
     *            java.lang.String
     */
    @Override
    public void setDateImpression(java.lang.String newDateImpression) {
        dateImpression = newDateImpression;
    }

    /**
     * Sets the documentType.
     * 
     * @param documentType
     *            The documentType to set
     */
    @Override
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @param string
     */
    @Override
    public void setFactureType(String string) {
        titreDocument = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.06.2002 20:42:47)
     * 
     * @param newFlagCodeMaj
     *            boolean
     */
    @Override
    public void setFlagCodeMaj(boolean newFlagCodeMaj) {
        flagCodeMaj = newFlagCodeMaj;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2003 13:41:58)
     * 
     * @param newFromIdExterneRole
     *            java.lang.String
     */
    @Override
    public void setFromIdExterneRole(java.lang.String newFromIdExterneRole) {
        fromIdExterneRole = newFromIdExterneRole;
    }

    /**
     * @param string
     */
    public void setIdPersRef(String string) {
        idPersRef = string;
    }

    /**
     * @param string
     */
    public void setIdSign1(String string) {
        idSign1 = string;
    }

    /**
     * @param string
     */
    public void setIdSign2(String string) {
        idSign2 = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:25:37)
     * 
     * @param newIdSousType
     *            java.lang.String
     */
    @Override
    public void setIdSousType(java.lang.String newIdSousType) {
        idSousType = newIdSousType;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:25:47)
     * 
     * @param newIdTri
     *            java.lang.String
     */
    @Override
    public void setIdTri(java.lang.String newIdTri) {
        idTri = newIdTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:25:47)
     * 
     * @param newIdTriDecompte
     *            java.lang.String
     */
    @Override
    public void setIdTriDecompte(java.lang.String newIdTriDecompte) {
        idTriDecompte = newIdTriDecompte;
    }

    /**
     * Sets the impressionClassName.
     * 
     * @param impressionClassName
     *            The impressionClassName to set
     */
    @Override
    public void setImpressionClassName(String impressionClassName) {
        ImpressionClassName = impressionClassName;
    }

    public void setImpressionDef(Boolean impressionDef) {
        this.impressionDef = impressionDef;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.05.2003 11:25:09)
     * 
     * @param newInterfaceImpressionContainer
     *            java.util.HashMap
     */
    @Override
    public void setInterfaceImpressionContainer(java.util.HashMap newInterfaceImpressionContainer) {
        interfaceImpressionContainer = newInterfaceImpressionContainer;
    }

    public void setJadeUser(JadeUser jadeUser) {
        this.jadeUser = jadeUser;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 13:45:09)
     * 
     * @param newLibelle
     *            java.lang.String
     */
    @Override
    public void setLibelle(java.lang.String newLibelle) {
        libelle = newLibelle;
    }

    public void setModeFonctionnement(ModeFonctionnementEnum modeFonctionnement) {
        this.modeFonctionnement = modeFonctionnement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 17:05:05)
     * 
     * @param newModImpManager
     *            globaz.musca.db.facturation.FAModuleImpressionManager
     */
    public void setModImpManager(globaz.musca.db.facturation.FAModuleImpressionManager newModImpManager) {
        modImpManager = newModImpManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 16:50:59)
     * 
     * @param newModImpMap
     *            java.util.HashMap
     */
    @Override
    public void setModImpMap(java.util.HashMap newModImpMap) {
        modImpMap = newModImpMap;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 14:17:54)
     * 
     * @param newNbImprimer
     *            long
     */
    @Override
    public void setNbImprimer(int newNbImprimer) {
        nbImprimer = newNbImprimer;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.06.2002 16:48:19)
     * 
     * @param newSizeManager
     *            int
     */
    @Override
    public void setSizeManager(int newSizeManager) {
        sizeManager = newSizeManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2003 13:44:12)
     * 
     * @param newTillIdExterneRole
     *            java.lang.String
     */
    @Override
    public void setTillIdExterneRole(java.lang.String newTillIdExterneRole) {
        tillIdExterneRole = newTillIdExterneRole;
    }

    public void setUnificationProcess(boolean unificationProcess) {
        this.unificationProcess = unificationProcess;
    }
}
