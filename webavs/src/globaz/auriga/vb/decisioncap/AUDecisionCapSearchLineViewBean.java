package globaz.auriga.vb.decisioncap;

import globaz.globall.db.BSessionUtil;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;

public class AUDecisionCapSearchLineViewBean {
    private SimpleDecisionCAP decisionCap = null;

    public AUDecisionCapSearchLineViewBean(SimpleDecisionCAP decisionCap) {
        this.decisionCap = decisionCap;
    }

    public String getAnnee() {
        return decisionCap.getAnnee();
    }

    public String getCodeSystemEtat() {
        return decisionCap.getEtat();
    }

    public String getCotisationAnnuelle() {
        return decisionCap.getCotisationAnnuelle();
    }

    public String getCotisationBrute() {
        return decisionCap.getCotisationBrute();
    }

    public String getCotisationMensuelle() {
        return decisionCap.getCotisationMensuelle();
    }

    public String getCotisationPeriode() {
        return decisionCap.getCotisationPeriode();
    }

    public String getCotisationTrimestrielle() {
        return decisionCap.getCotisationTrimestrielle();
    }

    public String getDateDebut() {
        return decisionCap.getDateDebut();
    }

    public String getDateDonnees() {
        return decisionCap.getDateDonnees();
    }

    public String getDateFin() {
        return decisionCap.getDateFin();
    }

    public String getEtat() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(decisionCap.getEtat());
    }

    public String getIdAffiliation() {
        return decisionCap.getIdAffiliation();
    }

    public String getIdDecisionCap() {
        return decisionCap.getIdDecision();
    }

    public String getIdDecisionRectifiee() {
        return decisionCap.getIdDecisionRectifiee();
    }

    public String getPeriode() {
        return getDateDebut() + " - " + getDateFin();
    }

    public String getPrestation() {
        return decisionCap.getPrestation();
    }

    public String getType() {
        return decisionCap.getType();
    }

    public String getTypeLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(getType());
    }
}
