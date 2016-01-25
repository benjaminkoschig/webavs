/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DHI
 * 
 */
public class ComplexControleurEnvoi extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String dateJob = null;
    String descriptionJob = null;
    String idJob = null;
    String statusEnvoi = null;
    String statusEnvoiJob = null;

    String subTypeJob = null;

    String typeJob = null;

    String userJob = null;

    /**
	 * 
	 */
    public ComplexControleurEnvoi() {
        super();
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return null;
    }

    /**
     * @return the statusEnvoi
     */
    public String getStatusEnvoi() {
        return statusEnvoi;
    }

    /**
     * @return the statusEnvoiJob
     */
    public String getStatusEnvoiJob() {
        return statusEnvoiJob;
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

    /**
     * @return the userJob
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
    }

    /**
     * @param statusEnvoi
     *            the statusEnvoi to set
     */
    public void setStatusEnvoi(String statusEnvoi) {
        this.statusEnvoi = statusEnvoi;
    }

    /**
     * @param statusEnvoiJob
     *            the statusEnvoiJob to set
     */
    public void setStatusEnvoiJob(String statusEnvoiJob) {
        this.statusEnvoiJob = statusEnvoiJob;
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

    /**
     * @param userJob
     *            the userJob to set
     */
    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

}
