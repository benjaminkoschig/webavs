package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche des saisies ADI
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
     * Critère id décompte
     */
    private String forIdDecompteAdi = null;

    /**
     * critère enfant
     */
    private String forIdEnfant = null;

    /**
     * Critère périodeMax
     */
    private String forPeriodeMax = null;

    /**
     * Critère périodeMin
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
     *            critère id décompte
     */
    public void setForIdDecompteAdi(String forIdDecompte) {
        forIdDecompteAdi = forIdDecompte;
    }

    /**
     * @param forIdEnfant
     *            critère enfant
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * @param forPeriodeMax
     *            critère max
     */
    public void setForPeriodeMax(String forPeriodeMax) {
        this.forPeriodeMax = forPeriodeMax;
    }

    /**
     * @param forPeriodeMin
     *            critère période min
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
