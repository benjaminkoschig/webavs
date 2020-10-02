package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleLienZoneLocalite extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String dateDebut = null;

    private String dateFin = null;

    private String idLienZoneLocalite = null;

    private String idLocalite = null;

    private String idZoneForfait = null;

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    private String pourcentage = null;

    @Override
    public String getId() {
        return idLienZoneLocalite;
    }

    /**
     * @return the idLienZoneLocalite
     */
    public String getIdLienZoneLocalite() {
        return idLienZoneLocalite;
    }

    /**
     * @return the idLocalite
     */
    public String getIdLocalite() {
        return idLocalite;
    }

    /**
     * @return the idZoneForfait
     */
    public String getIdZoneForfait() {
        return idZoneForfait;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idLienZoneLocalite = id;
    }

    /**
     * @param idLienZoneLocalite
     *            the idLienZoneLocalite to set
     */
    public void setIdLienZoneLocalite(String idLienZoneLocalite) {
        this.idLienZoneLocalite = idLienZoneLocalite;
    }

    /**
     * @param idLocalite
     *            the idLocalite to set
     */
    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    /**
     * @param idZoneForfait
     *            the idZoneForfait to set
     */
    public void setIdZoneForfait(String idZoneForfait) {
        this.idZoneForfait = idZoneForfait;
    }

    public String getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(String pourcentage) {
        this.pourcentage = pourcentage;
    }
}
