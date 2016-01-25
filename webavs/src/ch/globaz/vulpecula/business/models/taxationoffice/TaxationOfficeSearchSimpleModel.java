package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class TaxationOfficeSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 1L;

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<TaxationOfficeSimpleModel> whichModelClass() {
        return TaxationOfficeSimpleModel.class;
    }

}
