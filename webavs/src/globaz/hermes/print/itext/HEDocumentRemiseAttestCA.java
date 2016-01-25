package globaz.hermes.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.hermes.application.HEApplication;
import globaz.hermes.babel.HECTConstantes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HEDocumentRemiseAttestCA extends FWIDocumentManager {

    private static final long serialVersionUID = -2436059291915490606L;
    public final static String DOCUMENT_TYPE_NUMBER = "0167CCI";
    public final static String JASP_PROP_HEADER_ADRESSE_CAISSE = "header.adresse.caisse.";
    public final static String SERVICE_PROPERTIES = "service";
    private static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";
    public final static String TYPE_AFFILI_EMPLOY = "804002";
    public final static String TYPE_AFFILI_EMPLOY_D_F = "804012";
    public final static String TYPE_AFFILI_INDEP = "804001";
    public final static String TYPE_AFFILI_INDEP_EMPLOY = "804005";
    public final static String TYPE_AFFILI_NON_ACTIF = "804004";
    public final static String TYPE_AFFILI_PROVIS = "804007";
    public final static String TYPE_AFFILI_SELON_ART_1A = "804006";
    public final static String TYPE_AFFILI_TSE = "804008";
    public final static String TYPE_AFFILI_TSE_VOLONTAIRE = "804011";

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

    private String adresse = "";
    private HEDocumentRemiseCAStruct affilie;
    private String anneeCot = "";
    private String codeIsoLangue = "FR";

    protected String cs_domaine = HECTConstantes.CS_HE_DOMAINE;

    protected String cs_typeDocument = "";
    private String dateNaiss = "";
    protected ICTDocument document;
    private String Id = "";

    protected String idDocument = "";
    private String idLot = "";

    private boolean isEmployeur = false;
    private boolean isEmpty = false;
    private boolean isIndé = false;
    private boolean isRentier = false;
    private Iterator iterateur;
    private String langue = "";
    private String langueSingle = "";
    private String motif = "";

    private String nAffilie = "";
    private String nnss = "";
    private String noEmploye = "";
    private String nom = "";

    protected String nomDocument = "";
    private String noSuccursale = "";
    protected int numDocument = 0;
    private String politesse = "";
    private String prenom = "";

    private String service = new String();

    private String single = "false";

    private boolean start = true;
    public List tabAttest = new ArrayList();
    public List tabCertif = new ArrayList();

    public HEDocumentRemiseAttestCA() {
        super();
    }

    public HEDocumentRemiseAttestCA(BProcess parent) throws FWIException {
        super(parent, HEApplication.DEFAULT_APPLICATION_ROOT, "RemiseCA");

    }

    public HEDocumentRemiseAttestCA(BSession session) throws FWIException {
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
                nbDocument = getSession().getApplication().getProperty("certificatTailleLot", nbDocument);
                iNbDocument = Integer.parseInt(nbDocument);
            } catch (Exception e) {
                iNbDocument = 50; // "safe" value
                JadeLogger.warn(this,
                        "Une valeur par défaut a étée utilisée pour la taille de lot des certificats d'assuré");
            }
            this.mergePDF(docInfo, false, iNbDocument, true, null, null);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        super.afterExecuteReport();

    }

    @Override
    public void beforeBuildReport() throws FWIException {

        setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ATTEST);
        super.setParametres(
                "P_SUBREPORT_FOOTER",
                getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), FWIImportProperties.SIGNATURE_FILENAME));
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber(HEDocumentRemiseAttestCA.DOCUMENT_TYPE_NUMBER);
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
        try {
            setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ATTEST);
            super.setTemplateFile("HERMES_LETTRE_REMISE_CA_ATTEST");
            setImpressionParLot(true);
            setTailleLot(0);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    public void createDataSource() throws Exception {
        beforeExecuteReport();
        pageAttest();
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
            out.append(HEDocumentRemiseAttestCA.TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    public void filldata() {
        try {
            HEDocumentRemiseCAArray data = new HEDocumentRemiseCAArray(getTransaction(), getSession());
            data.setNoEmploye(noEmploye);
            data.setNoSuccursale(noSuccursale);
            data.setAttest(true);
            data.setIdlot(getIdLot());
            data.setService(getService());
            data.fillList();
            iterateur = data.tabCA.iterator();
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
        }

    }

    public void fillsingle() {
        affilie = new HEDocumentRemiseCAStruct();
        affilie.setAdresse(getAdresse());
        affilie.setAnneeCot(getAnneeCot());
        affilie.setDateNaiss(getDateNaiss());
        affilie.setLangue(getLangueSingle());
        affilie.setMotif(getMotif());
        affilie.setNAffilie(getNAffilie());
        try {
            affilie.setAffiliation(getAffiliation(getNAffilie()));
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        affilie.setNnss(getNnss());
        affilie.setNom(getNom());
        affilie.setPrenom(getPrenom());
        affilie.setPolitesse(getPolitesse());
        affilie.setNumeroEmploye(noEmploye);
        affilie.setNumeroSuccursale(noSuccursale);
        codeIsoLangue = affilie.getLangue();
        tabCertif.add(affilie);
        iterateur = tabCertif.iterator();
    }

    // méthode BABEL pour intégrer le catalogue de text

    protected String formatMessage(StringBuffer message, Object[] args) {
        return HEDocumentRemiseAttestCA.formatMessage(message.toString(), args);
    }

    public String getAdresse() {
        return adresse;
    }

    public AFAffiliation getAffiliation(String noAffilie) throws Exception {
        AFAffiliationManager AffManager = new AFAffiliationManager();
        AFAffiliation crtAff;
        AffManager.setSession(getSession());
        AffManager.setForAffilieNumero(noAffilie);
        AffManager.changeManagerSize(1);
        AffManager.find();

        if ((crtAff = (AFAffiliation) AffManager.getFirstEntity()) != null) {
            return crtAff;
        }
        return null;

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

    public String getNoEmploye() {
        return noEmploye;
    }

    public String getNom() {
        return nom;
    }

    public String getNomDocument() {
        return nomDocument;
    }

    public String getNoSuccursale() {
        return noSuccursale;
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

    public String getTypeDocument() {
        return cs_typeDocument;
    }

    public boolean isEmployeur() {
        return isEmployeur;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isIndé() {
        return isIndé;
    }

    public boolean isRentier() {
        return isRentier;
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

    /**
     * Méthode qui indique si on doit imprimer l'attest
     * 
     * @return si on doit imprimer l'attest
     */
    private boolean needAttest() {
        // c'est un indépendant --> pas d'attestation
        if (affilie.isInde()) {
            return false;
        }
        // c'est un rentier --> pas d'attestation
        if (affilie.isRentier()) {
            return false;
        } else {
            try {
                // Pour le reste on test quand même que le salarié n'est pas l'affilié
                AFAffiliation af = affilie.getAffiliation();
                if (NSUtil.unFormatAVS(af.getTiers().getNumAvsActuel()).equals(NSUtil.unFormatAVS(affilie.getNnss()))) {
                    // pas d'attest
                    return false;
                } else {
                    // une attest
                    return true;
                }
            } catch (Exception e) {
                // fix 1-11-1 si pas de tiers...
                return false;
            }
        }
    }

    @Override
    public boolean next() throws FWIException {

        if (start) {
            if (getSingle().equals("true")) {
                fillsingle();
            } else {
                filldata();
            }
            start = false;
        }
        // Modifs jmc, si pas d'affilié ou si catégorie indépendant
        if (iterateur.hasNext()) {
            affilie = (HEDocumentRemiseCAStruct) iterateur.next();
            while (!needAttest() && !"true".equals(getSingle())) {
                if (iterateur.hasNext()) {
                    affilie = (HEDocumentRemiseCAStruct) iterateur.next();
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public void pageAttest() throws Exception {
        Map champs = new HashMap();
        List lignes = new LinkedList();

        String formatAdresse = affilie.getAdresse();

        if (affilie.isRentier()) {
            if (!JadeStringUtil.contains(affilie.getAdresse(), affilie.getPolitesse())) {
                formatAdresse = affilie.getPolitesse() + "\n" + affilie.getAdresse();
            } else {
                formatAdresse = affilie.getPolitesse();
            }
        }
        // Fix JMC, je reprends la langue de l'affilie, s'il n'est pas nul, pour
        // éviter les autres.
        if (!JadeStringUtil.isEmpty(affilie.getLangue())) {
            codeIsoLangue = affilie.getLangue();
        } else if ((null != affilie.getAffiliation()) && !"true".equals(single)) {
            codeIsoLangue = affilie.getAffiliation().getTiers().getLangueIso();
        }
        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
        crBean.setUser(getSession().getUserInfo());
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), codeIsoLangue);

        crBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue));
        crBean.setAdresse(formatAdresse);

        setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ATTEST);

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

        this.setParametres("P_TEXT_TITRE", titre_pdf.toString());

        caisseReportHelper.addHeaderParameters(this, crBean);

        setDocumentTitle(getSession().getApplication().getLabel("HERMES_10058", codeIsoLangue) + " "
                + affilie.getNnss());

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
            politesseCorps = new StringBuffer(this.formatMessage(getPolitesseCorps(1),
                    new Object[] { affilie.getPolitesse() }));
        }

        if (!JadeStringUtil.isBlank(politesseCorps.toString())) {
            this.setParametres("P_FORM_POLITESSE", politesseCorps.toString());
        } else {
            this.setParametres("P_FORM_POLITESSE", " ");
        }

        StringBuffer corps2 = new StringBuffer("");
        corps2.append(getTexte(3, 1).toString());
        corps2.append(getTexte(4, 1).toString());
        corps2.append(getTexte(5, 1).toString());

        String adr = "";
        if (affilie.isRentier()) {
            adr = affilie.getAdresse().replaceAll("\r\n", ", ");
            if (adr.length() > 0) {
                adr = adr.substring(0, (adr.length() - 2));
            }
        } else {
            // Modif jmc 1.5.1 Adresse exploitation => si vide domicile
            // S120411_004 - Domaine Cotisation
            // Modification du domaine standard en domain CI_ARC

            adr = affilie
                    .getAffiliation()
                    .getTiers()
                    .getAdresseAsString(getDocumentInfo(), HEAdresseFormaterForAttest.CS_ADRESSE_EXPLOITATION,
                            IConstantes.CS_APPLICATION_DEFAUT, affilie.getAffiliation().getAffilieNumero(),
                            JACalendar.todayJJsMMsAAAA(), new HEAdresseFormaterForAttest(), false, null);
            if (JadeStringUtil.isBlank(adr)) {
                adr = affilie
                        .getAffiliation()
                        .getTiers()
                        .getAdresseAsString(getDocumentInfo(), HEAdresseFormaterForAttest.CS_ADRESSE_EXPLOITATION,
                                IConstantes.CS_APPLICATION_DEFAUT, "", JACalendar.todayJJsMMsAAAA(),
                                new HEAdresseFormaterForAttest(), false, null);
            }
            if (JadeStringUtil.isBlank(adr)) {
                adr = affilie
                        .getAffiliation()
                        .getTiers()
                        .getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                                IConstantes.CS_APPLICATION_DEFAUT, "", JACalendar.todayJJsMMsAAAA(),
                                new HEAdresseFormaterForAttest(), false, null);
            }
            if (JadeStringUtil.isBlank(adr)) {
                adr = affilie
                        .getAffiliation()
                        .getTiers()
                        .getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                HEApplication.CS_DOMAINE_ADRESSE_CI_ARC, affilie.getAffiliation().getAffilieNumero(),
                                JACalendar.todayJJsMMsAAAA(), new HEAdresseFormaterForAttest(), true, null);
            }
        }

        String inscrit = "";
        if (affilie.getPolitesse().equals("Madame")) {
            inscrit = "e";
        }

        String decompte = "";
        if (!JadeStringUtil.isBlank(affilie.getNAffilie())) {
            decompte = "\n\n" + getSession().getApplication().getLabel("HERMES_10056", codeIsoLangue)
                    + affilie.getNAffilie();
        }
        if (JadeStringUtil.isBlankOrZero(affilie.getDateEnregistrement())) {
            affilie.setDateEnregistrement(getAnneeCot());

        }

        String nomPrenom = affilie.getNom() + affilie.getPrenom();
        if (JadeStringUtil.isEmpty(nomPrenom) || (nomPrenom.length() > 40)) {
            JadeSmtpClient.getInstance().sendMail(
                    getEMailAddress(),
                    FWMessageFormat.format(
                            getSession().getLabel("MAIL_SUBJECT_ERROR_IMPRESSION_ATTESTATION_ASSURANCE"),
                            affilie.getNnss()),
                    getSession().getLabel("MAIL_BODY_ERROR_IMPRESSION_ATTESTATION_ASSURANCE"), null);
        }

        this.setParametres(
                "P_TEXT_CORPS",
                this.formatMessage(corps2,
                        new Object[] { politesseCorps.toString(), inscrit, affilie.getNom(), affilie.getPrenom(),
                                affilie.getDateNaiss(), " " + affilie.getNnss(), " " + affilie.getDateEnregistrement(),
                                adr, decompte }));
        this.setParametres("P_SIGNATURE", getTexte(6, 1).toString());
        this.setParametres("P_PATH_LOGO", getModelPath());

        if (!JadeStringUtil.isBlank(getService())) {
            getDocumentInfo().setPublishProperty(HEDocumentRemiseAttestCA.SERVICE_PROPERTIES, getService());
        }

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

    public void setEmployeur(boolean isEmployeur) {
        this.isEmployeur = isEmployeur;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
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

    public void setIndé(boolean isIndé) {
        this.isIndé = isIndé;
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

    public void setNoEmploye(String noEmploye) {
        this.noEmploye = noEmploye;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public void setNoSuccursale(String noSuccursale) {
        this.noSuccursale = noSuccursale;
    }

    public void setPolitesse(String politesse) {
        this.politesse = politesse;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRentier(boolean isRentier) {
        this.isRentier = isRentier;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public void setTypeDocument(String typeDocument) {
        cs_typeDocument = typeDocument;
    }

}
