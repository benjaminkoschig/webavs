package globaz.helios.db.modeles;

import globaz.jade.client.util.JadeStringUtil;

public class CGEnteteModeleEcritureManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdMandat = "";
    private java.lang.String forIdModeleEcriture = "";
    private java.lang.String forLibelle = "";

    private String orderBy = "";

    /**
     * Commentaire relatif au constructeur CGModeleEcritureManager.
     */
    public CGEnteteModeleEcritureManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGEMODP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGEMODP.IDENTETEMODECRIT ";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdMandat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (!JadeStringUtil.isBlank(getForLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLEFR like '%" + getForLibelle() + "%'";
        }

        if (!JadeStringUtil.isBlank(getForIdModeleEcriture())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMODELEECRITURE=" + _dbWriteString(statement.getTransaction(), getForIdModeleEcriture());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGEnteteModeleEcriture();
    }

    /**
     * Returns the forIdMandat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdModeleEcriture.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdModeleEcriture() {
        return forIdModeleEcriture;
    }

    /**
     * Returns the forLibelle.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForLibelle() {
        return forLibelle;
    }

    /**
     * Returns the orderBy.
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the forIdMandat.
     * 
     * @param forIdMandat
     *            The forIdMandat to set
     */
    public void setForIdMandat(java.lang.String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

    /**
     * Sets the forIdModeleEcriture.
     * 
     * @param forIdModeleEcriture
     *            The forIdModeleEcriture to set
     */
    public void setForIdModeleEcriture(java.lang.String forIdModeleEcriture) {
        this.forIdModeleEcriture = forIdModeleEcriture;
    }

    /**
     * Sets the forLibelle.
     * 
     * @param forLibelle
     *            The forLibelle to set
     */
    public void setForLibelle(java.lang.String forLibelle) {
        this.forLibelle = forLibelle;
    }

    public void setOrderby(java.lang.String newOrderby) {
        orderBy = newOrderby;
    }

    /**
     * Sets the orderBy.
     * 
     * @param orderBy
     *            The orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
