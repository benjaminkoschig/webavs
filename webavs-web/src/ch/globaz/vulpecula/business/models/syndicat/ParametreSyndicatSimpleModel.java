package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ParametreSyndicatSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 5123278940631222673L;

    private String id;
    private String idCaisseMetier;
    private String idSyndicat;
    private String pourcentage;
    private String montantParTravailleur;
    private String dateDebut;
    private String dateFin;

    public String getIdCaisseMetier() {
        return idCaisseMetier;
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        this.idCaisseMetier = idCaisseMetier;
    }

    public String getIdSyndicat() {
        return idSyndicat;
    }

    public void setIdSyndicat(String idSyndicat) {
        this.idSyndicat = idSyndicat;
    }

    public String getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(String pourcentage) {
        this.pourcentage = pourcentage;
    }

    public String getMontantParTravailleur() {
        return montantParTravailleur;
    }

    public void setMontantParTravailleur(String montantParTravailleur) {
        this.montantParTravailleur = montantParTravailleur;
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
