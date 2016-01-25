package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class DetailGroupeLocaliteSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -4187192807221321365L;

    private String forId;
    private String forIdGroupeLocalite;
    private String forIdLocalite;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdGroupeLocalite() {
        return forIdGroupeLocalite;
    }

    public void setForIdGroupeLocalite(String forIdGroupeLocalite) {
        this.forIdGroupeLocalite = forIdGroupeLocalite;
    }

    @Override
    public Class<DetailGroupeLocaliteSimpleModel> whichModelClass() {
        return DetailGroupeLocaliteSimpleModel.class;
    }

    public String getForIdLocalite() {
        return forIdLocalite;
    }

    public void setForIdLocalite(String forIdLocalite) {
        this.forIdLocalite = forIdLocalite;
    }

}
