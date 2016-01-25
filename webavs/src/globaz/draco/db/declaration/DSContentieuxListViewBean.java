package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class DSContentieuxListViewBean extends BManager {
    /** Fichier DSCONTP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (TDTCON) */
    private String forCsContentieux = new String();
    /** (TDICON) */
    private String forIdContentieux = new String();
    /** (TAIDDE) */
    private String forIdDeclaration = new String();
    /** (TDTCON) */
    private String fromCsContentieux = new String();
    /** (TDICON) */
    private String fromIdContentieux = new String();
    /** (TAIDDE) */
    private String fromIdDeclaration = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "DSCONTP";
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
        if (getForIdContentieux().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TDICON=" + _dbWriteNumeric(statement.getTransaction(), getForIdContentieux());
        }

        // traitement du positionnement
        if (getForIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE=" + _dbWriteNumeric(statement.getTransaction(), getForIdDeclaration());
        }

        // traitement du positionnement
        if (getForCsContentieux().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TDTCON=" + _dbWriteNumeric(statement.getTransaction(), getForCsContentieux());
        }

        // traitement du positionnement
        if (getFromIdContentieux().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TDICON>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdContentieux());
        }

        // traitement du positionnement
        if (getFromIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdDeclaration());
        }

        // traitement du positionnement
        if (getFromCsContentieux().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TDTCON>=" + _dbWriteNumeric(statement.getTransaction(), getFromCsContentieux());
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
        return new DSContentieuxViewBean();
    }

    public String getForCsContentieux() {
        return forCsContentieux;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdContentieux() {
        return forIdContentieux;
    }

    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    public String getFromCsContentieux() {
        return fromCsContentieux;
    }

    public String getFromIdContentieux() {
        return fromIdContentieux;
    }

    public String getFromIdDeclaration() {
        return fromIdDeclaration;
    }

    public void setForCsContentieux(String newForCsContentieux) {
        forCsContentieux = newForCsContentieux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newD
     *            String
     */

    public void setForIdContentieux(String newForIdContentieux) {
        forIdContentieux = newForIdContentieux;
    }

    public void setForIdDeclaration(String newForIdDeclaration) {
        forIdDeclaration = newForIdDeclaration;
    }

    public void setFromCsContentieux(String newFromCsContentieux) {
        fromCsContentieux = newFromCsContentieux;
    }

    public void setFromIdContentieux(String newFromIdContentieux) {
        fromIdContentieux = newFromIdContentieux;
    }

    public void setFromIdDeclaration(String newFromIdDeclaration) {
        fromIdDeclaration = newFromIdDeclaration;
    }

}
