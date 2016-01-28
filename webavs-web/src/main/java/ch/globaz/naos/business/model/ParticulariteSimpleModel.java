package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ParticulariteSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 421709565519321594L;

    private String particulariteId;
    private String affiliationId;
    private String particularite;
    private String dateDebut;
    private String dateFin;
    private String champNumerique;
    private String champAlphanumerique;

    public String getParticulariteId() {
        return particulariteId;
    }

    public void setParticulariteId(String particulariteId) {
        this.particulariteId = particulariteId;
    }

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

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
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

    @Override
    public String getId() {
        return particulariteId;
    }

    @Override
    public void setId(String particulariteId) {
        this.particulariteId = particulariteId;
    }
}
