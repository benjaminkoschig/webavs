package globaz.pavo.db.inscriptions.declaration;

import java.util.ArrayList;
import java.util.List;

public class CIImportPucs4DetailResultInscriptionBean {

    private String nss = "";
    private String nom = "";
    private String moisDebut = "";
    private String moisFin = "";
    private String annee = "";
    private String genre = "";
    private String revenuAVS = "";
    private String revenuCAF = "";
    private List<String> ciAdd = new ArrayList<String>();
    private List<String> errors = new ArrayList<String>();
    private List<String> infos = new ArrayList<String>();

    public String getRevenuAVS() {
        return revenuAVS;
    }

    public void setRevenuAVS(String revenuAVS) {
        this.revenuAVS = revenuAVS;
    }

    public String getRevenuCAF() {
        return revenuCAF;
    }

    public void setRevenuCAF(String revenuCAF) {
        this.revenuCAF = revenuCAF;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
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

    public String getMoisDebut() {
        return moisDebut;
    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getCiAdd() {
        return ciAdd;
    }

    public void setCiAdd(List<String> ciAdd) {
        this.ciAdd = ciAdd;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getInfos() {
        return infos;
    }

    public void setInfos(List<String> infos) {
        this.infos = infos;
    }

}