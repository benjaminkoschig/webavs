package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author JPA
 * 
 */
public class AssociationCotisationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 7865949937484423604L;

    private String forId;
    private String forIdEmployeur;

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

    @Override
    public Class<AssociationCotisationComplexModel> whichModelClass() {
        return AssociationCotisationComplexModel.class;
    }
}
