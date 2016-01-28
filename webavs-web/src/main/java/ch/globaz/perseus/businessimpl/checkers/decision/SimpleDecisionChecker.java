package ch.globaz.perseus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.models.decision.SimpleDecision;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleDecisionChecker extends PerseusAbstractChecker {

    /**
     * @param simpleDecision
     */
    public static void checkForCreate(SimpleDecision simpleDecision) {
        SimpleDecisionChecker.checkMandatory(simpleDecision);
    }

    /**
     * @param simpleDecision
     * @throws JadePersistenceException
     */
    public static void checkForDelete(SimpleDecision simpleDecision) throws JadePersistenceException {
        // Lecture de la demande
        Demande demande = new Demande();
        demande.getSimpleDemande().setId(simpleDecision.getIdDemande());
        demande = (Demande) JadePersistenceManager.read(demande);

        if (CSEtatDemande.VALIDE.getCodeSystem().equals(demande.getSimpleDemande().getCsEtatDemande())) {
            System.out.println("Decision ID = " + simpleDecision.getId() + ", Demande ID = "
                    + simpleDecision.getIdDemande() + ", Etat = " + simpleDecision.getCsEtat()
                    + ", Type de décision = " + simpleDecision.getCsTypeDecision() + ", Date du document = "
                    + simpleDecision.getDateDocument() + ", Date de préparation = "
                    + simpleDecision.getDatePreparation() + ", Date de validation = "
                    + simpleDecision.getDateValidation() + ", Date de suppression = "
                    + simpleDecision.getDateSuppression() + ", Numero de décision = "
                    + simpleDecision.getNumeroDecision() + ", Date du montant touché au RI "
                    + simpleDecision.getMontantToucheAuRI() + ", Spy = " + simpleDecision.getSpy() + ", CSpy = "
                    + simpleDecision.getCreationSpy() + ", Remarque utilisateur = "
                    + simpleDecision.getRemarqueUtilisateur() + ", Utilisateur de la préparation = "
                    + simpleDecision.getUtilisateurPreparation() + ", Utilisateur de la validation = "
                    + simpleDecision.getUtilisateurValidation());

            JadeThread
                    .logError(SimpleDecisionChecker.class.getName(), "perseus.decision.simpledecision.checkfordelete");
        }
    }

    /**
     * @param simpleDecision
     */
    public static void checkForUpdate(SimpleDecision simpleDecision) {
        SimpleDecisionChecker.checkMandatory(simpleDecision);
    }

    /**
     * @param simpleDecision
     */
    private static void checkMandatory(SimpleDecision simpleDecision) {

        if (JadeStringUtil.isIntegerEmpty(simpleDecision.getDatePreparation())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.decision.simpledecision.datepreparation.mandatory");
        }
        if (JadeStringUtil.isIntegerEmpty(simpleDecision.getDateDocument())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.decision.simpledecision.datedocument.mandatory");
        }
        if (CSTypeDecision.SUPPRESSION.getCodeSystem().equals(simpleDecision.getCsTypeDecision())
                && JadeStringUtil.isIntegerEmpty(simpleDecision.getDateSuppression())) {
            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                    "perseus.decision.simpledecision.datesuppression.mandatory");
        }

    }
}
