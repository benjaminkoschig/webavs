package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Mod�le de recherche des saisies ADI
 * 
 * @author GMO
 * 
 */
public class AdiSaisieSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crit�re id d�compte
     */
    private String forIdDecompteAdi = null;

    /**
     * crit�re enfant
     */
    private String forIdEnfant = null;

    /**
     * Crit�re p�riodeMax
     */
    private String forPeriodeMax = null;

    /**
     * Crit�re p�riodeMin
     */
    private String forPeriodeMin = null;

    /**
     * 
     * @return forIdDecompte
     */
    public String getForIdDecompteAdi() {
        return forIdDecompteAdi;
    }

    /**
     * @return forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * @return forPeriodeMax
     */
    public String getForPeriodeMax() {
        return forPeriodeMax;
    }

    /**
     * @return forPeriodeMin
     */
    public String getForPeriodeMin() {
        return forPeriodeMin;
    }

    /**
     * @param forIdDecompte
     *            crit�re id d�compte
     */
    public void setForIdDecompteAdi(String forIdDecompte) {
        forIdDecompteAdi = forIdDecompte;
    }

    /**
     * @param forIdEnfant
     *            crit�re enfant
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * @param forPeriodeMax
     *            crit�re max
     */
    public void setForPeriodeMax(String forPeriodeMax) {
        this.forPeriodeMax = forPeriodeMax;
    }

    /**
     * @param forPeriodeMin
     *            crit�re p�riode min
     */
    public void setForPeriodeMin(String forPeriodeMin) {
        this.forPeriodeMin = forPeriodeMin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return AdiSaisieModel.class;
    }

}
