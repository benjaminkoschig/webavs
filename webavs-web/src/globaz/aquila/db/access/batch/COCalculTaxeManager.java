package globaz.aquila.db.access.batch;

import globaz.aquila.common.COBManager;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 09:58:42)
 * 
 * @author: Administrator
 */
public class COCalculTaxeManager extends COBManager implements java.io.Serializable {

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
    private java.lang.String fromIdCalculTaxe = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + COCalculTaxe.TABLE_NAME;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return COCalculTaxe.FNAME_ID_CALCUL_TAXE;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un numéro
        if (getFromIdCalculTaxe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += COCalculTaxe.FNAME_ID_CALCUL_TAXE
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdCalculTaxe());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new COCalculTaxe();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdCalculTaxe() {
        return fromIdCalculTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @param newFromIdRole
     *            java.lang.String
     */
    public void setFromIdCalculTaxe(java.lang.String newFromIdCalculTaxe) {
        fromIdCalculTaxe = newFromIdCalculTaxe;
    }
}
