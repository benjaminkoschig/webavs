package globaz.tucana.db.statistique.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed Jun 28 11:01:20 CEST 2006
 */
public interface ITUTemporaireDefTable {
    /** agence - libell� de l'agence (STMPAG) */
    public final String AGENCE = "STMPAG";

    /** annee - ann�e de la statistique (STMPAN) */
    public final String ANNEE = "STMPAN";

    /** cantonCourt - code ofs du canton (STMPCC) */
    public final String CANTON_COURT = "STMPCC";

    /** cantonLong - libell� long du canton (STMPCL) */
    public final String CANTON_LONG = "STMPCL";

    /** categorie - libell� de la cat�gorie (STMPCT) */
    public final String CATEGORIE = "STMPCT";

    /** dateCreation - date de cr�ation (STMPDC) */
    public final String DATE_CREATION = "STMPDC";

    /** groupe - libell� du groupe cat�gorie (STMPGR) */
    public final String GROUPE = "STMPGR";

    /** idTemporaire - cl� primaire du fichier tusptmp (STMPID) */
    public final String ID_TEMPORAIRE = "STMPID";

    /** mois - mois de la statistique (STMPMO) */
    public final String MOIS_ALPHA = "STMPMA";

    /** mois - mois de la statistique (STMPMO) */
    public final String MOIS_NUME = "STMPMO";

    /** Table : TUSPTMP */
    public final String TABLE_NAME = "TUSPTMP";

    /** total - montantnombre total (STMPTO) */
    public final String TOTAL = "STMPTO";
}
