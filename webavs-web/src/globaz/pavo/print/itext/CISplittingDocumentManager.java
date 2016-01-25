package globaz.pavo.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.naos.itext.AFDocumentItextService;

/**
 * @author MMO
 * @since 14.09.2011
 */
public abstract class CISplittingDocumentManager extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOMAINE_DOCUMENT = "329000";
    public static final String TYPE_DOCUMENT = "340001";

    private String adresse = "";
    private ICaisseReportHelper caisseReportHelper = null;
    private ICTDocument catalogue = null;
    private String formulePolitesse = "";
    private boolean hasNext = true;
    private String langueISO = "";
    private String numeroAVS = "";

    @Override
    public void beforeBuildReport() throws FWIException {
        // NE FAIT RIEN
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // NE FAIT RIEN
    }

    @Override
    public void createDataSource() throws Exception {

        try {
            initDoc();

            // paramètres du texte
            // {0} = Formule de politesse
            String corps2 = AFDocumentItextService.getTexte(catalogue, 1, getParametersCorps2());
            this.setParametres("P_CORPS1", " ");
            this.setParametres("P_CORPS2", corps2);

            setCaisseReportHelper(CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), getLangueISO()));

            fillHeader();

            fillFooter();

        } catch (Exception e) {
            getSession().addError(
                    getSession().getLabel("DOCUMENT_IMPRESSION_PROBLEME") + " : " + this.getClass().getName() + " : "
                            + e.toString());

            getMemoryLog().logMessage(
                    getSession().getLabel("DOCUMENT_IMPRESSION_PROBLEME") + " : " + this.getClass().getName() + " : "
                            + e.toString(), FWMessage.ERREUR, this.getClass().getName());

            if (getDocumentInfo() != null) {
                getDocumentInfo().setDocumentNotes(getMemoryLog().getMessagesInString());
            }

            throw e;

        }

    }

    public void fillDocInfo(String theNumeroInfoRom) throws Exception {
        getDocumentInfo().setTemplateName(getTemplateFile());
        getDocumentInfo()
                .setDocumentProperty("pyxis.tiers.numero.avs.non.formatte", NSUtil.unFormatAVS(getNumeroAVS()));
        getDocumentInfo().setDocumentTypeNumber(theNumeroInfoRom);
        getDocumentInfo().setPublishDocument(true);
    }

    public void fillFooter() throws Exception {

        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();

        signBean.setService2("");
        signBean.setSignataire2("");

        // on récupère la propriété "signature.nom.caisse" du jasperGlobazProperties
        String caisseSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE
                + getLangueISO().toUpperCase());
        signBean.setSignatureCaisse(caisseSignature);

        // on récupère la propriété "signature.nom.service" du jasperGlobazProperties
        String serviceSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE
                + getLangueISO().toUpperCase());

        // la méthode _replaceVars permet de remplacer les chaine de type {user.service}
        String serviceSignatureFinal = ACaisseReportHelper._replaceVars(serviceSignature, getSession().getUserId(),
                null);
        signBean.setService(serviceSignatureFinal);

        String theSignataire = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_SIGNATAIRE
                + getLangueISO().toUpperCase());

        // la méthode _replaceVars permet de remplacer les chaines de type {user.Manager}
        String theSignataireF = ACaisseReportHelper._replaceVars(theSignataire, getSession().getUserId(), null);
        signBean.setSignataire(theSignataireF);

        caisseReportHelper.addSignatureParameters(this, signBean);
    }

    public void fillHeader() throws Exception {

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        headerBean.setAdresse(getAdresse());
        headerBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), getLangueISO()));
        headerBean.setNoAvs(getNumeroAVS());
        headerBean.setConfidentiel(true);
        headerBean.setNomCollaborateur(getSession().getUserFullName());
        headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        headerBean.setUser(getSession().getUserInfo());

        caisseReportHelper.addHeaderParameters(this, headerBean);
    }

    public String getAdresse() {
        return adresse;
    }

    public ICaisseReportHelper getCaisseReportHelper() {
        return caisseReportHelper;
    }

    public ICTDocument getCatalogue() {
        return catalogue;
    }

    public String getFormulePolitesse() {
        return formulePolitesse;
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public String getLangueISO() {
        return langueISO;
    }

    public String getNumeroAVS() {
        return numeroAVS;
    }

    public String[] getParametersCorps2() {
        return new String[] { getFormulePolitesse() };
    }

    public void initDoc() throws Exception {
        // NE FAIT RIEN
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {

        // HACK pour faire que cette méthode ne retourne qu'1 fois vrai
        if (getHasNext()) {
            setHasNext(false);
            return true;
        }
        return false;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCaisseReportHelper(ICaisseReportHelper caisseReportHelper) {
        this.caisseReportHelper = caisseReportHelper;
    }

    public void setCatalogue(ICTDocument catalogue) {
        this.catalogue = catalogue;
    }

    public void setFormulePolitesse(String formulePolitesse) {
        this.formulePolitesse = formulePolitesse;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setLangueISO(String langueISO) {
        this.langueISO = langueISO;
    }

    public void setNumeroAVS(String numeroAVS) {
        this.numeroAVS = numeroAVS;
    }

}
