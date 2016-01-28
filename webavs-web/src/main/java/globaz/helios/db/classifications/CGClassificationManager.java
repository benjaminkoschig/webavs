package globaz.helios.db.classifications;

public class CGClassificationManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdMandat = new String();
    private java.lang.String forIdTypeClassification = new String();

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 09:05:03)
     */
    public CGClassificationManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGCLASP";
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
        if (getForIdMandat() != null && getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        // traitement du positionnement
        if (getForIdTypeClassification() != null && getForIdTypeClassification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPECLASSIFICA=" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeClassification());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGClassification();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdTypeClassification.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeClassification() {
        return forIdTypeClassification;
    }

    /**
     * Setter
     */
    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Sets the forIdTypeClassification.
     * 
     * @param forIdTypeClassification
     *            The forIdTypeClassification to set
     */
    public void setForIdTypeClassification(java.lang.String forIdTypeClassification) {
        this.forIdTypeClassification = forIdTypeClassification;
    }

}
