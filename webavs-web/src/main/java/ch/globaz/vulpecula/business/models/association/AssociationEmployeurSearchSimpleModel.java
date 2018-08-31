/**
 *
 */
package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class AssociationEmployeurSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 3553507742090707047L;
    private String forId;
    private String forIdEmployeur;
    private String forIdAssociation;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForIdAssociation() {
        return forIdAssociation;
    }

    public void setForIdAssociation(String forIdAssociation) {
        this.forIdAssociation = forIdAssociation;
    }

    @Override
    public Class<AssociationEmployeurSimpleModel> whichModelClass() {
        return AssociationEmployeurSimpleModel.class;
    }
}
