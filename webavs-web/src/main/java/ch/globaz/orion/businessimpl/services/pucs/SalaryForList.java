package ch.globaz.orion.businessimpl.services.pucs;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalary;

public class SalaryForList implements SalaryForListInterface {

    private String nss;
    private String nomPrenom;
    private Date dateNaissance;
    private String sexe;
    private PeriodeSalary periode;
    private String canton;
    private Montant slaire;
    private Montant ac1;
    private Montant ac2;
    private Montant af;
    private String cantonAf;

    @Override
    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Override
    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    @Override
    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @Override
    public PeriodeSalary getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodeSalary periode) {
        this.periode = periode;
    }

    @Override
    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    @Override
    public Montant getSlaire() {
        return slaire;
    }

    public void setSlaire(Montant slaire) {
        this.slaire = slaire;
    }

    @Override
    public Montant getAc1() {
        return ac1;
    }

    public void setAc1(Montant ac1) {
        this.ac1 = ac1;
    }

    @Override
    public Montant getAc2() {
        return ac2;
    }

    public void setAc2(Montant ac2) {
        this.ac2 = ac2;
    }

    @Override
    public Montant getAf() {
        return af;
    }

    public void setAf(Montant af) {
        this.af = af;
    }

    @Override
    public String getCantonAf() {
        return cantonAf;
    }

    public void setCantonAf(String cantonAf) {
        this.cantonAf = cantonAf;
    }

    @Override
    public String toString() {
        return "SalariesForList [nss=" + nss + ", nomPrenom=" + nomPrenom + ", dateNaissance=" + dateNaissance
                + ", sexe=" + sexe + ", periode=" + periode + ", canton=" + canton + ", slaire=" + slaire + ", ac1="
                + ac1 + ", ac2=" + ac2 + ", af=" + af + ", cantonAf=" + cantonAf + "]";
    }

}
