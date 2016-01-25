package globaz.osiris.db.contentieux;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 09:31:45)
 * 
 * @author: Administrator
 */
public class CATrancheTaxeManager extends globaz.globall.db.BManager implements java.io.Serializable {

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

    private java.lang.String forIdCalculTaxe = new String();
    private java.lang.String fromValeurPlafond = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CATXTTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "IDTRANCHETAXE";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement pour un idCalculTaxe
        if (getForIdCalculTaxe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCALCULTAXE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCalculTaxe());
        }

        // traitement du positionnement à partir d'une valeur plafond
        if (getFromValeurPlafond().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "VALEURPLAFOND>=" + this._dbWriteNumeric(statement.getTransaction(), getFromValeurPlafond());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CATrancheTaxe();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 07:48:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCalculTaxe() {
        return forIdCalculTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 08:23:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromValeurPlafond() {
        return fromValeurPlafond;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 07:48:40)
     * 
     * @param newForIdTaxe
     *            java.lang.String
     */
    public void setForIdCalculTaxe(java.lang.String newForIdCalculTaxe) {
        forIdCalculTaxe = newForIdCalculTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 08:23:22)
     * 
     * @param newFromValeurPlafond
     *            java.lang.String
     */
    public void setFromValeurPlafond(java.lang.String newFromValeurPlafond) {
        fromValeurPlafond = newFromValeurPlafond;
    }
}
