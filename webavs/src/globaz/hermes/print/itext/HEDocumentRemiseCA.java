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
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.hermes.application.HEApplication;
import globaz.hermes.babel.HECTConstantes;
import globaz.hermes.utils.HEUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
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

public class HEDocumentRemiseCA extends FWIDocumentManager {

    private static final long serialVersionUID = -836985335859410848L;
    public final static String JASP_PROP_HEADER_ADRESSE_CAISSE = "header.adresse.caisse.";
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

    private HEDocumentRemiseCAStruct affilie;
    private String codeIsoLangue = "FR";

    protected String cs_domaine = HECTConstantes.CS_HE_DOMAINE;

    protected String cs_typeDocument = "";

    protected ICTDocument document;
    protected String idDocument = "";
    private String idLot = "";
    private boolean interA = true;
    private boolean interB = true;

    private boolean intro = false;
    private Iterator iterateurCertif;
    private String langue = "";
    protected String nomDocument = "";

    protected int numDocument = 0;

    private boolean start = true;
    public ArrayList tabAttest = new ArrayList();

    public ArrayList tabCertif = new ArrayList();

    public HEDocumentRemiseCA() {
        super();
    }

    public HEDocumentRemiseCA(BProcess parent) throws FWIException {
        super(parent, HEApplication.DEFAULT_APPLICATION_ROOT, "RemiseCA");

    }

