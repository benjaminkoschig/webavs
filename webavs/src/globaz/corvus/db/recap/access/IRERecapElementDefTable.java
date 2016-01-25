package globaz.corvus.db.recap.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:45:28 CET 2007
 */
public interface IRERecapElementDefTable {
    /** cas - nb rente (ZRCCAS) */
    public final String CAS = "ZRNCAS";

    /** codeRecap - code récap (IRERecapElementCode) (ZRLCOD) */
    public final String CODE_RECAP = "ZRLCOD";

    /** idRecapElement - id récap élément (pk) (ZSIELR) */
    public final String ID_RECAP_ELEMENT = "ZSIELR";

    /** idRecapMensuelle - id récap mensuelle (fk) (ZSIRM) */
    public final String ID_RECAP_MENSUELLE = "ZSIRM";

    /** montant - montant récap (ZRMMON) */
    public final String MONTANT = "ZRMMON";

    /** Table : REELMREC */
    public final String TABLE_NAME = "REELMREC";

}
