package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class AffiliationSyndicatSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -8392862412316850826L;

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<AffiliationSyndicatSimpleModel> whichModelClass() {
        return AffiliationSyndicatSimpleModel.class;
    }

}
