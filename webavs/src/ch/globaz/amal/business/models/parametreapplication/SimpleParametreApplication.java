/**
 * 
 */
package ch.globaz.amal.business.models.parametreapplication;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleParametreApplication extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csGroupeParametre = null;

    private String csTypeParametre = null;

    private String idParametreApplication = null;

    private String valeurParametre = null;

    public String getCsGroupeParametre() {
        return csGroupeParametre;
    }

    /**
     * @return the csTypeParametre
     */
    public String getCsTypeParametre() {
        return csTypeParametre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idParametreApplication;
    }

    /**
     * @return the idParametreApplication
     */
    public String getIdParametreApplication() {
        return idParametreApplication;
    }

    /**
     * @return the valeurParametre
     */
    public String getValeurParametre() {
        return valeurParametre;
    }

    public void setCsGroupeParametre(String csGroupeParametre) {
        this.csGroupeParametre = csGroupeParametre;
    }

    /**
     * @param csTypeParametre
     *            the csTypeParametre to set
     */
    public void setCsTypeParametre(String csTypeParametre) {
        this.csTypeParametre = csTypeParametre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idParametreApplication = id;
    }

    /**
     * @param idParametreApplication
     *            the idParametreApplication to set
     */
    public void setIdParametreApplication(String idParametreApplication) {
        this.idParametreApplication = idParametreApplication;
    }

    /**
     * @param valeurParametre
     *            the valeurParametre to set
     */
    public void setValeurParametre(String valeurParametre) {
        this.valeurParametre = valeurParametre;
    }

}
