package ch.globaz.orion.ws.cotisation;

import java.util.Date;
import ch.globaz.orion.ws.enums.SexeSalarie;

public class Salarie {
    private String nss;
    private String nomPrenom;
    private Date dateNaissance;
    private SexeSalarie sexe;
    private Date dateRetraite;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public SexeSalarie getSexe() {
        return sexe;
    }

    public void setSexe(SexeSalarie sexe) {
        this.sexe = sexe;
    }

    public Date getDateRetraite() {
        return dateRetraite;
    }

    public void setDateRetraite(Date dateRetraite) {
        this.dateRetraite = dateRetraite;
    }
}
