package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleZoneForfaitsSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsCanton = null;
    private String forDesignation = null;
    private String forIdZoneForfait = null;
    private String forType = null;

    /**
     * @return the forCsCanton
     */
    public String getForCsCanton() {
        return forCsCanton;
    }

    /**
     * @return the forDesignation
     */
    public String getForDesignation() {
        return forDesignation;
    }

    /**
     * @return the forIdZoneForfait
     */
    public String getForIdZoneForfait() {
        return forIdZoneForfait;
    }

    /**
     * @param forCsCanton
     *            the forCsCanton to set
     */
    public void setForCsCanton(String forCsCanton) {
        this.forCsCanton = forCsCanton;
    }

    /**
     * @param forDesignation
     *            the forDesignation to set
     */
    public void setForDesignation(String forDesignation) {
        this.forDesignation = forDesignation;
    }

    /**
     * @param forIdZoneForfait
     *            the forIdZoneForfait to set
     */
    public void setForIdZoneForfait(String forIdZoneForfait) {
        this.forIdZoneForfait = forIdZoneForfait;
    }

    public String getForType() {
        return forType;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    @Override
    public Class<SimpleZoneForfaits> whichModelClass() {
        return SimpleZoneForfaits.class;
    }

}
