package ch.globaz.vulpecula.business.models.comptabilite;

import globaz.jade.persistence.model.JadeSimpleModel;

public class RubriqueSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 3141586104883781932L;

    private String idRubrique;
    private String idExterne;

    public String getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    @Override
    public String getId() {
        return idRubrique;
    }

    @Override
    public void setId(String id) {
        idRubrique = id;
    }
}
