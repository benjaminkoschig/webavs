package ch.globaz.vulpecula.business.models.comptabilite;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ReferenceRubriqueSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -7592443291078222484L;

    private String idReferenceRubrique;
    private String idRubrique;
    private String idCodeReference;

    public String getIdReferenceRubrique() {
        return idReferenceRubrique;
    }

    public void setIdReferenceRubrique(String idReferenceRubrique) {
        this.idReferenceRubrique = idReferenceRubrique;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public String getIdCodeReference() {
        return idCodeReference;
    }

    public void setIdCodeReference(String idCodeReference) {
        this.idCodeReference = idCodeReference;
    }

    @Override
    public String getId() {
        return idReferenceRubrique;
    }

    @Override
    public void setId(String id) {
        idReferenceRubrique = id;
    }

}
