/**
 *
 */
package ch.globaz.vulpecula.business.models.registres;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ParametreCotisationAssociationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 1L;

    @Override
    public Class<ParametreCotisationAssociationComplexModel> whichModelClass() {
        return ParametreCotisationAssociationComplexModel.class;
    }

    private String forId;
    private String forIdCotisationAssociationProfessionnelle;
    private String forGenre;
    private String forGenreNot;
    private String forLibelleLike;

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

    public String getForGenre() {
        return forGenre;
    }

    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    public String getForGenreNot() {
        return forGenreNot;
    }

    public void setForGenreNot(String forGenreNot) {
        this.forGenreNot = forGenreNot;
    }

    public String getForLibelleLike() {
        return forLibelleLike;
    }

    public void setForLibelleLike(String forLibelleLike) {
        this.forLibelleLike = "%" + forLibelleLike;
    }
}
