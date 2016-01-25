package globaz.cygnus.services.validerDecision;

public class RFValiderDecisionServiceFactory {

    public static RFGenererDecisionsGedComptabilistationService getRFGenererDecisionsGedComptabilistationService() {
        return new RFGenererDecisionsGedComptabilistationService();
    }

    public static RFValiderDecisionAvasadService getRFValiderDecisionAvasadService() {
        return new RFValiderDecisionAvasadService();
    }

    public static RFValiderDecisionNonValideeService getRFValiderDecisionNonValideeService() {
        return new RFValiderDecisionNonValideeService();
    }

    public static RFValiderDecisionUniqueService getRFValiderDecisionUniqueService() {
        return new RFValiderDecisionUniqueService();
    }

}
