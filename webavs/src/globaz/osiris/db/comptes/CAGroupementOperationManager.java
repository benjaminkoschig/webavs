package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 15:45:11)
 * 
 * @author: Administrator
 */
public class CAGroupementOperationManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * Getter
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Setter
     */

    private java.lang.String forIdGroupement = new String();
    private java.lang.String forIdOperation = new String();
    private String forTypeGroupement = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = _getCollection() + "CAGROPP";
        // Si type de groupement
        if (getForTypeGroupement().length() != 0) {
            from = from + " INNER JOIN " + _getCollection() + "CAGROUP ON " + _getCollection()
                    + "CAGROPP.IDGROUPEMENT = " + _getCollection() + "CAGROUP.IDGROUPEMENT";
        }
        return from;
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

        // traitement du positionnement selon l'identifiant du groupe
        if (getForIdGroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDGROUPEMENT=" + this._dbWriteNumeric(statement.getTransaction(), getForIdGroupement());
        }

        // traitement du positionnement selon l'identifiant de l'operation
        if (getForIdOperation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDOPERATION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdOperation());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAGroupementOperation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 13:22:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdGroupement() {
        return forIdGroupement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 14:51:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdOperation() {
        return forIdOperation;
    }

    /**
     * Returns the forTypeGroupement.
     * 
     * @return String
     */
    public String getForTypeGroupement() {
        return forTypeGroupement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 13:22:58)
     * 
     * @param newForIdGroupement
     *            java.lang.String
     */
    public void setForIdGroupement(java.lang.String newForIdGroupement) {
        forIdGroupement = newForIdGroupement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 14:51:46)
     * 
     * @param newForIdOperation
     *            java.lang.String
     */
    public void setForIdOperation(java.lang.String newForIdOperation) {
        forIdOperation = newForIdOperation;
    }

    /**
     * Sets the forTypeGroupement.
     * 
     * @param forTypeGroupement
     *            The forTypeGroupement to set
     */
    public void setForTypeGroupement(String forTypeGroupement) {
        this.forTypeGroupement = forTypeGroupement;
    }

}
