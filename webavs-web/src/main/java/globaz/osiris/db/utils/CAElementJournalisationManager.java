package globaz.osiris.db.utils;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 09:10:44)
 * 
 * @author: Administrator
 */
public class CAElementJournalisationManager extends globaz.globall.db.BManager implements java.io.Serializable {

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

    private java.lang.String forIdPosteJournalisation = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAEJOUP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "DATE";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdPosteJournalisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPOSJOU=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPosteJournalisation());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAElementJournalisation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:57:45)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdPosteJournalisation() {
        return forIdPosteJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:57:45)
     * 
     * @param newForIdPosteJournalisation
     *            java.lang.String
     */
    public void setForIdPosteJournalisation(java.lang.String newForIdPosteJournalisation) {
        forIdPosteJournalisation = newForIdPosteJournalisation;
    }
}
