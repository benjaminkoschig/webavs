package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ComptabiliserLotSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsProprietaire = null;
    private String forEtatCs = null;
    private String forIdLot = null;
    private String forTypeLot = null;

    public String getForCsProprietaire() {
        return forCsProprietaire;
    }

    public String getForEtatCs() {
        return forEtatCs;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForTypeLot() {
        return forTypeLot;
    }

    public void setForCsProprietaire(String forCsProprietaire) {
        this.forCsProprietaire = forCsProprietaire;
    }

    public void setForEtatCs(String forEtatCs) {
        this.forEtatCs = forEtatCs;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForTypeLot(String forTypeLot) {
        this.forTypeLot = forTypeLot;
    }

    @Override
    public Class<ComptabiliserLot> whichModelClass() {
        return ComptabiliserLot.class;
    }

}
