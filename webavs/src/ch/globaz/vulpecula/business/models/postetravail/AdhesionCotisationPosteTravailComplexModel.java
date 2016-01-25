package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;

/**
 * Représentation en base de données de la relation entre une adhésion à une cotisation de poste de travail, une
 * assurance et une cotisation.
 * 
 */
public class AdhesionCotisationPosteTravailComplexModel extends JadeComplexModel {
    private AdhesionCotisationPosteTravailSimpleModel adhesionCotisationPosteTravail;
    private CotisationComplexModel cotisationComplexModel;

    public AdhesionCotisationPosteTravailComplexModel() {
        adhesionCotisationPosteTravail = new AdhesionCotisationPosteTravailSimpleModel();
        cotisationComplexModel = new CotisationComplexModel();
    }

    public AdhesionCotisationPosteTravailSimpleModel getAdhesionCotisationPosteTravail() {
        return adhesionCotisationPosteTravail;
    }

    public void setAdhesionCotisationsPosteTravail(
            final AdhesionCotisationPosteTravailSimpleModel adhesionCotisationsPosteTravail) {
        adhesionCotisationPosteTravail = adhesionCotisationsPosteTravail;
    }

    public CotisationComplexModel getCotisationComplexModel() {
        return cotisationComplexModel;
    }

    public void setCotisationComplexModel(final CotisationComplexModel cotisationComplexModel) {
        this.cotisationComplexModel = cotisationComplexModel;
    }

    public void setAdhesionCotisationPosteTravail(
            final AdhesionCotisationPosteTravailSimpleModel adhesionCotisationPosteTravail) {
        this.adhesionCotisationPosteTravail = adhesionCotisationPosteTravail;
    }

    @Override
    public String getId() {
        return adhesionCotisationPosteTravail.getId();
    }

    @Override
    public String getSpy() {
        return adhesionCotisationPosteTravail.getSpy();
    }

    @Override
    public void setId(final String id) {
        adhesionCotisationPosteTravail.setId(id);
    }

    @Override
    public void setSpy(final String spy) {
        adhesionCotisationPosteTravail.setSpy(spy);
    }
}
