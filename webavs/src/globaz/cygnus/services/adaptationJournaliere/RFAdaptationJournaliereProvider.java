package globaz.cygnus.services.adaptationJournaliere;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDecision;

public class RFAdaptationJournaliereProvider {

    public static RFAdaptationJournaliereAbstractHandler getHandler(RFAdaptationJournaliereContext context,
            BSession session, BTransaction transaction, List<String[]> logsList, String gestionnaire,
            boolean isAdaptationAnnuelle) throws Exception {

        if (!isAdaptationAnnuelle) {
            // Si suppresion -> pas de création nouvelle Qd + Réallouer l'argent non payé sur la Qd si suppression frqp
            // PP ou régime + diminution de la prestation + màj date de fin de traitement dans la demande -> A Voir
            // Si octroi ou octroi partiel -> création nouvelle Qd

            if (context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_SUPPRESSION_SC)) {

                return new RFAdaptationJournaliereSuppressionHandler(context, session, transaction, logsList,
                        gestionnaire, false);

            } else if (context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_OCTROI_AC)
                    || context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_PARTIEL_AC)
                    || context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_REFUS_AC)) {

                return new RFAdaptationJournaliereOctroiHandler(context, session, transaction, logsList, gestionnaire,
                        false);

            } else {
                throw new Exception("RFAdaptationJournaliereProvider.getHandler(): handler introuvable");
            }
        } else {
            return new RFAdaptationJournaliereOctroiHandler(context, session, transaction, logsList, gestionnaire, true);
        }

    }
}
