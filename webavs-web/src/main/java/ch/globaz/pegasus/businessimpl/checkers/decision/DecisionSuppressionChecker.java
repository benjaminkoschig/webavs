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
     * Validation d'une d�cision de suppression avant la cr�ation
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

        // l'�tat de la demande ne doit pas �tre dans un �tat consid�r� comme non valide pour la cr�ation de la d�cision
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
     * l'�tat de la demande est consid�r� comme non valide pour la cr�ation d'une d�cision de suppression si cet �tat
     * est supprim�, transf�r�, refus� ou renonc�
     * 
     * @param etatDemande
     * @return
     */
    private static boolean isEtatDemandeNonValide(String etatDemande) {
        return IPCDemandes.CS_SUPPRIME.equals(etatDemande) || IPCDemandes.CS_TRANSFERE.equals(etatDemande)
                || IPCDemandes.CS_REFUSE.equals(etatDemande) || IPCDemandes.CS_RENONCE.equals(etatDemande);
    }
}
