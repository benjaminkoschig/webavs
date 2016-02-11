package ch.globaz.corvus.process.dnra;

import ch.globaz.common.domaine.DateRente;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.Sexe;

public class InfoTiers {
    private String idTiers;
    private String nss;
    private String nom;
    private String prenom;
    private String nomUpper;
    private String prenomUpper;
    private DateRente dateNaissance;
    private Sexe sexe;
    private String codeNationalite;
    private DateRente dateDeces;
    private Pays pays;
    private EtatCivil etatCivil;

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

    public DateRente getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(DateRente dateNaissance) {
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

    public DateRente getDateDeces() {
        return dateDeces;
    }

    public void setDateDeces(DateRente dateDeces) {
        this.dateDeces = dateDeces;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getNomUpper() {
        return nomUpper;
    }

    public void setNomUpper(String nomUpper) {
        this.nomUpper = nomUpper;
    }

    public String getPrenomUpper() {
        return prenomUpper;
    }

    public void setPrenomUpper(String prenomUpper) {
        this.prenomUpper = prenomUpper;
    }

    @Override
    public String toString() {
        return "InfoTiers [idTiers=" + idTiers + ", nss=" + nss + ", nom=" + nom + ", prenom=" + prenom + ", nomUpper="
                + nomUpper + ", prenomUpper=" + prenomUpper + ", dateNaissance=" + dateNaissance + ", sexe=" + sexe
                + ", codeNationalite=" + codeNationalite + ", dateDeces=" + dateDeces + ", pays=" + pays
                + ", etatCivil=" + etatCivil + "]";
    }

}
