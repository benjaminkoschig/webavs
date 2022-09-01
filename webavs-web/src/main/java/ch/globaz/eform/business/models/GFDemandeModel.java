package ch.globaz.eform.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class GFDemandeModel extends JadeSimpleModel {
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
