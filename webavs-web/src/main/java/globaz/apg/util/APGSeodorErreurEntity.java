package globaz.apg.util;

import java.util.List;

public class APGSeodorErreurEntity {
    String dateDebutPeriode = "";
    String dateFinPeriode = "";
    int codeService;
    Long nombreJours;
    String messageErreur;

    public APGSeodorErreurEntity(){

    }

    public APGSeodorErreurEntity(String messageErreur){
        this.messageErreur = messageErreur;
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

    public String getMessageErreur() {
        return messageErreur;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }
}
