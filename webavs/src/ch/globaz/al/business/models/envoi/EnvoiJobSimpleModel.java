/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 *         Classe du modèle de données simple Envoi Job >> table ALCTLJOB
 * 
 */
public class EnvoiJobSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJob = null;
    private String jobDate = null;
    private String jobDescription = null;
    private String jobStatus = null;
    private String jobSubType = null;
    private String jobType = null;
    private String jobUser = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdJob();
    }

    /**
     * @return the idJob
     */
    public String getIdJob() {
        return idJob;
    }

    /**
     * @return the jobDate
     */
    public String getJobDate() {
        return jobDate;
    }

    /**
     * @return the jobDescription
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * @return the jobStatus
     */
    public String getJobStatus() {
        return jobStatus;
    }

    /**
     * @return the jobSubType
     */
    public String getJobSubType() {
        return jobSubType;
    }

    /**
     * @return the jobType
     */
    public String getJobType() {
        return jobType;
    }

    /**
     * @return the jobUser
     */
    public String getJobUser() {
        return jobUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        setIdJob(id);
    }

    /**
     * @param idJob
     *            the idJob to set
     */
    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    /**
     * @param jobDate
     *            the jobDate to set
     */
    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    /**
     * @param jobDescription
     *            the jobDescription to set
     */
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    /**
     * @param jobStatus
     *            the jobStatus to set
     */
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    /**
     * @param jobSubType
     *            the jobSubType to set
     */
    public void setJobSubType(String jobSubType) {
        this.jobSubType = jobSubType;
    }

    /**
     * @param jobType
     *            the jobType to set
     */
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    /**
     * @param jobUser
     *            the jobUser to set
     */
    public void setJobUser(String jobUser) {
        this.jobUser = jobUser;
    }

}
