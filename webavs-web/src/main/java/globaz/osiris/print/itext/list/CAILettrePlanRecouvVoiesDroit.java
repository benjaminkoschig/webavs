package globaz.osiris.print.itext.list;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.external.IntTiers;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;

/**
 * @author sel, 23 oct. 2006
 */
public class CAILettrePlanRecouvVoiesDroit extends CADocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_AFFILIE_FORMATTE = "numero.affilie.formatte";
    private static final String NUMERO_AFFILIE_NON_FORMATTE = "numero.affilie.non.formatte";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "CAIEcheancierVoiesDroit";
    /** Données du formulaire */
    private String idPlanRecouvrement = "";
    private String joindreBVR = "";

    private CAPlanRecouvrement plan = null;

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvVoiesDroit(BProcess parent) throws FWIException {
        super(parent, CAILettrePlanRecouvVoiesDroit.TEMPLATE_NAME);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvVoiesDroit(BSession parent) throws FWIException {
        super(parent, CAILettrePlanRecouvVoiesDroit.TEMPLATE_NAME);
    }

    @Override
    public void afterPrintDocument() {
        try {
            super.afterPrintDocument();
        } catch (FWIException e) {
            JadeLogger.error(this, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.print.itext.list.CADocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();

        if ((getPlanRecouvrement() != null) && (getPlanRecouvrement() instanceof CAPlanRecouvrement)) {
            if (!JadeStringUtil.isBlank((getPlanRecouvrement()).getCompteAnnexe().getId())) {
                String numAff = (getPlanRecouvrement()).getCompteAnnexe().getIdExterneRole();
                getDocumentInfo().setDocumentProperty(CAILettrePlanRecouvVoiesDroit.NUMERO_AFFILIE_FORMATTE, numAff);
                try {
                    IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                            TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
                    getDocumentInfo().setDocumentProperty(CAILettrePlanRecouvVoiesDroit.NUMERO_AFFILIE_NON_FORMATTE,
                            affilieFormater.unformat(numAff));
                    TIDocumentInfoHelper.fill(getDocumentInfo(),
                            (getPlanRecouvrement()).getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                            numAff, affilieFormater.unformat(numAff));
                } catch (Exception e) {
                    getDocumentInfo().setDocumentProperty(CAILettrePlanRecouvVoiesDroit.NUMERO_AFFILIE_NON_FORMATTE,
                            numAff);
                }
            }
        }
        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();

        try {
            imprimerVoiesDroit();
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }
        setNumeroReferenceInforom(CAILettrePlanRecouvDecision.NUMERO_REFERENCE_INFOROM);
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        IntTiers affilie = null;

        // Récupération des données
        setPlanRecouvrement((CAPlanRecouvrement) currentEntity());

        // On récupère les documents nécessaires du catalogue de textes
        setTypeDocument(CACodeSystem.CS_TYPE_SURSIS_VOIES_DROIT);

        if (!JadeStringUtil.isBlank(plan.getCompteAnnexe().getId())) {
            affilie = plan.getCompteAnnexe().getTiers();
        }
        _setLangueFromTiers(affilie);
        // Gestion du modèle et du titre
        setTemplateFile(CAILettrePlanRecouvVoiesDroit.TEMPLATE_NAME);

        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(null, true, false, false);

        String texte = getTexte(1, 1).toString();
        texte += getTexte(2, 1).toString();
        texte += getTexte(3, 1).toString();
        texte += getTexte(4, 1).toString();
        texte += getTexte(5, 1).toString();
        texte += getTexte(6, 1).toString();
        texte += getTexte(7, 1).toString();
        texte += getTexte(8, 1).toString();
        texte += getTexte(9, 1).toString();
        texte += "   "; // Pour des raisons obscures, JasperReport imprime les 3 derniers caractères sur une nouvelle
                        // page.
        // TODO sel régler ce problème !!

        // Renseigne les paramètres du document
        this.setParametres(CAILettrePlanRecouvParam.P_TEXTVD, texte);
    }

    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getJoindreBVR() {
        return joindreBVR;
    }

    public CAPlanRecouvrement getPlanRecouvrement() {
        return plan;
    }

    /**
     * Imprimer les voies de droit pour le plan de recouvrement
     */
    private void imprimerVoiesDroit() throws JAException {
        try {
            CAPlanRecouvrement planRecouvrement = new CAPlanRecouvrement();
            planRecouvrement.setSession(getSession());
            planRecouvrement.setId(getIdPlanRecouvrement());
            planRecouvrement.retrieve(getTransaction());
            if (!JadeStringUtil.isBlank(planRecouvrement.getId())) {
                addEntity(planRecouvrement);
            }
        } catch (Exception e) {
            super._addError(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
            super.setMsgType(FWViewBeanInterface.WARNING);
            super.setMessage(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
            throw new JAException(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
        }
    }

    public void setIdPlanRecouvrement(String idPlanRecouvrement) {
        this.idPlanRecouvrement = idPlanRecouvrement;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setJoindreBVR(String string) {
        joindreBVR = string;
    }

    public void setPlanRecouvrement(CAPlanRecouvrement plan) {
        this.plan = plan;
    }

}