    public HEDocumentRemiseCA(BSession session) throws FWIException {
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
    protected void _validate() throws Exception {

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber("0168CCI");
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
        if (intro) {
            super.setParametres(
                    "P_SUBREPORT_FOOTER",
                    getDefaultModelPath() + "/"
                            + getTemplateProperty(getDocumentInfo(), FWIImportProperties.SIGNATURE_FILENAME));
            setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ATTEST);
        } else {
            setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ACC);
            // path pour le stick employeur.
            super.setParametres("P_DEFAULT_MODEL_PATH", getModelPath());
            super.setParametres(
                    "P_SUBREPORT_FOOTER",
                    getDefaultModelPath() + "/"
                            + getTemplateProperty(getDocumentInfo(), FWIImportProperties.SIGNATURE_FILENAME));
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            if (intro) {
                setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ACC);
                if (affilie.isEmployeur()) {
                    super.setTemplateFile(HEUtil.getModeleCAEmp());
                } else {
                    super.setTemplateFile(HEUtil.getModeleCA());
                }
            } else {
                setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ATTEST);
                super.setTemplateFile("HERMES_LETTRE_REMISE_CA_ATTEST");
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

    }

    public boolean checkAVS(String AVS) {
        HEDocumentRemiseCAStruct aff;
        AVS = globaz.commons.nss.NSUtil.formatAVSNewNum(AVS);

        for (int i = 0; i < tabCertif.size(); i++) {
            aff = (HEDocumentRemiseCAStruct) tabCertif.get(i);
            if (aff.getNnss().equals(AVS)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createDataSource() throws Exception {
        beforeExecuteReport();
        if (intro) {
            pageAcc();
        } else {
            pageAttest();
        }
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
            out.append(TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    protected String formatMessage(StringBuffer message, Object[] args) {
        return formatMessage(message.toString(), args);
    }

    public String getDefaultModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "defaultModel", '\\',
                    '/');
        } catch (Exception e) {
            return "";
        }
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "hermesRoot\\model",
                    '\\', '/');
        } catch (Exception e) {
            return "";
        }
    }

    public String getNomDocument() {
        return nomDocument;
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

    @Override
    public boolean next() throws FWIException {

        if (start) {
            start = false;
            intro = true;
        }

        if (interA == true && interB == true) {
            if (iterateurCertif.hasNext()) {
                affilie = (HEDocumentRemiseCAStruct) iterateurCertif.next();
                interA = false;
                interB = false;
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    public void pageAcc() throws Exception {
        HashMap champs;
        List lignes;

        champs = new HashMap();
        lignes = new LinkedList();

        String sexe = affilie.getPolitesse();

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), codeIsoLangue);

        setDocumentTitle("Lettre d'Accompagnement " + affilie.getNnss());
        StringBuffer add = new StringBuffer("");

        add.append(getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE + codeIsoLangue));
        add.append("\n");
        add.append(getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_ADRESSE_CAISSE
                + codeIsoLangue));
        add.append("\n\n\n");

        JADate a = JACalendar.today();
        add.append(a.getDay());
        add.append(" " + JACalendar.getMonthName(a.getMonth(), "FR") + " ");
        add.append(a.getYear());

        // On remplit le fichier Itext avec la structure
        setParametres("P_ADRESSE_CAISSE", add.toString().replaceAll(", ", "\n"));

        setParametres("P_ADRESSE_ASSURE", ((affilie.isEmployeur()) ? affilie.getAdresse() : affilie.getPolitesse()
                + "\n" + affilie.getAdresse()));

        StringBuffer titre_pdf = new StringBuffer("");
        dumpNiveau(1, titre_pdf, "");

        setParametres("P_TEXT_INTRO", formatMessage(titre_pdf, new Object[] { "", "", affilie.getNnss() }));

        StringBuffer poli = new StringBuffer("");
        dumpNiveau(2, poli, "");

        // Fomule de politesse spécifique
        if (!JadeStringUtil.isEmpty(affilie.getFormulePolitesseSpecifique())) {
            sexe = affilie.getFormulePolitesseSpecifique();
        }
        setParametres("FORM_POLITESSE", formatMessage(poli, new Object[] { sexe }));

        StringBuffer corps = new StringBuffer("");

        corps.append(getTexte(3, 1).toString());
        corps.append(getTexte(5, 1).toString());
        corps.append(getTexte(5, 2).toString());
        corps.append(getTexte(6, 1).toString());

        setParametres("P_TEXT_CORPS", formatMessage(corps, new Object[] { sexe, "\n" }));

        setParametres("P_NOM", affilie.getNom());
        setParametres("P_PRENOM", affilie.getPrenom());
        setParametres("P_DATE_NAISS", affilie.getDateNaiss());
        setParametres("P_NUM_AVS", affilie.getNnss());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        caisseReportHelper.addHeaderParameters(this, headerBean);

        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
        signBean.setService2("");
        signBean.setSignataire2("");
        signBean.setService(getSession().getLabel("MSG_SERVICE_NOM"));
        signBean.setSignataire(getSession().getUserFullName());
        signBean.setSignatureCaisse(getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        caisseReportHelper.addSignatureParameters(this, signBean);

        lignes.add(champs);
        setDataSource(lignes);

        document = null;

        intro = false;
        interA = true;

    }

    public void pageAttest() {
        try {

            HashMap champs;
            List lignes;

            champs = new HashMap();
            lignes = new LinkedList();

            setTypeDocument(HECTConstantes.CS_TYPE_LETTRE_REMISE_CA_ATTEST);

            StringBuffer titre_pdf = new StringBuffer("");
            dumpNiveau(1, titre_pdf, "");
            if (!JadeStringUtil.isBlankOrZero(affilie.getNumeroSuccursale())) {
                if (!JadeStringUtil.isBlankOrZero(affilie.getNumeroEmploye())) {
                    setParametres("P_REF", getSession().getApplication().getLabel("REFERENCE", affilie.getLangue())
                            + affilie.getNumeroSuccursale() + "/" + affilie.getNumeroEmploye());
                } else {
                    setParametres("P_REF", getSession().getApplication().getLabel("REFERENCE", affilie.getLangue())
                            + affilie.getNumeroSuccursale() + "/ -");
                }
            } else {
                if (!JadeStringUtil.isBlankOrZero(affilie.getNumeroEmploye())) {
                    setParametres("P_REF", getSession().getApplication().getLabel("REFERENCE", affilie.getLangue())
                            + "- /" + affilie.getNumeroEmploye());
                } else {
                    // no succursale et no employe sont vide --> pas de mention
                    // de la référence sur le document
                    setParametres("P_REF", "");
                }

            }

            setParametres("P_TEXT_TITRE", titre_pdf.toString());

            setDocumentTitle("Attestation d'assurance " + affilie.getNnss());

            // Fomule de politesse spécifique
            if (!JadeStringUtil.isEmpty(affilie.getFormulePolitesseSpecifique())) {
                setParametres("P_FORM_POLITESSE", affilie.getFormulePolitesseSpecifique() + ",");
            } else {
                setParametres("P_FORM_POLITESSE", affilie.getPolitesse() + ",");
            }

            StringBuffer corps2 = new StringBuffer("");

            corps2.append(getTexte(3, 1).toString());
            corps2.append(getTexte(4, 1).toString());
            corps2.append(getTexte(4, 2).toString());
            corps2.append(getTexte(4, 3).toString());
            corps2.append(getTexte(5, 1).toString());

            // Remise en forme de l'adresse :
            String adr = "";

            if (affilie.isRentier()) {
                adr = affilie.getAdresse().replaceAll("\r\n", ", ");
            } else {
                adr = affilie.getAdresse().replaceAll("\n", ", ");
            }

            if (adr.length() > 0) {
                adr = adr.substring(0, (adr.length() - 2));
            }
            // Fix jmc
            if (JadeStringUtil.isBlankOrZero(affilie.getDateEnregistrement())) {
                affilie.setDateEnregistrement(affilie.getAnneeCot());

            }

            setParametres(
                    "P_TEXT_CORPS",
                    formatMessage(
                            corps2,
                            new Object[] { affilie.getPolitesse(), "\n", affilie.getNom(), affilie.getPrenom(),
                                    affilie.getDateNaiss(), " " + affilie.getNnss(),
                                    " " + affilie.getDateEnregistrement(), adr, " " + affilie.getNAffilie() }));

            lignes.add(champs);
            setDataSource(lignes);
            document = null;

        } catch (Exception e) {
            intro = true;
            interB = true;
        }

        intro = true;
        interB = true;

    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public void setTypeDocument(String typeDocument) {
        cs_typeDocument = typeDocument;
    }

}
