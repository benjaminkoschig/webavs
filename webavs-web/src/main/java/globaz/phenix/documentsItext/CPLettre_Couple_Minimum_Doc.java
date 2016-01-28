package globaz.phenix.documentsItext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFUtil;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.translation.CodeSystem;

/**
 * Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPLettre_Couple_Minimum_Doc extends CPIDecision_Doc {
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
    public CPLettre_Couple_Minimum_Doc() throws Exception {
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
    public CPLettre_Couple_Minimum_Doc(BProcess parent) throws java.lang.Exception {
        super(parent);
    }

    /**
     * Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPLettre_Couple_Minimum_Doc(BSession session) throws java.lang.Exception {
        super(session);
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    @Override
    protected void _headerText(CaisseHeaderReportBean headerBean) {
        try {
            setHeaderBean(headerBean);
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
        }
    }

    @Override
    public void afterBuildReport() {
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
        super.setTemplateFile(CPLettre_Couple_Minimum_Doc.TEMPLATE_NAME);
        langueDoc = AFUtil.toLangueIso(decision.getLangue());
        // On récupère les documents du catalogue de textes nécessaires
        documents = getICTDocument();
        // Affiliation
        AFAffiliation affi = new AFAffiliation();
        affi.setSession(getSession());
        affi.setAffiliationId(decision.getIdAffiliation());
        affi.retrieve();
        setAffiliation(affi);

        setDocumentInfo();
        getDocumentInfo().setDocumentTypeNumber("0079CCP");
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueDoc);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _headerText(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        // Agence communale
        if (!JadeStringUtil.isBlank(decisionAdresse.getDesignation1Admin())) {
            caisseReportHelper.addSignatureParameters(this,
                    decisionAdresse.getDesignation1Admin() + " " + decision.getDesignation2Admin()); // agence
        } else {
            caisseReportHelper.addSignatureParameters(this, "");
        }
        // Renseigne les paramètres du document
        String texte = getTexte(1, 1).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_CONCERNE, texte);
        texte = getTexte(2, 1).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_TITRE, texte);
        texte = getTexte(3, 1).toString();
        texte += getTexte(3, 2).toString();
        texte += getTexte(3, 3).toString();
        texte += getTexte(3, 4).toString();
        texte += getTexte(3, 5).toString();
        // texte += getTexte(4, 1).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_CORPS, texte);
    }

    /**
     * Formate le texte. Remplace un {0} par la date d'échéance
     * 
     * @param paragraphe
     * @return
     * @throws Exception
     */
    private StringBuffer format(StringBuffer paragraphe) {
        StringBuffer res = new StringBuffer("");
        try {
            String titre = getAffiliation().getTiers().getFormulePolitesse(null);

            for (int i = 0; i < paragraphe.length(); i++) {
                if (paragraphe.charAt(i) != '{') {
                    res.append(paragraphe.charAt(i));
                } else if (paragraphe.charAt(i + 1) == '1') {
                    res.append(titre);
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '2') {
                    res.append(getDecision().getAnneeDecision());
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '3') {
                    float cotiMinimumDouble = getCotiIndMinimum() * 2;
                    if (cotiMinimumDouble % 1 == 0) {
                        res.append(JANumberFormatter.formatZeroValues(Float.toString(cotiMinimumDouble), false, true)
                                + ".--");
                    } else {
                        res.append(JANumberFormatter.fmt(Float.toString(cotiMinimumDouble), true, true, true, 2));
                    }
                    i = i + 2;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            this._addError(getTransaction(), e.getMessage());
        }

        return res;
    }

    /**
     * Retourne la cotisation minimum indépendant pour une année
     * 
     * @return float
     */
    public float getCotiIndMinimum() {
        try {
            return CPTableIndependant.getCotisationMinimum(getTransaction(), getDecision().getDebutDecision());
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return 0;
        }
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
        document.setCsTypeDocument(CodeSystem.CS_TYPE_LETTRE_COUPLE_MINIMUM);
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
     * Récupère les textes du catalogue de texte
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getTexte(int niveau, int position) {
        StringBuffer resString = new StringBuffer("");
        try {
            if (document == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = documents[0].getTextes(niveau);
                resString.append(listeTextes.getTexte(position));
            }
        } catch (Exception e3) {
            getMemoryLog()
                    .logMessage(e3.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return this.format(resString);
    }
}
