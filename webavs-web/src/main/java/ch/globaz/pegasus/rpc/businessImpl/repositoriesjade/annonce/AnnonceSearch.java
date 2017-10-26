package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;

public class AnnonceSearch {
    private String annonceId;
    private String nss;
    private String nom;
    private String prenom;
    private EtatAnnonce etat;
    private CodeTraitement codeTraitement;
    private Date periodeDateDebut;
    private Date periodeDateFin;
    private boolean rechercheFamille;
    private String order;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
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

    public EtatAnnonce getEtat() {
        return etat;
    }

    public void setEtat(EtatAnnonce etat) {
        this.etat = etat;
    }

    public CodeTraitement getCodeTraitement() {
        return codeTraitement;
    }

    public void setCodeTraitement(CodeTraitement codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    public Date getPeriodeDateDebut() {
        return periodeDateDebut;
    }

    public void setPeriodeDateDebut(Date periodeDateDebut) {
        this.periodeDateDebut = periodeDateDebut;
    }

    public Date getPeriodeDateFin() {
        return periodeDateFin;
    }

    public void setPeriodeDateFin(Date periodeDateFin) {
        this.periodeDateFin = periodeDateFin;
    }

    public boolean isRechercheFamille() {
        return rechercheFamille;
    }

    public void setRechercheFamille(boolean rechercheFamille) {
        this.rechercheFamille = rechercheFamille;
    }

    public String getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(String annonceId) {
        this.annonceId = annonceId;
    }

}
