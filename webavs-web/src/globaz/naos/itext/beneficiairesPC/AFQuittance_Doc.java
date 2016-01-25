package globaz.naos.itext.beneficiairesPC;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocator;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.bean.JadeUserDetail;
import globaz.jade.admin.user.service.JadeUserDetailService;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.util.Iterator;
import java.util.Locale;
import net.sf.jasperreports.engine.JasperPrint;

public class AFQuittance_Doc extends AFAbstractTiersDocument {

    private static final long serialVersionUID = -5184878851651115971L;
    private static final String DOC_NO_EXIST = "0170CAF";

    private static final String DOC_NO_NOUV = "0171CAF";
    private static final String DOC_NO_RET = "0172CAF";
    private static final String NOM_DOC = "QUITTANCE";
    private static final String P_ANNEXE = "P_ANNEXE";
    private static final String P_TEXTE = "P_TEXTE";

    private static final String P_TITRE = "P_TITRE";
    private static final String P_USER = "P_USER";
    private static final String P_USER_TEL = "P_USER_TEL";

    private static final String TEMPLATE_FILE_NAME = "NAOS_QUITTANCE";

    private String annee = "";

    private String dateEvaluation = "";
    private boolean hasNext = true;
    private String nombreHeures = "";
    private String nombreQuittances = "";
    private String numAffilie = "";

    private String numAvs = "";
    private TITiers tiers;
    private int typeQuittance = 1;
    private String user = "";

    public AFQuittance_Doc() throws Exception {
        super();
    }

