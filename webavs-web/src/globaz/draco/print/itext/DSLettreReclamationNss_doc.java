package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.draco.application.DSApplication;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.naos.itext.AFDocumentItextService;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author SCO
 * @since 19 juil. 2011
 */
public class DSLettreReclamationNss_doc extends FWIDocumentManager {

    private static final long serialVersionUID = -7307274381359360281L;
    public static final String DOCUMENT_LETTRE_RAPPEL = "125011";
    public static final String DOCUMENT_LETTRE_RECLAMATION = "125010";
    public static final String GENRE_DOCUMENT_DEFINITIF = "DEFINITIF";
    public static final String GENRE_DOCUMENT_DUBPLICATA = "DUBPLICATA";
    public static final String GENRE_DOCUMENT_SIMULATION = "SIMULATION";

    private static final String LETTRE_RAPPEL = "LETTRE_RAPPEL";
    private static final String LETTRE_RECLAMATION = "LETTRE_RECLAMATION";
    private static final String MODEL_LETTRE_RAPPEL = "DRACO_LETTRE_RAPPEL";
    private static final String MODEL_LETTRE_RECLAMATION = "DRACO_LETTRE_RECLAMATION";

    public static final String NUMERO_INFOROM_RAPPEL = "0265CDS";
    public static final String NUMERO_INFOROM_RECLAMATION = "0264CDS";

    private boolean archiveDocument = true;
    public ICTDocument catalogue = null;
    private String dateDocument = "";
    private String dateLettreReclamation = "";
    private String formulePolitesse = "";
    protected boolean hasNext = true;
    private String idTiers = null;
    private String intituleLettre = "";
    private String langueIsoRequerant = "fr";// langue du requerant
    private Collection<DSLettreReclamationNssBean> m_container = new ArrayList<DSLettreReclamationNssBean>();
    private String numAffilie = null;
    private String numeroRappel = "";
    private String observations = "";
    private boolean publishDocument = true;
    private TITiersViewBean tiers = null;
    private String typeDocument = null;
    private String annee = "";

    /**
     * Constructeur de CELettreProchainControle_Doc
     */
    public DSLettreReclamationNss_doc() throws Exception {
        this(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
        archiveDocument = false;
    }

    /**
     * Constructeur de CELettreProchainControle_Doc
     */
    public DSLettreReclamationNss_doc(BProcess parent) throws java.lang.Exception {
        super(parent, DSApplication.DEFAULT_APPLICATION_ROOT, "");
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
            setDocumentTitle(getSession().getLabel(DSLettreReclamationNss_doc.LETTRE_RECLAMATION));
        } else {
            setDocumentTitle(getSession().getLabel(DSLettreReclamationNss_doc.LETTRE_RAPPEL));
        }
        setParentWithCopy(parent);
        archiveDocument = false;
    }

