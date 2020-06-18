package globaz.osiris.print.itext.list;

import ch.globaz.common.util.GenerationQRCode;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import ch.globaz.common.document.reference.ReferenceQR;
import globaz.osiris.external.IntTiers;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_DS;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * Classe abstraite parente de tous les documents du projet osiris. Centralise les fonctionalités communes aux documents
 * 
 * @see globaz.framework.printing.itext.FWIDocumentManager
 * @author Kurus, 10-mai-2005
 */
public abstract class CADocumentManager extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String JASP_PROP_BODY_ADR_CAISSE_BVR = "body.adr.caisse.bvr.";
    /** Template properties */
    public final static String JASP_PROP_BODY_AFFILIE = "body.affilie.";
    public final static String JASP_PROP_BODY_CACLIBELLE = "body.caclibelle.";
    public final static String JASP_PROP_BODY_CACMONTANT = "body.cacmontant.";
    public final static String JASP_PROP_BODY_CACREPORT = "body.cacreport.";
    public final static String JASP_PROP_BODY_CACTEXT_MINIMEPOS = "body.cactext.minimepos.";
    public final static String JASP_PROP_BODY_COL_LIBELLE_CACFRAIS = "body.col.libelle.cacfrais.";
    public final static String JASP_PROP_BODY_COL_LIBELLE_CACINTERET = "body.col.libelle.cacinteret.";
    public final static String JASP_PROP_BODY_COL_LIBELLE_CACMONTANTINIT = "body.col.libelle.cacmontantinit.";
    public final static String JASP_PROP_BODY_COL_LIBELLE_CACPAIEMENT = "body.col.libelle.cacpaiement.";
    public final static String JASP_PROP_BODY_COL_LIBELLE_CACTAXESSOMM = "body.col.libelle.cactaxessomm.";
    public final static String JASP_PROP_BODY_COMPANY_NAME = "nom.caisse.";
    public final static String JASP_PROP_BODY_EXERCICE = "body.exercice.";
    public final static String JASP_PROP_BODY_FORMULE_DESTINATAIRE_FEMME = "body.formule.destinataire.femme.";
    public final static String JASP_PROP_BODY_FORMULE_DESTINATAIRE_HOMME = "body.formule.destinataire.homme.";
    public final static String JASP_PROP_BODY_FORMULE_DESTINATAIRE_HOMME_FEMME = "body.formule.destinataire.homme.femme.";
    public final static String JASP_PROP_BODY_LABEL_CACPAGE = "body.label.cacpage.";
    public final static String JASP_PROP_BODY_NUMERO_COMPTE = "body.numero.compte.";
    public final static String JASP_PROP_BODY_TRI_NOM = "body.tri.nom.";
    public final static String JASP_PROP_BODY_TRI_NUMERO = "body.tri.numero.";
    /** Seuil de l'exécution directe ou différée */
    private static final int JOB_QUEUE_THRESHOLD = 10;
    protected int factureImpressionNo = 0;

    private static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";

    /**
     * remplace dans message {n} par args[n].
     * <p>
     * Evite que {@link MessageFormat} ne lance une erreur ou ne se comporte pas correctement si le message contient des
     * apostrophes.
     * </p>
     * <p>
     * En attendant qu'elle soit dans le framework.
     * </p>
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    public static final String formatMessage(String message, Object[] args) {
        StringBuilder buffer = new StringBuilder(message);

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }

        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
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
    public static final String formatMontant(String montant) {
        if (JadeStringUtil.isBlank(montant)) {
            return "";
        }
        // rechercher un formatteur de date pour la langue du document
        return JANumberFormatter.format(montant);
    }

    /** Collection pour un DataSource */
    private ArrayList beanList = new ArrayList();
    /** Entité courante */
    private Object currentEntity = null;
    // Champs pour le catalogue de textes
    protected ICTDocument document;
    /** Liste des entités à traiter */
    private ArrayList entityList = new ArrayList();
    /** Document principal */
    private CADocumentManager firstDocument = null;
    protected String idDocument = "";
    /** Itérateur sur la liste des entités */
    private Iterator iEntityList = null;
    /** L'annonce de l'assuré */
    // protected CAExtraitAnnonceAssureBean annonceAssure = null;
    /** Langue du document */
    private String langue = getSession().getIdLangueISO();

    /** Liste annexes */
    private String listeAnnexes = "";
    protected String nomDocument = "";
    protected int numDocument = 0;
    private String numeroReferenceInforom = "";
    /** PrintCompletionDoc */
    private boolean printCompletionDoc = true;

    /** PrintOutline */
    private boolean printOutline = false;

    /** Map d'une ligne de données */
    private Map row = new HashMap();

    protected String typeDocument = "";

    // Ajout pour QR Facture
    protected ReferenceQR qrFacture = null;
    protected CAImpressionBulletinsSoldes_DS sectionCourante;
    protected CACompteAnnexe compteAnnexe;
    protected CASection section = null;
    protected CAEcheancePlan echeance = null;

    /*
     * @TODO Supprimer ces vieux constructeurs
     */
    public CADocumentManager() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), FWIDocumentManager.DEFAULT_FILE_NAME);
    }

    public CADocumentManager(BProcess parent) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, FWIDocumentManager.DEFAULT_FILE_NAME);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @param fileName
     *            Le nom du fichier
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CADocumentManager(BProcess parent, String fileName) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, fileName);
    }

    /**
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CADocumentManager(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public CADocumentManager(BSession parent) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, FWIDocumentManager.DEFAULT_FILE_NAME);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @param fileName
     *            Le nom du fichier
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CADocumentManager(BSession parent, String fileName) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, fileName);
    }

    /**
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CADocumentManager(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#_createDocumentInfo()
     */
    @Override
    protected void _createDocumentInfo() {
        super._createDocumentInfo();
        super.getDocumentInfo().setDocumentTypeNumber(getNumeroReferenceInforom());
    }

    @Override
    public void afterExecuteReport() {
        super.afterExecuteReport();
        try {
            GenerationQRCode.deleteQRCodeImage();
        } catch (IOException e) {
            getMemoryLog().logMessage("Erreur lors de la suppression de l'image QR-Code : " + e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * @return La langue du document
     */
    protected String _getLangue() {
        return langue.toUpperCase();
    }

    /**
     * Retourne un String représentant la valeur de la propriété
     * 
     * @param property
     *            Le nom de la propriété
     * @param additionalValue
     *            Une valeur additionnelle
     * @return La valeur de la propriété
     */
    protected String _getProperty(String property, String additionalValue) {
        StringBuilder buffer = new StringBuilder("");
        if ((!JadeStringUtil.isBlank(property))
                && (getTemplateProperty(getDocumentInfo(), property + _getLangue()) != null)) {
            buffer.append(getTemplateProperty(getDocumentInfo(), property + _getLangue()));
        }
        if (!JadeStringUtil.isBlank(additionalValue)) {
            buffer.append(additionalValue);
        }
        return buffer.toString();
    }

    /**
     * Gestion de l'en-tête/pied de page/signature
     * 
     * @param adresseDestination
     *            L'adresse du destinataire du document
     * @param hasHeader
     *            <code>true</code> si le document contient un en-tête
     * @param hasFooter
     *            <code>true</code> si le document contient un pied de page
     * @param hasSignature
     *            <code>true</code> si le document contient une signature
     * @throws Exception
     *             En cas de problème
     */
    protected void _handleHeaders(Object adresseDestination, boolean hasHeader, boolean hasFooter, boolean hasSignature)
            throws Exception {
        this._handleHeaders(adresseDestination, hasHeader, hasFooter, hasSignature, JACalendar.today().toString());
    }

    /**
     * Gestion de l'en-tête/pied de page/signature
     * 
     * @param adresseDestination
     *            L'adresse du destinataire du document
     * @param hasHeader
     *            <code>true</code> si le document contient un en-tête
     * @param hasFooter
     *            <code>true</code> si le document contient un pied de page
     * @param hasSignature
     *            <code>true</code> si le document contient une signature
     * @throws Exception
     *             En cas de problème
     */
    protected void _handleHeaders(Object adresseDestination, boolean hasHeader, boolean hasFooter,
            boolean hasSignature, String date) throws Exception {
        getDocumentInfo().setTemplateName("");
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), _getLangue());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
            headerBean.setConfidentiel(true);
        }
        // Adresse du destinataire
        if (adresseDestination != null) {
            if (adresseDestination instanceof CAPlanRecouvrement) {
                headerBean.setAdresse(((CAPlanRecouvrement) adresseDestination).getAdressePaiementTiers());
            } else {
                headerBean.setAdresse((String) adresseDestination);
            }
        }
        // Date
        headerBean.setDate(formatDate(date));
        headerBean.setUser(getSession().getUserInfo());
        headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        if (!JadeStringUtil.isBlank(getSession().getUserFullName())) {
            headerBean.setNomCollaborateur(getSession().getUserFullName());
        } else {
            headerBean.setNomCollaborateur(getSession().getUserInfo().getFirstname() + " "
                    + getSession().getUserInfo().getLastname());
        }
        headerBean.setEmailCollaborateur(getSession().getUserInfo().getEmail());

        // Paramètres relatifs à l'annonce
        headerBean.setNoAffilie("");
        if ((adresseDestination != null) && (adresseDestination instanceof CAPlanRecouvrement)) {
            if (!JadeStringUtil.isBlank(((CAPlanRecouvrement) adresseDestination).getCompteAnnexe().getId())) {
                String numAff = ((CAPlanRecouvrement) adresseDestination).getCompteAnnexe().getIdExterneRole();
                headerBean.setNoAffilie(numAff);

                getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
                try {
                    IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                            TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                            affilieFormater.unformat(numAff));
                    TIDocumentInfoHelper.fill(getDocumentInfo(), ((CAPlanRecouvrement) adresseDestination)
                            .getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE, numAff, affilieFormater
                            .unformat(numAff));
                } catch (Exception e) {
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
                }
                getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
                getDocumentInfo().setArchiveDocument(true);
            }
        }
        if (hasHeader) {
            caisseReportHelper.addHeaderParameters(this, headerBean);
        }
        if (hasFooter) {
            caisseReportHelper.addFooterParameters(this);
        }
        if (hasSignature) {
            caisseReportHelper.addSignatureParameters(this);
        }
    }


    /**
     * Spécifie la langue du document
     * 
     * @param langueDoc
     *            La langue du document
     */
    protected void _setLangueDocument(String langueDoc) {
        if (!JadeStringUtil.isBlank(langueDoc)) {
            langue = langueDoc;
        }
    }

    /**
     * Spécifie la langue du document en fonction de la langue de l'affilié
     * 
     * @param affilie
     *            La langue de l'affilié
     */
    protected void _setLangueFromAffilie(TITiersViewBean affilie) {
        if (affilie != null) {
            langue = getSession().getCode(affilie.getLangue());
        }
    }

    /**
     * Spécifie la langue du document en fonction de la langue d'un tiers
     * 
     * @param tiers
     *            Le tiers
     */
    protected void _setLangueFromTiers(IntTiers tiers) {
        if (tiers != null) {
            langue = tiers.getLangueISO();
        }
    }

    /**
     * Ajoute un groupe d'entités à la liste
     * 
     * @param allEntities
     *            Le groupe d'entités
     * @return <code>true</code> si l'ajout s'est bien passé
     */
    public boolean addAllEntities(Collection allEntities) {
        return entityList.addAll(allEntities);
    }

    /**
     * Ajoute une entité à la liste
     * 
     * @param entity
     *            L'entité
     * @return <code>true</code> si l'ajout s'est bien passé
     */
    public boolean addEntity(Object entity) {
        return entityList.add(entity);
    }

    /**
     * Ne fait rien par défaut
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * Ne fait rien par défaut
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument()
     */
    @Override
    public boolean beforePrintDocument() {
        if (firstDocument != null) {
            // Gestion du titre et du nom
            setDocumentTitle(firstDocument.getExporter().getExportFileName());
            getExporter().setExportFileName(firstDocument.getExporter().getExportFileName());
        }
        try {
            if (isPrintOutline()) {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_ALL);
            } else {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
            }
            return isPrintCompletionDoc() && super.beforePrintDocument();
        } catch (FWIException e) {
            // CAST OMMIT
        }
        return super.beforePrintDocument();
    }

    /**
     * Retourne l'entité courante
     * 
     * @return L'entité
     */
    public Object currentEntity() {
        return currentEntity;
    }

    /**
     * Regroupe tous les textes du même niveau en les séparant par la chaine de caractères passée en paramètre.
     * 
     * @param niveau
     *            du catalogue de texte
     * @param out
     *            buffer regroupant les textes du même niveau
     * @param paraSep
     *            Chaine de séparation entre les positions du niveau.
     */
    protected void dumpNiveau(int niveau, StringBuilder out, String paraSep) {
        try {
            for (Iterator paraIter = loadCatalogue().getTextes(niveau).iterator(); paraIter.hasNext();) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }

                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.append(CADocumentManager.TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
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
        return JACalendar.format(date, _getLangue());
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
        return CADocumentManager.formatMessage(message.toString(), args);
    }

    /**
     * permet de trouver l'année pour l'idExterne d'une section (utilisé par ex. par la ligne technique à la FER)
     */
    protected String getAnneeFromSection(CASection section) {
        String annee = "";
        if ((section != null) && (section.getIdExterne() != null)) {
            if (section.getIdExterne().length() >= 4) {
                annee = section.getIdExterne().substring(0, 4);
            }
        }
        return annee;
    }

    /**
     * @return un JRDataSource de type JRMapCollectionDataSource
     */
    protected JRDataSource getEntityBeanDataSource() {
        return new JRMapCollectionDataSource(beanList);
    }

    /**
     * @return the idDocument
     */
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * @return La liste des annexes
     */
    public String getListeAnnexes() {
        return listeAnnexes;
    }

    /**
     * @return the nomDocument
     */
    protected String getNomDocument() {
        return nomDocument;
    }

    /**
     * @return the numDocument
     */
    protected int getNumDocument() {
        return numDocument;
    }

    /**
     * @return the numeroReferenceInforom
     */
    public String getNumeroReferenceInforom() {
        return numeroReferenceInforom;
    }


    /**
     * Récupère les textes du catalogue de texte
     * 
     * @param niveau
     * @param position
     * @return
     */
    protected StringBuilder getTexte(int niveau, int position) {
        StringBuilder resString = new StringBuilder("");
        try {
            ICTListeTextes listeTextes = loadCatalogue().getTextes(niveau);
            resString.append(listeTextes.getTexte(position));
        } catch (Exception e3) {
            getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                    getSession().getLabel("ERROR_GETTING_LIST_TEXT") + niveau + ":" + position);
        }
        return resString;
    }

    /**
     * @return the typeDocument
     */
    protected String getTypeDocument() {
        return typeDocument;
    }

    public boolean isPrintCompletionDoc() {
        return printCompletionDoc;
    }

    public boolean isPrintOutline() {
        return printOutline;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        if (entityList.size() <= CADocumentManager.JOB_QUEUE_THRESHOLD) {
            return GlobazJobQueue.READ_SHORT;
        } else {
            return GlobazJobQueue.READ_LONG;
        }
    }

    /**
     * retourne le catalogue de texte pour le document courant.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected ICTDocument loadCatalogue() throws Exception {
        if (document == null) {
            ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            loader.setActif(Boolean.TRUE);
            if (!JadeStringUtil.isBlank(getNomDocument())) {
                loader.setNom(getNomDocument());
            } else if (!JadeStringUtil.isBlank(getIdDocument())) {
                loader.setIdDocument(getIdDocument());
            } else {
                loader.setDefault(Boolean.TRUE);
            }
            loader.setCodeIsoLangue(_getLangue());
            loader.setCsDomaine(CACodeSystem.CS_DOMAINE_CA);
            loader.setCsTypeDocument(getTypeDocument());

            ICTDocument[] candidats = loader.load();

            document = candidats[numDocument];
        }

        return document;
    }

    /**
     * Initialise une nouvelle ligne de donnée pour la Map JRDataSource
     */
    protected void newLineEntityBean() {
        if (row.size() > 0) {
            beanList.add(row);
        }
        row = new HashMap();
    }

    /**
     * Retourne <code>true</code> s'il reste des entités à traiter et prépare l'entité courante.
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     * @return <code>true</code> s'il reste des entités
     * @throws FWIException
     *             En cas de problème
     */
    @Override
    public boolean next() throws FWIException {
        if (iEntityList == null) {
            iEntityList = entityList.iterator();
        }
        boolean hasNext = iEntityList.hasNext();
        if (hasNext) {
            currentEntity = iEntityList.next();
        }
        return hasNext;
    }

    /**
     * Set un champ d'information pour la Map JRDataSource
     * 
     * @param name
     * @param obj
     */
    protected void setEntityBean(String name, Object obj) {
        row.put(name, obj);
    }

    /**
     * @param document
     *            Document principal
     */
    public void setFirstDocument(CADocumentManager document) {
        firstDocument = document;
    }

    /**
     * @param idDocument
     *            the idDocument to set
     */
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    /**
     * @param string
     *            des annexes
     */
    public void setListeAnnexes(String string) {
        listeAnnexes = string;
    }

    /**
     * @param nomDocument
     *            the nomDocument to set
     */
    protected void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    /**
     * @param numDocument
     *            the numDocument to set
     */
    protected void setNumDocument(int numDocument) {
        this.numDocument = numDocument;
    }

    /**
     * Référence Inforom du document
     * 
     * @param numeroReferenceInforom
     *            the numeroReferenceInforom to set
     */
    public void setNumeroReferenceInforom(String numeroReferenceInforom) {
        this.numeroReferenceInforom = numeroReferenceInforom;
    }

    public void setPrintCompletionDoc(boolean b) {
        printCompletionDoc = b;
    }

    public void setPrintOutline(boolean b) {
        printOutline = b;
    }

    /**
     * Définit le type du document
     *
     * @param typeDocument
     *            the typeDocument to set
     */
    protected void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }


    // Initialisation des variables QR
    public void initVariableQR(FWCurrency montantTotal, String idTier) {

        qrFacture.setMonnaie(qrFacture.DEVISE_DEFAUT);
        qrFacture.setMontant(Objects.isNull(montantTotal)? "" : montantTotal.toString());
        qrFacture.setLangueDoc(_getLangue());


        try {
            qrFacture.recupererIban();
            if (!qrFacture.genererAdresseDebiteur(idTier)) {
                // si l'adresse n'est pas trouvé en DB, alors chargement d'une adresse Combiné
                qrFacture.setDebfAdressTyp(ReferenceQR.COMBINE);

                // S'il s'agit d'une adresse combiné, et que le nombre de caractère dépasse les 70
                // Il faut donc séparé l'adresse sur deux lignes, et mettre la deuxième partie sur la ligne 2
                String adresseDebiteur = _getAdressePrincipale();
                if (adresseDebiteur.length() > 70 && (adresseDebiteur.substring(0, 70).lastIndexOf("\n")!= -1)) {
                    qrFacture.setDebfRueOuLigneAdresse1(adresseDebiteur.substring(0, adresseDebiteur.substring(0, 70).lastIndexOf("\n")));
                    qrFacture.setDebfNumMaisonOuLigneAdresse2(adresseDebiteur.substring(adresseDebiteur.substring(0, 70).lastIndexOf("\n"), adresseDebiteur.length()));
                } else {
                    qrFacture.setDebfRueOuLigneAdresse1(adresseDebiteur);
                }
            }

            // Si la sectionCourante est null, c'est que les informations sont contenues dans le plan de recouvrement.
            if (Objects.isNull(sectionCourante)) {
                CAPlanRecouvrement plan = echeance.getPlanRecouvrement();
                String idRole = plan.getCompteAnnexe().getIdRole();
                String idExterneRole = plan.getCompteAnnexe().getIdExterneRole();
                String idPlan = plan.getIdPlanRecouvrement();

                qrFacture.genererReferenceQR(idRole, idExterneRole, true, "", idPlan, plan.getIdCompteAnnexe(), Objects.isNull(montantTotal)? "" : montantTotal.toString());
            } else {
                qrFacture.genererReferenceQR(sectionCourante.getSection());
            }


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
     * L'adresse de paiement est l'adresse de courrier
     *
     * @return l'adresse courrier du domaine facturation sinon l'adresse de domicile du domaine standard
     * @throws Exception
     */
    public String _getAdressePrincipale() throws Exception {
        String result = _getAdresseCourrier();
        if (!JadeStringUtil.isBlank(result)) {
            return result;
        } else {
            return _getAdresseDomicile();
        }
    }

    /**
     * Via le tiers du compte annexe.
     *
     * @return l'adresse du domaine Facturation de type Courrier
     * @throws Exception
     */
    public String _getAdresseCourrier() throws Exception {
        IntTiers tiers = compteAnnexe.getTiers();
        if (tiers == null) {
            return "";
        } else {
            String domaine = section.getDomaine();
            if (JadeStringUtil.isBlankOrZero(domaine)) {
                domaine = compteAnnexe._getDefaultDomainFromRole();
            }
            return tiers.getAdresseAsString(getDocumentInfo(), section.getTypeAdresse(), domaine,
                    compteAnnexe.getIdExterneRole(), JACalendar.today().toStr("."));
        }
    }

    /**
     * Via le tiers du compte annexe.
     *
     * @return l'adresse du domaine Standard de type Domicile
     * @throws Exception
     */
    public String _getAdresseDomicile() throws Exception {
        IntTiers tiers = compteAnnexe.getTiers();
        if (tiers == null) {
            return "";
        } else {
            return tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, compteAnnexe.getIdExterneRole(), JACalendar.today().toStr("."));
        }
    }

    /**
     * Le montant de la facture après les compensations.
     *
     * @return le montant de la section moins le montant de la facture.
     */
    public String _getMontantApresCompensation() {
        FWCurrency montant = new FWCurrency(sectionCourante.getSection().getSolde());
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(montant.toStringFormat()), false, true, false, 2);
    }

    /**
     * Getter QrFacture
     *
     * @return
     */
    public ReferenceQR getQrFacture() {
        return qrFacture;
    }

    /**
     * Setter qrFacture
     *
     * @param qrFacture
     */
    public void setQrFacture(ReferenceQR qrFacture) {
        this.qrFacture = qrFacture;
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
            // Vu que l'on va recharger le dataSource, il faut retirer l'incrément qui a été ajouté
            if (factureImpressionNo > 0) {
                --factureImpressionNo;
            }
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
