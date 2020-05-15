package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class APDroitPanSituationManager extends PRAbstractManager {

    private String forIdDroit = "";

    public APDroitPanSituationManager() {
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new APDroitPanSituation();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APDroitPanSituation.TABLE_NAME + "."
                    + APDroitPanSituation.FIELDNAME_ID_DROIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }
        return sqlWhere;
    }

    @Override
    public String getOrderByDefaut() {
        return APDroitPanSituation.FIELDNAME_ID_APG_PANDEMIE;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

}
