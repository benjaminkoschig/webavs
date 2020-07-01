package globaz.cygnus.services.validerDecision;

public class RFValiderDecisionServiceFactory {

    public static RFGenererDecisionsGedComptabilistationService getRFGenererDecisionsGedComptabilistationService() {
        return new RFGenererDecisionsGedComptabilistationService();
    }

    public static RFValiderDecisionAvasadService getRFValiderDecisionAvasadService() {
        return new RFValiderDecisionAvasadService();
    }

    public static RFValiderDecisionSecutelService getRFValiderDecisionSecutelService() {
        return new RFValiderDecisionSecutelService();
    }

    public static RFValiderDecisionTmrService getRFValiderDecisionTmrService() {
        return new RFValiderDecisionTmrService();
    }

    public static RFValiderDecisionNonValideeService getRFValiderDecisionNonValideeService() {
        return new RFValiderDecisionNonValideeService();
    }

    public static RFValiderDecisionUniqueService getRFValiderDecisionUniqueService() {
        return new RFValiderDecisionUniqueService();
    }

}
