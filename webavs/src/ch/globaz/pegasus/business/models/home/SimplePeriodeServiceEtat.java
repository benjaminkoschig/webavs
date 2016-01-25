package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePeriodeServiceEtat extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csServiceEtat = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idHome = null;
    private String idSimplePeriodeServiceEtat = null;

    /**
     * @return the csServiceEtat
     */
    public String getCsServiceEtat() {
        return csServiceEtat;
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
        return idSimplePeriodeServiceEtat;
    }

    /**
     * @return the idHome
     */
    public String getIdHome() {
        return idHome;
    }

    /**
     * @return the idSimplePeriodeServiceEtat
     */
    public String getIdSimplePeriodeServiceEtat() {
        return idSimplePeriodeServiceEtat;
    }

    /**
     * @param csServiceEtat
     *            the csServiceEtat to set
     */
    public void setCsServiceEtat(String csServiceEtat) {
        this.csServiceEtat = csServiceEtat;
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
        idSimplePeriodeServiceEtat = id;
    }

    /**
     * @param idHome
     *            the idHome to set
     */
    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    /**
     * @param idSimplePeriodeServiceEtat
     *            the idSimplePeriodeServiceEtat to set
     */
    public void setIdSimplePeriodeServiceEtat(String idSimplePeriodeServiceEtat) {
        this.idSimplePeriodeServiceEtat = idSimplePeriodeServiceEtat;
    }

}
