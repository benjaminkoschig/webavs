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
        // incr�mente de +1 le num�ro
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
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
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
     *            L'objet d'acc�s � la base
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
     * Ins�rez la description de la m�thode ici.
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
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
