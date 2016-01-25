package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;

public class ValiderDecisionUtils {
    public static String DROIT_INITIAL = "1";

    private static Map<String, List<DecisionApresCalcul>> groupByDateDebut(List<DecisionApresCalcul> decisions)
            throws DecisionException {
        if ((decisions == null) || (decisions.size() == 0)) {
            throw new DecisionException("Unable to resolveLastDecisions, the decisions passed is null! or empty");
        }
        Map<String, List<DecisionApresCalcul>> mapDecisions = JadeListUtil.groupBy(decisions,
                new Key<DecisionApresCalcul>() {
                    @Override
                    public String exec(DecisionApresCalcul e) {
                        return e.getPcAccordee().getSimplePCAccordee().getDateDebut();
                    }
                });
        return mapDecisions;
    }

    public static boolean isDroitInitial(String noVersionDroitCourant) {
        return (ValiderDecisionUtils.DROIT_INITIAL).equals(noVersionDroitCourant);
    }

    public static boolean isDroitInitial(ValiderDecisionAcData validerDecisionAc) {
        return ValiderDecisionUtils.isDroitInitial(validerDecisionAc.getNoVersionDroit());
    }

    public static DecisionApresCalcul resolvedLastDecisionRequerant(List<DecisionApresCalcul> decisions,
            String lastDateDebut) throws DecisionException {
        List<DecisionApresCalcul> lastDecision = ValiderDecisionUtils.resolveLastDecisions(decisions, lastDateDebut);
        for (DecisionApresCalcul dc : lastDecision) {
            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(dc.getPcAccordee().getSimplePCAccordee()
                    .getCsRoleBeneficiaire())) {
                return dc;
            }
        }
        throw new DecisionException("Unable to resoloved the last decision for the requerant");
    }

    public static List<DecisionApresCalcul> resolveLastDecisions(List<DecisionApresCalcul> decisions,
            String lastDateDebut) throws DecisionException {
        if ((decisions == null) || (decisions.size() == 0)) {
            throw new DecisionException("Unable to resolveLastDecisions, the decisions passed is null! or empty");
        }
        if (lastDateDebut == null) {
            throw new DecisionException("Unable to resolveLastDecisions , the lastDateDebut is null!");
        }
        Map<String, List<DecisionApresCalcul>> mapDecisions = ValiderDecisionUtils.groupByDateDebut(decisions);
        List<DecisionApresCalcul> decisionsReturn = mapDecisions.get(lastDateDebut);
        if (decisionsReturn.size() > 3) {
            throw new DecisionException("Too many decsions was founded with this date: " + lastDateDebut);
        }
        return decisionsReturn;
    }

    // private boolean isDecisionCourante(DecisionApresCalcul decision) {
    // return JadeStringUtil
    // .isBlankOrZero(decision.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision());
    // }
}
