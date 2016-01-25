package globaz.draco.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSDecompteLtnBlob;
import globaz.draco.db.declaration.DSDecompteLtnBlobManager;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.draco.print.itext.DSDecompteImpotLtn_Doc;
import globaz.draco.util.DSDecompteLtnBlobUtils;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * @author BJO <h1>Permet de générer toutes les listes de "DSDecompteImpotLtn_Doc". Imprime 1 liste par canton
 *         concernés.
 */
public class DSDecompteImpotLtnProcess extends BProcess {

    private static final long serialVersionUID = -2110793830324210417L;
    private String annee;// annee pour l'attestation fiscale
    // imprimer à cette date
    private String cantonSelectionne;
    // document
    private String dateImpression;// Pour ré-imprimer des attestations déjà
    private String dateValeur = JACalendar.todayJJsMMsAAAA();// date sur
    private ArrayList<DSDeclarationViewBean> declarationToTagMore = new ArrayList<DSDeclarationViewBean>();
    private ArrayList<DSDeclarationViewBean> declarationValable = new ArrayList<DSDeclarationViewBean>();
    private Hashtable inscriptionIndividuelleParCanton = new Hashtable();
    private boolean reImpression = false;
    private boolean simulation = false;
    private String typeImpression = "pdf";
    private static final int AJOUTE_33 = 1;

    public DSDecompteImpotLtnProcess() {
        super();
    }

    public DSDecompteImpotLtnProcess(BProcess parent) {
        super(parent);
    }

    public DSDecompteImpotLtnProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean success = true;
        try {
            // Vérification des paramètres
            if (JadeStringUtil.isBlank(getAnnee())) {
                getMemoryLog().logMessage(getSession().getLabel("CHAMPS_ANNEE_VIDE"), FWMessage.FATAL,
                        this.getClass().getName());
            }
            if (JAUtil.isDateEmpty(getDateValeur())) {
                getMemoryLog().logMessage(getSession().getLabel("CHAMPS_DATE_VALEUR_VIDE"), FWMessage.FATAL,
                        this.getClass().getName());
            }
            if (JadeStringUtil.isBlank(getEMailAddress())) {
                getMemoryLog().logMessage(getSession().getLabel("MSG_EMAILADDRESS_VIDE"), FWMessage.FATAL,
                        this.getClass().getName());
            }

            // Sortir en cas d'erreurs
            if (getMemoryLog().isOnFatalLevel()) {
                return false;
            }

            // DEBUT DU PROCESS
            // Récupération des déclarations de type LTN (122005)
            DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
            declarationManager.setSession(getSession());
            declarationManager.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN);
            declarationManager.setForAnnee(getAnnee());

            // si la case reimpression est cochée et que le champs date pour la réimpression est rempli on réimprime
            // les blobs correspondant
            if (getReImpression()) {
                // recherche des decompteLtnBlob
                DSDecompteLtnBlobManager decompteLtnBlobManager = new DSDecompteLtnBlobManager();
                decompteLtnBlobManager.setSession(getSession());
                decompteLtnBlobManager.setForDateImpression(dateImpression);
                decompteLtnBlobManager.setForAnnee(annee);
                decompteLtnBlobManager.find();
                if (decompteLtnBlobManager.size() > 0) {
                    for (int i = 0; i < decompteLtnBlobManager.size(); i++) {
                        DSDecompteLtnBlob decompteLtnBlob = (DSDecompteLtnBlob) decompteLtnBlobManager.getEntity(i);
                        // récupération du blob
                        byte[] bytes = (byte[]) DSDecompteLtnBlobUtils.readBlob(decompteLtnBlob.getIdBlob(),
                                getTransaction());
                        // création du fichier et envoi email
                        File file = ByteArrayToFile(
                                Jade.getInstance().getSharedDir() + "/DecompteLtn_"
                                        + JadeUUIDGenerator.createStringUUID() + "_" + i + "."
                                        + decompteLtnBlob.getFileExtension(), bytes);
                        JadePublishDocumentInfo docInfo = createDocumentInfo();
                        docInfo.setApplicationDomain("DRACO");
                        docInfo.setDocumentTitle("DecompteLTN");
                        docInfo.setDocumentTypeNumber(DSDecompteImpotLtn_Doc.NUMERO_REFERENCE_INFOROM);
                        docInfo.setDocumentSubject(getSession().getLabel("REIMP_DECOMPTE_IMPOTS_LTN_TITRE_MAIL") + " "
                                + decompteLtnBlob.getCanton());
                        docInfo.setDocumentProperty("annee", getAnnee());
                        docInfo.setPublishDocument(true);
                        docInfo.setArchiveDocument(false);
                        this.registerAttachedDocument(docInfo, file.getAbsolutePath());
                    }
                }
                // on sort du processus
                return true;
            } else {
                // pour n'imprimer que les décomptes jamais imprimer (en mode
                // non simulation)
                declarationManager.setForDateImpressionDecompteLtnIsNullOrZero(new Boolean(true));
            }

