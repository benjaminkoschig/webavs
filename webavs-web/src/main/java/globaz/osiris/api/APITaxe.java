package globaz.osiris.api;

/**
 * Insérez la description du type ici. Date de création : (25.09.2002 16:14:51)
 * 
 * @author: Administrator
 */
public interface APITaxe {
    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantBase();

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:47:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantTaxe();

    public globaz.framework.util.FWCurrency getMontantTaxeToCurrency();

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTaux();

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:24)
     * 
     * @param newMontantBase
     *            java.lang.String
     */
    public void setMontantBase(java.lang.String newMontantBase);

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:47:10)
     * 
     * @param newMontantTaxe
     *            java.lang.String
     */
    public void setMontantTaxe(java.lang.String newMontantTaxe);

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:43)
     * 
     * @param newTaux
     *            java.lang.String
     */
    public void setTaux(java.lang.String newTaux);
}
