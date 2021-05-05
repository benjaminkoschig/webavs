package globaz.musca.process;

import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.skirnir.client.SkirnirUtils;
import globaz.docinfo.FADocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIContainer;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.log.JadeLogger;
import globaz.jade.pdf.JadePdfUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.api.IFAPassage;
import globaz.musca.api.musca.FAImpressionFactureEBill;
import globaz.musca.api.musca.FAImpressionFactureEBillXml;
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.constantes.EFAProperties;
import globaz.musca.db.facturation.*;
import globaz.musca.external.IntModuleImpression;
import globaz.musca.itext.FAImpressionFacture_BVR_Doc;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osiris.ch.ebill.send.invoice.InvoiceEnvelope;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;


public class FAImpressionFactureEBillProcess extends FAImpressionFactureProcess {

    public Map<FAEnteteFacture, FAAfactManager> afactParEnteteFactureEBill = new LinkedHashMap<>();

    private final static String FACTURES_TYPE_EBILL = "eBill";
    private final static String FACTURES_TYPE_AUTOMATIQUE = "automatique";
    private final static String FACTURES_TYPE_PAPIER = "papier";

    private EBillSftpProcessor serviceFtp;
    private static final Logger LOGGER = LoggerFactory.getLogger(FAImpressionFactureEBillProcess.class);
    private boolean forcerImpression;
    private String typeFacture;
    private int facturePapier = 0;
    private int factureEBill = 0;

