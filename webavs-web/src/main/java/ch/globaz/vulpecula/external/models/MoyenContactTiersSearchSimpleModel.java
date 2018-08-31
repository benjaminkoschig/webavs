package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class MoyenContactTiersSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -4187192807221321365L;

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<MoyenContactTiersSimpleModel> whichModelClass() {
        return MoyenContactTiersSimpleModel.class;
    }
}
