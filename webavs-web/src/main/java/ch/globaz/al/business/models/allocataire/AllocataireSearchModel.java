/**
 * 
 */
package ch.globaz.al.business.models.allocataire;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Permet des recherches sur les allocataires
 * 
 * @author jts
 * 
 */
public class AllocataireSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Identifiant de l'allocataire
     */

    private String forIdAllocataire = null;

    /**
     * @return the forIdAllocataire
     */
    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    /**
     * @param forIdAllocataire
     *            the forIdAllocataire to set
     */
    public void setForIdAllocataire(String forIdAllocataire) {
        this.forIdAllocataire = forIdAllocataire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AllocataireModel> whichModelClass() {
        return AllocataireModel.class;
    }

}
