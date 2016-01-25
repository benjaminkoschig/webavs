/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFVerificationTypesDeSoinsData {

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String csGenrePcAccordee = "";
    private String csTypeCsBeneficiaire = "";
    private String csTypePcAccordee = "";
    private Boolean isConventionne = false;
    private String montantAPayer = "";
    private String montantFacture44 = "";
    private String montantVerseOAI = "";

    public RFVerificationTypesDeSoinsData(String codeTypeDeSoin, String codeSousTypeDeSoin, Boolean isConventionne,
            String montantAPayer, String montantVerseOAI, String montantFacture44, String csTypePcAccordee,
            String csTypeCsBeneficiaire, String csGenrePcAccordee) {
        this.codeTypeDeSoin = codeTypeDeSoin;
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
        this.isConventionne = isConventionne;
        this.montantAPayer = montantAPayer;
        this.montantVerseOAI = montantVerseOAI;
        this.montantFacture44 = montantFacture44;
        this.csTypePcAccordee = csTypePcAccordee;
        this.csTypeCsBeneficiaire = csTypeCsBeneficiaire;
        this.csGenrePcAccordee = csGenrePcAccordee;
    }

    public final String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCsGenrePcAccordee() {
        return csGenrePcAccordee;
    }

    public String getCsTypeCsBeneficiaire() {
        return csTypeCsBeneficiaire;
    }

    public String getCsTypePcAccordee() {
        return csTypePcAccordee;
    }

    public Boolean getIsConventionne() {
        return isConventionne;
    }

    public String getMontantAPayer() {
        return montantAPayer;
    }

    public String getMontantFacture44() {
        return montantFacture44;
    }

    public String getMontantVerseOAI() {
        return montantVerseOAI;
    }

    public final void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCsGenrePcAccordee(String csGenrePcAccordee) {
        this.csGenrePcAccordee = csGenrePcAccordee;
    }

    public void setCsTypeCsBeneficiaire(String csTypeCsBeneficiaire) {
        this.csTypeCsBeneficiaire = csTypeCsBeneficiaire;
    }

    public void setCsTypePcAccordee(String csTypePcAccordee) {
        this.csTypePcAccordee = csTypePcAccordee;
    }

    public void setIsConventionne(Boolean isConventionne) {
        this.isConventionne = isConventionne;
    }

    public void setMontantAPayer(String montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

    public void setMontantFacture44(String montantFacture44) {
        this.montantFacture44 = montantFacture44;
    }

    public void setMontantVerseOAI(String montantVerseOAI) {
        this.montantVerseOAI = montantVerseOAI;
    }

}
