package globaz.osiris.db.comptes;

import globaz.globall.db.BTransaction;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 15:44:12)
 * 
 * @author: Administrator
 */
public class CAGroupementOperation extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CAGroupement groupement = null;
    private java.lang.String idGroupement = new String();
    private java.lang.String idOperation = new String();
    private CAOperation operation = null;

    // code systeme

    /**
     * Commentaire relatif au constructeur CAGroupementOperation
     */
    public CAGroupementOperation() {
        super();
    }

    /**
     * Effectue des traitements après une suppression de la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la suppression de l'entité de la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterDelete()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // Supprimer le groupement si vide
        if (!getGroupement().hasOperations(transaction)) {
            getGroupement().delete(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7330"));
            }
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAGROPP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idGroupement = statement.dbReadNumeric("IDGROUPEMENT");
        idOperation = statement.dbReadNumeric("IDOPERATION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdGroupement(), getSession().getLabel("7320"));
        _propertyMandatory(statement.getTransaction(), getIdOperation(), getSession().getLabel("7331"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDGROUPEMENT", this._dbWriteNumeric(statement.getTransaction(), getIdGroupement(), ""));
        statement.writeKey("IDOPERATION", this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDGROUPEMENT",
                this._dbWriteNumeric(statement.getTransaction(), getIdGroupement(), "idGroupement"));
        statement.writeField("IDOPERATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), "idOperation"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 13:19:46)
     * 
     * @return globaz.osiris.db.comptes.CAGroupement
     */
    public CAGroupement getGroupement() {
        // Si pas déjà chargé
        if (groupement == null) {
            groupement = new CAGroupement();
            groupement.setSession(getSession());
            groupement.setIdGroupement(getIdGroupement());
            try {
                groupement.retrieve();
                if (groupement.isNew() || groupement.hasErrors()) {
                    groupement = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                groupement = null;

            }
        }

        return groupement;

    }

    /**
     * Getter
     */
    public java.lang.String getIdGroupement() {
        return idGroupement;
    }

    public java.lang.String getIdOperation() {
        return idOperation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 13:19:46)
     * 
     * @return globaz.osiris.db.comptes.CAGroupement
     */
    public CAOperation getOperation() {
        // Si pas déjà chargé
        if (operation == null) {
            operation = new CAOperation();
            operation.setSession(getSession());
            operation.setIdOperation(getIdOperation());
            try {
                operation.retrieve();
                if (operation.isNew() || operation.hasErrors()) {
                    operation = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                operation = null;
            }
        }

        return operation;
    }

    /**
     * Setter
     */
    public void setIdGroupement(java.lang.String newIdGroupement) {
        idGroupement = newIdGroupement;
        groupement = null;
    }

    public void setIdOperation(java.lang.String newIdOperation) {
        idOperation = newIdOperation;
    }
}
