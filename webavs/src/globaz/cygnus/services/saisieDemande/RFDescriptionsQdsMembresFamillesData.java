package globaz.cygnus.services.saisieDemande;

public class RFDescriptionsQdsMembresFamillesData {

    String adressePaiement = "";
    String descriptionQd = "";
    String idTiers = null;
    String membresFamilleCC = null;

    public String getAdressePaiement() {
        return adressePaiement;
    }

    public String getDescriptionQd() {
        return descriptionQd;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMembresFamilleCC() {
        return membresFamilleCC;
    }

    public void setAdressePaiement(String adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    public void setDescriptionQd(String descriptionQd) {
        this.descriptionQd = descriptionQd;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMembresFamilleCC(String membresFamilleCC) {
        this.membresFamilleCC = membresFamilleCC;
    }

}
