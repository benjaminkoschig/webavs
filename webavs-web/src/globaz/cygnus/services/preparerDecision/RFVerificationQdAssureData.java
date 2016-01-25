/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFVerificationQdAssureData {

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String dateDebutTraitement = "";
    private String dateFacture = "";
    private String idQdPrincipale = "";
    private String idTiers = "";
    private boolean isBeneficiaireEnfant = false;
    private String montantAPayer = "";

    public RFVerificationQdAssureData(String dateDebutTraitement, String dateFacture, String idTiers,
            String montantAPayer, String idQdPrincipale, String codeSousTypeDeSoin, String codeTypeDeSoin,
            boolean isBeneficiaireEnfant) {
        this.dateDebutTraitement = dateDebutTraitement;
        this.dateFacture = dateFacture;
        this.idTiers = idTiers;
        this.montantAPayer = montantAPayer;
        this.idQdPrincipale = idQdPrincipale;
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
        this.codeTypeDeSoin = codeTypeDeSoin;
        this.isBeneficiaireEnfant = isBeneficiaireEnfant;
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateFacture() {
        return dateFacture;
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

    public boolean isBeneficiaireEnfant() {
        return isBeneficiaireEnfant;
    }

    public void setBeneficiaireEnfant(boolean isBeneficiaireEnfant) {
        this.isBeneficiaireEnfant = isBeneficiaireEnfant;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
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
