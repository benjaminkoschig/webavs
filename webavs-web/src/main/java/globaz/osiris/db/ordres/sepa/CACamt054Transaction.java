package globaz.osiris.db.ordres.sepa;

import globaz.osiris.parser.IntBVRPojo;

/**
 * Pojo métier servant a la création de ligne BVR dans un journal par la ComptaAux
 * 
 * Ce pojo reprend le contrat de l'ancienne manière de traiter les BVR (fichier plat)
 * 
 * Alimenté après unmarshall et mapping d'un camt054 (info du C et D-level) lors d'un traitement BVR
 * 
 * @author cel
 * 
 */
public class CACamt054Transaction implements IntBVRPojo {

    private String codeRejet;
    private String dateDepot;
    private String dateInscription;
    private String dateTraitement;
    private String genreEcriture;
    private String genreTransaction;
    private String montant;
    private String numeroAdherent;
    private String numeroReference;
    private String referenceInterne;
    private String taxeTraitement;
    private String taxeVersement;

    private String accountServicerReference;
    private String debtor;
    private String bankTransactionCode;

    /**
     * never used by process
     * 
     * @param codeRejet
     */
    public void setCodeRejet(String codeRejet) {
        this.codeRejet = codeRejet;
    }

    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public void setGenreEcriture(String genreEcriture) {
        this.genreEcriture = genreEcriture;
    }

    public void setGenreTransaction(String genreTransaction) {
        this.genreTransaction = genreTransaction;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNumeroAdherent(String numeroAdherent) {
        this.numeroAdherent = numeroAdherent;
    }

    public void setNumeroReference(String numeroReference) {
        this.numeroReference = numeroReference;
    }

    public void setReferenceInterne(String referenceInterne) {
        this.referenceInterne = referenceInterne;
    }

    /**
     * never used by process
     * 
     * @param taxeTraitement
     */
    public void setTaxeTraitement(String taxeTraitement) {
        this.taxeTraitement = taxeTraitement;
    }

    /**
     * never used by process
     * 
     * @param taxeVersement
     */
    public void setTaxeVersement(String taxeVersement) {
        this.taxeVersement = taxeVersement;
    }

    @Override
    public String getCodeRejet() {
        return codeRejet;
    }

    @Override
    public String getDateDepot() {
        return dateDepot;
    }

    @Override
    public String getDateInscription() {
        return dateInscription;
    }

    @Override
    public String getDateTraitement() {
        return dateTraitement;
    }

    @Override
    public String getGenreEcriture() {
        return genreEcriture;
    }

    @Override
    public String getGenreTransaction() {
        return genreTransaction;
    }

    @Override
    public String getMontant() {
        return montant;
    }

    @Override
    public String getNumeroAdherent() {
        return numeroAdherent;
    }

    @Override
    public String getNumeroReference() {
        return numeroReference;
    }

    @Override
    public String getReferenceInterne() {
        return referenceInterne;
    }

    @Override
    public String getTaxeTraitement() {
        return taxeTraitement;
    }

    @Override
    public String getTaxeVersement() {
        return taxeVersement;
    }

    @Override
    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }

    @Override
    public String getAccountServicerReference() {
        return accountServicerReference;
    }

    public void setAccountServicerReference(String accountServicerReference) {
        this.accountServicerReference = accountServicerReference;
    }

    @Override
    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

}
