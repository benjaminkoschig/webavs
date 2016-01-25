package globaz.cygnus.services.validerDecision;

import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.db.decisions.RFCopieDecision;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.process.RFValiderDecisionAvasadProcess;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import java.util.ArrayList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RFValiderDecisionAvasadService extends RFValiderDecisionService {

    private void chercherDecisionsAvasad(
            RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager,
            String idGestionnaire, String idExecutionProcess, BSession session) throws Exception {

        decisionJointTiersManager.setSession(session);
        decisionJointTiersManager.setForCsEtatDecision(IRFDecisions.NON_VALIDE);

        if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
            decisionJointTiersManager.setIdGestionnaire(idGestionnaire);
        }

        decisionJointTiersManager.setForIdExecutionProcess(idExecutionProcess);

        decisionJointTiersManager.setForOrderBy(RFDecision.FIELDNAME_ID_DECISION + "," + RFDemande.FIELDNAME_ID_DEMANDE
                + "," + RFCopieDecision.FIELDNAME_ID_COPIE + "," + RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);
        decisionJointTiersManager.changeManagerSize(0);
        decisionJointTiersManager.find();
    }

    public void commitTransaction(BTransaction transaction, boolean isSimulation) {

        if ((transaction != null)) {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly() || isSimulation) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public JadePrintDocumentContainer genererDecisionDocument(String dateSurDocument, DocumentData docData,
            String emailAdress, String idGestionnaire, boolean isMiseEnGed, boolean isSimulation,
            FWMemoryLog memoryLog, StringBuffer pdfDecisionURL, ArrayList<RFDecisionDocumentData> decisionArray,
            RFValiderDecisionAvasadProcess process, String idExecutionProcess, BSession session,
            BTransaction transaction) throws Exception {

        // TopazSystem.getInstance().getDocBuilder().setOpenedDocumentsVisible(true);

        RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager = new RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager();
        chercherDecisionsAvasad(decisionJointTiersManager, idGestionnaire, idExecutionProcess, session);

        if (decisionJointTiersManager.size() <= 0) {
            throw new Exception(session.getLabel("PROCESS_VALIDER_DECISION_BODY_MAIL_AUCUNE_DECISION"));
        }

        rechercherDecisions(decisionJointTiersManager, decisionArray, session);

        return getDocumentDecisionOoAvasad(session, emailAdress, decisionArray, pdfDecisionURL, dateSurDocument,
                docData, false, isSimulation, "", false, process, memoryLog, true);

    }

    public void recupererDecisions(String idGestionnaire, String idExecutionProcess, BSession session,
            ArrayList<RFDecisionDocumentData> decisionArray) throws Exception {
        RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager = new RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager();
        chercherDecisionsAvasad(decisionJointTiersManager, idGestionnaire, idExecutionProcess, session);

        if (decisionJointTiersManager.size() <= 0) {
            throw new Exception(session.getLabel("PROCESS_VALIDER_DECISION_BODY_MAIL_AUCUNE_DECISION"));
        }

        rechercherDecisions(decisionJointTiersManager, decisionArray, session);
    }

    public void logErreurs(String emailAdresse, FWMemoryLog memoryLog, Exception e,
            RFValiderDecisionAvasadProcess process, BSession session, BTransaction transaction) {

        JadeLogger.error(process, e.toString());
        e.printStackTrace();
        memoryLog.logMessage(e.getMessage(), FWMessage.ERREUR, "RFValiderDecisionsProcess.run())");

        if (transaction != null) {
            transaction.setRollbackOnly();
        }

        try {

            envoyerMail(emailAdresse, session.getLabel("PROCESS_VALIDER_DECISIONS_FAILED"), memoryLog
                    .getMessagesInHtml().replaceAll("</br>", "<br>"), null);

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

}
