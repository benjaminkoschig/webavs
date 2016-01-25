package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DetailGroupeLocaliteSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -6554506554411471387L;

    public static final String ORDER_BY_ID_DESC = "idDesc";
    public static final String ORDER_BY_ID_GROUPE_LOCALITE_DESC = "idGroupeLocaliteDesc";

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

    public String getForIdLocalite() {
        return forIdLocalite;
    }

    public void setForIdLocalite(String forIdLocalite) {
        this.forIdLocalite = forIdLocalite;
    }

    @Override
    public Class<DetailGroupeLocaliteComplexModel> whichModelClass() {
        return DetailGroupeLocaliteComplexModel.class;
    }
}
