package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import java.util.List;

/**
 * Modèle de recherche pour {@link AdhesionCotisationPosteTravailComplexModel}
 * 
 */
public class AdhesionCotisationPosteTravailSearchComplexModel extends JadeSearchComplexModel {
    private String forId;
    private String forIdPosteTravail;
    private Collection<String> forIdIn;
    private String forIdEmployeur;
    private String forIdCotisation;

    public Collection<String> getForIdIn() {
        return forIdIn;
    }

    public void setForIdIn(final Collection<String> forIdIn) {
        this.forIdIn = forIdIn;
    }

    public void setInId(final List<String> ids) {
        forIdIn = ids;
    }

    public String getForIdCotisation() {
        return forIdCotisation;
    }

    public void setForIdCotisation(String forIdCotisation) {
        this.forIdCotisation = forIdCotisation;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    public void setForIdPosteTravail(String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    @Override
    public Class<AdhesionCotisationPosteTravailComplexModel> whichModelClass() {
        return AdhesionCotisationPosteTravailComplexModel.class;
    }
}
