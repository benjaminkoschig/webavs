package ch.globaz.vulpecula.business.models.postetravail;

import java.util.Collection;
import java.util.List;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche pour {@link AdhesionCotisationPosteTravailComplexModel}
 * 
 */
public class AdhesionCotisationPosteTravailSearchComplexModel extends JadeSearchComplexModel {
    private String forId;
    private String forIdPosteTravail;
    private Collection<String> forIdIn;
    
    public Collection<String> getForIdIn() {
        return forIdIn;
    }

    public void setForIdIn(final Collection<String> forIdIn) {
        this.forIdIn = forIdIn;
    }

    public void setInId(final List<String> ids) {
        forIdIn = ids;
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

    @Override
    public Class<AdhesionCotisationPosteTravailComplexModel> whichModelClass() {
        return AdhesionCotisationPosteTravailComplexModel.class;
    }
}
