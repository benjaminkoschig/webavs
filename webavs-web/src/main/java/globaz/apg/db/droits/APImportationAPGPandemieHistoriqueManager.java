package globaz.apg.db.droits;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class APImportationAPGPandemieHistoriqueManager extends PRAbstractManager {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String forId = "";

    @Override
    public String getOrderByDefaut() {
        return APImportationAPGPandemieHistorique.FIELDNAME_ID;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new APImportationAPGPandemieHistorique();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forId)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APImportationAPGPandemieHistorique.FIELDNAME_ID + "="
                    + _dbWriteNumeric(statement.getTransaction(), forId);
        }

        return sqlWhere;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }
}
