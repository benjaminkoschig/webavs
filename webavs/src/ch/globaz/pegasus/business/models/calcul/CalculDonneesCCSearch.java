package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class CalculDonneesCCSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forDateDebut = null;

    private String forDateFin = null;

    private String forIdDroit = null;
    private List forIdMembreFamilleSFIn = null;

    public String getForDateDebut() {
        return forDateDebut;
    }

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
     * @return the forIdMembreFamilleSFIn
     */
    public List getForIdMembreFamilleSFIn() {
        return forIdMembreFamilleSFIn;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdMembreFamilleSFIn
     *            the forIdMembreFamilleSFIn to set
     */
    public void setForIdMembreFamilleSFIn(List forIdMembreFamilleSFIn) {
        this.forIdMembreFamilleSFIn = forIdMembreFamilleSFIn;
    }

    @Override
    public Class whichModelClass() {
        return CalculDonneesCC.class;
    }

}
