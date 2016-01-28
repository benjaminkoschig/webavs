/**
 *
 */
package ch.globaz.vulpecula.business.models.postetravail;

import java.util.Collection;
import java.util.List;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author jpa
 * 
 */
public class AdhesionCotisationPosteTravailSearchSimpleModel extends JadeSearchSimpleModel {
	private static final long serialVersionUID = 1L;
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
    public Class<AdhesionCotisationPosteTravailSimpleModel> whichModelClass() {
        return AdhesionCotisationPosteTravailSimpleModel.class;
    }
}
