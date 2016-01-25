package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 07:32:20)
 * 
 * @author: Administrator
 */
public class CATypeSectionManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Getter
     */

    public final static java.lang.String ORDER_IDTYPESECTION = "1";
    /**
     * Setter
     */

    private java.lang.String fromIdTypeSection = new String();
    private java.lang.String orderBy = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CATSECP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        if (getOrderBy().equals(CATypeSectionManager.ORDER_IDTYPESECTION)) {
            return "IDTYPESECTION";
        } else {
            return "IDTYPESECTION";
        }

    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un numéro
        if (getFromIdTypeSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPESECTION>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdTypeSection());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CATypeSection();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2002 14:19:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdTypeSection() {
        return fromIdTypeSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 09:18:55)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2002 14:19:24)
     * 
     * @param newFromIdTypeSection
     *            java.lang.String
     */
    public void setFromIdTypeSection(java.lang.String newFromIdTypeSection) {
        fromIdTypeSection = newFromIdTypeSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 09:18:55)
     * 
     * @param newOrderBy
     *            java.lang.String
     */
    public void setOrderBy(java.lang.String newOrderBy) {
        orderBy = newOrderBy;
    }
}
