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
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.api.ITIRole;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AFLettreLibre extends FWIDocumentManager {

    private static final long serialVersionUID = -1012522866700946013L;
    public final static String NUM_REF_INFOROM_LETTRE_LIBRE = "0187CAF";
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
    private String codeIsoLangue = "FR";
    AFControleEmployeur controle = new AFControleEmployeur();
    protected String cs_domaine = CodeSystem.DOMAINE_CONT_EMPL;
    protected String cs_typeDocument = CodeSystem.TYPE_LETTRE_LIBRE;
    private String dateEnvoi = new String();
    protected ICTDocument document;
    private String idControle = new String();
    protected String idDocument = "";
    private String idPersRef = new String();
    AFControleEmployeurManager manager = new AFControleEmployeurManager();
    protected String nomDocument = "";
    // Champs pour le catalogue de textes
    protected int numDocument = 0;

    private BSession session;

    private boolean start = true;

    private String textelibre = "";

    public AFLettreLibre() {
        super();
    }

    public AFLettreLibre(BProcess parent) throws FWIException {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "LettreLibre");
        super.setDocumentTitle(getSession().getLabel("LETTRE_LIBRE"));
        setParentWithCopy(parent);

    }

    public AFLettreLibre(BSession session) throws FWIException {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "LettreLibre");
        super.setDocumentTitle(getSession().getLabel("LETTRE_LIBRE"));
        this.session = session;
    }

    protected void _headerText(CaisseHeaderReportBean headerBean) {

        try {
            // texte de la date
            if (JadeStringUtil.isBlank(dateEnvoi)) {
                dateEnvoi = JACalendar.todayJJsMMsAAAA();
            }
            headerBean.setDate(JACalendar.format(dateEnvoi, controle.getLangueTiers()));

            // adresse du tiers
            headerBean.setAdresse(adressePrincipale);

            // numéro AVS
            headerBean.setNoAvs("");

            // No affilié
            headerBean.setNoAffilie(controle.getNumAffilie());

            headerBean.setUser(getSession().getUserInfo());
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());

        } catch (Exception e) {
            getMemoryLog().logMessage("Les paramêtres de l'objet peuvent ne pas avoir été mis correctement",
                    FWMessage.AVERTISSEMENT, headerBean.getClass().getName());
            ;
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
        super.setTemplateFile("NAOS_LETTRE_LIBRE");

    }

    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();
        setDocumentTitle(getSession().getLabel("LETTRE_LIBRE"));
        start = false;
        HashMap champs;
        List lignes;

        champs = new HashMap();
        lignes = new LinkedList();

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

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        JadeUser reviseur;
        JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();

        String politesse = GetPolitesse(controle.getNumAffilie());

        StringBuffer titre = new StringBuffer("");
        StringBuffer corps = new StringBuffer("");

        titre.append(getTexte(1, 1).toString());
        corps.append(getTextelibre());
        corps.append("\n\n");
        corps.append(getTexte(3, 1).toString());
        corps.append("\n\n");

        this.setParametres("L_TITRE", this.formatMessage(titre, new Object[] { controle.getDateDebutControle(),
                controle.getDateFinControle() }));
        this.setParametres("L_POLITESSE", politesse);
        this.setParametres("L_CORPS", this.formatMessage(corps, new Object[] { "", "", politesse }));

        _headerText(headerBean);

        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addSignatureParameters(this);

        lignes.add(champs);
        this.setDataSource(lignes);

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
            out.append(AFLettreLibre.TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    public void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber(AFLettreLibre.NUM_REF_INFOROM_LETTRE_LIBRE);
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

    protected String formatMessage(StringBuffer message, Object[] args) {
        return AFLettreLibre.formatMessage(message.toString(), args);
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
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

    public String getIdPersRef() {
        return idPersRef;
    }

    public String getNomDocument() {
        return nomDocument;
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

    protected StringBuffer getTexte(int niveau, int position) {
        StringBuffer resString = new StringBuffer("");
        try {
            ICTListeTextes listeTextes = loadCatalogue().getTextes(niveau);
        } catch (Exception e3) {
            getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                    getSession().getLabel("ERROR_GETTING_LIST_TEXT") + niveau + ":" + position);
        }
        return resString;
    }

    public String getTextelibre() {
        return textelibre;
    }

    public String getTypeDocument() {
        return cs_typeDocument;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

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

        return start;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdPersRef(String idPersRef) {
        this.idPersRef = idPersRef;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public void setTextelibre(String textelibre) {
        this.textelibre = textelibre;
    }

    public void setTypeDocument(String typeDocument) {
        cs_typeDocument = typeDocument;
    }

}
