package globaz.hercule.itext.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.db.controleEmployeur.CEImprimerDecision;
import globaz.hercule.db.controleEmployeur.CEImprimerDecisionManager;
import globaz.hercule.service.CEDocumentItextService;
import globaz.hercule.service.CETiersService;
import globaz.hercule.utils.CodeSystem;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.util.Iterator;

public class CELettreDecision extends FWIDocumentManager {

    private static final long serialVersionUID = -6305609988155879346L;
    private static final String DOC_NO = "0186CCE";
    // Numéro du document
    public final static String MODEL_NAME_DECISION = "HERCULE_LETTRE_DECISION";

    // ---------------------------------------------------- BABEL
    private static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";

    protected String adressePrincipale;

    private boolean beanNotfull = true;

    private String codeIsoLangue = "FR";

    CEControleEmployeur controle = new CEControleEmployeur();
    private CEImprimerDecision crtLigne;

    // code système du catalogue de texte pour le domaine (même code système
    // pour tous les pdf)
    protected String cs_domaine = CodeSystem.DOMAINE_CONT_EMPL;
    // code système du catalogue de texte pour le type de document (un code
    // système par pdf)
    protected String cs_typeDocument = CodeSystem.TYPE_LETTRE_DECISION;

    // ---------------------------------------------------- BABEL

    protected ICTDocument document;

    private String formulePolitesse = "";
    private String idControle = new String();
    protected String idDocument = "";
    private String idPassage = new String();
    private String idPersRef = new String();
    protected String nomDocument = "";
    private Boolean nonImprimable = Boolean.FALSE;
    // Champs pour le catalogue de textes
    protected int numDocument = 0;
    private String numeroFacture = new String();
    FAPassage passage = null;
    private String referenceFacture = "";
    private String role = new String();
    private boolean start = true;

    private BStatement Statement;

    private TITiersViewBean tiers = null;

    private String totalFacture = new String();
    private boolean wantReferenceFacture = false;

    /**
     * Constructeur de CELettreDecision
     */
    public CELettreDecision() {
        super();
    }

    /**
     * Constructeur de CELettreDecision
     */
    public CELettreDecision(BProcess parent) throws FWIException {
        super(parent, CEApplication.DEFAULT_APPLICATION_HERCULE, "LettreDecision");

    }

    /**
     * Constructeur de CELettreDecision
     */
    public CELettreDecision(BSession session) throws FWIException {
        super(session, CEApplication.DEFAULT_APPLICATION_HERCULE, "LettreDecision");
    }

    protected void _headerText(CaisseHeaderReportBean headerBean, String dateImpression) {

        try {

            // texte de la date
            if (JadeStringUtil.isBlankOrZero(dateImpression)) {
                headerBean.setDate(JACalendar.format(getDatePassage(), controle.getLangueTiers()));
            } else {
                headerBean.setDate(JACalendar.format(dateImpression, controle.getLangueTiers()));
            }

            // adresse du tiers
            headerBean
                    .setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(),
                            controle.getNumAffilie()));

            headerBean.setUser(getSession().getUserInfo());
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            if (wantReferenceFacture && !JadeStringUtil.isBlank(getReferenceFacture())) {
                JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                JadeUser user = null;
                user = service.loadForVisa(getReferenceFacture());
                if (user != null) {
                    headerBean.setUser(user);
                    headerBean.setNomCollaborateur(user.getFirstname() + " " + user.getLastname());
                    headerBean.setTelCollaborateur(user.getPhone());
                }
            }

            // numéro AVS
            headerBean.setNoAvs(tiers.getNumAvsActuel());

            // No affilié
            headerBean.setNoAffilie(controle.getNumAffilie());

