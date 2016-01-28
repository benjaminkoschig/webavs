package globaz.musca.db.gestionJourFerie;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author MMO
 * @since 3 aout. 2010
 */
public class FAGestionJourFerieManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String AND = " AND ";
    public static final String FROM = " FROM ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String ON = " ON ";
    /**
     * Constantes
     */
    public static final String SELECT = " SELECT ";
    public static final String WHERE = " WHERE ";

    /**
     * Attributs
     */
    private String forDateJour = "";
    private String fromDateJour = "";
    private String inIdJour = "";
    private String toDateJour = "";

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append(FAGestionJourFerie.F_ID_JOUR);
        sqlFields.append(" , ");
        sqlFields.append(FAGestionJourFerie.F_DATE_JOUR);
        sqlFields.append(" , ");
        sqlFields.append(FAGestionJourFerie.F_LIBELLE);

        return sqlFields.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + FAGestionJourFerie.TABLE_NAME);

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return FAGestionJourFerie.F_DATE_JOUR;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isBlank(getForDateJour())) {
            sqlAddCondition(
                    sqlWhere,
                    FAGestionJourFerie.F_DATE_JOUR + " = "
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateJour()));
        }

        if (!JadeStringUtil.isBlank(getInIdJour())) {
            sqlAddCondition(sqlWhere, FAGestionJourFerie.F_ID_JOUR + " IN(" + getInIdJour() + ") ");
        }

        if (!JadeStringUtil.isBlank(getFromDateJour())) {
            sqlAddCondition(
                    sqlWhere,
                    FAGestionJourFerie.F_DATE_JOUR + " >= "
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateJour()));
        }

        if (!JadeStringUtil.isBlank(getToDateJour())) {
            sqlAddCondition(
                    sqlWhere,
                    FAGestionJourFerie.F_DATE_JOUR + " <= "
                            + this._dbWriteDateAMJ(statement.getTransaction(), getToDateJour()));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FAGestionJourFerie();
    }

    /**
     * getter
     */

    public String getForDateJour() {
        return forDateJour;
    }

    public String getFromDateJour() {
        return fromDateJour;
    }

    public String getInIdJour() {
        return inIdJour;
    }

    public String getToDateJour() {
        return toDateJour;
    }

    /**
     * setter
     */
    public void setForDateJour(String newForDateJour) {
        forDateJour = newForDateJour;
    }

    public void setFromDateJour(String newFromDateJour) {
        fromDateJour = newFromDateJour;
    }

    public void setInIdJour(String newInIdJour) {
        inIdJour = newInIdJour;
    }

    public void setToDateJour(String newToDateJour) {
        toDateJour = newToDateJour;
    }

    protected void sqlAddCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(FAGestionJourFerieManager.AND);
        }
        sqlWhere.append(condition);
    }

}
