/**
 * 
 */
package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Permet d'effecteur des recherches de congé payé
 * 
 * @since WebBMS 0.01.04
 */
public class CongePayeSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private String forIdPosteTravail;

    public static final String WHERE_WITHDATE = "withPeriode";

    @Override
    public Class<CongePayeSimpleModel> whichModelClass() {
        return CongePayeSimpleModel.class;
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
}
