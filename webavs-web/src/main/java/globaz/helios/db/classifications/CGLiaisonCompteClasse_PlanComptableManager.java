package globaz.helios.db.classifications;

import globaz.jade.client.util.JadeStringUtil;

public class CGLiaisonCompteClasse_PlanComptableManager extends globaz.globall.db.BManager implements
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdCentreCharge = "";
    private java.lang.String forIdClasseCompte = "";
    private java.lang.String forIdCompte = "";

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        String table1 = _getCollection() + "CGPLANP";
        String table2 = _getCollection() + "CGCOMTP";
        String table3 = _getCollection() + "CGSOLDP";
        String table4 = _getCollection() + "CGCPGRP";
        String from = " " + table1;
        from += " INNER JOIN " + table2 + " ON (" + table1 + ".IDCOMPTE=" + table2 + ".IDCOMPTE)";
        from += " INNER JOIN " + table4 + " ON (" + table1 + ".IDCOMPTE=" + table4 + ".IDCOMPTE)";
        from += " LEFT OUTER JOIN " + table3 + " ON (" + table1 + ".IDCOMPTE=" + table3 + ".IDCOMPTE)";

        return from;
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

        if (!JadeStringUtil.isIntegerEmpty(getForIdCentreCharge())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGSOLDP.IDCENTRECHARGE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCentreCharge());
        } else {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "( " + _getCollection() + "CGSOLDP.IDCENTRECHARGE="
                    + _dbWriteNumeric(statement.getTransaction(), "0") + " OR " + _getCollection()
                    + "CGSOLDP.IDCENTRECHARGE is null ) ";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new globaz.helios.db.comptes.CGPlanComptableViewBean();
    }

    /**
     * Returns the forIdCentreCharge.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:47:52)
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
     * Sets the forIdCentreCharge.
     * 
     * @param forIdCentreCharge
     *            The forIdCentreCharge to set
     */
    public void setForIdCentreCharge(java.lang.String forIdCentreCharge) {
        this.forIdCentreCharge = forIdCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:47:52)
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
