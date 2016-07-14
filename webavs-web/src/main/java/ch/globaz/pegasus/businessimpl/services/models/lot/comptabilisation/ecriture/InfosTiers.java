package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;

public class InfosTiers {
    private CompteAnnexeSimpleModel compteAnnexe;
    private String idDomaineApplication;
    private String idTiers;
    private String idTiersAddressePaiement;
    private String nss;
    private String nom;
    private String prenom;

    public InfosTiers() {
    }

    public CompteAnnexeSimpleModel getCompteAnnexe() {
        return compteAnnexe;
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

    public String getIdCompteAnnexe() {
        if (compteAnnexe == null) {
            return null;
        }
        return compteAnnexe.getIdCompteAnnexe();
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAddressePaiement() {
        return idTiersAddressePaiement;
    }

    public void setCompteAnnexe(CompteAnnexeSimpleModel compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAddressePaiement(String idTiersAddressePaiement) {
        this.idTiersAddressePaiement = idTiersAddressePaiement;
    }

    @Override
    public String toString() {
        return "InfosTiers [ idDomaineApplication=" + idDomaineApplication + ", idTiers=" + idTiers
                + ", idTiersAddressePaiement=" + idTiersAddressePaiement + ", idCompteAnnexe="
                + compteAnnexe.getIdCompteAnnexe() + " ]";
    }
}
