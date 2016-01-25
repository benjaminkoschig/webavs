package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TauxCongePayeSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -1062052951804785815L;

    private String forId;
    private String forIdCongePaye;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdCongePaye() {
        return forIdCongePaye;
    }

    public void setForIdCongePaye(String forIdCongePaye) {
        this.forIdCongePaye = forIdCongePaye;
    }

    @Override
    public Class<TauxCongePayeComplexModel> whichModelClass() {
        return TauxCongePayeComplexModel.class;
    }
}
