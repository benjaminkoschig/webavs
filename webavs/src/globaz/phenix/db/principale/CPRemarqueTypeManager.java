package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPRemarqueTypeManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IOIDRE) */
    private String forEmplacement = "";
    /** Fichier CPREMAP */
    private String forIdRemarqueType = "";
    /** (IOTEMP) */
    private String forLangue = "";
    /** (IOTLAN) */
    private String fromTexteRemarqueType = "";

    /** (IOTEXT) */
    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPREMAP";
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
        return "IOIDRE";
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
        if (getForIdRemarqueType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IOIDRE=" + _dbWriteNumeric(statement.getTransaction(), getForIdRemarqueType());
        }

        // traitement du positionnement
        if (getForEmplacement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IOTEMP=" + _dbWriteNumeric(statement.getTransaction(), getForEmplacement());
        }

        // traitement du positionnement
        if (getForLangue().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IOTLAN=" + _dbWriteNumeric(statement.getTransaction(), getForLangue());
        }

        // traitement du positionnement
        if (getFromTexteRemarqueType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IOTEXT like "
                    + _dbWriteString(statement.getTransaction(), "%" + getFromTexteRemarqueType() + "%");
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
        return new CPRemarqueType();
    }

    public String getForEmplacement() {
        return forEmplacement;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdRemarqueType() {
        return forIdRemarqueType;
    }

    public String getForLangue() {
        return forLangue;
    }

    public String getFromTexteRemarqueType() {
        return fromTexteRemarqueType;
    }

    public void setForEmplacement(String newForEmplacement) {
        forEmplacement = newForEmplacement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdRemarqueType(String newForIdRemarqueType) {
        forIdRemarqueType = newForIdRemarqueType;
    }

    public void setForLangue(String newForLangue) {
        forLangue = newForLangue;
    }

    public void setFromTexteRemarqueType(String newFromTexteRemarqueType) {
        fromTexteRemarqueType = newFromTexteRemarqueType;
    }
}
