/*
 * Créé le 31 août. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRDateFormater;

/**
 * N'est utilisee que pour la generation de la liste de l'etat des rentes
 * 
 * @author BSC
 * 
 */
public class REEtatRentesManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

        return fromClauseBuffer.toString();
    }

    private String forDate = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = new String();

        fields = REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ", "
                + REPrestationsAccordees.FIELDNAME_FRACTION_RENTE + ", " + "SUM("
                + REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION + ") AS "
                + REEtatRentes.FIELDNAME_MONTANT_TOTAL_FOR_CODE + ", " + "SUM(1) AS "
                + REEtatRentes.FIELDNAME_NB_FOR_CODE;

        return fields;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        return REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ", "
                + REPrestationsAccordees.FIELDNAME_FRACTION_RENTE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT ");
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(" FROM ");
            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(sqlWhere);
            }
            String sqlGroupBy = _getGroupBy(statement);
            if ((sqlGroupBy != null) && (sqlGroupBy.trim().length() != 0)) {
                sqlBuffer.append(" GROUP BY ");
                sqlBuffer.append(sqlGroupBy);
            }
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = new String();

        if (!JadeStringUtil.isIntegerEmpty(forDate)) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + "<="
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDate) + " AND ( "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL " + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " >= "
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDate) + ")";
        }

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }

        // que les rentes accordees dans les etats "valide" ou "diminue" ou
        // "partiel"
        sqlWhere += " ( " + REPrestationsAccordees.FIELDNAME_CS_ETAT + " = " + IREPrestationAccordee.CS_ETAT_VALIDE
                + " OR " + REPrestationsAccordees.FIELDNAME_CS_ETAT + " = " + IREPrestationAccordee.CS_ETAT_DIMINUE
                + " OR " + REPrestationsAccordees.FIELDNAME_CS_ETAT + " = " + IREPrestationAccordee.CS_ETAT_PARTIEL
                + " ) ";

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REEtatRentes();
    }

    /**
     * @return
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * @param string
     */
    public void setForDate(String string) {
        forDate = string;
    }

}
