package globaz.naos.itext;

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
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.service.CETiersService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.AFUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;

/**
 * @author SCO
 * @since 05 juil. 2011
 */
public class AFAttestationPersonnelle_doc extends FWIDocumentManager {

    private static final long serialVersionUID = -5238188255242552694L;
    private static final String DOCUMENT_ATTESTATION_PERSONNELLE = CodeSystem.TYPE_CAT_ATTESTATION_PERSONNELLE;
    private static final String MODEL_ATTESTATION_PERSONNELLE = "NAOS_ATTESTATION_PERSONNELLE";
    private static final String NOMDOC_ATTESTATION_PERSONNELLE = "NOMDOC_ATTESTATION_PERSONNELLE";
    public static final String NUMERO_INFOROM = "0271CAF";
    private static final String TITRE_ATTESTATION_PERSONNELLE = "TITRE_ATTESTATION_PERSONNELLE";

    private String annee = null;
    private ICTDocument catalogue = null;
    private String dateEnvoi = null;
    protected boolean hasNext = true;
    private String idTiers = null;
    private String langueIsoRequerant = "fr";// langue du requerant
    private String numAffilie = null;
    private boolean publishDocument = true;
    private String sommeAnnee = null;
    private String sommeAnterieur = null;
    private String totalCoti = null;

