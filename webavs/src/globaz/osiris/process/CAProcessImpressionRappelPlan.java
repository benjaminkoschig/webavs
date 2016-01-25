package globaz.osiris.process;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.print.itext.list.CAIRappelPlanRecouv;
import globaz.osiris.utils.CASursisPaiement;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;

/**
 * @author sel, 04.07.2007
 */
public class CAProcessImpressionRappelPlan extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateRef = "";
    private CAIRappelPlanRecouv document = null;
    private String idPlanRecouvrement = "";

    private Boolean withEcheancier = new Boolean(false);

    /**
     * Constructor for CAProcessImpressionPlan.
     */
    public CAProcessImpressionRappelPlan() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param parent
     */
    public CAProcessImpressionRappelPlan(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param session
     */
    public CAProcessImpressionRappelPlan(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // Met à jour les échéances

            // Création des documents
            document = CASursisPaiement.createRappel(this, getSession(), getIdPlanRecouvrement(), getDateRef());
            CASursisPaiement.updateEcheancesEchues(getTransaction(), getIdPlanRecouvrement(), getDateRef(),
                    getDateLimite());

            if (isWithEcheancier().booleanValue()) {
                // Créer l'échéancier
                CAPlanRecouvrement planRecouvrement = CASursisPaiement.getPlanRecouvrement(getTransaction(),
                        getIdPlanRecouvrement());
                CASursisPaiement.createEcheancier(this, getTransaction(), planRecouvrement);
            }

            // Fusionne les documents et les envois
            fusionneDocuments(document.getPlanRecouvrement());

            // Tester si abort
            if (isAborted()) {
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " : " + e.getMessage());
            setMsgType(FWViewBeanInterface.WARNING);
            setMessage(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " : " + e.getMessage());
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
    }

    /**
     * Fusionne les documents. <br>
     * Envoie un e-mail avec les pdf fusionnés. <br>
     * 
     * @author: sel Créé le : 16 nov. 06
     * @throws Exception
     */
    private void fusionneDocuments(CAPlanRecouvrement plan) throws Exception {
        // Fusionne les documents (Décision, voies de droit et échéancier)
        // Les documents fusionnés sont effacés (théoriquement!!)

        document.setDocumentTitle(getSession().getLabel("OSIRIS_IMPRESSION_RAPPEL_SURSIS") + " "
                + plan.getCompteAnnexe().getDescription());

        // GED
        JadePublishDocumentInfo info = null;

        info = super.createDocumentInfo();
        info.setDocumentTypeNumber(CAIRappelPlanRecouv.NUMERO_REFERENCE_INFOROM);
        IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
        TIDocumentInfoHelper.fill(info, plan.getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE, plan
                .getCompteAnnexe().getIdExterneRole(), affilieFormater.unformat(plan.getCompteAnnexe()
                .getIdExterneRole()));

        // Envoie un e-mail avec les pdf fusionnés
        info.setPublishDocument(true);
        info.setArchiveDocument(false);
        this.mergePDF(info, false, 500, false, null);
    }

    /**
     * Détermine la date limite
     * 
     * @return la date limite à utiliser pour les echéances.
     * @throws JAException
     */
    private JADate getDateLimite() throws JAException {
        JACalendar cal = new JACalendarGregorian();
        return cal.addDays(new JADate(getDateRef()), -CAParametres.getDelaiSuspension(getTransaction()));
    }

    /**
     * Date sur document
     * 
     * @return the dateRef
     */
    public String getDateRef() {
        return dateRef;
    }

    /**
     * Sujet du mail
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // Sujet du mail
        return document.getDocumentTitle();
    }

    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return the withEcheancier
     */
    public Boolean getWithEcheancier() {
        return withEcheancier;
    }

    /**
     * @return the withEcheancier
     */
    public Boolean isWithEcheancier() {
        return withEcheancier;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param dateRef
     *            the dateRef to set
     */
    public void setDateRef(String dateRef) {
        this.dateRef = dateRef;
    }

    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    /**
     * @param withEcheancier
     *            the withEcheancier to set
     */
    public void setWithEcheancier(Boolean withEcheancier) {
        this.withEcheancier = withEcheancier;
    }
}
