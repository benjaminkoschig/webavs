package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeComplexModel;

public class OperationComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -4443397293920098785L;

    private String idOperation;
    private String idTiers;
    private String numAvsActuel;
    private String titre;
    private String nom;
    private String prenom;
    private String npa;
    private String dateNaissance;
    private String cantonResidence;
    private String montant;
    private String date;
    private String referencePermis;
    private String langue;
    private String idTypeOperation;
    private String idExterne;
    private String spy;

    @Override
    public String getId() {
        return idOperation;
    }

    @Override
    public void setId(String id) {
        idOperation = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdOperation() {
        return idOperation;
    }

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getNumAvsActuel() {
        return numAvsActuel;
    }

    public void setNumAvsActuel(String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCantonResidence() {
        return cantonResidence;
    }

    public void setCantonResidence(String cantonResidence) {
        this.cantonResidence = cantonResidence;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getReferencePermis() {
        return referencePermis;
    }

    public void setReferencePermis(String referencePermis) {
        this.referencePermis = referencePermis;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    public void setIdTypeOperation(String idTypeOperation) {
        this.idTypeOperation = idTypeOperation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }
}
