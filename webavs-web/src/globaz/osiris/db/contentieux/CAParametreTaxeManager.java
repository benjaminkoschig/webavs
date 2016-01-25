package globaz.osiris.db.contentieux;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (17.12.2001 11:26:19)
 * 
 * @author: Administrator
 */
public class CAParametreTaxeManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdCalculTaxe = new String();
    private java.lang.String forIdRubrique = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CATXPTP";
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

        // traitement du positionnement pour un idCalculTaxe
        if (getForIdCalculTaxe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCALCULTAXE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCalculTaxe());
        }

        // traitement du positionnement pour un idRubrique
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDRUBRIQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAParametreTaxe();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:16:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCalculTaxe() {
        return forIdCalculTaxe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 15:31:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdRubrique() {
        return forIdRubrique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 09:16:17)
     * 
     * @param newForIdCalculTaxe
     *            java.lang.String
     */
    public void setForIdCalculTaxe(java.lang.String newForIdCalculTaxe) {
        forIdCalculTaxe = newForIdCalculTaxe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 15:31:03)
     * 
     * @param newForIdRubrique
     *            java.lang.String
     */
    public void setForIdRubrique(java.lang.String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }
}
