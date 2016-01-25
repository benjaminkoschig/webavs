package ch.globaz.amal.business.models.subsideannee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleSubsideAnneeSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeSubside = null;
    private String forIdSubsideAnnee = null;
    private String forLimiteRevenu = null;
    private String forLimiteRevenuGOE = null;
    private String forLimiteRevenuLOE = null;

    public String getForAnneeSubside() {
        return forAnneeSubside;
    }

    public String getForIdSubsideAnnee() {
        return forIdSubsideAnnee;
    }

    public String getForLimiteRevenu() {
        return forLimiteRevenu;
    }

    public String getForLimiteRevenuGOE() {
        return forLimiteRevenuGOE;
    }

    public String getForLimiteRevenuLOE() {
        return forLimiteRevenuLOE;
    }

    public void setForAnneeSubside(String forAnneeSubside) {
        this.forAnneeSubside = forAnneeSubside;
    }

    public void setForIdSubsideAnnee(String forIdSubsideAnnee) {
        this.forIdSubsideAnnee = forIdSubsideAnnee;
    }

    public void setForLimiteRevenu(String forLimiteRevenu) {
        this.forLimiteRevenu = forLimiteRevenu;
    }

    public void setForLimiteRevenuGOE(String forLimiteRevenuGOE) {
        this.forLimiteRevenuGOE = forLimiteRevenuGOE;
    }

    public void setForLimiteRevenuLOE(String forLimiteRevenuLOE) {
        this.forLimiteRevenuLOE = forLimiteRevenuLOE;
    }

    @Override
    public Class whichModelClass() {
        return SimpleSubsideAnnee.class;
    }

}
