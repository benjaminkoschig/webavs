package ch.globaz.al.business.models.allocataire;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche permettant la recherche de données agriculteur
 * 
 * @author PTA
 */
public class AgricoleSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant pour l'allocataire
     */
    private String forIdAllocataire = null;

    /**
     * @return the forIdAllocataire
     */
    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    /**
     * 
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
    public Class<AgricoleModel> whichModelClass() {
        return AgricoleModel.class;
    }

}
