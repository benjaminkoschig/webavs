/**
 *
 */
package ch.globaz.vulpecula.business.models.employeur;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author sel
 * 
 */
public class EmployeurSearchSimpleModel extends JadeSearchSimpleModel {

    private String forIdAffiliation = null;
    private String forIdEmployeur = null;

    /**
     * @return the forIdAffiliation
     */
    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * @return the forIdEmployeur
     */
    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    /**
     * @param forIdAffiliation
     *            the forIdAffiliation to set
     */
    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    /**
     * @param forIdEmployeur
     *            the forIdEmployeur to set
     */
    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EmployeurSimpleModel> whichModelClass() {
        return EmployeurSimpleModel.class;
    }

}
