package globaz.musca.db.gestionJourFerie;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author MMO
 * @since 3 aout. 2010
 */
public class FAGestionJourFerieDomaineManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String AND = " AND ";
    public static final String FROM = " FROM ";
    /**
     * Constantes
     */
    public static final String SELECT = " SELECT ";
    public static final String WHERE = " WHERE ";

    /**
     * Attributs
     */
    private String forIdFerie = "";

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append(FAGestionJourFerieDomaine.F_ID_FERCOS);
        sqlFields.append(" , ");
        sqlFields.append(FAGestionJourFerieDomaine.F_ID_FERIE);
        sqlFields.append(" , ");
        sqlFields.append(FAGestionJourFerieDomaine.F_ID_DOMAINE);

        return sqlFields.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + FAGestionJourFerieDomaine.TABLE_NAME);

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return FAGestionJourFerieDomaine.F_ID_FERCOS;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isBlank(getForIdFerie())) {
            sqlAddCondition(
                    sqlWhere,
                    FAGestionJourFerieDomaine.F_ID_FERIE + " = "
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdFerie()));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FAGestionJourFerieDomaine();
    }

    /**
     * getter
     */
    public String getForIdFerie() {
        return forIdFerie;
    }

    /**
     * setter
     */
    public void setForIdFerie(String newForIdFerie) {
        forIdFerie = newForIdFerie;
    }

    private void sqlAddCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(FAGestionJourFerieDomaineManager.AND);
        }
        sqlWhere.append(condition);
    }

}
