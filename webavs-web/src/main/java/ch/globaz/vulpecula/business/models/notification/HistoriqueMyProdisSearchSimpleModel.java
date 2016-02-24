package ch.globaz.vulpecula.business.models.notification;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class HistoriqueMyProdisSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 3526047480878910949L;

    private String forIdTiers;

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    @Override
    public Class<HistoriqueMyProdisSimpleModel> whichModelClass() {
        return HistoriqueMyProdisSimpleModel.class;
    }
}
