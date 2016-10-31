/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFVerificationQdPrincipaleData {

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String dateDebutTraitement = "";
    private String dateFacture = "";
    private String idTiers = "";
    private String montantAPayer = "";
    private String idQdPrincipale = "";

    public RFVerificationQdPrincipaleData(String dateDebutTraitement, String dateFacture, String idTiers,
            String montantAPayer, String codeTypeDeSoin, String codeSousTypeDeSoin) {
        this.dateDebutTraitement = dateDebutTraitement;
        this.dateFacture = dateFacture;
        this.idTiers = idTiers;
        this.montantAPayer = montantAPayer;
        this.codeTypeDeSoin = codeTypeDeSoin;
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public RFVerificationQdPrincipaleData(String idQdPrincipale, String dateDebutTraitement, String dateFacture,
            String idTiers, String montantAPayer, String codeTypeDeSoin, String codeSousTypeDeSoin) {
        this.dateDebutTraitement = dateDebutTraitement;
        this.dateFacture = dateFacture;
        this.idTiers = idTiers;
        this.montantAPayer = montantAPayer;
        this.codeTypeDeSoin = codeTypeDeSoin;
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
        this.idQdPrincipale = idQdPrincipale;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
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

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantAPayer(String montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

}
