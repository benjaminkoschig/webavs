package globaz.osiris.api;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (25.09.2002 16:14:51)
 * 
 * @author: Administrator
 */
public interface APITaxe {
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:46:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantBase();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:47:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantTaxe();

    public globaz.framework.util.FWCurrency getMontantTaxeToCurrency();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:46:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTaux();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:46:24)
     * 
     * @param newMontantBase
     *            java.lang.String
     */
    public void setMontantBase(java.lang.String newMontantBase);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:47:10)
     * 
     * @param newMontantTaxe
     *            java.lang.String
     */
    public void setMontantTaxe(java.lang.String newMontantTaxe);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:46:43)
     * 
     * @param newTaux
     *            java.lang.String
     */
    public void setTaux(java.lang.String newTaux);
}
