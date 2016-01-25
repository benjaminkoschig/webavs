package globaz.phenix.db.divers;

import globaz.jade.client.util.JadeStringUtil;

public class CPTableIndependantManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String forIdTableInd = "";

    private java.lang.String fromAnneeInd = "";

    private java.lang.String fromRevenuInd = "";

    private java.lang.String order = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPTINDP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (!JadeStringUtil.isEmpty(order)) {
            return order;
        } else {
            return "JBANNE DESC, JBMREV DESC";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdTableInd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JBITIN=" + _dbWriteNumeric(statement.getTransaction(), getForIdTableInd());
        }

        // traitement du positionnement
        if (getFromAnneeInd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JBANNE<=" + _dbWriteNumeric(statement.getTransaction(), getFromAnneeInd());
        }

        // traitement du positionnement
        if (getFromRevenuInd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JBMREV<=" + _dbWriteNumeric(statement.getTransaction(), getFromRevenuInd());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPTableIndependant();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdTableInd() {
        return forIdTableInd;
    }

    public java.lang.String getFromAnneeInd() {
        return fromAnneeInd;
    }

    public java.lang.String getFromRevenuInd() {
        return fromRevenuInd;
    }

    /**
     * Returns the order.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Trier par ordre decroissant année de revenu. Date de création : (09.03.2004 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDescendant() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("JBANNE DESC");
        } else {
            setOrder(getOrder() + ", JBANNE DESC");
        }
    }

    /**
     * Trier par ordre decroissant le revenu indépendant. Date de création : (09.03.2004 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByRevenuAscendant() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("JBMREV");
        } else {
            setOrder(getOrder() + ", JBMREV");
        }
    }

    /**
     * Trier par ordre decroissant le revenu indépendant. Date de création : (09.03.2004 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByRevenuDescendant() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("JBMREV DESC");
        } else {
            setOrder(getOrder() + ", JBMREV DESC");
        }
    }

    /**
     * Setter
     */
    public void setForIdTableInd(java.lang.String newForIdTableInd) {
        forIdTableInd = newForIdTableInd;
    }

    public void setFromAnneeInd(java.lang.String newFromAnneeInd) {
        fromAnneeInd = newFromAnneeInd;
    }

    public void setFromRevenuInd(java.lang.String newFromRevenuInd) {
        fromRevenuInd = newFromRevenuInd;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(java.lang.String order) {
        this.order = order;
    }

}
