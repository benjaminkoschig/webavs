package globaz.helios.db.comptes;

import globaz.globall.db.BTransaction;

public class CGRemarque extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idRemarque = new String();
    private java.lang.String remarque = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGRemarque
     */
    public CGRemarque() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdRemarque(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGREMAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRemarque = statement.dbReadNumeric("IDREMARQUE");
        remarque = statement.dbReadString("REMARQUE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDREMARQUE", _dbWriteNumeric(statement.getTransaction(), getIdRemarque(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDREMARQUE", _dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("REMARQUE", _dbWriteString(statement.getTransaction(), getRemarque(), "remarque"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdRemarque() {
        return idRemarque;
    }

    public java.lang.String getRemarque() {
        return remarque;
    }

    /**
     * Setter
     */
    public void setIdRemarque(java.lang.String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setRemarque(java.lang.String newRemarque) {
        remarque = newRemarque;
    }
}
