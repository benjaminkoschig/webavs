package db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationListViewBean;
import globaz.naos.translation.CodeSystem;

public class LASuiviCaisseListViewBean extends AFSuiviCaisseAffiliationListViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFrom(BStatement statement) {
        String suiviCaisse = _getCollection() + "AFSUAFP";
        String tiersAdmin = _getCollection() + "TIADMIP";

        String getFrom = super._getFrom(statement);

        getFrom += " LEFT OUTER JOIN " + tiersAdmin + " ON (" + tiersAdmin + ".HTITIE=" + suiviCaisse + ".HTITIE)";

        return getFrom;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "( MYTGEN = " + CodeSystem.GENRE_CAISSE_AVS + " OR MYTGEN = " + CodeSystem.GENRE_CAISSE_AF + " ) ";
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LASuiviCaisseViewBean();
    }

}
