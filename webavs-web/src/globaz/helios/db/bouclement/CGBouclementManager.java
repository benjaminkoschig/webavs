package globaz.helios.db.bouclement;

public class CGBouclementManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdMandat = "";
    private java.lang.String forIdTypeBouclement = "";

    /**
     * Getter
     */

    /**
     * Setter
     */

    /**
     * Commentaire relatif au constructeur CGBouclementManager.
     */
    public CGBouclementManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGBOUCP";
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
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }
        if (getForIdTypeBouclement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEBOUCLEMENT=" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeBouclement());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGBouclement();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 15:13:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 16:47:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeBouclement() {
        return forIdTypeBouclement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 15:13:35)
     * 
     * @param newForIdMandat
     *            java.lang.String
     */
    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 16:47:24)
     * 
     * @param newForIdTypeBouclement
     *            java.lang.String
     */
    public void setForIdTypeBouclement(java.lang.String newForIdTypeBouclement) {
        forIdTypeBouclement = newForIdTypeBouclement;
    }
}
