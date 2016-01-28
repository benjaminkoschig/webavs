package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPLienTypeDecRemarque extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Fichier CPLTDRP */
    private String idLienTypeRemarque = "";
    /** (ITILRT) */
    private String idRemarqueType = "";
    /** (IOIDRE) */
    private String typeDecision = "";

    /** (IATTDE) */
    /**
     * Commentaire relatif au constructeur CPLienTypeDecRemarque
     */
    public CPLienTypeDecRemarque() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdLienTypeRemarque(_incCounter(transaction, idLienTypeRemarque));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPLTDRP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLienTypeRemarque = statement.dbReadNumeric("ITILRT");
        idRemarqueType = statement.dbReadNumeric("IOIDRE");
        typeDecision = statement.dbReadNumeric("IATTDE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ITILRT", _dbWriteNumeric(statement.getTransaction(), getIdLienTypeRemarque(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("ITILRT",
                _dbWriteNumeric(statement.getTransaction(), getIdLienTypeRemarque(), "idLienTypeRemarque"));
        statement.writeField("IOIDRE",
                _dbWriteNumeric(statement.getTransaction(), getIdRemarqueType(), "idRemarqueType"));
        statement.writeField("IATTDE", _dbWriteNumeric(statement.getTransaction(), getTypeDecision(), "typeDecision"));
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdLienTypeRemarque() {
        return idLienTypeRemarque;
    }

    public String getIdRemarqueType() {
        return idRemarqueType;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdLienTypeRemarque(String newIdLienTypeRemarque) {
        idLienTypeRemarque = newIdLienTypeRemarque;
    }

    public void setIdRemarqueType(String newIdRemarqueType) {
        idRemarqueType = newIdRemarqueType;
    }

    public void setTypeDecision(String newTypeDecision) {
        typeDecision = newTypeDecision;
    }
}
