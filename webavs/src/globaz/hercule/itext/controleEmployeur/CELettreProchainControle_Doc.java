package globaz.hercule.itext.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.hercule.utils.CodeSystem;
import globaz.naos.application.AFApplication;
import globaz.naos.util.AFUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;

/**
 * 
 * @since (26.02.2003 16:54:19)
 * @author: Administrator
 * @revision SCO
 */
public class CELettreProchainControle_Doc extends FWIDocumentManager {

    private static final long serialVersionUID = -7837951415855127384L;
    public final static String NUM_REF_INFOROM_LETTRE_PROCHAIN_CONTROLE = "0192CCE";
    public final static String PROP_SIGN_NOM_CAISSE = "signature.nom.caisse.";
    protected final static String TEMPLATE_FILENAME = "HERCULE_LETTRE_PROCHAIN_CONTROLE";

    public static String getTemplateFilename() {
        return CELettreProchainControle_Doc.TEMPLATE_FILENAME;
    }

    public ICTDocument catalogue = null;
    private CEControleEmployeur controle;
    private String dateHeure = "";
    private String formulePolitesse = "";
    // private TITiers tiers = null;
    private String idControleEmployeur = new String();
    private String idDocument = "";
    protected String idDocumentDefaut = "";
    private String langueIsoRequerant = "fr";// langue du requerant
    private String reviseur = "";
    private boolean start = true;
    private TITiersViewBean tiers = null;

    /**
     * Constructeur de CELettreProchainControle_Doc
     */
    public CELettreProchainControle_Doc() throws Exception {
        this(new BSession(CEApplication.DEFAULT_APPLICATION_NAOS));
    }

