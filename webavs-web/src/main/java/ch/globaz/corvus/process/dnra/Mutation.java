package ch.globaz.corvus.process.dnra;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.DateRente;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.Sexe;

class Mutation {
    private String nss = null;
    private String newNss = null;
    private boolean isValide = false;
    private String nom = null;
    private String prenom = null;
    private Sexe sexe = null;
    private DateRente dateNaissance = null;
    private String codeNationalite = null;
    private String sourceDonnees = "";
    private Date dateDece;
    private EtatCivil etatCivil;
    private String raisonDuPartenariatDissous;
    private DateRente dateChangementEtatCivil;
    private Pays pays;
    private TypeMutation typeMutation;
    private String nssInactive;

    public TypeMutation getTypeMutation() {
        return typeMutation;
    }

    public String getNssInactive() {
        return nssInactive;
    }

    public void setNssInactive(String nssInactive) {
        this.nssInactive = nssInactive;
    }

    public void setTypeMutation(TypeMutation typeMutation) {
        this.typeMutation = typeMutation;
    }

    public EtatCivil getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(EtatCivil etatCivil) {
        this.etatCivil = etatCivil;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
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

    public DateRente getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(DateRente dateNaissance) {
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

    public String getRaisonDuPartenariatDissous() {
        return raisonDuPartenariatDissous;
    }

    public void setRaisonDuPartenariatDissous(String raisonDuPartenariatDissous) {
        this.raisonDuPartenariatDissous = raisonDuPartenariatDissous;
    }

    public DateRente getDateChangementEtatCivil() {
        return dateChangementEtatCivil;
    }

    public void setDateChangementEtatCivil(DateRente dateChangementEtatCivil) {
        this.dateChangementEtatCivil = dateChangementEtatCivil;
    }

    @Override
    public String toString() {
        return "Mutation [nss=" + nss + ", newNss=" + newNss + ", isValide=" + isValide + ", nom=" + nom + ", prenom="
                + prenom + ", sexe=" + sexe + ", dateNaissance=" + dateNaissance + ", codeNationalite="
                + codeNationalite + ", sourceDonnees=" + sourceDonnees + ", dateDece=" + dateDece + ", etatCivil="
                + etatCivil + ", raisonDuPartenariatDissous=" + raisonDuPartenariatDissous
                + ", dateChangementEtatCivil=" + dateChangementEtatCivil + ", pays=" + pays + ", typeMutation="
                + typeMutation + ", nssInactive=" + nssInactive + "]";
    }

}
