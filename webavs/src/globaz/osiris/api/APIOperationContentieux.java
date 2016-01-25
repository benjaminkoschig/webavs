package globaz.osiris.api;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (26.06.2002 13:37:37)
 * 
 * @author: Administrator
 */
public interface APIOperationContentieux extends APIOperation {
    java.lang.String VIREMENT = "210001";

    public String getDateDeclenchement();

    public String getDateExecution();

    public Boolean getEstDeclenche();

    public Boolean getEstExtourne();

    public Boolean getEstIgnoree();

    public Boolean getEstModifie();

    public String getIdAdresse();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:42:42)
     * 
     * @return java.lang.String
     */
    public String getIdParametreEtape();

    public String getIdTiers();

    public String getIdTiersOfficePoursuites();

    public String getTaxes();

    public void setEvenementContentieux(APIEvenementContentieux newEvct);
}
