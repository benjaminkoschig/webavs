/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

/**
 * @author dhi
 * 
 */
public class RevenuHistoriqueSearch extends RevenuSearch {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnneeHistorique = null;

    private String forIdRevenuDeterminant = null;

    private String forIdRevenuHistorique = null;

    private Boolean forIsRecalcul = null;

    private Boolean forRevenuActif = null;

    /**
	 * 
	 */
    public RevenuHistoriqueSearch() {
    }

    /**
     * @return the forAnneeHistorique
     */
    public String getForAnneeHistorique() {
        return forAnneeHistorique;
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

    public Boolean getForIsRecalcul() {
        return forIsRecalcul;
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

    public void setForIsRecalcul(Boolean forIsRecalcul) {
        this.forIsRecalcul = forIsRecalcul;
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
        return RevenuHistorique.class;
    }

}
