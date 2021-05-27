package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import lombok.Getter;
import lombok.Setter;

public class APImportationAPGHistoriqueManager extends PRAbstractManager {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String forId = "";

    @Override
    public String getOrderByDefaut() {
        return APImportationAPGHistorique.FIELDNAME_ID;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new APImportationAPGHistorique();
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

            sqlWhere += APImportationAPGHistorique.FIELDNAME_ID + "="
                    + _dbWriteNumeric(statement.getTransaction(), forId);
        }

        return sqlWhere;
    }
}
