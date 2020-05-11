package globaz.apg.util;

import java.util.List;

public class APGSeodorErreurEntity {
    String dateDebutPeriode = "";
    String dateFinPeriode = "";
    int codeService;
    Long nombreJours;

    public APGSeodorErreurEntity(){

    }

    public APGSeodorErreurEntity(String dateDebutPeriode, String dateFinPeriode, int codeService, Long nombreJours) {
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
        this.codeService = codeService;
        this.nombreJours = nombreJours;
    }

    public String toString(){
        return dateDebutPeriode + " - " + dateFinPeriode + " - " + nombreJours + " - " + codeService;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public int getCodeService() {
        return codeService;
    }

    public void setCodeService(int codeService) {
        this.codeService = codeService;
    }

    public Long getNombreJours() {
        return nombreJours;
    }

    public void setNombreJours(Long nombreJours) {
        this.nombreJours = nombreJours;
    }
}
