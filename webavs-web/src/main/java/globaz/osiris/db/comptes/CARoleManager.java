package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 10:12:02)
 * 
 * @author: Administrator
 */
public class CARoleManager extends globaz.globall.db.BManager implements java.io.Serializable {

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

    private java.lang.String fromIdRole = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CARROLP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "IDROLE";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un numéro
        if (getFromIdRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDROLE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdRole());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CARole();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdRole() {
        return fromIdRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @param newFromIdRole
     *            java.lang.String
     */
    public void setFromIdRole(java.lang.String newFromIdRole) {
        fromIdRole = newFromIdRole;
    }
}
