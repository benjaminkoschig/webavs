package ch.globaz.amal.businessimpl.services.sedexCO;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;

public class PersonneAssuree {
    private String nssAssure = null;
    private String nomPrenomAssure = null;
    private Periode primePeriode = null;
    private Montant primeMontant = null;
    private Periode sharingPeriode = null;
    private Montant sharingMontant = null;

    public String getNssAssure() {
        return nssAssure;
    }

    public void setNssAssure(String nssAssure) {
        this.nssAssure = nssAssure;
    }

    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    public void setNomPrenomAssure(String nomPrenomAssure) {
        this.nomPrenomAssure = nomPrenomAssure;
    }

    public Periode getPrimePeriode() {
        return primePeriode;
    }

    public void setPrimePeriode(Periode primePeriode) {
        this.primePeriode = primePeriode;
    }

    public Montant getPrimeMontant() {
        return primeMontant;
    }

    public void setPrimeMontant(Montant primeMontant) {
        this.primeMontant = primeMontant;
    }

    public Periode getSharingPeriode() {
        return sharingPeriode;
    }

    public void setSharingPeriode(Periode sharingPeriode) {
        this.sharingPeriode = sharingPeriode;
    }

    public Montant getSharingMontant() {
        return sharingMontant;
    }

    public void setSharingMontant(Montant sharingMontant) {
        this.sharingMontant = sharingMontant;
    }

}
