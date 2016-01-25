/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DHI
 * 
 */
public class ComplexControleurAnnonceDetailSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDetailFamille = null;
    private String forIdJob = null;
    private String forStatusEnvoi = null;

    /**
     * @return the forIdDetailFamille
     */
    public String getForIdDetailFamille() {
        return forIdDetailFamille;
    }

    /**
     * @return the forIdJob
     */
    public String getForIdJob() {
        return forIdJob;
    }

    /**
     * @return the forStatusEnvoi
     */
    public String getForStatusEnvoi() {
        return forStatusEnvoi;
    }

    /**
     * @param forIdDetailFamille
     *            the forIdDetailFamille to set
     */
    public void setForIdDetailFamille(String forIdDetailFamille) {
        this.forIdDetailFamille = forIdDetailFamille;
    }

    /**
     * @param forIdJob
     *            the forIdJob to set
     */
    public void setForIdJob(String forIdJob) {
        this.forIdJob = forIdJob;
    }

    /**
     * @param forStatusEnvoi
     *            the forStatusEnvoi to set
     */
    public void setForStatusEnvoi(String forStatusEnvoi) {
        this.forStatusEnvoi = forStatusEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ComplexControleurAnnonceDetail.class;
    }

}
