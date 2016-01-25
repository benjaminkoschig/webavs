package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPRemarqueDecisionManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IAIDEC) */
    private String forEmplacement = "";
    /** (INIREM) */
    private String forIdDecision = "";
    /** Fichier CPREDEP */
    private String forIdRemarqueDecision = "";
    /** (INTEMP) */
    private String fromTexteRemarqueDecision = "";
    /** (INTEXT) */
    private java.lang.String orderBy = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPREDEP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdRemarqueDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "INIREM=" + _dbWriteNumeric(statement.getTransaction(), getForIdRemarqueDecision());
        }

        // traitement du positionnement
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC=" + _dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }

        // traitement du positionnement
        if (getForEmplacement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "INTEMP=" + _dbWriteNumeric(statement.getTransaction(), getForEmplacement());
        }

        // traitement du positionnement
        if (getFromTexteRemarqueDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "INTEXT>=" + _dbWriteString(statement.getTransaction(), getFromTexteRemarqueDecision());
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPRemarqueDecision();
    }

    public String getForEmplacement() {
        return forEmplacement;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdRemarqueDecision() {
        return forIdRemarqueDecision;
    }

    public String getFromTexteRemarqueDecision() {
        return fromTexteRemarqueDecision;
    }

    /**
     * Returns the orderBy.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrderBy() {
        if (orderBy == null) {
            return "";
        } else {
            return orderBy;
        }
    }

    public void setForEmplacement(String newForEmplacement) {
        forEmplacement = newForEmplacement;
    }

    public void setForIdDecision(String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdRemarqueDecision(String newForIdRemarqueDecision) {
        forIdRemarqueDecision = newForIdRemarqueDecision;
    }

    public void setFromTexteRemarqueDecision(String newFromTexteRemarqueDecision) {
        fromTexteRemarqueDecision = newFromTexteRemarqueDecision;
    }

    /**
     * Sets the orderBy.
     * 
     * @param orderBy
     *            The orderBy to set
     */
    public void setOrderBy(java.lang.String orderBy) {
        this.orderBy = orderBy;
    }

}
