package globaz.phenix.documentsItext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFUtil;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.translation.CodeSystem;

/**
 * Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPLettre_SalarieDispense_Doc extends CPIDecision_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "PHENIX_LETTRE";
    private ICTDocument document = null;
    // Variables pour la recherche des textes pour le document
    private ICTDocument[] documents = null;
    private ICTDocument res[] = null;

    /**
     * Date de création : (26.02.2003 16:56:39)
     */
    public CPLettre_SalarieDispense_Doc() throws Exception {
        this(new BSession(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    /**
     * Date de création : (26.02.2003 17:00:08)
     * 
     * @param parent
     *            BProcess
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPLettre_SalarieDispense_Doc(BProcess parent) throws java.lang.Exception {
        super(parent);
        super.setProcessAppelant(processAppelant);
    }

    /**
     * Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPLettre_SalarieDispense_Doc(BSession session) throws java.lang.Exception {
        super(session);
        super.setProcessAppelant(processAppelant);
    }

    /**
     * Surcharge :
     * 
     * @see globaz.phenix.documentsItext.CPIDecision_Doc#_headerText(globaz.caisse.report.helper.CaisseHeaderReportBean)
     * @param headerBean
     */
    @Override
    protected void _headerText(CaisseHeaderReportBean headerBean) {
        try {
            setHeaderBean(headerBean);
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
        }
    }

    /**
     * Surcharge :
     * 
     * @see globaz.phenix.documentsItext.CPIDecision_Doc#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() {
        // super.beforeExecuteReport();
        // On va initialiser les documents
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            super.setImpressionParLot(true);
            super.setTailleLot(0);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        // nom du template
        super.setTemplateFile(CPLettre_SalarieDispense_Doc.TEMPLATE_NAME);
        // On récupère les documents du catalogue de textes nécessaires
        langueDoc = AFUtil.toLangueIso(decision.getLangue());
        documents = getICTDocument();
        // Affiliation
        AFAffiliation affi = new AFAffiliation();
        affi.setSession(getSession());
        affi.setAffiliationId(decision.getIdAffiliation());
        affi.retrieve();
        setAffiliation(affi);

        // Info
        setDocumentInfo();
        getDocumentInfo().setDocumentTypeNumber("0080CCP");
        // GED
        if (getEnvoiGed().equals(Boolean.TRUE)) {
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);
        }

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueDoc);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _headerText(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        // Agence communale
        if (!JadeStringUtil.isBlank(decision.getDesignation1Admin())) {
            caisseReportHelper.addSignatureParameters(this,
                    decision.getDesignation1Admin() + " " + decision.getDesignation2Admin()); // agence
        } else {
            caisseReportHelper.addSignatureParameters(this, "");
        }
        // Renseigne les paramètres du document
        String texte = this.getTexteDocument(documents, document, 1, 1, "").toString();
        this.setParametres(CPIListeDecisionParam.PARAM_CONCERNE, texte);
        texte = this.getTexteDocument(documents, document, 2, 1,
                CodeSystem.getLibelle(getSession(), getDecision().getTitre_tiers(), langueDoc)).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_TITRE, texte);
        texte = this.getTexteDocument(documents, document, 3, 1, "").toString();
        texte += this.getTexteDocument(documents, document, 3, 2, getDecision().getAnneeDecision()).toString();
        texte += this.getTexteDocument(documents, document, 3, 3, getDecision().getAnneeDecision()).toString();
        texte += this.getTexteDocument(documents, document, 3, 4, "").toString();
        texte += this.getTexteDocument(documents, document, 3, 5, "").toString();
        texte += this.getTexteDocument(documents, document, 4, 1,
                CodeSystem.getLibelle(getSession(), getDecision().getTitre_tiers(), langueDoc)).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_CORPS, texte);
    }

    /**
     * Récupère le document permettant d'ajouter les textes du catalogue de texte au document
     * 
     * @author: sel Créé le : 11 déc. 06
     * @return
     */
    private ICTDocument[] getICTDocument() {
        // if (res == null) {
        document.setISession(getSession());
        document.setCsDomaine(CodeSystem.CS_DOMAINE_CP);
        document.setCsTypeDocument(CodeSystem.CS_TYPE_LETTRE_SALARIE_DISPENSE);
        document.setDefault(new Boolean(true));
        document.setCodeIsoLangue(langueDoc); // "FR"
        document.setActif(new Boolean(true));
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
        // }
        return res;
    }
}
