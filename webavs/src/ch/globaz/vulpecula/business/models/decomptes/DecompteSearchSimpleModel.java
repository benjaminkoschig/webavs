/**
 *
 */
package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author jpa
 * 
 */
public class DecompteSearchSimpleModel extends JadeSearchSimpleModel {
    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<DecompteSimpleModel> whichModelClass() {
        return DecompteSimpleModel.class;
    }
}
