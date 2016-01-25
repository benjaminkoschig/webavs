package globaz.tucana.db.parametrage.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Classe représentant les rubriques associées à une catégorie
 * 
 * @author fgo date de création : 22 juin 06
 * @version : version 1.0
 * 
 */
public class TUCategorieRubrique extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** csOperation - cs operation tu_oper (CSOPER) */
    private String csOperation = new String();

    /** Table : TUBPCRU */

    /** csRubrique - cs rubrique tu_rubr (CSRUBR) */
    private String csRubrique = new String();

    /** idCategorieRubrique - clé primaire du fichier tubpcru (BCRUID) */
    private String idCategorieRubrique = new String();

    /** idGroupeCategorie - clé primaire du fichier groupe de rubrique (BGRCID) */
    private String idGroupeCategorie = new String();

    /**
     * Constructeur de la classe TUCategorieRubrique
     */
    public TUCategorieRubrique() {
        super();
    }

    /**
     * Méthode qui incrémente la clé primaire
     * 
     * @param transaction
     *            BTransaction transaction
     * @throws Exception
     *             exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCategorieRubrique(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table TUBPCRU
     * 
     * @return String TUBPCRU
     */
    @Override
    protected String _getTableName() {
        return ITUCategorieRubriqueDefTable.TABLE_NAME;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCategorieRubrique = statement.dbReadNumeric(ITUCategorieRubriqueDefTable.ID_CATEGORIE_RUBRIQUE);
        idGroupeCategorie = statement.dbReadNumeric(ITUCategorieRubriqueDefTable.ID_GROUPE_CATEGORIE);
        csRubrique = statement.dbReadNumeric(ITUCategorieRubriqueDefTable.CS_RUBRIQUE);
        csOperation = statement.dbReadNumeric(ITUCategorieRubriqueDefTable.CS_OPERATION);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Indique la clé principale UCategorieRubrique() du fichier TUBPCRU
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                ITUCategorieRubriqueDefTable.ID_CATEGORIE_RUBRIQUE,
                _dbWriteNumeric(statement.getTransaction(), getIdCategorieRubrique(),
                        "idCategorieRubrique - clé primaire du fichier tubpcru"));
    }

    /**
     * Ecriture des propriétés
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écritrues des propriétés
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                ITUCategorieRubriqueDefTable.ID_CATEGORIE_RUBRIQUE,
                _dbWriteNumeric(statement.getTransaction(), getIdCategorieRubrique(),
                        "idCategorieRubrique - clé primaire du fichier tubpcru"));
        statement.writeField(
                ITUCategorieRubriqueDefTable.ID_GROUPE_CATEGORIE,
                _dbWriteNumeric(statement.getTransaction(), getIdGroupeCategorie(),
                        "idGroupeCategorie - clé primaire du fichier groupe de rubrique"));
        statement.writeField(ITUCategorieRubriqueDefTable.CS_RUBRIQUE,
                _dbWriteNumeric(statement.getTransaction(), getCsRubrique(), "csRubrique - cs rubrique tu_rubr"));
        statement.writeField(ITUCategorieRubriqueDefTable.CS_OPERATION,
                _dbWriteNumeric(statement.getTransaction(), getCsOperation(), "csOperation - cs operation tu_oper"));
    }

    /**
     * Renvoie la zone csOperation - cs opération tu_oper (CSOPER)
     * 
     * @return String csOperation - cs operation tu_oper
     */
    public String getCsOperation() {
        return csOperation;
    }

    /**
     * Renvoie la zone csRubrique - cs rubrique tu_rubr (CSRUBR)
     * 
     * @return String csRubrique - cs rubrique tu_rubr
     */
    public String getCsRubrique() {
        return csRubrique;
    }

    /**
     * Renvoie la zone idCategorieRubrique - clé primaire du fichier tubpcru (BCRUID)
     * 
     * @return String idCategorieRubrique - clé primaire du fichier tubpcru
     */
    public String getIdCategorieRubrique() {
        return idCategorieRubrique;
    }

    /**
     * Renvoie la zone idGroupeCategorie - clé primaire du fichier groupe de rubrique (BGRCID)
     * 
     * @return String idGroupeCategorie - clé primaire du fichier groupe de rubrique
     */
    public String getIdGroupeCategorie() {
        return idGroupeCategorie;
    }

    /**
     * Modifie la zone csOperation - cs opération tu_rubr (CSOPER)
     * 
     * @param newCsOperation
     *            - cs rubrique tu_rubr String
     */
    public void setCsOperation(String newCsOperation) {
        csOperation = newCsOperation;
    }

    /**
     * Modifie la zone csRubrique - cs rubrique tu_rubr (CSRUBR)
     * 
     * @param newCsRubrique
     *            - cs rubrique tu_rubr String
     */
    public void setCsRubrique(String newCsRubrique) {
        csRubrique = newCsRubrique;
    }

    /**
     * Modifie la zone idCategorieRubrique - clé primaire du fichier tubpcru (BCRUID)
     * 
     * @param newIdCategorieRubrique
     *            - clé primaire du fichier tubpcru String
     */
    public void setIdCategorieRubrique(String newIdCategorieRubrique) {
        idCategorieRubrique = newIdCategorieRubrique;
    }

    /**
     * Modifie la zone idGroupeCategorie - clé primaire du fichier groupe de rubrique (BGRCID)
     * 
     * @param newIdGroupeRubrique
     *            - clé primaire du fichier groupe de rubrique String
     */
    public void setIdGroupeCategorie(String newIdGroupeRubrique) {
        idGroupeCategorie = newIdGroupeRubrique;
    }

}
