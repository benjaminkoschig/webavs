/*
 * Créé le 4 sept. 07
 */
package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * Utilise pour la generation de la liste des annonces
 * 
 * @author BSC
 * 
 */
public class REAnnoncesGroupePrestationsManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // pour le regroupement par groupe de prestations
    private static final String FIELDNAME_GROUPE_PRESTATION = " GRPRST ";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";

    public static final String GROUPE_API_AI = "6";

    private static final String GROUPE_API_AI_IN_CODE = " IN ('81', '82', '83', '84', '88', '91', '92', '93') ";
    public static final String GROUPE_API_AVS = "5";
    private static final String GROUPE_API_AVS_IN_CODE = " IN ('85', '86', '87', '89', '94', '95', '96', '97') ";
    public static final String GROUPE_RE_AI = "4";
    private static final String GROUPE_RE_AI_IN_CODE = " IN ('70','72', '73', '74', '75', '76') ";
    public static final String GROUPE_RE_AVS = "2";

    private static final String GROUPE_RE_AVS_IN_CODE = " IN ('20', '23', '24', '25', '26', '45') ";
    public static final String GROUPE_RO_AI = "3";
    private static final String GROUPE_RO_AI_IN_CODE = " IN ('50', '53', '54', '55', '56') ";
    public static final String GROUPE_RO_AVS = "1";
    private static final String GROUPE_RO_AVS_IN_CODE = " IN ('10', '13', '14', '15', '16', '33', '34', '35', '36') ";
    private static final String REAAL1A_FIELDS = " YXITIE, YXDDEB, YXDFIN, YXLGEN, YXMMEN, ZAACOA, YXLCOM";

    // pour l'order by par nom et prenom
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean forAnnoncesSubsquentes = false;
    private String forMoisRapport = "";
    private boolean wantGroupePrestation = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(final BStatement statement) {
        if (wantGroupePrestation) {
            StringBuffer sqlFields = new StringBuffer();

            // les champs de la tables des rentes accordees
            sqlFields.append(REAnnoncesGroupePrestationsManager.REAAL1A_FIELDS);

            // le champs GROUPE_PRESTATION
            sqlFields.append(", CASE ");
            sqlFields.append(" WHEN " + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION
                    + REAnnoncesGroupePrestationsManager.GROUPE_RO_AVS_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REAnnoncesGroupePrestationsManager.GROUPE_RO_AVS);
            sqlFields.append(" WHEN " + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION
                    + REAnnoncesGroupePrestationsManager.GROUPE_RE_AVS_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REAnnoncesGroupePrestationsManager.GROUPE_RE_AVS);
            sqlFields.append(" WHEN " + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION
                    + REAnnoncesGroupePrestationsManager.GROUPE_RO_AI_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REAnnoncesGroupePrestationsManager.GROUPE_RO_AI);
            sqlFields.append(" WHEN " + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION
                    + REAnnoncesGroupePrestationsManager.GROUPE_RE_AI_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REAnnoncesGroupePrestationsManager.GROUPE_RE_AI);
            sqlFields.append(" WHEN " + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION
                    + REAnnoncesGroupePrestationsManager.GROUPE_API_AVS_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REAnnoncesGroupePrestationsManager.GROUPE_API_AVS);
            sqlFields.append(" WHEN " + REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION
                    + REAnnoncesGroupePrestationsManager.GROUPE_API_AI_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REAnnoncesGroupePrestationsManager.GROUPE_API_AI);

            sqlFields.append(" END AS " + REAnnoncesGroupePrestationsManager.FIELDNAME_GROUPE_PRESTATION);

            return sqlFields.toString();
        } else {
            return super._getFields(statement);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(final BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);

        // jointure entre la table des headers et la table de niveau A1
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnoncesAbstractLevel1A.TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_1A);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(REAnnonceHeader.FIELDNAME_ID_ANNONCE);

        // jointure entre table des rentes accordees et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnoncesGroupePrestationsManager.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(REAnnoncesAbstractLevel1A.FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REAnnoncesGroupePrestationsManager.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REAnnoncesGroupePrestationsManager.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(final BStatement statement) {

        String where = "";

        if (!JadeStringUtil.isIntegerEmpty(forMoisRapport)) {
            if (where.length() != 0) {
                where += " AND ";
            }
            where += REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT
                    + "="
                    + this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisRapport));
        }

        if (forAnnoncesSubsquentes) {
            if (where.length() != 0) {
                where += " AND ";
            }
            where += REAnnoncesAbstractLevel1A.FIELDNAME_CODE_MUTATION + " IN ('77', '78')";
        } else {
            if (where.length() != 0) {
                where += " AND ";
            }
            where += REAnnoncesAbstractLevel1A.FIELDNAME_CODE_MUTATION + " NOT IN ('77', '78')";
        }

        return where;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnoncesGroupePrestations();
    }

    /**
     * @return
     */
    public String getForMoisRapport() {
        return forMoisRapport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        if (wantGroupePrestation) {
            return REAnnoncesGroupePrestationsManager.FIELDNAME_GROUPE_PRESTATION + ", "
                    + REAnnoncesGroupePrestationsManager.FIELDNAME_NOM + ", "
                    + REAnnoncesGroupePrestationsManager.FIELDNAME_PRENOM;
        } else {
            return REAnnoncesGroupePrestationsManager.FIELDNAME_NOM + ", "
                    + REAnnoncesGroupePrestationsManager.FIELDNAME_PRENOM;
        }
    }

    public boolean isForAnnoncesSubsquentes() {
        return forAnnoncesSubsquentes;
    }

    public void setForAnnoncesSubsquentes(final boolean forAnnoncesSubsquentes) {
        this.forAnnoncesSubsquentes = forAnnoncesSubsquentes;
    }

    /**
     * @param string
     */
    public void setForMoisRapport(final String string) {
        forMoisRapport = string;
    }

    /**
     * @param b
     */
    public void setWantGroupePrestation(final boolean b) {
        wantGroupePrestation = b;
    }

    /**
     * @return
     */
    public boolean wantGroupePrestation() {
        return wantGroupePrestation;
    }

}
