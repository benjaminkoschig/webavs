package ch.globaz.eform.business.search;

import ch.globaz.eform.business.models.GFSuiviModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class GFSuiviSearch extends JadeSearchSimpleModel {

    public GFSuiviSearch() {
        super(JadeSearchSimpleModel.SIZE_NOLIMIT);
    }

    @Override
    public Class whichModelClass() {
        return GFSuiviModel.class;
    }
}