    /**
     * Lancement de l'impression en mode batch Date de création : (05.05.2003 15:53:19)
     *
     * @param args java.lang.String[]
     */
    public static void main(String[] args) {

        System.out
                .println("Utiliser FAImpressionFacture <user> <pwd> <[idPassage]> <dateImpression> <from> <to> <email> <y> pour JADE\n");
        FAImpressionFactureEBillProcess process = null;

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
            process = new FAImpressionFactureEBillProcess();
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


    public FAImpressionFactureEBillProcess() throws Exception {
        super();
    }

    public FAImpressionFactureEBillProcess(BProcess parent) throws Exception {
        super(parent);
    }

    public FAImpressionFactureEBillProcess(BSession session) throws Exception {
        super(session);
    }

    /**
     * Fermeture du service ftp.
     */
    private void closeServiceFtp() {
        if (serviceFtp != null) {
            serviceFtp.disconnectQuietly();
        }
    }

    /**
     * Initialisation du service ftp.
     */
    private void initServiceFtp() throws PropertiesException {
        if (serviceFtp == null) {
            serviceFtp = new EBillSftpProcessor();
        }
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

    @Override
    public boolean _createDocument(FAEnteteFactureManager manager, IntModuleImpression interface_moduleImpression) {

        BStatement statement = null;
        try {
            boolean isEBillActive = CAApplication.getApplicationOsiris().getCAParametres().isEbill(getSession());
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

                        // Garde en mémoire les entêtes/afact à envoyer sous forme de facture eBill en fin de processus
                        CACompteAnnexe compteAnnexe = getCompteAnnexe(entete, getSession(), getTransaction());
                        if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.geteBillAccountID()) && isEBillActive) {
                            afactParEnteteFactureEBill.put(entete, afact);
                        }

                        interface_moduleImpression = _getInterfaceImpressionFromCache(FAImpressionFactureEBill.class.getName());
                        interface_moduleImpression.beginPrinting(passage, this);
                        interface_moduleImpression.add(entete);
                        nbImprimer++;

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

    /**
     * Méthode permettant de créer la facture eBill ou le bulletin de soldes eBill,
     * de générer et remplir le fichier puis de l'envoyer sur le ftp.
     *
     * @param compteAnnexe            : le compte annexe
     * @param entete                  : l'entête de la facture
     * @param enteteReference         : l'entête de référence pour les bulletin de soldes (vide dans le cas d'une facture bvr)
     * @param montantBulletinSoldes   : contient le montant total de la factures (seulement rempli dans le case d'un bulletin de soldes)
     * @param lignesParPaireIdExterne : contient les lignes de bulletins de soldes (vide dans le cas d'une factures)
     * @param reference               : la référence BVR ou QR.
     * @param attachedDocument        : le fichier crée par l'impression classique à joindre en base64 dans le fichier eBill
     * @throws Exception
     */
    private void creerFichierEBill(CACompteAnnexe compteAnnexe, FAEnteteFacture entete, FAEnteteFacture enteteReference, String montantBulletinSoldes, List<Map> lignesParPaireIdExterne, String reference, JadePublishDocument attachedDocument) throws Exception {

        String billerId = CAApplication.getApplicationOsiris().getCAParametres().getEbillBillerId();

        // Génère et ajoute un transactionId à l'entête
        entete.addEBillTransactionID(getTransaction());
        entete.update();

        // met à jour le status eBill de la section
        updateSectionEtatEtTransactionID(compteAnnexe, entete.getIdExterneFacture(), entete.geteBillTransactionID());

        // Initialisation de l'objet à marshaller dans la facture eBill
        FAImpressionFactureEBillXml factureEBill = new FAImpressionFactureEBillXml();
        factureEBill.setEntete(entete);
        factureEBill.setEnteteReference(enteteReference);
        factureEBill.setPassage(passage);
        factureEBill.setReference(reference);
        factureEBill.setLignesParPaireIdExterne(lignesParPaireIdExterne);
        factureEBill.setBillerId(billerId);
        factureEBill.setSession(getSession());
        factureEBill.setMontantBulletinSoldes(montantBulletinSoldes);
        factureEBill.setAttachedDocument(attachedDocument);
        factureEBill.seteBillAccountID(compteAnnexe.geteBillAccountID());

        // Init de la facture eBill
        factureEBill.initFactureEBill();

        // Rempli l'objet qui sera marshallé dans la facture eBill
        InvoiceEnvelope content = factureEBill.createFileContent();

        // Creation du fichier XML
        String filename = billerId + "_" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYYYY) + "_" + entete.geteBillTransactionID() + ".xml";
        String localPath = Jade.getInstance().getPersistenceDir() + serviceFtp.getFolderOutName() + filename;
        File localFile = new File(localPath);
        LOGGER.info("Création du fichier xml eBill : " + localFile.getAbsoluteFile() + "...");

        // Marshall de l'objet dans le fichier XML
        factureEBill.marshallDansFichier(content, localFile);

        // Upload du fichier XML sur le sftp
        try (FileInputStream retrievedFile = new FileInputStream(localFile)) {
            serviceFtp.sendFile(retrievedFile, filename);
            LOGGER.info("Fichier eBill envoyé sur le ftp : " + localFile.getAbsoluteFile() + "...");
        }
    }

    /**
     * Mise à jour du statut eBill de la section et de son transactionID eBill
     * recherche avec un id de compte auxiliaire passe l'état à pending.
     *
     * @param compteAnnexe  le compte annexe utilisé pour trouver la section.
     * @param transactionId l'id de transaction lié au traitement.
     */
    private void updateSectionEtatEtTransactionID(final CACompteAnnexe compteAnnexe, final String idExterneFacture, final String transactionId) {
        try {
            // Récupération de la section
            CASectionManager manager = new CASectionManager();

            manager.setSession(getSession());
            manager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            manager.setForIdExterne(idExterneFacture);
            manager.find(BManager.SIZE_NOLIMIT);

            // Met à jour le statut eBill de la section
            if (manager.size() == 1) {
                CASection section = (CASection) manager.get(0);
                section.seteBillEtat(CATraitementEtatEBillEnum.NUMERO_ETAT_REJECTED_OR_PENDING);
                section.seteBillErreur("");
                section.seteBillTransactionID(transactionId);
                section.update();
            } else {
                getMemoryLog().logMessage("Impossible de trouver une section unique avec l'id externe : " + idExterneFacture, FWViewBeanInterface.WARNING, this.getClass().getName());
            }

        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de trouver une section avec l'id externe : " + idExterneFacture + " : " + e.getMessage(), FWViewBeanInterface.WARNING, this.getClass().getName());
        }
    }

    protected boolean _createDocumentFromManager(FAEnteteFactureManager manager) {
        boolean success;

        try {
            if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE)) {
                CAInteretMoratoireManager intMana = new CAInteretMoratoireManager();
                intMana.setSession(getSession());
                intMana.setForIdJournalFacturation(passage.getIdPassage());
                shouldNbImprimer = intMana.getCount(getTransaction());
            } else if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES) || passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES_EBILL)) {
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

        try {
            if (EFAProperties.USE_USER_SESSION_FOR_HEADER.getBooleanValue()) {
                setJadeUser(getSession().getUserInfo());
            }
        } catch (PropertiesException ex) {
            JadeLogger.warn(this, "The property [" + EFAProperties.USE_USER_SESSION_FOR_HEADER.getPropertyName()
                    + "] is undefined!");
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
        modImpMap = new HashMap();

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
                && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES_EBILL))
                && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_LETTRE_TAXE_CO2))) {
            executeRemboursementLSV(); // TODO ESVE TEMP REMOVE FOR SPEED
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
                    && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES)
                    && !(passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES_EBILL)))) {
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
                    FWMessage.INFORMATION, "");
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

            } else if (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES) || (passage.getModuleEnCours().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES_EBILL))) {
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

            // Effectue le traitement eBill pour les documents concernés et les envoient sur le ftp
            boolean isEBillActive = CAApplication.getApplicationOsiris().getCAParametres().isEbill(getSession());
            boolean impressionPapierUniquement = StringUtils.equals(FACTURES_TYPE_PAPIER, typeFacture);

            // On imprime les factures eBill si :
            //  - eBill est actif
            //  - l'impression papier pour un cas eBil n'est pas forcé
            //  - le mode impression papier uniquement n'est pas sélectionné
            if (isEBillActive && !forcerImpression && !impressionPapierUniquement) {
                try {
                    initServiceFtp();
                    traiterFacturesEBill();
                    traiterBulletinDeSoldesEBill();
                } catch (Exception exception) {
                    LOGGER.error("Impossible de créer les fichiers eBill : " + exception.getMessage(), exception);
                    getMemoryLog().logMessage(getSession().getLabel("BODEMAIL_EBILL_FAILED") + exception.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                } finally {
                    closeServiceFtp();
                }
            }

            if (impressionPapierUniquement) {
                removeEBillAttachedDocuments();
            }

            boolean factureEBillUniquement = StringUtils.equals(FACTURES_TYPE_EBILL, typeFacture);

            if (factureEBillUniquement) {
                removeAllAttachedDocuments();
            } else {
                facturePapier = getAttachedDocuments().size();
            }

            // on modifie l'objet du mail
            String objetMail = emailSubject + "::(" + nbImprimer + ")::" + passage.getIdPassage() + "::"
                    + passage.getDateFacturation();
            super.setEMailObject(objetMail);
            docinfo.setDocumentSubject(objetMail);

            // on ajoute les logs au docInfo sinon ils ne sont plus récupérer.
            getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_EBILL_FAPAPIER") + facturePapier,
                    FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_EBILL_FAELEC") + factureEBill,
                    FWMessage.INFORMATION, this.getClass().getName());
            docinfo.setDocumentNotes(getMemoryLog().getMessagesInString());

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
                FWMessage.INFORMATION, this.getClass().getName());

        // EDI0001 - SKIRNIR - invoque the rest service
        // EDI0001 - SKIRNIR - invoque the rest service
        try {
            SkirnirUtils.buildDefaultClient(getSession()).publishFacturation(Long.valueOf(getIdPassage()), //
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
            JadeLogger.warn(this, "Exception encountered trying to invoke publish service: " + e);
        }

        return success;

    }

    /**
     * Méthode permettant de retirer toutes les factures des documents à imprimer.
     */
    private void removeAllAttachedDocuments() {
        nbImprimer -= getAttachedDocuments().size();
        getAttachedDocuments().removeAll(getAttachedDocuments());
    }

    /**
     * Méthode permettant de retirer les factures eBill des documents à imprimer.
     */
    private void removeEBillAttachedDocuments() throws Exception {
        for (Map.Entry<FAEnteteFacture, FAAfactManager> entry : afactParEnteteFactureEBill.entrySet()) {

            FAEnteteFacture entete = entry.getKey();

            CACompteAnnexe compteAnnexe = getCompteAnnexe(entete, getSession(), getTransaction());
            if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.geteBillAccountID())) {
                removeAndReturnAttachedDocument(entete);
                nbImprimer--;
            }

        }

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
     * Méthode permettant de traiter les factures eBill
     * en attente d'être envoyé dans le processus actuel.
     */
    private void traiterFacturesEBill() throws Exception {
        Iterator<IntModuleImpression> doc = interfaceImpressionContainer.values().iterator();

        while (doc.hasNext()) {
            IntModuleImpression myMod = doc.next();

            if (myMod.get_document() instanceof FAImpressionFacture_BVR_Doc) {
                for (Map.Entry<PaireIdExterneEBill, List<Map>> lignesParPaireIdExterne : ((FAImpressionFacture_BVR_Doc) myMod.get_document()).getLignesParPaireIdExterne().entrySet()) {

                    Map.Entry<FAEnteteFacture, FAAfactManager> afactParEnteteFactureEBill = getAfactParEnteteFactureEBill(lignesParPaireIdExterne.getKey());
                    if (afactParEnteteFactureEBill != null) {
                        FAEnteteFacture entete = afactParEnteteFactureEBill.getKey();

                        String reference = getReference(myMod.get_document());
                        CACompteAnnexe compteAnnexe = getCompteAnnexe(entete, getSession(), getTransaction());
                        if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.geteBillAccountID())) {
                            JadePublishDocument attachedDocument = removeAndReturnAttachedDocument(entete);
                            creerFichierEBill(compteAnnexe, entete, null, null, lignesParPaireIdExterne.getValue(), reference, attachedDocument);
                            factureEBill++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Récupération de la référence dans le document.
     *
     * @param document : le document d'impression
     * @return le code référence.
     */
    private String getReference(FWIDocumentInterface document) {
        String reference;
        ReferenceQR qrFacture = null;
        ReferenceBVR bvrFacture = new ReferenceBVR();
        if (document instanceof CAImpressionBulletinsSoldes_Doc) {
            qrFacture = ((CAImpressionBulletinsSoldes_Doc) document).getQrFacture();
            bvrFacture = ((CAImpressionBulletinsSoldes_Doc) document).getBvr();
        } else if (document instanceof FAImpressionFacture_BVR_Doc) {
            qrFacture = ((FAImpressionFacture_BVR_Doc) document).getQrFacture();
            bvrFacture = ((FAImpressionFacture_BVR_Doc) document).getBvr();
        }
        if (Objects.nonNull(qrFacture)) {
            reference = qrFacture.getReference();
        } else {
            reference = bvrFacture.getLigneReference();
        }
        return reference;
    }

    /**
     * Méthode permettant de traiter les bulletins de soldes eBill
     * en attente d'être envoyé dans le processus actuel.
     */
    private void traiterBulletinDeSoldesEBill() throws Exception {
        Iterator<IntModuleImpression> doc = interfaceImpressionContainer.values().iterator();

        while (doc.hasNext()) {
            IntModuleImpression myMod = doc.next();

            if (myMod.get_document() instanceof CAImpressionBulletinsSoldes_Doc) {
                for (Map.Entry<PaireIdExterneEBill, List<Map>> lignesParPaireIdExterneEBill : ((CAImpressionBulletinsSoldes_Doc) myMod.get_document()).getLignesParPaireIdExterneEBill().entrySet()) {

                    FAEnteteFacture entete = getAfactParEnteteFactureEBill(lignesParPaireIdExterneEBill.getKey(), getIdPassage());
                    FAEnteteFacture enteteReference = getEnteteFactureReference(lignesParPaireIdExterneEBill.getKey());

                    CACompteAnnexe compteAnnexe = getCompteAnnexe(entete, getSession(), getTransaction());
                    CACompteAnnexe compteAnnexeReference = getCompteAnnexe(enteteReference, getSession(), getTransaction());
                    String reference = getReference(myMod.get_document());
                    if (compteAnnexe != null && compteAnnexeReference != null
                            && !JadeStringUtil.isBlankOrZero(compteAnnexe.geteBillAccountID())
                            && !JadeStringUtil.isBlankOrZero(compteAnnexeReference.geteBillAccountID())
                            && !JadeStringUtil.isBlankOrZero(enteteReference.geteBillTransactionID())) {
                        JadePublishDocument attachedDocument = removeAndReturnAttachedDocument(enteteReference);
                        creerFichierEBill(compteAnnexe, entete, enteteReference, lignesParPaireIdExterneEBill.getKey().getMontant(), lignesParPaireIdExterneEBill.getValue(), reference, attachedDocument);
                        factureEBill++;
                    }

                }
            }
        }
    }

    /**
     * Méthode permetant de rechercher le fichier généré durant l'impression
     * de le retourner pour être ajouter à la facture eBill et de le supprimer
     * de la listes de fichiers à merger dans l'impression actuelle
     *
     * @param enteteFacture : l'entete qui permet d'identifier le fichier à retourner
     * @return le fichier généré durant l'impression
     */
    private JadePublishDocument removeAndReturnAttachedDocument(FAEnteteFacture enteteFacture) {
        JadePublishDocument attachedDocument = null;
        Iterator<JadePublishDocument> iter = getAttachedDocuments().iterator();
        while (iter.hasNext()) {
            final JadePublishDocument doc = iter.next();
            if (enteteFacture.getIdExterneFacture().equals(doc.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get("osiris.section.idExterne"))
                    && enteteFacture.getIdExterneRole().equals(doc.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get("numero.role.formatte"))) {
                attachedDocument = doc;
                iter.remove();
                break;
            }
        }
        return attachedDocument;
    }

    /**
     * Méthode permetant de rechercher une entête eBill par le biais d'une paire d'idExterneRole et d'idExterneFacture
     * cette méthode est utilisé dans le processus d'impression des factures eBill.
     *
     * @param paireIdExterneEBill : pair d'idExterneRole et d'idExterneFacture
     * @return l'entête ou null
     */
    private Map.Entry<FAEnteteFacture, FAAfactManager> getAfactParEnteteFactureEBill(PaireIdExterneEBill paireIdExterneEBill) {
        for (Map.Entry<FAEnteteFacture, FAAfactManager> entry : afactParEnteteFactureEBill.entrySet()) {
            FAEnteteFacture enteteFacture = entry.getKey();
            if (paireIdExterneEBill.getIdExterneRole().equals(enteteFacture.getIdExterneRole()) && paireIdExterneEBill.getIdExterneFactureCompensation().equals(enteteFacture.getIdExterneFacture())) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Méthode permetant de rechercher une entête eBill par le biais d'une paire d'idExterneRole et d'idExterneFactureCompensation
     * cette méthode est utilisé dans le processus d'impression des bulletins de soldes eBill.
     *
     * @param paireIdExterneEBill : pair d'idExterneRole et d'idExterneFactureCompensation
     * @return l'entête ou null
     */
    private FAEnteteFacture getEnteteFactureReference(PaireIdExterneEBill paireIdExterneEBill) throws Exception {
        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(getSession());
        manager.setForIdExterneRole(paireIdExterneEBill.getIdExterneRole());
        manager.setForIdExterneFacture(paireIdExterneEBill.getIdExterneFactureCompensation());
        manager.find(BManager.SIZE_NOLIMIT);
        FAEnteteFacture entete = (FAEnteteFacture) manager.getFirstEntity();
        return entete;
    }

    /**
     * Méthode permetant de rechercher une entête eBill par le biais de l'idExterneFactureCompensation
     * cette méthode est utilisé dans le processus d'impression des bulletins de soldes eBill.
     *
     * @param paireIdExterneEBill : une combinaison d'idExterneRole et d'idExterneFactureCompensation
     * @param idPassage           : l'id de passage
     * @return l'entête
     */
    private FAEnteteFacture getAfactParEnteteFactureEBill(PaireIdExterneEBill paireIdExterneEBill, String idPassage) throws Exception {
        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(getSession());
        manager.setForIdExterneRole(paireIdExterneEBill.getIdExterneRole());
        manager.setForIdPassage(idPassage);
        manager.find(BManager.SIZE_NOLIMIT);
        FAEnteteFacture entete = (FAEnteteFacture) manager.getFirstEntity();
        return entete;
    }

    public boolean getForcerImpression() {
        return forcerImpression;
    }

    public void setForcerImpression(boolean forcerImpression) {
        this.forcerImpression = forcerImpression;
    }

    public String getTypeFacture() {
        return typeFacture;
    }

    public void setTypeFacture(String typeFacture) {
        this.typeFacture = typeFacture;
    }
}
