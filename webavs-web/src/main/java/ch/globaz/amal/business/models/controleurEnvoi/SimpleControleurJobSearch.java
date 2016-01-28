/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleControleurJobSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateJob = null;
    private String forDateMax = null;
    private String forDateMin = null;
    private String forIdJob = null;
    private String forNotStatusEnvoi = null;
    private String forStatusEnvoi = null;
    private String forSubTypeJob = null;
    private String forTypeJob = null;
    private String forUserJob = null;

    /**
     * @return the forDateJob
     */
    public String getForDateJob() {
        return forDateJob;
    }

    /**
     * @return the forDateMax
     */
    public String getForDateMax() {
        return forDateMax;
    }

    /**
     * @return the forDateMin
     */
    public String getForDateMin() {
        return forDateMin;
    }

    /**
     * @return the forIdJob
     */
    public String getForIdJob() {
        return forIdJob;
    }

    /**
     * @return the forNotStatusEnvoi
     */
    public String getForNotStatusEnvoi() {
        return forNotStatusEnvoi;
    }

    /**
     * @return the forStatusEnvoi
     */
    public String getForStatusEnvoi() {
        return forStatusEnvoi;
    }

    public String getForSubTypeJob() {
        return forSubTypeJob;
    }

    /**
     * @return the forTypeJob
     */
    public String getForTypeJob() {
        return forTypeJob;
    }

    /**
     * @return the forUserJob
     */
    public String getForUserJob() {
        return forUserJob;
    }

    /**
     * @param forDateJob
     *            the forDateJob to set
     */
    public void setForDateJob(String forDateJob) {
        this.forDateJob = forDateJob;
    }

    /**
     * @param forDateMax
     *            the forDateMax to set
     */
    public void setForDateMax(String forDateMax) {
        this.forDateMax = forDateMax;
    }

    /**
     * @param forDateMin
     *            the forDateMin to set
     */
    public void setForDateMin(String forDateMin) {
        this.forDateMin = forDateMin;
    }

    /**
     * @param forIdJob
     *            the forIdJob to set
     */
    public void setForIdJob(String forIdJob) {
        this.forIdJob = forIdJob;
    }

    /**
     * @param forNotStatusEnvoi
     *            the forNotStatusEnvoi to set
     */
    public void setForNotStatusEnvoi(String forNotStatusEnvoi) {
        this.forNotStatusEnvoi = forNotStatusEnvoi;
    }

    /**
     * @param forStatusEnvoi
     *            the forStatusEnvoi to set
     */
    public void setForStatusEnvoi(String forStatusEnvoi) {
        this.forStatusEnvoi = forStatusEnvoi;
    }

    public void setForSubTypeJob(String forSubTypeJob) {
        this.forSubTypeJob = forSubTypeJob;
    }

    /**
     * @param forTypeJob
     *            the forTypeJob to set
     */
    public void setForTypeJob(String forTypeJob) {
        this.forTypeJob = forTypeJob;
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
        return SimpleControleurJob.class;
    }

}
