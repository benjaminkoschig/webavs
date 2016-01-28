package globaz.helios.db.classifications;

public class CGLiaisonDefinitionListe_MandatManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdClassification = "";
    private java.lang.String forIdMandat = "";

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGDFLIP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGDFLIP.IDCLASSIFICATION IN (SELECT IDCLASSIFICATION FROM "
                    + _getCollection() + "CGCLASP WHERE IDMANDAT = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat()) + ")";
        }
        if (getForIdClassification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGCLASP.IDCLASSIFICATION="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdClassification());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGDefinitionListe();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 11:30:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdClassification() {
        return forIdClassification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 11:25:16)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 11:30:35)
     * 
     * @param newForIdClassification
     *            java.lang.String
     */
    public void setForIdClassification(java.lang.String newForIdClassification) {
        forIdClassification = newForIdClassification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 11:25:16)
     * 
     * @param newForIdMandat
     *            java.lang.String
     */
    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }
}
