package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimplePeriodeServiceEtatSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutBefore = null;
    private String forDateDebutCheckPeriode = null;
    private String forDateFinCheckPeriode = null;
    private String forIdHome = null;
    private String forIdPeriodeServiceEtat = null;

    public final static String CHECL_FOR_ANTERIEURS_PERIODES = "checkForAnterieurPeriods";

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
     * @return the forIdHome
     */
    public String getForIdHome() {
        return forIdHome;
    }

    public String getForIdPeriodeServiceEtat() {
        return forIdPeriodeServiceEtat;
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
     * @param forIdHome
     *            the forIdHome to set
     */
    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    public void setForIdPeriodeServiceEtat(String forIdPeriodeServiceEtat) {
        this.forIdPeriodeServiceEtat = forIdPeriodeServiceEtat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimplePeriodeServiceEtat> whichModelClass() {
        return SimplePeriodeServiceEtat.class;
    }

}
