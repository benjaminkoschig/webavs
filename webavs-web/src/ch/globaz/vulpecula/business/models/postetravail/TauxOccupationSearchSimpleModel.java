/**
 *
 */
package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author jpa
 * 
 */
public class TauxOccupationSearchSimpleModel extends JadeSearchSimpleModel {
    private String forIdPosteTravail;

    private String forIdTauxOccupation;

    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    public String getForIdTauxOccupation() {
        return forIdTauxOccupation;
    }

    public void setForIdPosteTravail(String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    public void setForIdTauxOccupation(String forIdTauxOccupation) {
        this.forIdTauxOccupation = forIdTauxOccupation;
    }

    @Override
    public Class<TauxOccupationSimpleModel> whichModelClass() {
        return TauxOccupationSimpleModel.class;
    }
}
