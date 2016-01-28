/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 *         Modèle de recherche d'un paramètre envoi
 * 
 */
public class EnvoiParametresSimpleModelSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCsTypeParametre = null;

    private String forIdParametreEnvoi = null;

    /**
     * @return the forCsTypeParametre
     */
    public String getForCsTypeParametre() {
        return forCsTypeParametre;
    }

    /**
     * @return the forIdParametreEnvoi
     */
    public String getForIdParametreEnvoi() {
        return forIdParametreEnvoi;
    }

    /**
     * @param forCsTypeParametre
     *            the forCsTypeParametre to set
     */
    public void setForCsTypeParametre(String forCsTypeParametre) {
        this.forCsTypeParametre = forCsTypeParametre;
    }

    /**
     * @param forIdParametreEnvoi
     *            the forIdParametreEnvoi to set
     */
    public void setForIdParametreEnvoi(String forIdParametreEnvoi) {
        this.forIdParametreEnvoi = forIdParametreEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return EnvoiParametresSimpleModel.class;
    }

}
