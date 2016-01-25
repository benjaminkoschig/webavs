/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author dhi
 * 
 */
public class RevenuHistoriqueComplexSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnneeHistorique = null;

    private String forIdContribuable = null;

    private String forIdRevenu = null;

    private String forIdRevenuDeterminant = null;

    private String forIdRevenuHistorique = null;

    private Boolean forRevenuActif = null;

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
     * @return the forIdRevenuDeterminant
     */
    public String getForIdRevenuDeterminant() {
        return forIdRevenuDeterminant;
    }

    /**
     * @return the forIdRevenuHistorique
     */
    public String getForIdRevenuHistorique() {
        return forIdRevenuHistorique;
    }

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
     * @param forIdRevenuDeterminant
     *            the forIdRevenuDeterminant to set
     */
    public void setForIdRevenuDeterminant(String forIdRevenuDeterminant) {
        this.forIdRevenuDeterminant = forIdRevenuDeterminant;
    }

    /**
     * @param forIdRevenuHistorique
     *            the forIdRevenuHistorique to set
     */
    public void setForIdRevenuHistorique(String forIdRevenuHistorique) {
        this.forIdRevenuHistorique = forIdRevenuHistorique;
    }

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
        return RevenuHistoriqueComplex.class;
    }

}
