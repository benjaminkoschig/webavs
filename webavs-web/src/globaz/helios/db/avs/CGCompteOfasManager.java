package globaz.helios.db.avs;

public class CGCompteOfasManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String beginWithIdExterne = "";
    private java.lang.String forIdExterne = new String();
    private java.lang.String forIdMandat = new String();
    private java.lang.Boolean forReleveAVSCompteAdministration = null;
    private java.lang.Boolean forReleveAVSCompteExploitation = null;

    /**
     * Commentaire relatif au constructeur CGCompteOfasManager.
     */
    public CGCompteOfasManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGOFCPP";
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
        if (getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGOFCPP.IDMANDAT="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        // traitement du positionnement
        if (getForIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGOFCPP.IDEXTERNE="
                    + _dbWriteString(statement.getTransaction(), getForIdExterne());
        }
        // traitement du positionnement pour IdExterne
        else if (getBeginWithIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGOFCPP.IDEXTERNE LIKE '" + getBeginWithIdExterne() + "%'";
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGCompteOfas();
    }

    /**
     * Returns the beginWithIdExterne.
     * 
     * @return java.lang.String
     */
    public java.lang.String getBeginWithIdExterne() {
        return beginWithIdExterne;
    }

    public java.lang.String getForIdExterne() {
        return forIdExterne;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 18:39:09)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForReleveAVSCompteAdministration() {
        return forReleveAVSCompteAdministration;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 10:33:43)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForReleveAVSCompteExploitation() {
        return forReleveAVSCompteExploitation;
    }

    /**
     * Sets the beginWithIdExterne.
     * 
     * @param beginWithIdExterne
     *            The beginWithIdExterne to set
     */
    public void setBeginWithIdExterne(java.lang.String beginWithIdExterne) {
        this.beginWithIdExterne = beginWithIdExterne;
    }

    public void setForIdExterne(java.lang.String newForIdExterne) {
        forIdExterne = newForIdExterne;
    }

    /**
     * Setter
     */
    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 18:39:09)
     * 
     * @param newForReleveAVS
     *            java.lang.Boolean
     */
    public void wantForReleveAVSCompteAdministration(java.lang.Boolean newForReleveAVS) {
        forReleveAVSCompteAdministration = newForReleveAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 10:33:43)
     * 
     * @param newForReleveAVSCompteExploitation
     *            java.lang.Boolean
     */
    public void wantForReleveAVSCompteExploitation(java.lang.Boolean newForReleveAVSCompteExploitation) {
        forReleveAVSCompteExploitation = newForReleveAVSCompteExploitation;
    }

}
