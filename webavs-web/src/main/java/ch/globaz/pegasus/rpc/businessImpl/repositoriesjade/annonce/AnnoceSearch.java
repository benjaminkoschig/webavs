package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

public class AnnoceSearch {
    private String nss;
    private String nom;
    private String prenom;
    private String etat;
    private String codeTraitement;
    private String periode;
    private boolean rechercheFamille;

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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCodeTraitement() {
        return codeTraitement;
    }

    public void setCodeTraitement(String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public boolean isRechercheFamille() {
        return rechercheFamille;
    }

    public void setRechercheFamille(boolean rechercheFamille) {
        this.rechercheFamille = rechercheFamille;
    }

}
