package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ParametreSyndicatSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 5337275645943635214L;

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<ParametreSyndicatSimpleModel> whichModelClass() {
        return ParametreSyndicatSimpleModel.class;
    }

}
