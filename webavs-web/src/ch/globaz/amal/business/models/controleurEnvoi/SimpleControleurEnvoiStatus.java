/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleControleurEnvoiStatus extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAnnonce = null;
    private String idEnvoi = null;
    private String idJob = null;
    private String idStatus = null;
    private String jobError = null;
    private String noGroupe = null;
    private String statusEnvoi = null;
    private String typeEnvoi = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idStatus;
    }

    /**
     * @return the idAnnonce
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * @return the idEnvoi
     */
    public String getIdEnvoi() {
        return idEnvoi;
    }

    /**
     * @return the idJob
     */
    public String getIdJob() {
        return idJob;
    }

    /**
     * @return the idStatus
     */
    public String getIdStatus() {
        return idStatus;
    }

    /**
     * @return the jobError
     */
    public String getJobError() {
        return jobError;
    }

    /**
     * @return the noGroupe
     */
    public String getNoGroupe() {
        return noGroupe;
    }

    /**
     * @return the statusEnvoi
     */
    public String getStatusEnvoi() {
        return statusEnvoi;
    }

    /**
     * @return the typeEnvoi
     */
    public String getTypeEnvoi() {
        return typeEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idStatus = id;
    }

    /**
     * @param idAnnonce
     *            the idAnnonce to set
     */
    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    /**
     * @param idEnvoi
     *            the idEnvoi to set
     */
    public void setIdEnvoi(String idEnvoi) {
        this.idEnvoi = idEnvoi;
    }

    /**
     * @param idJob
     *            the idJob to set
     */
    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    /**
     * @param idStatus
     *            the idStatus to set
     */
    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    /**
     * @param jobError
     *            the jobError to set
     */
    public void setJobError(String jobError) {
        this.jobError = jobError;
    }

    /**
     * @param noGroupe
     *            the noGroupe to set
     */
    public void setNoGroupe(String noGroupe) {
        this.noGroupe = noGroupe;
    }

    /**
     * @param statusEnvoi
     *            the statusEnvoi to set
     */
    public void setStatusEnvoi(String statusEnvoi) {
        this.statusEnvoi = statusEnvoi;
    }

    /**
     * @param typeEnvoi
     *            the typeEnvoi to set
     */
    public void setTypeEnvoi(String typeEnvoi) {
        this.typeEnvoi = typeEnvoi;
    }

}
