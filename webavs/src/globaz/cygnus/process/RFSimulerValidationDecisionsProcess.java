/*
 * Créé le 4 avril 2011
 */
package globaz.cygnus.process;

import globaz.cygnus.services.validerDecision.RFSimulerValidationService;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * Liste de controle pour la simulation de la validation des décisions
 * 
 * @author FHA
 * @revision JJE 04.08.2011
 */
@Deprecated
public class RFSimulerValidationDecisionsProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String idGestionnaire = "";
    private Boolean isPourValidation = Boolean.FALSE;

    /**
     * Crée une nouvelle instance de la classe RFSimulerValidationDecisionsProcess.
     */
    public RFSimulerValidationDecisionsProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe RFSimulerValidationDecisionsProcess.
     */
    public RFSimulerValidationDecisionsProcess(BSession session) {
        super();
        setSession(session);
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean _executeProcess() {

        try {

            JadePublishDocumentInfo docInfo = RFSimulerValidationService.lancerSimulationValidation(getEMailAddress(),
                    getIdGestionnaire(), true, createDocumentInfo(), "", getISession(), getTransaction(),
                    getIsPourValidation(), RFPropertiesUtils.getIdTiersAvanceSas(), "");

            getMemoryLog().logMessage(RFSimulerValidationService.getMemoryLog());

            if ((docInfo != null) && !getMemoryLog().hasErrors()) {
                this.registerAttachedDocument(docInfo, RFSimulerValidationService.getDocPath());
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR,
                    "RFSimulerValidationDecisionsProcess._executeProcess()");

            return false;
        }

    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {

        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_VALIDER_DECISIONS_SIMULATION_SUCCESS");
        } else {
            return getSession().getLabel("PROCESS_VALIDER_DECISIONS_SIMULATION_FAILED");
        }

    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public Boolean getIsPourValidation() {
        return isPourValidation;
    }

    @Override
    public String getSubject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCESS_VALIDER_DECISIONS_SIMULATION_FAILED");
        } else {
            return getSession().getLabel("PROCESS_VALIDER_DECISIONS_SIMULATION_SUCCESS");
        }
    }

    @Override
    public String getSubjectDetail() {
        return getMemoryLog().getMessagesInString();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIsPourValidation(Boolean isPourValidation) {
        this.isPourValidation = isPourValidation;
    }

}
