package globaz.corvus.db.recap.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:34:32 CET 2007
 */
public interface IRERecapMensuelleDefTable {
    /** csEtat - cs état récap mensuelle (ZRTETA) */
    public final String CS_ETAT = "ZRTETA";

    /** idRecap - id récap (clé etrangère) (ZRDREC) */
    public final String DATE_RAPPORT_MENSUEL = "ZRDREC";

    /** idRecapMensuelle - id récap mensuelle (ZRIRM) */
    public final String ID_RECAP_MENSUELLE = "ZRIRM";

    /** pspy - espion création (CSPY) */
    public final String PSPY = "CSPY";

    /** Table : RERECMEN */
    public final String TABLE_NAME = "RERECMEN";
}
