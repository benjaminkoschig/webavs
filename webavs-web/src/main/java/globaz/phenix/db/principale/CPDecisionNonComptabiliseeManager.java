package globaz.phenix.db.principale;

import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;

public class CPDecisionNonComptabiliseeManager extends CPDecisionAffiliationTiersManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
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
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        int anneeEnCours = 0;
        try {
            anneeEnCours = JACalendar.getYear(JACalendar.today().toString());
        } catch (Exception e) {
            anneeEnCours = 0;
        }
        sqlWhere += "IAANNE <=" + anneeEnCours + " AND IATETA=" + CPDecision.CS_PB_COMPTABILISATION;
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionNonComptabilisee();
    }

    /**
     * @return
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    /**
     * @param string
     */
    public void setOrderBy(java.lang.String string) {
        orderBy = string;
    }

}
