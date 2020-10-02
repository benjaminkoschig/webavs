package ch.globaz.pegasus.excel.model;

import java.io.Serializable;

public class PCListeComparaisonCommuneModel implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    String idDroit = "";
    String idTiers= "";
    String idDonneePersonne="";
    String nss= "";
    String nom= "";
    String idLocaliteFromPc ="";
    String idLocaliteFromTiers="";
    String nssRequerant= "";
    String nomRequerant= "";
    String idLocaliteFromRequerant="";
    String nomLocaliteFromPC="";
    String nomLocaliteFromTiers="";
    String csRoleFamille="";
    boolean isRequerant;
    String remarque= "";
    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getIdDonneePersonne() {
        return idDonneePersonne;
    }

    public void setIdDonneePersonne(String idDonneePersonne) {
        this.idDonneePersonne = idDonneePersonne;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public String getNssRequerant() {
        return nssRequerant;
    }

    public void setNssRequerant(String nssRequerant) {
        this.nssRequerant = nssRequerant;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getIdLocaliteFromPc() {
        return idLocaliteFromPc;
    }

    public void setIdLocaliteFromPc(String idLocaliteFromPc) {
        this.idLocaliteFromPc = idLocaliteFromPc;
    }

    public String getIdLocaliteFromTiers() {
        return idLocaliteFromTiers;
    }

    public void setIdLocaliteFromTiers(String idLocaliteFromTiers) {
        this.idLocaliteFromTiers = idLocaliteFromTiers;
    }

    public String getNomLocaliteFromPC() {
        return nomLocaliteFromPC;
    }

    public void setNomLocaliteFromPC(String nomLocaliteFromPC) {
        this.nomLocaliteFromPC = nomLocaliteFromPC;
    }

    public String getNomLocaliteFromTiers() {
        return nomLocaliteFromTiers;
    }

    public void setNomLocaliteFromTiers(String nomLocaliteFromTiers) {
        this.nomLocaliteFromTiers = nomLocaliteFromTiers;
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
    public String getIdLocaliteFromRequerant() {
        return idLocaliteFromRequerant;
    }

    public void setIdLocaliteFromRequerant(String idLocaliteFromRequerant) {
        this.idLocaliteFromRequerant = idLocaliteFromRequerant;
    }
    public String getCsRoleFamille() {
        return csRoleFamille;
    }

    public void setCsRoleFamille(String csRoleFamille) {
        this.csRoleFamille = csRoleFamille;
    }

    public boolean isRequerant() {
        return isRequerant;
    }

    public void setRequerant(boolean requerant) {
        isRequerant = requerant;
    }
}
