/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFVerificationConventionData {

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String csDegreApi = "";
    private String csGenrePc = "";
    private String csTypeBeneficiaire = "";
    private String csTypePc = "";
    private String dateDebutTraitement = "";
    private String dateFacture = "";
    private String etatDemande = "";
    private String idDemandeToIgnore = "";
    private String idFournisseur = "";
    private String idQdPrincipale = "";
    private String idTiers = "";
    private String montantAPayer = "";

    public RFVerificationConventionData(String idTiers, String codeSousTypeDeSoin, String codeTypeDeSoin,
            String idFournisseur, String dateDebutTraitement, String dateFacture, String etatDemande,
            String montantAPayer, String idQdPrincipale, String idDemandeToIgnore, String csTypeBeneficiaire,
            String csTypePc, String csGenrePc, String degreApi) {
        this.idTiers = idTiers;
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
        this.codeTypeDeSoin = codeTypeDeSoin;
        this.idFournisseur = idFournisseur;
        this.dateDebutTraitement = dateDebutTraitement;
        this.dateFacture = dateFacture;
        this.etatDemande = etatDemande;
        this.montantAPayer = montantAPayer;
        this.idQdPrincipale = idQdPrincipale;
        this.idDemandeToIgnore = idDemandeToIgnore;
        this.csTypeBeneficiaire = csTypeBeneficiaire;
        this.csGenrePc = csGenrePc;
        this.csTypePc = csTypePc;
        csDegreApi = degreApi;
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCsDegreApi() {
        return csDegreApi;
    }

    public String getCsGenrePc() {
        return csGenrePc;
    }

    public String getCsTypeBeneficiaire() {
        return csTypeBeneficiaire;
    }

    public String getCsTypePc() {
        return csTypePc;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getEtatDemande() {
        return etatDemande;
    }

    public String getIdDemandeToIgnore() {
        return idDemandeToIgnore;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantAPayer() {
        return montantAPayer;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCsDegreApi(String csDegreApi) {
        this.csDegreApi = csDegreApi;
    }

    public void setCsGenrePc(String csGenrePc) {
        this.csGenrePc = csGenrePc;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        this.csTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setCsTypePc(String csTypePc) {
        this.csTypePc = csTypePc;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setEtatDemande(String etatDemande) {
        this.etatDemande = etatDemande;
    }

    public void setIdDemandeToIgnore(String idDemandeToIgnore) {
        this.idDemandeToIgnore = idDemandeToIgnore;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantAPayer(String montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

}
