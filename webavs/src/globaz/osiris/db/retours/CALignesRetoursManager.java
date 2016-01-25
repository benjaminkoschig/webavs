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

public class CALignesRetoursManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsType = null;
    private String forIdRetour = null;
    private String orderBy = CALignesRetours.FIELDNAME_ID_RETOUR + " DESC";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdRetour())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CALignesRetours.FIELDNAME_ID_RETOUR + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdRetour());
        }
        if (!JadeStringUtil.isBlank(getForCsType())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CALignesRetours.FIELDNAME_TYPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForCsType());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CALignesRetours();
    }

    public String getForCsType() {
        return forCsType;
    }

    public String getForIdRetour() {
        return forIdRetour;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

    public void setForIdRetour(String forIdRetour) {
        this.forIdRetour = forIdRetour;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
