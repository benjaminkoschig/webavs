package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimplePrixChambreSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutBefore = null;
    private String forDateDebutCheckPeriode = null;
    private String forDateFinCheckPeriode = null;
    private String forIdPrixChambre = null;
    private String forIdTypeChambre = null;

    public String getForDateDebutBefore() {
        return forDateDebutBefore;
    }

    /**
     * @return the forDateDebutCheckPeriode
     */
    public String getForDateDebutCheckPeriode() {
        return forDateDebutCheckPeriode;
    }

    /**
     * @return the forDateFinCheckPeriode
     */
    public String getForDateFinCheckPeriode() {
        return forDateFinCheckPeriode;
    }

    /**
     * @return the forIdPrixChambre
     */
    public String getForIdPrixChambre() {
        return forIdPrixChambre;
    }

    /**
     * @return the forIdTypeChambre
     */
    public String getForIdTypeChambre() {
        return forIdTypeChambre;
    }

    public void setForDateDebutBefore(String forDateDebutBefore) {
        this.forDateDebutBefore = forDateDebutBefore;
    }

    /**
     * @param forDateDebutCheckPeriode
     *            the forDateDebutCheckPeriode to set
     */
    public void setForDateDebutCheckPeriode(String forDateDebutCheckPeriode) {
        this.forDateDebutCheckPeriode = forDateDebutCheckPeriode;
    }

    /**
     * @param forDateFinCheckPeriode
     *            the forDateFinCheckPeriode to set
     */
    public void setForDateFinCheckPeriode(String forDateFinCheckPeriode) {
        this.forDateFinCheckPeriode = forDateFinCheckPeriode;
    }

    /**
     * @param forIdPrixChambre
     *            the forIdPrixChambre to set
     */
    public void setForIdPrixChambre(String forIdPrixChambre) {
        this.forIdPrixChambre = forIdPrixChambre;
    }

    /**
     * @param forIdTypeChambre
     *            the forIdTypeChambre to set
     */
    public void setForIdTypeChambre(String forIdTypeChambre) {
        this.forIdTypeChambre = forIdTypeChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimplePrixChambre.class;
    }

}
