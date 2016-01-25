/**
 *
 */
package ch.globaz.vulpecula.business.models.traduction;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class TraductionSearchSimpleModel extends JadeSearchSimpleModel {

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<TraductionSimpleModel> whichModelClass() {
        return TraductionSimpleModel.class;
    }
}
