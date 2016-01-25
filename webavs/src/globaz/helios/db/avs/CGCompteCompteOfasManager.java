package globaz.helios.db.avs;

public class CGCompteCompteOfasManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdCompte = new String();
    private java.lang.String forIdCompteOfas = new String();

    /**
     * Commentaire relatif au constructeur CGCompteCompteOfasManager.
     */
    public CGCompteCompteOfasManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGCPCOP";
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
        if (getForIdCompteOfas().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTEOFAS=" + _dbWriteNumeric(statement.getTransaction(), getForIdCompteOfas());
        }

        // traitement du positionnement
        if (getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTE=" + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGCompteCompteOfas();
    }

    public java.lang.String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdCompteOfas() {
        return forIdCompteOfas;
    }

    public void setForIdCompte(java.lang.String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    /**
     * Setter
     */
    public void setForIdCompteOfas(java.lang.String newForIdCompteOfas) {
        forIdCompteOfas = newForIdCompteOfas;
    }
}
