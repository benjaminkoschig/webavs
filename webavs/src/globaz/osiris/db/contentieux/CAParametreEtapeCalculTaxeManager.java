package globaz.osiris.db.contentieux;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 11:00:29)
 * 
 * @author: Administrator
 */
public class CAParametreEtapeCalculTaxeManager extends globaz.globall.db.BManager implements java.io.Serializable {

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

    private java.lang.String forIdParametreEtape = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CACTPEP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "IDPARAMETREETAPE, IDCALCULTAXE";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un idParametreEtape
        if (getForIdParametreEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPARAMETREETAPE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdParametreEtape());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAParametreEtapeCalculTaxe();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.06.2002 13:26:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdParametreEtape() {
        return forIdParametreEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.06.2002 13:26:13)
     * 
     * @param newForIdParametreEtape
     *            java.lang.String
     */
    public void setForIdParametreEtape(java.lang.String newForIdParametreEtape) {
        forIdParametreEtape = newForIdParametreEtape;
    }
}
