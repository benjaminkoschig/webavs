package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPCommentaireRemarqueTypeManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IOIDRE) */
    private String forIdCommentaire = "";
    /** Fichier CPCORTP */
    private String forIdRemarqueType = "";

    /** (IMTICO) */
    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPCORTP";
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
        if (getForIdCommentaire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IMTICO=" + _dbWriteNumeric(statement.getTransaction(), getForIdCommentaire());
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet ?tendant BEntity
     * 
     * @return BEntity un objet rep?sentant le r?sultat
     * @throws Exception
     *             la cr?ation a ?chou?e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPCommentaireRemarqueType();
    }

    public String getForIdCommentaire() {
        return forIdCommentaire;
    }

    /**
     * Ins?rez la description de la m?thode ici.
     * 
     * @return String
     */
    public String getForIdRemarqueType() {
        return forIdRemarqueType;
    }

    public void setForIdCommentaire(String newForIdCommentaire) {
        forIdCommentaire = newForIdCommentaire;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdRemarqueType(String newForIdRemarqueType) {
        forIdRemarqueType = newForIdRemarqueType;
    }
}
