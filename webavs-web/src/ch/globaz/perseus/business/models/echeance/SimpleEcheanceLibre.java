package ch.globaz.perseus.business.models.echeance;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleEcheanceLibre extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateButoire = null;
    private String description = null;
    private String idDossier = null;
    private String idSimpleEcheanceLibre = null;
    private String motif = null;

    public String getDateButoire() {
        return dateButoire;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getId() {
        return idSimpleEcheanceLibre;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdSimpleEcheanceLibre() {
        return idSimpleEcheanceLibre;
    }

    public String getMotif() {
        return motif;
    }

    public void setDateButoire(String dateButoire) {
        this.dateButoire = dateButoire;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(String idSimpleEcheanceLibre) {
        this.idSimpleEcheanceLibre = idSimpleEcheanceLibre;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdSimpleEcheanceLibre(String idSimpleEcheanceLibre) {
        this.idSimpleEcheanceLibre = idSimpleEcheanceLibre;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

}
