package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class TauxCongePayeSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private String forIdCotisation;
    private String forIdCongePaye;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdCotisation() {
        return forIdCotisation;
    }

    public void setForIdCotisation(String forIdCotisation) {
        this.forIdCotisation = forIdCotisation;
    }

    public String getForIdCongePaye() {
        return forIdCongePaye;
    }

    public void setForIdCongePaye(String forIdCongePaye) {
        this.forIdCongePaye = forIdCongePaye;
    }

    @Override
    public Class<TauxCongePayeSimpleModel> whichModelClass() {
        return TauxCongePayeSimpleModel.class;
    }

}
