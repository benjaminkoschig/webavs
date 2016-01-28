package globaz.musca.process;

import globaz.docinfo.FADocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIContainer;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.pdf.JadePdfUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAModuleImpression;
import globaz.musca.api.IFAPassage;
import globaz.musca.api.IFAPrintDoc;
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
import globaz.musca.itext.FANewImpressionFacture_BVR_Doc;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (26.02.2002 14:53:33)
 * 
 * @author: Administrator
 */
public class FANewImpressionFactureProcess extends FAGenericProcess implements IFAImpressionFactureProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOCTYPE_FACTURE = "1";
    public static final String DOCTYPE_LETTER = "2";
    public final static String FACTURES_A_IMPRIMER_BLOQUEES = "bloquees";
    public final static String FACTURES_A_IMPRIMER_NORMALES = "normales";
    // private final static String WRAPPER_WORKINGDIR = "work";
    public final static String FACTURES_A_IMPRIMER_TOUTES = "toutes";
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
    public java.util.HashMap<String, IntModuleImpression> interfaceImpressionContainer = null;
    protected boolean isNewDocument = true;

    // Factures normales)
    // user pour r�f�rence
    JadeUser jadeUser = null;

    private java.lang.String libelle = new String();
    protected globaz.musca.db.facturation.FAModuleImpressionManager modImpManager;
    protected java.util.HashMap<String, FAModuleImpression> modImpMap;
    // Variable pour le comptage
    /* CHANGED */protected int nbImprimer = 0; // compteur d'ent�tes de facture
    // nombre d'ent�tes de facture que le manager contient
    protected int nbPasImprime = 0;
    private int shouldNbImprimer = 0;
    private int sizeManager = 0;

    private java.lang.String tillIdExterneRole = new String();
    // public static final int TAILLE_LOT =
    // (getSession().getApplication()).getProperty("mettreGed");
    private String titreDocument; // nom donn� au document (Factures bloqu�es,

    public boolean unificationProcess = false;

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FANewImpressionFactureProcess() throws Exception {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FANewImpressionFactureProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FANewImpressionFactureProcess(BSession session) throws Exception {
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
            // traitement des lignes trouv�es
            int progressCounter = 0;
            while (((myEntity = manager.cursorReadNext(statement)) != null) && (!myEntity.isNew())
                    && !super.isAborted()) {
                setProgressCounter(++progressCounter);
                if (isAborted()) {
                    break;
                } else {
                    // la prochaine facture � imprimer
                    // valeur dynamique du compteur du progress

                    // Instancier une classe d'impl�mentation avec son nom si
                    // non vide
                    if (!JadeStringUtil.isBlank(impressionClassName)) {

                        interface_moduleImpression = _getInterfaceImpressionFromCache(impressionClassName);

                        interface_moduleImpression.beginPrinting(passage, this);
                        interface_moduleImpression.add(myEntity);
                        // construit le document pour la prochaine facture avec
                        // l'ent�te suivante
                        nbImprimer++;

                    } else {
                        // il n'existe pas d'impl�mentation de classe
                        // d'impression pour ce type de
                        // d�copmpte
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
            // traitement des lignes trouv�es
            int progressCounter = manager.getCount();
            setProgressScaleValue(progressCounter);
            int cpt = 0;
            while (((entete = (FAEnteteFacture) manager.cursorReadNext(statement)) != null) && (!entete.isNew())) {
                cpt++;
                setProgressDescription(entete.getIdExterneRole() + " <br>" + cpt + "/" + progressCounter + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affili� : " + entete.getIdExterneRole()
                            + " <br>" + cpt + "/" + progressCounter + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affili� : " + entete.getIdExterneRole() + " <br>"
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
                        // la prochaine facture � imprimer
                        // valeur dynamique du compteur du progress
                        // this.setProgressCounter(++progressCounter);
                        FAModuleImpression moduleImpression = (FAModuleImpression) _getModuleImpressionByCritere(entete);
                        // imprimer la facture si le module d'impression
                        // correspond � ce type d'impression de facture
                        if (moduleImpression != null) {
                            String nomClasse = moduleImpression.getNomClasse();

                            // Instancier une classe d'impl�mentation avec son
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
                                // facture avec l'ent�te suivante
                                nbImprimer++;

                            } else {
                                // il n'existe pas d'impl�mentation de classe
                                // d'impression pour ce type de
                                // d�copmpte
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
        // le manager ne contient aucune ent�te de facture
        if (shouldNbImprimer == 0) {
            // getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FAPRINT_NOFACTURE"),
            // globaz.framework.util.FWMessage.INFORMATION, getClass().getName());
            return true;
        }

        // Entrer les informations pour l' �tat du process
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_CREATIONDOCUMENTS"));
        if (shouldNbImprimer > 0) {
            setProgressScaleValue(shouldNbImprimer);
        } else {
            setProgressScaleValue(1);
        }

        // interface_moduleImpression is of type FAImpressionFactureBVR
        IntModuleImpression interface_moduleImpression = null;

        // appel de la m�thode d'impression effective
        if (JadeStringUtil.isEmpty(getDocumentType()) || JadeStringUtil.isBlank(getDocumentType())) {
            success = this._createDocument(manager, interface_moduleImpression);
        } else if (FANewImpressionFactureProcess.DOCTYPE_LETTER.equalsIgnoreCase(getDocumentType())) {
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
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */

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
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                return false;
            }
        }

        // mettre le passage pass� en param en variable de classe
        this.setPassage(passage);
        try {
            if (!JadeStringUtil.isBlank(getPassage().getPersonneRef())) {
                JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                jadeUser = service.loadForVisa(getPassage().getPersonneRef());
            }
        } catch (Exception ex) {
            // impossible de r�cup�rer les informations de la personne de
            // contact
            jadeUser = null;
        }

        // instancier un nouveau cache
        modImpMap = new java.util.HashMap<String, FAModuleImpression>();

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
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
        }

        if (!globaz.globall.util.JAUtil.isDateEmpty(dateImpression)) {
        }
        if (!(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE))
                && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES))) {
            executeRemboursementLSV();
        }

        // �tat du process
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_RECHERCHEDONNEE"));

        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(getSession());
        manager.setForIdEntete(getIdEnteteFacture());
        manager.setForIdPassage(getIdPassage());
        manager.setForTriDecompte(getIdTriDecompte());
        manager.setForIdSousType(getIdSousType());
        manager.setFromIdExterneRole(getFromIdExterneRole());
        manager.setForTillIdExterneRole(getTillIdExterneRole());
        manager.setOrderBy(getIdTri());

        if (aImprimer.equals(FANewImpressionFactureProcess.FACTURES_A_IMPRIMER_NORMALES)) {
            imprimable = true;
            manager.setForNonImprimable(Boolean.FALSE); // imprimer seulement
            // les factures normales
            manager.setForNonImprimable2(new Integer(1));
            setFactureType("Factures normales");
            success = _createDocumentFromManager(manager);
        } else if (aImprimer.equals(FANewImpressionFactureProcess.FACTURES_A_IMPRIMER_BLOQUEES)) {
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
        // sujet de l'email � envoyer
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
            // attacher le document � l'email
            if (getImpressionDef().booleanValue()
                    || (getSession().getApplication()).getProperty("impressionDef").equals("true")) {
                docinfo.setFinalDocument(true);
            }
            boolean duplex = false;
            boolean separerDoc = false;
            if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE)) {
                docinfo.setDocumentType("globaz.musca.itext.FADetailInteretMoratoire_Doc");
                // docinfo.setDocumentTypeNumber("???");
                // impossible de d�terminer un num�ro pour cette classe car g�re
                // les num�ros suivants :
                // NUM_REF_INFOROM_IM_COT_ARR = "0094CFA";
                // NUM_REF_INFOROM_IM_PAI_TAR = "0095CFA";
                // NUM_REF_INFOROM_INT_REMUN = "0096CFA";
                // NUM_REF_INFOROM_IM_REM_TAR = "0128CFA";
                // NUM_REF_INFOROM_IM_25_POUR = "0129CFA";

            } else if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES)) {
                docinfo.setDocumentType("globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc");
                docinfo.setDocumentTypeNumber(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE);
            } else if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_IMPRESSION)) {
                docinfo.setDocumentType(FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                docinfo.setDocumentTypeNumber(FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                duplex = (getSession().getApplication()).getProperty("gestionVerso").equals("avec");
                docinfo.setDuplex(duplex);
                separerDoc = true;
            } else if (getName().equals("FAPassageImprimerDecomptesViewBean")
                    || getName().equals("FAPassageImprimerDecomptesSansLSVRembViewBean")) {
                docinfo.setDocumentType(FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                docinfo.setDocumentTypeNumber(FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                duplex = (getSession().getApplication()).getProperty("gestionVerso").equals("avec");
                docinfo.setDuplex(duplex);
                separerDoc = true;
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
                    getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
                }
            }
        } catch (Exception f) {
            getMemoryLog().logMessage("Impossible de fusionner les documents: " + f.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }

        // permet l'affichage des donn�es du processus
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_ENVOIEMAIL"));
        // zone email
        getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FAPRINT_ATTACHMENTINFO"),
                globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());

        return success;

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // initialiser l'executionDate avec l'heure actuelle
        Date today = new Date();
        // l'assigner � la classe parente BProcess
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

        // V�rifier si le passage a les crit�res de validit� pour une impression
        if (!this._passageIsValid((IFAPassage) passage)) {
            abort();
            return false;
        }

        // initialiser le passage
        if (!_initializePassage(passage)) {
            abort();
            return false;
        }

        // au pr�alable, g�n�rer les modules associ�s (manuellement) au passage
        /*
         * if (!_passageModuleGenerate(passage)) { this.abort(); return false; };
         */

        // Ex�cuter l'impression
        // ----------------------------------------------------------------
        boolean estImprime = _executeImpressionProcess(passage);

        // finaliser le passage (le d�verrouiller)
        // un passage comptabilis� ne change plus jamais d'�tat
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
            // s'il est comptabilise, le d�lock�
        } else {
            if (!_finalizePassage(passage)) {
                abort();
                return false;
            }
        }

        return estImprime;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.05.2003 11:20:23)
     * 
     * @return globaz.musca.api.IFAModuleImpression
     */
    @Override
    public IntModuleImpression _getInterfaceImpressionFromCache(String nomClasse) {

        if (interfaceImpressionContainer == null) {
            interfaceImpressionContainer = new HashMap<String, IntModuleImpression>();
        }

        if (interfaceImpressionContainer.containsKey(nomClasse) && !isNewDocument) {
            return interfaceImpressionContainer.get(nomClasse);
        } else {
            try {
                isNewDocument = false;
                Class cl = Class.forName(nomClasse);
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2003 14:58:23)
     * 
     * @return globaz.musca.db.facturation.FAModuleImpression
     */
    @Override
    public IFAModuleImpression _getModuleImpressionByCritere(IFAPrintDoc intEnteteFacture) {

        // On utilise qu'un type de document, plus facile pour la maintenance
        // cette m�thode devient obsol�te, enteteFacture n'est plus prise en
        // compte
        // voir FAImpressionFacture_DS
        String critereDecompte = FAModuleImpression.CS_DECOMPTE_POSITIF;
        String modeRecouvrement = FAModuleImpression.CS_BVR;

        // Rechercher dans le cache le module d'impression
        // -----------------------------------------------------------------------
        if (modImpMap == null) {
            modImpMap = new java.util.HashMap<String, FAModuleImpression>();
        }
        String key = critereDecompte + modeRecouvrement;
        if (modImpMap.containsKey(key)) {
            return modImpMap.get(key);
            // le rechercher dans la DB... syst�me de secours
        } else {
            FAModuleImpressionManager moduleManager = new FAModuleImpressionManager();
            moduleManager.setSession(getSession());
            moduleManager.setForIdCritereDecompte(critereDecompte);
            moduleManager.setForIdModeRecouvrement(modeRecouvrement);
            try {
                moduleManager.find();
                FAModuleImpression module = null;
                // prendre le premier qui vient... c'est pas correct mais �a
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.05.2003 11:02:21)
     * 
     * @return boolean
     * @param passage
     *            globaz.musca.db.facturation.FAPassage
     */
    @Override
    public boolean _passageIsValid(IFAPassage p) { // si le passage n'existe
        // pas, arr�ter le process
        FAPassage passage = (FAPassage) p;

        if ((passage == null) || passage.isNew()) {
            return false;
        } // quitter si l'�tat du passage est ouvert
        if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {

            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_PRINTNONCOMPTA") + " : passage: " + getIdPassage(),
                    FWMessage.INFORMATION, this.getClass().getName());
            return false;
        } // quitter si le passage est d�j� verrouill�
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
     * G�n�rer les modules associ�s au passage. Si l'action d'un des modules est GENERER, effectuer la g�n�ration. Date
     * de cr�ation : (20.05.2003 11:12:27)
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
            // it�rer sur tous les modules li�s au passage
            while ((modulePassage = (FAModulePassage) modPassManager.cursorReadNext(statement)) != null) {
                // Le nom de la classe d'impl�mentation depuis la BD
                String nomClasse = modulePassage.getNomClasse();
                // si l'action du module est vide (avant: g�n�rer) et que la
                // classe existe, executer le module
                if (FAModulePassage.CS_ACTION_VIDE.equalsIgnoreCase(modulePassage.getIdAction())
                        && !globaz.jade.client.util.JadeStringUtil.isBlank(nomClasse)) {
                    try {
                        // effacer les afacts
                        _deleteAfactDuModule(null, modulePassage);
                        // Instancier une classe anonyme avec son nom
                        Class<?> cl = Class.forName(nomClasse);
                        IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
                        // g�n�rer
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
            interfaceImpressionContainer = new HashMap<String, IntModuleImpression>();
        }
        interfaceImpressionContainer.put(nomClasse, module);
    }

    protected void executeRemboursementLSV() {

        // Relance les processus de remboursement et de recouvrement
        // Ne tiendra pas compte des ent�tes qui sont en mode retenu
        String errorMessage1 = "";

        FAPassageRemboursementProcess process = instanceForPassageRemboursementProcess();
        errorMessage1 = process.updateEnteteForRemboursement(passage, getSession(), getTransaction(),
                getFromIdExterneRole(), getTillIdExterneRole());

        if (!JadeStringUtil.isEmpty(errorMessage1)) {
            getMemoryLog().logMessage(errorMessage1, FWMessage.ERREUR, "");
        }

        String errorMessage2 = new FAPassageRecouvrementProcess().updateEnteteForRecouvrement(passage, getSession(),
                getTransaction(), getFromIdExterneRole(), getTillIdExterneRole());
        if (!JadeStringUtil.isEmpty(errorMessage2)) {
            getMemoryLog().logMessage(errorMessage2, FWMessage.ERREUR, "");
        }

    }

    /**
     * @return
     */
    @Override
    public java.lang.String getAImprimer() {
        return aImprimer;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.06.2002 07:42:07)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.05.2003 13:41:58)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:25:37)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdSousType() {
        return idSousType;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:25:47)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdTri() {
        return idTri;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:25:47)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.05.2003 11:25:09)
     * 
     * @return java.util.HashMap
     */
    @Override
    public java.util.HashMap<String, IntModuleImpression> getInterfaceImpressionContainer() {
        return interfaceImpressionContainer;
    }

    public JadeUser getJadeUser() {
        return jadeUser;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:45:09)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2003 17:05:05)
     * 
     * @return globaz.musca.db.facturation.FAModuleImpressionManager
     */
    public globaz.musca.db.facturation.FAModuleImpressionManager getModImpManager() {
        return modImpManager;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2003 16:50:59)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.06.2002 16:48:19)
     * 
     * @return int
     */
    @Override
    public int getSizeManager() {
        return sizeManager;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.05.2003 13:44:12)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    private FAPassageRemboursementProcess instanceForPassageRemboursementProcess() {
        boolean hasPassageModuleRemboursementInfoRom200 = false; // par d�faut,
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.06.2002 20:42:47)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.06.2002 07:42:07)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.06.2002 20:42:47)
     * 
     * @param newFlagCodeMaj
     *            boolean
     */
    @Override
    public void setFlagCodeMaj(boolean newFlagCodeMaj) {
        flagCodeMaj = newFlagCodeMaj;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.05.2003 13:41:58)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:25:37)
     * 
     * @param newIdSousType
     *            java.lang.String
     */
    @Override
    public void setIdSousType(java.lang.String newIdSousType) {
        idSousType = newIdSousType;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:25:47)
     * 
     * @param newIdTri
     *            java.lang.String
     */
    @Override
    public void setIdTri(java.lang.String newIdTri) {
        idTri = newIdTri;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:25:47)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.05.2003 11:25:09)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:45:09)
     * 
     * @param newLibelle
     *            java.lang.String
     */
    @Override
    public void setLibelle(java.lang.String newLibelle) {
        libelle = newLibelle;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2003 17:05:05)
     * 
     * @param newModImpManager
     *            globaz.musca.db.facturation.FAModuleImpressionManager
     */
    public void setModImpManager(globaz.musca.db.facturation.FAModuleImpressionManager newModImpManager) {
        modImpManager = newModImpManager;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2003 16:50:59)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.06.2002 16:48:19)
     * 
     * @param newSizeManager
     *            int
     */
    @Override
    public void setSizeManager(int newSizeManager) {
        sizeManager = newSizeManager;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.05.2003 13:44:12)
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
