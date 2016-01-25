package globaz.osiris.db.contentieux;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (17.12.2001 09:58:42)
 * 
 * @author: Administrator
 */
public class CACalculTaxeManager extends globaz.globall.db.BManager implements java.io.Serializable {

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
        return _getCollection() + "CATXCTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "IDCALCULTAXE";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un num�ro
        if (getFromIdCalculTaxe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCALCULTAXE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdCalculTaxe());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CACalculTaxe();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.05.2002 10:03:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdCalculTaxe() {
        return fromIdCalculTaxe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.05.2002 10:03:34)
     * 
     * @param newFromIdRole
     *            java.lang.String
     */
    public void setFromIdCalculTaxe(java.lang.String newFromIdCalculTaxe) {
        fromIdCalculTaxe = newFromIdCalculTaxe;
    }
}
