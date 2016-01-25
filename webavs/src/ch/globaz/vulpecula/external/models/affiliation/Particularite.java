package ch.globaz.vulpecula.external.models.affiliation;

import ch.globaz.vulpecula.domain.models.common.Date;

/**
 * Représente une particularité de l'affiliation
 * 
 * @author Jonas Paratte (JPA) | Créé le 12.11.2014
 * 
 */
public class Particularite {
    private String id;
    private String affiliationId;
    private String particularite;
    private Date dateDebut;
    private Date dateFin;
    private String champNumerique;
    private String champAlphanumerique;

    public String getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public String getParticularite() {
        return particularite;
    }

    public void setParticularite(String particularite) {
        this.particularite = particularite;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getChampNumerique() {
        return champNumerique;
    }

    public void setChampNumerique(String champNumerique) {
        this.champNumerique = champNumerique;
    }

    public String getChampAlphanumerique() {
        return champAlphanumerique;
    }

    public void setChampAlphanumerique(String champAlphanumerique) {
        this.champAlphanumerique = champAlphanumerique;
    }

    /**
     * Retourne l'id de l'assurance
     * 
     * @return String représentant l'id
     */
    public String getId() {
        return id;
    }

    /**
     * Mise à jour de l'id de l'assurance
     * 
     * @param id Nouvel id de l'assurance
     */
    public void setId(final String id) {
        this.id = id;
    }

}
