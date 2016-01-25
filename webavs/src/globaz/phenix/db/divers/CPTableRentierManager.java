package globaz.phenix.db.divers;

public class CPTableRentierManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnneeRentier = "";
    private java.lang.String forIdTableRentier = "";
    private java.lang.String forRevenuRentier = "";
    private java.lang.String fromAnneeRentier = "";
    private java.lang.String fromRevenuRentier = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPTRENP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "JCANNE DESC, JCMREV DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdTableRentier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JCITRE=" + _dbWriteNumeric(statement.getTransaction(), getForIdTableRentier());
        }

        // traitement du positionnement
        if (getForAnneeRentier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JCANNE=" + _dbWriteNumeric(statement.getTransaction(), getForAnneeRentier());
        }

        // traitement du positionnement
        if (getForRevenuRentier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JCMREV=" + _dbWriteNumeric(statement.getTransaction(), getForRevenuRentier());
        }

        // traitement du positionnement
        if (getFromAnneeRentier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JCANNE<=" + _dbWriteNumeric(statement.getTransaction(), getFromAnneeRentier());
        }

        // traitement du positionnement
        if (getFromRevenuRentier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JCMREV<=" + _dbWriteNumeric(statement.getTransaction(), getFromRevenuRentier());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPTableRentier();
    }

    public java.lang.String getForAnneeRentier() {
        return forAnneeRentier;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdTableRentier() {
        return forIdTableRentier;
    }

    public java.lang.String getForRevenuRentier() {
        return forRevenuRentier;
    }

    public java.lang.String getFromAnneeRentier() {
        return fromAnneeRentier;
    }

    public java.lang.String getFromRevenuRentier() {
        return fromRevenuRentier;
    }

    public void setForAnneeRentier(java.lang.String newForAnneeRentier) {
        forAnneeRentier = newForAnneeRentier;
    }

    /**
     * Setter
     */
    public void setForIdTableRentier(java.lang.String newForIdTableRentier) {
        forIdTableRentier = newForIdTableRentier;
    }

    public void setForRevenuRentier(java.lang.String newForRevenuRentier) {
        forRevenuRentier = newForRevenuRentier;
    }

    public void setFromAnneeRentier(java.lang.String newFromAnneeRentier) {
        fromAnneeRentier = newFromAnneeRentier;
    }

    public void setFromRevenuRentier(java.lang.String newFromRevenuRentier) {
        fromRevenuRentier = newFromRevenuRentier;
    }
}
