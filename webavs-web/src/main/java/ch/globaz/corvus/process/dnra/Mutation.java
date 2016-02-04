package ch.globaz.corvus.process.dnra;

import ch.globaz.common.domaine.Date;
import ch.globaz.pyxis.domaine.Sexe;

class Mutation {
    private String nss = null;
    private String newNss = null;
    private String codeMutation = null;
    private boolean isValide = false;
    private String nom = null;
    private String prenom = null;
    private Sexe sexe = null;
    private Date dateNaissance = null;
    private String codeNationalite = null;
    private String sourceDonnees = "";
    private Date dateDece;
    private String codeEtatCivil;
    private String raisonDuPartenariatDissous;
    private Date dateChangementEtatCivil;

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

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNewNss() {
        return newNss;
    }

    public void setNewNss(String newNss) {
        this.newNss = newNss;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }

    public boolean isValide() {
        return isValide;
    }

    public void setValide(boolean isValide) {
        this.isValide = isValide;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCodeNationalite() {
        return codeNationalite;
    }

    public void setCodeNationalite(String codeNationalite) {
        this.codeNationalite = codeNationalite;
    }

    public String getSourceDonnees() {
        return sourceDonnees;
    }

    public void setSourceDonnees(String sourceDonnees) {
        this.sourceDonnees = sourceDonnees;
    }

    public Date getDateDece() {
        return dateDece;
    }

    public void setDateDece(Date dateDece) {
        this.dateDece = dateDece;
    }

    public String getCodeEtatCivil() {
        return codeEtatCivil;
    }

    public void setCodeEtatCivil(String codeEtatCivil) {
        this.codeEtatCivil = codeEtatCivil;
    }

    public String getRaisonDuPartenariatDissous() {
        return raisonDuPartenariatDissous;
    }

    public void setRaisonDuPartenariatDissous(String raisonDuPartenariatDissous) {
        this.raisonDuPartenariatDissous = raisonDuPartenariatDissous;
    }

    public Date getDateChangementEtatCivil() {
        return dateChangementEtatCivil;
    }

    public void setDateChangementEtatCivil(Date dateChangementEtatCivil) {
        this.dateChangementEtatCivil = dateChangementEtatCivil;
    }
}
