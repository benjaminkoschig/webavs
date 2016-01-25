package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleLienZoneLocaliteSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String WITH_DATE_VALABLE_LE = "withDateValable";
    private String forDateDebut = null;
    private String forDateValable = null;
    private String forIdLocalite = null;
    private String forIdZoneForfait = null;

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forIdLocalite
     */
    public String getForIdLocalite() {
        return forIdLocalite;
    }

    /**
     * @return the forIdZoneForfait
     */
    public String getForIdZoneForfait() {
        return forIdZoneForfait;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forIdLocalite
     *            the forIdLocalite to set
     */
    public void setForIdLocalite(String forIdLocalite) {
        this.forIdLocalite = forIdLocalite;
    }

    /**
     * @param forIdZoneForfait
     *            the forIdZoneForfait to set
     */
    public void setForIdZoneForfait(String forIdZoneForfait) {
        this.forIdZoneForfait = forIdZoneForfait;
    }

    @Override
    public Class<SimpleLienZoneLocalite> whichModelClass() {
        return SimpleLienZoneLocalite.class;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public String getForDateValable() {
        return forDateValable;
    }

}
