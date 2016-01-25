package ch.globaz.pegasus.business.models.dettecomptatcompense;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class DetteComptatCompenseSearch extends JadeSearchSimpleModel {
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
    public Class<SimpleDetteComptatCompense> whichModelClass() {
        return SimpleDetteComptatCompense.class;
    }
}
