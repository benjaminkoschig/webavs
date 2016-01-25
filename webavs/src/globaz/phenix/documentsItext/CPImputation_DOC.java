package globaz.phenix.documentsItext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.util.FAUtil;
import globaz.naos.util.AFUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.translation.CodeSystem;
import net.sf.jasperreports.engine.JRExporterParameter;

/**
 * Date de création : (02.05.2003 10:26:03)
 * 
 * @author: Administrator
 */
public class CPImputation_DOC extends CPIDecision_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "PHENIX_MISEENCPT";
    private ICTDocument document = null;
    // Variables pour la recherche des textes pour le document
    private ICTDocument[] documents = null;
    private boolean remboursementFraisAdmin = false;

    private ICTDocument res[] = null;

    /**
     * Date de création : (26.03.2003 09:38:26)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CPImputation_DOC(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Date de création : (26.03.2003 09:38:26)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CPImputation_DOC(BSession session) throws Exception {
        super(session);
    }

    /**
     * Retourne le montant encosé pour la mise en compte Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String _getMiseEnCompte() {
        try {
            processAppelant.incProgressCounter();
            CPDonneesBase donnee = new CPDonneesBase();
            donnee.setSession(getSession());
            donnee.setIdDecision(getDecision().getIdDecision());
            donnee.retrieve();
            if (isRemboursementFraisAdmin()) {
                String fraisImputation = CPToolBox.calculFraisImputation(donnee.getCotisation1(), decision, this);
                String imputation = JANumberFormatter.deQuote(donnee.getCotisation1());
                float montant = 0;
                float cumul = 0;
                if (!JadeStringUtil.isEmpty(imputation)) {
                    montant = Float.parseFloat(imputation);
                }
                if (!JadeStringUtil.isEmpty(fraisImputation)) {
                    cumul = Float.parseFloat(fraisImputation);
                }
                cumul = new FWCurrency(cumul + montant).floatValue();
                if (cumul == 0) {
                    return "0";
                } else {
                    return JANumberFormatter.fmt(Float.toString(cumul), true, false, true, 2);
                }
            } else {
                return donnee.getCotisation1();
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return "";
        }
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    @Override
    protected void _headerText(CaisseHeaderReportBean headerBean) {
        try {
            // Zones communes à toutes les différents type de décisions
            setHeaderBean(headerBean);
            // Zone pour décision de type mise en compte
            if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(getDecision().getTypeDecision())) {
                super.setParametres(CPIListeDecisionParam.PARAM_MONTANT_MISEENCOMPTE, _getMiseEnCompte());
            } else if (getDecision().isNonActif()) {
                // Zone pour décision de type non actif
            }
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
     * Surcharge :
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument()
     */
    @Override
    public boolean beforePrintDocument() {
        try {
            getExporter().setExporterOutline(JRExporterParameter.OUTLINE_ALL);
        } catch (FWIException e) {
            // CAST OMMIT -> on laisse par defaut si prb !!!
        }
        return super.beforePrintDocument() && ((size() > 0) && !isAborted());
    }

    /**
     * Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        // nom du template
        super.setTemplateFile(CPImputation_DOC.TEMPLATE_NAME);
        // On récupère les documents du catalogue de textes nécessaires
        langueDoc = AFUtil.toLangueIso(decision.getLangue());
        documents = getICTDocument();

        try {
            FAPassage thePassage = FAUtil.loadPassage(getDecision().getIdPassage(), getSession());
            FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), thePassage);
        } catch (Exception e) {
            JadeLogger.warn(this, "Unable to load passage : " + e.getMessage());
        }

        // Info
        setDocumentInfo();
        getDocumentInfo().setDocumentTypeNumber("0119CCP");
        // GED
        if (getEnvoiGed().equals(Boolean.TRUE)) {
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);
        }

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), "" + langueDoc);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        // Get Parameters
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
        String texte = "";
        if (!CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
            texte = this.getTexteDocument(documents, document, 1, 1, "").toString();
        } else {
            texte = this.getTexteDocument(documents, document, 1, 2, "").toString();
        }
        this.setParametres(CPIListeDecisionParam.PARAM_CONCERNE, texte);
        texte = this.getTexteDocument(documents, document, 2, 1,
                CodeSystem.getLibelle(getSession(), getDecision().getTitre_tiers(), langueDoc)).toString();
        texte += this.getTexteDocument(documents, document, 2, 2, getDecision().getAnneeDecision()).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_TEXTE1, texte);
        texte = this.getTexteDocument(documents, document, 2, 3, "").toString();
        if (!CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
            texte += this.getTexteDocument(documents, document, 4, 1, "").toString();
            texte += this.getTexteDocument(documents, document, 4, 2, _getAdresseCaisse()).toString();
            texte += this.getTexteDocument(documents, document, 4, 3, "").toString();
        }
        texte += this.getTexteDocument(documents, document, 5, 1,
                CodeSystem.getLibelle(getSession(), getDecision().getTitre_tiers(), langueDoc)).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_TEXTE2, texte);
    };

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
        document.setCsTypeDocument(CodeSystem.CS_TYPE_LETTRE_IMPUTATION);
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

    /**
     * @return
     */
    public boolean isRemboursementFraisAdmin() {
        return remboursementFraisAdmin;
    }

    /**
     * @param b
     */
    public void setRemboursementFraisAdmin(boolean b) {
        remboursementFraisAdmin = b;
    }

}
