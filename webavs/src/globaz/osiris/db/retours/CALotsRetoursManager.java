package globaz.osiris.db.retours;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author acr
 */

public class CALotsRetoursManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatLot = null;
    private String forIdCompteFinancier = null;
    private String forIdLot = null;
    private String forLibelleLotLike = null;
    private String fromDateLot = null;
    private String orderBy = CALotsRetours.FIELDNAME_DATE_LOT + " DESC, " + CALotsRetours.FIELDNAME_ID_LOT + " DESC ";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CALotsRetours.TABLE_NAME_LOTS_RETOURS;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CALotsRetours.FIELDNAME_ID_LOT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        if (!JadeStringUtil.isBlank(getFromDateLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CALotsRetours.FIELDNAME_DATE_LOT + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateLot());
        }
        if (!JadeStringUtil.isBlank(getForCsEtatLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }

            if (CALotsRetours.CLE_ETAT_LOT_NON_LIQUIDE.equals(getForCsEtatLot())) {
                sqlWhere += CALotsRetours.FIELDNAME_ETAT_LOT + "<>"
                        + this._dbWriteNumeric(statement.getTransaction(), CALotsRetours.CS_ETAT_LOT_LIQUIDE);
            } else {
                sqlWhere += CALotsRetours.FIELDNAME_ETAT_LOT + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getForCsEtatLot());
            }
        }
        if (!JadeStringUtil.isBlank(getForLibelleLotLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + CALotsRetours.FIELDNAME_LIBELLE_LOT + ") like UPPER ("
                    + this._dbWriteString(statement.getTransaction(), "%" + getForLibelleLotLike() + "%") + ")";
        }
        if (!JadeStringUtil.isBlank(getForIdCompteFinancier())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CALotsRetours.FIELDNAME_ID_COMPTE_FINANCIER + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteFinancier());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CALotsRetours();
    }

    public String getForCsEtatLot() {
        return forCsEtatLot;
    }

    public String getForIdCompteFinancier() {
        return forIdCompteFinancier;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForLibelleLotLike() {
        return forLibelleLotLike;
    }

    public String getFromDateLot() {
        return fromDateLot;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setForCsEtatLot(String forCsEtatLot) {
        this.forCsEtatLot = forCsEtatLot;
    }

    public void setForIdCompteFinancier(String forIdCompteFinancier) {
        this.forIdCompteFinancier = forIdCompteFinancier;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForLibelleLotLike(String forLibelleLotLike) {
        this.forLibelleLotLike = forLibelleLotLike;
    }

    public void setFromDateLot(String fromDateLot) {
        this.fromDateLot = fromDateLot;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
