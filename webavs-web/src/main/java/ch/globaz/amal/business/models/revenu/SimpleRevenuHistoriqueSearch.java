/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleRevenuHistoriqueSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeHistorique = null;
    private String forIdContribuable = null;
    private String forIdRevenu = null;
    private String forIdRevenuHistorique = null;
    private Boolean forIsRecalcul = null;
    private Boolean forRevenuActif = null;

    /**
	 * 
	 */
    public SimpleRevenuHistoriqueSearch() {
    }

    /**
     * @return the forAnneeHistorique
     */
    public String getForAnneeHistorique() {
        return forAnneeHistorique;
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @return the forIdRevenu
     */
    public String getForIdRevenu() {
        return forIdRevenu;
    }

    /**
     * @return the forIdRevenuHistorique
     */
    public String getForIdRevenuHistorique() {
        return forIdRevenuHistorique;
    }

    public Boolean getForIsRecalcul() {
        return forIsRecalcul;
    }

    /**
     * @return the forRevenuActif
     */
    public Boolean getForRevenuActif() {
        return forRevenuActif;
    }

    /**
     * @param forAnneeHistorique
     *            the forAnneeHistorique to set
     */
    public void setForAnneeHistorique(String forAnneeHistorique) {
        this.forAnneeHistorique = forAnneeHistorique;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @param forIdRevenu
     *            the forIdRevenu to set
     */
    public void setForIdRevenu(String forIdRevenu) {
        this.forIdRevenu = forIdRevenu;
    }

    /**
     * @param forIdRevenuHistorique
     *            the forIdRevenuHistorique to set
     */
    public void setForIdRevenuHistorique(String forIdRevenuHistorique) {
        this.forIdRevenuHistorique = forIdRevenuHistorique;
    }

    public void setForIsRecalcul(Boolean forIsRecalcul) {
        this.forIsRecalcul = forIsRecalcul;
    }

    /**
     * @param forRevenuActif
     *            the forRevenuActif to set
     */
    public void setForRevenuActif(Boolean forRevenuActif) {
        this.forRevenuActif = forRevenuActif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleRevenuHistorique.class;
    }

}
