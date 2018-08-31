/**
 *
 */
package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class LigneFactureAssociationProfessionnelleSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 3553507742090707047L;
    private String forId;
    private String forIdEntete;
    private String forIdAssociationCotisation;

    public String getForIdEntete() {
        return forIdEntete;
    }

    public void setForIdEntete(String forIdEntete) {
        this.forIdEntete = forIdEntete;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<LigneFactureAssociationProfessionnelleSimpleModel> whichModelClass() {
        return LigneFactureAssociationProfessionnelleSimpleModel.class;
    }

    public String getForIdAssociationCotisation() {
        return forIdAssociationCotisation;
    }

    public void setForIdAssociationCotisation(String forIdAssociationCotisation) {
        this.forIdAssociationCotisation = forIdAssociationCotisation;
    }
}
