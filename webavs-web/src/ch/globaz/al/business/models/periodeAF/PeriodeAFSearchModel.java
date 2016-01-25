package ch.globaz.al.business.models.periodeAF;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Mod�le de recherche de p�riode AF
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
     * Crit�re de la date effective de la p�riode
     */
    private String forDatePeriode = null;
    /**
     * Crit�re de l'�tat de la p�riode
     */
    private String forEtat = null;

    /**
     * Crit�re de l'id p�riode
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
     *            crit�re date p�riode
     */
    public void setForDatePeriode(String forDatePeriode) {
        this.forDatePeriode = forDatePeriode;
    }

    /**
     * @param forEtat
     *            crit�re �tat de la p�riode
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @param forIdPeriode
     *            crit�re id de la p�riodes
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
