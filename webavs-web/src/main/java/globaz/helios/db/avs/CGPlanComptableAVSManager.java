package globaz.helios.db.avs;

public class CGPlanComptableAVSManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String forNumeroCompteAVS = "";

    private java.lang.String forType = new String();

    /**
     * Commentaire relatif au constructeur CGPlanComptableAVSManager.
     */
    public CGPlanComptableAVSManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGPCAVP";
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

        // traitement du positionnement
        if (getForNumeroCompteAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NUMERO=" + _dbWriteNumeric(statement.getTransaction(), getForNumeroCompteAVS());
        }

        if (getForType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TYPE=" + _dbWriteNumeric(statement.getTransaction(), getForType());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGPlanComptableAVS();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 13:36:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroCompteAVS() {
        return forNumeroCompteAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 16:04:01)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForType() {
        return forType;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 13:36:53)
     * 
     * @param newForNumeroCompteAVS
     *            java.lang.String
     */
    public void setForNumeroCompteAVS(java.lang.String newForNumeroCompteAVS) {
        forNumeroCompteAVS = newForNumeroCompteAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 16:04:01)
     * 
     * @param newForType
     *            java.lang.String
     */
    public void setForType(java.lang.String newForType) {
        forType = newForType;
    }
}
