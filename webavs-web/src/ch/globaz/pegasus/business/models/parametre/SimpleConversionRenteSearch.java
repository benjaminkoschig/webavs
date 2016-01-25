package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleConversionRenteSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAge = null;
    private String forDateDebut = null;
    private String forDateFin = null;

    /**
     * @return the forAge
     */
    public String getForAge() {
        return forAge;
    }

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @param forAge
     *            the forAge to set
     */
    public void setForAge(String forAge) {
        this.forAge = forAge;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    @Override
    public Class<SimpleConversionRente> whichModelClass() {
        return SimpleConversionRente.class;
    }

}
