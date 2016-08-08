package globaz.pavo.db.inscriptions.declaration;

import globaz.framework.util.FWCurrency;

public class CIImportPucs4ResumeBean {

    private long nbrInscriptionsTraites = 0;
    private FWCurrency montantInscritionsTraites = new FWCurrency();
    private long nbrInscriptionsErreur = 0;
    private FWCurrency montantInscriptionsErreur = new FWCurrency();
    private long nbrInscriptionsSuspens = 0;
    private FWCurrency montantInscriptionsSuspens = new FWCurrency();
    private long nbrInscriptionsCI = 0;
    private FWCurrency montantInscriptionsCI = new FWCurrency();
    private long nbrInscriptionsNegatives = 0;
    private FWCurrency montantInscriptionsNegatives = new FWCurrency();
    private long nbrInscriptionsTotalControle = 0;
    private FWCurrency montantTotalControle = new FWCurrency();

    public long getNbrInscriptionsTraites() {
        return nbrInscriptionsTraites;
    }

    public void setNbrInscriptionsTraites(long nbrInscriptionsTraites) {
        this.nbrInscriptionsTraites = nbrInscriptionsTraites;
    }

    public FWCurrency getMontantInscritionsTraites() {
        return montantInscritionsTraites;
    }

    public void setMontantInscritionsTraites(FWCurrency montantInscritionsTraites) {
        this.montantInscritionsTraites = montantInscritionsTraites;
    }

    public long getNbrInscriptionsErreur() {
        return nbrInscriptionsErreur;
    }

    public void setNbrInscriptionsErreur(long nbrInscriptionsErreur) {
        this.nbrInscriptionsErreur = nbrInscriptionsErreur;
    }

    public FWCurrency getMontantInscriptionsErreur() {
        return montantInscriptionsErreur;
    }

    public void setMontantInscriptionsErreur(FWCurrency montantInscriptionsErreur) {
        this.montantInscriptionsErreur = montantInscriptionsErreur;
    }

    public long getNbrInscriptionsSuspens() {
        return nbrInscriptionsSuspens;
    }

    public void setNbrInscriptionsSuspens(long nbrInscriptionsSuspens) {
        this.nbrInscriptionsSuspens = nbrInscriptionsSuspens;
    }

    public FWCurrency getMontantInscriptionsSuspens() {
        return montantInscriptionsSuspens;
    }

    public void setMontantInscriptionsSuspens(FWCurrency montantInscriptionsSuspens) {
        this.montantInscriptionsSuspens = montantInscriptionsSuspens;
    }

    public long getNbrInscriptionsCI() {
        return nbrInscriptionsCI;
    }

    public void setNbrInscriptionsCI(long nbrInscriptionsCI) {
        this.nbrInscriptionsCI = nbrInscriptionsCI;
    }

    public FWCurrency getMontantInscriptionsCI() {
        return montantInscriptionsCI;
    }

    public void setMontantInscriptionsCI(FWCurrency montantInscriptionsCI) {
        this.montantInscriptionsCI = montantInscriptionsCI;
    }

    public long getNbrInscriptionsNegatives() {
        return nbrInscriptionsNegatives;
    }

    public void setNbrInscriptionsNegatives(long nbrInscriptionsNegatives) {
        this.nbrInscriptionsNegatives = nbrInscriptionsNegatives;
    }

    public FWCurrency getMontantInscriptionsNegatives() {
        return montantInscriptionsNegatives;
    }

    public void setMontantInscriptionsNegatives(FWCurrency montantInscriptionsNegatives) {
        this.montantInscriptionsNegatives = montantInscriptionsNegatives;
    }

    public long getNbrInscriptionsTotalControle() {
        return nbrInscriptionsTotalControle;
    }

    public void setNbrInscriptionsTotalControle(long nbrInscriptionsTotalControle) {
        this.nbrInscriptionsTotalControle = nbrInscriptionsTotalControle;
    }

    public FWCurrency getMontantTotalControle() {
        return montantTotalControle;
    }

    public void setMontantTotalControle(FWCurrency montantTotalControle) {
        this.montantTotalControle = montantTotalControle;
    }

}