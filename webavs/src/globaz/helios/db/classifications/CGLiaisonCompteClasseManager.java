package globaz.helios.db.classifications;

public class CGLiaisonCompteClasseManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdClasseCompte = "";
    private java.lang.String forIdCompte = "";

    /**
     * Getter
     */

    /**
     * Setter
     */

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGCPGRP";
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
        if (getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTE=" + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }
        if (getForIdClasseCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCLASSECOMPTE=" + _dbWriteNumeric(statement.getTransaction(), getForIdClasseCompte());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGLiaisonCompteClasse();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:48:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdClasseCompte() {
        return forIdClasseCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:47:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:48:18)
     * 
     * @param newForIdClasseCompte
     *            java.lang.String
     */
    public void setForIdClasseCompte(java.lang.String newForIdClasseCompte) {
        forIdClasseCompte = newForIdClasseCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:47:59)
     * 
     * @param newForIdCompte
     *            java.lang.String
     */
    public void setForIdCompte(java.lang.String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }
}
