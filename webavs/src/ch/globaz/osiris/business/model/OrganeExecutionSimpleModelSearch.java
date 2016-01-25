package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class OrganeExecutionSimpleModelSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class<OrganeExecutionSimpleModel> whichModelClass() {
        return OrganeExecutionSimpleModel.class;
    }

}
