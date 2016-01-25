/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.process;

import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.services.validerDecision.RFValiderDecisionServiceFactory;
import globaz.framework.util.FWMemoryLog;
import globaz.jade.job.AbstractJadeJob;
import java.util.ArrayList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFGenererDecisionsGedComptabilisationProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateSurDocument = "";
    private DocumentData docData = new DocumentData();
    private String emailAdress = "";
    private String idGestionnaire = "";
    private String idLot = null;
    private Boolean isMiseEnGed = Boolean.FALSE;
    private String[] listeIdsDecisions = null;
    private FWMemoryLog memoryLog = new FWMemoryLog();

    /**
     * Crée une nouvelle instance de la classe RFValiderDecisionsProcess.
     */
    public RFGenererDecisionsGedComptabilisationProcess() {
        super();
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public DocumentData getDocData() {
        return docData;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdLot() {
        return idLot;
    }

    public Boolean getIsMiseEnGed() {
        return isMiseEnGed;
    }

    public String[] getListeIdsDecisions() {
        return listeIdsDecisions;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    public String getMessageMail(String idLot) {

        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCESS_MISE_EN_GED_ECHEC__DECISIONS_COMPTABILISATION_LOT") + idLot;
        } else {
            return getSession().getLabel("PROCESS_MISE_EN_GED_REUSSIE_DECISIONS_COMPTABILISATION_LOT") + idLot;
        }
    }

    @Override
    public String getName() {
        return null;
    }

    public String getObjetMail() {

        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCESS_MISE_EN_GED_COMPTABILISATION_ECHEC");
        } else {

            return getSession().getLabel("PROCESS_MISE_EN_GED_COMPTABILISATION_SUCCES_TITRE");

        }
    }

    @Override
    public void run() {

        try {

            RFSetEtatProcessService.setEtatProcessValiderDecision(true, getSession());

            ArrayList<RFDecisionDocumentData> decisionArray = new ArrayList<RFDecisionDocumentData>();

            this.createDocuments(RFValiderDecisionServiceFactory.getRFGenererDecisionsGedComptabilistationService()
                    .genererDecisionDocument(getDocData(), getEmailAdress(), getIdGestionnaire(), getIsMiseEnGed(),
                            getMemoryLog(), decisionArray, this, listeIdsDecisions, idLot, getSession(),
                            getTransaction()));

            RFValiderDecisionServiceFactory.getRFGenererDecisionsGedComptabilistationService().envoyerMail(
                    getEmailAdress(), getObjetMail(), getMessageMail(idLot), null);

        } catch (Exception e1) {
            RFValiderDecisionServiceFactory.getRFGenererDecisionsGedComptabilistationService().logErreurs(
                    getEmailAdress(), idLot, getMemoryLog(), e1, this, getSession(), getTransaction());

        } finally {
            RFValiderDecisionServiceFactory.getRFGenererDecisionsGedComptabilistationService().commitTransaction(
                    getTransaction());

            try {
                RFSetEtatProcessService.setEtatProcessValiderDecision(false, getSession());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDocData(DocumentData docData) {
        this.docData = docData;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIsMiseEnGed(Boolean isMiseEnGed) {
        this.isMiseEnGed = isMiseEnGed;
    }

    public void setListeIdsDecisions(String[] listeIdsDecisions) {
        this.listeIdsDecisions = listeIdsDecisions;
    }

    public void setMemoryLog(FWMemoryLog memoryLog) {
        this.memoryLog = memoryLog;
    }

}
