package ch.globaz.al.business.models.periodeAF;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche de période AF
 * 
 * @author GMO
 * 
 */
public class PeriodeAFSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère de la date effective de la période
     */
    private String forDatePeriode = null;
    /**
     * Critère de l'état de la période
     */
    private String forEtat = null;

    /**
     * Critère de l'id période
     */
    private String forIdPeriode = null;

    /**
     * @return forDatePeriode
     */
    public String getForDatePeriode() {
        return forDatePeriode;
    }

    /**
     * @return forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return forIdPeriode
     */
    public String getForIdPeriode() {
        return forIdPeriode;
    }

    /**
     * @param forDatePeriode
     *            critère date période
     */
    public void setForDatePeriode(String forDatePeriode) {
        this.forDatePeriode = forDatePeriode;
    }

    /**
     * @param forEtat
     *            critère état de la période
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @param forIdPeriode
     *            critère id de la périodes
     */
    public void setForIdPeriode(String forIdPeriode) {
        this.forIdPeriode = forIdPeriode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return PeriodeAFModel.class;
    }

}
