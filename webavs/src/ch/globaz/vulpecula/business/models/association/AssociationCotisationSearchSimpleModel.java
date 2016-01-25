/**
 *
 */
package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class AssociationCotisationSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 3553507742090707047L;
    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<AssociationCotisationSimpleModel> whichModelClass() {
        return AssociationCotisationSimpleModel.class;
    }
}
