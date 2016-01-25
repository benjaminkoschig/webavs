package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class LigneTaxationSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Class<LigneTaxationSimpleModel> whichModelClass() {
        return LigneTaxationSimpleModel.class;
    }
}
