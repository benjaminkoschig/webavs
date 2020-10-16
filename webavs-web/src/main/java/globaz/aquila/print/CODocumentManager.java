package globaz.aquila.print;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.util.GenerationQRCode;
import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.helper.ICOEtapeHelper;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.CO006ExecuterRetraitOpposition;
import globaz.aquila.db.access.batch.transition.CO007ExecuterDemandeMainlevee;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxFactory;
import globaz.aquila.db.access.poursuite.COExtraitCompteManager;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.cataloguetxt.COCatalogueTextesService;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.service.taxes.COTaxe;
import globaz.aquila.service.tiers.COTiersService;
import globaz.aquila.util.COStringUtils;
import globaz.babel.api.ICTListeTextes;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CATypeOperation;
import globaz.osiris.db.comptes.CATypeOperationManager;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.interets.CARubriqueSoumiseInteretManager;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import net.sf.jasperreports.engine.JRExporterParameter;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Classe abstraite parente de touts les documents du projet aquila.<br>
 * Centralise les fonctionalités communes aux documents.<br>
 * 19-aug-2004
 * 
 * @author Alexandre Cuva, sel
 */
public abstract class CODocumentManager extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public ReferenceQR qrFacture = null;

    /**
     * <H1>Description</H1>
     * 
     * @author vre
     */
    public static class COIDContentieux implements Serializable {

        // ~ Static fields/initializers
        // ---------------------------------------------------------------------------------

        private static final long serialVersionUID = 2411084533761928109L;

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String idContentieux = null;
        private String libSequence = null;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        public COIDContentieux() {

        }

        /**
         * Crée une nouvelle instance de la classe COIDContentieux.
         * 
         * @param contentieux
         *            DOCUMENT ME!
         */
        public COIDContentieux(COContentieux contentieux) {
            idContentieux = contentieux.getIdContentieux();
            libSequence = contentieux.getSequence().getLibSequence();
        }

        public COIDContentieux(String idContentieux, String libSequence) {
            this.idContentieux = idContentieux;
            this.libSequence = libSequence;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * getter pour l'attribut id contentieux.
         * 
         * @return la valeur courante de l'attribut id contentieux
         */
        public String getIdContentieux() {
            return idContentieux;
        }

        /**
         * getter pour l'attribut lib sequence.
         * 
         * @return la valeur courante de l'attribut lib sequence
         */
        public String getLibSequence() {
            return libSequence;
        }

        /**
         * setter pour l'attribut id contentieux.
         * 
         * @param string
         *            une nouvelle valeur pour cet attribut
         */
        public void setIdContentieux(String string) {
            idContentieux = string;
        }

        /**
         * setter pour l'attribut lib sequence.
         * 
         * @param string
         *            une nouvelle valeur pour cet attribut
         */
        public void setLibSequence(String string) {
            libSequence = string;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "[idContentieux: " + idContentieux + "; libSequence: " + libSequence + "]";
        }
    }

    /**
     * <H1>Description</H1>
     * <p>
     * une classe qui reprend les éléments importants d'une opération comptable sur une section mise au contentieux.
     * </p>
     * <p>
     * Cette class est quasiment un copier-coller ddes classes de génération de l'extrait de compte de Osiris. Elles ont
     * été recopiées car les informations concernant les intérêts ne sont pas disponibles dans les classes de Osiris et
     * que selon DDA, il n'est pas recommmandé de modifier les classes de Osiris.
     * </p>
     * 
     * @author vre
     */
    protected class COSituationCompteItem {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String description = null;
        private CAExtraitCompte extraitCompte = null;
        private FWCurrency montant = null;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private COSituationCompteItem(CAExtraitCompte extraitCompte) {
            this.extraitCompte = extraitCompte;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * getter pour l'attribut date operation.
         * 
         * @return la valeur courante de l'attribut date operation
         */
        public String getDateOperation() {
            return extraitCompte.getDate();
        }

        public CAExtraitCompte getExtraitCompte() {
            return extraitCompte;
        }

        /**
         * @see CAExtraitCompteManager#getDescription(CAExtraitCompte, BTransaction, String, boolean)
         */
        public String getDescription() {
            if (description == null) {
                description = getDescriptionExtraitCompte(extraitCompte);
            }

            return description;
        }

        /**
         */
        public FWCurrency getMontant() {
            if (montant == null) {
                if (extraitCompte.getIdTypeOperation().startsWith(APIOperation.CAECRITURE)
                        || extraitCompte.getIdTypeOperation().startsWith(APIOperation.CAAUXILIAIRE)) {
                    montant = new FWCurrency(extraitCompte.getMontant());
                } else {
                    montant = new FWCurrency();
                }
            }

            return montant;
        }

        /**
         * @return true si c'est un paiement (EP)
         */
        public boolean isCompensation() {
            return extraitCompte.getIdTypeOperation().equals(APIOperation.CAECRITURECOMPENSATION);
        }

        /**
         * @param csEtape
         * @return true si la ligne de taxe ne doit pas s'afficher sur le document
         */
        public boolean isLineBlocked(String csEtape) {
            return CORequisitionPoursuiteUtil.isLineBlocked(getTransaction(), csEtape, extraitCompte.getRubrique()
                    .getIdRubrique());
        }

        /**
         * getter pour l'attribut montant different zero.
         * 
         * @return la valeur courante de l'attribut montant different zero
         */
        public boolean isMontantDifferentZero() {
            return !getMontant().isZero();
        }

        /**
         * getter pour l'attribut montant negatif.
         * 
         * @return la valeur courante de l'attribut montant negatif
         */
        public boolean isMontantNegatif() {
            return getMontant().isNegative();
        }

        /**
         * retourne vrai si le montant est positif.
         * 
         * @return true si le montant est positif.
         */
        public boolean isMontantPositif() {
            return getMontant().isPositive();
        }

        /**
         * @return true si c'est un paiement (EP)
         */
        public boolean isPaiement() {
            return extraitCompte.getIdTypeOperation().equals(APIOperation.CAPAIEMENT)
                    || extraitCompte.getIdTypeOperation().equals(APIOperation.CAPAIEMENTBVR);
        }

        /**
         * @return true si c'est un paiement qui provient de l'OP
         */
        public boolean isPaiementOP() {
            return !JadeStringUtil.isIntegerEmpty(extraitCompte.getProvenancePmt());
        }

        /**
         * Si j'ai une rubrique et qu'elle fait partis de la liste des soumis à intérêt (cfr : compta.aux.)
         * 
         * @param session pour le manager des rubrique
         * @return si trouvé l'id rubrique de l'item courant dans la table des rubrique soumises à IM <br/>
         *         (sans session ou en erreur retourne faux)
         */
        public boolean isSoumisInteret(BSession session) {
            if (extraitCompte.getRubrique() != null) {
                CARubriqueSoumiseInteretManager rsiManager = new CARubriqueSoumiseInteretManager();
                rsiManager.setSession(session);
                rsiManager.setForIdRubrique(extraitCompte.getRubrique().getId());
                try {
                    if (rsiManager.getCount() == 1) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

    }

    /* Constantes utilisées pour la sommation et la décision indiquant s'il y a un verso. */
    protected static final String AVEC_VERSO = "avec";
    public static final String CODE_SYSTEM_INFOROM246_PAR = "5230001";
    public static final String CODE_SYSTEM_INFOROM246_PERS = "5230002";
    public static final int CONCERNE_NB_CHAR_PER_LINE = 70;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    protected static final String GESTION_VERSO_AQUILA = "gestionVersoAquila";
    protected static final String JASP_PROP_BODY_ADRESSE_CAISSE = "header.adresse.caisse.";
    protected static final String JASP_PROP_BODY_AVANCE_FRAIS = "body.avance.frais.";
    protected static final String JASP_PROP_BODY_COMPTE_CHEQUES_CAISSE = "body.compte.cheques.caisse.";

    public static final String JASP_PROP_BODY_CORPS_EN_ANNEXE_TEXTE = "body.en.annexe.texte.";
    public static final String JASP_PROP_BODY_CORPS_EN_COPIE_DEBITEUR = "body.en.copie.debiteur.";
    public static final String JASP_PROP_BODY_CORPS_EN_COPIE_OP = "body.en.copie.op.";
    public static final String JASP_PROP_BODY_CORPS_EN_COPIE_TEXTE = "body.en.copie.texte.";
    public static final String JASP_PROP_BODY_CORPS_TEXTE_SUSPENSION = "body.corps.texte.suspension.";
    public static final String JASP_PROP_BODY_FORMULE_DESTINATAIRE = "body.formule.destinataire.";
    protected static final String JASP_PROP_BODY_FORMULE_PRIVILEGE_CAISSE = "body.formule.privilege.caisse.";

    protected static final String JASP_PROP_BODY_NOM_ADRESSE_CAISSE = "body.nom.adresse.caisse.";
    protected static final String JASP_PROP_BODY_NOM_CAISSE = "body.nom.caisse.";

    public static final String JASP_PROP_BODY_NOM_ETAPE_RADIEE = "body.nom.etape.radiee.";
    protected static final String JASP_PROP_BODY_PARA = "body.para.";
    /** Template properties. */
    protected static final String JASP_PROP_BODY_POURCENT_INTERET_MORATOIRE = "body.pourcent.interet.moratoire.";

    protected static final String JASP_PROP_DOCUMENT_TITLE = "document.title.";
    protected static final String JASP_PROP_TITRE_PARA = "body.titre.";

    /** Seuil de l'exécution directe ou différée. */
    private static final int JOB_QUEUE_THRESHOLD = 10;

    private static Map TYPE_OP_CACHE;

    private COCatalogueTextesService catalogueTextesUtil = null;

    protected transient COContentieux curContentieux = null;
    private transient COTransition curTransition = null;
    protected String dateExecution = null;
    protected transient IntTiers destinataireDocument = null;
    protected String cantonOfficePoursuite = null;
    private boolean documentConfidentiel = false;
    private transient CODocumentManager firstDocument = null;

    protected COHistoriqueService historiqueService = null;
    // champs utilises lors de l'execution du document mais pas publies sur le serveur de job
    private transient Iterator idEntityIterator = null;
    // champs qui seront serialises
    private List idsContentieuxList = null;
    private String idTransition = null;
    private String langueDoc = null;
    private String listeAnnexes = null;
    private FWCurrency montantTotalDetail = null;
    private FWCurrency montantTotalPaiement = null;

    /** Est prévisionnel et n'existe pas encore au contentieux ! **/
    protected transient Boolean nouveauContentieux = null;
    private String numeroReferenceInforom = null;
    protected transient Boolean previsionnel = null;

    private boolean printCompletionDoc = true;
    private boolean printOutline = false;
    private List taxes = null;
    private COTiersService tiersService = null;

    private transient JadeUser userCollaborateur = null;
    private String userIdCollaborateur = null;
    protected String montantTotalIM = null;

    public void setMontantTotalIM(String montantTotalIM) {
        this.montantTotalIM = montantTotalIM;
    }
    
    public String getMontantTotalIM() {
        return montantTotalIM;
    }

    /**
     * Retourne le domaine d'adresse standard à utiliser par tous les documents.
     * <p>
     * Note: lors de la recherche d'une adresse, il faut utiliser une recherche sur tous les domaines (c'est-à-dire avec
     * le boolean {@link TITiers#getAdresseAsDataSource(String, String, String, boolean) hérité} à vrai) pour le cas où
     * l'adresse pour le domaine d'application retourné par cette méthode ne serait pas renseigné pour un tiers.
     * </p>
     * 
     * @return le domaine d'adresse standard à utiliser par tous les documents.
     */
    public static final String getIdApplicationAdresse() {
        return IConstantes.CS_APPLICATION_CONTENTIEUX;
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public FWCurrency getMontantTotalPaiement() {
        return montantTotalPaiement;
    }

    public void setMontantTotalPaiement(FWCurrency montantTotalPaiement) {
        this.montantTotalPaiement = montantTotalPaiement;
    }

    /**
     * Crée une nouvelle instance de la classe CODocumentManager.
     */
    public CODocumentManager() {
        super();
        catalogueTextesUtil = new COCatalogueTextesService();
        nouveauContentieux = Boolean.FALSE;
        historiqueService = COServiceLocator.getHistoriqueService();
        tiersService = COServiceLocator.getTiersService();
        montantTotalDetail = new FWCurrency();
    }

    /**
     * Crée une nouvelle instance de la classe CODocumentManager.
     * 
     * @param session
     * @throws FWIException
     */
    public CODocumentManager(BSession session) throws FWIException {
        this(session, FWIDocumentManager.DEFAULT_FILE_NAME);

    }

    /**
     * @param session
     * @param fileName
     *            : nom du document PDF
     * @throws FWIException
     */
    public CODocumentManager(BSession session, String fileName) throws FWIException {
        super(session, COApplication.APPLICATION_AQUILA_ROOT, fileName);
        catalogueTextesUtil = new COCatalogueTextesService();
        nouveauContentieux = Boolean.FALSE;
        historiqueService = COServiceLocator.getHistoriqueService();
        tiersService = COServiceLocator.getTiersService();
        montantTotalDetail = new FWCurrency();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#_createDocumentInfo()
     */
    @Override
    protected void _createDocumentInfo() {
        super._createDocumentInfo();

        super.getDocumentInfo().setTemplateName(getTemplateFile());
        super.getDocumentInfo().setDocumentTypeNumber(getNumeroReferenceInforom());
        // TODO sch 1 sept. 2010 : Voir si on peut supprimer les lignes suivantes
        if (firstDocument != null) {
            // Gestion du titre et du nom
            setDocumentTitle(firstDocument.getExporter().getExportFileName());
            getExporter().setExportFileName(firstDocument.getExporter().getExportFileName());
        }

        COApplication app = null;
        IFormatData affilieFormater = null;
        try {
            app = (COApplication) GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            affilieFormater = app.getAffileFormater();
        } catch (Exception e1) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE"));
        }
        try {
            // On rempli le documentInfo avec les infos du document
            TIDocumentInfoHelper.fill(getDocumentInfo(), curContentieux.getSection().getCompteAnnexe().getIdTiers(),
                    getSession(), curContentieux.getSection().getCompteAnnexe().getIdRole(), curContentieux
                            .getSection().getCompteAnnexe().getIdExterneRole(),
                    affilieFormater.unformat(curContentieux.getSection().getCompteAnnexe().getIdExterneRole()));
            CADocumentInfoHelper.fill(getDocumentInfo(), curContentieux.getSection());
            getDocumentInfo().setDocumentProperty("babel.type.id", "CTX");
            getDocumentInfo()
                    .setDocumentProperty("aquila.contentieux.idContentieux", curContentieux.getIdContentieux());
            getDocumentInfo().setDocumentProperty("aquila.contentieux.numero.poursuite",
                    curContentieux.getNumPoursuite());
            getDocumentInfo().setDocumentProperty("annee", getAnneeFromContentieux());
            getDocumentInfo().setDocumentProperty("document.date", getDateExecution());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * La langue du document.
     * 
     * @return La langue du document
     */
    protected String _getLangue() {
        return getLangue().toUpperCase();
    }

    /**
     * Retourne un String représentant la valeur de la propriété.
     * 
     * @param property
     *            Le nom de la propriété
     * @return La valeur de la propriété
     */
    protected String _getProperty(String property) {
        return this._getProperty(property, null);
    }

    /**
     * Retourne un String représentant la valeur de la propriété.
     * 
     * @param property
     *            Le nom de la propriété
     * @param additionalValue
     *            Une valeur additionnelle
     * @return La valeur de la propriété
     */
    protected String _getProperty(String property, String additionalValue) {
        String buffer = "";

        try {
            if (!JadeStringUtil.isEmpty(property)) {
                buffer = getTemplateProperty(getDocumentInfo(), property + _getLangue());
            }

            if (!JadeStringUtil.isEmpty(additionalValue)) {
                buffer += additionalValue;
            }
        } catch (NullPointerException npe) {
            buffer = "NULL";
        }

        return buffer;
    }

    /**
     * Gestion de l'en-tête/pied de page/signature.
     *
     * @param contentieux
     *            Le contentieux concerné par ce document
     * @param hasHeader
     *            <code>true</code> si le document contient un en-tête
     * @param hasFooter
     *            <code>true</code> si le document contient un pied de page
     * @param hasSignature
     *            <code>true</code> si le document contient une signature
     * @param adresse
     *            l'adresse du destinataire
     * @throws Exception
     *             En cas de problème
     */
    protected void _handleHeaders(COContentieux contentieux, boolean hasHeader, boolean hasFooter,
            boolean hasSignature, String adresse) throws Exception {
        String date = getDateExecution(); // Date d'exécution correspond à la date sur document.

        if (JadeStringUtil.isEmpty(date)) {
            date = JACalendar.todayJJsMMsAAAA();
        }

        this._handleHeaders(contentieux, hasHeader, hasFooter, hasSignature, adresse, date);
    }

    /**
     * @param contentieux
     *            Le contentieux concerné par ce document
     * @param hasHeader
     *            <code>true</code> si le document contient un en-tête
     * @param hasFooter
     *            <code>true</code> si le document contient un pied de page
     * @param hasSignature
     *            <code>true</code> si le document contient une signature
     * @param adresse
     *            l'adresse du destinataire
     * @param date
     *            la date sur le document
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected void _handleHeaders(COContentieux contentieux, boolean hasHeader, boolean hasFooter,
            boolean hasSignature, String adresse, String date) throws Exception {
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), _getLangue());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        // Bug 7823
        if (isDocumentConfidentiel()) {
            headerBean.setConfidentiel(true);
        }

        // Paramètres relatifs au collaborateur
        headerBean.setNomCollaborateur(getCollaborateur().getFirstname() + " " + getCollaborateur().getLastname());
        headerBean.setTelCollaborateur(getCollaborateur().getPhone());
        headerBean.setEmailCollaborateur(getCollaborateur().getEmail());
        headerBean.setUser(getCollaborateur());

        // Adresse du destinataire
        headerBean.setAdresse(adresse);

        // Date
        headerBean.setDate(formatDate(date));

        // Paramètres relatifs à l'affilié
        if (contentieux != null) {
            headerBean.setNoAffilie(contentieux.getCompteAnnexe().getIdExterneRole());
            headerBean.setNoSection(contentieux.getSection().getIdExterne());
            headerBean.setNoAvs("");

            // Renseignement du numéro ide
            AFIDEUtil.addNumeroIDEInDoc(getSession(), headerBean, contentieux.getCompteAnnexe().getIdTiers(),
                    contentieux.getCompteAnnexe().getIdExterneRole(), contentieux.getSection().getIdExterne(),
                    contentieux.getCompteAnnexe().getIdRole());
        }

        // de toutes facon (a cause de la resolution de variable dans le pavé signature...)
        // if (hasHeader) {
        caisseReportHelper.addHeaderParameters(getImporter(), headerBean);
        // }

        if (hasFooter) {
            caisseReportHelper.addFooterParameters(getImporter());
        }

        if (hasSignature) {
            caisseReportHelper.addSignatureParameters(getImporter());
        }
    }

    /**
     * Gestion de l'en-tête/pied de page/signature.
     * 
     * @param destinataire
     *            Le destinataire du document
     * @param contentieux
     *            Le contentieux concerné par ce document
     * @param hasHeader
     *            <code>true</code> si le document contient un en-tête
     * @param hasFooter
     *            <code>true</code> si le document contient un pied de page
     * @param hasSignature
     *            <code>true</code> si le document contient une signature
     * @throws Exception
     *             En cas de problème
     */
    protected void _handleHeaders(IntTiers destinataire, COContentieux contentieux, boolean hasHeader,
            boolean hasFooter, boolean hasSignature) throws Exception {
        this._handleHeaders(contentieux, hasHeader, hasFooter, hasSignature, getAdressePrincipaleEnvoiOP(destinataire));
    }

    /**
     * Spécifie la langue du document en fonction de la langue d'un tiers.
     * 
     * @param tiers
     *            Le tiers
     */
    protected void _setLangueFromTiers(IntTiers tiers) {
        if (tiers != null) {
            langueDoc = tiers.getLangueISO();

            // Renseigne la langue à utiliser pour le catalogue de textes en fonctions du tiers
            getCatalogueTextesUtil().setLangueDoc(tiers.getLangueISO());
        }
    }

    /**
     * Ajoute une entité à la liste.
     * 
     * @param contentieux
     *            L'entité
     * @return <code>true</code> si l'ajout s'est bien passé
     */
    public boolean addContentieux(COContentieux contentieux) {
        if (idsContentieuxList == null) {
            idsContentieuxList = new LinkedList();
        }

        return idsContentieuxList.add(new COIDContentieux(contentieux));
    }

    /**
     * Ajoute un groupe d'entités à la liste.
     * 
     * @param contentieuxList
     *            Le groupe d'entités
     * @return <code>true</code> si l'ajout s'est bien passé
     */
    public boolean addContentieux(Collection contentieuxList) {
        boolean retValue = true;

        for (Iterator contentieux = contentieuxList.iterator(); contentieux.hasNext();) {
            retValue = retValue && this.addContentieux((COContentieux) contentieux.next());
        }

        return retValue;
    }

    /**
     * Ajoute une entité à la liste pour un cas prévisionnel qui n'a pas encore de dossier au contentieux
     * 
     * @param contentieux
     *            L'entité
     * @return <code>true</code> si l'ajout s'est bien passé
     */
    public boolean addContentieuxPrevisionnel(COContentieux contentieux) {
        if (idsContentieuxList == null) {
            idsContentieuxList = new LinkedList();
        }

        return idsContentieuxList.add(contentieux);
    }

    /**
     * Place le séparateur à la fin
     * 
     * @param info
     * @param separator
     * @return
     */
    private String addInfo(String info, String separator) {
        if (!JadeStringUtil.isBlank(info)) {
            return info + separator;
        }
        return "";
    }

    /**
     * Ajoute les lignes pour les {@link #getTaxes() nouvelles taxes} à la source de donnée et retourne le total des
     * nouvelles taxes.
     * 
     * @param dataSource
     *            la source de données à modifier
     * @param fDesc
     *            le nom du champ du document dans lequel afficher la description de l'opération
     * @param fMontant
     *            le nom du champ du document dans lequel afficher le montant de l'opération
     * @param fDevise
     *            le nom du champ du document dans lequel afficher le libellé de la devise
     * @param devise
     *            le libellé de la devise
     * @return le montant total des nouvelles taxes
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected FWCurrency addTaxesToDS(List dataSource, String fDesc, String fMontant, String fDevise, String devise)
            throws Exception {
        FWCurrency totalNouvellesTaxes = null;

        if ((getTaxes() != null) && (getTaxes().size() > 0)) {
            for (Iterator taxeIter = getTaxes().iterator(); taxeIter.hasNext();) {
                COTaxe taxe = (COTaxe) taxeIter.next();
                HashMap fields = new HashMap();

                fields.put(fDesc, taxe.loadCalculTaxe(getSession()).getRubrique().getDescription(getLangue()));
                fields.put(fMontant, taxe.getMontantTaxe());
                fields.put(fDevise, devise);
                dataSource.add(fields);

                if (totalNouvellesTaxes == null) {
                    totalNouvellesTaxes = taxe.getMontantTaxeToCurrency();
                } else {
                    totalNouvellesTaxes.add(taxe.getMontantTaxeToCurrency());
                }
            }
        }

        return totalNouvellesTaxes;
    }

    /**
     * Après l'impression d'un document
     */
    @Override
    public void afterPrintDocument() {
        try {
            if (getParent() == null) {
                getDocumentInfo().setPublishDocument(true);
            } else {
                getDocumentInfo().setPublishDocument(false);
            }

            /*
             * Configuration dans JadePublishResourceLocator.xml Exemple de configuration :
             * 
             * <resource name="PdfPropertyPrevisionnel" archiving="false"
             * class="globaz.jade.publish.provider.pdfproperty.JadePublishResourceImpl">
             * <next.resource>MailWithXML</next.resource> <properties> <property name ="previsionnel">
             * <cm.from.left>10.0</cm.from.left> <cm.from.right>0.0</cm.from.right> <cm.from.bottom>0.5</cm.from.bottom>
             * <cm.from.top>0.0</cm.from.top> <font.name>Helvetica</font.name> <font.size>6</font.size>
             * <font.color>0,0,255</font.color> </property> </properties> </resource>
             */
            if (getPrevisionnel().booleanValue()) {
                getDocumentInfo().setArchiveDocument(false);
                getDocumentInfo().setDocumentProperty("previsionnel", "previsionnel");
            } else {
                // On dit qu'on veut archiver le document dans la GED
                getDocumentInfo().setArchiveDocument(true);
                getDocumentInfo().setDocumentProperty("previsionnel", "");
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    getSession().getLabel("GED_ERROR_GETTING_TIERS_INFO"));
        }
    }

    /**
     * Ne fait rien par défaut.
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * Initialise le catalogue de textes.
     * 
     * @throws FWIException
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            getCatalogueTextesUtil().setSession(getSession());
            getCatalogueTextesUtil().setTypeDocument(getTransition().getEtapeSuivante().getLibAction());

        } catch (Exception e) {
            throw new FWIException(e);
        }
    }

    @Override
    public void afterExecuteReport() {
        super.afterExecuteReport();
        try {
            GenerationQRCode.deleteQRCodeImage();
        } catch (IOException e) {
            this.log("Erreur lors de la suppression de l'image QR-Code : " + e.getMessage());
        }
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument()
     */
    @Override
    public boolean beforePrintDocument() {
        try {
            if (isPrintOutline()) {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_ALL);
            } else {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
            }

            return isPrintCompletionDoc() && super.beforePrintDocument();
        } catch (FWIException e) {
            this._addError(getSession().getCurrentThreadTransaction(), e.getMessage());
        }

        return super.beforePrintDocument();
    }

    /**
     * @param fromDate
     *            la date à partir de laquelle la situation de compte doit débuter
     * @param fDesc
     *            le nom du champ du document dans lequel afficher la description de l'opération
     * @param fMontant
     *            le nom du champ du document dans lequel afficher le montant de l'opération
     * @param fDevise
     *            le nom du champ du document dans lequel afficher le libellé de la devise
     * @param devise
     *            le libellé de la devise
     * @param csEtape
     *            concernée
     * @param idJournal
     *            du premier journal de la section
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected List<Map<String, String>> createSituationCompteDS(String fromDate, String fDesc, String fMontant,
            String fDevise, String devise, String csEtape, String idJournal) throws Exception {

        return createSituationCompteDSApresDateRP(fromDate, false, fDesc, fMontant, fDevise, devise, csEtape, idJournal);
    }

    /**
     * Methode prenant toutes les creances et va les assembler par nature rubrique et gardera toujours la date la plus
     * récente. Les compensations/paiements sont uniquement totalisés sur leurs montants.
     * 
     * @param fromDate
     *            la date à partir de laquelle la situation de compte doit débuter
     * @param fDesc
     *            le nom du champ du document dans lequel afficher la description de l'opération
     * @param fMontant
     *            le nom du champ du document dans lequel afficher le montant de l'opération
     * @param fDevise
     *            le nom du champ du document dans lequel afficher le libellé de la devise
     * @param devise
     *            le libellé de la devise
     * @param csEtape
     *            concernée
     * @param idJournal
     *            du premier journal de la section
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected LinkedList<Map<String, String>> createSituationCompteDSNouveauRegimePourRdP(String fromDate,
            String fDesc, String fMontant, String fDevise, String devise, String csEtape, String idJournal)
            throws Exception {
        FWCurrency totalPaiement = new FWCurrency(0);
        // [
        // KEY1;({KEY2,VALUE2}{KEY2,VALUE2}{KEY2,VALUE2}),
        // KEY1;({KEY2,VALUE2}{KEY2,VALUE2}{KEY2,VALUE2})
        // ]
        Map<String, Map<String, String>> lignes = new HashMap<String, Map<String, String>>();

        for (Iterator<COSituationCompteItem> operIter = situationCompte(fromDate, idJournal, csEtape).iterator(); operIter
                .hasNext();) {
            COSituationCompteItem item = operIter.next();

            if (!item.isLineBlocked(csEtape) && item.isMontantDifferentZero()) {
                if (item.isCompensation() == true || item.isPaiement() == true || item.isSoumisInteret(getSession())) {
                    totalPaiement.add(item.getMontant());
                } else {

                    // Chaque ligne sur le document est la concatenation de chaque nature rubrique
                    // Si la nature a deja été vu dans l extrait, on va assembler le montant, prendre la date la plus
                    // recente
                    if (lignes.containsKey(item.getExtraitCompte().getRubrique().getNatureRubrique())) {
                        Map<String, String> fields = lignes.get(item.getExtraitCompte().getRubrique()
                                .getNatureRubrique());

                        String date = fields.get(CAExtraitCompte.SECTIONDATE_FIELD);
                        String dateJournalItem = item.getExtraitCompte().getDate();

                        // Récupérer la date la plus récente
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateJournalItem, date) == true) {
                            fields.put(CAExtraitCompte.SECTIONDATE_FIELD, dateJournalItem);
                        }

                        FWCurrency montant = new FWCurrency(fields.get(fMontant));
                        montant.add(item.getMontant());
                        fields.put(fMontant, montant.toString());
                    } else {
                        HashMap<String, String> fields = new HashMap<String, String>();

                        fields.put(fDesc, item.getDescription());
                        fields.put(CAExtraitCompte.SECTIONDATE_FIELD, item.getExtraitCompte().getDate());
                        fields.put(fMontant, formatMontant(item.getMontant().toString()));
                        fields.put(fDevise, devise);
                        fields.put(CARubrique.FIELD_NATURERUBRIQUE, item.getExtraitCompte().getRubrique()
                                .getNatureRubrique());

                        // Ajout d'une ligne dans le document (Description, devise, montant)
                        lignes.put(item.getExtraitCompte().getRubrique().getNatureRubrique(), fields);
                    }
                    montantTotalDetail.add(item.getMontant());
                }
            }
        }

        setMontantTotalPaiement(totalPaiement);

        LinkedList<Map<String, String>> linked = new LinkedList<Map<String, String>>();
        linked.addAll(lignes.values());
        return linked;
    }

    /**
     * Methode prenant toutes les creances a partir d'une date si le booléen useDate est actif.
     * 
     * @param fromAfterDateExecutionRP
     * @param useDate
     * @param fDesc
     * @param fMontant
     * @param fDevise
     * @param devise
     * @param csEtape
     * @param idJournal
     * @return
     * @throws Exception
     */
    protected List<Map<String, String>> createSituationCompteDSApresDateRP(String fromAfterDateExecutionRP,
            boolean useDate, String fDesc, String fMontant, String fDevise, String devise, String csEtape,
            String idJournal) throws Exception {

        List<Map<String, String>> lignes = new LinkedList<Map<String, String>>();

        for (Iterator<COSituationCompteItem> operIter = situationCompteNouveauRegime(fromAfterDateExecutionRP, useDate,
                idJournal, csEtape).iterator(); operIter.hasNext();) {
            COSituationCompteItem item = operIter.next();

            if (!item.isLineBlocked(csEtape) && item.isMontantDifferentZero()) {
                HashMap<String, String> fields = new HashMap<String, String>();

                fields.put(fDesc, item.getDescription());
                fields.put(CAExtraitCompte.SECTIONDATE_FIELD, item.getExtraitCompte().getDate());
                fields.put(fMontant, formatMontant(item.getMontant().toString()));
                fields.put(fDevise, devise);
                fields.put(CARubrique.FIELD_NATURERUBRIQUE, item.getExtraitCompte().getRubrique().getNatureRubrique());

                // Ajout d'une ligne dans le document (Description, devise, montant)
                lignes.add(fields);
            }
            montantTotalDetail.add(item.getMontant());
        }

        return lignes;
    }

    /**
     * Methode prenant toutes les creances et va uniquement garder les créances ayant été comptabilisé jusqu'à une date
     * donnée (fromAfterDateExecutionRP)
     * 
     * @param fromDate
     * @param fromAfterDateExecutionRP
     * @param fDesc
     * @param fMontant
     * @param fDevise
     * @param devise
     * @param csEtape
     * @param idJournal
     * @return
     * @throws Exception
     */
    protected LinkedList<Map<String, String>> createSituationCompteDSNouveauRegimePourRCPetRV(String fromDate,
            String fromAfterDateExecutionRP, String fDesc, String fMontant, String fDevise, String devise,
            String csEtape, String idJournal) throws Exception {
        FWCurrency totalPaiement = new FWCurrency(0);
        // [
        // KEY1;({KEY2,VALUE2}{KEY2,VALUE2}{KEY2,VALUE2}),
        // KEY1;({KEY2,VALUE2}{KEY2,VALUE2}{KEY2,VALUE2})
        // ]
        Map<String, Map<String, String>> lignes = new HashMap<String, Map<String, String>>();

        for (Iterator<COSituationCompteItem> operIter = situationCompte(fromDate, idJournal, csEtape).iterator(); operIter
                .hasNext();) {
            COSituationCompteItem item = operIter.next();

            if (!item.isLineBlocked(csEtape) && item.isMontantDifferentZero()) {
                // On ne prend que les creances avant la date d'execution du RP
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), item.getDateOperation(),
                        fromAfterDateExecutionRP) == false) {

                    if (item.isCompensation() == true || item.isPaiement() == true) {
                        totalPaiement.add(item.getMontant());
                    } else {

                        // Chaque ligne sur le document est la concatenation de chaque nature rubrique
                        // Si la nature a deja été vu dans l extrait, on va assembler le montant, prendre la date la
                        // plus
                        // recente
                        if (lignes.containsKey(item.getExtraitCompte().getRubrique().getNatureRubrique())) {
                            Map<String, String> fields = lignes.get(item.getExtraitCompte().getRubrique()
                                    .getNatureRubrique());

                            String date = fields.get(CAExtraitCompte.SECTIONDATE_FIELD);
                            String dateJournalItem = item.getExtraitCompte().getDate();

                            // Récupérer la date la plus récente
                            if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateJournalItem, date) == true) {
                                fields.put(CAExtraitCompte.SECTIONDATE_FIELD, dateJournalItem);
                            }

                            FWCurrency montant = new FWCurrency(fields.get(fMontant));
                            montant.add(item.getMontant());
                            fields.put(fMontant, montant.toString());
                        } else {
                            HashMap<String, String> fields = new HashMap<String, String>();

                            fields.put(fDesc, item.getDescription());
                            fields.put(CAExtraitCompte.SECTIONDATE_FIELD, item.getExtraitCompte().getDate());
                            fields.put(fMontant, formatMontant(item.getMontant().toString()));
                            fields.put(fDevise, devise);
                            fields.put(CARubrique.FIELD_NATURERUBRIQUE, item.getExtraitCompte().getRubrique()
                                    .getNatureRubrique());

                            // Ajout d'une ligne dans le document (Description, devise, montant)
                            lignes.put(item.getExtraitCompte().getRubrique().getNatureRubrique(), fields);
                        }
                        montantTotalDetail.add(item.getMontant());
                    }
                }
            }
        }

        setMontantTotalPaiement(totalPaiement);

        LinkedList<Map<String, String>> linked = new LinkedList<Map<String, String>>();
        linked.addAll(lignes.values());
        return linked;
    }

    /**
     * Transforme une date au format jj.mm.aaaa en une date en toutes lettres en utilisant la langue du document.
     * 
     * @param date
     *            une date au format jj.mm.aaaa
     * @return une date au format java.text FULL (exemple 1er juiller 2005)
     */
    protected String formatDate(String date) {
        if (JadeStringUtil.isBlank(date)) {
            return "";
        }

        // rechercher un formatteur de date pour la langue du document
        return JACalendar.format(date, getLangue());
    }

    /**
     * remplace dans message {n} par args[n].
     * <p>
     * Evite que {@link MessageFormat} ne lance une erreur ou ne se comporte pas correctement si le message contient des
     * apostrophes.
     * </p>
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    protected String formatMessage(StringBuilder message, Object[] args) {
        return COStringUtils.formatMessage(message.toString(), args);
    }

    /**
     * Transforme un montant avec les paramètres :
     * <UL>
     * <LI><I>format : </I>#'###.##</LI>;
     * <LI><I>multiple : </I>0.05</LI>;
     * <LI><I>décimales :</I>2</LI>;
     * <LI><I>arrondi : </I>au plus proche</LI>.
     * </UL>
     * 
     * @return un String représentant le montant formattée.
     * @param montant
     *            montant à formatter.
     */
    protected String formatMontant(String montant) {
        if (JadeStringUtil.isBlank(montant)) {
            return "";
        }
        // rechercher un formatteur de date pour la langue du document
        return JANumberFormatter.format(montant);
    }

    /**
     * Si le domaine et le type d'adresse n'est pas défini dans la section, retourne l'adresse de courrier du tiers du
     * domaine par défaut.
     * 
     * @param tiers
     * @return String
     * @throws Exception
     */
    private String getAdresseCourrier(IntTiers tiers) throws Exception {
        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(getSession(), tiers);
        if (tiers == null) {
            return "";
        } else {
            return pyTiers.getAdresseAsString(getTypeAdresseCourrier(), getDomaineDefaut(), getDateExecution(),
                    curContentieux.getCompteAnnexe().getIdExterneRole());
        }
    }

    /**
     * Si le domaine et le type d'adresse n'est pas défini dans la section, retourne l'adresse de courrier du tiers du
     * domaine par défaut.
     * 
     * @param tiers
     * @return TIAdresseDataSource
     * @throws Exception
     */
    private TIAdresseDataSource getAdresseCourrierData(IntTiers tiers, String langue) throws Exception {
        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(getSession(), tiers);
        if (tiers == null) {
            return null;
        } else {
            return pyTiers.getAdresseAsDataSource(getTypeAdresseCourrier(), getDomaineDefaut(), curContentieux
                    .getCompteAnnexe().getIdExterneRole(), getDateExecution(), true, langue);
        }
    }

    /**
     * getter pour l'attribut adresse data source.
     * 
     * @param tiers
     * @return la valeur courante de l'attribut adresse data source
     * @throws Exception
     */
    protected TIAdresseDataSource getAdresseDataSourcePrincipal(IntTiers tiers) throws Exception {
        return this.getAdresseDataSourcePrincipal(tiers, null);
    }

    /**
     * getter pour l'attribut adresse data source.
     * 
     * @param tiers
     * @param langue
     * @return la valeur courante de l'attribut adresse data source
     * @throws Exception
     */
    protected TIAdresseDataSource getAdresseDataSourcePrincipal(IntTiers tiers, String langue) throws Exception {
        TIAdresseDataSource result = getAdresseCourrierData(tiers, langue);
        if (result != null) {
            return result;
        } else {
            try {
                return getAdresseDomicileData(tiers, langue);
            } catch (Exception e) {
                this.log("Impossible de trouver l'adresse de l'assuré: " + e.getMessage());
                return new TIAdresseDataSource();
            }
        }
    }

    /**
     * A partir de la RP, document à destination de l'OP ou du tribunal. <br>
     * Cascade des Adresses voulue :
     * <ol>
     * <li>Domaine : <b>contentieux</b>, Type : courrier</li>
     * <li>Domaine : standard, Type : <b>domicile</b></li>
     * <li>Domaine : standard, Type : <b>courrier</b></li>
     * </ol>
     * 
     * @param tiers
     * @return TIAdresseDataSource pour les documents à destination de l'OP ou du tribunal.
     * @throws Exception
     */
    protected TIAdresseDataSource getAdresseDataSourcePrincipalEnvoiOP(IntTiers tiers) throws Exception {
        if (!ICOEtapeHelper.isEtapePoursuite(getTransition().getEtapeSuivante().getIdEtape())) {
            return this.getAdresseDataSourcePrincipal(tiers);
        } else {
            TITiers pyTiers = tiersService.loadTiers(getSession(), tiers);
            TIAdresseDataSource adresse = null;

            // Adresse Contentieux
            adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_CONTENTIEUX, curContentieux.getCompteAnnexe().getIdExterneRole(),
                    getDateExecution(), false, null);
            if (adresse == null) {
                adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_CONTENTIEUX, getDateExecution(), false);
            }

            // Adresse Domicile
            if (adresse == null) {
                adresse = getAdresseDomicileData(tiers, null);
            }

            // Adresse de Courrier
            if (adresse == null) {
                // Controle une adresse de domaine Contentieux dont on à pas renseigné l'idExterne.
                adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_DEFAUT, curContentieux.getCompteAnnexe().getIdExterneRole(),
                        getDateExecution(), false, null);
            }
            if (adresse == null) {
                adresse = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_DEFAUT, getDateExecution(), false);
            }

            return adresse;
        }
    }

    /**
     * Retourne l'adresse de domicile du tiers
     * 
     * @param tiers
     * @return String
     * @throws Exception
     */
    private String getAdresseDomicile(IntTiers tiers) throws Exception {
        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(getSession(), tiers);
        if (tiers == null) {
            return "";
        } else {
            return pyTiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                    getDateExecution(), curContentieux.getCompteAnnexe().getIdExterneRole());
        }
    }

    /**
     * Retourne l'adresse de domicile du tiers
     * 
     * @param tiers
     * @return TIAdresseDataSource
     * @throws Exception
     */
    private TIAdresseDataSource getAdresseDomicileData(IntTiers tiers, String langue) throws Exception {
        // Récupérer le tiers
        TITiers pyTiers = tiersService.loadTiers(getSession(), tiers);
        if (tiers == null) {
            return null;
        } else {
            return pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, curContentieux.getCompteAnnexe().getIdExterneRole(),
                    getDateExecution(), true, langue);
        }
    }

    /**
     * @param affilie
     * @return l'adresse du tier sur une ligne avec comme séparateur ", ".
     * @throws Exception
     */
    protected String getAdresseInLine(IntTiers affilie) throws Exception {
        return this.getAdresseInLine(affilie, ", ");
    }

    /**
     * @param affilie
     * @param separator
     *            entre les éléments de l'adresse
     * @return l'adresse du tiers avec le séparateur définit.
     * @throws Exception
     */
    protected String getAdresseInLine(IntTiers affilie, String separator) throws Exception {
        StringBuilder adresse = new StringBuilder("");
        TIAdresseDataSource adresseDS = getAdresseDataSourcePrincipalEnvoiOP(affilie);

        adresse.append(addInfo(adresseDS.ligne1, " "));
        adresse.append(addInfo(adresseDS.ligne2, " "));
        adresse.append(addInfo(adresseDS.ligne3, " "));
        adresse.append(addInfo(adresseDS.ligne4, separator));
        adresse.append(addInfo(adresseDS.attention, separator));
        adresse.append(addInfo(adresseDS.rue, " "));
        adresse.append(addInfo(adresseDS.numeroRue, separator));
        adresse.append(addInfo(adresseDS.casePostale, separator));
        adresse.append(addInfo(adresseDS.localiteNpa, " "));
        adresse.append(addInfo(adresseDS.localiteNom, separator));

        // Suppression du dernier separator
        if (adresse.length() >= separator.length()) {
            adresse.setLength(adresse.length() - separator.length());
        }

        return adresse.toString();
    }

    /**
     * getter pour l'attribut adresse paiement string.
     * 
     * @param tiers
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut adresse paiement string
     * @throws Exception
     *             DOCUMENT ME!
     * @deprecated
     */
    @Deprecated
    protected String getAdressePaiementString(IntTiers tiers) throws Exception {
        try {
            return tiersService.getAdressePaiementString(getSession(), tiers, getDateExecution());
        } catch (Exception e) {
            e.printStackTrace();
            this.log("Impossible de trouver l'adresse de paiement: " + e.getMessage());

            return "";
        }
    }

    /**
     * Retourne l'adresse de courrier. Si aucune adresse de courrier n'est définit, retourne l'adresse de domicile.
     * 
     * @param tiers
     * @return String
     * @throws Exception
     */
    public String getAdressePrincipale(IntTiers tiers) throws Exception {
        // l'adresse de paiement est l'adresse de courrier
        String result = getAdresseCourrier(tiers);
        if (!JadeStringUtil.isBlank(result)) {
            return result;
        } else {
            return getAdresseDomicile(tiers);
        }
    }


    /**
     * Retourne l'adresse de courrier. Si aucune adresse de courrier n'est définit, retourne l'adresse de domicile.
     *
     * @param tiers
     * @return TIAdresseDataSource
     * @throws Exception
     */
    public TIAdresseDataSource getAdressePrincipaleAsData(IntTiers tiers) throws Exception {
        return getAdresseDataSourcePrincipal(tiers, tiers.getLangueISO());
    }

    /**
     * Retourne l'adresse principale si c'est une étape avant la RP. <br/>
     * sinon retourne l'adresse contentieux si elle est définie<br/>
     * sinon retourne l'adresse de domicile<br/>
     * sinon retourne l'adresse de courrier
     * 
     * @param tiers
     * @return String pour les documents à destination de l'OP ou du tribunal.
     * @throws Exception
     */
    public String getAdressePrincipaleEnvoiOP(IntTiers tiers) throws Exception {
        String adresse = "";
        if (tiers != null) {
            // Récupérer le tiers
            TITiers pyTiers = tiersService.loadTiers(getSession(), tiers);
            if (!ICOEtapeHelper.isEtapePoursuite(getTransition().getEtapeSuivante().getIdEtape())) {
                adresse = getAdressePrincipale(tiers);
            } else {
                // Adresse Contentieux
                adresse = pyTiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_CONTENTIEUX, curContentieux.getCompteAnnexe().getIdExterneRole(),
                        getDateExecution(), new TIAdresseFormater(), false, null);
                if (JadeStringUtil.isBlank(adresse)) {
                    adresse = pyTiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IConstantes.CS_APPLICATION_CONTENTIEUX, getDateExecution(), new TIAdresseFormater(), false);
                }

                // Adresse Domicile
                if (JadeStringUtil.isBlank(adresse)) {
                    adresse = getAdresseDomicile(tiers);
                }

                // Adresse de Courrier
                if (JadeStringUtil.isBlank(adresse)) {
                    // Controle une adresse de domaine Contentieux dont on à pas renseigné l'idExterne.
                    adresse = pyTiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IConstantes.CS_APPLICATION_DEFAUT, curContentieux.getCompteAnnexe().getIdExterneRole(),
                            getDateExecution(), new TIAdresseFormater(), false, null);
                }
                if (JadeStringUtil.isBlank(adresse)) {
                    adresse = pyTiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IConstantes.CS_APPLICATION_DEFAUT, getDateExecution(), new TIAdresseFormater(), false);
                }
            }
        }
        return adresse;
    }

    /**
     * permet de trouver l'année pour l'idExterne d'une section (utilisé par ex. par la ligne technique à la FER)
     */
    protected String getAnneeFromContentieux() {
        String annee = "";
        if ((curContentieux != null) && (curContentieux.getSection() != null)) {
            if (curContentieux.getSection().getIdExterne().length() >= 4) {
                annee = curContentieux.getSection().getIdExterne().substring(0, 4);
            }
        }
        return annee;
    }

    /**
     * @return the catalogueTextesUtil
     */
    public COCatalogueTextesService getCatalogueTextesUtil() {
        return catalogueTextesUtil;
    }

    /**
     * La valeur courante de la propriété.
     * 
     * @return La valeur courante de la propriété
     * @throws Exception
     *             DOCUMENT ME!
     */
    private JadeUser getCollaborateur() throws Exception {
        if ((userCollaborateur == null) && !JadeStringUtil.isEmpty(userIdCollaborateur)) {
            userCollaborateur = JadeAdminServiceLocatorProvider.getLocator().getUserService().load(userIdCollaborateur);
        }

        return userCollaborateur;
    }

    /**
     * getter pour l'attribut dateExecution.
     * 
     * @return la valeur courante de l'attribut dateExecution
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * @param extraitCompte
     * @return la description pour l'extrait de compte
     */
    public String getDescriptionExtraitCompte(CAExtraitCompte extraitCompte) {
        if (!JadeStringUtil.isIntegerEmpty(extraitCompte.getLibelleExtraitCompte())) {
            return getSession().getCodeLibelle(extraitCompte.getLibelleExtraitCompte());
        } else {
            if (extraitCompte.getIdTypeOperation().equalsIgnoreCase(APIOperation.CAECRITURE)
                    || extraitCompte.getIdTypeOperation().equalsIgnoreCase(APIOperation.CAAUXILIAIRE)) {
                return extraitCompte.getRubrique().getDescription(getLangue());
            } else {
                StringBuilder buffer = new StringBuilder(getTypeOperationDescriptionFromCache(
                        extraitCompte.getIdTypeOperation(), getLangue()));
                buffer.append(" ");
                // TODO sel : ne devrait pas utiliser texte
                buffer.append(formatMessage(
                        new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 9, 90)),
                        new Object[] { formatDate(extraitCompte.getDate()),
                                getSession().getCodeLibelle(extraitCompte.getProvenancePmt()), "" }));

                return buffer.toString();
            }
        }
    }

    /**
     * Si le domaine n'est pas défini dans la section, retourne TIApplication.CS_DEFAUT
     * 
     * @return
     */
    private String getDomaineDefaut() {
        String domaine;

        if (!JadeStringUtil.isIntegerEmpty(curContentieux.getSection().getDomaine())) {
            domaine = curContentieux.getSection().getDomaine();
        } else {
            domaine = curContentieux.getCompteAnnexe()._getDefaultDomainFromRole();
        }
        return domaine;
    }

    /**
     * Retourne la formule de politesse pour ce tiers.
     * 
     * @param tiers
     *            un tiers osiris
     * @return une formule de politesse
     * @throws Exception
     * @see globaz.pyxis.db.tiers.TITiers#getFormulePolitesse(String)
     */
    protected String getFormulePolitesse(IntTiers tiers) throws Exception {
        return tiersService.getFormulePolitesse(getSession(), tiers, tiers.getLangueISO());
    }

    /**
     * getter pour l'attribut ids contentieux list.
     * 
     * @return la valeur courante de l'attribut ids contentieux list
     */
    public List getIdsContentieuxList() {
        return idsContentieuxList;
    }

    /**
     * getter pour l'attribut id transition.
     * 
     * @return la valeur courante de l'attribut id transition
     */
    public String getIdTransition() {
        return idTransition;
    }

    /**
     * retourne le code iso de la langue pour ce document.
     * 
     * @return la valeur courante de l'attribut langue
     */
    protected String getLangue() {
        if (langueDoc == null) {
            langueDoc = getSession().getIdLangueISO();
        }

        return langueDoc;
    }

    /**
     * Utilisé pour la demande de mainlevée {@link CO006ExecuterRetraitOpposition} La liste des annexes.
     * 
     * @return La liste des annexes
     */
    public String getListeAnnexes() {
        return listeAnnexes;
    }

    /**
     * @return the montantTotalDetail
     */
    public FWCurrency getMontantTotalDetail() {
        return montantTotalDetail;
    }

    /**
     * Retourne le label passser en paramètre suivi de l'idContentieux, idExterneRole du compte annexe et idExterne de
     * la section.<br>
     * Exemple : Erreur contentieux [21] 123.456 200801000
     * 
     * @param label
     *            label : message d'erreur
     * @return label [idContentieux] idExterneRole idExterne
     */
    private String getMsgErreurContentieux(String label) {
        StringBuffer msg = new StringBuffer(label);
        if (curContentieux != null) {
            msg.append(" [" + curContentieux.getIdContentieux() + "]");
            if (curContentieux.getCompteAnnexe() != null) {
                msg.append(" " + curContentieux.getCompteAnnexe().getIdExterneRole());
            }
            if (curContentieux.getSection() != null) {
                msg.append(" " + curContentieux.getSection().getIdExterne());
            }
        }

        return msg.toString();
    }

    /**
     * @return the nouveauContentieux
     */
    public Boolean getNouveauContentieux() {
        return nouveauContentieux;
    }

    /**
     * Retourne le CCP principal de la caisse
     * 
     * @author: sel Créé le : 28 nov. 06
     * @return le N° du compte (ex: 01-12345-1)
     * @throws Exception
     */
    public String getNumeroCCP() throws Exception {
        String resString = "";
        // TODO sel : modifier l'accès au catalogue de textes de Musca
        if (!JadeStringUtil.isBlank(this._getProperty(ACaisseReportHelper.JASP_PROP_BODY_CCP))) {
            resString = this._getProperty(ACaisseReportHelper.JASP_PROP_BODY_CCP);
        } else if (getCatalogueTextesUtil().getMuscaDocument() != null) {
            ICTListeTextes listeTextes = getCatalogueTextesUtil().getMuscaDocument().getTextes(2);
            resString = listeTextes.getTexte(1).toString();
        }

        return resString;
    }

    // -- Gestion des adresses

    /**
     * @return the numeroReferenceInforom
     */
    public String getNumeroReferenceInforom() {
        return numeroReferenceInforom;
    }

    /**
     * Est prévisionnel et n'existe pas encore au contentieux !
     * 
     * @return the previsionnel
     */
    public Boolean getPrevisionnel() {
        return previsionnel;
    }

    /**
     * getter pour l'attribut taxes.
     * 
     * @return la valeur courante de l'attribut taxes
     */
    public List getTaxes() {
        return taxes;
    }

    /**
     * @return the tiersService
     */
    public COTiersService getTiersService() {
        return tiersService;
    }

    /**
     * La valeur courante de la propriété.
     * 
     * @return La valeur courante de la propriété
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected COTransition getTransition() throws Exception {
        if (curTransition == null) {
            curTransition = new COTransition();
            curTransition.setIdTransition(idTransition);
            curTransition.setSession(getSession());
            curTransition.retrieve();
        }

        return curTransition;
    }

    /**
     * Si le type d'adresse n'est pas défini dans la section, retourne TIAvoirAdresse.CS_COURRIER
     * 
     * @return
     */
    private String getTypeAdresseCourrier() {
        String type = "";

        if (!JadeStringUtil.isIntegerEmpty(curContentieux.getSection().getTypeAdresse())) {
            type = curContentieux.getSection().getTypeAdresse();
        } else {
            type = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        }
        return type;
    }

    /**
     */
    protected String getTypeOperationDescriptionFromCache(String typeOperation, String language) {
        if (CODocumentManager.TYPE_OP_CACHE == null) {
            try {
                CATypeOperationManager manager = new CATypeOperationManager();

                manager.setSession(getSession());
                manager.find();
                CODocumentManager.TYPE_OP_CACHE = new HashMap();

                for (int i = 0; i < manager.size(); i++) {
                    CATypeOperation type = (CATypeOperation) manager.getEntity(i);

                    CODocumentManager.TYPE_OP_CACHE.put(type.getIdTypeOperation() + Locale.FRENCH.getLanguage(),
                            type.getDescription(Locale.FRENCH.getLanguage()));
                    CODocumentManager.TYPE_OP_CACHE.put(type.getIdTypeOperation() + Locale.GERMAN.getLanguage(),
                            type.getDescription(Locale.GERMAN.getLanguage()));
                    CODocumentManager.TYPE_OP_CACHE.put(type.getIdTypeOperation() + Locale.ITALIAN.getLanguage(),
                            type.getDescription(Locale.ITALIAN.getLanguage()));
                }
            } catch (Exception e) {
                // Do nothing
            }
        }

        if (CODocumentManager.TYPE_OP_CACHE != null) {
            return (String) CODocumentManager.TYPE_OP_CACHE.get(typeOperation + language.toLowerCase());
        } else {
            CATypeOperation cacheTypeOperation = new CATypeOperation();

            cacheTypeOperation.setISession(getSession());
            cacheTypeOperation.setIdTypeOperation(typeOperation);

            try {
                cacheTypeOperation.retrieve();

                if (cacheTypeOperation.isNew()) {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }

            return cacheTypeOperation.getDescription(language);
        }
    }

    /**
     * getter pour l'attribut user id collaborateur.
     * 
     * @return la valeur courante de l'attribut user id collaborateur
     */
    public String getUserIdCollaborateur() {
        return userIdCollaborateur;
    }

    public String giveLibelleInfoRom246() {
        String theIdRole = "";
        String theLibelle = "";

        try {
            theIdRole = curContentieux.getCompteAnnexe().getIdRole();

            if (IntRole.ROLE_AFFILIE_PARITAIRE.equalsIgnoreCase(theIdRole)) {
                theLibelle = CodeSystem.getLibelleIso(getSession(), CODocumentManager.CODE_SYSTEM_INFOROM246_PAR,
                        getLangue());
            } else if (IntRole.ROLE_AFFILIE_PERSONNEL.equalsIgnoreCase(theIdRole)) {
                theLibelle = CodeSystem.getLibelleIso(getSession(), CODocumentManager.CODE_SYSTEM_INFOROM246_PERS,
                        getLangue());
            }

        } catch (Exception e) {
            theLibelle = "";
        }

        return theLibelle;
    }

    /**
     * @return the documentConfidentiel
     */
    public boolean isDocumentConfidentiel() {
        return documentConfidentiel;
    }

    /**
     * getter pour l'attribut print completion doc.
     * 
     * @return la valeur courante de l'attribut print completion doc
     */
    public boolean isPrintCompletionDoc() {
        return printCompletionDoc;
    }

    /**
     * getter pour l'attribut print outline.
     * 
     * @return la valeur courante de l'attribut print outline
     */
    public boolean isPrintOutline() {
        return printOutline;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        if (idsContentieuxList.size() <= CODocumentManager.JOB_QUEUE_THRESHOLD) {
            return GlobazJobQueue.READ_SHORT;
        } else {
            return GlobazJobQueue.READ_LONG;
        }
    }

    /**
     * ajoute un message de type erreur dans le memory log.
     * 
     * @param message
     *            le message a logger
     */
    protected void log(String message) {
        this.log(message, FWMessage.ERREUR);
    }

    /**
     * ajoute un message du type donné dans le memory log.
     * 
     * @param message
     *            le message à logger
     * @param msgType
     *            le type de message à logger
     */
    protected void log(String message, String msgType) {
        getMemoryLog().logMessage(getMsgErreurContentieux(message), msgType, this.getClass().getName());
    }

    /**
     * Retourne <code>true</code> si il reste des entités à traiter et prépare l'entité courante.
     * 
     * @return <code>true</code> si il reste des entités
     * @throws FWIException
     *             En cas de problème
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (idEntityIterator == null) {
            idEntityIterator = idsContentieuxList.iterator();
        }

        boolean hasNext = idEntityIterator.hasNext();

        if (hasNext) {
            String idCtx = "";
            try {
                if (!nouveauContentieux.booleanValue()) {
                    COIDContentieux idContentieux = (COIDContentieux) idEntityIterator.next();
                    idCtx = idContentieux.toString();
                    curContentieux = COContentieuxFactory.loadContentieux(getSession(),
                            idContentieux.getIdContentieux());
                    curContentieux.refreshLinks(getTransaction());
                } else {
                    curContentieux = (COContentieux) idEntityIterator.next();
                }

                // destinataire est l'affilié
                destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
                _setLangueFromTiers(destinataireDocument);
                // Renseigne la langue à utiliser pour le catalogue de textes en fonctions du tiers
                getCatalogueTextesUtil().setLangueDoc(getLangue());

            } catch (Exception e) {
                throw new FWIException("impossible de charger le contentieux: " + idCtx);
            }
        }

        return hasNext;
    }

    /**
     * @param catalogueTextesUtil
     *            the catalogueTextesUtil to set
     */
    public void setCatalogueTextesUtil(COCatalogueTextesService catalogueTextesUtil) {
        this.catalogueTextesUtil = catalogueTextesUtil;
    }

    /**
     * @param userCollaborateur
     *            La nouvelle valeur de la propriété
     */
    public void setCollaborateur(JadeUser userCollaborateur) {
        this.userCollaborateur = userCollaborateur;
        setUserIdCollaborateur(userCollaborateur.getIdUser());
    }

    /**
     * setter pour l'attribut dateExecution.
     * 
     * @param dateExecution
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateExecution(String dateExecution) {
        this.dateExecution = dateExecution;
    }

    /**
     * @param documentConfidentiel
     *            the documentConfidentiel to set
     */
    public void setDocumentConfidentiel(boolean documentConfidentiel) {
        this.documentConfidentiel = documentConfidentiel;
    }

    /**
     * setter pour l'attribut EMail using collaborateur.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void setEMailUsingCollaborateur() throws Exception {
        if (getCollaborateur() != null) {
            setEMailAddress(getCollaborateur().getEmail());
        } else {
            throw new Exception("Collaborateur n'est pas renseigné");
        }
    }

    /**
     * setter pour l'attribut ids contentieux list.
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdsContentieuxList(List list) {
        idsContentieuxList = list;
    }

    /**
     * @param idTransition
     *            La nouvelle valeur de la propriété
     */
    public void setIdTransition(String idTransition) {
        this.idTransition = idTransition;
    }

    /**
     * Utilisé pour la demande de mainlevée {@link CO007ExecuterDemandeMainlevee}
     * 
     * @param string
     *            des annexes
     */
    public void setListeAnnexes(String string) {
        listeAnnexes = string;
    }

    /**
     * @param montantTotalDetail
     *            the montantTotalDetail to set
     */
    public void setMontantTotalDetail(FWCurrency montantTotalDetail) {
        this.montantTotalDetail = montantTotalDetail;
    }

    /**
     * @param nouveauContentieux
     *            the nouveauContentieux to set
     */
    public void setNouveauContentieux(Boolean nouveauContentieux) {
        this.nouveauContentieux = nouveauContentieux;
    }

    /**
     * @param numeroReferenceInforom
     *            the numeroReferenceInforom to set
     */
    public void setNumeroReferenceInforom(String numeroReferenceInforom) {
        this.numeroReferenceInforom = numeroReferenceInforom;
    }

    /**
     * Est prévisionnel et n'existe pas encore au contentieux !
     * 
     * @param previsionnel
     *            the previsionnel to set
     */
    public void setPrevisionnel(Boolean previsionnel) {
        this.previsionnel = previsionnel;
    }

    /**
     * setter pour l'attribut print completion doc.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrintCompletionDoc(boolean b) {
        printCompletionDoc = b;
    }

    /**
     * setter pour l'attribut print outline.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrintOutline(boolean b) {
        printOutline = b;
    }

    /**
     * setter pour l'attribut taxes.
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setTaxes(List list) {
        taxes = list;
    }

    /**
     * setter pour l'attribut user id collaborateur.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setUserIdCollaborateur(String string) {
        userIdCollaborateur = string;
    }

    /**
     * Retourne un résumé des opérations sur une section sous la forme d'une liste d'instance de SituationCompteItem
     * triées par type d'opération.
     * 
     * @param fromDate
     *            seules les opérations effectuées après cette date sont retournées
     * @param idJournal
     *            premier journal de la section
     * @param csEtape
     *            Etape du contentieurx en cours
     * @return une liste jamais nulle, peut-être vide d'instances de SituationCompteItem
     * @throws Exception
     */
    protected List<COSituationCompteItem> situationCompte(String fromDate, String idJournal, String csEtape)
            throws Exception {

        return situationCompteNouveauRegime(fromDate, false, idJournal, csEtape);
    }

    /**
     * Retourne un résumé des opérations sur une section sous la forme d'une liste d'instance de SituationCompteItem
     * triées par type d'opération.
     * 
     * @param fromDate
     *            seules les opérations effectuées après cette date sont retournées
     * @param idJournal
     *            premier journal de la section
     * @param csEtape
     *            Etape du contentieurx en cours
     * @return une liste jamais nulle, peut-être vide d'instances de SituationCompteItem
     * @throws Exception
     */
    protected List<COSituationCompteItem> situationCompteNouveauRegime(String fromDate, Boolean useDate,
            String idJournal, String csEtape) throws Exception {
        LinkedList<COSituationCompteItem> retValue = new LinkedList<COSituationCompteItem>();
        COExtraitCompteManager extraitCompteManager = new COExtraitCompteManager();
        // initialiser le manager des extraits de compte
        extraitCompteManager.setSession(getSession());
        extraitCompteManager.setForNotIdJournal(idJournal);
        extraitCompteManager.setForSectionForPmtComp(curContentieux.getIdSection());
        extraitCompteManager.setFromDate(fromDate);
        extraitCompteManager.setUseDate(useDate);

        if (ICOEtape.CS_DEMANDE_DE_MAINLEVEE_ENVOYEE.equals(csEtape)
                || ICOEtape.CS_FRAIS_ET_INTERETS_RECLAMES.equals(csEtape) || ICOEtape.CS_DECISION.equals(csEtape)) {
            // Afficher les pmt de l'OP
            extraitCompteManager.setAffichePaiementOP(true);
        }
        extraitCompteManager.find(getTransaction());

        for (int id = 0; id < extraitCompteManager.size(); ++id) {
            retValue.add(new COSituationCompteItem((CAExtraitCompte) extraitCompteManager.getEntity(id)));
        }
        return retValue;
    }

    /**
     * Permet de corriger un problème de iText. <br/>
     * Force un retour à la ligne (<code>\n</code>) tous les X ligne.<br/>
     * X étant définit par le paramètre nbCharPerLine.
     * 
     * @param source
     * @param nbCharPerLine
     * @return la source avec des \n tous les nbCharPerLine
     */
    protected String splitOnXChar(String source, int nbCharPerLine) {
        StringBuilder resultat = new StringBuilder("");
        String[] lines = source.split("\n");

        // Parcour toutes les lignes de la source
        for (int j = 0; j < lines.length; j++) {
            String line = lines[j];

            // Si une ligne est trop longue, on force en retour à la ligne
            if (line.length() > nbCharPerLine) {
                String tab[] = line.split(" ");

                boolean flag = true;
                String part2 = "";
                for (int i = tab.length - 1; flag || (i == 0); i--) {
                    part2 = tab[i] + part2;
                    if ((line.length() - part2.length()) < nbCharPerLine) {
                        flag = false;
                    } else {
                        part2 = " " + part2;
                    }
                }

                // Remplace l'espace par un retour à la ligne
                line = JadeStringUtil.insert(line, "\n", line.length() - part2.trim().length());
                // temp = JadeStringUtil.remove(temp, temp.lastIndexOf(" "), 1);
            }
            resultat.append(line);
            resultat.append("\n");
        }

        return resultat.toString();
    }

    /**
     *
     * Initialisation la QR facture
     *
     * @param montantTotal : le montant de la facture
     */
    public void initVariableQR(FWCurrency montantTotal) {

        qrFacture.setMonnaie(getCatalogueTextesUtil().texte(getParent(), 3, 2).contains(COCatalogueTextesService.TEXTE_INTROUVABLE)?
                qrFacture.DEVISE_DEFAUT : getCatalogueTextesUtil().texte(getParent(), 3, 2) );
        qrFacture.setMontant(Objects.isNull(montantTotal)? "" : montantTotal.toString());
        qrFacture.setLangueDoc(langueDoc);

        try {
            //qrFacture.setCrePays(qrFacture.getCodePays());
            qrFacture.recupererIban();
            if (!qrFacture.genererAdresseDebiteur(curContentieux.getCompteAnnexe().getIdTiers())) {

                // S'il s'agit d'une adresse combiné, et que le nombre de caractère dépasse les 70
                // Il faut donc séparé l'adresse sur deux lignes, et mettre la deuxième partie sur la ligne 2
                String adresseDebiteurAsString = getAdresseDestinataire();
                TIAdresseDataSource adresseDebiteurAsData = getAdresseDestinataireAsData();
                if (Objects.nonNull(adresseDebiteurAsData)) {
                    qrFacture.setDebfAdressTyp(ReferenceQR.STRUCTURE);
                    qrFacture.setDebfNom(adresseDebiteurAsData.fullLigne1);
                    qrFacture.setDebfPays(adresseDebiteurAsData.paysIso);
                    qrFacture.setDebfCodePostal(adresseDebiteurAsData.localiteNpa);
                    qrFacture.setDebfLieu(adresseDebiteurAsData.localiteNom);
                    qrFacture.setDebfRueOuLigneAdresse1(adresseDebiteurAsData.rue);
                    qrFacture.setDebfNumMaisonOuLigneAdresse2(adresseDebiteurAsData.numeroRue);
                } else {
                    // si l'adresse n'est pas trouvé en DB, alors chargement d'une adresse Combiné
                    qrFacture.setDebfAdressTyp(ReferenceQR.COMBINE);
                    if (adresseDebiteurAsString.length() > 70 && (adresseDebiteurAsString.substring(0, 70).lastIndexOf("\n")!= -1)) {
                        qrFacture.setDebfRueOuLigneAdresse1(adresseDebiteurAsString.substring(0, adresseDebiteurAsString.substring(0, 70).lastIndexOf("\n")));
                        qrFacture.setDebfNumMaisonOuLigneAdresse2(adresseDebiteurAsString.substring(adresseDebiteurAsString.substring(0, 70).lastIndexOf("\n"), adresseDebiteurAsString.length()));
                    } else {
                        qrFacture.setDebfRueOuLigneAdresse1(adresseDebiteurAsString);
                    }
                }
            }
            qrFacture.genererReferenceQR(curContentieux.getSection());

            // Il n'existe pas pour l'heure actuel d'adresse de créditeur en DB.
            // Elle est récupérée depuis le catalogue de texte au format Combinée
            qrFacture.genererCreAdresse();
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche des élements de la sommation : " + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }


    }

    /**
     * @return l'adresse définie dans la section sinon getAdresseString(destinataireDocument)
     * @throws Exception
     */
    public String getAdresseDestinataire() throws Exception {
        return getAdressePrincipale(destinataireDocument);
    }

    /**
     * @return l'adresse définie dans la section sinon getAdresseString(destinataireDocument)
     * @throws Exception
     */
    public TIAdresseDataSource getAdresseDestinataireAsData() throws Exception {
        return getAdressePrincipaleAsData(destinataireDocument);
    }

    /**
     * @return La langue du documet
     */
    public String getLangueDoc() {
        return langueDoc;
    }

    /**
     * Détermine le total de page d'un document afin de pouvoir gérer les X dans les BVR.
     * Pour cela, on construit en mémoire le document.
     */
    protected void computeTotalPage() {
        int nbPages = 0;
        FWIImporterInterface importDoc = super.getImporter();
        try {
            String sourceFilename = importDoc.getImportPath() + "/" + getJasperTemplate() + importDoc.getImportType();

            // On construit le document pour connaitre le nb de page total
            JasperPrint m_document = JasperFillManager.fillReport(sourceFilename, importDoc.getParametre(),
                    getDataSource());
            if ((m_document != null)) {
                nbPages = m_document.getPages().size();
            }

            // On recharge le data source
            createDataSource();
        } catch (Exception e) {
            getMemoryLog().logMessage("Problème pour déterminer le nb de page total du document : " + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        // On passe le nb de page au document
        setParametres("P_NOMBRE_PAGES", nbPages);
    }

    /**
     * Retourne le template Jasper PRINCIPAL utilisé pour générer le document. (p.ex : DOCUMENT_VIDE pour
     * DOCUMENT_VIDE.japser)
     *
     * @return String représentant le fichier jasper pour la génération du document.
     */
    public abstract String getJasperTemplate();

}
