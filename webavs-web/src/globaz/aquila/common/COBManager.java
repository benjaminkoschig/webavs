package globaz.aquila.common;

import globaz.globall.db.BManager;

/**
 * Classe abstraite parente de tous les containers du projet aquila
 * 
 * @see globaz.globall.db.BManager
 * @author Pascal Lovy, 07-oct-2004
 */
public abstract class COBManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final String AND = " AND ";
    protected static final String AS_FIELD = " AS ";
    protected static final String BETWEEN = " BETWEEN ";
    protected static final String COUNT = "COUNT(*)";
    protected static final String DECIMAL = " DECIMAL ";
    protected static final String DIFFERENT = "<>";
    protected static final String EGAL = "=";
    protected static final String FROM = " FROM ";
    protected static final String GREATER_DB_OPERAND = " > ";

    protected static final String GROUP_BY = " GROUP BY ";
    protected static final String IN = " IN ";
    protected static final String INNER_JOIN = " INNER JOIN ";
    protected static final String IS_NOT_NULL = " IS NOT NULL ";
    protected static final String IS_NULL = " IS NULL ";
    protected static final String LEFT_JOIN = " LEFT JOIN ";
    protected static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";
    protected static final String LIKE = " LIKE ";
    protected static final String NOT_IN = " NOT IN ";

    protected static final String ON = " ON ";
    protected static final String OR = " OR ";
    protected static final String ORDER_BY = " ORDER BY ";
    protected static final String PLUS_GRAND_EGAL = ">=";
    protected static final String PLUS_PETIT_EGAL = "<=";
    protected static final String SELECT = "SELECT ";
    protected static final String SMALLER_DB_OPERAND = " < ";
    protected static final String SUBSTRING = " SUBSTR ";
    protected static final String UNION = " UNION ";
    protected static final String UNION_ALL = " UNION ALL ";

    protected static final String WHERE = " WHERE ";
    protected static final String ZERO = "0";

    /**
     * Permet l'ajout d'une condition dans la clause WHERE. <br>
     * 
     * @param sqlWhere
     * @param condition
     *            à ajouter au where
     */
    protected void addCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(COBManager.AND);
        }
        sqlWhere.append(condition);
    }

    /**
     * Crée l'entity associé au manager. Utilisé par le testfrmk.
     * 
     * @return COBEntity
     * @throws java.lang.Exception
     */
    public COBEntity createNewEntity() throws java.lang.Exception {
        return (COBEntity) _newEntity();

    }
}
