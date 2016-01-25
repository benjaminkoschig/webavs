package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;

public class InfosTiers {
    private CompteAnnexeSimpleModel compteAnnexe;
    private String idDomaineApplication;
    private String idTiers;
    private String idTiersAddressePaiement;

    public InfosTiers() {
    }

    public InfosTiers(CompteAnnexeSimpleModel compteAnnexe, String idDomaineApplication, String idTiers,
            String idTiersAddressePaiement) {
        super();
        this.compteAnnexe = compteAnnexe;
        this.idDomaineApplication = idDomaineApplication;
        this.idTiers = idTiers;
        this.idTiersAddressePaiement = idTiersAddressePaiement;
    }

    public CompteAnnexeSimpleModel getCompteAnnexe() {
        return compteAnnexe;
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
