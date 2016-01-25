package ch.globaz.vulpecula.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AffiliationCaisseMaladieSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 507690924369064905L;

    private String id;
    private String idTravailleur;
    private String idCaisseMaladie;
    private String moisDebut;
    private String moisFin;
    private String dateDebutAnnonce;
    private String dateFinAnnonce;
    private String idPosteTravail;

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    public String getDateDebutAnnonce() {
        return dateDebutAnnonce;
    }

    public void setDateDebutAnnonce(String dateDebutAnnonce) {
        this.dateDebutAnnonce = dateDebutAnnonce;
    }

    public String getDateFinAnnonce() {
        return dateFinAnnonce;
    }

    public void setDateFinAnnonce(String dateFinAnnonce) {
        this.dateFinAnnonce = dateFinAnnonce;
    }

    public String getIdCaisseMaladie() {
        return idCaisseMaladie;
    }

    public void setIdCaisseMaladie(String idCaisseMaladie) {
        this.idCaisseMaladie = idCaisseMaladie;
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
