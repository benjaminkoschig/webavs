package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class DetailGroupeLocaliteSimpleModel extends JadeSimpleModel {

    private static final long serialVersionUID = -1211963644797977796L;

    private String id;
    private String idGroupeLocalite;
    private String idLocalite;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdGroupeLocalite() {
        return idGroupeLocalite;
    }

    public void setIdGroupeLocalite(String idGroupeLocalite) {
        this.idGroupeLocalite = idGroupeLocalite;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

}
