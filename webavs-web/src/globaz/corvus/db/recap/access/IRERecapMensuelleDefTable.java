package globaz.corvus.db.recap.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:34:32 CET 2007
 */
public interface IRERecapMensuelleDefTable {
    /** csEtat - cs �tat r�cap mensuelle (ZRTETA) */
    public final String CS_ETAT = "ZRTETA";

    /** idRecap - id r�cap (cl� etrang�re) (ZRDREC) */
    public final String DATE_RAPPORT_MENSUEL = "ZRDREC";

    /** idRecapMensuelle - id r�cap mensuelle (ZRIRM) */
    public final String ID_RECAP_MENSUELLE = "ZRIRM";

    /** pspy - espion cr�ation (CSPY) */
    public final String PSPY = "CSPY";

    /** Table : RERECMEN */
    public final String TABLE_NAME = "RERECMEN";
}
