/**
 *
 */
package ch.globaz.vulpecula.business.models.registres;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ParametreCotisationAssociationSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 3553507742090707047L;
    private String forId;
    private String forIdCotisationAssociationProfessionnelle;
    private String forGenreNot;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdCotisationAssociationProfessionnelle() {
        return forIdCotisationAssociationProfessionnelle;
    }

    public void setForIdCotisationAssociationProfessionnelle(String forIdCotisationAssociationProfessionnelle) {
        this.forIdCotisationAssociationProfessionnelle = forIdCotisationAssociationProfessionnelle;
    }

    public String getForGenreNot() {
        return forGenreNot;
    }

    public void setForGenreNot(String forGenreNot) {
        this.forGenreNot = forGenreNot;
    }

    @Override
    public Class<ParametreCotisationAssociationSimpleModel> whichModelClass() {
        return ParametreCotisationAssociationSimpleModel.class;
    }
}
