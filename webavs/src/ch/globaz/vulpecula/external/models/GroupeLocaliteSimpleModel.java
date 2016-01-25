package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class GroupeLocaliteSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 3974853420161403412L;

    private String id;
    private String nomGroupeFR;
    private String nomGroupeDE;
    private String nomGroupeIT;
    private String noGroupe;
    private String typeGroupe;

    @Override
    public String getId() {
        return id;
    }

    public String getNomGroupeFR() {
        return nomGroupeFR;
    }

    public void setNomGroupeFR(String nomGroupeFR) {
        this.nomGroupeFR = nomGroupeFR;
    }

    public String getNomGroupeDE() {
        return nomGroupeDE;
    }

    public void setNomGroupeDE(String nomGroupeDE) {
        this.nomGroupeDE = nomGroupeDE;
    }

    public String getNomGroupeIT() {
        return nomGroupeIT;
    }

    public void setNomGroupeIT(String nomGroupeIT) {
        this.nomGroupeIT = nomGroupeIT;
    }

    public String getNoGroupe() {
        return noGroupe;
    }

    public void setNoGroupe(String noGroupe) {
        this.noGroupe = noGroupe;
    }

    public String getTypeGroupe() {
        return typeGroupe;
    }

    public void setTypeGroupe(String typeGroupe) {
        this.typeGroupe = typeGroupe;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
