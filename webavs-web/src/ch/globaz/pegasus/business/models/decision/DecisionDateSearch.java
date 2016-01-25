package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DecisionDateSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdVersionDroit = null;

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    @Override
    public Class<DecisionDate> whichModelClass() {
        return DecisionDate.class;
    }

}
