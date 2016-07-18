package globaz.cygnus.services.comptabilite;

/**
 * @author fha
 */
public class RFOrdreVersementData {

    private String csRole = "";
    private String idDomaineApplication = "";
    private String idExterne = "";
    private String idOrdreVersement = "";
    private String idRole = "";
    private String idSectionDette = "";
    private String idSousTypeSoin = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private String idTypeSoin = "";
    private Boolean isCompense = Boolean.FALSE;
    private Boolean isForcerPayement = Boolean.FALSE;
    private Boolean isImportation = Boolean.FALSE;
    private String montantDepassementQD = "";
    private String montantOrdreVersement = "";
    private String nomTiers = "";
    private String nssTiers = "";
    private String numeroFacture = "";
    private String prenomTiers = "";
    private String typeOrdreVersement = "";
    private String refPaiement;

    public RFOrdreVersementData(String idOrdreVersement, String typeOrdreVersement, String numeroFacture,
            String idExterne, String csRole, String idDomaineApplication, String idTiersAdressePaiement,
            String idTiers, String montantOrdreVersement, String idRole, String idTypeSoin, Boolean isForcerPayement,
            String nssTiers, String nomTiers, String prenomTiers, String montantDepassementQD, Boolean isImportation,
            Boolean isCompense, String idSousTypeSoin, String idSectionDette, String refPaiement) {
        super();
        this.idOrdreVersement = idOrdreVersement;
        this.typeOrdreVersement = typeOrdreVersement;
        this.numeroFacture = numeroFacture;
        this.idExterne = idExterne;
        this.csRole = csRole;
        this.idDomaineApplication = idDomaineApplication;
        this.idTiersAdressePaiement = idTiersAdressePaiement;
        this.idTiers = idTiers;
        this.montantOrdreVersement = montantOrdreVersement;
        this.idRole = idRole;
        this.idTypeSoin = idTypeSoin;
        this.isForcerPayement = isForcerPayement;
        this.nssTiers = nssTiers;
        this.nomTiers = nomTiers;
        this.prenomTiers = prenomTiers;
        this.montantDepassementQD = montantDepassementQD;
        this.isImportation = isImportation;
        this.isCompense = isCompense;
        this.idSousTypeSoin = idSousTypeSoin;
        this.idSectionDette = idSectionDette;
        this.refPaiement = refPaiement;
    }

    public String getCsRole() {
        return csRole;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getIdSectionDette() {
        return idSectionDette;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public Boolean getIsCompense() {
        return isCompense;
    }

    public Boolean getIsForcerPayement() {
        return isForcerPayement;
    }

    public Boolean getIsImportation() {
        return isImportation;
    }

    public String getMontantDepassementQD() {
        return montantDepassementQD;
    }

    public String getMontantOrdreVersement() {
        return montantOrdreVersement;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getNssTiers() {
        return nssTiers;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public String getTypeOrdreVersement() {
        return typeOrdreVersement;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setIdSectionDette(String idSectionDette) {
        this.idSectionDette = idSectionDette;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setIsCompense(Boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setIsForcerPayement(Boolean isForcerPayement) {
        this.isForcerPayement = isForcerPayement;
    }

    public void setIsImportation(Boolean isImportation) {
        this.isImportation = isImportation;
    }

    public void setMontantDepassementQD(String montantDepassementQD) {
        this.montantDepassementQD = montantDepassementQD;
    }

    public void setMontantOrdreVersement(String montantOrdreVersement) {
        this.montantOrdreVersement = montantOrdreVersement;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public void setTypeOrdreVersement(String typeOrdreVersement) {
        this.typeOrdreVersement = typeOrdreVersement;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

}
