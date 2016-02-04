package ch.globaz.corvus.process.dnra;

import ch.globaz.common.domaine.Date;
import ch.globaz.pyxis.domaine.Sexe;

public class InfoTiers {
    private String nss;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private Sexe sexe = null;
    private String codeNationalite = null;
    private Date dateDeces;
    private String codeEtatCivil;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
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

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getCodeNationalite() {
        return codeNationalite;
    }

    public void setCodeNationalite(String codeNationalite) {
        this.codeNationalite = codeNationalite;
    }

    public Date getDateDeces() {
        return dateDeces;
    }

    public void setDateDeces(Date dateDeces) {
        this.dateDeces = dateDeces;
    }

    public String getCodeEtatCivil() {
        return codeEtatCivil;
    }

    public void setCodeEtatCivil(String codeEtatCivil) {
        this.codeEtatCivil = codeEtatCivil;
    }
}
