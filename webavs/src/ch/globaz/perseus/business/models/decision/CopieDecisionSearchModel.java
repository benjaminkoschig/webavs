package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * 
 * @author MBO
 * 
 */

public class CopieDecisionSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCopieDecision = null;
    private String forIdDecision = null;
    private String forIdTiers = null;
    private String forRemarqueCopieDecision = null;

    public String getForIdCopieDecision() {
        return forIdCopieDecision;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForRemarqueCopieDecision() {
        return forRemarqueCopieDecision;
    }

    public void setForIdCopieDecision(String forIdCopieDecision) {
        this.forIdCopieDecision = forIdCopieDecision;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForRemarqueCopieDecision(String forRemarque) {
        forRemarqueCopieDecision = forRemarque;
    }

    @Override
    public Class whichModelClass() {
        return CopieDecision.class;
    }

}
