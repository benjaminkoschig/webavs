package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

import java.util.ArrayList;
import java.util.List;

public class APPrestationJointDroitManager extends APPrestationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> notForEtatDroit = new ArrayList();

    @Override
    protected String _getOrder(BStatement statement) {
        return super._getOrder(statement) + ", " + APPrestation.FIELDNAME_IDPRESTATIONAPG;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String tablePrestation = _getCollection() + APPrestation.TABLE_NAME;
        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;

        StringBuilder sql = new StringBuilder(tablePrestation);

        // jointure entre tables prestation et droits
        sql.append(" INNER JOIN ");
        sql.append(tableDroitLAPG);
        sql.append(" ON ");
        sql.append(tablePrestation).append(".").append(APPrestation.FIELDNAME_IDDROIT);
        sql.append("=");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sql = "";

        String superWhere = super._getWhere(statement);
        if (superWhere != null) {
            sql += superWhere;
        }

        if (!getNotForEtatDroit().isEmpty()) {
            if (sql.length() != 0) {
                sql += " AND ";
            }

            StringBuilder valuesStr = new StringBuilder();
            for (String genre : getNotForEtatDroit()) {
                if (!JadeStringUtil.isEmpty(valuesStr.toString())) {
                    valuesStr.append(",");
                }
                valuesStr.append(this._dbWriteNumeric(statement.getTransaction(), genre));
            }

            sql += _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_ETAT
                    + " NOT IN (" + valuesStr.toString() + ")";
        }

        return sql;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestation();
    }

    public List<String> getNotForEtatDroit() {
        return notForEtatDroit;
    }

    public void setNotForEtatDroit(List<String> notForEtatDroit) {
        this.notForEtatDroit = notForEtatDroit;
    }
}
