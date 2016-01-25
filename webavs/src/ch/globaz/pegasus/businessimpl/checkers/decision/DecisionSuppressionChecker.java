package ch.globaz.pegasus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * @author bjo
 * 
 */
public class DecisionSuppressionChecker extends PegasusAbstractChecker {

    /**
     * Validation d'une décision de suppression avant la création
     * 
     * @param decisionSuppression
     * @throws DecisionException
     */
    public static void checkForCreate(DecisionSuppression decisionSuppression) throws DecisionException {
        if (decisionSuppression == null) {
            throw new DecisionException("decisionSuppression cannot be null !!!");
        }

        Demande demande = decisionSuppression.getVersionDroit().getDemande();

        if (demande == null) {
            throw new DecisionException("demande cannot be null !!! decisionSuppressionId = "
                    + decisionSuppression.getId());
        }

        // l'état de la demande ne doit pas être dans un état considéré comme non valide pour la création de la décision
        // de suppression
        String etatDemande = demande.getSimpleDemande().getCsEtatDemande();
        if (isEtatDemandeNonValide(etatDemande)) {
            throw new DecisionException(JadeThread.getMessage("pegasus.droit.corriger.droitMotifDeces.isEtatNonValide"));
        }

        // la demande ne doit pas avoir de date de fin
        if (!JadeStringUtil.isBlankOrZero((demande.getSimpleDemande().getDateFin()))) {
            throw new DecisionException(
                    JadeThread.getMessage("pegasus.droit.corriger.droitMotifDeces.demande.hasDateFin"));
        }
    }

    /**
     * l'état de la demande est considéré comme non valide pour la création d'une décision de suppression si cet état
     * est supprimé, transféré, refusé ou renoncé
     * 
     * @param etatDemande
     * @return
     */
    private static boolean isEtatDemandeNonValide(String etatDemande) {
        return IPCDemandes.CS_SUPPRIME.equals(etatDemande) || IPCDemandes.CS_TRANSFERE.equals(etatDemande)
                || IPCDemandes.CS_REFUSE.equals(etatDemande) || IPCDemandes.CS_RENONCE.equals(etatDemande);
    }
}
