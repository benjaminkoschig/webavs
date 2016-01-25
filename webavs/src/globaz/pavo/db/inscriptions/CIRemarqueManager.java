package globaz.pavo.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Liste de remarques. Date de création : (13.11.2002 09:54:27)
 * 
 * @author: ema
 */
public class CIRemarqueManager extends BManager {
    /** Fichier CIREMAP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /***/
    private String forEcritureId = new String();

    /** (KIIREM) */
    private String forIdRemarque = new String();

    /**
     * Commentaire relatif au constructeur CIRemarqueManager.
     */
    public CIRemarqueManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIREMAP";
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
        if (getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KIIREM=" + _dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }

        // traitement du positionnement
        if (getForEcritureId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KBIECR=" + _dbWriteNumeric(statement.getTransaction(), getForEcritureId());
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
        return new CIRemarque();
    }

    public String getForEcritureId() {
        return forEcritureId;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdRemarque() {
        return forIdRemarque;
    }

    public void setForEcritureId(String newForEcritureId) {
        forEcritureId = newForEcritureId;
    }

    public void setForIdRemarque(String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }
}
