package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class DSLigneEmployeListViewBean extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (TAIDDE) */
    private String forIdDeclaration = new String();
    /** Fichier DSLIEMP */
    /** (TCILIE) */
    private String forIdLigneEmploye = new String();
    /** (KANAVS) */
    private String forNumeroAVS = new String();
    /** (TAIDDE) */
    private String fromIdDeclaration = new String();
    /** (TCILIE) */
    private String fromIdLigneEmploye = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "DSLIEMP";
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
        if (getForIdLigneEmploye().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TCILIE=" + _dbWriteNumeric(statement.getTransaction(), getForIdLigneEmploye());
        }
        // traitement du positionnement
        if (getForIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE=" + _dbWriteNumeric(statement.getTransaction(), getForIdDeclaration());
        }
        // traitement du positionnement
        if (getForNumeroAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KANAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAVS());
        }
        // traitement du positionnement
        if (getFromIdLigneEmploye().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TCILIE>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdLigneEmploye());
        }
        // traitement du positionnement
        if (getFromIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdDeclaration());
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
        return new DSLigneEmployeViewBean();
    }

    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdLigneEmploye() {
        return forIdLigneEmploye;
    }

    public String getForNumeroAVS() {
        return forNumeroAVS;
    }

    public String getFromIdDeclaration() {
        return fromIdDeclaration;
    }

    public String getFromIdLigneEmploye() {
        return fromIdLigneEmploye;
    }

    public void setForIdDeclaration(String newForIdDeclaration) {
        forIdDeclaration = newForIdDeclaration;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newD
     *            String
     */
    public void setForIdLigneEmploye(String newForIdLigneEmploye) {
        forIdLigneEmploye = newForIdLigneEmploye;
    }

    public void setForNumeroAVS(String newForNumeroAVS) {
        forNumeroAVS = newForNumeroAVS;
    }

    public void setFromIdDeclaration(String newFromIdDeclaration) {
        fromIdDeclaration = newFromIdDeclaration;
    }

    public void setFromIdLigneEmploye(String newFromIdLigneEmploye) {
        fromIdLigneEmploye = newFromIdLigneEmploye;
    }
}