/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleControleurJob extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateJob = null;
    private String descriptionJob = null;
    private String idJob = null;
    private String statusEnvoi = null;
    private String subTypeJob = null;
    private String typeJob = null;
    private String userJob = null;

    /**
     * @return the dateJob
     */
    public String getDateJob() {
        return dateJob;
    }

    /**
     * @return the descriptionJob
     */
    public String getDescriptionJob() {
        return descriptionJob;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idJob;
    }

    /**
     * @return the idJob
     */
    public String getIdJob() {
        return idJob;
    }

    /**
     * @return the statusEnvoi
     */
    public String getStatusEnvoi() {
        return statusEnvoi;
    }

    public String getSubTypeJob() {
        return subTypeJob;
    }

    /**
     * @return the typeJob
     */
    public String getTypeJob() {
        return typeJob;
    }

    public String getUserJob() {
        return userJob;
    }

    /**
     * @param dateJob
     *            the dateJob to set
     */
    public void setDateJob(String dateJob) {
        this.dateJob = dateJob;
    }

    /**
     * @param descriptionJob
     *            the descriptionJob to set
     */
    public void setDescriptionJob(String descriptionJob) {
        this.descriptionJob = descriptionJob;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idJob = id;
    }

    /**
     * @param idJob
     *            the idJob to set
     */
    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    /**
     * @param statusEnvoi
     *            the statusEnvoi to set
     */
    public void setStatusEnvoi(String statusEnvoi) {
        this.statusEnvoi = statusEnvoi;
    }

    public void setSubTypeJob(String subTypeJob) {
        this.subTypeJob = subTypeJob;
    }

    /**
     * @param typeJob
     *            the typeJob to set
     */
    public void setTypeJob(String typeJob) {
        this.typeJob = typeJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

}
