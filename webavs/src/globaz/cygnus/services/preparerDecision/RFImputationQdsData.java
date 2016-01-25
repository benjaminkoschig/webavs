/*
 * Créé le 25 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import java.math.BigDecimal;

/**
 * 
 * @author JJE
 * 
 */
public class RFImputationQdsData {

    private String anneeQd = "";
    private BigDecimal chargeRfm = new BigDecimal(0);
    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String csCodeTypeDeSoin = "";
    private String csDegreApi = "";
    private String csGenrePcAccordee = "";
    private String csTypeBeneficiaire = "";
    private String csTypePcAccordee = "";
    private String dateDebutPetiteQd = "";
    private String dateFinPetiteQd = "";
    private boolean hasSoldeExcedentModifie = false;
    private String idDossier = "";
    private String idGestionnaire = "";
    private String idPotQdAssure = "";
    private String idQd = "";
    private String idQdPrincipale = "";
    private String idTiers = "";
    private boolean isLAPRAMS = true;
    private boolean isNew = true;
    private boolean isPlafonnee = true;
    private boolean isQdAssure = false;
    private boolean isQdPrincipale = false;
    private boolean isQdVirtuelle = false;
    private boolean isRI = true;
    private String mntPlafondPot = "";
    private BigDecimal mntResiduel = new BigDecimal(0);
    private BigDecimal montantAutresQdBigDec = new BigDecimal(0);
    private BigDecimal montantCorrectionBigDec = new BigDecimal(0);
    private String remboursementConjoint = "";
    private String remboursementRequerant = "";
    private BigDecimal soldeExcedent = new BigDecimal(0);

    public String getAnneeQd() {
        return anneeQd;
    }

    public BigDecimal getChargeRfm() {
        return chargeRfm;
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCsCodeTypeDeSoin() {
        return csCodeTypeDeSoin;
    }

    public String getCsDegreApi() {
        return csDegreApi;
    }

    public String getCsGenrePcAccordee() {
        return csGenrePcAccordee;
    }

    public String getCsTypeBeneficiaire() {
        return csTypeBeneficiaire;
    }

    public String getCsTypePcAccordee() {
        return csTypePcAccordee;
    }

    public String getDateDebutPetiteQd() {
        return dateDebutPetiteQd;
    }

    public String getDateFinPetiteQd() {
        return dateFinPetiteQd;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPotQdAssure() {
        return idPotQdAssure;
    }

    public String getIdQd() {
        return idQd;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMntPlafondPot() {
        return mntPlafondPot;
    }

    public BigDecimal getMntResiduel() {
        return mntResiduel;
    }

    public BigDecimal getMontantAutresQdBigDec() {
        return montantAutresQdBigDec;
    }

    public BigDecimal getMontantCorrectionBigDec() {
        return montantCorrectionBigDec;
    }

    public String getRemboursementConjoint() {
        return remboursementConjoint;
    }

    public String getRemboursementRequerant() {
        return remboursementRequerant;
    }

    public BigDecimal getSoldeExcedent() {
        return soldeExcedent;
    }

    public boolean isHasSoldeExcedentModifie() {
        return hasSoldeExcedentModifie;
    }

    public boolean isLAPRAMS() {
        return isLAPRAMS;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isPlafonnee() {
        return isPlafonnee;
    }

    public boolean isQdAssure() {
        return isQdAssure;
    }

    public boolean isQdPrincipale() {
        return isQdPrincipale;
    }

    public boolean isQdVirtuelle() {
        return isQdVirtuelle;
    }

    public boolean isRI() {
        return isRI;
    }

    public void setAnneeQd(String anneeQd) {
        this.anneeQd = anneeQd;
    }

    public void setChargeRfm(BigDecimal chargeRfm) {
        this.chargeRfm = chargeRfm;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCsCodeTypeDeSoin(String csCodeTypeDeSoin) {
        this.csCodeTypeDeSoin = csCodeTypeDeSoin;
    }

    public void setCsDegreApi(String csDegreApi) {
        this.csDegreApi = csDegreApi;
    }

    public void setCsGenrePcAccordee(String csGenrePcAccordee) {
        this.csGenrePcAccordee = csGenrePcAccordee;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        this.csTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setCsTypePcAccordee(String csTypePcAccordee) {
        this.csTypePcAccordee = csTypePcAccordee;
    }

    public void setDateDebutPetiteQd(String dateDebutPetiteQd) {
        this.dateDebutPetiteQd = dateDebutPetiteQd;
    }

    public void setDateFinPetiteQd(String dateFinPetiteQd) {
        this.dateFinPetiteQd = dateFinPetiteQd;
    }

    public void setHasSoldeExcedentModifie(boolean hasSoldeExcedentModifie) {
        this.hasSoldeExcedentModifie = hasSoldeExcedentModifie;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPotQdAssure(String idPotQdAssure) {
        this.idPotQdAssure = idPotQdAssure;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLAPRAMS(boolean isLAPRAMS) {
        this.isLAPRAMS = isLAPRAMS;
    }

    public void setMntPlafondPot(String mntPlafondPot) {
        this.mntPlafondPot = mntPlafondPot;
    }

    public void setMntResiduel(BigDecimal mntResiduel) {
        this.mntResiduel = mntResiduel;
    }

    public void setMontantAutresQdBigDec(BigDecimal montantAutresQdBigDec) {
        this.montantAutresQdBigDec = montantAutresQdBigDec;
    }

    public void setMontantCorrectionBigDec(BigDecimal montantCorrectionBigDec) {
        this.montantCorrectionBigDec = montantCorrectionBigDec;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setPlafonnee(boolean isPlafonnee) {
        this.isPlafonnee = isPlafonnee;
    }

    public void setQdAssure(boolean isQdAssure) {
        this.isQdAssure = isQdAssure;
    }

    public void setQdPrincipale(boolean isQdPrincipale) {
        this.isQdPrincipale = isQdPrincipale;
    }

    public void setQdVirtuelle(boolean isQdVirtuelle) {
        this.isQdVirtuelle = isQdVirtuelle;
    }

    public void setRemboursementConjoint(String remboursementConjoint) {
        this.remboursementConjoint = remboursementConjoint;
    }

    public void setRemboursementRequerant(String remboursementRequerant) {
        this.remboursementRequerant = remboursementRequerant;
    }

    public void setRI(boolean isRI) {
        this.isRI = isRI;
    }

    public void setSoldeExcedent(BigDecimal soldeExcedent) {
        this.soldeExcedent = soldeExcedent;
    }

}