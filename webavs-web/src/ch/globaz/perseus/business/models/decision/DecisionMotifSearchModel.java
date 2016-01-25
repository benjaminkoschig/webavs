package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DecisionMotifSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCommentaire = null;
    private String forIdDecision = null;
    private String forIdMotif = null;

    public String getForCommentaire() {
        return forCommentaire;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdMotif() {
        return forIdMotif;
    }

    public void setForCommentaire(String forCommentaire) {
        this.forCommentaire = forCommentaire;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdMotif(String forIdMotif) {
        this.forIdMotif = forIdMotif;
    }

    @Override
    public Class whichModelClass() {
        return DecisionMotif.class;
    }

}
