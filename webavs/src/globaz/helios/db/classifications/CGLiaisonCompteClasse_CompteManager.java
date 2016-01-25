package globaz.helios.db.classifications;

import globaz.helios.db.comptes.CGCompte;

public class CGLiaisonCompteClasse_CompteManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdClasseCompte = "";
    private java.lang.String forIdCompte = "";

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CGCPGRP";
        String table2 = "CGCOMTP";
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".IDCOMPTE=" + _getCollection() + table2 + ".IDCOMPTE)";
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
            sqlWhere += _getCollection() + "CGCPGRP.IDCOMPTE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }
        if (getForIdClasseCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGCPGRP.IDCLASSECOMPTE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdClasseCompte());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGCompte();
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
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:49:03)
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
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:49:03)
     * 
     * @param newForIdCompte
     *            java.lang.String
     */
    public void setForIdCompte(java.lang.String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }
}
