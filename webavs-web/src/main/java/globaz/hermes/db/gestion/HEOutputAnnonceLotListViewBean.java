package globaz.hermes.db.gestion;

import globaz.globall.db.BStatement;
import globaz.jade.common.Jade;

public class HEOutputAnnonceLotListViewBean extends HEOutputAnnonceListViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forTypeLot = "";
    private boolean addConditionToReceiveCentrale = false;
    private String schema;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.db.gestion.HEAnnoncesListViewBean#_getFrom(globaz.globall .db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);
        from += (" INNER JOIN " + _getCollection() + "HELOTSP AS HELOTSP ON HELOTSP.RMILOT=HEANNOP.RMILOT");
        return from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hermes.db.gestion.HEOutputAnnonceListViewBean#_getWhere(globaz .globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (forTypeLot.length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RMTTYP=" + _dbWriteNumeric(statement.getTransaction(), forTypeLot);


            // Modification pour les reception centrale. La requête n'érait pas bonne. On ajout notre correction seulement sur on le demande via le boolean
            if (addConditionToReceiveCentrale) {
                StringBuilder whereClauseForCentrale = new StringBuilder();
                whereClauseForCentrale.append(" AND HEANNOP.RMILOT = (SELECT HEA.RMILOT ");
                whereClauseForCentrale.append("FROM "+getSchema()+".HEANNOP HEA ");
                whereClauseForCentrale.append("INNER JOIN "+getSchema()+".HELOTSP AS HELO ON HELO.RMILOT = HEA.RMILOT ");
                whereClauseForCentrale.append("WHERE SUBSTR(HEA.RNLENR, 1, 2) = '"+getForCodeApplication()+"' ");
                whereClauseForCentrale.append("AND HEA.RNTSTA = "+ _dbWriteNumeric(statement.getTransaction(), getForStatut()));
                whereClauseForCentrale.append(" AND (HEA.RNDECP = 0 OR HEA.RNDECP IS NULL) ");
                whereClauseForCentrale.append("AND HELO.RMTTYP = "+ forTypeLot);
                whereClauseForCentrale.append(" ORDER BY HEA.RNDDAN DESC ");
                whereClauseForCentrale.append("FETCH FIRST ROW ONLY) ");
                sqlWhere += whereClauseForCentrale.toString();
            }
        }

        return sqlWhere;
    }

    /**
     * @return Returns the forTypeLot.
     */
    public String getForTypeLot() {
        return forTypeLot;
    }

    /**
     * @param forTypeLot
     *            The forTypeLot to set.
     */
    public void setForTypeLot(String forTypeLot) {
        this.forTypeLot = forTypeLot;
    }

    public boolean isAddConditionToReceiveCentrale() {
        return addConditionToReceiveCentrale;
    }

    public void setAddConditionToReceiveCentrale(boolean addConditionToReceiveCentrale) {
        this.addConditionToReceiveCentrale = addConditionToReceiveCentrale;
    }

    private String getSchema() {

        if (schema == null) {
            schema = Jade.getInstance().getDefaultJdbcSchema();
        }
        return schema;
    }
}
