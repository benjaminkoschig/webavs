package globaz.helios.db.comptes;

public class CGLibelleStandardListViewBean extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String beginWithIdLibelleStandard = "";
    private java.lang.String forIdLibelleStandard = "";
    private java.lang.String forIdMandat = "";

    /**
     * Getter
     */

    /**
     * Setter
     */

    /**
     * Commentaire relatif au constructeur CGOldLibelleStandardManager.
     */
    public CGLibelleStandardListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGLISTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGLISTP.IDLIBELLESTANDARD";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdMandat() != null && getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (getBeginWithIdLibelleStandard() != null && getBeginWithIdLibelleStandard().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDLIBELLESTANDARD LIKE '" + getBeginWithIdLibelleStandard() + "%'";
        }

        if (getForIdLibelleStandard() != null && getForIdLibelleStandard().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDLIBELLESTANDARD=" + _dbWriteString(statement.getTransaction(), getForIdLibelleStandard());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGLibelleStandardViewBean();
    }

    /**
     * Returns the beginWithIdLibelleStandard.
     * 
     * @return java.lang.String
     */
    public java.lang.String getBeginWithIdLibelleStandard() {
        return beginWithIdLibelleStandard;
    }

    /**
     * Returns the forIdLibelleStandard.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdLibelleStandard() {
        return forIdLibelleStandard;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 14:43:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Sets the beginWithIdLibelleStandard.
     * 
     * @param beginWithIdLibelleStandard
     *            The beginWithIdLibelleStandard to set
     */
    public void setBeginWithIdLibelleStandard(java.lang.String beginWithIdLibelleStandard) {
        this.beginWithIdLibelleStandard = beginWithIdLibelleStandard;
    }

    /**
     * Sets the forIdLibelleStandard.
     * 
     * @param forIdLibelleStandard
     *            The forIdLibelleStandard to set
     */
    public void setForIdLibelleStandard(java.lang.String forIdLibelleStandard) {
        this.forIdLibelleStandard = forIdLibelleStandard;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 14:43:00)
     * 
     * @param newForIdMandat
     *            java.lang.String
     */
    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

}
