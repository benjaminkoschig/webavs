package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ZoneLocaliteSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String WITH_DATE_VALABLE_LE = "withDateValable";
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateValable = null;
    private String forIdLocalite = null;
    private String forIdZoneForfait = null;

    private String likeNumPostal = null;

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

    public String getForDateValable() {
        return forDateValable;
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
     * @return the likeNumPostal
     */
    public String getLikeNumPostal() {
        return likeNumPostal;
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

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;

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

    /**
     * @param likeNumPostal
     *            the likeNumPostal to set
     */
    public void setLikeNumPostal(String likeNumPostal) {
        this.likeNumPostal = likeNumPostal;
    }

    @Override
    public Class<ZoneLocalite> whichModelClass() {
        return ZoneLocalite.class;
    }

}
