package ch.globaz.vulpecula.business.models.controleemployeur;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ControleEmployeurSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 4066722389039318943L;

    private String id;
    private String idEmployeur;
    private String idUtilisateur;
    private String numeroMeroba;
    private String montant;
    private String dateControle;
    private String dateAu;
    private String type;
    private Boolean autresMesures;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNumeroMeroba() {
        return numeroMeroba;
    }

    public void setNumeroMeroba(String numeroMeroba) {
        this.numeroMeroba = numeroMeroba;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDateControle() {
        return dateControle;
    }

    public void setDateControle(String dateControle) {
        this.dateControle = dateControle;
    }

    public String getDateAu() {
        return dateAu;
    }

    public void setDateAu(String dateAu) {
        this.dateAu = dateAu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAutresMesures() {
        return autresMesures;
    }

    public void setAutresMesures(Boolean autresMesures) {
        this.autresMesures = autresMesures;
    }
}
