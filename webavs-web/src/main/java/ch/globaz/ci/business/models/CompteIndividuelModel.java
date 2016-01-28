package ch.globaz.ci.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class CompteIndividuelModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateNaissance = null;
    private String idCompte = null;
    private String idNationalite = null;
    private String nomPrenom = null;
    private String numAvsActuel = null;
    private String sexe = null;

    public String getDateNaissance() {
        return dateNaissance;
    }

    @Override
    public String getId() {
        return idCompte;
    }

    public String getIdCompte() {
        return idCompte;
    }

    public String getIdNationalite() {
        return idNationalite;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumAvsActuel() {
        return numAvsActuel;
    }

    public String getSexe() {
        return sexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public void setId(String id) {
        idCompte = id;

    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    public void setIdNationalite(String idNationalite) {
        this.idNationalite = idNationalite;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNumAvsActuel(String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
