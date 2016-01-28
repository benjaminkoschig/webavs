package globaz.osiris.db.contentieux;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 08:43:01)
 * 
 * @author: Administrator
 */
public class CAEtapeManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forTypeEtape = "";
    private java.lang.String fromIdEtape = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAETCTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "TYPEETAPE";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un numéro
        if (getFromIdEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDETAPE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdEtape());
        }

        if (getForTypeEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TYPEETAPE=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeEtape());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAEtape();
    }

    public String getForTypeEtape() {
        return forTypeEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdEtape() {
        return fromIdEtape;
    }

    public void setForTypeEtape(String forTypeEtape) {
        this.forTypeEtape = forTypeEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.05.2002 10:03:34)
     * 
     * @param newFromIdRole
     *            java.lang.String
     */
    public void setFromIdEtape(java.lang.String newFromIdEtape) {
        fromIdEtape = newFromIdEtape;
    }

}