    /**
     * Constructeur de CELettreProchainControle_Doc
     */
    public DSLettreReclamationNss_doc(BSession session) throws java.lang.Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, "");
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
            setDocumentTitle(getSession().getLabel(DSLettreReclamationNss_doc.LETTRE_RECLAMATION));
        } else {
            setDocumentTitle(getSession().getLabel(DSLettreReclamationNss_doc.LETTRE_RAPPEL));
        }
        archiveDocument = false;
    }

    public String _getDateImpression(TITiersViewBean tiers) {
        String dateImpression = null;

        if (!JadeStringUtil.isEmpty(getDateDocument())) {
            dateImpression = JACalendar.format(getDateDocument(), tiers.getLangueIso());
        } else {
            dateImpression = JACalendar.format(JACalendar.todayJJsMMsAAAA(), tiers.getLangueIso());
        }

        return dateImpression;
    }

    /**
     * @param bean
     * @param tiers
     * @throws Exception
     */
    public void _setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {
        if (tiers == null) {
            tiers = AFUtil.retrieveTiersViewBean(getSession(), getIdTiers());
        }

        // bean.setAdresse(tiers.getAdresseAsString(DSApplication.DEFAULT_APPLICATION_DRACO,
        // JACalendar.todayJJsMMsAAAA()));

        bean.setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA(), getNumAffilie()));
        bean.setDate(bean.getDate());
        bean.setNoAvs(tiers.getNumAvsActuel());
        bean.setConfidentiel(true);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
        bean.setNoAffilie(getNumAffilie());
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.getImporter().setBeanCollectionDataSource(m_container);
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
            setTemplateFile(DSLettreReclamationNss_doc.MODEL_LETTRE_RECLAMATION);
        } else {
            setTemplateFile(DSLettreReclamationNss_doc.MODEL_LETTRE_RAPPEL);
        }
    }

    @Override
    public void createDataSource() throws Exception {
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
            setDocumentTitle(getNumAffilie() + " "
                    + getSession().getLabel(DSLettreReclamationNss_doc.LETTRE_RECLAMATION));
            getDocumentInfo().setDocumentTypeNumber(DSLettreReclamationNss_doc.NUMERO_INFOROM_RECLAMATION);
        } else {
            setDocumentTitle(getNumAffilie() + " " + getSession().getLabel(DSLettreReclamationNss_doc.LETTRE_RAPPEL));
            getDocumentInfo().setDocumentTypeNumber(DSLettreReclamationNss_doc.NUMERO_INFOROM_RAPPEL);
        }
        getDocumentInfo().setArchiveDocument(isArchiveDocument());
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(getDateDocument());

        // Chargement du catalogue
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
            this.loadCatalogue(DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION);
            // Récupération du document ITEXT
            setTemplateFile(DSLettreReclamationNss_doc.MODEL_LETTRE_RECLAMATION);
        } else {
            this.loadCatalogue(DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RAPPEL);
            // Récupération du document ITEXT
            setTemplateFile(DSLettreReclamationNss_doc.MODEL_LETTRE_RAPPEL);
        }

        // Liste des arguments
        // {0} = Formule de politesse
        // {1} = intitulé de la déclaration
        // {2} = date de la lettre de rappel
        // {3} = numero du rappel

        String[] listeArgument = { getFormulePolitesse(), getIntituleLettre(), getDateLettreReclamation(),
                getNumeroRappel() };

        // Mise en place du texte
        // Entete de colonne
        this.setParametres("P_COL1", getSession().getApplication().getLabel("DOC_NOM", langueIsoRequerant));
        this.setParametres("P_COL2", getSession().getApplication().getLabel("DOC_NSS", langueIsoRequerant));
        this.setParametres("P_COL3", getSession().getApplication().getLabel("DOC_PERIODE", langueIsoRequerant));
        this.setParametres("P_COL4", getSession().getApplication().getLabel("DOC_REVENU", langueIsoRequerant));

        // Texte
        this.setParametres("P_TEXTE1", AFDocumentItextService.getTexte(catalogue, 1, listeArgument));
        this.setParametres("P_TEXTE2", AFDocumentItextService.getTexte(catalogue, 2, listeArgument));
        this.setParametres("P_TEXTE3", JadeStringUtil.isEmpty(getObservations()) ? " " : getObservations());
        this.setParametres("P_TEXTE4", AFDocumentItextService.getTexte(catalogue, 3, listeArgument));

        // Numero affilié passé au docinfo
        fillDocInfo();

        // Mise en place du header et du footer
        setHeaderAndFooter(tiers, _getDateImpression(tiers));

    }

    public void fillDocInfo() throws Exception {
        // Numero affilié passé au docinfo
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAffilie());
        getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                AFUtil.giveNumeroAffilieNonFormate(getNumAffilie()));
        getDocumentInfo().setDocumentProperty("annee", getAnnee());

        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getDocumentInfo().getDocumentProperty("numero.affilie.formatte"), getDocumentInfo()
                            .getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "afterPrintDocument()", e);
        }
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDateLettreReclamation() {
        return dateLettreReclamation;
    }

    public String getFormulePolitesse() {
        return formulePolitesse;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIntituleLettre() {
        return intituleLettre;
    }

    public String getLangueIsoRequerant() {
        return langueIsoRequerant;
    }

    public Collection<DSLettreReclamationNssBean> getM_container() {
        return m_container;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumeroRappel() {
        return numeroRappel;
    }

    public String getObservations() {
        return observations;
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public boolean isArchiveDocument() {
        return archiveDocument;
    }

    public boolean isPublishDocument() {
        return publishDocument;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Chargement du catalogue
     */
    public void loadCatalogue(String typeDocument) {
        // récupération du catalogue de texte
        try {
            catalogue = AFDocumentItextService.retrieveCatalogue(getSession(), langueIsoRequerant, "124001",
                    typeDocument);
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR,
                    this.getClass().getName());
            abort();
        }
    }

    /**
     * Chargement du catalogue avec un idDocuement
     */
    public void loadCatalogue(String typeDocument, String idDocument, String idDocumentDefault) {
        // récupération du catalogue de texte
        try {
            catalogue = AFDocumentItextService.retrieveCatalogue(getSession(), langueIsoRequerant, CodeSystem.DOMAINE,
                    typeDocument, idDocument, idDocumentDefault);
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR,
                    this.getClass().getName());
            abort();
        }
    }

    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = false;
        }

        return retValue;
    }

    public void setArchiveDocument(boolean archiveDocument) {
        this.archiveDocument = archiveDocument;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDateLettreReclamation(String dateLettreReclamation) {
        this.dateLettreReclamation = dateLettreReclamation;
    }

    public void setFormulePolitesse(String formulePolitesse) {
        this.formulePolitesse = formulePolitesse;
    }

    /**
     * @param tiers
     * @throws Exception
     */
    public void setHeaderAndFooter(TITiersViewBean tiers, String dateImpression) throws Exception {
        // mise en place du header
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), getLangueIsoRequerant());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setDate(dateImpression);

        _setHeader(headerBean, tiers);
        caisseReportHelper.addHeaderParameters(this, headerBean);

        // mise en place de la signature
        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
        signBean.setService2("");
        signBean.setSignataire2("");

        // on récupère la propriété "signature.nom.caisse" du
        // jasperGlobazProperties
        String caisseSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE
                + getLangueIsoRequerant().toUpperCase());
        signBean.setSignatureCaisse(caisseSignature);

        // on récupère la propriété "signature.nom.service" du
        // jasperGlobazProperties
        String serviceSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE
                + getLangueIsoRequerant().toUpperCase());
        // la méthode _replaceVars permet de remplacer les chaine de type
        // {user.service}
        serviceSignature = ACaisseReportHelper._replaceVars(serviceSignature, getSession().getUserId(), null);
        signBean.setService(serviceSignature);

        // Signature du signataire
        String signataireSignature = getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE + langueIsoRequerant.toUpperCase());
        signataireSignature = ACaisseReportHelper._replaceVars(signataireSignature, getSession().getUserId(), null);
        signBean.setSignataire(signataireSignature);

        caisseReportHelper.addSignatureParameters(this, signBean);
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIntituleLettre(String intituleLettre) {
        this.intituleLettre = intituleLettre;
    }

    public void setLangueIsoRequerant(String langueIsoRequerant) {
        this.langueIsoRequerant = langueIsoRequerant;
    }

    public void setM_container(Collection<DSLettreReclamationNssBean> m_container) {
        this.m_container = m_container;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumeroRappel(String numeroRappel) {
        this.numeroRappel = numeroRappel;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setPublishDocument(boolean publishDocument) {
        this.publishDocument = publishDocument;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
}
