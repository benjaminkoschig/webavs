package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.utils.CASursisPaiement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CAProcessSuspendrePlanRecouvrement extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 8522656124032081890L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateSuspension;
    private String idPlanRecouvrement;
    private Boolean sendSommation = new Boolean(true);

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 */
    @Override
    protected void _executeCleanUp() {
        // nope
    }

    /**
     * @return false si problèmes
     * @throws Exception
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        CAPlanRecouvrement planRecouvrement = CASursisPaiement.getPlanRecouvrement(getTransaction(),
                getIdPlanRecouvrement());
        // le plan doit être actif - on ne fait pas ca dans validate pour que
        // l'utilisateur recoive un mail
        if (!CAPlanRecouvrement.CS_ACTIF.equals(planRecouvrement.getIdEtat())) {
            this.log(getSession().getLabel("PLAN_SUSPENSION_INACTIF"));
            abort();
            return false;
        }
        try {
            CASursisPaiement.annulerPlan(this, getTransaction(), planRecouvrement, getDateSuspension(),
                    getSendSommation());
            this.log(getSession().getLabel("PLAN_SUSPENDU") + " (" + getIdPlanRecouvrement() + ").",
                    FWMessage.INFORMATION);
            return true;
        } catch (Exception e) {
            this.log(e.toString());
            abort();
            return false;
        }
    }

    /**
     * getter pour l'attribut date suspension.
     * 
     * @return la valeur courante de l'attribut date suspension
     */
    public String getDateSuspension() {
        return dateSuspension;
    }

    /**
     * getter pour l'attribut EMail object.
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PLAN_SUSPENSION_EMAIL");
    }

    /**
     * getter pour l'attribut id plan recouvrement.
     * 
     * @return la valeur courante de l'attribut id plan recouvrement
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    /**
     * @return the sendSommation
     */
    public Boolean getSendSommation() {
        return sendSommation;
    }

    /**
     * @see globaz.globall.db.GlobazJobQueue
     * @return
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * log un message d'erreur dans le memory log.
     * 
     * @param messageErreur
     *            le message d'erreur
     */
    private void log(String messageErreur) {
        this.log(messageErreur, FWMessage.ERREUR);
    }

    /**
     * log un message du type donné dans le memory log.
     * 
     * @param message
     *            le message à logger
     * @param type
     *            le type du message
     */
    private void log(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
    }

    /**
     * setter pour l'attribut date suspension.
     * 
     * @param dateSuspension
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSuspension(String dateSuspension) {
        this.dateSuspension = dateSuspension;
    }

    /**
     * setter pour l'attribut id plan recouvrement.
     * 
     * @param idPlanRecouvrement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPlanRecouvrement(String idPlanRecouvrement) {
        this.idPlanRecouvrement = idPlanRecouvrement;
    }

    /**
     * @param sendSommation
     *            the sendSommation to set
     */
    public void setSendSommation(Boolean sendSommation) {
        this.sendSommation = sendSommation;
    }
}
