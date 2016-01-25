package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 16:10:35)
 * 
 * @author: Administrator
 */
public class CAGroupementManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdOperationMaster = new String();
    private String forTypeGroupement = new String();

    /**
     * Getter
     */

    /**
     * Setter
     */

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAGROUP";
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

        // IdOperationMaster
        if (getForIdOperationMaster().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDOPERATIONMASTER="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOperationMaster());
        }

        // TypeGroupement
        if (getForTypeGroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TYPEGROUPEMENT=" + this._dbWriteString(statement.getTransaction(), getForTypeGroupement());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAGroupement();
    }

    /**
     * Returns the forIdOperationMaster.
     * 
     * @return String
     */
    public String getForIdOperationMaster() {
        return forIdOperationMaster;
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
     * Sets the forIdOperationMaster.
     * 
     * @param forIdOperationMaster
     *            The forIdOperationMaster to set
     */
    public void setForIdOperationMaster(String forIdOperationMaster) {
        this.forIdOperationMaster = forIdOperationMaster;
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
