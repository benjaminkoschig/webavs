package globaz.osiris.process;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvBVR4;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvEcheancier;
import globaz.osiris.utils.CASursisPaiement;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import java.util.ArrayList;

/**
 * @author sel <br>
 *         Date : 20 mai 08
 */
public class CAProcessImpressionEcheancier extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String dateRef = "";

    private CAILettrePlanRecouvEcheancier document = null;
    private String idPlanRecouvrement = "";
    private Boolean impAvecBVR = new Boolean(false);

    /**
     * Constructor for CAProcessImpressionPlan.
     */
    public CAProcessImpressionEcheancier() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param parent
     */
    public CAProcessImpressionEcheancier(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param session
     */
    public CAProcessImpressionEcheancier(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CAPlanRecouvrement planRecouvrement = CASursisPaiement.getPlanRecouvrement(getTransaction(),
                    getIdPlanRecouvrement());
            document = CASursisPaiement.createEcheancier(this, getTransaction(), planRecouvrement);

            fusionneDocuments(planRecouvrement); // Fusionne les documents ci-dessus (Décision, voies de droit et
                                                 // échéancier)

            ArrayList echeances = (ArrayList) document.currentEntity();
            if (!JadeStringUtil.isBlank(document.getPlanRecouvrement().getId()) && getImpAvecBVR().booleanValue()
                    && (echeances != null)) {
                createBVR(planRecouvrement, echeances, document);
            }

            // Tester si abort
            if (isAborted()) {
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getSession().getLabel("OSIRIS_IMPRESSION_ECHEANCIER") + " : " + e.getMessage());
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
     * Prépare et retourne le document "BVR"
     * 
     * @author: sel Créé le : 16 nov. 06
     * @param plan
     * @param echeances
     * @return le document "BVR"
     * @throws FWIException
     * @throws Exception
     */
    private CAILettrePlanRecouvBVR4 createBVR(CAPlanRecouvrement plan, ArrayList echeances,
            CAILettrePlanRecouvEcheancier echeancier) throws FWIException, Exception {
        // Instancie le document : BVR
        CAILettrePlanRecouvBVR4 documentBVR = new CAILettrePlanRecouvBVR4(this);
        documentBVR.setSession(getSession());
        documentBVR.setDateRef(getDateRef());
        documentBVR.addAllEntities(echeances);
        documentBVR.setPlanRecouvrement(plan);
        documentBVR.setCumulSolde(echeancier.getCumulSolde());
        documentBVR.setImpressionParLot(true);
        documentBVR.setTailleLot(500);

        // Demander le traitement du document
        documentBVR.setEMailAddress(getEMailAddress());
        documentBVR.executeProcess();

        return documentBVR;
    }

    /**
     * Fusionne les documents (Décision, voies de droit et échéancier). <br>
     * Envoie un e-mail avec les pdf fusionnés. <br>
     * 
     * @author: sel Créé le : 16 nov. 06
     * @throws Exception
     */
    private void fusionneDocuments(CAPlanRecouvrement plan) throws Exception {
        // Fusionne les documents (Décision, voies de droit et échéancier)
        // Les documents fusionnés sont effacés
        // GED
        JadePublishDocumentInfo info = super.createDocumentInfo();
        info.setDocumentTypeNumber(CAILettrePlanRecouvEcheancier.NUMERO_REFERENCE_INFOROM);
        IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
        TIDocumentInfoHelper.fill(info, plan.getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE, plan
                .getCompteAnnexe().getIdExterneRole(), affilieFormater.unformat(plan.getCompteAnnexe()
                .getIdExterneRole()));

        // Envoie un e-mail avec les pdf fusionnés
        info.setPublishDocument(true);
        info.setArchiveDocument(true);
        this.mergePDF(info, true, 500, false, null);
    }

    /**
     * @return the dateRef
     */
    public String getDateRef() {
        return dateRef;
    }

    @Override
    protected String getEMailObject() {
        // Sujet du mail
        return document.getDocumentTitle();
    }

    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    public Boolean getImpAvecBVR() {
        return impAvecBVR;
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

    public void setImpAvecBVR(Boolean newImpAvecBVR) {
        impAvecBVR = newImpAvecBVR;
    }
}
