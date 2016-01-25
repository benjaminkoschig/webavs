package globaz.cygnus.services.validerDecision;

import globaz.cygnus.db.decisions.RFCopieDecision;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.process.RFValiderDecisionUniqueProcess;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import java.util.ArrayList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RFValiderDecisionUniqueService extends RFValiderDecisionService {

    private void chercherDecisionsAValider(String idDecision,
            RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager,
            String idGestionnaire, BSession session) throws Exception {

        decisionJointTiersManager.setSession(session);
        decisionJointTiersManager.setForIdDecision(idDecision);
        // Permet de générer le document de décsions pour la cas de l'avasad
        decisionJointTiersManager.setForIdExecutionProcess("withAndWithoutIdProcess");

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
            String emailAdress, String idGestionnaire, String idDecision, boolean isMiseEnGed, boolean isSimulation,
            FWMemoryLog memoryLog, StringBuffer pdfDecisionURL, ArrayList<RFDecisionDocumentData> decisionArray,
            RFValiderDecisionUniqueProcess process, BSession session, BTransaction transaction) throws Exception {

        memoryLog.setSession(session);

        // TODO : Pour debug lors de la génération de document
        // TopazSystem.getInstance().getDocBuilder().setOpenedDocumentsVisible(true);

        RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager = new RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager();
        chercherDecisionsAValider(idDecision, decisionJointTiersManager, idGestionnaire, session);

        if (decisionJointTiersManager.size() <= 0) {
            throw new Exception(session.getLabel("PROCESS_VALIDER_DECISION_BODY_MAIL_AUCUNE_DECISION"));
        }

        rechercherDecisions(decisionJointTiersManager, decisionArray, session);

        return getDocumentDecisionOo(session, emailAdress, decisionArray, pdfDecisionURL, dateSurDocument, docData,
                isMiseEnGed, isSimulation, null, true, process, memoryLog);

    }

    public void logErreurs(String emailAdresse, FWMemoryLog memoryLog, Exception e,
            RFValiderDecisionUniqueProcess process, BSession session, BTransaction transaction) {

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
