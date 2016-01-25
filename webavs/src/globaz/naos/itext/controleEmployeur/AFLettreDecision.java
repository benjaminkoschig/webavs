package globaz.naos.itext.controleEmployeur;

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
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.db.controleEmployeur.AFImprimerDecision;
import globaz.naos.db.controleEmployeur.AFImprimerDecisionManager;
import globaz.naos.translation.CodeSystem;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AFLettreDecision extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Numéro du document
    private static final String DOC_NO = "0186CAF";
    private static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";

    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);
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

    protected String adressePrincipale;
    private boolean beanNotfull = true;
    private String codeIsoLangue = "FR";
    AFControleEmployeur controle = new AFControleEmployeur();
    private AFImprimerDecision crtLigne;

    // code système du catalogue de texte pour le domaine (même code système
    // pour tous les pdf)
    protected String cs_domaine = CodeSystem.DOMAINE_CONT_EMPL;

    // code système du catalogue de texte pour le type de document (un code
    // système par pdf)
    protected String cs_typeDocument = CodeSystem.TYPE_LETTRE_DECISION;
    protected ICTDocument document;
    private String idControle = new String();
    protected String idDocument = "";
    private String idPassage = new String();
    private String idPersRef = new String();
    // private String langue = getSession().getIdLangueISO();
    private String langue = "";
    private AFImprimerDecisionManager liste;
    AFControleEmployeurManager manager = new AFControleEmployeurManager();
    protected String nomDocument = "";
    private Boolean nonImprimable = Boolean.FALSE;
    // Champs pour le catalogue de textes
    protected int numDocument = 0;
    private String numeroFacture = new String();
    FAPassage passage = null;
    private String role = new String();

    private BSession session;
    private boolean start = true;
    private BStatement Statement;

    private String totalFacture = new String();

    public AFLettreDecision() {
        super();
    }

    public AFLettreDecision(BProcess parent) throws FWIException {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "LettreDecision");

    }

    public AFLettreDecision(BSession session) throws FWIException {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "LettreDecision");
        this.session = session;
    }

    protected void _headerText(CaisseHeaderReportBean headerBean) {

        try {

            // texte de la date
            headerBean.setDate(JACalendar.format(getDatePassage(), controle.getLangueTiers()));

            // adresse du tiers
            headerBean.setAdresse(adressePrincipale);

            headerBean.setUser(getSession().getUserInfo());
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());

            // numéro AVS
            headerBean.setNoAvs("");

            // No affilié
            headerBean.setNoAffilie(controle.getNumAffilie());

            headerBean.setNoSection(getNumeroFacture());

        } catch (Exception e) {
            getMemoryLog().logMessage("Les paramêtres de l'objet peuvent ne pas avoir été mis correctement",
                    FWMessage.AVERTISSEMENT, headerBean.getClass().getName());
            ;
        }

    }

    private void _letterBody() {
        try {
            String politesse = GetPolitesse(controle.getNumAffilie());

            StringBuffer titre = new StringBuffer("");
            StringBuffer intro = new StringBuffer("");
            StringBuffer montant = new StringBuffer("");
            StringBuffer corps = new StringBuffer("");

            titre.append(getTexte(1, 1).toString());
            intro.append(getTexte(2, 1).toString());
            montant.append(getTexte(2, 2).toString());
            corps.append(getTexte(2, 3).toString());
            corps.append(getTexte(3, 1).toString());
            corps.append(getTexte(3, 2).toString());

            this.setParametres("L_TITRE", this.formatMessage(titre, new Object[] { "", controle.getRapportNumero() }));
            this.setParametres("L_POLITESSE", politesse);
            this.setParametres(
                    "L_INTRO",
                    this.formatMessage(intro,
                            new Object[] { "", "", JACalendar.format(controle.getDateEffective(), codeIsoLangue),
                                    controle.getDateDebutControle(), controle.getDateFinControle() }));
            this.setParametres("L_MONTANT", this.formatMessage(montant, new Object[] { totalFacture }));
            this.setParametres("L_CORPS", this.formatMessage(corps, new Object[] { politesse }));
        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: " + e.getMessage());
        }

    }

    @Override
    public void afterExecuteReport() {
        super.afterExecuteReport();
        try {
            this.mergePDF(getDocumentInfo(), true, 500, false, null);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.setParametres(
                "P_SUBREPORT_FOOTER",
                getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), FWIImportProperties.SIGNATURE_FILENAME));

    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        super.setTemplateFile("NAOS_LETTRE_DECISION");
        setTailleLot(1);
        setImpressionParLot(true);
    }

    @Override
    public void createDataSource() throws Exception {

        try {
            FAPassage thePassage = FAUtil.loadPassage(getIdPassage(), getSession());
            FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), thePassage);
        } catch (Exception e) {
            JadeLogger.warn(this, "Unable to load passage : " + e.getMessage());
        }

        fillDocInfo();
        HashMap champs;
        List lignes;
        champs = new HashMap();
        lignes = new LinkedList();

        setDocumentTitle(getSession().getLabel("DECISION_CONTROLE"));

        manager.setSession(getSession());
        manager.setForControleEmployeurId(getIdControle());
        manager.find();
        if (manager.size() > 0) {
            controle = (AFControleEmployeur) manager.getFirstEntity();
        }

        try {
            adressePrincipale = controle.getAdressePrincipale(getTransaction(), controle.getDatePrevue());
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de retrouver l'adress principale, " + "du domicile pour : " + "ID="
                            + controle.getNumAffilie(), FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        setCodeIsoLangue(controle.getLangueTiers());

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());

        CaisseHeaderReportBean hb = new CaisseHeaderReportBean();

        _headerText(hb);
        _letterBody();

        caisseReportHelper.addHeaderParameters(this, hb);
        caisseReportHelper.addSignatureParameters(this);
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
            JadeLogger.error(this, e);
            out.append(AFLettreDecision.TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    protected void fillBean() {
        try {
            if (beanNotfull) {
                liste = new AFImprimerDecisionManager();
                liste.setSession(getSession());
                liste.setIdPassage(getIdPassage());
                Statement = liste.cursorOpen(getTransaction());
                beanNotfull = false;
            }
            if ((crtLigne = (AFImprimerDecision) liste.cursorReadNext(Statement)) != null) {
                setTotalFacture(crtLigne.getTotalFacture());
                setIdControle(crtLigne.getIdControle());
                setNumeroFacture(crtLigne.getNumeroFacture());
                setNonImprimable(crtLigne.getNonImprimable());
                setRole(crtLigne.getRole());
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
        getDocumentInfo().setDocumentTypeNumber(AFLettreDecision.DOC_NO);
        getDocumentInfo().setDocumentType(AFLettreDecision.DOC_NO);
    }

    protected String formatMessage(StringBuffer message, Object[] args) {
        return AFLettreDecision.formatMessage(message.toString(), args);
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

    protected String GetPolitesse(String NumAffilie) {
        String politesse = "";
        try {
            AFAffiliationManager AFF_MNG = new AFAffiliationManager();
            AFAffiliation AFF;
            AFF_MNG.setSession(getSession());
            AFF_MNG.setForAffilieNumero(NumAffilie);
            AFF_MNG.changeManagerSize(1);
            AFF_MNG.find();

            if ((AFF = (AFAffiliation) AFF_MNG.getFirstEntity()) != null) {
                politesse = AFF.getTiers().getFormulePolitesse(AFF.getTiers().getLangue());
            }
        } catch (Exception e) {
        }
        if (!JadeStringUtil.endsWith(politesse, ",")) {
            politesse += ",";
        }
        return politesse;
    }

    // méthode BABEL FIN

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

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    // méthode BABEL pour intégrer le catalogue de text
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

            // loader.setCsDestinataire();
            loader.setCodeIsoLangue(getCodeIsoLangue());
            loader.setCsDomaine(cs_domaine);
            loader.setCsTypeDocument(getTypeDocument());

            ICTDocument[] candidats = loader.load();

            document = candidats[numDocument];
        }

        return document;
    }

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
