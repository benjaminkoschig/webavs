package ch.globaz.corvus.business.models.recapinfo;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleInfoRecap extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private String date;
    private String id;
    private String idLot;
    private String idTiers;
    private String montant;

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void setId(String id) {
        this.id = id;

    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
