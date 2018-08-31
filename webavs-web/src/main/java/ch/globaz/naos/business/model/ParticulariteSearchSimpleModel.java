package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ParticulariteSearchSimpleModel extends JadeSearchSimpleModel {

    private static final long serialVersionUID = -7082435733248476823L;

    private String forParticularite = "";
    private String forIdAffiliation = "";
    private String forDateDebutLessOrEquals;
    private String forPeriodeDebutLessOrEquals;

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public String getForParticularite() {
        return forParticularite;
    }

    public void setForParticularite(String forParticularite) {
        this.forParticularite = forParticularite;
    }

    public String getForDateDebutLessOrEquals() {
        return forDateDebutLessOrEquals;
    }

    public void setForDateDebutLessOrEquals(String forDateDebutLessOrEquals) {
        this.forDateDebutLessOrEquals = forDateDebutLessOrEquals;
    }

    /**
     * @return the forPeriodeDebutLessOrEquals
     */
    public String getForPeriodeDebutLessOrEquals() {
        return forPeriodeDebutLessOrEquals;
    }

    /**
     * @param forPeriodeDebutLessOrEquals the forPeriodeDebutLessOrEquals to set
     */
    public void setForPeriodeDebutLessOrEquals(String forPeriodeDebutLessOrEquals) {
        this.forPeriodeDebutLessOrEquals = forPeriodeDebutLessOrEquals;
    }

    @Override
    public Class<ParticulariteSimpleModel> whichModelClass() {
        return ParticulariteSimpleModel.class;
    }
}
