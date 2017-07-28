package globaz.osiris.db.ordres.sepa;

import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.osiris.parser.IntBVRPojo;

/**
 * Pojo m�tier servant a la cr�ation de ligne BVR dans un journal par la ComptaAux
 * 
 * Ce pojo reprend le contrat de l'ancienne mani�re de traiter les BVR (fichier plat)
 * 
 * Aliment� apr�s unmarshall et mapping d'un camt054 (info du C et D-level) lors d'un traitement BVR
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
    private String codeMonnaie;
    private String numeroAdherent;
    private String numeroReference;
    private String referenceInterne;
    private String taxeTraitement;
    private String taxeVersement;
    private String endToEndId;
    private String accountServicerReference;
    private String debtor;
    private String bankTransactionCode;
    private FWMemoryLog memoryLog;
    private BSession session = null;

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

    /**
     * @return the codeMontant
     */
    @Override
    public String getCodeMonnaie() {
        return codeMonnaie;
    }

    /**
     * @param codeMontant the codeMontant to set
     */
    public void setCodeMonnaie(String codeMonnaie) {
        this.codeMonnaie = codeMonnaie;
    }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }

    @Override
    public String getEndToEndId() {
        return endToEndId;
    }

    @Override
    public FWMemoryLog getMemoryLog() {
        if (memoryLog == null) {
            memoryLog = new FWMemoryLog();
        }
        memoryLog.setSession(getSession());
        return memoryLog;
    }

    @Override
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.05.2003 13:21:10)
     * 
     * @param newSession
     *            BSession
     */
    @Override
    public void setSession(BSession newSession) {
        session = newSession;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.05.2003 13:21:10)
     * 
     * @return BSession
     */
    @Override
    public BSession getSession() {
        return session;
    }
}
