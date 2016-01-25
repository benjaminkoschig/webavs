package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class GroupeLocaliteSearchSimpleModel extends JadeSearchSimpleModel {

    private static final long serialVersionUID = 2543805631189692343L;

    private String forId;
    private String forNoGroupe;
    private String forTypeGroupe;

    public String getForId() {
        return forId;
    }

    public String getForNoGroupe() {
        return forNoGroupe;
    }

    public void setForNoGroupe(String forNoGroupe) {
        this.forNoGroupe = forNoGroupe;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<GroupeLocaliteSimpleModel> whichModelClass() {
        return GroupeLocaliteSimpleModel.class;
    }

    public String getForTypeGroupe() {
        return forTypeGroupe;
    }

    public void setForTypeGroupe(String forTypeGroupe) {
        this.forTypeGroupe = forTypeGroupe;
    }
}
