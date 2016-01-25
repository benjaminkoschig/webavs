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
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.db.adressecourrier.TILocalite;

/**
 * Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPLettre_Remise_Doc extends CPIDecision_Doc {
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
    public CPLettre_Remise_Doc() throws Exception {
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
    public CPLettre_Remise_Doc(BProcess parent) throws java.lang.Exception {
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
    public CPLettre_Remise_Doc(BSession session) throws java.lang.Exception {
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
        super.setTemplateFile(CPLettre_Remise_Doc.TEMPLATE_NAME);
        langueDoc = AFUtil.toLangueIso(decision.getLangue());
        // On récupère les documents du catalogue de textes nécessaires
        documents = getICTDocument();
        // Affiliation
        AFAffiliation affi = new AFAffiliation();
        affi.setSession(getSession());
        affi.setAffiliationId(decision.getIdAffiliation());
        affi.retrieve();
        setAffiliation(affi);

        // Info
        setDocumentInfo();
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
        if (!JadeStringUtil.isBlank(decisionAdresse.getDesignation1Admin())) {
            caisseReportHelper.addSignatureParameters(this,
                    decisionAdresse.getDesignation1Admin() + " " + decision.getDesignation2Admin()); // agence
        } else {
            caisseReportHelper.addSignatureParameters(this, "");
        }
        // Renseigne les paramètres du document
        String texte = this.getTexteDocument(documents, document, 1, 1).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_CONCERNE, remplaceParametres(texte));
        texte = this.getTexteDocument(documents, document, 2, 1).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_TITRE, remplaceParametres(texte));
        texte = this.getTexteDocument(documents, document, 3, 1).toString();
        texte += this.getTexteDocument(documents, document, 3, 2).toString();
        texte += this.getTexteDocument(documents, document, 3, 3).toString();
        texte += this.getTexteDocument(documents, document, 3, 4).toString();
        texte += this.getTexteDocument(documents, document, 3, 5).toString();
        texte += this.getTexteDocument(documents, document, 3, 6).toString();
        this.setParametres(CPIListeDecisionParam.PARAM_CORPS, remplaceParametres(texte));
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
        document.setCsTypeDocument(CodeSystem.CS_TYPE_LETTRE_REMISE);
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

    /*
     * replace les paramètres (variables) défini dans les textes entre {}
     */
    @Override
    public String remplaceParametres(String texte) throws Exception {
        String chaineModifiee = super.remplaceParametres(texte);
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        while ((index1 != -1) && (index2 != -1)) {
            String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
            // Pour le genre d'affilié --> avec/sans activité lucrative
            if (CPSignet.CS_LIBELLECOMMUNE.equals(chaineARemplacer)) {
                String libelleCommune = "";
                if (!JadeStringUtil.isEmpty(getDecision().getIdCommune())) {
                    TILocalite loca = new TILocalite();
                    loca.setIdLocalite(getDecision().getIdCommune());
                    loca.setSession(getSession());
                    loca.retrieve();
                    if (!loca.isNew()) {
                        libelleCommune = loca.getLocalite();
                    }
                }
                chaineModifiee = CPToolBox.replaceString(chaineModifiee, CPSignet.CS_LIBELLECOMMUNE, libelleCommune);
            }
            // on recherche les prochaines {}
            index1 = chaineModifiee.indexOf("{", index2);
            index2 = chaineModifiee.indexOf("}", index2 + 1);
        }
        return chaineModifiee;
    }
}
