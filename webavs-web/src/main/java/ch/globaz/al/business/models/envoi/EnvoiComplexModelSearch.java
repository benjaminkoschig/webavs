/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author dhi
 * 
 */
public class EnvoiComplexModelSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forEnvoiStatus = null;

    private String forIdDossier = null;

    private String forIdEnvoi = null;

    private String forIdJob = null;

    private String forJobDate = null;

    private String forJobDateMax = null;

    private String forJobDateMin = null;

    private String forJobStatus = null;

    private String forJobUser = null;

    private String forNotJobStatusEnvoi = null;

    private String forNotStatusEnvoi = null;

    /**
     * @return the forEnvoiStatus
     */
    public String getForEnvoiStatus() {
        return forEnvoiStatus;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdEnvoi
     */
    public String getForIdEnvoi() {
        return forIdEnvoi;
    }

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
     * @return the forNotStatusEnvoi
     */
    public String getForNotStatusEnvoi() {
        return forNotStatusEnvoi;
    }

    /**
     * @param forEnvoiStatus
     *            the forEnvoiStatus to set
     */
    public void setForEnvoiStatus(String forEnvoiStatus) {
        this.forEnvoiStatus = forEnvoiStatus;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdEnvoi
     *            the forIdEnvoi to set
     */
    public void setForIdEnvoi(String forIdEnvoi) {
        this.forIdEnvoi = forIdEnvoi;
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

    /**
     * @param forNotStatusEnvoi
     *            the forNotStatusEnvoi to set
     */
    public void setForNotStatusEnvoi(String forNotStatusEnvoi) {
        this.forNotStatusEnvoi = forNotStatusEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EnvoiComplexModel> whichModelClass() {
        return EnvoiComplexModel.class;
    }

}
