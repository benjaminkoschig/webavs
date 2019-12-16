package ch.globaz.al.impotsource.models;

import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;

public class TauxImpositionSearchSimpleModel extends DomaineJadeAbstractSearchModel {
    private static final long serialVersionUID = 296951735253843848L;

    private String forCanton;
    private String forTypeImposition;

    @Override
    public Class<TauxImpositionSimpleModel> whichModelClass() {
        return TauxImpositionSimpleModel.class;
    }

    public String getForCanton() {
        return forCanton;
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    public String getForTypeImposition() {
        return forTypeImposition;
    }

    public void setForTypeImposition(String forTypeImposition) {
        this.forTypeImposition = forTypeImposition;
    }

    public void setForTypeImpostiion(TypeImposition forTypeImposition) {
        this.forTypeImposition = forTypeImposition.getValue();
    }
}
