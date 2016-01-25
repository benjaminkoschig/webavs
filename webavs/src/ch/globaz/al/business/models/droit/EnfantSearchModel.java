/**
 * 
 */
package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Permet la recherche d'une enfant.
 * 
 * @author jts
 * 
 */
public class EnfantSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche par l'identifiant de l'enfant
     */
    private String forIdEnfant = null;

    /**
     * @return the forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * @param forIdEnfant
     *            the forIdEnfant to set
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return EnfantModel.class;
    }

}