            declarationManager.find(BManager.SIZE_NOLIMIT);

            // parcourir le manager et insérer les déclarations dont le solde=0
            // dans le arraylist "declarationValable"
            for (int i = 0; i < declarationManager.size(); i++) {
                boolean valable = true;
                // récupération du compte annexe et vérification du solde des
                // cotisations
                try {
                    DSDeclarationViewBean declaration = (DSDeclarationViewBean) declarationManager.getEntity(i);
                    List<DSDeclarationViewBean> declarationComps = new ArrayList<DSDeclarationViewBean>();
                    BigDecimal sommeSolde = null;
                    BISession osirisSession = GlobazSystem.getApplication("OSIRIS").newSession(getSession());
                    // Récupérer le compte annexe
                    CACompteAnnexe compteAnnexe = new CACompteAnnexe();
                    compteAnnexe.setISession(osirisSession);
                    compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compteAnnexe.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                            getSession().getApplication()));
                    compteAnnexe.setIdExterneRole(declaration.getAffiliation().getAffilieNumero());
                    compteAnnexe.retrieve(getTransaction());
                    if (JadeStringUtil.isBlank(compteAnnexe.getIdCompteAnnexe())) {
                        // System.out.println("Compte annexe inexistant");
                        valable = false;
                    }

                    CASectionManager sectionManager = new CASectionManager();
                    sectionManager.setISession(osirisSession);
                    sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());

                    // on sélectionne les DS LTN et les DS LTN COMPLEMENTAIRE (les 2 doivent être soldées)
                    ArrayList<String> categorieList = new ArrayList<String>();
                    categorieList.add(APISection.ID_CATEGORIE_SECTION_LTN);
                    categorieList.add(APISection.ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE);
                    sectionManager.setForCategorieSectionIn(categorieList);

                    sectionManager.setForIdTypeSection(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
                    sectionManager.setLikeIdExterne(annee);
                    sectionManager.setForExcludeCode900Moratoire(Boolean.TRUE);
                    sectionManager.find(BManager.SIZE_NOLIMIT);

                    if (sectionManager.size() > 0) {
                        sommeSolde = sectionManager.getSum(CASection.FIELD_SOLDE);
                    }
                    if (sectionManager.size() > 1) {
                        // regarde si il existe un 38 pour cet affilié et cette année
                        DSDeclarationListViewBean declarationCompManager = new DSDeclarationListViewBean();
                        declarationCompManager.setSession(getSession());
                        declarationCompManager.setForAffiliationId(declaration.getAffiliationId());
                        declarationCompManager.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE);
                        declarationCompManager.setForAnnee(annee);
                        declarationCompManager.find();
                        // si il n'existe pas le même nombre de section (osiris) que le nombre de DS (draco) c'est qu'il
                        // y a une erreur => l'affilié n'est pas valable
                        if (sectionManager.size() != (declarationCompManager.size() + AJOUTE_33)) {
                            valable = false;
                        } else {
                            if (declarationCompManager.size() > 0) {
                                for (int k = 0; k < declarationCompManager.size(); k++) {
                                    DSDeclarationViewBean declarationComp = (DSDeclarationViewBean) declarationCompManager
                                            .getEntity(k);
                                    if (JadeStringUtil.isBlankOrZero(declarationComp.getDateImpressionDecompteImpots())) {
                                        declarationComps.add(declarationComp);
                                    }
                                }
                            }
                        }
                    }
                    if (sommeSolde != null) {
                        // Si le solde est positif la déclaration n'est pas valable
                        if (sommeSolde.doubleValue() != 0) {
                            valable = false;
                        }
                    }
                    // si la somme est null, la déclaration n'est pas en comptabilité (OSIRIS) => pas valable
                    if (sommeSolde == null) {
                        // System.out.println("la déclaration n'est pas comptabilisée");
                        valable = false;
                    }

                    // si la déclaration est valable, on la place dans le arrayList
                    if (valable) {
                        // si la déclaration principale avait été détaguée lors de la création d'un complément, c'est à
                        // dire qu'elle à la valeur 11.11.1111 => on ne veut pas que les inscriptions soient traitées à
                        // nouveau, par contre on veut quand même taguée si on est pas en mode simulation
                        if (!declaration.getDateImpressionDecompteImpots().equals("11.11.1111")) {
                            declarationValable.add(declaration);
                        } else {
                            declarationToTagMore.add(declaration);
                        }
                        // et également les 38 si il y en a
                        if (declarationComps.size() > 0) {
                            for (DSDeclarationViewBean decl : declarationComps) {
                                declarationValable.add(decl);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Parcourir les déclarations valables et trier les inscriptions
            // individuelle par canton dans un hashtable
            setProgressDescription("Préparation de l'impression");
            setProgressScaleValue(declarationValable.size());
            for (int i = 0; i < declarationValable.size(); i++) {
                if (!isAborted()) {
                    DSDeclarationViewBean declarationEnCours = declarationValable.get(i);
                    // récupération des inscriptions individuelle pour la
                    // déclaration en cours
                    try {
                        DSInscriptionsIndividuellesManager inscriptionIndManager = new DSInscriptionsIndividuellesManager();
                        inscriptionIndManager.setSession(getSession());
                        inscriptionIndManager.setForIdDeclaration(declarationEnCours.getIdDeclaration());
                        inscriptionIndManager.find(BManager.SIZE_NOLIMIT);

                        for (int j = 0; j < inscriptionIndManager.size(); j++) {
                            DSInscriptionsIndividuelles inscriptionIndividuelleEnCours = (DSInscriptionsIndividuelles) inscriptionIndManager
                                    .getEntity(j);

                            // récupération du tiers correspondant à
                            // l'inscritpion individuelle
                            String nssSalarie = inscriptionIndividuelleEnCours.getNumeroAvs().toString();
                            nssSalarie = NSUtil.formatAVSUnknown(nssSalarie);

                            TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
                            personneAvsManager.setSession(getSession());
                            personneAvsManager.setForNumAvsActuel(nssSalarie);
                            personneAvsManager.find();
                            TITiersViewBean tiers = (TITiersViewBean) personneAvsManager.getFirstEntity();
                            // si le tiers est null (si le nss a changé par
                            // exemple, on recherche dans l'historique)
                            if (tiers == null) {
                                System.out.println("tiers non trouvé -> recherche dans l'historique : " + nssSalarie);
                                TIHistoriqueAvsManager historiqueAVSManager = new TIHistoriqueAvsManager();
                                historiqueAVSManager.setSession(getSession());
                                historiqueAVSManager.setForNumAvs(nssSalarie);
                                historiqueAVSManager.find();
                                if (historiqueAVSManager.size() > 0) {
                                    TIHistoriqueAvs historiqueAvs = (TIHistoriqueAvs) historiqueAVSManager
                                            .getFirstEntity();
                                    tiers = new TITiersViewBean();
                                    tiers.setSession(getSession());
                                    tiers.setIdTiers(historiqueAvs.getIdTiers());
                                    tiers.retrieve();
                                }
                            }

                            if (tiers == null) {
                                System.out.println("tiers non trouvé -> erreur : " + nssSalarie);
                                getMemoryLog().logMessage(
                                        getSession().getLabel("FACTURATION_TIERS_NON_TROUVE") + nssSalarie,
                                        FWMessage.ERREUR, this.getClass().toString());
                                abort();
                            }

                            // récupération du canton de domicile du tiers
                            TIAvoirAdresse avoirAdresse = TITiers.findAvoirAdresse(
                                    IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                                    JACalendar.todayJJsMMsAAAA(), tiers.getIdTiers(), getSession());
                            if (avoirAdresse == null) {
                                System.out.println("avoir adresse null : " + nssSalarie);
                                getMemoryLog().logMessage(getSession().getLabel("ERROR_GETTING_ADRESSE") + nssSalarie,
                                        FWMessage.ERREUR, this.getClass().toString());
                                abort();
                            }
                            TILocaliteManager localiteManager = new TILocaliteManager();
                            localiteManager.setSession(getSession());
                            // BZ 8482 - Inclusion des localités inactives
                            localiteManager.setInclureInactif(true);
                            localiteManager.setForIdLocalite(avoirAdresse.getIdLocalite());
                            localiteManager.find();
                            TILocalite localiteTiers = (TILocalite) localiteManager.getFirstEntity();

                            // récupération de l'id du canton du tiers, qui va
                            // servir de clé dans le hashtable
                            // ATTENTION NE PAS RECUPERER LE CANTON DEPUIS TIERS
                            // MAIS DEPUIS TILOCALITE
                            String idCantonTiers = localiteTiers.getIdCanton();

                            if (!inscriptionIndividuelleParCanton.containsKey(idCantonTiers)) {
                                inscriptionIndividuelleParCanton.put(idCantonTiers, new ArrayList());
                            }

                            // récupération du arraylist correspondant au canton
                            // et insertion de l'inscription individuelle
                            ArrayList listInscriptionIndividuelle = (ArrayList) inscriptionIndividuelleParCanton
                                    .get(idCantonTiers);
                            listInscriptionIndividuelle.add(inscriptionIndividuelleEnCours);
                        }
                    } catch (Exception e) {
                        getMemoryLog().logMessage("Processus en erreur", FWMessage.ERREUR, this.getClass().toString());
                        // abort();
                    }
                } else {
                    return false;
                }
                incProgressCounter();
            }

            // si l'utilisateur n'as pas séléctionné de canton on imprime chaque
            // canton
            if (JadeStringUtil.isBlank(getCantonSelectionne())) {
                // parcourir le hastable et imprimer un décompte pour chaque
                // canton (en parcourant les clés)
                Enumeration enuma = inscriptionIndividuelleParCanton.keys();
                setProgressDescription("Impression");
                setProgressScaleValue(inscriptionIndividuelleParCanton.size());
                int incDoc = 0;
                while (enuma.hasMoreElements()) {
                    // récupération du canton en cours
                    String enumaElement = (String) enuma.nextElement();
                    String canton = getSession().getCodeLibelle(enumaElement) + " ("
                            + getSession().getCode(enumaElement) + ")";
                    String codeCanton = getSession().getCode(enumaElement);

                    // récupération de la liste d'inscription du canton en cours
                    ArrayList listInscriptionIndividuelle = (ArrayList) inscriptionIndividuelleParCanton
                            .get(enumaElement);
                    try {
                        // transmission des paramètres et création du document
                        DSDecompteImpotLtn_Doc document = new DSDecompteImpotLtn_Doc(getSession());
                        document.setSimulation(getSimulation());
                        document.setTypeImpression(getTypeImpression());
                        document.setEMailAddress(getEMailAddress());
                        document.setDateValeur(getDateValeur());
                        document.setAnnee(getAnnee());
                        document.setCanton(canton);
                        document.setListInscriptionIndividuelle(listInscriptionIndividuelle);
                        document.setParent(this);
                        document.executeProcess();
                        incProgressCounter();

                        // si pas en mode simulation, on met les fichiers générés en DB
                        if (!simulation) {
                            // récupération du document généré et enregistrement en DB (BLOB)
                            List attachedDocuments = getAttachedDocuments();
                            if ((attachedDocuments.size() > 0) && (attachedDocuments.get(incDoc) != null)) {
                                JadePublishDocument doc = (JadePublishDocument) attachedDocuments.get(incDoc);
                                String docLocation = doc.getDocumentLocation();
                                saveDocInDb(incDoc, codeCanton, docLocation, getTypeImpression());
                            } else {
                                throw new Exception("No attachedDocuments");
                            }
                        }
                        incDoc++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // si tout s'est bien passé, que simulation est décoché et que
                // l'utilsateur n'a pas choisi un canton particulier
                // on flag toutes les déclarations qui ont été traitées
                if (!simulation) {
                    try {
                        // tag les ds qui ont été traitées
                        for (int y = 0; y < declarationValable.size(); y++) {
                            DSDeclarationViewBean declaration = declarationValable.get(y);
                            System.out.println("déclaration tagé : " + declaration.getIdDeclaration());
                            declaration.setDateImpressionDecompteImpots(dateValeur);
                            declaration.wantCallValidate(false);
                            declaration.wantCallMethodBefore(false);
                            declaration.update(getTransaction());
                        }

                        // Tag également les ds qui avaient été éventuellement détaguée lors de la création d'un 38
                        for (int z = 0; z < declarationToTagMore.size(); z++) {
                            DSDeclarationViewBean declaration = declarationToTagMore.get(z);
                            System.out.println("déclaration tagé : " + declaration.getIdDeclaration());
                            declaration.setDateImpressionDecompteImpots(dateValeur);
                            declaration.wantCallValidate(false);
                            declaration.wantCallMethodBefore(false);
                            declaration.update(getTransaction());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // parcourir le hastable et imprimer un décompte QUE pour le
                // canton séléctionné
                Enumeration enuma = inscriptionIndividuelleParCanton.keys();
                while (enuma.hasMoreElements()) {
                    // récupération du canton en cours
                    String enumaElement = (String) enuma.nextElement();
                    String canton = getSession().getCodeLibelle(enumaElement) + " ("
                            + getSession().getCode(enumaElement) + ")";

                    // si le idcanton du canton séléctionné est égal au canton
                    // de la liste en cours
                    if (getCantonSelectionne().equalsIgnoreCase(enumaElement)) {
                        // récupération de la liste d'inscription du canton en
                        // cours
                        ArrayList listInscriptionIndividuelle = (ArrayList) inscriptionIndividuelleParCanton
                                .get(enumaElement);
                        try {
                            // transmission des paramètres et création du
                            // document
                            DSDecompteImpotLtn_Doc document = new DSDecompteImpotLtn_Doc(getSession());
                            document.setSimulation(getSimulation());
                            document.setTypeImpression(getTypeImpression());
                            document.setEMailAddress(getEMailAddress());
                            document.setDateValeur(getDateValeur());
                            document.setAnnee(getAnnee());
                            document.setCanton(canton);
                            document.setListInscriptionIndividuelle(listInscriptionIndividuelle);
                            document.setParent(this);
                            document.executeProcess();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            success = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            e.printStackTrace();
        }
        return success;
    }

    @Override
    protected void _validate() throws Exception {
        if (!getReImpression()) {
            if (JadeStringUtil.isBlank(getAnnee())) {
                this._addError(getTransaction(), getSession().getLabel("CHAMPS_ANNEE_VIDE"));
            }
            if (JAUtil.isDateEmpty(getDateValeur())) {
                this._addError(getTransaction(), getSession().getLabel("CHAMPS_DATE_VALEUR_VIDE"));
            }
            if (JadeStringUtil.isBlank(getEMailAddress())) {
                this._addError(getTransaction(), getSession().getLabel("MSG_EMAILADDRESS_VIDE"));
            }
            if (!JadeStringUtil.isBlank(getCantonSelectionne()) && !getSimulation()) {
                this._addError(getTransaction(), getSession().getLabel("MSG_PAS_SIMULATION_ET_CANTON_SELECTIONNE"));
            }
        } else {
            if (JadeStringUtil.isBlank(getAnnee())) {
                this._addError(getTransaction(), getSession().getLabel("CHAMPS_ANNEE_VIDE"));
            }
            if (JAUtil.isDateEmpty(getDateImpression())) {
                this._addError(getTransaction(), getSession().getLabel("CHAMPS_DATE_REIMPRESSION_VIDE"));
            }
            if (JadeStringUtil.isBlank(getEMailAddress())) {
                this._addError(getTransaction(), getSession().getLabel("MSG_EMAILADDRESS_VIDE"));
            }
        }

        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
            setSendMailOnError(true);
        }
    }

    /**
     * Convertie une tableau de byte en une instance de File et retourne le fichier
     * 
     * @param fileName
     * @param bytes
     * @return
     * @throws Exception
     */
    private File ByteArrayToFile(String fileName, byte[] bytes) throws Exception {
        FileOutputStream fileOutputStream = null;

        try {
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(bytes);
            return file;
        } catch (IOException e) {
            throw new Exception("Cannot write file", e);
        }
    }

    /**
     * Retourne un tableau de byte représentant un fichier
     * 
     * @param fileLocation
     * @return
     * @throws Exception
     */
    private byte[] fileToByteArray(String fileLocation) throws Exception {
        File file = new File(fileLocation);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[(int) file.length()];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                baos.write(buf, 0, readNum); // no doubt here is 0
                // System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            throw new Exception();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCantonSelectionne() {
        return cantonSelectionne;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("DECOMPTE_LTN_GEN_ECHOUE");
        } else {
            return getSession().getLabel("DECOMPTE_LTN_GEN_REUSSI");
        }
    }

    public boolean getReImpression() {
        return reImpression;
    }

    public boolean getSimulation() {
        return simulation;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Enregistre le document du décompte LTN en base de donnée (blob)
     * 
     * @param idDoc
     * @param codeCanton
     * @param docLocation
     * @throws Exception
     * @throws JadePersistenceException
     */
    private void saveDocInDb(int idDoc, String codeCanton, String docLocation, String fileExtension) throws Exception,
            JadePersistenceException {
        if (!JadeStringUtil.isBlank(docLocation)) {
            String idBlob = this.getClass().getName() + "_" + JadeUUIDGenerator.createStringUUID() + "_" + idDoc;
            byte[] bytes = fileToByteArray(docLocation);

            // Création du Blob (FWBLOB)
            DSDecompteLtnBlobUtils.addBlob(idBlob, bytes, getTransaction());

            // Création du décompteLtnBlob (lien entre le décompte et le blob)
            DSDecompteLtnBlob decompteLtnBlob = new DSDecompteLtnBlob();
            decompteLtnBlob.setSession(getSession());
            decompteLtnBlob.setIdBlob(idBlob);
            decompteLtnBlob.setDateImpression(getDateValeur());
            decompteLtnBlob.setCanton(codeCanton);
            decompteLtnBlob.setAnnee(getAnnee());
            // si c'est au format excel on utilise le format xml
            if (fileExtension.equals("xls")) {
                decompteLtnBlob.setFileExtension("xml");
            } else {
                decompteLtnBlob.setFileExtension(fileExtension);
            }
            decompteLtnBlob.add(getTransaction());
        } else {
            throw new Exception("docLocation is null!");
        }
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCantonSelectionne(String cantonSelectionne) {
        this.cantonSelectionne = cantonSelectionne;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setReImpression(boolean reImpression) {
        this.reImpression = reImpression;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
