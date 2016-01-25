package globaz.aries.vb.decisioncgas;

import globaz.globall.db.BSessionUtil;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;

public class ARDecisionCgasSearchLineViewBean {

    private SimpleDecisionCGAS decisionCgas = null;

    public ARDecisionCgasSearchLineViewBean(SimpleDecisionCGAS theDecisionCgas) {
        decisionCgas = theDecisionCgas;
    }

    public String getAnnee() {
        return decisionCgas.getAnnee();
    }

    public String getCodeSystemEtat() {
        return decisionCgas.getEtat();
    }

    public String getCodeSystemType() {
        return decisionCgas.getType();
    }

    public String getCotisationPeriode() {
        return decisionCgas.getCotisationPeriode();
    }

    public String getDateDecision() {
        return decisionCgas.getDateDonnees();
    }

    public String getEtat() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(decisionCgas.getEtat());
    }

    public String getIdAffiliation() {
        return decisionCgas.getIdAffiliation();
    }

    public String getIdDecision() {
        return decisionCgas.getIdDecision();
    }

    public String getIdDecisionRectifiee() {
        return decisionCgas.getIdDecisionRectifiee();
    }

    public String getPeriode() {
        return decisionCgas.getDateDebut() + " - " + decisionCgas.getDateFin();
    }

    public String getType() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(decisionCgas.getType());
    }

}
