package globaz.cygnus.services.validerDecision;

import globaz.cygnus.db.decisions.RFCopieDecision;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.process.RFGenererDecisionsGedComptabilisationProcess;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import java.util.ArrayList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RFGenererDecisionsGedComptabilistationService extends RFValiderDecisionService {

    private void chercherDecisionsAValider(
            RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager,
            String idGestionnaire, BSession session, String[] listeIdsDecisions) throws Exception {

        decisionJointTiersManager.setSession(session);
        decisionJointTiersManager.setForListeIdsDecisions(listeIdsDecisions);

        if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
            decisionJointTiersManager.setIdGestionnaire(idGestionnaire);
        }

        decisionJointTiersManager.setForOrderBy(RFDecision.FIELDNAME_ID_DECISION + "," + RFDemande.FIELDNAME_ID_DEMANDE
                + "," + RFCopieDecision.FIELDNAME_ID_COPIE + "," + RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);

        // I140902_024 : erreur de mise en ged des décisions AVASAD
        decisionJointTiersManager.setForIdExecutionProcess("withAndWithoutIdProcess");

        decisionJointTiersManager.changeManagerSize(0);
        decisionJointTiersManager.find();
    }

    public void commitTransaction(BTransaction transaction) {

        if ((transaction != null)) {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
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

    public JadePrintDocumentContainer genererDecisionDocument(DocumentData docData, String emailAdress,
            String idGestionnaire, boolean isMiseEnGed, FWMemoryLog memoryLog,
            ArrayList<RFDecisionDocumentData> decisionArray, RFGenererDecisionsGedComptabilisationProcess process,
            String[] listeIdsDecisions, String idLot, BSession session, BTransaction transaction) throws Exception {

        memoryLog.setSession(session);

        // TODO : Pour debug lors de la génération de document
        // TopazSystem.getInstance().getDocBuilder().setOpenedDocumentsVisible(true);

        RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager = new RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager();
        chercherDecisionsAValider(decisionJointTiersManager, idGestionnaire, session, listeIdsDecisions);

        if (decisionJointTiersManager.size() <= 0) {
            throw new Exception(session.getLabel("JSP_PROCESS_MISE_EN_GED_DECISIONS_ECHEC") + idLot);
        }

        rechercherDecisions(decisionJointTiersManager, decisionArray, session);

        return getDocumentDecisionOo(session, emailAdress, decisionArray, new StringBuffer(), "", docData, isMiseEnGed,
                false, idLot, false, process, memoryLog);

    }

    public void logErreurs(String emailAdresse, String idLot, FWMemoryLog memoryLog, Exception e,
            RFGenererDecisionsGedComptabilisationProcess process, BSession session, BTransaction transaction) {

        JadeLogger.error(process, e.toString());
        e.printStackTrace();

        if (transaction != null) {
            transaction.setRollbackOnly();
        }

        try {

            envoyerMail(emailAdresse, session.getLabel("PROCESS_MISE_EN_GED_COMPTABILISATION_ECHEC_TITRE"),
                    session.getLabel("PROCESS_MISE_EN_GED_ECHEC__DECISIONS_COMPTABILISATION_LOT") + idLot + "\n" + "\n"
                            + e.getMessage().toString(), null);

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

}
