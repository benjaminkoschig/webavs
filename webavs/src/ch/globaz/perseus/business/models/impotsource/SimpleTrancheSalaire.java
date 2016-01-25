package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleTrancheSalaire extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeDebut = null;
    private String anneeFin = null;
    private String idTrancheSalaire = null;
    private String salaireBrutInferieur = null;
    private String salaireBrutSuperieur = null;

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    @Override
    public String getId() {
        return idTrancheSalaire;
    }

    public String getIdTrancheSalaire() {
        return idTrancheSalaire;
    }

    public String getSalaireBrutInferieur() {
        return salaireBrutInferieur;
    }

    public String getSalaireBrutSuperieur() {
        return salaireBrutSuperieur;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    @Override
    public void setId(String id) {
        idTrancheSalaire = id;

    }

    public void setIdTrancheSalaire(String idTrancheSalaire) {
        this.idTrancheSalaire = idTrancheSalaire;
    }

    public void setSalaireBrutInferieur(String salaireBrutInferieur) {
        this.salaireBrutInferieur = salaireBrutInferieur;
    }

    public void setSalaireBrutSuperieur(String salaireBrutSuperieur) {
        this.salaireBrutSuperieur = salaireBrutSuperieur;
    }

}
