package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Search model pour la recherche de données pour le calcul des PC accordées d'un droit.
 * 
 * @author ECO
 * 
 */
public class CalculDonneesDroitSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutChambre = "01.01.1970";// TODO a voir avec hone-chambres
    private String forDateDebutDF = "01.1970";
    private String forDateFin = null;
    private String forIdDroit = null;

    /**
     * @return the forDateDebutChambre
     */
    public String getForDateDebutChambre() {
        return forDateDebutChambre;
    }

    /**
     * @return the forDateDebutDF
     */
    public String getForDateDebutDF() {
        return forDateDebutDF;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {

        forDateDebutDF = JadeDateUtil.convertDateMonthYear(forDateDebut);
        forDateDebutChambre = forDateDebut;// TODO voir en haut declaration variables
    }

    /**
     * @param forDateDebutChambre
     *            the forDateDebutChambre to set
     */
    public void setForDateDebutChambre(String forDateDebutChambre) {
        this.forDateDebutChambre = forDateDebutChambre;
    }

    /**
     * @param forDateDebutDF
     *            the forDateDebutDF to set
     */
    public void setForDateDebutDF(String forDateDebutDF) {
        this.forDateDebutDF = JadeDateUtil.convertDateMonthYear(forDateDebutDF);
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = JadeDateUtil.convertDateMonthYear(forDateFin);
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    @Override
    public Class whichModelClass() {
        return CalculDonneesDroit.class;
    }

}
