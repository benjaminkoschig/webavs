package ch.globaz.pegasus.rpc.domaine;

import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class RpcDecisionWithIdPlanCal {
    private final Decision decision;
    private final String idPlanCalcul;
    private final RoleMembreFamille roleMembreFamille;

    public RpcDecisionWithIdPlanCal(Decision decision) {
        this.decision = decision;
        idPlanCalcul = null;
        roleMembreFamille = null;
    }

    public RpcDecisionWithIdPlanCal(Decision decision, String idPlanCalcul, RoleMembreFamille roleMembreFamille) {
        this.decision = decision;
        this.idPlanCalcul = idPlanCalcul;
        this.roleMembreFamille = roleMembreFamille;
    }

    public Decision getDecision() {
        return decision;
    }

    public String getIdPlanCalcul() {
        return idPlanCalcul;
    }

    public RoleMembreFamille getRoleMembreFamille() {
        return roleMembreFamille;
    }

}
