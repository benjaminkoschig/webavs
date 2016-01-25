package globaz.osiris.api.ordre;

public interface APIOrdreGroupe {

    public String getDateCreation();

    public String getDateEcheance();

    public String getMotif();

    public String getNbTransactions() throws Exception;

    public String getNumeroOG();

    public APIOrganeExecution getOrganeExecution() throws Exception;

    public String getTotal() throws Exception;

    public String getTypeOrdreGroupe();
}
