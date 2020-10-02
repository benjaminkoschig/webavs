package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleHome extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idHome = null;
    private String idTiersHome = null;
    private Boolean isHorsCanton = false;
    private String nomBatiment = null;
    private String numeroIdentification = null;

    @Override
    public String getId() {
        return idHome;
    }

    /**
     * @return the idHome
     */
    public String getIdHome() {
        return idHome;
    }

    /**
     * @return the idTiersHome
     */
    public String getIdTiersHome() {
        return idTiersHome;
    }

    public Boolean getIsHorsCanton() {
        return isHorsCanton;
    }

    /**
     * @return the nomBatiment
     */
    public String getNomBatiment() {
        return nomBatiment;
    }

    /**
     * @return the numeroIdentification
     */
    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    @Override
    public void setId(String id) {
        idHome = id;
    }

    /**
     * @param idHome
     *            the idHome to set
     */
    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    /**
     * @param idTiersHome
     *            the idTiersHome to set
     */
    public void setIdTiersHome(String idTiersHome) {
        this.idTiersHome = idTiersHome;
    }

    public void setIsHorsCanton(Boolean isHorsCanton) {
        this.isHorsCanton = isHorsCanton;
    }

    /**
     * @param nomBatiment
     *            the nomBatiment to set
     */
    public void setNomBatiment(String nomBatiment) {
        this.nomBatiment = nomBatiment;
    }

    /**
     * @param numeroIdentification
     *            the numeroIdentification to set
     */
    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

}
