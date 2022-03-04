package ch.globaz.eform.business.search;

import ch.globaz.eform.business.models.GFEFormModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class GFEFormSearch extends JadeSearchSimpleModel {
    private String forId = null;

    public GFEFormSearch() {
        super(JadeSearchSimpleModel.SIZE_NOLIMIT);
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<GFEFormModel> whichModelClass() {
        return GFEFormModel.class;
    }
}
