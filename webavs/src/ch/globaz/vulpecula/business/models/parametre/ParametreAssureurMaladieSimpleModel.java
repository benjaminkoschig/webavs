/**
 *
 */
package ch.globaz.vulpecula.business.models.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author sel
 * 
 */
public class ParametreAssureurMaladieSimpleModel extends JadeSimpleModel {

    private String designation = null;
    private String designationSearch = null;

    private String idParametreAssureurMaladie = null;

    private String mode = null;

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    public String getDesignationSearch() {
        return designationSearch;
    }

    @Override
    public String getId() {
        return getIdParametreAssureurMaladie();
    }

    /**
     * @return the idParametreAssureurMaladie
     */
    public String getIdParametreAssureurMaladie() {
        return idParametreAssureurMaladie;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param designation
     *            the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
        designationSearch = JadeStringUtil.convertSpecialChars(designation.toUpperCase());
    }

    public void setDesignationSearch(String designationSearch) {
        this.designationSearch = designationSearch;
    }

    @Override
    public void setId(String id) {
        setIdParametreAssureurMaladie(id);
    }

    /**
     * @param idParametreAssureurMaladie
     *            the idParametreAssureurMaladie to set
     */
    public void setIdParametreAssureurMaladie(String idParametreAssureurMaladie) {
        this.idParametreAssureurMaladie = idParametreAssureurMaladie;
    }

    /**
     * @param mode
     *            the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

}
