package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CotisationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 6717624959133765127L;

    private String forId;
    private String forIdAffilie;
    private String forDateDebutGreaterEquals;
    private String forDateDebutLessEquals;
    private String forDateFinGreaterEquals;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;

    }

    public String getForDateDebutGreaterEquals() {
        return forDateDebutGreaterEquals;
    }

    public void setForDateDebutGreaterEquals(String forDateDebut) {
        forDateDebutGreaterEquals = forDateDebut;
    }

    public String getForDateDebutLessEquals() {
        return forDateDebutLessEquals;
    }

    public void setForDateDebutLessEquals(String forDateDebutLessThan) {
        forDateDebutLessEquals = forDateDebutLessThan;
    }

    /**
     * @return the forDateFinGreaterEquals
     */
    public String getForDateFinGreaterEquals() {
        return forDateFinGreaterEquals;
    }

    /**
     * @param forDateFinGreaterEquals the forDateFinGreaterEquals to set
     */
    public void setForDateFinGreaterEquals(String forDateFinGreaterEquals) {
        this.forDateFinGreaterEquals = forDateFinGreaterEquals;
    }

    @Override
    public Class<CotisationComplexModel> whichModelClass() {
        return CotisationComplexModel.class;
    }

}
