package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPLienTypeDecRemarqueManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Fichier CPLTDRP */
    private String forIdRemarqueType = "";
    /** (IOIDRE) */
    private String forTypeDecision = "";

    /** (IATTDE) */
    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPLTDRP";
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
        if (getForTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE=" + _dbWriteNumeric(statement.getTransaction(), getForTypeDecision());
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
        return new CPLienTypeDecRemarque();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdRemarqueType() {
        return forIdRemarqueType;
    }

    public String getForTypeDecision() {
        return forTypeDecision;
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

    public void setForTypeDecision(String newForTypeDecision) {
        forTypeDecision = newForTypeDecision;
    }
}
