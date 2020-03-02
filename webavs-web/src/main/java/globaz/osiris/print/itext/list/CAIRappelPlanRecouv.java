package globaz.osiris.print.itext.list;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.external.IntTiers;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author sel Date 29.06.2007
 */
public class CAIRappelPlanRecouv extends CADocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String NUMERO_REFERENCE_INFOROM = "0131GCA";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "CAIEcheancierRappel";

    private String compteCADesc = "";
    private String dateRef = "";
    /** Données du formulaire */
    private String idPlanRecouvrement = "";
    private CAPlanRecouvrement plan = null;

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAIRappelPlanRecouv(BProcess parent) throws FWIException {
        super(parent, CAIRappelPlanRecouv.TEMPLATE_NAME);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAIRappelPlanRecouv(BSession parent) throws FWIException {
        super(parent, CAIRappelPlanRecouv.TEMPLATE_NAME);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();

        getDocumentInfo().setDocumentDate(getDateRef());
        getDocumentInfo().setDocumentTitle(getSession().getLabel("SURSIS_RAPPEL"));

        if ((getPlanRecouvrement() != null) && (getPlanRecouvrement() instanceof CAPlanRecouvrement)) {
            if (!JadeStringUtil.isBlank((getPlanRecouvrement()).getCompteAnnexe().getId())) {
                String numAff = (getPlanRecouvrement()).getCompteAnnexe().getIdExterneRole();
                getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
                try {
                    IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                            TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                            affilieFormater.unformat(numAff));
                    TIDocumentInfoHelper.fill(getDocumentInfo(),
                            (getPlanRecouvrement()).getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                            numAff, affilieFormater.unformat(numAff));
                } catch (Exception e) {
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
                }
            }
        }

        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport ()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();

        try {
            imprimerRappelPlanRecouv();
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }

        setNumeroReferenceInforom(CAIRappelPlanRecouv.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        // On va initialiser les documents
        setTypeDocument(CACodeSystem.CS_TYPE_SURSIS_RAPPEL); // Définit le type
        // de document à
        // utiliser.

        IntTiers affilie = null;

        // Récupération des données
        setPlanRecouvrement((CAPlanRecouvrement) currentEntity());

        if (!JadeStringUtil.isBlank(getPlanRecouvrement().getCompteAnnexe().getId())) {
            compteCADesc = getPlanRecouvrement().getCompteAnnexe().getDescription();
            affilie = getPlanRecouvrement().getCompteAnnexe().getTiers();
        }
        _setLangueFromTiers(affilie);
        // Gestion du modèle et du titre
        setTemplateFile(CAIRappelPlanRecouv.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " " + compteCADesc);
        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(getPlanRecouvrement(), true, false, true, getDateRef());

        // Renseigne les paramètres du document
        StringBuilder corps = new StringBuilder("");
        dumpNiveau(1, corps, "");
        // plan.getCompteAnnexe().getDescription() == affilie.getNom()
        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: <ul> <li>{0} = '\n' ;
         * <li>{1} = id Plan ; <li>{2} = Date première Echeance ; <li>{3} = idExterneRole du compte annexe ; <li>{4} =
         * Date du plan </ul>
         */
        this.setParametres(
                CAILettrePlanRecouvParam.P_CONCERNE,
                this.formatMessage(
                        corps,
                        new Object[] { "\n", getPlanRecouvrement().getId(),
                                JACalendar.format(plan.getDateEcheance(), JACalendar.FORMAT_DDsMMsYYYY),
                                getPlanRecouvrement().getCompteAnnexe().getIdExterneRole(),
                                JACalendar.format(getPlanRecouvrement().getDate(), JACalendar.FORMAT_DDsMMsYYYY) }));

        StringBuilder body = new StringBuilder();
        dumpNiveau(2, body, "");
        dumpNiveau(3, body, "");
        dumpNiveau(4, body, "");
        dumpNiveau(5, body, "");
        dumpNiveau(6, body, "");

        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: <ul> <li>{0} = '\n' ;
         * <li>{1} = formule de politesse ; <li>{2} = Plan ; <li>{3} = Montant ; <li>{4} = CHF ; <li>{5} = Date du plan
         * </ul>
         */
        this.setParametres(
                CAILettrePlanRecouvParam.P_TEXT_CORPS,
                this.formatMessage(body,
                        new Object[] { "\n", getFormulePolitesse(), getPlanRecouvrement().getLibelle(),
                                getPlanRecouvrement().getSoldeResiduelPlanFormate(), getTexte(9, 99).toString(),
                                JACalendar.format(getPlanRecouvrement().getDate(), JACalendar.FORMAT_DDsMMsYYYY) }));
    }

    /**
     * @return the dateRef
     */
    public String getDateRef() {
        return dateRef;
    }

    /**
     * @return le titre dans la langue du tier.
     */
    private String getFormulePolitesse() {
        String titre = "";

        try {
            if (!JadeStringUtil.isBlank(getPlanRecouvrement().getCompteAnnexe().getTiers().getId())) {
                TITiers pyTiers = new TITiersViewBean();
                pyTiers.setIdTiers(getPlanRecouvrement().getCompteAnnexe().getTiers().getId());
                pyTiers.setSession(getSession());
                pyTiers.retrieve();
                titre = pyTiers.getFormulePolitesse(TITiers.langueISOtoCodeSystem(getPlanRecouvrement()
                        .getCompteAnnexe().getTiers().getLangueISO()));
            }
        } catch (Exception e) {
            super._addError(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " : " + e.getMessage());
        }

        if (JadeStringUtil.isBlank(titre)) {
            titre = _getProperty(CADocumentManager.JASP_PROP_BODY_FORMULE_DESTINATAIRE_HOMME_FEMME, "");
        }

        return titre;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return the plan
     */
    public CAPlanRecouvrement getPlanRecouvrement() {
        return plan;
    }

    /**
     * Imprimer la lettre de decision pour le plan de recouvrement
     */
    private void imprimerRappelPlanRecouv() throws JAException {
        try {
            CAPlanRecouvrement planRecouvrement = new CAPlanRecouvrement();
            planRecouvrement.setSession(getSession());
            planRecouvrement.setId(getIdPlanRecouvrement());
            planRecouvrement.retrieve(getTransaction());
            if (!JadeStringUtil.isBlank(planRecouvrement.getId())) {
                addEntity(planRecouvrement);
            }
        } catch (Exception e) {
            super._addError(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " : " + e.getMessage());
            super.setMsgType(FWViewBeanInterface.WARNING);
            super.setMessage(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " : " + e.getMessage());
            throw new JAException(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " : " + e.getMessage());
        }
    }

    /**
     * @param dateRef
     *            the dateRef to set
     */
    public void setDateRef(String dateRef) {
        this.dateRef = dateRef;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    /**
     * @param plan
     *            the plan to set
     */
    public void setPlanRecouvrement(CAPlanRecouvrement plan) {
        this.plan = plan;
    }
}
