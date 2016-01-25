/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DHI
 * 
 */
public class ComplexControleurEnvoiDetail extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String anneeHistorique = null;
    String dateEnvoi = null;
    String dateJob = null;
    String descriptionJob = null;
    String idContribuable = null;
    String idDetailFamille = null;
    String idFamille = null;
    String idJob = null;
    String idStatus = null;
    Boolean isContribuable = null;
    String jobError = null;
    String libelleEnvoi = null;
    String noGroupe = null;
    String nomPrenom = null;
    String numModele = null;
    String statusEnvoi = null;
    String subTypeJob = null;
    String typeJob = null;
    String userJob = null;

    /**
	 * 
	 */
    public ComplexControleurEnvoiDetail() {
        super();
    }

    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the dateEnvoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
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
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idDetailFamille
     */
    public String getIdDetailFamille() {
        return idDetailFamille;
    }

    /**
     * @return the idFamille
     */
    public String getIdFamille() {
        return idFamille;
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
     * @return the isContribuable
     */
    public Boolean getIsContribuable() {
        return isContribuable;
    }

    public String getJobError() {
        return jobError;
    }

    /**
     * @return the libelleEnvoi
     */
    public String getLibelleEnvoi() {
        return libelleEnvoi;
    }

    /**
     * @return the noGroupe
     */
    public String getNoGroupe() {
        return noGroupe;
    }

    /**
     * @return the nomPrenom
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return the numModele
     */
    public String getNumModele() {
        return numModele;
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

    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param dateEnvoi
     *            the dateEnvoi to set
     */
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
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
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idDetailFamille
     *            the idDetailFamille to set
     */
    public void setIdDetailFamille(String idDetailFamille) {
        this.idDetailFamille = idDetailFamille;
    }

    /**
     * @param idFamille
     *            the idFamille to set
     */
    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
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
     * @param isContribuable
     *            the isContribuable to set
     */
    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    public void setJobError(String jobError) {
        this.jobError = jobError;
    }

    /**
     * @param libelleEnvoi
     *            the libelleEnvoi to set
     */
    public void setLibelleEnvoi(String libelleEnvoi) {
        this.libelleEnvoi = libelleEnvoi;
    }

    /**
     * @param noGroupe
     *            the noGroupe to set
     */
    public void setNoGroupe(String noGroupe) {
        this.noGroupe = noGroupe;
    }

    /**
     * @param nomPrenom
     *            the nomPrenom to set
     */
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * @param numModele
     *            the numModele to set
     */
    public void setNumModele(String numModele) {
        this.numModele = numModele;
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
