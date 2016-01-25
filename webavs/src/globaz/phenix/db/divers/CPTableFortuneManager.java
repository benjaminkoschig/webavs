package globaz.phenix.db.divers;

public class CPTableFortuneManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnneeFortune = "";
    private java.lang.String forCanton = "";
    private java.lang.String forIdTableFortune = "";
    private java.lang.String fromAnneeFortune = "";
    private java.lang.String fromCanton = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPTFORP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "JDANNE DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdTableFortune().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JDITFO=" + _dbWriteNumeric(statement.getTransaction(), getForIdTableFortune());
        }

        // traitement du positionnement
        if (getForAnneeFortune().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JDANNE=" + _dbWriteNumeric(statement.getTransaction(), getForAnneeFortune());
        }

        // traitement du positionnement
        if (getForCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JDTCAN=" + _dbWriteNumeric(statement.getTransaction(), getForCanton());
        }

        // traitement du positionnement
        if (getFromAnneeFortune().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JDANNE<=" + _dbWriteNumeric(statement.getTransaction(), getFromAnneeFortune());
        }

        // traitement du positionnement
        if (getFromCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JDTCAN>=" + _dbWriteNumeric(statement.getTransaction(), getFromCanton());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPTableFortune();
    }

    public java.lang.String getForAnneeFortune() {
        return forAnneeFortune;
    }

    public java.lang.String getForCanton() {
        return forCanton;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdTableFortune() {
        return forIdTableFortune;
    }

    public java.lang.String getFromAnneeFortune() {
        return fromAnneeFortune;
    }

    public java.lang.String getFromCanton() {
        return fromCanton;
    }

    public void setForAnneeFortune(java.lang.String newForAnneeFortune) {
        forAnneeFortune = newForAnneeFortune;
    }

    public void setForCanton(java.lang.String newForCanton) {
        forCanton = newForCanton;
    }

    /**
     * Setter
     */
    public void setForIdTableFortune(java.lang.String newForIdTableFortune) {
        forIdTableFortune = newForIdTableFortune;
    }

    public void setFromAnneeFortune(java.lang.String newFromAnneeFortune) {
        fromAnneeFortune = newFromAnneeFortune;
    }

    public void setFromCanton(java.lang.String newFromCanton) {
        fromCanton = newFromCanton;
    }
}
