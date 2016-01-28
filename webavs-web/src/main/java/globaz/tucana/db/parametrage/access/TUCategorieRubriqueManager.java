package globaz.tucana.db.parametrage.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Liste des cat�gories rubriques
 * 
 * @author fgo date de cr�ation : 22 juin 06
 * @version : version 1.0
 * 
 */
public class TUCategorieRubriqueManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Pour csOperation - cs operation tu_oper est = � ... (CSOPER) */
    private String forCsOperation = new String();

    /** Pour csRubrique - cs rubrique tu_rubr est = � ... (CSRUBR) */
    private String forCsRubrique = new String();

    /** Table : TUBPCRU */

    /**
     * Pour idCategorieRubrique - cl� primaire du fichier tubpcru est = � ... (BCRUID)
     */
    private String forIdCategorieRubrique = new String();

    /**
     * Pour idGroupeRubrique - cl� primaire du fichier groupe de rubrique est = � ... (BGRCID)
     */
    private String forIdGroupeRubrique = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "TUBPCRU" (Model : TUCategorieRubrique)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(ITUCategorieRubriqueDefTable.TABLE_NAME).toString();
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param statement
     *            de type BStatement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Retourne la clause WHERE de la requete SQL
     * 
     * @param statement
     *            BStatement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        /* composant de la requete initialises avec les options par defaut */
        StringBuffer sqlWhere = new StringBuffer();
        // traitement du positionnement
        if (getForIdCategorieRubrique().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUCategorieRubriqueDefTable.ID_CATEGORIE_RUBRIQUE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdCategorieRubrique()));
        }
        // traitement du positionnement
        if (getForIdGroupeRubrique().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUCategorieRubriqueDefTable.ID_GROUPE_CATEGORIE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdGroupeRubrique()));
        }
        // traitement du positionnement
        if (getForCsRubrique().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUCategorieRubriqueDefTable.CS_RUBRIQUE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsRubrique()));
        }
        // traitement du positionnement
        if (getForCsOperation().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(ITUCategorieRubriqueDefTable.CS_OPERATION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsOperation()));
        }
        return sqlWhere.toString();
    }

    /**
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TUCategorieRubrique();
    }

    /**
     * R�cu�re l'instruction sql ex�cut�e
     * 
     * 
     */
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();
    }

    /**
     * Renvoie forCsOpertation;
     * 
     * @return String csOperation - cs op�ratino tu_oper;
     */
    public String getForCsOperation() {
        return forCsOperation;
    }

    /**
     * Renvoie forCsRubrique;
     * 
     * @return String csRubrique - cs rubrique tu_rubr;
     */
    public String getForCsRubrique() {
        return forCsRubrique;
    }

    /**
     * Renvoie forIdCategorieRubrique;
     * 
     * @return String idCategorieRubrique - cl� primaire du fichier tubpcru;
     */
    public String getForIdCategorieRubrique() {
        return forIdCategorieRubrique;
    }

    /**
     * Renvoie forIdGroupeRubrique;
     * 
     * @return String idGroupeRubrique - cl� primaire du fichier groupe de rubrique;
     */
    public String getForIdGroupeRubrique() {
        return forIdGroupeRubrique;
    }

    /**
     * S�lection par forCsOperation
     * 
     * @param newForCsOperation
     *            String - cs op�ration tu_oper
     */
    public void setForCsOperation(String newForCsOperation) {
        forCsOperation = newForCsOperation;
    }

    /**
     * S�lection par forCsRubrique
     * 
     * @param newForCsRubrique
     *            String - cs rubrique tu_rubr
     */
    public void setForCsRubrique(String newForCsRubrique) {
        forCsRubrique = newForCsRubrique;
    }

    /**
     * S�lection par forIdCategorieRubrique
     * 
     * @param newForIdCategorieRubrique
     *            String - cl� primaire du fichier tubpcru
     */
    public void setForIdCategorieRubrique(String newForIdCategorieRubrique) {
        forIdCategorieRubrique = newForIdCategorieRubrique;
    }

    /**
     * S�lection par forIdGroupeRubrique
     * 
     * @param newForIdGroupeRubrique
     *            String - cl� primaire du fichier groupe de rubrique
     */
    public void setForIdGroupeRubrique(String newForIdGroupeRubrique) {
        forIdGroupeRubrique = newForIdGroupeRubrique;
    }

}
