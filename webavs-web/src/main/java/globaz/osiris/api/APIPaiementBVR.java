package globaz.osiris.api;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 13:19:41)
 * 
 * @author: Administrator
 */
public interface APIPaiementBVR extends APIPaiement {
    public String getDateDepot();

    public String getDateInscription();

    public String getDateTraitement();

    public String getGenreTransaction();

    public String getIdOrganeExecution();

    public String getReferenceBVR();

    public String getReferenceInterne();

    public String getBankTransactionCode();

    public String getAccountServicerReference();

    public String getDebtor();

    public void setBankTransactionCode(java.lang.String bankTransactionCode);

    public void setDebtor(java.lang.String debtor);

    public void setAccountServicerReference(java.lang.String accountServicerReference);

    public void setDateDepot(String newDateDepot);

    public void setDateInscription(String newDateInscription);

    public void setDateTraitement(String newDateTraitement);

    public void setGenreTransaction(String newGenreTransaction);

    public void setIdOrganeExecution(String newIdOrganeExecution);

    public void setReferenceBVR(String newReferenceBVR);

    public void setReferenceInterne(String newReferenceInterne);
}
