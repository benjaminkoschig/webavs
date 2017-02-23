package globaz.hercule.itext.controleEmployeur;

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
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author SCO
 * @since 9 août 2010
 */
public class CEDSLettre extends FWIDocumentManager implements ILEGeneration {

    private static final long serialVersionUID = 4266553091720608519L;
    public final static String CE_DOCUMENT_D002 = "836100";
    public final static String CE_DOCUMENT_D003 = "836102";
    public final static String CE_DOCUMENT_D005 = "836101";
    public final static String CE_DOCUMENT_DELEGUER_TIERS = "836105";
    public final static String CE_DOCUMENT_LETTRE_LIBRE = "836030";
    public final static String CE_DOCUMENT_LETTRE_LIBRE_CATALOGUE = "836104";
    public final static String CE_DOCUMENT_SOMMATION = "836103";
    public final static String CE_DOMAINE = "835001";

    public final static String MODEL_NAME_COMPTE_RENDU = "HERCULE_DOCUMENT_COMPTE_RENDU";
    public final static String MODEL_NAME_D002 = "HERCULE_DOCUMENT_D002";
    public final static String MODEL_NAME_D003 = "HERCULE_DOCUMENT_D003";
    public final static String MODEL_NAME_D005 = "HERCULE_DOCUMENT_D005";
    public final static String MODEL_NAME_DELEGUER_TIERS = "HERCULE_DOCUMENT_DELEGUER_TIERS";
    public final static String MODEL_NAME_LETTRE_LIBRE = "HERCULE_LETTRE_LIBRE";
    public final static String MODEL_NAME_LETTRE_LIBRE_CATALOGUE = "HERCULE_DOCUMENT_LETTRE_LIBRE";
    public final static String MODEL_NAME_SOMMATION = "HERCULE_DOCUMENT_SOMMATION";

    private String annee;
    public ICTDocument catalogue = null;
    private String categorieMasse;
    private String dateDebutAffiliation;
    private String dateDebutControle;
    private String dateEffective;
    private String dateEnvoi;
    private String dateFinControle;
    protected String dateImpression;
    protected LEParamEnvoiDataSource docCourant;
    protected LEEnvoiDataSource documentDataSource;
    private String idEnvoiParent;
    private String idTiers;
    private String idAffiliation;
    private String langueIsoRequerant = "fr";// langue du requerant

    private String numAffilie;
    private boolean publishDocument = true;
    private String visaReviseur;

    /**
     * Constructeur de CEDSLettre
     */
    public CEDSLettre(BProcess parent, String application, String document) throws java.lang.Exception {
        super(parent, application, document);
        setParentWithCopy(parent);
    }

