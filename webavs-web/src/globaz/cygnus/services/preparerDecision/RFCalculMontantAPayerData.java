/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author JJE
 * 
 */
public class RFCalculMontantAPayerData {

    private String csDegreApi = "";
    private String csGenrePcAccordee = "";
    private String csTypeBeneficiaire = "";
    private String csTypePcAccordee = "";
    private String dateDebutPetiteQd = "";
    private String dateDeFinPetiteQd = "";
    private boolean hasSoldeExcedent = false;
    private String idConvention = "";
    private String idPotAssure = "";
    private String idQd = "";
    private Set<String[]> idStrMotifDeRefus = new HashSet<String[]>();// Tbl d'ids/montant motifs de refus systeme
    private boolean isConventionNonTrouvee = false;
    private boolean isLaprams = false;
    private boolean isPlafonnee = true;
    private boolean isQdAssure = false;
    private boolean isQdPrincipale = false;
    private boolean isRi = false;
    private String montantAccepte = "0";
    private BigDecimal montantAutresQdBigDec = new BigDecimal(0);
    private String montantResiduelInitial = "0";
    private String plafondPotAssure = "0";
    private String remboursementConjoint = "";

    private String remboursementRequerant = "";
    // public String soldeExcedent = "0";
    private String soldeExcedentInitial = "0";

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

    public String getDateDeFinPetiteQd() {
        return dateDeFinPetiteQd;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getIdPotAssure() {
        return idPotAssure;
    }

    public String getIdQd() {
        return idQd;
    }

    public Set<String[]> getIdStrMotifDeRefus() {
        return idStrMotifDeRefus;
    }

    public String getMontantAccepte() {
        return montantAccepte;
    }

    public BigDecimal getMontantAutresQdBigDec() {
        return montantAutresQdBigDec;
    }

    public String getMontantResiduelInitial() {
        return montantResiduelInitial;
    }

    public String getPlafondPotAssure() {
        return plafondPotAssure;
    }

    public String getRemboursementConjoint() {
        return remboursementConjoint;
    }

    public String getRemboursementRequerant() {
        return remboursementRequerant;
    }

    public String getSoldeExcedentInitial() {
        return soldeExcedentInitial;
    }

    public boolean isConventionNonTrouvee() {
        return isConventionNonTrouvee;
    }

    public boolean isHasSoldeExcedent() {
        return hasSoldeExcedent;
    }

    public boolean isLaprams() {
        return isLaprams;
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

    public boolean isRi() {
        return isRi;
    }

    public void setConventionNonTrouvee(boolean isConventionNonTrouvee) {
        this.isConventionNonTrouvee = isConventionNonTrouvee;
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

    public void setDateDeFinPetiteQd(String dateDeFinPetiteQd) {
        this.dateDeFinPetiteQd = dateDeFinPetiteQd;
    }

    public void setHasSoldeExcedent(boolean hasSoldeExcedent) {
        this.hasSoldeExcedent = hasSoldeExcedent;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdPotAssure(String idPotAssure) {
        this.idPotAssure = idPotAssure;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setIdStrMotifDeRefus(Set<String[]> idStrMotifDeRefus) {
        this.idStrMotifDeRefus = idStrMotifDeRefus;
    }

    public void setLaprams(boolean isLaprams) {
        this.isLaprams = isLaprams;
    }

    public void setMontantAccepte(String montantAccepte) {
        this.montantAccepte = montantAccepte;
    }

    public void setMontantAutresQdBigDec(BigDecimal montantAutresQdBigDec) {
        this.montantAutresQdBigDec = montantAutresQdBigDec;
    }

    public void setMontantResiduelInitial(String montantResiduelInitial) {
        this.montantResiduelInitial = montantResiduelInitial;
    }

    public void setPlafondPotAssure(String plafondPotAssure) {
        this.plafondPotAssure = plafondPotAssure;
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

    public void setRemboursementConjoint(String remboursementConjoint) {
        this.remboursementConjoint = remboursementConjoint;
    }

    public void setRemboursementRequerant(String remboursementRequerant) {
        this.remboursementRequerant = remboursementRequerant;
    }

    public void setRi(boolean isRi) {
        this.isRi = isRi;
    }

    public void setSoldeExcedentInitial(String soldeExcedentInitial) {
        this.soldeExcedentInitial = soldeExcedentInitial;
    }

}
