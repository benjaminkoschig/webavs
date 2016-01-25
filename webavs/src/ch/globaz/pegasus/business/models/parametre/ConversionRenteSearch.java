package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ConversionRenteSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String WITH_DATE_VALABLE = "withDateValable";
    private String forAge = null;
    private String forAnnee = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forIdConversionRente = null;

    /**
     * @return the forAge
     */
    public String getForAge() {
        return forAge;
    }

    /**
     * @return the forAnne
     */
    public String getForAnnee() {
        return forAnnee;
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
     * @return the forIdConversionRente
     */
    public String getForIdConversionRente() {
        return forIdConversionRente;
    }

    /**
     * @param forAge
     *            the forAge to set
     */
    public void setForAge(String forAge) {
        this.forAge = forAge;
    }

    /**
     * @param forAnne
     *            the forAnne to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
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

    /**
     * @param forIdConversionRente
     *            the forIdConversionRente to set
     */
    public void setForIdConversionRente(String forIdConversionRente) {
        this.forIdConversionRente = forIdConversionRente;
    }

    @Override
    public Class<ConversionRente> whichModelClass() {
        return ConversionRente.class;
    }

}
