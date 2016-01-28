/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * author fha
 */
public class RFLotManager extends RELotManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forOrderBy = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idGestionnaire = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = super._getFields(statement);

        fields += ", " + RFLot.FIELDNAME_ID_GESTIONNAIRE;

        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFLot.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RELot.TABLE_NAME_LOT;
        getFrom += " ON ";
        getFrom += RELot.FIELDNAME_ID_LOT;
        getFrom += "=";
        getFrom += RFLot.FIELDNAME_ID_LOT_RFM;

        return getFrom;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
        return sqlOrder.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(getForCsType())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            if (RELotManager.FOR_CS_TYPE_LOT_IN_DECISION_MENSUEL.equals(getForCsType())) {
                whereClause.append(RELot.FIELDNAME_TYPE_LOT);
                whereClause.append(" IN (");
                whereClause.append(IRELot.CS_TYP_LOT_DECISION + ", " + IRELot.CS_TYP_LOT_MENSUEL + ") ");
            } else {
                whereClause.append(RELot.FIELDNAME_TYPE_LOT);
                whereClause.append(" IN (");
                whereClause.append(getForCsType());
                whereClause.append(")");

            }
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_ETAT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsLotOwner())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_LOT_OWNER);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsLotOwner()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getFromDateCreation())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_DATE_CREATION);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateCreation())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_DATE_CREATION);
            whereClause.append(" = ");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getIdGestionnaire())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RFLot.FIELDNAME_ID_GESTIONNAIRE);
            whereClause.append(" = ");
            whereClause.append(this._dbWriteString(statement.getTransaction(), getIdGestionnaire()));
        }

        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFLot();
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

}