    /**
     * Constructeur de CELettreProchainControle_Doc
     */
    public CELettreProchainControle_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, "LETTREPROCHAINCONTROLE");
        super.setDocumentTitle(getSession().getLabel("OPTIONS_LETTRE_PROCHAIN_CONTROLE"));
        setParentWithCopy(parent);

    }

    /**
     * Constructeur de CELettreProchainControle_Doc
     */
    public CELettreProchainControle_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, "LETTREPROCHAINCONTROLE");
        super.setDocumentTitle(getSession().getLabel("OPTIONS_LETTRE_PROCHAIN_CONTROLE"));
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#_executeCleanUp()
     */
    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    public void _setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {
        bean.setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), controle.getNumAffilie()));

        bean.setDate(bean.getDate());
        bean.setNoAvs(tiers.getNumAvsActuel());
        bean.setConfidentiel(true);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
        bean.setNoAffilie(controle.getNumAffilie());
    }

    @Override
    public void afterPrintDocument() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", controle.getNumAffilie());

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(controle.getNumAffilie()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), controle.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    controle.getNumAffilie(), affilieFormater.unformat(controle.getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", controle.getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
    }

    /**
     * Retourne la décision ou null en cas d'exception
     */
    @Override
    public void beforeBuildReport() {
        // super.setSendMailOnError(true);
        super.setDocumentTitle(controle.getNumAffilie() + " - " + controle.getNomTiers());
        getDocumentInfo().setDocumentTypeNumber(CELettreProchainControle_Doc.NUM_REF_INFOROM_LETTRE_PROCHAIN_CONTROLE);

    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTemplateFile(CELettreProchainControle_Doc.TEMPLATE_FILENAME);
        setTailleLot(500);

        controle = new CEControleEmployeur();
        controle.setSession(getSession());
        controle.setIdControleEmployeur(getIdControleEmployeur());
        try {
            controle.retrieve();
        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());

            } finally {
            }
        } finally {
        }

        // récupération du requerant en cours et du tiers correspondant

        try {
            tiers = CETiersService.retrieveTiersViewBean(getSession(), controle.getIdTiers());

            // définit le titre (Madame, Monsieur) du requérant
            formulePolitesse = tiers.getFormulePolitesse(null);

        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        // récupération de la langue du tiers
        setLangueIsoRequerant(AFUtil.toLangueIso(tiers.getLangue()));

        // Chargement du catalogue
        loadCatalogue(CodeSystem.TYPE_LETTRE_PROCHAIN_CONTROLE);
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        setDocumentTitle(controle.getNumAffilie() + " - " + controle.getNomTiers());
        getDocumentInfo().setDocumentTypeNumber(CELettreProchainControle_Doc.NUM_REF_INFOROM_LETTRE_PROCHAIN_CONTROLE);
        getDocumentInfo().setDocumentDate(JACalendar.todayJJsMMsAAAA());

        start = false;

        // Liste des arguments
        // {0} = Formule de pollitesse
        // {1} = date de debut d'affiliation
        // {2} = Annee courante
        // {3} = numéro d'affilié
        // {4} = date de debut de controle
        // {5} = date de fin de controle
        // {6} = date effective
        // {7} = Nom du reviseur
        // {8} = date et heure
        String[] listeArgument = { formulePolitesse, " ", " ", controle.getNumAffilie(),
                controle.getDateDebutControle(), controle.getDateFinControle(), controle.getDateEffective(),
                getReviseur(), " ", getDateHeure() };

        // Mise en place du texte
        this.setParametres(CELettreProchainControle_Param.PARAM_TITLE,
                CEDocumentItextService.getTexte(catalogue, 1, listeArgument));
        this.setParametres(CELettreProchainControle_Param.PARAM_TITLE2,
                CEDocumentItextService.getTexte(catalogue, 2, listeArgument));
        this.setParametres(CELettreProchainControle_Param.PARAM_TEXTTOP,
                CEDocumentItextService.getTexte(catalogue, 3, listeArgument));
        this.setParametres(CELettreProchainControle_Param.PARAM_TEXTTOP1,
                CEDocumentItextService.getTexte(catalogue, 4, listeArgument));
        this.setParametres(CELettreProchainControle_Param.PARAM_TEXTTOP2,
                CEDocumentItextService.getTexte(catalogue, 5, listeArgument));
        this.setParametres(CELettreProchainControle_Param.PARAM_TEXTBOTTOM,
                CEDocumentItextService.getTexte(catalogue, 6, listeArgument));

        // Mise en place du header et du footer
        setHeaderAndFooter(tiers, JACalendar.format(JACalendar.todayJJsMMsAAAA(), tiers.getLangueIso()));

    }

    public ICTDocument getCatalogue() {
        return catalogue;
    }

    public String getDateHeure() {
        return dateHeure;
    }

    public String getIdControleEmployeur() {
        return idControleEmployeur;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDocumentDefaut() {
        return idDocumentDefaut;
    }

    public String getLangueIsoRequerant() {
        return langueIsoRequerant;
    }

    public String getReviseur() {
        return reviseur;
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
                    CodeSystem.DOMAINE_CONT_EMPL, typeDocument, getIdDocument(), getIdDocumentDefaut());
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR,
                    this.getClass().getName());
            abort();
        }
    }

    @Override
    public boolean next() throws FWIException {
        return start;
    }

    public void setCatalogue(ICTDocument catalogue) {
        this.catalogue = catalogue;
    }

    public void setDateHeure(String dateHeure) {
        this.dateHeure = dateHeure;
    }

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
                ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE + getLangueIsoRequerant().toUpperCase());
        String signataireSignatureFinal = ACaisseReportHelper._replaceVars(signataireSignature, getSession()
                .getUserId(), null);
        signBean.setSignataire(signataireSignatureFinal);

        caisseReportHelper.addSignatureParameters(this, signBean);
    }

    public void setIdControleEmployeur(String idControleEmployeur) {
        this.idControleEmployeur = idControleEmployeur;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdDocumentDefaut(String idDocumentDefaut) {
        this.idDocumentDefaut = idDocumentDefaut;
    }

    public void setLangueIsoRequerant(String langueIsoRequerant) {
        this.langueIsoRequerant = langueIsoRequerant;
    }

    public void setReviseur(String reviseur) {
        this.reviseur = reviseur;
    }

}