    /**
     * Constructeur public de la classe
     * 
     * @param session
     * @throws FWIException
     */
    public AFAttestationPersonnelle_doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    /**
     * Constructeur public de la classe en passant une session.
     * 
     * @param session
     * @throws FWIException
     */
    public AFAttestationPersonnelle_doc(BSession session) throws FWIException {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, session
                .getLabel(AFAttestationPersonnelle_doc.NOMDOC_ATTESTATION_PERSONNELLE));
    }

    private String resolveDateImpression(TITiersViewBean tiers) {
        String dateImpression;

        if (!JadeStringUtil.isEmpty(getDateEnvoi())) {
            dateImpression = JACalendar.format(getDateEnvoi(), tiers.getLangueIso());
        } else {
            dateImpression = JACalendar.format(JACalendar.todayJJsMMsAAAA(), tiers.getLangueIso());
        }

        return dateImpression;
    }

    private void setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {
        bean.setAdresse(tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION));
        bean.setDate(bean.getDate());
        bean.setNoAvs(tiers.getNumAvsActuel());
        bean.setConfidentiel(true);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
        bean.setNoAffilie(getNumAffilie());

        AFIDEUtil.addNumeroIDEInDocForNumAffilie(getSession(), bean, getNumAffilie());
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        // Not implemented
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFAttestationPersonnelle_doc.MODEL_ATTESTATION_PERSONNELLE);
    }

    @Override
    public void createDataSource() throws Exception {
        setDocumentTitle(getNumAffilie() + " "
                + getSession().getLabel(AFAttestationPersonnelle_doc.TITRE_ATTESTATION_PERSONNELLE));
        getDocumentInfo().setDocumentTypeNumber(AFAttestationPersonnelle_doc.NUMERO_INFOROM);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(getDateEnvoi());

        // récupération du requerant en cours et du tiers correspondant
        TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), getIdTiers());

        // récupération de la langue du tiers
        setLangueIsoRequerant(AFUtil.toLangueIso(tiers.getLangue()));

        // définit le titre (Madame, Monsieur) du requérant
        String formulePolitesse = tiers.getFormulePolitesse(null);

        // Chargement du catalogue
        this.loadCatalogue(AFAttestationPersonnelle_doc.DOCUMENT_ATTESTATION_PERSONNELLE);

        // Liste des arguments
        // {0} = Formule de politesse
        // {1} = année de l'attestation
        // {2} = somme de l'année
        // {3} = somme des années antérieures
        // {4} = total des cotisations
        // {5} = label "FACTUREES" ou "CREDITEES" suivant si total est positif ou pas
        // {6} = Montant total label "FACTUREES" ou "CREDITEES" suivant si total est positif ou pas

        String valueLabelFactureCredit;
        String valueLabelMontantTotalFactureCredit;
        if (Double.parseDouble(JANumberFormatter.deQuote(getTotalCoti())) > 0) {
            valueLabelFactureCredit = getSession().getApplication().getLabel("FACTUREES", getLangueIsoRequerant());
            valueLabelMontantTotalFactureCredit = getSession().getApplication().getLabel("MONTANT_TOTAL_FACTUREES",
                    getLangueIsoRequerant());
        } else {
            valueLabelFactureCredit = getSession().getApplication().getLabel("CREDITEES", getLangueIsoRequerant());
            valueLabelMontantTotalFactureCredit = getSession().getApplication().getLabel("MONTANT_TOTAL_CREDITEES",
                    getLangueIsoRequerant());
        }

        String[] listeArgument = { formulePolitesse, getAnnee(),
                JANumberFormatter.fmt(getSommeAnnee(), true, true, false, 2),
                JANumberFormatter.fmt(getSommeAnterieur(), true, true, false, 2),
                JANumberFormatter.fmt(getTotalCoti(), true, true, false, 2), valueLabelFactureCredit,
                valueLabelMontantTotalFactureCredit };

        // Mise en place du texte
        this.setParametres("P_TEXTE1", AFDocumentItextService.getTexte(catalogue, 1, listeArgument));
        this.setParametres("P_TEXTE2", AFDocumentItextService.getTexte(catalogue, 2, listeArgument));
        this.setParametres("P_TEXTE3", AFDocumentItextService.getTexte(catalogue, 3, listeArgument));
        this.setParametres("P_TEXTE4", AFDocumentItextService.getTexte(catalogue, 4, listeArgument));
        this.setParametres("P_TEXTE5", AFDocumentItextService.getTexte(catalogue, 5, listeArgument));
        this.setParametres("P_TEXTE6", AFDocumentItextService.getTexte(catalogue, 6, listeArgument));

        // Récupération du document ITEXT
        setTemplateFile(AFAttestationPersonnelle_doc.MODEL_ATTESTATION_PERSONNELLE);

        // Numero affilié passé au docinfo
        fillDocInfo();

        // Mise en place du header et du footer
        setHeaderAndFooter(tiers, resolveDateImpression(tiers));
    }

    private void fillDocInfo() throws Exception {
        // Numero affilié passé au docinfo
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAffilie());
        getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                AFUtil.giveNumeroAffilieNonFormate(getNumAffilie()));
        getDocumentInfo().setDocumentProperty("annee", getAnnee());

        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);

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

    public String getDateEnvoi() {
        return dateEnvoi;
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

    public String getSommeAnnee() {
        return sommeAnnee;
    }

    public String getSommeAnterieur() {
        return sommeAnterieur;
    }

    public String getTotalCoti() {
        return totalCoti;
    }

    public boolean isPublishDocument() {
        return publishDocument;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Chargement du catalogue
     * 
     * @param typeDocument Le type du document
     */
    public void loadCatalogue(String typeDocument) {
        loadCatalogue(typeDocument, null, null);
    }

    /**
     * Chargement du catalogue avec un idDocument
     * 
     * @param typeDocument
     * @param idDocument
     * @param idDocumentDefault
     */
    public void loadCatalogue(String typeDocument, String idDocument, String idDocumentDefault) {
        // récupération du catalogue de texte

        try {
            catalogue = AFDocumentItextService.retrieveCatalogue(getSession(), langueIsoRequerant, CodeSystem.DOMAINE,
                    typeDocument, idDocument, idDocumentDefault);
        } catch (FWIException e) {
            getMemoryLog().logMessage(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR,
                    this.getClass().getName());
            JadeLogger.error(this, e);
            abort();
        }

    }

    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = false;
        }

        return retValue;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    /**
     * Permet de setter le header et le pied de page du document.
     * 
     * @param tiers Le tiers reletif au document
     * @param dateImpression La date d'impression
     * @throws Exception
     */
    private void setHeaderAndFooter(TITiersViewBean tiers, String dateImpression) throws Exception {

        // mise en place du header
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), getLangueIsoRequerant());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setDate(dateImpression);

        setHeader(headerBean, tiers);
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

        String serviceSignatureFinal = ACaisseReportHelper._replaceVars(serviceSignature, getSession().getUserId(),
                null);
        signBean.setService(serviceSignatureFinal);
        signBean.setSignataire(getSession().getUserFullName());
        // BZ 8012
        caisseReportHelper.addSignatureParameters(this);
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLangueIsoRequerant(String langueIsoRequerant) {
        this.langueIsoRequerant = langueIsoRequerant;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setPublishDocument(boolean publishDocument) {
        this.publishDocument = publishDocument;
    }

    public void setSommeAnnee(String sommeAnnee) {
        this.sommeAnnee = sommeAnnee;
    }

    public void setSommeAnterieur(String sommeAnterieur) {
        this.sommeAnterieur = sommeAnterieur;
    }

    public void setTotalCoti(String totalCoti) {
        this.totalCoti = totalCoti;
    }
}
