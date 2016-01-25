package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRenteVerseeATortSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer forIdRenteVerseeATort;

    public SimpleRenteVerseeATortSearchModel() {
        super();

        forIdRenteVerseeATort = null;
    }

    public Integer getForIdRenteVerseeATort() {
        return forIdRenteVerseeATort;
    }

    public void setForIdRenteVerseeATort(Integer forIdRenteVerseeATort) {
        this.forIdRenteVerseeATort = forIdRenteVerseeATort;
    }

    @Override
    public Class<? extends JadeSimpleModel> whichModelClass() {
        return SimpleRenteVerseeATort.class;
    }
}
