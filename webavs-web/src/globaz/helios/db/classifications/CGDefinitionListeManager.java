package globaz.helios.db.classifications;

public class CGDefinitionListeManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdClassification = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
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

        // traitement du positionnement
        if (getForIdClassification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCLASSIFICATION=" + _dbWriteNumeric(statement.getTransaction(), getForIdClassification());
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
     * Getter
     */
    public java.lang.String getForIdClassification() {
        return forIdClassification;
    }

    /**
     * Setter
     */
    public void setForIdClassification(java.lang.String newForIdClassification) {
        forIdClassification = newForIdClassification;
    }
}
