package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDonneeHistorisee extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csType = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idDonneeHistorisee = null;

    /**
     * @return the csType
     */
    public String getCsType() {
        return csType;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idDonneeHistorisee;
    }

    /**
     * @return the idDonneeHistorisee
     */
    public String getIdDonneeHistorisee() {
        return idDonneeHistorisee;
    }

    /**
     * @param csType
     *            the csType to set
     */
    public void setCsType(String csType) {
        this.csType = csType;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idDonneeHistorisee = id;
    }

    /**
     * @param idDonneeHistorisee
     *            the idDonneeHistorisee to set
     */
    public void setIdDonneeHistorisee(String idDonneeHistorisee) {
        this.idDonneeHistorisee = idDonneeHistorisee;
    }

}
