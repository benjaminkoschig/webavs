/**
 * 
 */
package ch.globaz.amal.business.models.parametreapplication;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleParametreApplicationSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsGroupeParametre = null;
    private String forCsTypeParametre = null;
    private String forIdParametreApplication = null;
    private String inCsGroupeParametre = null;

    public String getForCsGroupeParametre() {
        return forCsGroupeParametre;
    }

    /**
     * @return the forCsTypeParametre
     */
    public String getForCsTypeParametre() {
        return forCsTypeParametre;
    }

    /**
     * @return the forIdParametreApplication
     */
    public String getForIdParametreApplication() {
        return forIdParametreApplication;
    }

    public String getInCsGroupeParametre() {
        return inCsGroupeParametre;
    }

    public void setForCsGroupeParametre(String forCsGroupeParametre) {
        this.forCsGroupeParametre = forCsGroupeParametre;
    }

    /**
     * @param forCsTypeParametre
     *            the forCsTypeParametre to set
     */
    public void setForCsTypeParametre(String forCsTypeParametre) {
        this.forCsTypeParametre = forCsTypeParametre;
    }

    /**
     * @param forIdParametreApplication
     *            the forIdParametreApplication to set
     */
    public void setForIdParametreApplication(String forIdParametreApplication) {
        this.forIdParametreApplication = forIdParametreApplication;
    }

    public void setInCsGroupeParametre(String inCsGroupeParametre) {
        this.inCsGroupeParametre = inCsGroupeParametre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleParametreApplication.class;
    }

}
