/**
 * 
 */
package ch.globaz.amal.business.models.parametremodel;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleParametreModelSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeSystemeFormule = null;
    private String forIdFormule = null;
    private String forIdParametreModel = null;

    /**
     * @return the forCodeSystemeFormule
     */
    public String getForCodeSystemeFormule() {
        return forCodeSystemeFormule;
    }

    /**
     * @return the forIdFormule
     */
    public String getForIdFormule() {
        return forIdFormule;
    }

    /**
     * @return the forIdParametreModel
     */
    public String getForIdParametreModel() {
        return forIdParametreModel;
    }

    /**
     * @param forCodeSystemeFormule
     *            the forCodeSystemeFormule to set
     */
    public void setForCodeSystemeFormule(String forCodeSystemeFormule) {
        this.forCodeSystemeFormule = forCodeSystemeFormule;
    }

    /**
     * @param forIdFormule
     *            the forIdFormule to set
     */
    public void setForIdFormule(String forIdFormule) {
        this.forIdFormule = forIdFormule;
    }

    /**
     * @param forIdParametreModel
     *            the forIdParametreModel to set
     */
    public void setForIdParametreModel(String forIdParametreModel) {
        this.forIdParametreModel = forIdParametreModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleParametreModel.class;
    }

}
