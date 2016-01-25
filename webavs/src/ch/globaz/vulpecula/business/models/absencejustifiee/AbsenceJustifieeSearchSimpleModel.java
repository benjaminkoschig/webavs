package ch.globaz.vulpecula.business.models.absencejustifiee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class AbsenceJustifieeSearchSimpleModel extends JadeSearchSimpleModel {

    private static final long serialVersionUID = 1L;

    private String forId;
    private String forIdPosteTravail;

    @Override
    public Class<AbsenceJustifieeSimpleModel> whichModelClass() {
        return AbsenceJustifieeSimpleModel.class;
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
