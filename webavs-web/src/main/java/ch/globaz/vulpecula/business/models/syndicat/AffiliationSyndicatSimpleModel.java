package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AffiliationSyndicatSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 244340009820581962L;

    private String id;
    private String idTravailleur;
    private String idSyndicat;
    private String dateDebut;
    private String dateFin;

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getIdSyndicat() {
        return idSyndicat;
    }

    public void setIdSyndicat(String idSyndicat) {
        this.idSyndicat = idSyndicat;
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
