package globaz.phenix.db.principale;

import globaz.globall.db.BStatement;

public class CPDecisionAffiliationPassageManager extends CPDecisionAffiliationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forInStatus = "";
    private String forNotInStatus = "";
    private String orderBy = "";

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement);
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "FAPASSP";
        return from + " INNER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".IDPASSAGE="
                + _getCollection() + "CPDECIP.EBIPAS)";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrderBy();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // appel du parent
        String sqlWhere = super._getWhere(statement);
        // traitement du positionnement
        if (getForNotInStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.STATUS NOT IN (" + getForNotInStatus() + ")";
        }
        if (getForInStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.STATUS IN (" + getForInStatus() + ")";
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionNonComptabilisee();
    }

    public String getForInStatus() {
        return forInStatus;
    }

    public String getForNotInStatus() {
        return forNotInStatus;
    }

    /**
     * @return
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    public void setForInStatus(String forInStatus) {
        this.forInStatus = forInStatus;
    }

    public void setForNotInStatus(String forNotInStatus) {
        this.forNotInStatus = forNotInStatus;
    }

    /**
     * @param string
     */
    public void setOrderBy(java.lang.String string) {
        orderBy = string;
    }

}
