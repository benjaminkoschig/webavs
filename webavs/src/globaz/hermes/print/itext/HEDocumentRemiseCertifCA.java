package globaz.hermes.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.hermes.application.HEApplication;
import globaz.hermes.babel.HECTConstantes;
import globaz.hermes.utils.HEUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.pyxis.application.TIApplication;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HEDocumentRemiseCertifCA extends FWIDocumentManager {

    public final static String DOCUMENT_TYPE_NUMBER = "0168CCI";
    public final static String JASP_PROP_HEADER_ADRESSE_CAISSE = "header.adresse.caisse.";
    public final static String MOTIF_MOD = "MOTMOD";
    public final static String MOTIF_PERT = "MOTPERT";
    public final static String MOTIF_REM = "MOTREM";
    private static final long serialVersionUID = -7274168054511920783L;
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

    public static void main(String[] args) {
        HEDocumentRemiseCertifCA process = null;

        try {
            if (args.length < 3) {
                System.out
                        .println("java globaz.hermes.print.itext.HEDocumentRemiseCertifCA <uid> <pwd> <idLot> <email>");
                throw new Exception("Wrong number of arguments");
            }

            BSession session = new BSession(globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(HEApplication.DEFAULT_APPLICATION_HERMES).newSession(args[0], args[1]));

            process = new HEDocumentRemiseCertifCA();
            process.setSession(session);
            process.setDeleteOnExit(false);
            process.setIdLot(args[2]);
            process.setEMailAddress(args[3]);

            process.executeProcess();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private String adresse = "";
    private HEDocumentRemiseCAStruct affilie;
    private String anneeCot = "";
    private String caresp = "";

    private String codeIsoLangue = "FR";

    protected String cs_domaine = HECTConstantes.CS_HE_DOMAINE;
    protected String cs_typeDocument = "";
    private String dateNaiss = "";
    protected ICTDocument document;
    private String Id = "";
    protected String idDocument = "";
    private String idLot = "";
    private String isEmployeur = "false";

    private Iterator<HEDocumentRemiseCAStruct> iterateur;

    private String langue = "";
    private String langueSingle = "";

    private String motif = "";
    private String motif_modification = "";
    private String motif_perte = "";
    private String motif_remise = "";

    private String nAffilie = "";
    private String nnss = "";
    private String nom = "";
    protected String nomDocument = "";
    protected int numDocument = 0;
    private String politesse = "";
    private String prenom = "";
    private String service;

    private String single = "false";

    private boolean start = true;
    public List<?> tabAttest = new ArrayList<Object>();
    public List<HEDocumentRemiseCAStruct> tabCertif = new ArrayList<HEDocumentRemiseCAStruct>();

    public HEDocumentRemiseCertifCA() {
        super();
    }

    public HEDocumentRemiseCertifCA(BProcess parent) throws FWIException {
        super(parent, HEApplication.DEFAULT_APPLICATION_ROOT, "RemiseCA");

    }

    public HEDocumentRemiseCertifCA(BSession session) throws FWIException {
        super(session, HEApplication.DEFAULT_APPLICATION_ROOT, "RemiseCA");
    }

    /**
     * @return La langue du document
     */
    protected String _getLangue() {
        return langue.toUpperCase();
    }

    protected void _setLangue(String langue) {
        this.langue = langue;
    }

    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
        if (getDocumentInfo() != null) {
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);
        }
    }

    @Override
    public void afterExecuteReport() {
        super.afterExecuteReport();

        try {
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);

            String nbDocument = "100";
            int iNbDocument = 100;
            try {
                nbDocument = getSession().getApplication().getProperty("attestationCATailleLot", nbDocument);
                iNbDocument = Integer.parseInt(nbDocument);
            } catch (Exception e) {
                iNbDocument = 50; // "safe" value
                JadeLogger.warn(this,
                        "Une valeur par défaut a étée utilisée pour la taille de lot des Attestations de CA");
            }
            this.mergePDF(docInfo, false, iNbDocument, true, null, null);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        super.afterExecuteReport();

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber(HEDocumentRemiseCertifCA.DOCUMENT_TYPE_NUMBER);
            if ((affilie != null) && (affilie.getAffiliation() != null)) {
                try {
                    AFAffiliation affiliation = affilie.getAffiliation();
                    if (affiliation != null) {
                        String role = AFAffiliationUtil.getRoleParInd(affiliation);
                        IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                                AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
                        IFormatData avsFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                                TIApplication.DEFAULT_APPLICATION_PYXIS)).getAvsFormater();
                        getDocumentInfo()
                                .setDocumentProperty("numero.affilie.formatte", affiliation.getAffilieNumero());
                        getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                                affilieFormater.unformat(affiliation.getAffilieNumero()));
                        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE,
                                affiliation.getAffilieNumero());
                        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                                affilieFormater.unformat(affiliation.getAffilieNumero()));
                        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE,
                                affilie.getNnss());
                        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                                avsFormater.unformat(affilie.getNnss()));
                        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.ROLE_TIERS_DOCUMENT, role);
                    }
                } catch (Exception e) {
                    JadeCodingUtil.catchException(this, "afterPrintDocument", e);
                }
            }
        }
        // langue du stick employeur.
        String langue = "FR";
        if (!JadeStringUtil.isBlank(affilie.getLangue())) {
            langue = affilie.getLangue();
        }
        // path pour le stick employeur.
        super.setParametres("P_DEFAULT_MODEL_PATH", getModelPath() + "/CA_EMPLOYEUR_logo_" + langue.toLowerCase()
                + ".gif");
        super.setParametres(
                "P_SUBREPORT_FOOTER",
                getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), FWIImportProperties.SIGNATURE_FILENAME));
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {

            if (!start) {
                if (affilie.isEmployeur()) {
                    super.setTemplateFile(HEUtil.getModeleCAEmp());
                } else {
                    super.setTemplateFile(HEUtil.getModeleCA());
                }
            }
            setImpressionParLot(true);
            setTailleLot(0);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    public void createDataSource() throws Exception {
        beforeExecuteReport();
        pageAcc();

    }

    protected void dumpNiveau(int niveau, StringBuffer out, String paraSep) {
        try {
            for (Iterator<?> paraIter = loadCatalogue().getTextes(niveau).iterator(); paraIter.hasNext();) {

                if (out.length() > 0) {
                    out.append(paraSep);
                }

                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            out.append(HEDocumentRemiseCertifCA.TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    public void emptyspace() {
        setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ACC);
        System.out.println(getTexte(7, 1));
        String[] val = (getTexte(7, 1).toString()).split("#");

        if (val != null) {
            for (int i = 0; i < Integer.parseInt(val[1]); i++) {
                caresp += "";
            }
        } else {
            for (int i = 0; i < 30; i++) {
                caresp += "";
            }
        }

    }

    public void filldata() {
        try {
            HEDocumentRemiseCAArray data = new HEDocumentRemiseCAArray(getTransaction(), getSession());
            data.setCertif(true);
            data.setIdlot(getIdLot());
            data.setService(getService());
            data.fillList();

            iterateur = data.tabCA.iterator();
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
        }
    }

    // lorsque l'on passe un unique assuré
    public void fillsingle() {
        affilie = new HEDocumentRemiseCAStruct();
        affilie.setAdresse(getAdresse());
        affilie.setAnneeCot(getAnneeCot());
        affilie.setDateNaiss(getDateNaiss());
        affilie.setLangue(getLangueSingle());
        affilie.setMotif(getMotif());
        affilie.setNAffilie(getNAffilie());
        affilie.setNnss(getNnss());
        affilie.setNom(getNom());
        affilie.setPrenom(getPrenom());
        affilie.setPolitesse(getPolitesse());

        if (getIsEmployeur().equals("true")) {
            affilie.setEmployeur(true);
        } else {
            affilie.setEmployeur(false);
        }
        codeIsoLangue = affilie.getLangue();
        tabCertif.add(affilie);
        iterateur = tabCertif.iterator();
    }

    protected String formatMessage(StringBuffer message, Object[] args) {
        return HEDocumentRemiseCertifCA.formatMessage(message.toString(), args);
    }

    public String getAdresse() {
        return adresse;
    }

    public String getAnneeCot() {
        return anneeCot;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public String getDefaultModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "defaultModel", '\\',
                    '/');
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected String getEMailObject() {
        String documentFileTitle = (JadeStringUtil.isBlank(getFileTitle()) ? getDocumentTitle() : getFileTitle());
        StringBuffer buffer = new StringBuffer("L'impression du document '");
        buffer.append(documentFileTitle);
        if (isOnError()) {
            buffer.append("' s'est terminée en erreur");
            if (!JadeStringUtil.isBlank(getService())) {
                buffer.append("/" + getService());
            }
        } else {
            buffer.append("' s'est terminée avec succès");
            if (!JadeStringUtil.isBlank(getService())) {
                buffer.append("/" + getService());
            }
        }
        return buffer.toString();
    }

    public String getId() {
        return Id;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIsEmployeur() {
        return isEmployeur;
    }

    public String getLangueSingle() {
        return langueSingle;
    }

    public String getModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "hermesRoot\\model",
                    '\\', '/');
        } catch (Exception e) {
            return "";
        }
    }

    public String getMotif() {
        return motif;
    }

    public String getNAffilie() {
        return nAffilie;
    }

    public String getNnss() {
        return nnss;
    }

    public String getNom() {
        return nom;
    }

    public String getNomDocument() {
        return nomDocument;
    }

    public String getPolitesse() {
        return politesse;
    }

    public StringBuffer getPolitesseCorps(int position) {
        return getTexte(2, position);
    }

    public String getPrenom() {
        return prenom;
    }

    public String getService() {
        return service;
    }

    public String getSingle() {
        return single;
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

    private String getTitre() {
        String formeTitre = affilie.getPolitesse();
        if (!JadeStringUtil.isEmpty(formeTitre)) {
            FWParametersSystemCode cs = new FWParametersSystemCode();
            cs.setSession(getSession());
            cs.getCode(formeTitre);
            FWParametersUserCode codeUti = cs.getCodeUtilisateur(affilie.getCodeLangue());
            if (codeUti != null) {
                formeTitre = codeUti.getLibelle();
            }
        }
        return formeTitre;
    }

    public String getTypeDocument() {
        return cs_typeDocument;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    protected ICTDocument loadCatalogue() throws Exception {
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

        loader.setActif(Boolean.TRUE);
        if (!JadeStringUtil.isBlank(getNomDocument())) {
            loader.setNom(getNomDocument());
        } else if (!JadeStringUtil.isBlank(getIdDocument())) {
            loader.setIdDocument(getIdDocument());
        } else {
            loader.setDefault(Boolean.TRUE);
        }

        loader.setCodeIsoLangue(codeIsoLangue);
        loader.setCsDomaine(cs_domaine);
        loader.setCsTypeDocument(getTypeDocument());
        ICTDocument[] candidats = loader.load();

        document = candidats[numDocument];

        return document;
    }

    public String loadTypeText(String typemotif) {
        String temp = "";
        try {
            FWFindParameterManager param = new FWFindParameterManager();
            FWFindParameter parametre;
            param.setSession(getSession());
            param.setIdApplParametre(HEApplication.DEFAULT_APPLICATION_HERMES);
            param.setIdCleDiffere(typemotif);
            param.find(getTransaction());
            parametre = (FWFindParameter) param.getFirstEntity();
            temp = parametre.getValeurAlphaParametre();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return temp;

    }

    @Override
    public boolean next() throws FWIException {

        if (start) {
            if (getSingle().equals("true")) {
                fillsingle();
            } else {
                filldata();
            }
            emptyspace();
            // charge les motifs pour les textes depuis FWPARP
            motif_modification = loadTypeText(HEDocumentRemiseCertifCA.MOTIF_MOD);
            motif_perte = loadTypeText(HEDocumentRemiseCertifCA.MOTIF_PERT);
            motif_remise = loadTypeText(HEDocumentRemiseCertifCA.MOTIF_REM);
            start = false;
        }

        if (iterateur.hasNext()) {
            affilie = iterateur.next();
            return true;
        } else {
            return false;
        }

    }

    public void pageAcc() throws Exception {
        Map<?, ?> champs = new HashMap<Object, Object>();
        List<Map<?, ?>> lignes = new LinkedList<Map<?, ?>>();

        String sexe = affilie.getPolitesse();
        if (!JadeStringUtil.isBlank(affilie.getLangue())) {
            codeIsoLangue = affilie.getLangue();
        }

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), codeIsoLangue.toUpperCase());

        setDocumentTitle(getSession().getApplication().getLabel("HERMES_10059", codeIsoLangue) + " "
                + affilie.getNnss());
        StringBuffer add = new StringBuffer("");

        add.append(getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + JadeStringUtil.toUpperCase(codeIsoLangue)));
        add.append("\n");
        add.append(getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_ADRESSE_CAISSE
                + JadeStringUtil.toUpperCase(codeIsoLangue)));
        add.append("\n\n\n");

        add.append(JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue) + " ");

        // On remplit le fichier Itext avec la structure
        this.setParametres("P_ADRESSE_CAISSE", add.toString().replaceAll(", ", "\n"));

        String formatAdresse = affilie.getAdresse();

        if (affilie.isRentier()) {
            String titre = getTitre();
            if (!JadeStringUtil.isEmpty(titre) && !JadeStringUtil.contains(affilie.getAdresse(), titre)) {
                formatAdresse = titre + "\n" + affilie.getAdresse();
            } else {
                formatAdresse = affilie.getAdresse();
            }
        }

        this.setParametres("P_ADRESSE_ASSURE", formatAdresse);

        this.setParametres(
                "P_DATE",
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE
                        + JadeStringUtil.toUpperCase(codeIsoLangue))
                        + " " + JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue));

        StringBuffer titre_pdf = new StringBuffer("");
        dumpNiveau(1, titre_pdf, "");

        if (!JadeStringUtil.isBlankOrZero(affilie.getNumeroSuccursale())) {
            if (!JadeStringUtil.isBlankOrZero(affilie.getNumeroEmploye())) {
                this.setParametres("P_REF", getSession().getApplication().getLabel("REFERENCE", affilie.getLangue())
                        + affilie.getNumeroSuccursale() + "/" + affilie.getNumeroEmploye());
            } else {
                this.setParametres("P_REF", getSession().getApplication().getLabel("REFERENCE", affilie.getLangue())
                        + affilie.getNumeroSuccursale());
            }
        } else {
            if (!JadeStringUtil.isBlankOrZero(affilie.getNumeroEmploye())) {
                this.setParametres("P_REF", getSession().getApplication().getLabel("REFERENCE", affilie.getLangue())
                        + "- /" + affilie.getNumeroEmploye());
            } else {
                // no succursale et no employe sont vide --> pas de mention de
                // la référence sur le document
                this.setParametres("P_REF", "");
            }

        }

        this.setParametres("P_TEXT_INTRO", this.formatMessage(titre_pdf, new Object[] { "", "", affilie.getNnss() }));

        StringBuffer politesseCorps = null;

        if (!JadeStringUtil.isEmpty(affilie.getFormulePolitesseSpecifique())) {
            politesseCorps = new StringBuffer(affilie.getFormulePolitesseSpecifique());

        } else if (JadeStringUtil.isEmpty(affilie.getPolitesse())) {
            // si l'affilie est un employeur, on prend le niveau 2 du CT
            // (Madame, Monsieur)
            politesseCorps = getPolitesseCorps(2);

        } else {
            // si ce n'est pas un employeur, on prend le niveau 1 du CT ({0}) et
            // on forme
            // avec la forme politesse trouvee...
            if (affilie.isRentier()) {
                politesseCorps = new StringBuffer(this.formatMessage(getPolitesseCorps(1), new Object[] { getTitre() }));
            } else {
                politesseCorps = new StringBuffer(this.formatMessage(getPolitesseCorps(1),
                        new Object[] { affilie.getPolitesse() }));
            }
        }

        this.setParametres("FORM_POLITESSE", politesseCorps.toString());

        StringBuffer corps = new StringBuffer("");

        if (motif_perte.indexOf(affilie.getMotif()) != -1) {
            corps.append(getTexte(3, 3).toString());
        } else if (motif_modification.indexOf(affilie.getMotif()) != -1) {
            corps.append(getTexte(3, 2).toString());
        } else {
            corps.append(getTexte(3, 1).toString());
        }
        if (motif_remise.indexOf(affilie.getMotif()) != -1) {
            corps.append(getTexte(4, 1).toString());
        }
        corps.append(getTexte(5, 1).toString());
        corps.append(getTexte(5, 2).toString());
        if (motif_remise.indexOf(affilie.getMotif()) != -1) {
            corps.append(getTexte(4, 2).toString());
        }
        corps.append(getTexte(6, 1).toString());
        String inscrit = "";
        if (sexe.equals("Madame")) {
            inscrit = "e";
        }
        this.setParametres("P_TEXT_CORPS",
                this.formatMessage(corps, new Object[] { politesseCorps, "\n", "", inscrit }));

        String nomPrenom = affilie.getNom() + affilie.getPrenom();
        if (JadeStringUtil.isEmpty(nomPrenom) || (nomPrenom.length() > 40)) {
            JadeSmtpClient.getInstance().sendMail(
                    getEMailAddress(),
                    FWMessageFormat.format(getSession().getLabel("MAIL_SUBJECT_ERROR_IMPRESSION_CERTIFICAT_ASSURANCE"),
                            affilie.getNnss()),
                    getSession().getLabel("MAIL_BODY_ERROR_IMPRESSION_CERTIFICAT_ASSURANCE"), null);
        }

        this.setParametres("P_NOM", affilie.getNom());
        this.setParametres("P_PRENOM", affilie.getPrenom());

        this.setParametres("P_DATE_NAISS", affilie.getDateNaiss());
        this.setParametres("P_NUM_AVS", affilie.getNnss());

        this.setParametres("P_SPACE", caresp);

        this.setParametres("P_SIGNATURE", getTexte(6, 2).toString());

        // ALD : bz 1701
        StringBuffer refCA = new StringBuffer(affilie.getUser());
        refCA.append("/");
        refCA.append(affilie.getReference());
        refCA.append("/");
        refCA.append(affilie.getDateEnregistrement());
        if (!JadeStringUtil.isBlank(getService())) {
            this.setParametres("P_REFERENCE", refCA.toString() + "/" + getService());
        } else {
            this.setParametres("P_REFERENCE", refCA.toString());
        }

        // **********************
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        caisseReportHelper.addHeaderParameters(this, headerBean);

        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
        signBean.setService2("");
        signBean.setSignataire2("");
        signBean.setService(getSession().getLabel("MSG_SERVICE_NOM"));
        signBean.setSignataire(getSession().getUserFullName());
        signBean.setSignatureCaisse(getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + codeIsoLangue.toUpperCase()));
        caisseReportHelper.addSignatureParameters(this, signBean);
        lignes.add(champs);
        this.setDataSource(lignes);
        document = null;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAnneeCot(String anneeCot) {
        this.anneeCot = anneeCot;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIsEmployeur(String isEmployeur) {
        this.isEmployeur = isEmployeur;
    }

    public void setLangueSingle(String langueSingle) {
        this.langueSingle = langueSingle;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNAffilie(String affilie) {
        nAffilie = affilie;
    }

    public void setNnss(String nnss) {
        this.nnss = nnss;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public void setPolitesse(String politesse) {
        this.politesse = politesse;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setService(String newService) {
        service = newService;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public void setTypeDocument(String typeDocument) {
        cs_typeDocument = typeDocument;
    }
}
