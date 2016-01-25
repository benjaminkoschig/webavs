package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import java.io.Serializable;

public class AFReviseur extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // DB
    // Primary Key
    private java.lang.String idReviseur = new String();
    private java.lang.String idTiers = new String();
    private java.lang.String nomReviseur = new String();
    private java.lang.String typeReviseur = new String();
    private java.lang.String visa = new String();

    /**
     * Constructeur de AFControleEmployeur.
     */
    public AFReviseur() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setIdReviseur(this._incCounter(transaction, "0"));
        AFReviseurManager manager = new AFReviseurManager();
        manager.setSession(getSession());
        manager.setForVisa(getVisa());
        try {
            manager.find();
            if (manager.size() > 0) {
                _addError(transaction, "Le visa : " + getVisa() + " existe déjà !");
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFREVIP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idReviseur = statement.dbReadNumeric("MIIREV");
        idTiers = statement.dbReadNumeric("MIITIE");
        visa = statement.dbReadString("MILVIS");
        nomReviseur = statement.dbReadString("MILNOM");
        typeReviseur = statement.dbReadNumeric("MITTYR");

    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (getVisa().equals(null) || getVisa().equals("")) {
            _addError(statement.getTransaction(), "Le visa doit être saisie");
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MIIREV", this._dbWriteNumeric(statement.getTransaction(), getIdReviseur(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MIIREV", this._dbWriteNumeric(statement.getTransaction(), getIdReviseur(), "idReviseur"));
        statement.writeField("MIITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MILVIS", this._dbWriteString(statement.getTransaction(), getVisa(), "visa"));
        statement
                .writeField("MILNOM", this._dbWriteString(statement.getTransaction(), getNomReviseur(), "nomReviseur"));
        statement.writeField("MITTYR",
                this._dbWriteNumeric(statement.getTransaction(), getTypeReviseur(), "typeReviseur"));

    }

    /**
     * @return
     */
    public java.lang.String getIdReviseur() {
        return idReviseur;
    }

    /**
     * @return
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public java.lang.String getNomReviseur() {
        return nomReviseur;
    }

    public java.lang.String getTypeReviseur() {
        return typeReviseur;
    }

    /**
     * @return
     */
    public java.lang.String getVisa() {
        return visa;
    }

    /**
     * @param string
     */
    public void setIdReviseur(java.lang.String string) {
        idReviseur = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(java.lang.String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setNomReviseur(java.lang.String string) {
        nomReviseur = string;
    }

    public void setTypeReviseur(java.lang.String typeReviseur) {
        this.typeReviseur = typeReviseur;
    }

    /**
     * @param string
     */
    public void setVisa(java.lang.String string) {
        visa = string;
    }

}
