package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPCommentaireCommunicationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCommentaire = "";
    /** (IMTICO) */
    private String forIdCommentaireCf = "";
    /** (IRICCF) */
    private String forIdCommunicationRetour = "";

    /** (IBIDCF) */
    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPCOCFP";
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
        if (getForIdCommentaireCf().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IRICCF=" + _dbWriteNumeric(statement.getTransaction(), getForIdCommentaireCf());
        }

        // traitement du positionnement
        if (getForIdCommunicationRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKIRET=" + _dbWriteNumeric(statement.getTransaction(), getForIdCommunicationRetour());
        }

        // traitement du positionnement
        if (getForIdCommentaire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IMTICO=" + _dbWriteNumeric(statement.getTransaction(), getForIdCommentaire());
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
        return new CPCommentaireCommunication();
    }

    public String getForIdCommentaire() {
        return forIdCommentaire;
    }

    public String getForIdCommentaireCf() {
        return forIdCommentaireCf;
    }

    public String getForIdCommunicationRetour() {
        return forIdCommunicationRetour;
    }

    public void setForIdCommentaire(String newForIdCommentaire) {
        forIdCommentaire = newForIdCommentaire;
    }

    public void setForIdCommentaireCf(String newForIdCommentaireCf) {
        forIdCommentaireCf = newForIdCommentaireCf;
    }

    public void setForIdCommunicationRetour(String newForIdCommunication) {
        forIdCommunicationRetour = newForIdCommunication;
    }
}
