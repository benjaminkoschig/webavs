package globaz.corvus.db.recap.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:51:36 CET 2007
 */
public interface IRERecapInfoDefTable {
    /** nb d'élément additionné pour calculer le montant total */
    public final String CAS = "CAS";

    /** codeRecap - code récap (ZQNCOD) */
    public final String CODE_RECAP = "ZQNCOD";

    /** datePmt - date paiement AAAAMM (ZQDDAT) */
    public final String DATE_PMT = "ZQDDAT";

    /** idRecapInfo - id récap info (pk) (ZQIIFR) */
    public final String ID_RECAP_INFO = "ZQIIFR";

    /** idTiers - id tiers (fk) (ZQITIE) */
    public final String ID_TIERS = "ZQITIE";

    /** montant - montant (ZQMMON) */
    public final String MONTANT = "ZQMMON";

    /** tag de restauration, en cas d'eereur grave. Pour récupération manuelle. */
    public final String RESTORE_TAG = "ZQIRST";

    /** Table : REINFREC */
    public final String TABLE_NAME = "REINFREC";

    /**
     * total du montant - total du montant, chargé par le manager quand "getTotalByCode==true" (TOTAL)
     */
    public final String TOTAL_MONTANT = "TOTAL";

}
