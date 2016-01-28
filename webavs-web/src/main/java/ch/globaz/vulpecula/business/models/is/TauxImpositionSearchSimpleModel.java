package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import ch.globaz.vulpecula.domain.models.is.TypeImposition;

public class TauxImpositionSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 296951735253843848L;

    private String forId;
    private String forCanton;
    private String forTypeImposition;

    @Override
    public Class<TauxImpositionSimpleModel> whichModelClass() {
        return TauxImpositionSimpleModel.class;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
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
