package globaz.phenix.db.divers;

import globaz.jade.client.util.JadeStringUtil;

public class CPTableNonActifManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdTableNonActif = "";
    private java.lang.String fromStandardRevenu = "";
    private java.lang.String order = "";
    private java.lang.String tilAnneeVigueur = "";
    private java.lang.String tilRevenu = "";
    private java.lang.String tilRevenuCi = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPTNACP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (!JadeStringUtil.isEmpty(order)) {
            return order;
        } else {
            return "JAANNE DESC, JAMREV DESC";
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
        if (getForIdTableNonActif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JAITNA=" + _dbWriteNumeric(statement.getTransaction(), getForIdTableNonActif());
        }

        // traitement du positionnement
        if (getTilAnneeVigueur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JAANNE<=" + _dbWriteNumeric(statement.getTransaction(), getTilAnneeVigueur());
        }

        // traitement du positionnement
        if (getTilRevenu().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JAMREV<=" + _dbWriteNumeric(statement.getTransaction(), getTilRevenu());
        }
        // traitement du positionnement
        if (getTilRevenuCi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JAMRCI<=" + _dbWriteNumeric(statement.getTransaction(), getTilRevenuCi());
        }
        // traitement du positionnement
        if (getFromStandardRevenu().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JAMREV>" + _dbWriteNumeric(statement.getTransaction(), getTilRevenu());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPTableNonActif();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdTableNonActif() {
        return forIdTableNonActif;
    }

    /**
     * Returns the fromStandardRevenu.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromStandardRevenu() {
        return fromStandardRevenu;
    }

    /**
     * Returns the order.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    public java.lang.String getTilAnneeVigueur() {
        return tilAnneeVigueur;
    }

    public java.lang.String getTilRevenu() {
        return tilRevenu;
    }

    public java.lang.String getTilRevenuCi() {
        return tilRevenuCi;
    }

    /**
     * Trier par ordre decroissant année de revenu. Date de création : (09.03.2004 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDescendant() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("JAANNE DESC");
        } else {
            setOrder(getOrder() + ", JAANNE DESC");
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
            setOrder("JAMREV");
        } else {
            setOrder(getOrder() + ", JAMREV");
        }
    }

    /**
     * Setter
     */
    public void setForIdTableNonActif(java.lang.String newForIdTableNonActif) {
        forIdTableNonActif = newForIdTableNonActif;
    }

    /**
     * Sets the fromStandardRevenu.
     * 
     * @param fromStandardRevenu
     *            The fromStandardRevenu to set
     */
    public void setFromStandardRevenu(java.lang.String fromStandardRevenu) {
        this.fromStandardRevenu = fromStandardRevenu;
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

    public void setTilAnneeVigueur(java.lang.String newFromAnneeVigueur) {
        tilAnneeVigueur = newFromAnneeVigueur;
    }

    public void setTilRevenu(java.lang.String newFromRevenu) {
        tilRevenu = newFromRevenu;
    }

    public void setTilRevenuCi(java.lang.String fromRevenuCi) {
        tilRevenuCi = fromRevenuCi;
    }

}
