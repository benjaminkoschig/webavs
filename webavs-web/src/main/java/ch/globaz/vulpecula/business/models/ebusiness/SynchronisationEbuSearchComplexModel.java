/**
 * 
 */
package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author sel
 * 
 */
public class SynchronisationEbuSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 2216183971460403327L;

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    private boolean forDateSynchronisationIsEmpty;
    private String forIdEmployeur;
    private String yearsMonthFrom;
    private String yearsMonthTo;
    private String status;

    @Override
    public Class<SynchronisationEbuComplexModel> whichModelClass() {
        return SynchronisationEbuComplexModel.class;
    }

    /**
     * @return the forDateSynchronisationIsEmpty
     */
    public boolean isForDateSynchronisationIsEmpty() {
        return forDateSynchronisationIsEmpty;
    }

    /**
     * @param forDateSynchronisationIsEmpty the forDateSynchronisationIsEmpty to set
     */
    public void setForDateSynchronisationIsEmpty(boolean forDateSynchronisationIsEmpty) {
        this.forDateSynchronisationIsEmpty = forDateSynchronisationIsEmpty;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getYearsMonthFrom() {
        return yearsMonthFrom;
    }

    public void setYearsMonthFrom(String yearsMonthFrom) {
        this.yearsMonthFrom = yearsMonthFrom;
    }

    public String getYearsMonthTo() {
        return yearsMonthTo;
    }

    public void setYearsMonthTo(String yearsMonthTo) {
        this.yearsMonthTo = yearsMonthTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
