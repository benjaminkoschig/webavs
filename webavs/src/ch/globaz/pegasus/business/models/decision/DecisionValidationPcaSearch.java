package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DecisionValidationPcaSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forInIdVersionDroit = null;

    public String getForInIdVersionDroit() {
        return forInIdVersionDroit;
    }

    public void setForInIdVersionDroit(String forInIdVersionDroit) {
        this.forInIdVersionDroit = forInIdVersionDroit;
    }

    @Override
    public Class<DecisionValidationPca> whichModelClass() {
        return DecisionValidationPca.class;
    }
}
