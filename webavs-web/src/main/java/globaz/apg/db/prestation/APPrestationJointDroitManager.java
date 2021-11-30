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
    private String forEtatDroit;

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

        if (!getForEtatDroit().isEmpty()) {
            if (sql.length() != 0) {
                sql += " AND ";
            }

            sql += _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + "." + APDroitLAPG.FIELDNAME_ETAT
                    + " = " + getForEtatDroit();
        }

        return sql;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestation();
    }

    public String getForEtatDroit() {
        return forEtatDroit;
    }

    public void setForEtatDroit(String forEtatDroit) {
        this.forEtatDroit = forEtatDroit;
    }
}
