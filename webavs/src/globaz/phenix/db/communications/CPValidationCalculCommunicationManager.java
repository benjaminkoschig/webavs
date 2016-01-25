package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPValidationCalculCommunicationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IAIDEC) */
    private String forDateCalcul = "";
    private String forIdCommunicationRetour = "";
    /** (IKIRET) */
    private String forIdDecision = "";

    /** (ILDCAL) */
    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPVCCOP";
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
        return "";
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
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC=" + _dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }

        // traitement du positionnement
        if (getForIdCommunicationRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKIRET=" + _dbWriteNumeric(statement.getTransaction(), getForIdCommunicationRetour());
        }

        // traitement du positionnement
        if (getForDateCalcul().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ILDCAL=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateCalcul());
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
        return new CPValidationCalculCommunication();
    }

    /**
     * Returns the forDateCalcul.
     * 
     * @return String
     */
    public String getForDateCalcul() {
        return forDateCalcul;
    }

    public String getForIdCommunicationRetour() {
        return forIdCommunicationRetour;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * Sets the forDateCalcul.
     * 
     * @param forDateCalcul
     *            The forDateCalcul to set
     */
    public void setForDateCalcul(String forDateCalcul) {
        this.forDateCalcul = forDateCalcul;
    }

    public void setForIdCommunicationRetour(String newForIdCommunication) {
        forIdCommunicationRetour = newForIdCommunication;
    }

    public void setForIdDecision(String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

}
