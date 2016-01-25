/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 *         Modèle de recher d'un job envoi
 * 
 */
public class EnvoiJobSimpleModelSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String forIdJob = null;
    public String forJobDate = null;
    public String forJobDateMax = null;
    public String forJobDateMin = null;
    public String forJobStatus = null;
    public String forJobSubType = null;
    public String forJobType = null;
    public String forJobUser = null;
    public String forNotJobStatusEnvoi = null;

    /**
     * @return the forIdJob
     */
    public String getForIdJob() {
        return forIdJob;
    }

    /**
     * @return the forJobDate
     */
    public String getForJobDate() {
        return forJobDate;
    }

    /**
     * @return the forJobDateMax
     */
    public String getForJobDateMax() {
        return forJobDateMax;
    }

    /**
     * @return the forJobDateMin
     */
    public String getForJobDateMin() {
        return forJobDateMin;
    }

    /**
     * @return the forJobStatus
     */
    public String getForJobStatus() {
        return forJobStatus;
    }

    /**
     * @return the forJobSubType
     */
    public String getForJobSubType() {
        return forJobSubType;
    }

    /**
     * @return the forJobType
     */
    public String getForJobType() {
        return forJobType;
    }

    /**
     * @return the forJobUser
     */
    public String getForJobUser() {
        return forJobUser;
    }

    /**
     * @return the forNotJobStatusEnvoi
     */
    public String getForNotJobStatusEnvoi() {
        return forNotJobStatusEnvoi;
    }

    /**
     * @param forIdJob
     *            the forIdJob to set
     */
    public void setForIdJob(String forIdJob) {
        this.forIdJob = forIdJob;
    }

    /**
     * @param forJobDate
     *            the forJobDate to set
     */
    public void setForJobDate(String forJobDate) {
        this.forJobDate = forJobDate;
    }

    /**
     * @param forJobDateMax
     *            the forJobDateMax to set
     */
    public void setForJobDateMax(String forJobDateMax) {
        this.forJobDateMax = forJobDateMax;
    }

    /**
     * @param forJobDateMin
     *            the forJobDateMin to set
     */
    public void setForJobDateMin(String forJobDateMin) {
        this.forJobDateMin = forJobDateMin;
    }

    /**
     * @param forJobStatus
     *            the forJobStatus to set
     */
    public void setForJobStatus(String forJobStatus) {
        this.forJobStatus = forJobStatus;
    }

    /**
     * @param forJobSubType
     *            the forJobSubType to set
     */
    public void setForJobSubType(String forJobSubType) {
        this.forJobSubType = forJobSubType;
    }

    /**
     * @param forJobType
     *            the forJobType to set
     */
    public void setForJobType(String forJobType) {
        this.forJobType = forJobType;
    }

    /**
     * @param forJobUser
     *            the forJobUser to set
     */
    public void setForJobUser(String forJobUser) {
        this.forJobUser = forJobUser;
    }

    /**
     * @param forNotJobStatusEnvoi
     *            the forNotJobStatusEnvoi to set
     */
    public void setForNotJobStatusEnvoi(String forNotJobStatusEnvoi) {
        this.forNotJobStatusEnvoi = forNotJobStatusEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EnvoiJobSimpleModel> whichModelClass() {
        return EnvoiJobSimpleModel.class;
    }

}
