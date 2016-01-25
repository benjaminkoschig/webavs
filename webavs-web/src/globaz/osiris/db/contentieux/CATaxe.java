package globaz.osiris.db.contentieux;

import globaz.osiris.api.APITaxe;

/**
 * Insérez la description du type ici. Date de création : (12.06.2002 09:25:59)
 * 
 * @author: Administrator
 */
public class CATaxe implements APITaxe {
    private CACalculTaxe calculTaxe;
    private java.lang.String montantBase = new String();
    private java.lang.String montantTaxe = new String();
    private java.lang.String taux = new String();

    /**
     * Commentaire relatif au constructeur CATaxe.
     */
    public CATaxe() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 16:23:16)
     * 
     * @return globaz.osiris.db.contentieux.CACalculTaxe
     */
    public CACalculTaxe getCalculTaxe() {
        return calculTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:24)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontantBase() {
        return montantBase;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:47:10)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontantTaxe() {
        return montantTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2002 11:07:14)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    @Override
    public globaz.framework.util.FWCurrency getMontantTaxeToCurrency() {
        return new globaz.framework.util.FWCurrency(getMontantTaxe());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:43)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTaux() {
        return taux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 16:23:16)
     * 
     * @param newCalculTaxe
     *            globaz.osiris.db.contentieux.CACalculTaxe
     */
    public void setCalculTaxe(CACalculTaxe newCalculTaxe) {
        calculTaxe = newCalculTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:24)
     * 
     * @param newMontantBase
     *            java.lang.String
     */
    @Override
    public void setMontantBase(java.lang.String newMontantBase) {
        montantBase = newMontantBase;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:47:10)
     * 
     * @param newMontantTaxe
     *            java.lang.String
     */
    @Override
    public void setMontantTaxe(java.lang.String newMontantTaxe) {
        montantTaxe = newMontantTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 09:46:43)
     * 
     * @param newTaux
     *            java.lang.String
     */
    @Override
    public void setTaux(java.lang.String newTaux) {
        taux = newTaux;
    }
}
