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
}