    /**
     * Constructeur de CEDSLettre
     */
    public CEDSLettre(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, null);
    }

    /**
     * Constructeur de CEDSLettre
     */
    public CEDSLettre(BSession session, String application, String document) throws java.lang.Exception {
        super(session, application, document);
    }

    /**
     * Permet de récupérer la date d'impression <br/>
     * Si une date d'envoi est précisé, on retourne la date d'envoi sinon on retourne la date du jour
     * 
     * @param tiers
     * @return
     */
    public String _getDateImpression(TITiersViewBean tiers) {
        String dateImpression = null;

        if (!JadeStringUtil.isEmpty(getDateEnvoi())) {
            dateImpression = JACalendar.format(getDateEnvoi(), tiers.getLangueIso());
        } else {
            dateImpression = JACalendar.format(JACalendar.todayJJsMMsAAAA(), tiers.getLangueIso());
        }

        return dateImpression;
    }

    /**
     * Permet de rechercher le nom du reviseur par rapport a son visa
     * 
     * @return
     * @throws HerculeException
     */
    public String _getNomReviseur() {
        String retour = "";

        try {
            CEReviseur reviseur = CEControleEmployeurService.retrieveReviseur(getSession(), getTransaction(),
                    getVisaReviseur());
            if (reviseur != null) {
                retour = reviseur.getNomReviseur();
            }
        } catch (Exception e) {
            retour = "";
        }

        return retour;
    }

    /**
     * @param bean
     * @param tiers
     * @throws Exception
     */
    public void _setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {
        if (tiers == null) {
            tiers = CETiersService.retrieveTiersViewBean(getSession(), getIdTiers());
        }

        bean.setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA(), getNumAffilie()));

        bean.setDate(bean.getDate());
        bean.setNoAvs(tiers.getNumAvsActuel());
        bean.setConfidentiel(true);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
        bean.setNoAffilie(getNumAffilie());

        // Renseignement du numéro ide
        AFIDEUtil.addNumeroIDEInDoc(getSession(), bean, getIdAffiliation());

    }

    /**
     * Ajout des propriétés d'entrée
     * 
     * @param csTypeProp
     * @param valeur
     */
    public void addPropriete(String csTypeProp, String valeur) {
        if (ILEConstantes.CS_PARAM_GEN_ID_TIERS.equals(csTypeProp)) {
            setIdTiers(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_PERIODE.equals(csTypeProp)) {
            setAnnee(valeur);
        } else if (ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT.equals(csTypeProp)) {
            setIdEnvoiParent(valeur);
        } else if (CEDSLettre_Param.PARAM_CATEGORIE_MASSE.equals(csTypeProp)) {
            setCategorieMasse(valeur);
        } else if (CEDSLettre_Param.PARAM_DATE_DEBUT_AFFILIATION.equals(csTypeProp)) {
            setDateDebutAffiliation(valeur);
        } else if (CEDSLettre_Param.PARAM_NUM_AFFILIATION.equals(csTypeProp)) {
            setNumAffilie(valeur);
        } else if (CEDSLettre_Param.PARAM_VISA_REVISEUR.equals(csTypeProp)) {
            setVisaReviseur(valeur);
        } else if (CEDSLettre_Param.PARAM_DEBUT_PERIODE_CONTROLE.equals(csTypeProp)) {
            setDateDebutControle(valeur);
        } else if (CEDSLettre_Param.PARAM_FIN_PERIODE_CONTROLE.equals(csTypeProp)) {
            setDateFinControle(valeur);
        } else if (CEDSLettre_Param.PARAM_DATE_EFFECTIVE.equals(csTypeProp)) {
            setDateEffective(valeur);
        }
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
    }

    /**
     * Permet de passer des informations au doc info
     * 
     * @throws Exception
     */
    public void fillDocInfo() throws Exception {
        // Numero affilié passé au docinfo
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAffilie());
        getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                CEUtils.unFormatNumeroAffilie(getSession(), getNumAffilie()));

        getDocumentInfo().setDocumentDate(
                JadeStringUtil.isEmpty(dateImpression) ? JACalendar.todayJJsMMsAAAA() : dateImpression);

        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getDocumentInfo().getDocumentProperty("numero.affilie.formatte"), getDocumentInfo()
                            .getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "afterPrintDocument()", e);
        }
    }

    public String getAnnee() {
        return annee;
    }

    public String getCategorieMasse() {
        return categorieMasse;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateEffective() {
        return dateEffective;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateFinControle() {
        return dateFinControle;
    }

    public LEEnvoiDataSource getDocumentDataSource() {
        return documentDataSource;
    }

    public String getIdEnvoiParent() {
        return idEnvoiParent;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLangueIsoRequerant() {
        return langueIsoRequerant;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @see globaz.leo.process.generation.ILEGeneration#getResult()
     */
    @Override
    public LEEnvoiDataSource getResult() {
        return documentDataSource;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getVisaReviseur() {
        return visaReviseur;
    }

    /**
     * Permet de savoir si le document doit etre publié
     * 
     * @return
     */
    public boolean isPublishDocument() {
        return publishDocument;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Chargement du catalogue
     */
    public void loadCatalogue(String typeDocument) {
        // récupération du catalogue de texte
        try {
            catalogue = CEDocumentItextService.retrieveCatalogue(getSession(), langueIsoRequerant,
                    CodeSystem.DOMAINE_CONT_EMPL, typeDocument);
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
            catalogue = CEDocumentItextService.retrieveCatalogue(getSession(), langueIsoRequerant,
                    CodeSystem.DOMAINE_CONT_EMPL, typeDocument, idDocument, idDocumentDefault);
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR,
                    this.getClass().getName());
            abort();
        }
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#next()
     */
    @Override
    public boolean next() throws FWIException {
        boolean hasNext = false;
        if (docCourant == null) {
            hasNext = true;
            docCourant = documentDataSource.getParamEnvoi();
        }
        return hasNext;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCategorieMasse(String categorieMasse) {
        this.categorieMasse = categorieMasse;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutControle(String dateDebutControle) {
        this.dateDebutControle = dateDebutControle;
    }

    public void setDateEffective(String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setDateFinControle(String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    /**
     * @see globaz.leo.process.generation.ILEGeneration#setDateImpression(java.lang.String)
     */
    @Override
    public void setDateImpression(String date) {
        dateImpression = date;
    }

    /**
     * @see globaz.leo.process.generation.ILEGeneration#setDocumentDataSource(globaz.leo.db.data.LEEnvoiDataSource)
     */
    @Override
    public void setDocumentDataSource(LEEnvoiDataSource source) {
        documentDataSource = source;
    }

    /**
     * Mise ne place du header
     * 
     * @param tiers
     * @throws Exception
     */
    public void setHeader(TITiersViewBean tiers, String dateImpression) throws Exception {
        // mise en place du header
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), getLangueIsoRequerant());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setDate(dateImpression);
        _setHeader(headerBean, tiers);
        caisseReportHelper.addHeaderParameters(this, headerBean);
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
        String serviceSignatureFinal = ACaisseReportHelper._replaceVars(serviceSignature, getSession().getUserId(),
                null);
        signBean.setService(serviceSignatureFinal);

        // Signature du signataire
        String signataireSignature = getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE + langueIsoRequerant.toUpperCase());
        String signataireSignatureFinal = ACaisseReportHelper._replaceVars(signataireSignature, getSession()
                .getUserId(), null);
        signBean.setSignataire(signataireSignatureFinal);

        caisseReportHelper.addSignatureParameters(this, signBean);
    }

    public void setIdEnvoiParent(String idEnvoiParent) {
        this.idEnvoiParent = idEnvoiParent;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLangueIsoRequerant(String langueIsoRequerant) {
        this.langueIsoRequerant = langueIsoRequerant;
    }

    /**
     * @see globaz.leo.process.generation.ILEGeneration#setNomDoc(java.lang.String)
     */
    @Override
    public void setNomDoc(String nomDoc) {
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @see globaz.leo.process.generation.ILEGeneration#setPublishDocument(boolean)
     */
    @Override
    public void setPublishDocument(boolean value) {
        publishDocument = value;
    }

    /**
     * @see globaz.leo.process.generation.ILEGeneration#setSessionModule(globaz.globall.db.BSession)
     */
    @Override
    public void setSessionModule(BSession session) throws Exception {
    }

    public void setVisaReviseur(String visaReviseur) {
        this.visaReviseur = visaReviseur;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

}
