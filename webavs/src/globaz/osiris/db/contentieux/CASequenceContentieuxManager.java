package globaz.osiris.db.contentieux;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 09:05:22)
 * 
 * @author: Administrator
 */
public class CASequenceContentieuxManager extends globaz.globall.db.BManager implements java.io.Serializable {

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

    private java.lang.String fromIdSequenceContentieux = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CASQCTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "IDSEQCON";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un numéro
        if (getFromIdSequenceContentieux().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSEQCON>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdSequenceContentieux());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CASequenceContentieux();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdSequenceContentieux() {
        return fromIdSequenceContentieux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @param newFromIdRole
     *            java.lang.String
     */
    public void setFromIdSequenceContentieux(java.lang.String newFromIdSequenceContentieux) {
        fromIdSequenceContentieux = newFromIdSequenceContentieux;
    }
}