            headerBean.setNoSection(getNumeroFacture());

        } catch (Exception e) {
            getMemoryLog().logMessage("Les paramêtres de l'objet peuvent ne pas avoir été mis correctement",
                    FWMessage.AVERTISSEMENT, headerBean.getClass().getName());
        }

    };

    private void _letterBody() {
        try {
            StringBuffer titre = new StringBuffer("");
            StringBuffer politesse = new StringBuffer("");
            StringBuffer intro = new StringBuffer("");
            StringBuffer montant = new StringBuffer("");
            StringBuffer corps = new StringBuffer("");

            titre.append(getTexte(1, 1).toString());
            intro.append(getTexte(2, 1).toString());
            montant.append(getTexte(2, 2).toString());
            corps.append(getTexte(2, 3).toString());
            corps.append(getTexte(3, 1).toString());

            // Niveau 3.2 pas obligatoire
            try {
                ICTListeTextes listTexte = loadCatalogue().getTextes(3);
                corps.append(listTexte.getTexte(2).toString());
            } catch (Exception e) {
            }

            // Niveau politesse
            try {
                ICTListeTextes listTexte = loadCatalogue().getTextes(1);
                politesse.append(listTexte.getTexte(2).toString());
            } catch (Exception e) {
                politesse.append(formulePolitesse);
            }

            // Liste des arguments
            // {0} = Formule de pollitesse
            // {1} = date de debut d'affiliation
            // {2} = Annee courante
            // {3} = numéro d'affilié
            // {4} = date de debut de controle
            // {5} = date de fin de controle
            // {6} = date effective
            // {7} = Nom du reviseur
            // {8} = Nom du délégué
            // {9} = date et heure
            // {10}= Nom prénom
            // {11}= Numéro de rapport
            // {12}= Total de facture
            String[] listeArgument = { formulePolitesse, " ", " ", controle.getNumAffilie(),
                    controle.getDateDebutControle(), controle.getDateFinControle(),
                    JACalendar.format(controle.getDateEffective(), codeIsoLangue), " ", " ", " ", " ",
                    controle.getRapportNumero(), totalFacture };

            this.setParametres("L_TITRE", CEDocumentItextService.formatMessage(titre.toString(), listeArgument));
            this.setParametres("L_POLITESSE", CEDocumentItextService.formatMessage(politesse.toString(), listeArgument));
            this.setParametres("L_INTRO", CEDocumentItextService.formatMessage(intro.toString(), listeArgument));
            this.setParametres("L_MONTANT", CEDocumentItextService.formatMessage(montant.toString(), listeArgument));
            this.setParametres("L_CORPS", CEDocumentItextService.formatMessage(corps.toString(), listeArgument));

            // Annexe
            try {
                ICTListeTextes listTexte = loadCatalogue().getTextes(4);
                this.setParametres("L_ANNEXE",
                        CEDocumentItextService.formatMessage(listTexte.getTexte(1).toString(), listeArgument));
            } catch (Exception e) {
                this.setParametres("L_ANNEXE", " ");
            }

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: " + e.getMessage());
        }

    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#afterExecuteReport()
     */
    @Override
    public void afterExecuteReport() {
        super.afterExecuteReport();
        try {
            this.mergePDF(getDocumentInfo(), true, 500, false, null);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        super.setParametres(
                "P_SUBREPORT_FOOTER",
                getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), FWIImportProperties.SIGNATURE_FILENAME));

    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.setTemplateFile(CELettreDecision.MODEL_NAME_DECISION);

        setTailleLot(1);
        setImpressionParLot(true);
        setFileTitle(getSession().getLabel("DECISION_CONTROLE"));
        try {
            if ("true".equalsIgnoreCase(FWFindParameter.findParameter(getTransaction(), "1", "FAREFFACTU",
                    JACalendar.todayJJsMMsAAAA(), "", 0))) {
                wantReferenceFacture = true;
            }
        } catch (Exception e) {
            wantReferenceFacture = false;
        }
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CELettreDecision.DOC_NO);
        getDocumentInfo().setDocumentDate(JACalendar.todayJJsMMsAAAA());

        try {
            FAPassage thePassage = FAUtil.loadPassage(getIdPassage(), getSession());
            FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), thePassage);
        } catch (Exception e) {
            JadeLogger.warn(this, "Unable to load passage : " + e.getMessage());
        }

        fillDocInfo();

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(getSession());
        manager.setForControleEmployeurId(getIdControle());
        manager.find();
        if (manager.size() > 0) {
            controle = (CEControleEmployeur) manager.getFirstEntity();
        }

        try {

            tiers = CETiersService.retrieveTiersViewBean(getSession(), controle.getIdTiers());

            // définit le titre (Madame, Monsieur) du requérant
            formulePolitesse = tiers.getFormulePolitesse(null);

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de retrouver l'adress principale, " + "du domicile pour : " + "ID="
                            + controle.getNumAffilie(), FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        setDocumentTitle(controle.getNumAffilie() + " - " + getSession().getLabel("DECISION_CONTROLE"));

        setCodeIsoLangue(controle.getLangueTiers());

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());

        CaisseHeaderReportBean hb = new CaisseHeaderReportBean();

        // On prend la date d'inmpression du controle (Si elle est spécifiée).
        _headerText(hb, controle.getDateImpression());
        _letterBody();

        caisseReportHelper.addHeaderParameters(this, hb);
        caisseReportHelper.addSignatureParameters(this);

        this.setParametres(
                ICaisseReportHelper.PARAM_SIGNATURE_2SIGNATAIRES,
                getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE) + "   "
                        + controle.getControleurNom());
    }

    protected void dumpNiveau(int niveau, StringBuffer out, String paraSep) {
        try {
            for (Iterator paraIter = loadCatalogue().getTextes(niveau).iterator(); paraIter.hasNext();) {

                if (out.length() > 0) {
                    out.append(paraSep);
                }

                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            JadeLogger.error("", e);
            out.append(CELettreDecision.TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    protected void fillBean() {

        CEImprimerDecisionManager liste = new CEImprimerDecisionManager();

        try {
            if (beanNotfull) {

                liste.setSession(getSession());
                liste.setIdPassage(getIdPassage());
                Statement = liste.cursorOpen(getTransaction());
                beanNotfull = false;
            }
            if ((crtLigne = (CEImprimerDecision) liste.cursorReadNext(Statement)) != null) {
                setTotalFacture(crtLigne.getTotalFacture());
                setIdControle(crtLigne.getIdControle());
                setNumeroFacture(crtLigne.getNumeroFacture());
                setNonImprimable(crtLigne.getNonImprimable());
                setRole(crtLigne.getRole());
                setReferenceFacture(crtLigne.getReferenceFacture());
            } else {
                start = false;
                liste.cursorClose(Statement);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    public void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", controle.getNumAffilie());

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(controle.getNumAffilie()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), controle.getIdTiers(), getSession(), getRole(),
                    controle.getNumAffilie(), affilieFormater.unformat(controle.getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", controle.getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);

        if (getNonImprimable().booleanValue()) {
            getDocumentInfo().setSeparateDocument(true);
        } else {
            getDocumentInfo().setSeparateDocument(false);
        }
        getDocumentInfo().setDocumentTypeNumber(CELettreDecision.DOC_NO);
        getDocumentInfo().setDocumentType(CELettreDecision.DOC_NO);
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getDatePassage() {
        if (passage == null) {
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());

            try {
                passage.retrieve();
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                        getSession().getLabel("CONTEMP_PASSAGE_ERREUR") + getIdPassage());
            }
        }

        return passage.getDateFacturation();
    }

    public String getDefaultModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "defaultModel", '\\',
                    '/');
        } catch (Exception e) {
            return "";
        }
    }

    public String getIdControle() {
        return idControle;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdPersRef() {
        return idPersRef;
    }

    public String getNomDocument() {
        return nomDocument;
    }

    public Boolean getNonImprimable() {
        return nonImprimable;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getReferenceFacture() {
        return referenceFacture;
    }

    public String getRole() {
        return role;
    }

    protected StringBuffer getTexte(int niveau, int position) {
        StringBuffer resString = new StringBuffer("");
        try {

            ICTListeTextes listeTextes = loadCatalogue().getTextes(niveau);
            resString.append(listeTextes.getTexte(position));

        } catch (Exception e3) {
            getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                    getSession().getLabel("ERROR_GETTING_LIST_TEXT") + niveau + ":" + position);
        }
        return resString;
    }

    public String getTypeDocument() {
        return cs_typeDocument;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    // méthode BABEL pour intégrer le catalogue de text
    protected ICTDocument loadCatalogue() throws Exception {

        // Si le document n'est pas chargé ou que le codeisolanque n'est pas identique
        if (document == null || !document.getCodeIsoLangue().equals(codeIsoLangue)) {
            ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            loader.setActif(Boolean.TRUE);
            if (!JadeStringUtil.isBlank(getNomDocument())) {
                loader.setNom(getNomDocument());
            } else if (!JadeStringUtil.isBlank(getIdDocument())) {
                loader.setIdDocument(getIdDocument());
            } else {
                loader.setDefault(Boolean.TRUE);
            }

            // loader.setCsDestinataire();
            loader.setCodeIsoLangue(codeIsoLangue);
            loader.setCsDomaine(cs_domaine);
            loader.setCsTypeDocument(getTypeDocument());

            ICTDocument[] candidats = loader.load();

            document = candidats[numDocument];
        }

        return document;
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#next()
     */
    @Override
    public boolean next() throws FWIException {
        fillBean();
        return start;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdPersRef(String idPersRef) {
        this.idPersRef = idPersRef;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public void setNonImprimable(Boolean nonImprimable) {
        this.nonImprimable = nonImprimable;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setReferenceFacture(String referenceFacture) {
        this.referenceFacture = referenceFacture;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTotalFacture(String totalFacture) {
        this.totalFacture = totalFacture;
    }

    public void setTypeDocument(String typeDocument) {
        cs_typeDocument = typeDocument;
    }

}
