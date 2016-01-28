package globaz.phenix.db.divers;

import globaz.jade.client.util.JadeStringUtil;

public class CPTableAFIManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String forIdTableAfi = "";

    private java.lang.String fromAnneeAfi = "";

    private java.lang.String fromRevenuAfi = "";

    private java.lang.String order = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPTAFIP";
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
        if (getForIdTableAfi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JBITIN=" + _dbWriteNumeric(statement.getTransaction(), getForIdTableAfi());
        }

        // traitement du positionnement
        if (getFromAnneeAfi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JBANNE<=" + _dbWriteNumeric(statement.getTransaction(), getFromAnneeAfi());
        }

        // traitement du positionnement
        if (getFromRevenuAfi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JBMREV<=" + _dbWriteNumeric(statement.getTransaction(), getFromRevenuAfi());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPTableAFI();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdTableAfi() {
        return forIdTableAfi;
    }

    public java.lang.String getFromAnneeAfi() {
        return fromAnneeAfi;
    }

    public java.lang.String getFromRevenuAfi() {
        return fromRevenuAfi;
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
    public void setForIdTableAfi(java.lang.String newForIdTableInd) {
        forIdTableAfi = newForIdTableInd;
    }

    public void setFromAnneeAfi(java.lang.String newFromAnneeInd) {
        fromAnneeAfi = newFromAnneeInd;
    }

    public void setFromRevenuAfi(java.lang.String newFromRevenuInd) {
        fromRevenuAfi = newFromRevenuInd;
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
