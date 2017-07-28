package globaz.osiris.api;

public interface APIRecouvrement extends APIPaiement {
    public String getDateEcheance();

    public String getGenreTransaction();

    public String getIdOrganeExecution();

    public String getReference();

    public void setDateEcheance(String newDateEcheance);

    public void setGenreTransaction(String newGenreTransaction);

    public void setIdOrganeExecution(String newIdOrganeExecution);

    public void setReference(String newReference);

    public String getBankTransactionCode();

    public String getAccountServicerReference();

    public String getDebtor();

    public void setBankTransactionCode(java.lang.String bankTransactionCode);

    public void setDebtor(java.lang.String debtor);

    public void setAccountServicerReference(java.lang.String accountServicerReference);
}
