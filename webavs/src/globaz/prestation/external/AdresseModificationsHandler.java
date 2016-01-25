package globaz.prestation.external;

import java.io.Serializable;

public class AdresseModificationsHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    private LigneAdresseExternalService attention = null;
    private LigneAdresseExternalService casePostale = null;
    private LigneAdresseExternalService ligneAdresse1 = null;
    private LigneAdresseExternalService ligneAdresse2 = null;
    private LigneAdresseExternalService ligneAdresse3 = null;
    private LigneAdresseExternalService ligneAdresse4 = null;
    private LigneAdresseExternalService numeroRue = null;
    private LigneAdresseExternalService rue = null;
    private LigneAdresseExternalService rueRepertoire = null;
    private LigneAdresseExternalService titreAdresse = null;
    private LigneAdresseExternalService localite = null;
    private LigneAdresseExternalService numPostal = null;

    private Boolean hasModifications = false;

    public Boolean hasModifications() {
        return hasModifications;
    }

    public LigneAdresseExternalService getAttention() {
        return attention;
    }

    public void setAttention(LigneAdresseExternalService attention) {
        this.attention = attention;
        hasModifications = true;
    }

    public LigneAdresseExternalService getCasePostale() {
        return casePostale;
    }

    public void setCasePostale(LigneAdresseExternalService casePostale) {
        this.casePostale = casePostale;
        hasModifications = true;
    }

    public LigneAdresseExternalService getLigneAdresse1() {
        return ligneAdresse1;
    }

    public void setLigneAdresse1(LigneAdresseExternalService ligneAdresse1) {
        this.ligneAdresse1 = ligneAdresse1;
        hasModifications = true;
    }

    public LigneAdresseExternalService getLigneAdresse2() {
        return ligneAdresse2;
    }

    public void setLigneAdresse2(LigneAdresseExternalService ligneAdresse2) {
        this.ligneAdresse2 = ligneAdresse2;
        hasModifications = true;
    }

    public LigneAdresseExternalService getLigneAdresse3() {
        return ligneAdresse3;
    }

    public void setLigneAdresse3(LigneAdresseExternalService ligneAdresse3) {
        this.ligneAdresse3 = ligneAdresse3;
        hasModifications = true;
    }

    public LigneAdresseExternalService getLigneAdresse4() {
        return ligneAdresse4;
    }

    public void setLigneAdresse4(LigneAdresseExternalService ligneAdresse4) {
        this.ligneAdresse4 = ligneAdresse4;
        hasModifications = true;
    }

    public LigneAdresseExternalService getNumeroRue() {
        return numeroRue;
    }

    public void setNumeroRue(LigneAdresseExternalService numeroRue) {
        this.numeroRue = numeroRue;
        hasModifications = true;
    }

    public LigneAdresseExternalService getRue() {
        return rue;
    }

    public void setRue(LigneAdresseExternalService rue) {
        this.rue = rue;
        hasModifications = true;
    }

    public LigneAdresseExternalService getRueRepertoire() {
        return rueRepertoire;
    }

    public void setRueRepertoire(LigneAdresseExternalService rueRepertoire) {
        this.rueRepertoire = rueRepertoire;
        hasModifications = true;
    }

    public LigneAdresseExternalService getTitreAdresse() {
        return titreAdresse;
    }

    public void setTitreAdresse(LigneAdresseExternalService titreAdresse) {
        this.titreAdresse = titreAdresse;
        hasModifications = true;
    }

    public Boolean getHasModifications() {
        return hasModifications;
    }

    public void setHasModifications(Boolean hasModifications) {
        this.hasModifications = hasModifications;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public LigneAdresseExternalService getLocalite() {
        return localite;
    }

    public void setLocalite(LigneAdresseExternalService localite) {
        this.localite = localite;
    }

    public LigneAdresseExternalService getNumPostal() {
        return numPostal;
    }

    public void setNumPostal(LigneAdresseExternalService numPostal) {
        this.numPostal = numPostal;
    }

}
