package ch.globaz.vulpecula.business.models.comptabilite;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class RubriqueSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 3536398057705373935L;

    private String forId;
    private String forIdExterne;
    private String likeIdExterne;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdExterne() {
        return forIdExterne;
    }

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    @Override
    public Class<RubriqueSimpleModel> whichModelClass() {
        return RubriqueSimpleModel.class;
    }

}
