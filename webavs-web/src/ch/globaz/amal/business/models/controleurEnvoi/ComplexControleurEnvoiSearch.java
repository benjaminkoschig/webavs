/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DHI
 * 
 */
public class ComplexControleurEnvoiSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forDateJobMax = null;

    private String forDateJobMin = null;

    private String forIdJob = null;

    private String forStatusEnvoi = null;

    private String forStatusEnvoiJob = null;

    private String forTypeJob = null;

    private String forUserJob = null;

    /**
     * @return the forDateJobMax
     */
    public String getForDateJobMax() {
        return forDateJobMax;
    }

    /**
     * @return the forDateJobMin
     */
    public String getForDateJobMin() {
        return forDateJobMin;
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
     * @return the forStatusEnvoiJob
     */
    public String getForStatusEnvoiJob() {
        return forStatusEnvoiJob;
    }

    /**
     * @return the forUserJob
     */
    public String getForUserJob() {
        return forUserJob;
    }

    /**
     * @param forDateJobMax
     *            the forDateJobMax to set
     */
    public void setForDateJobMax(String forDateJobMax) {
        this.forDateJobMax = forDateJobMax;
    }

    /**
     * @param forDateJobMin
     *            the forDateJobMin to set
     */
    public void setForDateJobMin(String forDateJobMin) {
        this.forDateJobMin = forDateJobMin;
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

    /**
     * @param forStatusEnvoiJob
     *            the forStatusEnvoiJob to set
     */
    public void setForStatusEnvoiJob(String forStatusEnvoiJob) {
        this.forStatusEnvoiJob = forStatusEnvoiJob;
    }

    /**
     * @param forUserJob
     *            the forUserJob to set
     */
    public void setForUserJob(String forUserJob) {
        this.forUserJob = forUserJob;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        // TODO Auto-generated method stub
        return ComplexControleurEnvoi.class;
    }

    /**
     * @param forTypeJob the forTypeJob to set
     */
    public void setForTypeJob(String forTypeJob) {
        this.forTypeJob = forTypeJob;
    }

    /**
     * @return the forTypeJob
     */
    public String getForTypeJob() {
        return forTypeJob;
    }

}