    public AFQuittance_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFQuittance_Doc.NOM_DOC));
    }

    @Override
    public void afterBuildReport() {
        // Dans le cas d'un nouveau cas, on ajoute un verso
        if (getTypeQuittance() == 2) {
            JasperPrint verso = null;
            verso = getVerso();
            if (verso != null) {
                verso.setName("");
                super.getDocumentList().add(verso);
            }
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            // remplir l'en-tête et la signature
            ICaisseReportHelper crh = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), getLangueDestinataire());
            CaisseHeaderReportBean hb = new CaisseHeaderReportBean();

            hb.setAdresse(getTiers().getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(),
                    getNumAffilie() != null ? getNumAffilie() : null));
            hb.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), getTiers().getLangueIso()));
            hb.setEmailCollaborateur(getSession().getUserEMail());
            hb.setNoAffilie(getNumAffilie());
            hb.setNoAvs(getNumAvs());
            hb.setNomCollaborateur(getSession().getUserFullName());
            hb.setTelCollaborateur(getSession().getUserInfo().getPhone());
            hb.setUser(getSession().getUserInfo());
            crh.addHeaderParameters(this, hb);
            crh.addSignatureParameters(this);
            switch (getTypeQuittance()) {
                case 1:
                    setDocumentTitle(getSession().getLabel("QUITTANCE_CAS_EXISTANT") + " " + getNumAffilie());
                    getExporter().setExportFileName(
                            getSession().getLabel("QUITTANCE_CAS_EXISTANT") + " " + getNumAffilie());
                    break;
                case 2:
                    setDocumentTitle(getSession().getLabel("QUITTANCE_NOUVEAU_CAS") + " " + getNumAffilie());
                    getExporter().setExportFileName(
                            getSession().getLabel("QUITTANCE_NOUVEAU_CAS") + " " + getNumAffilie());
                    break;
                case 3:
                    setDocumentTitle(getSession().getLabel("QUITTANCE_RETOURNER") + " " + getNumAffilie());
                    getExporter().setExportFileName(
                            getSession().getLabel("QUITTANCE_RETOURNER") + " " + getNumAffilie());

                default:
                    break;
            }

            // renseigner le texte
            ICTDocument document = loadCatalogue();
            StringBuffer buffer = new StringBuffer();

            // ***************
            // -- LE TITRE -----------------------------------------------------------
            for (Iterator titresIter = document.getTextes(1).iterator(); titresIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }

                buffer.append(((ICTTexte) titresIter.next()).getDescription());
            }

            this.setParametres(AFQuittance_Doc.P_TITRE, buffer.toString());

            // -- LE TEXTE -------------------------------------------------------------
            switch (getTypeQuittance()) {
                case 1:
                    setTexteCas1(document, buffer);
                    break;
                case 2:
                    setTexteCas2(document, buffer);
                    break;
                case 3:
                    setTexteCas3(document, buffer);
                    break;
                default:
                    setTexteCas1(document, buffer);
                    break;
            }

            // -- L'ANNEXE --------------------------------------------------------------
            buffer.setLength(0);
            for (Iterator titresIter = document.getTextes(3).iterator(); titresIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }

                buffer.append(((ICTTexte) titresIter.next()).getDescription());
            }

            this.setParametres(AFQuittance_Doc.P_ANNEXE, buffer.toString());
            // -- USER --------------------------------------------------------------
            if (getUser().length() > 0) {
                buffer.setLength(0);
                for (Iterator titresIter = document.getTextes(4).iterator(); titresIter.hasNext();) {
                    if (buffer.length() > 0) {
                        buffer.append("\n");
                    }

                    buffer.append(((ICTTexte) titresIter.next()).getDescription());
                }
                int i = 0;
                String[] valParams = new String[2];
                // {debutAnnee}
                valParams[0] = getUserNomPrenom();
                valParams[1] = " ";
                while (buffer.indexOf("{") != -1) {
                    buffer = this.format(buffer, valParams[i++]);
                }

                this.setParametres(AFQuittance_Doc.P_USER, buffer.toString());
                this.setParametres(AFQuittance_Doc.P_USER_TEL, getUserTelephone());
            }
        } catch (Exception e) {
            abort();
            throw new FWIException("Erreur: " + e.getMessage(), e);
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFQuittance_Doc.TEMPLATE_FILE_NAME);
        setNumAvs(" ");
        // rechercher l'id tiers et le le numéro AVS
        if (JadeStringUtil.isEmpty(getIdTiers())) {
            try {
                AFAffiliationManager affM = new AFAffiliationManager();
                affM.setSession(getSession());
                affM.setForAffilieNumero(getNumAffilie());
                affM.setForTypesAffParitaires();
                affM.find(getTransaction());
                AFAffiliation aff = (AFAffiliation) affM.getFirstEntity();
                if (aff != null) {
                    setIdTiers(aff.getIdTiers());
                    setIdDestinataire(aff.getIdTiers());
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }

    }

    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();
    }

    /**
     * Après l'impression d'un document
     */
    @Override
    public void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAffilie());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            TIDocumentInfoHelper.fill(getDocumentInfo(), getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getNumAffilie(), affilieFormater.unformat(getNumAffilie()));

            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(getAnneeCourrante()));
        getDocumentInfo().setArchiveDocument(true);
        switch (getTypeQuittance()) {
            case 1:
                getDocumentInfo().setDocumentTypeNumber(AFQuittance_Doc.DOC_NO_EXIST);
                break;
            case 2:
                getDocumentInfo().setDocumentTypeNumber(AFQuittance_Doc.DOC_NO_NOUV);
                break;
            case 3:
                getDocumentInfo().setDocumentTypeNumber(AFQuittance_Doc.DOC_NO_RET);
                break;
            default:
                break;
        }
    }

    public StringBuffer format(StringBuffer paragraphe, String varTemp) {
        StringBuffer res = new StringBuffer("");
        String chaineModifiee = paragraphe.toString();
        ;
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        if ((index1 != -1) && (index2 != -1)) {
            String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
            // remplacement de la variable par sa valeur (varTemp)
            if (varTemp == "") {
                varTemp = " ";
            }
            res.append(CPToolBox.replaceString(paragraphe.toString(), chaineARemplacer, varTemp));
        } else {
            res.append(paragraphe.toString());
        }
        return res;
    }

    private String getAnneeCourrante() {
        if (JadeStringUtil.isEmpty(annee)) {
            annee = String.valueOf((JACalendar.today().getYear()));
        }
        return annee;
    }

    @Override
    public String getCategorie() {
        return ILEConstantes.CS_CATEGORIES_NOUVELLE_AFFILIATION;
    }

    public String getDateEvaluation() {
        return dateEvaluation;
    }

    @Override
    public String getDomaine() {
        return null;
    }

    /**
     * retourne la langue de l'affilie (doit être appellé ap.
     */
    private String getLangueDestinataire() throws Exception {
        String retValue = getTiers().getLangueIso().toLowerCase();

        if (Locale.FRENCH.getLanguage().equals(retValue) || Locale.GERMAN.getLanguage().equals(retValue)
                || Locale.ITALIAN.getLanguage().equals(retValue)) {
            return retValue;
        } else {
            return Locale.FRENCH.getLanguage();
        }
    }

    @Override
    public int getNbLevel() {
        return 0;
    }

    public String getNombreHeures() {
        return nombreHeures;
    }

    public String getNombreQuittances() {
        return nombreQuittances;
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFQuittance_Doc.NOM_DOC);
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumeroRappel() {
        return "";
    }

    @Override
    protected String getTemplate() {
        return AFQuittance_Doc.TEMPLATE_FILE_NAME;
    }

    private TITiers getTiers() throws Exception {
        if (tiers == null) {
            tiers = new TITiers();
            tiers.setIdTiers(getIdTiers());
            tiers.setSession(getSession());
            tiers.retrieve();
        }
        return tiers;
    }

    public int getTypeQuittance() {
        return typeQuittance;
    }

    public String getUser() {
        return user;
    }

    public String getUserNomPrenom() {
        try {
            Jade.getInstance();
            JadeUserService userService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserService();
            JadeUser user = userService.load(getUser());
            if ((user != null) && (!JadeStringUtil.isBlank(user.getFirstname()))) {
                return user.getFirstname() + " " + user.getLastname();
            } else {
                return " ";
            }
        } catch (Exception ex) {
            return " ";
        }
    }

    public String getUserTelephone() {
        Jade.getInstance();
        JadeAdminServiceLocator locator = JadeAdminServiceLocatorProvider.getLocator();
        JadeUserDetailService uds = locator.getUserDetailService();
        String key = FWSecureConstants.USER_DETAIL_PHONE; // Phone
        JadeUserDetail ud;
        try {
            ud = uds.load(getUser(), key);
            if (ud.getValue().length() > 0) {
                return ud.getValue();
            } else {
                return " ";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private JasperPrint getVerso() {
        try {
            java.util.List versoGenere = null;
            AFQuittanceVerso_Doc verso;
            verso = new AFQuittanceVerso_Doc();
            verso.setSession(getSession());
            verso.setSendCompletionMail(false);
            verso.setFileTitle("");
            verso.setTiers(getTiers());
            verso.setNumAvs(getNumAvs());
            verso.setUser(getUser());
            verso.setUserTelephone(getUserTelephone());
            verso.executeProcess();
            versoGenere = verso.getDocumentList();
            if (versoGenere.isEmpty()) {
                return null;
            } else {
                return (JasperPrint) versoGenere.get(0);
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void initDocument(String isoLangue) throws Exception {
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private ICTDocument loadCatalogue() throws Exception {
        // préparer le chargement
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

        loader.setActif(Boolean.TRUE);
        loader.setDefault(Boolean.TRUE);
        loader.setCsDomaine(CodeSystem.DOMAINE_BENEF_PC);
        switch (getTypeQuittance()) {
            case 1:
                loader.setIdDocument(CodeSystem.ID_DOCUMENT_QUITTANCE_CAS_EXISTANT);
                break;
            case 2:
                loader.setIdDocument(CodeSystem.ID_DOCUMENT_QUITTANCE_NOUVEAU_CAS);
                break;
            case 3:
                loader.setIdDocument(CodeSystem.ID_DOCUMENT_QUITTANCE_RETOURNER);
                break;
            default:
                loader.setIdDocument(CodeSystem.ID_DOCUMENT_QUITTANCE_CAS_EXISTANT);
                break;
        }
        loader.setCodeIsoLangue(getLangueDestinataire());
        // trouver le catalogue
        ICTDocument[] candidats = loader.load();

        if ((candidats == null) || (candidats.length == 0)) {
            throw new Exception("Impossible de trouver le catalogue de texte");
        }

        return candidats[0];
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = !hasNext;
        }

        return retValue;
    }

    public void setDateEvaluation(String _dateEvaluation) {
        dateEvaluation = _dateEvaluation;
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
    }

    public void setNombreHeures(String _nombreHeures) {
        nombreHeures = _nombreHeures;
    }

    public void setNombreQuittances(String nombreQuittances) {
        this.nombreQuittances = nombreQuittances;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    // Cas existant
    private void setTexteCas1(ICTDocument document, StringBuffer buffer) throws Exception {
        buffer.setLength(0);

        for (Iterator textesIter = document.getTextes(2).iterator(); textesIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n\n");
            }

            buffer.append(((ICTTexte) textesIter.next()).getDescription());
        }

        int i = 0;
        String[] valParams = new String[1];
        // {debutAnnee}
        valParams[0] = getFormulePolitesse();
        while (buffer.indexOf("{") != -1) {
            buffer = this.format(buffer, valParams[i++]);
        }

        this.setParametres(AFQuittance_Doc.P_TEXTE, buffer.toString());
    }

    // Nouveau Cas
    private void setTexteCas2(ICTDocument document, StringBuffer buffer) throws Exception {
        buffer.setLength(0);

        for (Iterator textesIter = document.getTextes(2).iterator(); textesIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n\n");
            }

            buffer.append(((ICTTexte) textesIter.next()).getDescription());
        }

        int i = 0;
        String[] valParams = new String[4];
        // {debutAnnee}
        valParams[0] = getFormulePolitesse();
        valParams[1] = getDateEvaluation();
        valParams[2] = getNombreHeures();
        valParams[3] = getFormulePolitesse();
        while (buffer.indexOf("{") != -1) {
            buffer = this.format(buffer, valParams[i++]);
        }

        this.setParametres(AFQuittance_Doc.P_TEXTE, buffer.toString());
    }

    private void setTexteCas3(ICTDocument document, StringBuffer buffer) throws Exception {
        buffer.setLength(0);

        for (Iterator textesIter = document.getTextes(2).iterator(); textesIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n\n");
            }

            buffer.append(((ICTTexte) textesIter.next()).getDescription());
        }

        int i = 0;
        String[] valParams = new String[3];
        // {debutAnnee}
        valParams[0] = getFormulePolitesse();
        valParams[1] = getNombreQuittances();
        valParams[2] = getFormulePolitesse();
        while (buffer.indexOf("{") != -1) {
            buffer = this.format(buffer, valParams[i++]);
        }

        this.setParametres(AFQuittance_Doc.P_TEXTE, buffer.toString());
    }

    public void setTypeQuittance(int typeQuittance) {
        this.typeQuittance = typeQuittance;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
