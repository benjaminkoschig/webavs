package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LigneTaxationSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idTaxationOffice;
    private String idCotisation;
    private String masse;
    private String taux;
    private String montant;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdTaxationOffice() {
        return idTaxationOffice;
    }

    public void setIdTaxationOffice(String idTaxationOffice) {
        this.idTaxationOffice = idTaxationOffice;
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public String getMasse() {
        return masse;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
