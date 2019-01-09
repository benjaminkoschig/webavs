package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PcaDecisionHistoriseeSearch  extends JadeSearchComplexModel {
    
    private String forDateValidite;
    private String forDateDecision;
    private String forIdPcaParent;
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class<PcaDecisionHistorisee> whichModelClass() {
        return PcaDecisionHistorisee.class;
    }

    public String getForDateValidite() {
        return forDateValidite;
    }

    public void setForDateValidite(String forDateValidite) {
        this.forDateValidite = forDateValidite;
    }

    public String getForDateDecision() {
        return forDateDecision;
    }

    public void setForDateDecision(String forDateDecision) {
        this.forDateDecision = forDateDecision;
    }

    public String getForIdPcaParent() {
        return forIdPcaParent;
    }

    public void setForIdPcaParent(String forIdPcaParent) {
        this.forIdPcaParent = forIdPcaParent;
    }
    
    

}
