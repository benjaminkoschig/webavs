package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Manager de <tt>CIPeriodeSplitting</tt>. Date de création : (12.11.2002 13:44:05)
 * 
 * @author: David Girardin
 */
public class CIPeriodeSplittingManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (KAIIND) */
    private String forCompteIndividuelId = new String();
    private String forJournalId = new String();
    /** (KLNPAR) */
    private String forPartenaireNumeroAvs = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CISPLIP";
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
        // par année de fin
        return "KLNAFI";
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
        if (getForCompteIndividuelId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KAIIND=" + _dbWriteNumeric(statement.getTransaction(), getForCompteIndividuelId());
        }
        // traitement du positionnement
        if (getForPartenaireNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KLNPAR=" + _dbWriteString(statement.getTransaction(), getForPartenaireNumeroAvs());
        }
        if (getForJournalId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KLIDJN=" + _dbWriteNumeric(statement.getTransaction(), getForJournalId());
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
        return new CIPeriodeSplitting();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForCompteIndividuelId() {
        return forCompteIndividuelId;
    }

    /**
     * Returns the forJournalId.
     * 
     * @return String
     */
    public String getForJournalId() {
        return forJournalId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 12:42:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForPartenaireNumeroAvs() {
        return forPartenaireNumeroAvs;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForCompteIndividuelId(String newForCompteIndividuelId) {
        forCompteIndividuelId = newForCompteIndividuelId;
    }

    /**
     * Sets the forJournalId.
     * 
     * @param forJournalId
     *            The forJournalId to set
     */
    public void setForJournalId(String forJournalId) {
        this.forJournalId = forJournalId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 12:42:40)
     * 
     * @param newForPartenaireNumeroAvs
     *            java.lang.String
     */
    public void setForPartenaireNumeroAvs(java.lang.String newForPartenaireNumeroAvs) {
        forPartenaireNumeroAvs = newForPartenaireNumeroAvs;
    }
}
